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

package com.perpetual.application.collector.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.log.util.*;
import com.perpetual.application.collector.log.util.BaseLogWriter;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.exception.PerpetualException;
import com.perpetual.util.GMTFormatter;

public class LogWriter extends BaseLogWriter {

	private static final Logger LOGGER = Logger.getLogger(LogWriter.class);
	
	private String host;
	private LogStoreManager logStore;
	private File annotatedLogFile;
	private File rawLogFile;
	private GMTFormatter formatter = null;
		
	public LogWriter (LogStoreManager logStore, String host)
	{
		super(logStore.getWriterThreadPriority());
		this.logStore = logStore;
		this.host = host;
		this.formatter = new GMTFormatter();
	}
	
	public void processWriteQueue () throws PerpetualException
	{
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("started processing writer queue for host " + this.host);
		}
		
		setWriting(true);
		SyslogMessage message = null;
		PrintWriter annotatedLogFileWriter = null;
		PrintWriter rawLogFileWriter = null;

		try {
	
			while (!this.writeQueue.isEmpty())
			{	
				message = (SyslogMessage) this.writeQueue.elementAt(0);
				this.writeQueue.removeElementAt(0);

				if (isLogReset()) {
					createLogFiles(message);
					setLogReset(false);
				}
				
				if (annotatedLogFileWriter == null) {
					annotatedLogFileWriter =
						new PrintWriter(new FileOutputStream(this.annotatedLogFile,
						 true));  //append mode
				}
				
				if (rawLogFileWriter == null) {
					rawLogFileWriter = new PrintWriter(new FileOutputStream(
								this.rawLogFile, true));  //append mode	
				}
						
				annotatedLogFileWriter.println(createAnnotatedLogEntry(message,
						this.formatter));
				rawLogFileWriter.println(createRawLogEntry(message));		
			}
		} 
		catch (IOException e) {
			throw new LogStoreManagerException("unable to log message"
				 + message + ":" +  e);
		}
		finally {
			if (annotatedLogFileWriter != null) {
				annotatedLogFileWriter.close();
				rawLogFileWriter.close();
				
				setWriting(false);
				
				if (LOGGER.isEnabledFor(Priority.DEBUG)) {
					LOGGER.debug("finished processing writer queue for host "
							+ this.host);
				}
			}
		}
	}
	
	// create "current" log files of the format
	// host_TS.log
	// need to do this only if the file does not exist
	// i.e. upon startup or after a rollover
	private void createLogFiles (SyslogMessage message) throws IOException {
		
		GMTFormatter format = new GMTFormatter();
		String startTSString = format.convertToGMT(message.getSystemTimestamp());
		
		this.annotatedLogFile = new File(this.logStore
							.getCurrentLogDirectory().getAbsolutePath()
						+ File.separator
						+ this.host
						+ "_"
						+ startTSString
						+ ".log");
		this.rawLogFile = new File(this.logStore.getCurrentLogDirectory()
							.getAbsolutePath()
							+ File.separator
							+ this.host
							+ "_"
							+  startTSString
							+ "_raw.log");
									
		if (!this.annotatedLogFile.exists()) {
			this.annotatedLogFile.createNewFile();
		}
				
		if (!this.rawLogFile.exists()) {
				this.rawLogFile.createNewFile();
		}
	}
	
	// static so we can access from the tests
	public static String createAnnotatedLogEntry (SyslogMessage message,
			GMTFormatter formatter) {
		StringBuffer result = new StringBuffer();
		
		// these are generated or determined by the collector 
		result.append(message.getPacketId());
		String ts = formatter.convertToGMT(message.getSystemTimestamp());
		result.append(LogStoreConstants.DELIMITER + ts);

		result.append(LogStoreConstants.DELIMITER + message.getRelayHost());
		
		// these are parsed out from the raw syslog message
		result.append(LogStoreConstants.DELIMITER + message.getSeverity());
		result.append(LogStoreConstants.DELIMITER + message.getFacility());
		result.append(LogStoreConstants.DELIMITER + message.getTimestamp());
		result.append(LogStoreConstants.DELIMITER + message.getHost());
		
		String tag;
		String processName;
		
		processName = message.getProcessName();
		
		if (processName == null) {
			tag = message.getTag();
			result.append(LogStoreConstants.DELIMITER + tag
					+ LogStoreConstants.DELIMITER + message.getContent());
		} else {
			result.append(LogStoreConstants.DELIMITER
				+ processName
				+ LogStoreConstants.DELIMITER
				+ "[" + message.getProcessId()+ "]:");
			result.append(message.getContent());
		}
		
		return result.toString();
	}
	
	public static String createRawLogEntry (SyslogMessage message) {
		
		return message.getRawMessage();
	}
		
}