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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.log.util.ILoggableMessage;


public class SyslogMessage implements ILoggableMessage {

	private static final Logger LOGGER = Logger.getLogger(SyslogMessage.class);

	private static int TIMESTAMP_LENGTH = 15;
	private static List MONTHS = new ArrayList();
	private static InetAddress HOST_ADDRESS = null;
	
	static {
		MONTHS.add("Jan");
		MONTHS.add("Feb");
		MONTHS.add("Mar");
		MONTHS.add("Apr");
		MONTHS.add("May");
		MONTHS.add("Jun");
		MONTHS.add("Jul");
		MONTHS.add("Aug");
		MONTHS.add("Sep");
		MONTHS.add("Oct");
		MONTHS.add("Nov");
		MONTHS.add("Dec");
		
		try {
			HOST_ADDRESS = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
	}

	// used to keep track of position within message
	private class ParsePosition 
	{
		int position;
		
		public ParsePosition (int position)
		{
			this.position = position;
		}
		
		public int getPosition() {
			return position;
		}

		public void setPosition(int i) {
			position = i;
		}

	}
	
	private QueueEntry entry = null;
	
	// indicates if raw message was in a format that had to fixed up per RFC
	private boolean rfcRulesApplied = false;
	
	
	private String packetId;
	private String rawMessage;
	private long systemTimestamp;
	private String relayHost;
	
	// parsed values

	private String priority;		
	private int severity = -1;
	private int facility = -1;
	private String timestamp = null;
	private String host = null;
	private String tag = null;
	private String processName = null;
	private int processId = 0;
	private String content = null;
	
	private SyslogMessage (String rawMessage) {
		this.rawMessage = rawMessage;
		
		parseRawMessage();
	}
	
	public SyslogMessage (QueueEntry entry)
	{			
		this.entry = entry;
		this.rawMessage = entry.getRawMessage();

		parseRawMessage();
	}
	
	// return either the raw message or the one with the RFC rules applied
	public String getStringifiedMessage ()
	{
		String result;
		
		if (!this.rfcRulesApplied) {
			// no fix up was done, simply use the raw message
			result = this.rawMessage;
		} else {
			result = getSerializedMessage();
		}
		
		return result;
	}
	
	public String getSerializedMessage()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<" + this.priority + ">");
		sb.append(this.timestamp);
		sb.append(" ");
		sb.append(this.host);
		sb.append(" ");
		
		if (this.processName == null) {
			tag = getTag();
			sb.append(tag + getContent());
		} else {
			sb.append(processName + "[" + getProcessId()+ "]:");
			sb.append(getContent());
		}

		return sb.toString();	
	}
	
	private void parseRawMessage() {

		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("attempting to parse raw message = '" + this.rawMessage);
		}

		String rawMessage = new String(this.rawMessage);  // create a copy
		
		try {		
			parseRawMessage(rawMessage);

		} catch (StringIndexOutOfBoundsException e) {			
			LOGGER.error("error in the format of the syslog message: " + e);
		}
		
		this.packetId = constructPacketId();
				 
		this.systemTimestamp = this.entry.getTimeStamp();
		
