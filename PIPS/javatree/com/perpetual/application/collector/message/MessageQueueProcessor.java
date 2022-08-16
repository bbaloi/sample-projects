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

package com.perpetual.application.collector.message;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.Controller;
import com.perpetual.application.collector.action.ActionProcessor;
import com.perpetual.application.collector.action.TextFileParser;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.log.util.LogStoreManagerException;
import com.perpetual.application.collector.throttle.ThrottleQueueControl;
import com.perpetual.util.resource.ResourceLoader;

public class MessageQueueProcessor extends Thread {

	private static final Logger LOGGER =
			Logger.getLogger(MessageQueueProcessor.class);	

	private String message;
	private String hostName;
	private long systemTimeStamp;
	private Controller controller;
	private long sleepTime;
	private MessageQueue messageQueue;
	private ThrottleQueueControl filter;
	private ActionProcessor actionProcessor = null;
	private Configuration configuration; 
	private int epsRating;
	private double tolerance;

	public MessageQueueProcessor (Controller controller,
			Configuration configuration)
	{
		this.controller = controller;
		this.configuration = configuration;
		
		this.sleepTime = ResourceLoader.getLongProperty(
			this.configuration.getProperties(),
			"sherlock.collector.message.queue.ProcessorIdleTime",
			MessageConstants.PROCESSOR_IDLE_TIME);
	
		this.messageQueue = controller.getMessageQueue();
		this.epsRating = ResourceLoader.getIntProperty(
			this.configuration.getProperties(),
			"sherlock.collector.EpsRating", 0);
		this.tolerance = ResourceLoader.getIntProperty(
				this.configuration.getProperties(),
				"sherlock.collector.EpsRatingTolerance", 0);

		this.filter = new ThrottleQueueControl();

		LOGGER.info("Creating incoming message processor, eps rating = "
				+ this.epsRating + ", tolerance = " + this.tolerance);

	}
	
	public void initialize() throws Exception
	{	
		if (this.configuration.getConfigurationFileAsReader() != null) {
			LOGGER.info("Reading action configuration from "
				+ this.configuration.getConfigurationFilePath());
			
			TextFileParser parser = new TextFileParser(
					this.configuration.getConfigurationFileAsReader(),
					this.configuration);
			List actionList = parser.parse();
			
			this.actionProcessor = new ActionProcessor(actionList);
			this.actionProcessor.openAllActions();
		} else {
			LOGGER.info("No action configuration file specified.");
			
			this.actionProcessor = new ActionProcessor(null);
		}	
	}
	
	public void run () {
		
		LOGGER.info("Starting message queue processor ...");
		LOGGER.info("Idle time = " + this.sleepTime);
		
		while (true) {
			if (!this.messageQueue.hasUnprocessedEntries()) {
				try {					
					Thread.sleep(this.sleepTime);
				} catch (InterruptedException e) {
				}
			}
			else {
				QueueEntry entry;
				while ((entry = (QueueEntry) this.messageQueue.getNextUnprocessedItem()) != null) {
					if (this.filter.isProcessable(this.messageQueue, entry,
							this.epsRating, this.tolerance)) {
						if (LOGGER.isEnabledFor(Priority.DEBUG)) {
							LOGGER.debug("Processing packet #" + entry.getPacketNumber());
							// could write out stats - processed/discarded
						}
					
						processMessage(entry);
						entry.setStatus(QueueEntry.PROCESSED);
					} else {
						if (LOGGER.isEnabledFor(Priority.DEBUG)) {
							LOGGER.debug("Discarding packet #" + entry.getPacketNumber());
						}
						entry.setStatus(QueueEntry.DISCARDED);
					}
				}
			}
			
		}
	}
	
	public void processMessage(QueueEntry entry)
	{
		SyslogMessage message = null;

		message = new SyslogMessage(entry);

		// process the message
		// the only thing we are doing is logging it
		// forwarding, email, etc. will come in later versions		
		try {
			this.controller.getLogStore().logMessage(message);
			this.actionProcessor.process(message);
		} catch (LogStoreManagerException e) {
			LOGGER.error("cannot log message: " + e);
			LOGGER.error("message = ' " + message + "'.");
		}
	}
}