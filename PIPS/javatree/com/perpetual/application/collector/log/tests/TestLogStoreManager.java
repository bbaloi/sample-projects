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
import java.io.FileReader;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.log.LogWriter;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;


public class TestLogStoreManager extends TestCase {

	private String tempDir = System.getProperty("java.io.tmpdir");
	private File logDir;
	private LogStoreManager logStore = null;
	private GMTFormatter formatter = new GMTFormatter();

	/**
	 * Constructor for TestLogStoreManager.
	 * @param arg0
	 */
	public TestLogStoreManager(String arg0) {
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
	
	public void testInitialization() throws Exception {
		
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), 0);
		System.out.println("initializing log store " + this.logDir.getAbsolutePath());
		
		this.logStore.initialize();
		
		File current = new File(this.logDir.getAbsolutePath() + File.separator
				 + LogStoreConstants.ACCUMULATION_ROOT);
		
		assertTrue("current accumulation directory exists", current.isDirectory());
		
		File archive = new File(this.logDir.getAbsolutePath() + File.separator
						 + LogStoreConstants.ARCHIVE_ROOT);
		
		assertTrue("archive directory exists", archive.isDirectory());
		
		assertTrue("state exists", this.logStore.getLogState().exists());
		assertEquals("rollover interval is correct", 0,
				this.logStore.getLogState().getRolloverInterval());
	}
	
	public void testLogMessage() throws Exception {
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), 0);
		
		this.logStore.initialize();
				
		String rawMessage1 = "<1>Sep 22 14:45:23 hostname "
					+ "processName[95]: this is the message";
		
		QueueEntry entry1 = new QueueEntry(rawMessage1, "relay-host",
				System.currentTimeMillis(), 123456);
		
		SyslogMessage message1 = new SyslogMessage(entry1);
					
		this.logStore.logMessage(message1);
		Thread.sleep(1000);
		
		// make sure the append works
		String rawMessage2 = "<1>Sep 22 14:45:23 hostname "
					+ "processName[191]: this is the message";
					
		QueueEntry entry2 = new QueueEntry(rawMessage2, "relay-host",
				System.currentTimeMillis(), 123457);


		SyslogMessage message2 = new SyslogMessage(entry2);		
								
		this.logStore.logMessage(message2);
		Thread.sleep(1000);
		
		
		File currentDir = this.logStore.getCurrentLogDirectory();
		
		File[] files = currentDir.listFiles();
		
		// should only be one
		assertEquals("two log files (incl raw)", 2, files.length);
		
		for (int i = 0; i < files.length; i++) {
		
			File logFile = files[i];
			
			boolean rawLog = logFile.getName().indexOf("_raw.log") > -1;
					 
			assertTrue("log file exists", logFile.exists());
			
			// check contents
			
			BufferedReader reader = new BufferedReader(new FileReader(logFile));
			
			String line1 = reader.readLine();
			
			if (rawLog) {
				assertEquals("raw log line is correct",
						LogWriter.createRawLogEntry(message1),
						line1);
			}
			else {
				assertEquals("annotated log line is correct",
						LogWriter.createAnnotatedLogEntry(message1, formatter),
						line1);
			}				
		
			String line2 = reader.readLine();
			
			if (rawLog) {
				assertEquals("raw log line is correct",
						LogWriter.createRawLogEntry(message2),
						line2);
			}
			else {
				assertEquals("annotated log line is correct",
						LogWriter.createAnnotatedLogEntry(message2, formatter),
						line2);
			}	
									
			String line3 = reader.readLine();
			
			assertNull("EOF reached", line3);
			
			reader.close();
		}									
	}

}
