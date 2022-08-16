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


package com.perpetual.application.collector;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.Listener;
import com.perpetual.application.collector.log.LogStoreManager;
import com.perpetual.application.collector.message.MessageConstants;
import com.perpetual.application.collector.message.MessageQueue;
import com.perpetual.application.collector.message.MessageQueueProcessor;
import com.perpetual.util.resource.ResourceLoader;
import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.PerpetualManagementAgent;
import javax.management.MBeanServer;
import javax.management.QueryExpSupport;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.Set;
import java.io.File;

import com.perpetual.application.collector.management.*;


public class Controller {
	
	private static final Logger LOGGER = Logger.getLogger(Controller.class);
	
	private Configuration configuration = null;
	private LogStoreManager logStore = null;
	private MessageQueue messageQueue = null;
	private Listener messageListener = null;
	private MessageQueueProcessor messageQueueProcessor = null;
	private boolean stopped = false;
	private Thread mainThread = null;
        private static ManagementConstants managementConstants=null;
        private PerpetualManagementAgent managementAgent=null;
        	
	public Controller (Configuration configuration)
	{
		this.configuration = configuration;
		
		try {
			if (this.configuration.getProperties() == null || this.configuration.getProperties().isEmpty()) 
                        {
				this.configuration.setProperties(ResourceLoader.loadProperties(getConfigPath()+"/sherlock-collector.properties"));
			} 
		} catch (IOException e) {
			LOGGER.error("cannot load collector properties, will use defaults.");
		}
		
		// create a message queue
		this.messageQueue = new MessageQueue(
				ResourceLoader.getIntProperty(this.configuration.getProperties(),
					"sherlock.collector.message.queue.InitialCapacity",
					MessageConstants.INITIAL_CAPACITY),
				ResourceLoader.getIntProperty(this.configuration.getProperties(),
							"sherlock.collector.message.queue.CapacityIncrement",
							MessageConstants.CAPACITY_INCREMENT),
				ResourceLoader.getIntProperty(this.configuration.getProperties(),
					"sherlock.collector.message.queue.GarbageCollectionInterval",
					MessageConstants.GARBAGE_COLLECTION_INTERVAL)			
				);

		String listenerThreadPriorityString = ResourceLoader.getStringProperty(
					this.configuration.getProperties(), "sherlock.collector.listener.ThreadPriority",
							"MAX_PRIORITY");
		
		int listenerThreadPriority = convertPriorityToInt(listenerThreadPriorityString);	


		// create a listener
		this.messageListener = new Listener(this.messageQueue,
						listenerThreadPriority,
						this.configuration);

		// create & start a message processor
		this.messageQueueProcessor = new MessageQueueProcessor(this,
					this.configuration);
					
		String logWriterPriorityString = ResourceLoader.getStringProperty(
				this.configuration.getProperties(),
						"sherlock.collector.log.WriterThreadPriority",
						"NORM_PRIORITY");
		
		int logWriterPriority = convertPriorityToInt(logWriterPriorityString);				
						
		this.logStore = new LogStoreManager(this.configuration.getLogStoreRootPath(),
					this.configuration.getRolloverInterval(), logWriterPriority);
		
	}
			
	public void start () throws Exception {
		
		this.mainThread = Thread.currentThread();
		
		this.logStore.initialize();
		
		this.messageQueueProcessor.initialize();
					
		this.messageQueueProcessor.start();
		this.messageQueue.initialize();
		this.messageListener.start();
		
		idle();
	}
	
	private void idle () {
		
		while (!this.stopped) {
			try {
				this.messageQueueProcessor.join();
			} catch (InterruptedException e) {
			}
		}

	}
	
	public void shutdown () {
		this.messageListener.shutdown();
		this.logStore.shutdown();
		this.stopped = true;
		this.mainThread.interrupt();
	}
	
	private int convertPriorityToInt (String priorityString)
	{
		int result = Thread.NORM_PRIORITY;  // default
		
		if ("NORM_PRIORITY".equals(priorityString)) {
			result = Thread.NORM_PRIORITY;
		} else if ("MAX_PRIORITY".equals(priorityString)) {
			result = Thread.MAX_PRIORITY;
		} else if ("MIN_PRIORITY".equals(priorityString)) {
			result = Thread.MIN_PRIORITY;
		}
		
		return result;
	}
	
