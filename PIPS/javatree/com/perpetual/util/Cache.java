package com.perpetual.util;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.log4j.Logger;


/**
	Suave Cache system.
	The workflow is like follows:
	<pre>
		cache = new Cache(n);
		item = new FileCacheItem(file);		// FileCacheItem implements Cache.Item
		cache.add(file, item, true);		// item is initially provided in a frozen state
		...
		item = (FileCacheItem)cache.checkOut(file);
		try
		{
			RandomAccessFile raf = (RandomAccessFile)item.getRandomAccessFile();
			// do stuff with raf here
		}
		finally
		{
			cache.checkIn(file);
		}
		...
		cache.remove(file);
	</pre>
*/
public class Cache
{
        private static Logger sLog = Logger.getLogger(Cache.class);
    
        private Date m_date;
	private int m_maxEntries;
	private Map m_frozenMap = new HashMap(), m_thawedMap = new LinkedHashMap(), m_checkedOutMap = new HashMap();
	private long m_lastAddTime;

	
        
	public Cache(int maxEntries)
	{
//		sLog.debug("Cache max entries " + maxEntries);

		m_maxEntries = maxEntries;
	}

	public Date getDate() { return m_date; }

	public int getItemCount()
	{
		return m_frozenMap.size() + m_thawedMap.size();
	}
	public int getThawedItemCount()
	{
		return m_thawedMap.size();
	}

	public void anticipatedAdd() throws Exception
	{
		m_lastAddTime = System.currentTimeMillis();
	}

	/**
	Add item to cache.
	@param key Reference to address entry by, eg, a 'File'.
	@param item An object implementing the Cache.Item interface which has the ability to take 'thaw' and 'freeze' commands as the Cache sees fit.
	@param isFrozen Specifies if item is frozen or not.  It does not actually thaw or freeze the item, it wants to know the condition the item is in now.
	*/
	public synchronized void add(Object key, Item item, boolean isFrozen) throws Exception
	{
		int n = m_frozenMap.size() + m_thawedMap.size();
		{
			// Check to see if we can predict that the user forgot to remove when he was done the last time.
			//
			long time = System.currentTimeMillis();
			if (m_lastAddTime != 0)
			{
				long elapsed = time - m_lastAddTime;
				if (elapsed > 5000 && n > 0)
					sLog.debug("Cache thinks that the programmer forgot to remove items from the cache, because roughly " + (elapsed + 500) / 1000 + " seconds went by, and during an add, there were already " + n + " items in the cache, while normally, after an elapsed time like this the cache would be empty.");
			}
			m_lastAddTime = time;
		}

		if (isFrozen)
		{
//			sLog.debug("Cache adding as frozen " + key);
			m_frozenMap.put(key, item);
		}
		else
		{
//			sLog.debug("Cache adding as thawed " + key);
			m_thawedMap.put(key, item);
		}
//		sLog.debug("Cache now holds " + (n + 1) + " items");
	}

	/**
	Remove an item from the cache.
	@param key Reference to the item to be removed.
	@param freeze Specifies if the item should be frozen or thawed before removal.
	*/
	public synchronized void remove(Object key, boolean freeze) throws Exception
	{
//		sLog.debug("Cache removing " + key);

		Item item = (Item)m_checkedOutMap.get(key);	// not removing yet !
		if (item != null)
			throw new Exception("Cache item not checked back in: " + key);

		item = (Item)m_thawedMap.get(key);	// not removing yet !
		if (item != null)
		{
			if (freeze)
			{
//				sLog.debug("Cache freezing " + key);
				item.freeze();
			}
			m_thawedMap.remove(key);		// now removed !
		}
		else
		{
			item = (Item)m_frozenMap.get(key);	// not removing yet !
			if (item == null)
				throw new Exception("Cache item does not exist: " + key);

			if (!freeze)
			{
//				sLog.debug("Cache thawing " + key);
                                try
                                {
                                    item.thaw();
                                }
                                catch(Exception excp)
                                {
                                     sLog.error("Couldn't thaw file; this means that the cache has the File pointer open but the Collector must have rolled the file over to archive - could not open non-existnet file !!!");
                                     m_thawedMap.remove(key);
                          
                                }
			}
			m_frozenMap.remove(key);	// removed !
		}

//		sLog.debug("Cache now holds " + (m_frozenMap.size() + m_thawedMap.size()) + " items");
	}

	/**
	Retrieves an item from the cache.  Once checked out, it's thawed, and locked, so it won't get frozen again, until it is checked back in.
	*/
	public synchronized Item checkOut(Object key) throws Exception
	{
//		sLog.debug("Cache checking out " + key);

		Item item = (Item)m_checkedOutMap.get(key);
		if (item != null)
			throw new Exception("Cache item already checked out: " + key);

		item = (Item)m_thawedMap.remove(key);		// removed !
		if (item == null)
		{
//			sLog.debug("Cache thawing " + key);

			// item is frozen, thaw it
			//
			item = (Item)m_frozenMap.get(key);	// don't remove it yet, something could go wrong below still
			if (item == null)
				throw new Exception("Cache item does not exist: " + key);

//			sLog.debug("Cache size is " + m_thawedMap.size());

			if (m_thawedMap.size() >= m_maxEntries)
			{
				// max thawed items, freeze least popular one, that is for now, the eldest one
				//
				Iterator i = m_thawedMap.entrySet().iterator();
				Map.Entry entry = (Map.Entry)i.next();

//				sLog.debug("Cache freezing " + entry.getKey());

				Item itemToFreeze = (Item)entry.getValue();	// eldest item
				itemToFreeze.freeze();
				m_frozenMap.put(entry.getKey(), itemToFreeze);
				i.remove();
			}
                        try
                        {
                            item.thaw();
                        }
                        catch(Exception excp)
                        {
                            sLog.error("Couldn't thaw file; this means that the cache has the File pointer open but the Collector must have rolled the file over to archive - could not open non-existnet file !!!");
                            m_thawedMap.remove(key);
                            //m_checkedOutMap.remove(key);
                        }
                        
			m_frozenMap.remove(key);
		}

		// careful, already removed from thawed map
		//
		m_checkedOutMap.put(key, item);

		return item;
	}

	/**
	Returns control of the item back to the Cache.  Once checked back in, the Cache has the option to freeze when it becomes necessary to 'make room' for other checkouts.
	*/
	public synchronized void checkIn(Object key) throws Exception
	{
//		sLog.debug("Cache checking in " + key);

		Item item = (Item)m_checkedOutMap.remove(key);		// removed !
		if (item == null)
			throw new Exception("Cache item not checked out: " + key);

		// must not allow errors, already removed from checked out map
		//
		m_thawedMap.put(key, item);
	}

	public static interface Item
	{
		void freeze() throws Exception;
		void thaw() throws Exception;
	}
	
}


