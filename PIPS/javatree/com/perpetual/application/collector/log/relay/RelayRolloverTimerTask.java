package com.perpetual.application.collector.log.relay;

import java.io.File;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.log.util.BaseRolloverTimerTask;
import com.perpetual.application.collector.log.util.RelayLogFile;
import com.perpetual.util.GMTFormatter;


public class RelayRolloverTimerTask extends BaseRolloverTimerTask {

	private static final Logger LOGGER = Logger.getLogger(RelayRolloverTimerTask.class);
	
	private GMTFormatter formatter = null;

	public RelayRolloverTimerTask ()
	{
		super();
		// needs reference to logStoreManager so it can suspend/release logging
		this.formatter = new GMTFormatter();
	}
	
	protected void moveCurrentLogs(File currentLogDir, File archiveLogDir,
			 long endTimeStamp) {
		
		File[] files = currentLogDir.listFiles();
		
		for (int i = 0; files != null && i < files.length; i++) {

			// file should be host_TS_relay.log
			File destDir = new File(archiveLogDir.getAbsolutePath()
					+ File.separator + getHostName(files[i].getName()));
			
			// get timestamp from host_TS.log 
			String startTimestampString = RelayLogFile
					.deriveStartTimeStampFromCurrentRelayLogFileName(files[i].getName());
			
			long startTimestamp = this.formatter
					.convertToMsFromEpoch(startTimestampString);		 
					
			if (!destDir.exists()) {
				destDir.mkdir();		
			}
			
			// rename all log files with proper timestamps
			// <LS>/current/hostid_TS_relay.log --> <LS>/archive/hostid/hostid_TS1_TS2_relay.log 
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
		String hostName = RelayLogFile.deriveHostFromRelayLogFileName(fileName);	
	
		result = RelayLogFile.constructRelayLogFileName(hostName,
								startTimeStamp, endTimeStamp);
	
		return result;
	}
	
	private String getHostName (String fileName) {
		String result = null;
	
		result = RelayLogFile.deriveHostFromRelayLogFileName(fileName);	
	
		return result;
	}

}