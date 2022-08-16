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
import java.util.Random;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.log.util.LogFile;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;

public class TestWithMultipleConcurrentWritersWithNoRollover extends TestCase {
	
	private final static int NUMBER_OF_WRITERS = 10; 
	private final static int NUMBER_OF_MESSAGES = 15;

	private class LogWriterRunnable implements Runnable
	{
		private LogStoreManager logStore;
		private String hostName;
		private int numberOfMessages;
		private Random random;
		private int id;
		
		public LogWriterRunnable (int id, LogStoreManager logStore,
				int numberOfMessages)
		{
			this.id = id;
			this.logStore = logStore;
			this.hostName = "host" + id;
			this.numberOfMessages = numberOfMessages;
			this.random = new Random((long) id);
		}
		
		public void run() {
	
			System.out.println("Writer[" + this.id + "] is starting" +
				" (attempting to log " + this.numberOfMessages + " messages).");
			
			for (int i = 0; i < this.numberOfMessages; i++) {		

				long interval = this.random.nextInt(5) * 1000;
				
				try {
					Thread.sleep(interval);	
				} catch (InterruptedException e) {
				}
	
	
				String rawMessage = "<1>Sep 22 14:45:23 " + this.hostName + " "
								+ "writer[" + this.id + "]: this is the message " + i;
 			
				QueueEntry entry = new QueueEntry(rawMessage, this.hostName + "-relay",
								System.currentTimeMillis(), 1000*this.id + i);
				
				SyslogMessage message = new SyslogMessage(entry);
						
				try {
					System.out.println("Writer[" + this.id + "] is logging message.");
					this.logStore.logMessage(message);
					System.out.println("Writer[" + this.id + "] just logged message.");
				} catch (Throwable e1) {
					System.err.println("writer[" + id + "] cannot log message");
				}
				
			}
			
			System.out.println("Writer[" + this.id + "] is finishing.");
		}		
	}
	
	private String tempDir = System.getProperty("java.io.tmpdir");
	private File logDir;
	private LogStoreManager logStore = null;
	private GMTFormatter formatter = new GMTFormatter();

	public TestWithMultipleConcurrentWritersWithNoRollover(String arg0) {
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
	
	public void testWithNoRollover () throws Exception
	{
		Thread[] threads = new Thread[NUMBER_OF_WRITERS];
		
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), 0);
		
		this.logStore.initialize();
		
		for (int i = 0; i < NUMBER_OF_WRITERS; i++) {
			LogWriterRunnable writer = new LogWriterRunnable(i, this.logStore,
					NUMBER_OF_MESSAGES);
	
			threads[i] = new Thread(writer);
			threads[i].start();	
		}
	
		// wait for them to finish
		for (int i = 0; i < NUMBER_OF_WRITERS; i++) {
			threads[i].join();
		}
		
		this.logStore.shutdown();
		Thread.sleep(1000); // shouldn't have to do this but one of the
								// writers may be about to process its queue	
		
		// check file system
		
		File[] files = this.logStore.getCurrentLogDirectory().listFiles();
		
		assertEquals("we have the right number of files",
			2*NUMBER_OF_WRITERS, files.length);
		
		// check each one has the correct number of messages
		
		BufferedReader reader = null;
		
		try {
			for (int i = 0; i < files.length; i++) {
				reader = new BufferedReader(new FileReader(files[i]));
				
				int nlines = 0;
				long previousTimeStamp = -1;
				String line;
				
				while ((line = reader.readLine()) != null) {
					nlines++;
			
					if (!LogFile.isRawLogFile(files[i].getName())) {					
						int firstSpace = line.indexOf(LogStoreConstants.DELIMITER);
						int nextSpace = line.substring(firstSpace + 1)
								.indexOf(LogStoreConstants.DELIMITER);
								
						long timeStamp = this.formatter.convertToMsFromEpoch(
								line.substring(firstSpace + 1,
								firstSpace + 1 + nextSpace));
						
						assertTrue("lines are written in timestamp order (line = + "
								+ nlines + ")", timeStamp >= previousTimeStamp);			
							
						previousTimeStamp = timeStamp;
					}		
				}
				
				reader.close();
				
				assertEquals("have all the messages in each file",
							NUMBER_OF_MESSAGES, nlines);
			}
		} catch (Exception e) {
			throw(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
	}
	
}
