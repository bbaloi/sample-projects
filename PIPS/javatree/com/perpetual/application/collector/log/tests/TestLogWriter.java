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
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.log.LogWriter;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;


public class TestLogWriter extends TestCase {

	private final static int NUMBER_OF_WRITERS = 10; 
	private final static int NUMBER_OF_MESSAGES = 100; 
	
	private File logDir;
	private String tempDir = System.getProperty("java.io.tmpdir");
	private LogStoreManager logStore = null;
	
	private List hosts = new ArrayList();
	private GMTFormatter formatter = new GMTFormatter();


	public TestLogWriter(String arg0) {
		super(arg0);
	}
	
	public void setUp() throws Exception {
		this.logDir = new File(tempDir + File.separator + "logstore");
			
		this.logDir.mkdir();
		
		this.logStore = new LogStoreManager(this.logDir.getAbsolutePath(), 0);
		this.logStore.initialize();
	}
	
	public void tearDown() throws Exception {
		if (this.logStore != null) {
			this.logStore.shutdown();
		}
		
		FileSystemUtilities.deleteRecursively(this.logDir);
	}

	public void testASingleWriter() throws Exception
	{
		LogWriter[] writers = new LogWriter[NUMBER_OF_WRITERS];
		long[] firstTimestamps = new long[NUMBER_OF_WRITERS];
		
		for (int i = 0; i < NUMBER_OF_WRITERS; i++) { 
			writers[i] = new LogWriter(this.logStore, "host" + i);
			writers[i].start();
			firstTimestamps[i] = -1;
		}
		
		for (int j = 0; j < NUMBER_OF_MESSAGES; j++) {
			for (int i = 0; i < NUMBER_OF_WRITERS; i++) { 
 			
				String rawMessage = "<1>Sep 22 14:45:23 host" + i
							+ " writer[" + i + "]: this is the message <<" + j + ">>";
 			
 				long timeStamp = System.currentTimeMillis();
				QueueEntry entry = new QueueEntry(rawMessage, "relay-host",
							timeStamp, 1000*i + j);
							 			
				SyslogMessage message = new SyslogMessage(entry);
				
				if (firstTimestamps[i] == -1) {
					firstTimestamps[i] = timeStamp;
				}
											
 				writers[i].addToWriteQueue(message);
 			}
		}
		
		Thread.sleep(1000);
		
		for (int i = 0; i < NUMBER_OF_WRITERS; i++) { 
			writers[i].shutdown();
			writers[i].join();
		}
	
		File[] files = this.logStore.getCurrentLogDirectory().listFiles();
		
		assertEquals("we have the right number of files (annotated + raw)",
			2 * NUMBER_OF_WRITERS, files.length);
		
		// check each one has the correct number of messages
		
		BufferedReader reader = null;
		
		try {
			for (int i = 0; i < files.length; i++) {
				reader = new BufferedReader(new FileReader(files[i]));
				
				int nlines = 0;
				long previousTimeStamp = -1;
				long previousMessageNumber = -1;
				String line;
				
				String host = files[i].getName().substring(0,
					files[i].getName().indexOf('_'));
				int hostIndex = Integer.parseInt(host.substring("host".length()));
				String startTimestamp = files[i].getName().substring(
						files[i].getName().indexOf('_'),
						files[i].getName().indexOf(".log"));
				boolean rawLog = files[i].getName().indexOf("_raw.log") > -1;
							
				while ((line = reader.readLine()) != null) {
					nlines++;
			
					if (!rawLog) {
					
						int firstSpace = line.indexOf(LogStoreConstants.DELIMITER);
						int nextSpace = line.substring(firstSpace + 1)
								.indexOf(LogStoreConstants.DELIMITER);
						
						long timeStamp = this.formatter.convertToMsFromEpoch(
								line.substring(firstSpace + 1,
								firstSpace + 1 + nextSpace));
						
						// check first timestamp
						if (nlines == 1) {
							assertEquals("first timestamp",
									firstTimestamps[hostIndex], timeStamp);
						}
								
				
						assertTrue("lines are written in timestamp order (line = + "
								+ nlines + ")", timeStamp >= previousTimeStamp);			
					
						previousTimeStamp = timeStamp;
					}
										
					int messageNumber = (new Integer(
						line.substring(line.indexOf("<<") + 2, line.indexOf(">>"))))
							.intValue();
						
					assertTrue("lines are written in order they were logged", 
										messageNumber == previousMessageNumber + 1);
					previousMessageNumber = messageNumber;								
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
