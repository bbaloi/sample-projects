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


package com.perpetual.util.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ResourceLoader 
{
    private static InputStream is=null;

	public static Properties loadProperties(String name) throws IOException
	{
		Properties result = new Properties();
		
		is = ResourceLoader.class.getClassLoader().getResourceAsStream(name);
                
		if (is == null) 
                {
                      is = new FileInputStream(name);
                      if(is==null)
			throw new IOException("cannot find resource " + name);
		}
		
		result.load(is);
		
		return result;
	}
	
	public static int getIntProperty (Properties properties, String key, 
			int defaultValue)
	{
		int result = defaultValue;
		
		if (properties != null) { 
	
			String value = properties.getProperty(key);
			
			if (value != null) {
				try {
					result = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
			}
		}
		
		return result;
	}

	public static long getLongProperty (Properties properties, String key, 
			long defaultValue)
	{
		long result = defaultValue;
	
		if (properties != null) { 	
			String value = properties.getProperty(key);
			
			if (value != null) {
				try {
					result = Long.parseLong(value);
				} catch (NumberFormatException e) {
				}
			}
		}	
		
		return result;
	}
	
	public static double getDoubleProperty (Properties properties, String key, 
			double defaultValue)
	{
		double result = defaultValue;
		
		if (properties != null) { 
			String value = properties.getProperty(key);
			
			if (value != null) {
				try {
					result = Double.parseDouble(value);
				} catch (NumberFormatException e) {
				}
			}
		}
				
		return result;
	}
	
	public static String getStringProperty (Properties properties, String key, 
			String defaultValue)
	{
		String result = defaultValue;
		
		if (properties != null) { 
			String value = properties.getProperty(key);
			
			if (value != null) {
				result = value;
			}
		}
				
		return result;
	}
}
