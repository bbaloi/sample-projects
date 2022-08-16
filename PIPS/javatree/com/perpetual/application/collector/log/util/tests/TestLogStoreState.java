/*
 * Sherlock syslog collector.
 * by Perpetual IP Systems GmbH
 * Copyright (c) 2003 Perpetual IP Systems GmbH
 * Web: www.perpetual-ip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 *     
 * NO WARRANTY  
 * -----------  
 * THIS SOFTWARE IS PROVIDED "AS-IS"  WITH ABSOLUTELY NO WARRANTY OF  
 * ANY KIND, EITHER IMPLIED OR EXPRESSED, INCLUDING, BUT NOT LIMITED  
 * TO, THE IMPLIED WARRANTIES OF FITNESS FOR ANY PURPOSE
 * AND MERCHANTABILITY.  
 *
 *    
 * NO LIABILITY  
 * ------------  
 * THE AUTHORS ASSUME ABSOLUTELY NO RESPONSIBILITY FOR ANY  
 * DAMAGES THAT MAY RESULT FROM THE USE,  
 * MODIFICATION OR REDISTRIBUTION OF THIS SOFTWARE, INCLUDING,  
 * BUT NOT LIMITED TO, LOSS OF DATA, CORRUPTION OR DAMAGE TO DATA,  
 * LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF SAVINGS,  
 * BUSINESS INTERRUPTION OR FAILURE TO INTEROPERATE WITH  
 * OTHER SOFTWARE, EVEN IF SUCH DAMAGES ARE FORESEEABLE.  
 *
 */

package com.perpetual.application.collector.log.util.tests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.util.BaseLogStoreManager;
import com.perpetual.application.collector.log.util.BaseLogWriter;
import com.perpetual.application.collector.log.util.BaseRolloverTimerTask;
import com.perpetual.application.collector.log.util.LogStoreState;
import com.perpetual.util.FileSystemUtilities;

public class TestLogStoreState extends TestCase {

	private class RolloverTimerTask extends BaseRolloverTimerTask
	{
		public void moveCurrentLogs (File source, File dest, long timeStamp)
		{
		}
	}

	private class LogStoreManager extends BaseLogStoreManager
	{
		public LogStoreManager (String root, long rolloverTimerInterval)
		{
			super(root, 0, Thread.NORM_PRIORITY, new RolloverTimerTask(), "label");	
		}
		
		public BaseLogWriter fetchWriterFromCache(String key) {
			return null;
		}
		
		public Map getWriterCache() {
			return new HashMap();
		}
	}

	private String tempDir = System.getProperty("java.io.tmpdir");
	private File logDir;
	private LogStoreManager logStore = null;

	/**
	 * Constructor for TestLogStoreManager.
	 * @param arg0
	 */
	public TestLogStoreState(String arg0) {
		super(arg0);
	}
	
	public void setUp () throws Exception
	{
		this.logDir = new File(tempDir + File.separator + "logstore");
		this.logDir.mkdir();
		
		System.out.println("Created " + this.logDir.getAbsolutePath());
	}

	public void tearDown () throws Exception
	{	
		if (this.logStore != null) {
			this.logStore.shutdown();
		}
		
		FileSystemUtilities.deleteRecursively(this.logDir);
	}
	
	public void testSaveAndLoad() throws Exception {
		
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), (long) 0);
		this.logStore.initialize();
		
		long startTime = System.currentTimeMillis();
		long lastRolloverTimeStamp = -1;
		long rolloverInterval = 1;
		
		LogStoreState state = new LogStoreState(this.logStore, startTime, 
			lastRolloverTimeStamp, rolloverInterval);

		state.save();
		
		File stateFile = state.getStateFile();
		assertTrue("state file exists", stateFile.exists());
		LogStoreState newState = new LogStoreState(this.logStore);
		assertTrue("state exists", newState.exists());
		
		newState.load();
		
		// check values
		
		assertEquals("rollover is correct", rolloverInterval, 
				newState.getRolloverInterval());
		assertEquals("startTime is correct", startTime, 
						newState.getStartTimeStamp());
	}
}
