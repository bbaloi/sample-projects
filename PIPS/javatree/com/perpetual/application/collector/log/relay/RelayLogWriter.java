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

package com.perpetual.application.collector.log.relay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.action.forward.ForwardedSyslogMessage;
import com.perpetual.application.collector.log.util.BaseLogWriter;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.application.collector.log.util.LogStoreManagerException;
import com.perpetual.exception.PerpetualException;
import com.perpetual.util.GMTFormatter;

public class RelayLogWriter extends BaseLogWriter {

	private static final Logger LOGGER = Logger.getLogger(RelayLogWriter.class);
	
	private String host;
	private RelayLogStoreManager logStore;
	private File relayLogFile;
	private GMTFormatter formatter = null;
		
	public RelayLogWriter (RelayLogStoreManager logStore, String host)
	{
		super(logStore.getWriterThreadPriority());
		this.logStore = logStore;
		this.host = host;
		this.formatter = new GMTFormatter();
	}
	
	public void processWriteQueue () throws PerpetualException
	{	
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("started processing relay writer queue for host "
					+ this.host);
		}
		
		setWriting(true);
		ForwardedSyslogMessage message = null;
		PrintWriter relayLogFileWriter = null;

		try {
	
			while (!this.writeQueue.isEmpty())
			{	
				message = (ForwardedSyslogMessage) this.writeQueue.elementAt(0);
				this.writeQueue.removeElementAt(0);

				if (isLogReset()) {
					createLogFiles(message);
					setLogReset(false);
				}
				
				if (relayLogFileWriter == null) {
					relayLogFileWriter =
						new PrintWriter(new FileOutputStream(this.relayLogFile,
						 true));  //append mode
				}
						
				relayLogFileWriter.println(createRelayLogEntry(message,
						this.formatter));		
			}
		} 
		catch (IOException e) {
			throw new LogStoreManagerException("unable to log message"
				 + message + ":" +  e);
		}
		finally {
			if (relayLogFileWriter != null) {
				relayLogFileWriter.close();
				
				setWriting(false);
				
				if (LOGGER.isEnabledFor(Priority.DEBUG)) {
					LOGGER.debug("finished processing writer queue for host "
							+ this.host);
				}
			}
		}
	}
	
	// create "current" log files of the format
	// host_TS_relay.log
	// need to do this only if the file does not exist
	// i.e. upon startup or after a rollover
	private void createLogFiles (ForwardedSyslogMessage message) throws IOException {
		
		GMTFormatter format = new GMTFormatter();
		String startTSString = format.convertToGMT(message.getForwardTimeStamp());
		
		this.relayLogFile = new File(this.logStore
							.getCurrentLogDirectory().getAbsolutePath()
						+ File.separator
						+ this.host
						+ "_"
						+ startTSString
						+ "_relay.log");
	
									
		if (!this.relayLogFile.exists()) {
			this.relayLogFile.createNewFile();
		}			
	}	
	
	// static so we can access from the tests
	public static String createRelayLogEntry (ForwardedSyslogMessage message,
			GMTFormatter formatter) {
		StringBuffer result = new StringBuffer();
		
		// these are generated or determined by the collector 
		result.append(message.getSyslogMessage().getPacketId());
		String ts = formatter.convertToGMT(message.getForwardTimeStamp());
		result.append(LogStoreConstants.DELIMITER + ts);
		result.append(LogStoreConstants.DELIMITER +
				message.getForwardHost().getHostName());
		result.append(LogStoreConstants.DELIMITER +
						message.getForwardHost().getPort());

		return result.toString();
	}
}