        private static String getConfigPath()
        {
            String cfgPath=null;
            String pipsHome = System.getProperty("perpetualhome");
            try
            {
                File pFile=new File(pipsHome);
                cfgPath=pFile.getCanonicalPath()+"/config";
                
            }
            catch(Exception excp)
            {
                excp.printStackTrace();
            }
            
            
            return cfgPath;
        }
	private static void initializeLog4j() 
        {
            try 
            {
             PropertyConfigurator.configure(ResourceLoader.loadProperties(getConfigPath()+"/log4j.properties"));
            } 
            catch (IOException e) 
            {
			System.err.println("Cannot initialize log4j: " + e);
			System.exit(1);
            }
	}	
	
	public MessageQueue getMessageQueue() {
		return messageQueue;
	}
	
	public int getPacketCount() {
		return this.messageQueue.getPacketCount();
	}
	
	private static Configuration parseCommandLine(String [] args) {
		Options options = new Options();
		
		Option logStoreRootOption = new Option("l", true, "logstore root directory");
		Option rolloverIntervalOption = new Option("r", true,
				"rollover interval (in ms)");
		Option listenerPortOption = new Option("p", true,
					"listener port");
		Option configurationFileOption = new Option("c", true,
							"action configuration file");
		Option bindingAddressOption = new Option("b", true,
							"binding IP address");					

		options.addOption(logStoreRootOption);
		options.addOption(rolloverIntervalOption);
		options.addOption(listenerPortOption);
		options.addOption(configurationFileOption);
		options.addOption(bindingAddressOption);
		
		
		CommandLineParser parser = new PosixParser();
		
		CommandLine commandLine;
		String logStoreRootPath = null;
		String configurationFilePath = null;
		long rolloverInterval = 0;
		int listenerPort = 0;
		String bindingAddressString = null;
		Configuration configuration = new Configuration();
		
		try {
			commandLine = parser.parse( options, args );
			
			if (commandLine.hasOption(logStoreRootOption.getOpt())) {
				logStoreRootPath = commandLine.getOptionValue(
						logStoreRootOption.getOpt());
				configuration.setLogStoreRootPath(logStoreRootPath);		
			} else {
				configuration.setLogStoreRootPathToDefault();
			}

			if (commandLine.hasOption(configurationFileOption.getOpt())) {
				configurationFilePath = commandLine.getOptionValue(
						configurationFileOption.getOpt());
				configuration.setConfigurationFilePath(configurationFilePath);	
			}			

			if (commandLine.hasOption(rolloverIntervalOption.getOpt())) {
				try {
					rolloverInterval = Long.parseLong(commandLine.getOptionValue(
							rolloverIntervalOption.getOpt()));
				} catch (NumberFormatException e) {
					throw new ParseException("rollover interval must be numeric");
				}
				configuration.setRolloverInterval(rolloverInterval);
			} else {
				configuration.setRolloverIntervalToDefault();
			}

			if (commandLine.hasOption(listenerPortOption.getOpt())) {
				try {
					listenerPort = Integer.parseInt(commandLine.getOptionValue(
							listenerPortOption.getOpt()));
				} catch (NumberFormatException e) {
					throw new ParseException("listener port must be numeric");
				}
				configuration.setListenerPort(listenerPort);
			} else {
				configuration.setListenerPortToDefault();
			}
			
			if (commandLine.hasOption(bindingAddressOption.getOpt())) {
				bindingAddressString = commandLine.getOptionValue(
						bindingAddressOption.getOpt());									
				configuration.setBindingAddress(bindingAddressString);
			} else {
				configuration.setBindingAddressToDefault();
			}

						
		}
		catch( ParseException pe ) {
			HelpFormatter formatter = new HelpFormatter();

			formatter.printUsage(new PrintWriter(System.out),
					80, "collector", options);

			LOGGER.fatal("Incorrect command line: " + pe.getMessage() );
		}

		return configuration;
	}
	
