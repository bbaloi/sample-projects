/*
 * Cache.java
 *
 * Created on November 7, 2003, 3:29 PM
 */

package com.perpetual.logserver.util;

import java.util.LinkedHashMap;
import java.util.Map;
import com.perpetual.util.PerpetualC2Logger;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;  
import java.lang.Math;
import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */
public class Cache implements java.io.Serializable
{
    
       private PerpetualC2Logger sLog = new PerpetualC2Logger( Cache.class );
       
        private Date m_date;
	private int m_maxEntries;
	private LinkedHashMap cache = null;
        private LinkedList    cacheEntryList = null;
        private List  garbageList = null;
	private long m_lastAddTime;
        private int percentBatchLRU;
        private LRUCollectionThread lLRUCollector=null;
        private int openFiles=0;
        
    /** Creates a new instance of Cache */
    
        public Cache(int maxEntries)
	{
//		sLog.debug("Cache max entries " + maxEntries);
		m_maxEntries = maxEntries;
                percentBatchLRU=20;
                init();
                
	}        
        public Cache(int maxEntries,int pPercentBatchLRU)
	{
//		sLog.debug("Cache max entries " + maxEntries);

		m_maxEntries = maxEntries;
                percentBatchLRU=pPercentBatchLRU;
                init();
               
	}
        private void init()
        {
            cache = new LinkedHashMap(m_maxEntries);
            cacheEntryList = new LinkedList();
            garbageList = new ArrayList();            
        }
        public void startLRU_gc()
        {
            lLRUCollector  = new LRUCollectionThread(this);
            lLRUCollector.start();
        }
        public Date getDate() { return m_date; }

	public int getItemCount()
	{
		return cache.size();
	}
        public boolean contains(Object Key)
        {
            if(cache.containsValue(Key))
                return true;
            else 
                return false;
        }
        public synchronized void add(CacheItem pItem) throws Exception
        {
            pItem.instantiate();//open file  
            openFiles++;          
            sLog.debug("Currently there are "+openFiles+" open files !");
            if(cache.size()>=m_maxEntries)
            {
                sLog.debug("Cache is full - will need to remove LRU object");
                removeLRU();
                cache.put(pItem.getKey(),pItem);
                cacheEntryList.add(pItem.getKey());//add at end of list                
            }
            else
            {
                cache.put(pItem.getKey(),pItem);
                cacheEntryList.add(pItem.getKey());//add at end of list
            }
        }
        public synchronized CacheItem get(Object key)
        {
            if(cache.containsValue(key))
            {
                //take this object and move to top of list - always the one at the end of the list is the LRU
                int index = cacheEntryList.indexOf(key);
                sLog.debug("Updating MRU/LRU list - item #"+index+" - "+(String)key);
                Object chosenOne  = cacheEntryList.remove(index);                
                cacheEntryList.add(chosenOne);
                return (CacheItem) cache.get(key);
            }
             return null;
        }
        private void removeLRU()
        {
            sLog.debug("Removing LRU");
            //determine which  is the oldest element in the cache & move it to the garbage collection list
            String lastElement = (String) cacheEntryList.removeFirst(); 
            CacheItem item = (CacheItem) cache.get(lastElement); 
            //if(!item.inUse())
            cache.remove(lastElement); 
            if(garbageList.size()>=m_maxEntries)
                cleanGarbage();
            garbageList.add(item);              
        }
        public synchronized void cleanGarbage()
        {
            sLog.debug("+++Cleaning garbage !+++");
            Iterator it = garbageList.iterator();
            while(it.hasNext())
            {
                CacheItem item = (CacheItem) it.next();
                if(item!=null)
                {
                    item.destroy();
                    openFiles--;
                }
                 sLog.debug("Currently there are "+openFiles+" open files !");
            }
            garbageList.clear();
        }
        public void cleanCacheInBatch()
        {
            int num = getPercentRecords();
            sLog.debug("Cleaning "+ num+"LRU records from cache");  
            if(cacheEntryList.size()>0)
            {                
                for(int i=0;i<num;i++)
                {
                    String key = (String) cacheEntryList.get(i);
                    CacheItem item = (CacheItem) cache.remove(key);
                    garbageList.add(item);                
                }            
                for(int x=0;x<num;x++)
                {
                  cacheEntryList.remove(x);
                }
            }
            
            //sLog.debug("Cleaning garbage");
            //cleanGarbage();            
        }
        public int getPercentRecords()
        {
            double maxSize = new Double(m_maxEntries).doubleValue();
            double lruSize = new Double(percentBatchLRU).doubleValue();
            double value = (maxSize*lruSize)/100;
            sLog.debug("double value percent lru:"+value);
            double percentValue =  Math.ceil(value);
            sLog.debug("percent batch lru:"+percentValue);

            return new Double(percentValue).intValue();       
        }
        public void close() throws Exception
        {
            sLog.debug("Closing & cleaning up cache !");
            
            Iterator it = cacheEntryList.iterator();
            while(it.hasNext())
            {
                String key = (String) it.next();
                CacheItem item = (CacheItem) cache.get(key);
                item.destroy();                
            }
            garbageList.clear();
            cacheEntryList.clear();
            cache.clear();
        }
}
