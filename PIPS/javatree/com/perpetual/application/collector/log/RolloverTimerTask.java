package com.perpetual.application.collector.log;

import java.io.File;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.log.util.BaseRolloverTimerTask;
import com.perpetual.application.collector.log.util.LogFile;
import com.perpetual.util.GMTFormatter;


public class RolloverTimerTask extends BaseRolloverTimerTask {

	private static final Logger LOGGER = Logger.getLogger(RolloverTimerTask.class);
	
	private GMTFormatter formatter = null;

	public RolloverTimerTask ()
	{
		super();
		// needs reference to logStoreManager so it can suspend/release logging
		this.formatter = new GMTFormatter();
	}
	
	protected void moveCurrentLogs(File currentLogDir, File archiveLogDir,
			 long endTimeStamp) {
		
		File[] files = currentLogDir.listFiles();
		
		for (int i = 0; files != null && i < files.length; i++) {

			// file should be host_TS.log or host_TS_raw.log
			File destDir = new File(archiveLogDir.getAbsolutePath()
					+ File.separator + getHostName(files[i].getName()));
			
			// get timestamp from host_TS.log 
			String startTimestampString = LogFile
					.deriveStartTimeStampFromCurrentLogFileName(files[i].getName());
			
			long startTimestamp = this.formatter
					.convertToMsFromEpoch(startTimestampString);		 
					
			if (!destDir.exists()) {
				destDir.mkdir();		
			}
			
			// rename all log files with proper timestamps
			// <LS>/current/hostid_TS.log --> <LS>/archive/hostid/hostid_TS1_TS2.log 
			File destFile = new File(destDir.getAbsoluteFile()
					+ File.separator
					+ constructLogFileName(files[i],
						startTimestamp, endTimeStamp));

			if (!files[i].renameTo(destFile)) {
				LOGGER.error(
						"cannot perform log rollover - cannot move "
						+ files[i].getAbsolutePath() + " to "
						+ destFile.getAbsolutePath());	
			}			
		}
	}

	private String constructLogFileName (File hostLogFile,
			long startTimeStamp, long endTimeStamp) {
		
		String result = null;
		
		String fileName = hostLogFile.getName();
		String hostName = LogFile.deriveHostFromLogFileName(fileName);	
		
		if (LogFile.isRawLogFile(fileName)) {	
			result = LogFile.constructRawLogFileName(hostName,
					startTimeStamp, endTimeStamp);
		}
		else {
			result = LogFile.constructAnnotatedLogFileName(hostName,
								startTimeStamp, endTimeStamp);
		}

		return result;
	}
	
	private String getHostName (String fileName) {
		String result = null;
	
		// TBD - for now just strip off the ".log" portion
	
		result = LogFile.deriveHostFromLogFileName(fileName);	
	
		return result;
	}

}