package com.perpetual.application.collector.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.log.util.BaseLogStoreManager;
import com.perpetual.application.collector.log.util.BaseLogWriter;


public class LogStoreManager extends BaseLogStoreManager {

	private static final Logger LOGGER = Logger.getLogger(LogStoreManager.class);

	private Map logWriterCache = null;

	public LogStoreManager (String logStoreRoot, long rolloverInterval,
			int writerThreadPriority )
	{
		super(logStoreRoot, rolloverInterval, writerThreadPriority, 
				new RolloverTimerTask(), "MAIN");
				
		this.logWriterCache = Collections.synchronizedMap(new HashMap());
	}	
	
	public LogStoreManager (String logStoreRoot, long rolloverInterval)
	{
		this(logStoreRoot, rolloverInterval, Thread.NORM_PRIORITY);
	}

	protected BaseLogWriter fetchWriterFromCache (String key)
	{
		LogWriter logWriter = null;
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("Fetching log writer from cache for key " + key);
		}
		
		synchronized (this.logWriterCache) {
			logWriter = (LogWriter) this.logWriterCache.get(key);
			
			if (logWriter == null) {
				// start up a log writer and add to cache
				logWriter = new LogWriter(this, key);
				logWriter.start();
			}
			
			this.logWriterCache.put(key, logWriter);
			
			this.logWriterCache.notifyAll();
		}
		
		return logWriter;
	}

	public Map getWriterCache() {
		return this.logWriterCache;
	}
}
