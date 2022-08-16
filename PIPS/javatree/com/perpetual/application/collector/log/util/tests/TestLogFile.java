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

package com.perpetual.application.collector.log.util.tests;

import com.perpetual.application.collector.log.util.LogFile;

import junit.framework.TestCase;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestLogFile extends TestCase {

	/**
	 * Constructor for TestLogFile.
	 * @param arg0
	 */
	public TestLogFile(String arg0) {
		super(arg0);
	}
	
	public void testDeriveHostFromLogFileName() throws Exception
	{
		String fileName = "myhost_XXX_YYY.log";
		
		String host = LogFile.deriveHostFromLogFileName(fileName);
		
		assertEquals("host name", "myhost", host);
	}
	
	public void testDeriveStartTimeStampFromLogFileName() throws Exception
	{
		String fileName = "myhost_XXX_YYY.log";
		
		String startTimestamp = LogFile.deriveStartTimeStampFromLogFileName(fileName);
		
		assertEquals("start timestamp", "XXX", startTimestamp);
	}

	public void testDeriveEndTimeStampFromLogFileName() throws Exception
	{
		String fileName = "myhost_XXX_YYY.log";
		
		String endTimestamp = LogFile.deriveEndTimeStampFromLogFileName(fileName);
		
		assertEquals("start timestamp", "YYY", endTimestamp);
	}

	public void testConstructAnnotatedLogFileName() throws Exception
	{
		long start = 1055340104250L - 500 * 1000;
		long end = 1055340104250L;
		
		String logFileName = LogFile.constructAnnotatedLogFileName("host", start, end);
		
		assertEquals("constructed log file name",
			"host_20030611-135324-250_20030611-140144-250.log", logFileName);
	}
	
	public void testConstructRawLogFileName() throws Exception
	{
		long start = 1055340104250L - 500 * 1000;
		long end = 1055340104250L;
	
		String logFileName = LogFile.constructRawLogFileName("host", start, end);
	
		assertEquals("constructed raw log file name",
			"host_20030611-135324-250_20030611-140144-250_raw.log", logFileName);
	}
	
	public void testDeriveStartTimeStampFromCurrentLogFileName() throws Exception
	{
		String fileName1 = "myhost_STARTTS.log";
		
		String startTS1 = LogFile.deriveStartTimeStampFromCurrentLogFileName(fileName1);
		
		assertEquals("start timestamp", "STARTTS", startTS1);
		
		String fileName2 = "myhost_STARTTS_raw.log";
		String startTS2 = LogFile.deriveStartTimeStampFromCurrentLogFileName(fileName1);
	
		assertEquals("start timestamp", "STARTTS", startTS2);
	}
}
