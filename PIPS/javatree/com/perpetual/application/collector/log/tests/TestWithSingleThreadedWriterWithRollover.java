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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.log.util.LogFile;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;

public class TestWithSingleThreadedWriterWithRollover extends TestCase {
	
	private final static int NUMBER_OF_HOSTS = 10; 
	private final static int NUMBER_OF_MESSAGES = 50;
	private final static int ROLLOVER_INTERVAL = 500;

	private class HostInformation 
	{
		private int numberOfAnnotatedMessages = 0;
		private int numberOfRawMessages = 0;
		
		public int getNumberOfAnnotatedMessages() {
			return numberOfAnnotatedMessages;
		}

		public int getNumberOfRawMessages() {
			return numberOfRawMessages;
		}

		public void incrementNumberOfAnnotatedMessages(int i) {
			numberOfAnnotatedMessages += i;
		}
		
		public void incrementNumberOfRawMessages(int i) {
			numberOfRawMessages += i;
		}
	}
	
	private String tempDir = System.getProperty("java.io.tmpdir");
	private File logDir;
	private LogStoreManager logStore = null;
	private GMTFormatter formatter = new GMTFormatter();

	public TestWithSingleThreadedWriterWithRollover(String arg0) {
		super(arg0);
	}

	public void setUp () throws Exception
	{
		this.logDir = new File(tempDir + File.separator + "logstore");
		this.logDir.mkdir();
	}

	public void tearDown () throws Exception
	{	
		FileSystemUtilities.deleteRecursively(this.logDir);
	}	
	
	public void testWithRollover () throws Exception
	{	
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(),
				ROLLOVER_INTERVAL);
		
		this.logStore.initialize();
		
		for (int i = 0; i < NUMBER_OF_MESSAGES; i++) {		
	
			for (int j = 0; j < NUMBER_OF_HOSTS; j++) {
				
				String rawMessage = "<1>Sep 22 14:45:23 " + "host" + j + " "
								+ "writer[" + j + "]: this is the message " + i;

				QueueEntry entry = new QueueEntry(rawMessage, "host" + j + "-relay",
								System.currentTimeMillis(), 1000*j + i);
				
				SyslogMessage message = new SyslogMessage(entry);	
					
				try {
					System.out.println("Writer[" + j + "] is logging message.");
					this.logStore.logMessage(message);
					System.out.println("Writer[" + j + "] just logged message.");
				} catch (Throwable e1) {
					System.err.println("writer[" + j + "] cannot log message");
				}
				
			}
		}
		
		Thread.sleep(6000); // shouldn't have to do this but one of the
							// writers may be about to process its queue		

		// wait for any remaining messages to flush out
		this.logStore.shutdown();
		
		
		Map loggedMessages = new HashMap();
		
		// check file system
		
		File[] files = this.logStore.getCurrentLogDirectory().listFiles();
		
		// check "current" folder
		
		for (int i = 0; i < files.length; i++) {
			BufferedReader reader = new BufferedReader(new FileReader(files[i]));
			
			String fileName = files[i].getName();
			String hostName = LogFile.deriveHostFromLogFileName(fileName);
			boolean rawLog = LogFile.isRawLogFile(fileName);
			
			int nlines = 0;
			
			while (reader.readLine() != null) {
				nlines++;
			}
			
			reader.close();
				
			HostInformation hostInformation = (HostInformation)
					loggedMessages.get(hostName);
			 	
			if (hostInformation == null) {
				hostInformation =  new HostInformation();
				loggedMessages.put(hostName, hostInformation);
			}
						
			if (rawLog) {
				hostInformation.incrementNumberOfRawMessages(nlines);
			} else {
				hostInformation.incrementNumberOfAnnotatedMessages(nlines);
			}
		}
		
		File[] archiveDirs = this.logStore.getArchiveLogDirectory().listFiles();

		for (int i = 0; i < archiveDirs.length; i++) {
			
			String fileName = archiveDirs[i].getName();
			String hostName = fileName;
			
			File[] logFiles = archiveDirs[i].listFiles();
			
			for (int j = 0; j < logFiles.length; j++) {
				BufferedReader reader = new BufferedReader(
						new FileReader(logFiles[j]));

				String logFileName = logFiles[j].getName();

				boolean rawLog = LogFile.isRawLogFile(logFileName);
			
				long startTimeStamp = formatter.convertToMsFromEpoch(LogFile
						.deriveStartTimeStampFromLogFileName(logFileName)); 
				
				long endTimeStamp = formatter.convertToMsFromEpoch(LogFile
										.deriveEndTimeStampFromLogFileName(logFileName)); 
				int nlines = 0;
			
				String line;
				long previousTimeStamp = -1;
				
				while ((line = reader.readLine()) != null) {
					nlines++;
					
					// check that the timestamp is within bounds
					
					if (!rawLog) {
						int firstSpace = line.indexOf(LogStoreConstants.DELIMITER);
						int nextSpace = line.substring(firstSpace + 1)
								.indexOf(LogStoreConstants.DELIMITER);
						
						long timeStamp = this.formatter.convertToMsFromEpoch(
								line.substring(firstSpace + 1,
								firstSpace + 1 + nextSpace));
								
//						assertTrue("timestamp is within range for " + hostName,
//								timeStamp >= startTimeStamp && timeStamp <= endTimeStamp);
					
						assertTrue("lines are written in timestamp order", 
									timeStamp >= previousTimeStamp);		
						previousTimeStamp = timeStamp;
					}					
				}
			
				reader.close();
			
				HostInformation hostInformation = (HostInformation)
						loggedMessages.get(hostName);
			 	
				if (hostInformation == null) {
					hostInformation =  new HostInformation();
					loggedMessages.put(hostName, hostInformation);
				}
						
				if (rawLog) {
					hostInformation.incrementNumberOfRawMessages(nlines);
				} else {
					hostInformation.incrementNumberOfAnnotatedMessages(nlines);
				}
			}
		}
		
		// each one should have written NUMBER_OF_MESSAGES lines
		
		for (Iterator i = loggedMessages.keySet().iterator(); i.hasNext();) {
			
			String host = (String) i.next();
			int numberOfAnnotatedLines = ((HostInformation) loggedMessages.get(host))
						.getNumberOfAnnotatedMessages();
			int numberOfRawLines = ((HostInformation) loggedMessages.get(host))
								.getNumberOfRawMessages();
				
		
			assertEquals("have the correct total number of annotated messages for " + host,
					NUMBER_OF_MESSAGES, numberOfAnnotatedLines);
			assertEquals("have the correct total number of raw messages for " + host,
								NUMBER_OF_MESSAGES, numberOfRawLines);	
		}
	}
	
}
