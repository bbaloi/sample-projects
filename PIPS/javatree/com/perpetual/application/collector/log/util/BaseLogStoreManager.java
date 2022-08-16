package com.perpetual.application.collector.log.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;



public abstract class BaseLogStoreManager {

	private static final Logger LOGGER = Logger.getLogger(BaseLogStoreManager.class);

	private LogStoreState logState = null;
	private Timer rolloverTimer = null;
	private BaseRolloverTimerTask rolloverTimerTask = null;
	protected String logStoreRoot = null;
	private long rolloverInterval;
	private boolean initialized = false;
	
	private Object suspendedMutex = new Object();
	private boolean suspended = false;
	private boolean stopped = false;
	
	private Object numberOfWritingThreadsMutex = new Object();
	private int numberOfWritingThreads = 0 ;
	private int writerThreadPriority;

	protected File logRootDirectory = null;	
	protected File currentLogDirectory = null;
	protected File archiveLogDirectory = null;

	public BaseLogStoreManager (String logStoreRoot, long rolloverInterval,
			int writerThreadPriority, BaseRolloverTimerTask rolloverTimerTask,
			String label)
	{
		this.logStoreRoot = logStoreRoot;
		this.rolloverInterval = rolloverInterval;
		this.rolloverTimerTask = rolloverTimerTask;
		this.writerThreadPriority = writerThreadPriority;
		
		LOGGER.info("Creating " + label + " logstore: \n"
			+ "  root = " + this.logStoreRoot + "\n"
			+ "  rollover interval = " + this.rolloverInterval + "\n"
			+ "  writer thread priority = " + this.writerThreadPriority);	
	}	
	
	public BaseLogStoreManager (String logStoreRoot, long rolloverInterval,
			BaseRolloverTimerTask rolloverTimerTask,  String label)
	{
		this(logStoreRoot, rolloverInterval, Thread.NORM_PRIORITY,
				rolloverTimerTask, label);
	}

	public void initialize () throws LogStoreManagerException
	{
		this.rolloverTimerTask.initialize(this);
		
		// create directories
		createLogDirectories();
		
		// check if this is a restart - there should be persisted state
		if (restarted()) {
			// do a rollover immediately to clean up files
			// and start from fresh
			LOGGER.info("Collector was restarted, doing an immediate rollover.");
			this.rolloverTimerTask.run();
		} else {
			LOGGER.info("Collector is starting with a new logstore.");
			initializeLogStoreState();
		}

		// start rollover timer
		startRolloverTimer();
		this.initialized = true;
	}

	protected void createLogDirectories() throws LogStoreManagerException {		
		this.logRootDirectory = new File(this.logStoreRoot);
	
	
		LOGGER.info("Initializing logstore under " + this.logStoreRoot);
			
		// make sure it's a directory
		
		if (!this.logRootDirectory.isDirectory()) {
			if (!this.logRootDirectory.mkdir()) {
				LOGGER.fatal("cannot create root log directory " + this.logStoreRoot);
				throw new LogStoreManagerException(this.logStoreRoot + " is not a directory.");
			}
		}
		
		this.currentLogDirectory = new File(this.logStoreRoot + File.separator +
				LogStoreConstants.ACCUMULATION_ROOT);
		
		if (this.currentLogDirectory.exists()) {
			if (!this.currentLogDirectory.isDirectory()) {
				throw new LogStoreManagerException(
					this.currentLogDirectory.getAbsolutePath()
					 + " is not a directory.");
			}
		}
		else {
			if (!this.currentLogDirectory.mkdir()) {
				throw new LogStoreManagerException("could not create directory "
						+ this.currentLogDirectory.getAbsolutePath());
			}
		}
		
		this.archiveLogDirectory = new File(this.logStoreRoot + File.separator +
				LogStoreConstants.ARCHIVE_ROOT);
		
		if (this.archiveLogDirectory.exists()) {
			if (!this.archiveLogDirectory.isDirectory()) {
				throw new LogStoreManagerException(
					this.archiveLogDirectory.getAbsolutePath()
					+ " is not a directory.");
			}
		}
		else {
			if (!this.archiveLogDirectory.mkdir()) {
				throw new LogStoreManagerException("could not create directory "
						+ this.archiveLogDirectory.getAbsolutePath());
			}
		}
	}
	
