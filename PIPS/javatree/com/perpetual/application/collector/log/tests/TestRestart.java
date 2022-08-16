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

package com.perpetual.application.collector.log.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.log.util.LogFile;
import com.perpetual.application.collector.log.util.LogStoreState;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;


public class TestRestart extends TestCase {

	public final static int NUMBER_OF_HOSTS = 10;
	public final static int NUMBER_OF_FILES = 20;
	
	private File logDir;
	private String tempDir = System.getProperty("java.io.tmpdir");
	private LogStoreManager logStore = null;
	private GMTFormatter formatter = new GMTFormatter();
	
	private List hosts = new ArrayList();
	

	public TestRestart(String arg0) {
		super(arg0);
	}
	
	public void setUp() throws Exception {

		this.logDir = new File(tempDir + File.separator + "logstore");
			
		this.logDir.mkdir();
		
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), 0);
		this.logStore.initialize();
		
		//populate current with some files
		File current = this.logStore.getCurrentLogDirectory();
		
		for (int i = 0; i < NUMBER_OF_HOSTS; i++) {
			String hostName = "host" + i;
			String timeStamp = formatter.convertToGMT(System.currentTimeMillis());
			String fileName = hostName + "_" + timeStamp + ".log";
			String rawFileName = hostName + "_" + timeStamp +"_raw.log";
			
			File annotatedFile = new File(current.getAbsolutePath() 
					+ File.separator + fileName);

			File rawFile = new File(current.getAbsolutePath() 
								+ File.separator + rawFileName);


			annotatedFile.createNewFile();
			rawFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(new FileOutputStream(annotatedFile));
			
			writer.println(timeStamp + "|This is annotated syslog for host" + i);
			writer.close();
			
			PrintWriter writer2 = new PrintWriter(new FileOutputStream(rawFile));
			
			writer2.println(timeStamp + "|This is raw syslog for host" + i);
			writer2.close();

			hosts.add(hostName);
		}
				
		// populate archive with some files - TBD
		
	}
	
	public void tearDown() throws Exception {
		if (this.logStore != null) {
			this.logStore.shutdown();
		}
		
		FileSystemUtilities.deleteRecursively(this.logDir);
	}

	public void testRestart() throws Exception
	{

		// we should have a prepopulated logstore
		// simulate a restart
		this.logStore.shutdown();
		
		assertTrue("state file exists", 
				this.logStore.getLogState().getStateFile().exists());
		
		// now the following should do an immediate rollover
		this.logStore.initialize();
		
		// now check the results of the rollover
				
		File current = this.logStore.getCurrentLogDirectory();
		File archive = this.logStore.getArchiveLogDirectory();
		File[] archiveHostDirectories = archive.listFiles();
		List actualHosts = new ArrayList();
		
		assertEquals("no files under current", 0, current.listFiles().length);
		assertEquals("correct number of directories under archive",
				NUMBER_OF_HOSTS, archiveHostDirectories.length);
				
		for (int i = 0; i < archiveHostDirectories.length; i++) {
			assertTrue("archive host directories exists",
					archiveHostDirectories[i].isDirectory());
			
			File[] logFiles = archiveHostDirectories[i].listFiles();
			
			assertEquals("contains two log files", 2, logFiles.length);
			
			boolean rawLogFound = false;
			
			for (int j = 0; j < logFiles.length; j++) {
				
				File logFile = logFiles[j];
				
				if (LogFile.isRawLogFile(logFile.getName())) {
					rawLogFound = true;
				}
				// read first line to get timestamp
				BufferedReader reader = new BufferedReader(
							new FileReader(logFile));

				String textLine = reader.readLine();
			
				reader.close();
				
				String startTimestampString = textLine.substring(0, textLine.indexOf('|'));
					
				// name is now host-TS1--TS2.log
				// TS1 should available from logstate
				// TS2 should lastRolloverTimeStamp from logState
				
				LogStoreState logState = this.logStore.getLogState();
				String fileName = logFile.getName();
				String host = LogFile.deriveHostFromLogFileName(fileName);
			
				String ts1String = LogFile.deriveStartTimeStampFromLogFileName(fileName);
				String ts2String = LogFile.deriveEndTimeStampFromLogFileName(fileName);
				
				long ts1 = this.formatter.convertToMsFromEpoch(ts1String);
				long ts2 = this.formatter.convertToMsFromEpoch(ts2String);
				
				assertEquals("start timestamp is correct",
					startTimestampString, ts1String);
				assertEquals("end timestamp is correct",
					logState.getLastRolloverTimeStamp(), ts2);
	
				actualHosts.add(host);
			}
			
			assertTrue("have a raw log file", rawLogFound);			
		}
		
		assertTrue("all hosts accounted for", hosts.containsAll(actualHosts));
		assertTrue("all hosts accounted for", actualHosts.containsAll(hosts));		
	}
	
	public void testRestartWithNoState () throws Exception
	{
		// we should have a prepopulated logstore
		// simulate a restart
		this.logStore.shutdown();
		
		assertTrue("state file is deleted", 
				this.logStore.getLogState().getStateFile().delete());
		
		// now the following should not do a rollover
		// instead it should create a new state file
		this.logStore.initialize();
		
		assertTrue("state file now exists",
			this.logStore.getLogState().getStateFile().exists());
	}
}
