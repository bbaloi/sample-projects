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


package com.perpetual.application.collector.configuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Configuration {

	private static final Logger LOGGER = Logger.getLogger(Configuration.class);

	public static final long DEFAULT_ROLLOVER_INTERVAL = 60*60*1000;  // one hour
	public static final String DEFAULT_LOGSTORE_ROOT = "/var/log/logstore";
	public static final int DEFAULT_LISTENER_PORT = 514;
	public static final String DEFAULT_BINDING_ADDRESS = "0.0.0.0";

	private String logStoreRootPath;
	private String configurationFilePath;
	private BufferedReader configurationFileReader;
	private long rolloverInterval;
	private int listenerPort;
	private String bindingAddress;
	private Properties properties;

	public Configuration() {	
	}
	
	public void setDefaults() {
		setRolloverIntervalToDefault();
		setLogStoreRootPath(DEFAULT_LOGSTORE_ROOT);
		setListenerPort(DEFAULT_LISTENER_PORT);
		setBindingAddress(DEFAULT_BINDING_ADDRESS);
		setProperties(new Properties());
		setConfigurationFilePath(null);
		setConfigurationFileReader(null);
	}
	
	public void loadPropertiesFrom (String propertiesStrings)
	{
		if (this.properties == null) {
			this.properties = new Properties();
		}
		try {
			this.properties.load(
					new ByteArrayInputStream(propertiesStrings.getBytes()));
		} catch (IOException e) {
		}
	}

	public String getLogStoreRootPath() {
		return logStoreRootPath;
	}

	public long getRolloverInterval() {
		return rolloverInterval;
	}

	public void setLogStoreRootPath(String string) {
		LOGGER.info("setting log store root to '" + string + "'.");
		
		logStoreRootPath = string;
	}

	public void setRolloverInterval(long l) {
		LOGGER.info("setting rollover interval to " + l + " ms.");
		
		rolloverInterval = l;
	}
	
	public void setRolloverIntervalToDefault()
	{
		setRolloverInterval(DEFAULT_ROLLOVER_INTERVAL);
	}

	public void setLogStoreRootPathToDefault()
	{
		setLogStoreRootPath(DEFAULT_LOGSTORE_ROOT);
	}
	
	public void setListenerPortToDefault()
	{
		setListenerPort(DEFAULT_LISTENER_PORT);
	}
	
	public int getListenerPort() {
		return listenerPort;
	}
	
	public void setListenerPort(int i) {
		LOGGER.info("setting listener port to " + i);
		listenerPort = i;
	}
	
	public void setBindingAddressToDefault()
	{
		setBindingAddress(DEFAULT_BINDING_ADDRESS);
	}
	
	public String getBindingAddress() {
		return bindingAddress;
	}
	
	public void setBindingAddress(String s) {
		LOGGER.info("setting listening address to " + s);
		bindingAddress = s;
	}
	
	
	/**
	 * @return
	 */
	public String getConfigurationFilePath() {
		return configurationFilePath;
	}

	/**
	 * @param string
	 */
	public void setConfigurationFilePath(String string) {
		LOGGER.info("setting configuration file to '" + string + "'");
		configurationFilePath = string;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}


	public BufferedReader getConfigurationFileReader() {
		return configurationFileReader;
	}

	public void setConfigurationFileReader(BufferedReader reader) {
		configurationFileReader = reader;
	}
	
	public BufferedReader getConfigurationFileAsReader ()
	{
		try {
			if (this.configurationFilePath != null) {
				this.configurationFileReader = new BufferedReader(
					new FileReader(new File(this.configurationFilePath)));
			}
		} catch (FileNotFoundException e) {
		}
		
		return getConfigurationFileReader();
	}
}