	private void initializeLogStoreState () throws LogStoreManagerException {
		
		long timeStamp = System.currentTimeMillis();
		
		this.logState = new LogStoreState(this, timeStamp, -1, this.rolloverInterval);
		
		// in future we'll want to check for existing state and do something,
		// like a rollover if necessary
		// but for now just store state and move on
		
		try {
			this.logState.save();
		} catch (Exception e) {
			throw new LogStoreManagerException(e);
		}
	}
	
	private boolean restarted () throws LogStoreManagerException
	{
		boolean restarted = false;
		
		this.logState = new LogStoreState(this);
		
		// try to load existing state, if there's a FileNotFound exception
		// we don't have a restart
		try {
			this.logState.load();
			restarted = true;
		} catch (FileNotFoundException e) {
			restarted = false;
		} catch (IOException e) {
			throw new LogStoreManagerException(e);
		}
		
		return restarted;
	}

	private void startRolloverTimer() {
		
		LOGGER.info("Starting Rollover timer for logstore under " + this.logStoreRoot);
		
		if (this.rolloverInterval > 0) {			
			this.rolloverTimer = new Timer();
			this.rolloverTimer.scheduleAtFixedRate(this.rolloverTimerTask,
				this.rolloverInterval,
				this.rolloverInterval);
		}
	}
	
	public void logMessage (ILoggableMessage message) throws LogStoreManagerException
	{
		if (!this.initialized) {
			initialize();
		}
		
		// do we need this?
		if (this.stopped) {
			throw new LogStoreManagerException(
				"attempting to log to a stopped LogManager");
		}
			
		
		waitIfSuspended();
		
		writeToLog(message);
	}

	private void waitIfSuspended() {
		try {
			synchronized (this.suspendedMutex) {
				if (this.suspended) {  // i.e. suspend == true
					this.suspendedMutex.wait();
				}
			}
				
		} catch (InterruptedException e) {
		}
	}
	
	public void suspendOperation (boolean resetCurrentLogs) 
	{
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("Suspending operation of logstore under " + this.logStoreRoot);
		}
		
		synchronized (this.suspendedMutex) {
			this.suspended = true;
		}
		// should wait for any writing threads to finish
		suspendAllWriters(resetCurrentLogs);
	}

	private void suspendAllWriters (boolean resetCurrentLogs)
	{
		for (Iterator i = getWriterCache().keySet().iterator(); i.hasNext(); ) {
			BaseLogWriter logWriter = (BaseLogWriter)
				getWriterCache().get((String) i.next());
			
			logWriter.suspendWriting(resetCurrentLogs);
		}
	}
	
	public void resumeOperation ()
	{
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("Resuming operation of logstore under " + this.logStoreRoot);
		}
		
		synchronized (this.suspendedMutex) {
			this.suspended = false;
			this.suspendedMutex.notifyAll();
		}
		resumeAllWriters();
		
	}

	private void resumeAllWriters ()
	{
		for (Iterator i = getWriterCache().keySet().iterator(); i.hasNext(); )
		{
			BaseLogWriter logWriter = (BaseLogWriter)
					getWriterCache().get((String) i.next());
			logWriter.resumeWriting();
		}
	}

	public void shutdown () {
		
		LOGGER.info("Shutting down Rollover timer for logstore under "
				 + this.logStoreRoot);

		this.rolloverTimer = null;
		shutdownAllWriters();		
		this.stopped = true;

	}
	
	private void shutdownAllWriters ()
	{
		for (Iterator i = getWriterCache().keySet().iterator(); i.hasNext(); )
		{
			BaseLogWriter logWriter = (BaseLogWriter)
					getWriterCache().get((String) i.next());	
			logWriter.shutdown();
		}
	}


	public File getLogRootDirectory() {
		return logRootDirectory;
	}

	public File getArchiveLogDirectory() {
		return archiveLogDirectory;
	}

	public File getCurrentLogDirectory() {
		return currentLogDirectory;
	}
	
	private void writeToLog (ILoggableMessage message) throws LogStoreManagerException
	{	
		BaseLogWriter logWriter = fetchWriterFromCache(message.getHost());
	
		logWriter.addToWriteQueue(message);
	}
	
	
	public LogStoreState getLogState() {
		return logState;
	}
	
	protected abstract BaseLogWriter fetchWriterFromCache (String key);
	protected abstract Map getWriterCache();

	/**
	 * @return
	 */
	public int getWriterThreadPriority() {
		return writerThreadPriority;
	}

}