		this.relayHost = this.entry.getSourceHost();
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("Parsed values:");
			LOGGER.debug("  packet id = " + this.packetId);
			LOGGER.debug("  system timestamp = " + this.systemTimestamp);
			LOGGER.debug("  relay host = " + this.relayHost);
			LOGGER.debug("  priority = " + this.priority);
			LOGGER.debug("  facility = " + this.facility);
			LOGGER.debug("  severity = " + this.severity);
			LOGGER.debug("  timestamp = " + this.timestamp);
			LOGGER.debug("  host = " + this.host);
			LOGGER.debug("  tag = " + this.tag);
			LOGGER.debug("  process name = " + this.processName);
			LOGGER.debug("  process id = " + this.processId);
			LOGGER.debug("  content = " + this.content);
		}
	}
	
	// check if it's a valid syslog packet
	// it should have <PRI> Mmm dd hh:mm:ss hostname ...
	// See RFC 3164

	private void parseRawMessage(String rawMessage)
	{
		ParsePosition position = new ParsePosition(0);

		String messageAfterPri;
		String messageAfterTimestamp;
		String messageAfterHost;
		String messageAfterTag;
		
		if (extractPri(rawMessage, position)) {
			// got a valid PRI
			// derive severity & facility from priority
	
			if (extractTimestamp(rawMessage, position)) {
				// got a valid timestamp
				
				extractHost(rawMessage, position);
				extractTagAndContent(rawMessage, position);

			} else {
				// not a valid timestamp
				// we have inserted a timestamp and host
				// don't even try to extract a host
				extractTagAndContent(rawMessage, position);
			}
		} else {
			// not a valid PRI
			// we have inserted a timestamp and host
			extractTagAndContent(rawMessage, position);
		}
	}
	
	private boolean extractPri (String rawMessage, ParsePosition position)
	{
		boolean validPri = false;
		int startingPosition = position.getPosition();
		String message = rawMessage.substring(startingPosition);
		int endPosition = -1;
		int startOfPri = message.indexOf('<');
		int endOfPri = message.indexOf('>');
		
		if (startOfPri == -1 || endOfPri == -1) {
			// no valid PRI
			// per instructions in RFC 3164:
			// Construct a PRI of 13, (facility = 1, severity = 5)
			constructDefaultPRI();
			constructDefaultTimestamp();
			constructDefaultHost();
			this.content = message;
			
			// continue parsing the rest of the message
			endPosition = startingPosition;
			validPri = false;
		} else {
		
			this.priority = message.substring(startingPosition + 1, endOfPri);
			
			int intPriority = -1;
			 
			try {
				intPriority = Integer.parseInt(this.priority);	
			} catch (NumberFormatException e) {
				LOGGER.error("cannot parse PRI integer from " + this.priority);
				throw e;
			}
			
			this.facility = intPriority / 8;
			this.severity = intPriority % 8;
			
			endPosition = endOfPri + 1;
			validPri = true;
		}
		
		position.setPosition(endPosition);
		
		return validPri;		
	}

	private boolean extractTimestamp (String rawMessage,ParsePosition position)
	{
		int startingPosition = position.getPosition();
		boolean validTimestamp = true;
		int endPosition;
		
		String timestamp = null;
		
		try {
			timestamp = rawMessage.substring(startingPosition, 
						startingPosition + TIMESTAMP_LENGTH);
			
		} catch (StringIndexOutOfBoundsException e) {
			validTimestamp = false;
		}
	
		if (validTimestamp && validateTimestamp(timestamp)) {
			endPosition =  startingPosition + TIMESTAMP_LENGTH + 1;
			this.timestamp = timestamp;
			validTimestamp = true;
		} else {
			// we don't have a valid timestamp
			// follow the instructions as in RFC 3164
			// that is, insert current time in the syslog date format
			// insert the host name as HOST
			// consider the rest of the message as CONTENT
			
			constructDefaultTimestamp();
			constructDefaultHost();
	
			// continue parsing rest of message
			endPosition = startingPosition;
			validTimestamp = false;
		}
		
		position.setPosition(endPosition);
		
		return validTimestamp;
	}
	
	private boolean extractHost (String rawMessage, ParsePosition position)
	{
		int startingPosition = position.getPosition();
		int endPosition = -1;
		boolean validHost = false;
		
		String message = rawMessage.substring(startingPosition);
		
		int spaceIndex = message.indexOf(' ');
		
		this.host = message.substring(0, spaceIndex);
	
		if (validateHostName(this.host)) {
			endPosition = startingPosition + 1 + spaceIndex;
			validHost = true;
		} else {
			//use relay host as host
			constructDefaultHost();
			
			// continue parsing rest of message
			endPosition = startingPosition;
			validHost = false;
		}
	
		position.setPosition(endPosition);
		
		return validHost;
	}
	
	private void extractTagAndContent (String rawMessage, ParsePosition position)
	{
		int startingPosition = position.getPosition();
		String message = rawMessage.substring(startingPosition);

		int endPosition = -1;
				
		// possible delimiters between TAG and CONTENT fields
		
		int leftBracketIndex = message.indexOf('[');
		int rightBracketIndex = message.indexOf(']');
		int rightBracketColonIndex = message.indexOf("]:");
		int spaceIndex = message.indexOf(' ');
		int colonIndex = message.indexOf(':');
		
		// check if tag is form process[pid]
		if (leftBracketIndex != -1 && rightBracketColonIndex != -1) {
			this.processName = message.substring(0, leftBracketIndex);
			try {
				this.processId = Integer.parseInt(
						message.substring(leftBracketIndex + 1,
							rightBracketColonIndex));
			} catch (NumberFormatException e) {
				LOGGER.error("cannot parse a numeric process id from " +
						message.substring(leftBracketIndex + 1,
							rightBracketColonIndex));
				this.processId = 0;		
			}
			endPosition = rightBracketColonIndex + 2;
			
		} else if (colonIndex != -1) {
			// check if of form tag: message
			this.tag = message.substring(0, colonIndex);
			endPosition = colonIndex;  // CONTENT includes colon and space
		}
		else if (spaceIndex != -1) {
			// check if of form tag<space>message
			this.tag = message.substring(0, spaceIndex);
			endPosition = spaceIndex; // CONTENT includes space
		} else {
			// search for first non-alphanumeric character
			// or first 32 characters
			
			int i = 0;
			while (i < message.length() && 
					Character.isLetterOrDigit(message.charAt(i)) && i < 32) {
				i++;		
			}
			
			this.tag = message.substring(0, i);
			
			endPosition = i;
		}
		
		this.content = message.substring(endPosition);
	}
	
	private boolean validateTimestamp (String timestamp)
	{		
		// this is all hardcoded but it's OK since syslog format 
		// won't change much
		boolean result = true;
		
		String month = null;
		int iday = -1;
		int ihour = -1;
		int iminute = -1;
		int isecond = -1;
		
		try {
			month = timestamp.substring(0, 3);
			String day = timestamp.substring(4, 6);
			iday = Integer.parseInt(day);
			String hour = timestamp.substring(7, 9);
			ihour = Integer.parseInt(hour);
			String minute = timestamp.substring(10, 12);
			iminute = Integer.parseInt(minute);
			String second = timestamp.substring(13);
			isecond = Integer.parseInt(second);

			if (LOGGER.isEnabledFor(Priority.DEBUG)) {
				LOGGER.debug("month = " + month + ", day = "
						+ day + ", hour = " + hour + ", minute = " + minute
						+ ", second = " + second);
			}
		
		} catch (NumberFormatException e) {			
			result = false;
		} catch (StringIndexOutOfBoundsException e) {			
			result = false;
		}
		
		
		if (result) {
			result = MONTHS.contains(month)
				&& (iday >= 1 && iday <= 31)
				&& (ihour >= 0 && ihour <= 23)
				&& (iminute >= 0 && iminute < 60)
				&& (isecond >= 0 && isecond < 60) 
				&& timestamp.charAt(3) == ' '
				&& timestamp.charAt(6) == ' '
				&& timestamp.charAt(9) == ':'
				&& timestamp.charAt(12) == ':';
		}

		return result;
		
	}
	
	// only certain characters are allowed in a host name
	private boolean validateHostName (String hostName)
	{
		return hostName.indexOf('\\') == -1 &&
				hostName.indexOf('_') == -1 &&
				hostName.indexOf('/') == -1 &&
				hostName.indexOf(':') == -1 &&
				hostName.indexOf('*') == -1 &&
				hostName.indexOf('?') == -1 &&
				hostName.indexOf('"') == -1 &&
				hostName.indexOf('<') == -1 &&
				hostName.indexOf('>') == -1 &&
				hostName.indexOf('|') == -1;
	}

	// in the case of a missing/invalid PRI construct per RFC 3164
	private void constructDefaultPRI() {
		
		this.priority = "13";
		this.facility = 1;
		this.severity = 5;
		
		this.rfcRulesApplied = true;
	}

	//	in the case of a missing/invalid timestamp construct per RFC 3164
	private void constructDefaultTimestamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss",
			Locale.US);
		
		long currentTime = -1;
		if (this.entry != null) {
			currentTime = this.entry.getTimeStamp();
		} else {
			this.host = null;
			currentTime = System.currentTimeMillis();
		}
		
		this.timestamp = dateFormat.format(new Date(currentTime));
		
		this.rfcRulesApplied = true;
	}
	
	private void constructDefaultHost() {
		this.host = this.entry.getSourceHost();
		
		this.rfcRulesApplied = true;
	}
	
	private String constructPacketId() {
		
		return HOST_ADDRESS.getHostAddress()
			+ "_" 
			+ this.entry.getTimeStamp()
			+ "_"
			+ this.entry.getPacketNumber();
	}
	
	public String getContent() {
		return content;
	}

	public QueueEntry getEntry() {
		return entry;
	}

	public int getFacility() {
		return facility;
	}

	public String getHost() {
		return host;
	}

	public String getPriority() {
		return priority;
	}

	public int getProcessId() {
		return processId;
	}

	public String getProcessName() {
		return processName;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public int getSeverity() {
		return severity;
	}

	public String getTag() {
		return tag;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getPacketId() {
		return packetId;
	}

	public long getSystemTimestamp() {
		return systemTimestamp;
	}

	public String getRelayHost() {
		return relayHost;
	}

	public boolean isRfcRulesApplied() {
		return rfcRulesApplied;
	}
}
