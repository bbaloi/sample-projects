package com.perpetual.application.collector.log.relay;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.action.forward.ForwardHost;
import com.perpetual.application.collector.action.forward.ForwardedSyslogMessage;
import com.perpetual.application.collector.log.util.BaseLogStoreManager;
import com.perpetual.application.collector.log.util.BaseLogWriter;
import com.perpetual.application.collector.log.util.LogStoreManagerException;
import com.perpetual.application.collector.message.SyslogMessage;


public class RelayLogStoreManager extends BaseLogStoreManager {

	private static final Logger LOGGER = Logger.getLogger(RelayLogStoreManager.class);

	private Map logWriterCache = null;

	public RelayLogStoreManager (String logStoreRoot, long rolloverInterval,
			int writerThreadPriority )
	{
		super(logStoreRoot, rolloverInterval, writerThreadPriority, 
				new RelayRolloverTimerTask(), "RELAY");
				
		this.logWriterCache = Collections.synchronizedMap(new HashMap());
	}	
	
	public RelayLogStoreManager (String logStoreRoot, long rolloverInterval)
	{
		this(logStoreRoot, rolloverInterval, Thread.NORM_PRIORITY);
	}

	protected BaseLogWriter fetchWriterFromCache (String key)
	{
		RelayLogWriter logWriter = null;
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("Fetching log writer from cache for key " + key);
		}
		
		synchronized (this.logWriterCache) {
			logWriter = (RelayLogWriter) this.logWriterCache.get(key);
			
			if (logWriter == null) {
				// start up a log writer and add to cache
				logWriter = new RelayLogWriter(this, key);
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
	
	// can be called by multiple ForwardQueueProcessor threads
	// need to make sure forwarded messages go into queue
	// in timestamp order
	public synchronized void logForwardedMessage (ForwardHost forwardHost, 
			SyslogMessage message)
		throws LogStoreManagerException
	{
		ForwardedSyslogMessage forwardedMessage = new ForwardedSyslogMessage(forwardHost,
				System.currentTimeMillis(), message);
		
		logMessage(forwardedMessage);
	}
}
