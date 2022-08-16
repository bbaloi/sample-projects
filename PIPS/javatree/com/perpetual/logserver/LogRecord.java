package com.perpetual.logserver;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import com.perpetual.util.PerpetualC2Logger;


public class LogRecord implements Comparable,java.io.Serializable
{
    
	public LogRecord(LogRecordFormat logRecordFormat, String line) throws Exception
	{
		m_logRecordFormat = logRecordFormat;
		m_line = line;
                m_fieldMap = m_logRecordFormat.parseLogRecord(m_line);
                
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		for (Iterator i = m_fieldMap.entrySet().iterator(); i.hasNext(); )
		{
			Map.Entry entry = (Map.Entry)i.next();
			buffer.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
		}
		return buffer.toString();
	}

	public Date getTime() throws Exception
	{
		LogRecordFormat.DateField field = (LogRecordFormat.DateField)getField("Time");
		return field.getDate();
	}

	public int compareTo(Object other)
	{
		LogRecordFormat.DateField time = (LogRecordFormat.DateField)m_fieldMap.get("Time"), otherTime = (LogRecordFormat.DateField)((LogRecord)other).m_fieldMap.get("Time");
		if (time == null)
			return otherTime != null ? 1 : m_line.compareTo(((LogRecord)other).m_line);
		if (otherTime == null)
			return -1;
		return time.getDate().compareTo(otherTime.getDate());
	}

	public String getLine() { return m_line; }

	public LogRecordFormat getLogRecordFormat()
	{
		return m_logRecordFormat;
	}

	public LogRecordFormat.Field getField(String name) throws Exception
	{
		LogRecordFormat.Field field = (LogRecordFormat.Field)m_fieldMap.get(name);
		if (field == null)
			throw new Exception("Can not find field named " + name);
		return field;
	}

	public Iterator iterateFields() throws Exception
	{
		return m_fieldMap.values().iterator();
	}

	public boolean matches(Map map) throws Exception
	{
           // sLog.debug("Matching Parameter Map:");
		for (Iterator i = m_fieldMap.values().iterator(); i.hasNext(); )
		{
			LogRecordFormat.Field field = (LogRecordFormat.Field)i.next();
                        if (!field.matches(map))
				return false;
		}
		return true;
	}

	private Map m_fieldMap;
	private LogRecordFormat m_logRecordFormat;
	private String m_line;
}


