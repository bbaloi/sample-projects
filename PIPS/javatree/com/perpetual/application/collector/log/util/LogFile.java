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

package com.perpetual.application.collector.log.util;

import com.perpetual.util.GMTFormatter;

// must be thread-safe - use only locals and parameters

public class LogFile {


	// construct <HOST>_TS1_TS2.log, TSs are in GMT
	// in the format "yyyyMMdd-HHmmss-S", see Javadocs on SimpleDateFormat
	public static String constructAnnotatedLogFileName (String hostName,
			long startTimestamp, long endTimestamp)
	{
		GMTFormatter format = new GMTFormatter();
		
		return hostName
			+ "_" + format.convertToGMT(startTimestamp)
			+ "_" + format.convertToGMT(endTimestamp)
			+ ".log";
	}
	
	public static String constructRawLogFileName (String hostName,
			long startTimestamp, long endTimestamp)
	{
		GMTFormatter format = new GMTFormatter();
		
		return hostName
			+ "_" + format.convertToGMT(startTimestamp)
			+ "_" + format.convertToGMT(endTimestamp)
			+ "_raw.log";
	}
	
	
	public static String deriveHostFromLogFileName (String logFileName)
	{
		int index;
		
		index = logFileName.indexOf('_');
		
		if (index == -1) {
			index = logFileName.indexOf(".log");
		}
		
		return logFileName.substring(0, index);
	}

	public static String deriveStartTimeStampFromCurrentLogFileName (
		String logFileName)
	{
//System.out.println("log file " + logFileName);		
		int indexFirstUnderscore = logFileName.indexOf('_');
	
		String restOfName = logFileName.substring(indexFirstUnderscore + 1);
		
		int endIndex = restOfName.indexOf('_');
		
		if (endIndex == -1) {
			endIndex = restOfName.indexOf(".log");
		}
		
		return restOfName.substring(0, endIndex);
	}
	
	public static String deriveStartTimeStampFromLogFileName (String logFileName)
	{
		int indexFirstUnderscore = logFileName.indexOf('_');
	
		String restOfName = logFileName.substring(indexFirstUnderscore + 1);
		
		return restOfName.substring(0, restOfName.indexOf('_'));
	}

	public static String deriveEndTimeStampFromLogFileName (String logFileName)
	{
		int indexFirstUnderscore = logFileName.indexOf('_');

		String restOfName = logFileName.substring(indexFirstUnderscore + 1);
		
		int indexSecondUnderscore = restOfName.indexOf('_');
	
		String restOfName2 = restOfName.substring(indexSecondUnderscore + 1);
	
		return restOfName2.substring(0, restOfName2.indexOf(".log"));
	}
	
	public static boolean isRawLogFile (String logFileName)
	{
		return logFileName.indexOf("_raw.log") > -1;
	}
}
