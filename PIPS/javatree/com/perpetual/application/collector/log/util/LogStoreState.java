package com.perpetual.application.collector.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LogStoreState {
	
	private final static String STATE_FILE = "state.properties";
	
	private BaseLogStoreManager logStore = null;
	private long startTimeStamp = -1;
	private long rolloverInterval = -1;
	private long lastRolloverTimeStamp = -1;
	private File stateFile = null;

	public LogStoreState (BaseLogStoreManager logStore) 
	{
		this.logStore = logStore;
		this.startTimeStamp = -1;
		this.rolloverInterval = -1;
		this.lastRolloverTimeStamp = -1;
		this.stateFile = new File(logStore.getLogRootDirectory().getAbsolutePath()
				+ File.separator
				+ STATE_FILE );	
	}
	
	public LogStoreState (BaseLogStoreManager logStore,
		 long startTimeStamp, long lastRolloverTimeStamp, 
		 long rolloverInterval)
	{
		this(logStore);
		
		this.startTimeStamp = startTimeStamp;
		this.lastRolloverTimeStamp = lastRolloverTimeStamp;
		this.rolloverInterval = rolloverInterval;
	}
	
	public void save () throws FileNotFoundException, IOException
	{
		Properties properties = new Properties();
		
		properties.setProperty("startTimeStamp",
			(new Long(this.startTimeStamp)).toString());
		properties.setProperty("rolloverInterval",
			(new Long(this.rolloverInterval)).toString());
		properties.setProperty("lastRolloverTimeStamp",
				(new Long(this.lastRolloverTimeStamp)).toString());
				
		OutputStream os = new FileOutputStream(this.stateFile);
		
		properties.store(os, "Log Store State");
		os.close();			
	}
	
	public void load () throws FileNotFoundException, IOException
	{
		Properties properties = new Properties();
		
		properties.setProperty("startTimestamp",
			(new Long(this.startTimeStamp)).toString());
		properties.setProperty("rolloverInterval",
			(new Long(this.rolloverInterval)).toString());
		properties.setProperty("rolloverRolloverTimeStamp",
					(new Long(this.lastRolloverTimeStamp)).toString());
		
		
		InputStream is = new FileInputStream(this.stateFile);
		
		properties.load(is);
		is.close();
		
		try {
			this.rolloverInterval = (new Long(properties
					.getProperty("rolloverInterval"))).longValue();
					
			this.startTimeStamp = (new Long(properties
					.getProperty("startTimeStamp"))).longValue();	
			
			this.lastRolloverTimeStamp = (new Long(properties
							.getProperty("lastRolloverTimeStamp"))).longValue();	
		} catch (NumberFormatException e) {
			// state file is corrupt - cannot safely continue
			throw new IOException("state file is corrupt");
		}
			
	}
	
	public boolean exists () {
		return this.stateFile.exists();
	}

	public long getRolloverInterval() {
		return rolloverInterval;
	}

	public long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setRolloverInterval(long l) {
		rolloverInterval = l;
	}

	public void setStartTimeStamp(long l) {
		startTimeStamp = l;
	}

	public File getStateFile() {
		return stateFile;
	}

	public long getLastRolloverTimeStamp() {
		return lastRolloverTimeStamp;
	}

	public void setLastRolloverTimeStamp(long l) {
		lastRolloverTimeStamp = l;
	}

}