	public static void printLicenseAndDisclaimer()
	{
		String licenseText = 
			"\n" +
		" Sherlock syslog collector.\n" +
		" by Perpetual IP Systems GmbH\n" +
		" Copyright (c) 2003 Perpetual IP Systems GmbH\n" +
		" Web: www.perpetual-ip.com\n" +
		"\n" +
		" This program is free software; you can redistribute it and/or modify\n" +
		" it under the terms of the GNU General Public License as published by\n" +
		" the Free Software Foundation; either version 2 of the License, or\n" +
		" (at your option) any later version.\n" +
		"\n" +
		" This program is distributed in the hope that it will be useful,\n" +
		" but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
		" MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
		" GNU General Public License for more details.\n" +
		"\n" +
		" You should have received a copy of the GNU General Public License\n" +
		" along with this program; if not, write to the Free Software\n" +
		" Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA\n" +
		"\n" +
		" This product includes software developed by the\n" +
		" Apache Software Foundation (http://www.apache.org/).\n" +
			"\n\n" +
			"NO WARRANTY\n" +
			"-----------\n" +
			"THIS SOFTWARE IS PROVIDED \"AS-IS\" WITH ABSOLUTELY NO WARRANTY OF\n" +
			"ANY KIND, EITHER IMPLIED OR EXPRESSED, INCLUDING, BUT NOT LIMITED\n" +
			"TO, THE IMPLIED WARRANTIES OF FITNESS FOR ANY PURPOSE\n"+
			"AND MERCHANTABILITY.\n" +
			"\n" +
			"WITHOUT LIMITING THE FOREGOING, THE AUTHORS DO NOT WARRANT\n" +
			"ANY RESULTS YOU MAY OBTAIN FROM USING THIS SOFTWARE.\n" +
			"\n\n" +
			"NO LIABILITY\n" +
			"------------\n" +
			"\n" +
			"THE AUTHORS ASSUME ABSOLUTELY NO RESPONSIBILITY FOR ANY\n" +
			"DAMAGES THAT MAY RESULT FROM THE USE,\n" +
			"MODIFICATION OR REDISTRIBUTION OF THIS SOFTWARE, INCLUDING,\n" +
			"BUT NOT LIMITED TO, LOSS OF DATA, CORRUPTION OR DAMAGE TO DATA,\n" +
			"LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF SAVINGS,\n" +
			"BUSINESS INTERRUPTION OR FAILURE TO INTEROPERATE WITH\n" +
			"OTHER SOFTWARE, EVEN IF ADVISED OF SUCH DAMAGES\n" +
			"OR THEY ARE FORESEEABLE.\n" +
			"\n\n";
			
			System.out.println(licenseText);
		
			LOGGER.info(licenseText);
	}


	public static void main(String[] args) {
		
		initializeLog4j();
		
		printLicenseAndDisclaimer();                
                
		
		Configuration configuration = parseCommandLine(args);
		
		Controller controller = new Controller(configuration);
                
                controller.initManagementAgent();
                controller.initManagementComponents(PerpetualManagementAgent.getInstance().getMBeanServer());
               
		try {
			controller.start();
		} catch (Exception e) {
			LOGGER.fatal("error starting collector " + e);
                        e.printStackTrace();
			System.exit(1);
		}
	}

	public LogStoreManager getLogStore() {
		return logStore;
	}
        
        public void initManagementAgent()
        {
            LOGGER.info("----Initialising Management Agent !----");            
            try
            {                
                managementConstants = new ManagementConstants();
                managementAgent = PerpetualManagementAgent.getInstance();
                managementAgent.initialize(null,null);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void initManagementComponents(MBeanServer pMBeanServer)
        {
            LOGGER.debug("------Loading up the MBeans !------");
            try
            {
                //MBeanServer lServer = WysdomManagementAgent.getInstance().getMBeanServer();
                ControllerMBeanLoader lBeanLoader = new ControllerMBeanLoader(pMBeanServer);
                lBeanLoader.loadMBeans();
            }
            catch(Exception excp)
            {
                excp.printStackTrace();
            }
        }              

}
