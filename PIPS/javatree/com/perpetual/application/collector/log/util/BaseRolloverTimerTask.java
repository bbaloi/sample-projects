package com.perpetual.application.collector.log.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public abstract class BaseRolloverTimerTask extends TimerTask {

	private static final Logger LOGGER = Logger.getLogger(BaseRolloverTimerTask.class);
	
	private BaseLogStoreManager logStoreManager = null;

	public BaseRolloverTimerTask ()
	{
	}

	public void initialize (BaseLogStoreManager logStoreManager)
	{
		// needs reference to logStoreManager so it can suspend/release logging
		this.logStoreManager = logStoreManager;
	}

	public void run() {
		
		LOGGER.info("Starting log rollover for logstore "
				+ this.logStoreManager.getLogRootDirectory().getAbsolutePath());
			
		if (this.logStoreManager != null) {
			LOGGER.debug("Suspending operation of log writers");
			this.logStoreManager.suspendOperation(true);
		}
		
		LogStoreState logState = this.logStoreManager.getLogState();
		
		// TBD - this may have to change - cos we've got queued messages
		long currentTimeStamp = System.currentTimeMillis();
		long startTimeStamp = logState.getLastRolloverTimeStamp();
		
		if (startTimeStamp == -1) {
			startTimeStamp = logState.getStartTimeStamp();
		}
		
		// rename "current" directory
				
		File currentLogDir = this.logStoreManager.getCurrentLogDirectory();
		File archiveLogDir = this.logStoreManager.getArchiveLogDirectory();
		
		// for now just rename all the file into the archive directory

		moveCurrentLogs(currentLogDir, archiveLogDir, currentTimeStamp);
		
		logState.setLastRolloverTimeStamp(currentTimeStamp);
		
		try {	
			logState.save();
		} catch (FileNotFoundException e) {
			LOGGER.error("cannot perform rollover: " + e);
		} catch (IOException e) {
			LOGGER.error("cannot perform rollover: " + e);
		}
		
		if (this.logStoreManager != null) {
			LOGGER.debug("Resuming operation of log writers");
			this.logStoreManager.resumeOperation();
		}
		
	}

	protected abstract void moveCurrentLogs(File currentLogDir, File archiveLogDir,
			 long endTimeStamp);
}