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


package com.perpetual.application.collector.message.tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestSyslogMessage extends TestCase {

	private static String TIMESTAMP = "Jun 21 14:45:14";
	private static String DEVICE_HOST_IP = "192.168.2.21";
	private static String COLLECTOR_HOST_IP = "192.168.2.5";
	private static String STRING_HOST = "ahost";
	private static String INVALID_HOST = "ahost:";
	private static String TAG_SPACE = "tag ";
	private static String TAG_PROCESS_PID = "processname[456]:";	
	private static String TAG_COLON = "tag:";
	private static String NON_ALPHA = "@";
	private static String CONTENT = "this is some content";
	private static String NO_SPACE_CONTENT = "this_is_some_content";
	
	private static String PRI_0_0 = "<0>";
	private static String PRI_20_5 = "<165>";
	
	private static String NICE_MESSAGE = PRI_0_0
			+ TIMESTAMP + " " + STRING_HOST + " " + TAG_PROCESS_PID + CONTENT;
	private static String ANOTHER_NICE_MESSAGE = PRI_20_5
				+ TIMESTAMP + " " + STRING_HOST + " " + TAG_PROCESS_PID + CONTENT;
	private static String SPACE_BETWEEN_TAG_AND_MESSAGE = PRI_20_5
					+ TIMESTAMP + " " + STRING_HOST + " " + TAG_SPACE + CONTENT;
					
	private static String COLON_BETWEEN_TAG_AND_MESSAGE = PRI_20_5
						+ TIMESTAMP + " " + STRING_HOST + " " + TAG_COLON + CONTENT;				
	private static String NON_ALPHA_BETWEEN_TAG_AND_MESSAGE = PRI_20_5
						+ TIMESTAMP + " " + STRING_HOST + " " + "tag"
						+ NON_ALPHA + NO_SPACE_CONTENT;
	private static String NO_VALID_PRI_MESSAGE = TAG_PROCESS_PID + CONTENT;
	private static String VALID_PRI_WITH_NO_TIMESTAMP_MESSAGE =
			PRI_20_5 + TAG_PROCESS_PID + CONTENT;
			
	private static String MESSAGE_WITH_INVALID_HOST = PRI_0_0
				+ TIMESTAMP + " " + INVALID_HOST + " " + TAG_COLON + CONTENT;		
	
	
	private QueueEntry entry;
			
	public TestSyslogMessage(String arg0) {
		super(arg0);
	}
			
	public void testNiceMessage () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();
		
		QueueEntry queueEntry = new QueueEntry(NICE_MESSAGE, DEVICE_HOST_IP,
			currentTimestamp, 1);
			
		SyslogMessage message = new SyslogMessage(queueEntry);
		
		assertEquals("facility", 0, message.getFacility());
		assertEquals("severity", 0, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", STRING_HOST, message.getHost());
		assertEquals("processname", "processname", message.getProcessName());
		assertEquals("processid", 456, message.getProcessId());
		assertEquals("content", CONTENT, message.getContent());
		
		assertFalse("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message", NICE_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getRawMessage(),
			message.getStringifiedMessage());		
	}

	public void testNiceMessageWithDifferentPRI () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();
	
		QueueEntry queueEntry = new QueueEntry(ANOTHER_NICE_MESSAGE, DEVICE_HOST_IP,
			currentTimestamp, 1);
		
		SyslogMessage message = new SyslogMessage(queueEntry);
	
		assertEquals("facility", 20, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", STRING_HOST, message.getHost());
		assertEquals("processname", "processname", message.getProcessName());
		assertEquals("processid", 456, message.getProcessId());
		assertEquals("content", CONTENT, message.getContent());
		
		assertFalse("rfc rules applied", message.isRfcRulesApplied());
		assertEquals("serialized message", ANOTHER_NICE_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getRawMessage(),
			message.getStringifiedMessage());		
	}

	public void testWithSpaceBetweenTagAndMessage () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();
	
		QueueEntry queueEntry = new QueueEntry(SPACE_BETWEEN_TAG_AND_MESSAGE,
			DEVICE_HOST_IP, currentTimestamp, 1);
		
		SyslogMessage message = new SyslogMessage(queueEntry);
	
		assertEquals("facility", 20, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", STRING_HOST, message.getHost());
		assertEquals("tag", "tag", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
		assertEquals("content", " " + CONTENT, message.getContent());
		
		assertFalse("rfc rules applied", message.isRfcRulesApplied());
		assertEquals("serialized message", SPACE_BETWEEN_TAG_AND_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getRawMessage(),
			message.getStringifiedMessage());		
	}
	
	public void testWithColonBetweenTagAndMessage () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();

		QueueEntry queueEntry = new QueueEntry(COLON_BETWEEN_TAG_AND_MESSAGE,
			DEVICE_HOST_IP, currentTimestamp, 1);
	
		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 20, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", STRING_HOST, message.getHost());
		assertEquals("tag", "tag", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
		
		assertEquals("content", ":" + CONTENT, message.getContent());
		
		assertFalse("rfc rules applied", message.isRfcRulesApplied());
		assertEquals("serialized message", COLON_BETWEEN_TAG_AND_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getRawMessage(),
			message.getStringifiedMessage());		
	}
	
	public void testWithNonAlphaBetweenTagAndMessage () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();

		QueueEntry queueEntry = new QueueEntry(NON_ALPHA_BETWEEN_TAG_AND_MESSAGE,
			DEVICE_HOST_IP, currentTimestamp, 1);
	
		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 20, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", STRING_HOST, message.getHost());
		assertEquals("tag", "tag", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
		
		assertEquals("content", NON_ALPHA + NO_SPACE_CONTENT, message.getContent());
		
		assertFalse("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message", NON_ALPHA_BETWEEN_TAG_AND_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getRawMessage(),
			message.getStringifiedMessage());		
	}
	
	public void testNoValidPRI () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
			Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));

		QueueEntry queueEntry = new QueueEntry(NO_VALID_PRI_MESSAGE,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 1, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertNull("tag", message.getTag());
		assertEquals("process name", "processname", message.getProcessName());
		assertEquals("processid", 456, message.getProcessId());
	
		assertEquals("content", CONTENT, message.getContent());
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
		assertEquals("serialized message",
				"<13>" + expectedTimestamp + " " + DEVICE_HOST_IP
				+ " " + NO_VALID_PRI_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());
	}
	
	public void testValidPRIWithNoTimestamp () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
			Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));

		QueueEntry queueEntry = new QueueEntry(VALID_PRI_WITH_NO_TIMESTAMP_MESSAGE,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 20, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertNull("tag", message.getTag());
		
		assertEquals("process name", "processname", message.getProcessName());
		assertEquals("processid", 456, message.getProcessId());

		assertEquals("content", CONTENT, message.getContent());
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());

		assertEquals("serialized message",
				"<165>" + expectedTimestamp + " " + DEVICE_HOST_IP
				+ " " + NO_VALID_PRI_MESSAGE,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());
	}
	
	public void testWithInvalidHost () throws Exception
	{
		long currentTimestamp = System.currentTimeMillis();

		QueueEntry queueEntry = new QueueEntry(MESSAGE_WITH_INVALID_HOST,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 0, message.getFacility());
		assertEquals("severity", 0, message.getSeverity());
		assertEquals("timestamp", TIMESTAMP, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertEquals("tag", "ahost", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
			
		assertEquals("content", ": " + TAG_COLON + CONTENT, message.getContent());
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());

		assertEquals("serialized message",
				"<0>" + TIMESTAMP + " " + DEVICE_HOST_IP + " "
				+ "ahost: " + TAG_COLON + CONTENT,
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());
	}

	// the following are some tests with real syslog messages
	
	public void testWithCrondMessage () throws Exception 
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
				Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));
		
		// this is case where there is no timestamp or host
		String rawMessage = "<78>CROND[16114]: (mail) CMD (/usr/bin/python -S /var/lib/mailman/cron/qrunner)";
		
		QueueEntry queueEntry = new QueueEntry(rawMessage,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 9, message.getFacility());
		assertEquals("severity", 6, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertNull("tag", message.getTag());
		assertEquals("process name", "CROND", message.getProcessName());
		assertEquals("processid", 16114, message.getProcessId());
			
		assertEquals("content", " (mail) CMD (/usr/bin/python -S /var/lib/mailman/cron/qrunner)", message.getContent());
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message",
				"<78>" + expectedTimestamp + " " + DEVICE_HOST_IP + " "
				+ "CROND[16114]: (mail) CMD (/usr/bin/python -S /var/lib/mailman/cron/qrunner)",
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());
	 
	}

	public void testWithSendmailMessage () throws Exception 
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
				Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));
		
		// this is case where there is no host
		String rawMessage = "<22>Jun 26 12:02:38 sendmail[23292]: [ID 801593 mail.info] h5QG2YLj023291: h5QG2cLj023292: clone: owner=owner-whine";
		
		QueueEntry queueEntry = new QueueEntry(rawMessage,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 2, message.getFacility());
		assertEquals("severity", 6, message.getSeverity());
		assertEquals("timestamp", "Jun 26 12:02:38", message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertNull("tag", message.getTag());
		assertEquals("process name", "sendmail", message.getProcessName());
		assertEquals("processid", 23292, message.getProcessId());
			
		assertEquals("content",
				" [ID 801593 mail.info] h5QG2YLj023291: h5QG2cLj023292: clone: owner=owner-whine", 
				message.getContent());
				
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message",
				"<22>Jun 26 12:02:38" + " " + DEVICE_HOST_IP + " "
				+ "sendmail[23292]: [ID 801593 mail.info] h5QG2YLj023291: h5QG2cLj023292: clone: owner=owner-whine",
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());		
	}

	public void testWithGconfdMessage () throws Exception 
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
				Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));
		
		// this is case where there is no host
		String rawMessage = "<14>gconfd (michael-26621): starting (version 2.2.0), pid 26621 user 'michael'";
		
		QueueEntry queueEntry = new QueueEntry(rawMessage,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 1,message.getFacility());
		assertEquals("severity", 6, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertEquals("tag", "gconfd (michael-26621)", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());

			
		assertEquals("content",
				": starting (version 2.2.0), pid 26621 user 'michael'", 
				message.getContent());
				
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message",
				"<14>" + expectedTimestamp + " " + DEVICE_HOST_IP + " "
				+ "gconfd (michael-26621): starting (version 2.2.0), pid 26621 user 'michael'",
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());					 
	}

	public void testWithJustPRIAndColon () throws Exception 
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
				Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));
		
		// this is case where there is no host
		String rawMessage = "<189>:";

		QueueEntry queueEntry = new QueueEntry(rawMessage,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 23, message.getFacility());
		assertEquals("severity", 5, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertEquals("tag", "", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
		
		assertEquals("content", ":", message.getContent());	
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
		
		assertEquals("serialized message",
				"<189>" + expectedTimestamp + " " + DEVICE_HOST_IP + " "
				+ ":",
				message.getSerializedMessage());
		assertEquals("stringified message", message.getSerializedMessage(),
			message.getStringifiedMessage());		
	}
	
	public void testWithCiscoMessage () throws Exception 
	{
		long currentTimestamp = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
				Locale.US);
		String expectedTimestamp = dateFormat.format(new Date(currentTimestamp));
		
		// this is case where there is no host
		String rawMessage = "<183>533: 2w1d: IP ARP: creating entry for IP address: 10.20.48.205, hw: 0800.20f0.eb87";

		QueueEntry queueEntry = new QueueEntry(rawMessage,
			DEVICE_HOST_IP, currentTimestamp, 1);

		SyslogMessage message = new SyslogMessage(queueEntry);

		assertEquals("facility", 22, message.getFacility());
		assertEquals("severity", 7, message.getSeverity());
		assertEquals("timestamp", expectedTimestamp, message.getTimestamp());
		assertEquals("host", DEVICE_HOST_IP, message.getHost());
		assertEquals("tag", "533", message.getTag());
		assertNull("process name", message.getProcessName());
		assertEquals("processid", 0, message.getProcessId());
		
		assertEquals("content",
				": 2w1d: IP ARP: creating entry for IP address: 10.20.48.205, hw: 0800.20f0.eb87",
				message.getContent());	
		
		assertTrue("rfc rules applied", message.isRfcRulesApplied());
	}	
}
