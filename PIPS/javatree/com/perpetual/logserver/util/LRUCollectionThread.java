/*
 * URLCollectionThread.java
 *
 * Created on November 9, 2003, 7:32 PM
 */

package com.perpetual.logserver.util;

import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class LRUCollectionThread extends Thread
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(LRUCollectionThread.class);
    private Cache lCache=null;
    /** Creates a new instance of URLCollectionThread */
    public LRUCollectionThread(Cache pCache) 
    {
        lCache=pCache;
    }    
    public void run()
    {
        try
		{
			while (true)
			{
				synchronized (this)
				{
					wait(Constants.CACHE_LRU_COLLECTION_INTERVAL);					
				}
                                sLog.debug("Cleaning up LRUs in LogStore cache !");
                                lCache.cleanCacheInBatch();
                        }
		}
		catch (InterruptedException ex)
		{
			sLog.debug("Cache LRU gc thread interrupted");
                        ex.printStackTrace();
		}
		catch (Throwable ex)
		{
			sLog.error("Error in  LRU gc thread", ex);
                        ex.printStackTrace();
		}
    }
}
