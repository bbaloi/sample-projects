package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;

import org.jdom.*;
import org.apache.log4j.Logger;


public class DiskLogFileStructure
{
	public DiskLogFileStructure(Element logFileStructure)
	{
		for (Iterator i = logFileStructure.getChildren("filenameformat").iterator(); i.hasNext(); )
		{
			m_filenameFormatList.add(new Format((Element)i.next()));
		}
		for (Iterator i = logFileStructure.getChildren("filetype").iterator(); i.hasNext(); )
		{
			m_fileTypes.add(new FileType((Element)i.next()));
		}
	}

	public FilenameProperties parseFileName(File file) throws Exception
	{
		for (Iterator i = m_filenameFormatList.iterator(); i.hasNext(); )
		{
			Format format = (Format)i.next();
			FilenameProperties props = format.parse(file);
			if (props != null)
			{
				if (props.isIgnore())
					return null;
				return props;
			}
		}
		return null;
	}

	private class FileType
	{
		private FileType(Element type)
		{
			m_name = type.getAttributeValue("name");
			m_pattern = Pattern.compile(type.getAttributeValue("pattern"));
			sLog.debug("New file type " + m_name + " matching " + m_pattern);
		}

		private String m_name;
		private Pattern m_pattern;
	}

	private class Format
	{
		private Format(Element format)
		{
			m_pattern = Pattern.compile(format.getAttributeValue("pattern"));
			String	host = format.getAttributeValue("host"),
					startDate = format.getAttributeValue("startdate"),
					endDate = format.getAttributeValue("enddate"),
					ignore = format.getAttributeValue("ignore"),
					dateFormat = format.getAttributeValue("dateformat");
			if (host != null) m_hostIndex = Integer.parseInt(host);
			if (startDate != null) m_startDateIndex = Integer.parseInt(startDate);
			if (endDate != null) m_endDateIndex = Integer.parseInt(endDate);
			if (ignore != null) m_ignore = new Boolean(ignore).booleanValue();
			if (dateFormat != null) {
				 m_dateFormat = new SimpleDateFormat(dateFormat);
				 m_dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			} 
		}

		private FilenameProperties parse(File file) throws Exception
		{
			Matcher matcher = m_pattern.matcher(file.getName());
			return matcher.matches() ? new FilenameProperties(matcher, this, file) : null;
		}

		private Pattern m_pattern;
		private int m_hostIndex = -1, m_startDateIndex = -1, m_endDateIndex = -1;
		private boolean m_ignore;
		private DateFormat m_dateFormat;
	}

	public class FilenameProperties
	{
		private FilenameProperties(Matcher matcher, Format format, File file) throws Exception
		{
			if (format.m_hostIndex >= 0) m_host = matcher.group(format.m_hostIndex);
			if (format.m_startDateIndex >= 0) m_startTime = format.m_dateFormat.parse(matcher.group(format.m_startDateIndex));
			if (format.m_endDateIndex >= 0) m_endTime = format.m_dateFormat.parse(matcher.group(format.m_endDateIndex));
			m_ignore = format.m_ignore;

			for (Iterator i = m_fileTypes.iterator(); i.hasNext(); )
			{
				FileType fileType = (FileType)i.next();
				if (fileType.m_pattern.matcher(file.getPath()).matches())
				{
					m_type = fileType.m_name;
					break;
				}
			}
		}

		public String toString() { return "host=" + m_host + " startTime=" + m_startTime + " endTime=" + m_endTime + " type=" + m_type; }
		public String getHost() { return m_host; }
		public Date getStartTime() { return m_startTime; }
		public Date getEndTime() { return m_endTime; }
		public boolean isIgnore() { return m_ignore; }
		public String getType() { return m_type; }

		private String m_host;
		private Date m_startTime, m_endTime;
		private boolean m_ignore;
		private String m_type;
	}

	private List m_filenameFormatList = new ArrayList(), m_fileTypes = new ArrayList();
	private static Logger sLog = Logger.getLogger(DiskLogFileStructure.class);
}



