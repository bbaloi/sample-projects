package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;
//import java.rmi.*;
import java.text.*;
//import javax.ejb.*;

import org.apache.log4j.Logger;
import org.jdom.*;

import com.perpetual.util.ResourceLoader;


public class LogSystem
{
	private LogSystem() throws Exception
	{
		m_config = ResourceLoader.loadResourceAsJdomElement("config.xml");
		String logDir = System.getProperty("syslogdir");
		m_syslogDir = logDir != null ? new File(logDir) : new File(getParam("SysLogDir"));
		if (!m_syslogDir.exists())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " not found at all");
		if (!m_syslogDir.isDirectory())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " is not a directory");

		m_logDatabase = new DiskLogDatabase(m_syslogDir, m_config.getChild("disklogdatabase"));
		m_logDatabase.setAutoRefresh(true);
	}

	public DiskLogDatabase getLogDatabase() { return m_logDatabase; }

	public String getParam(String name) throws Exception
	{
		for (Iterator i = m_config.getChild("params").getChildren("param").iterator(); i.hasNext(); )
		{
			Element param = (Element)i.next();
			if (name.equals(param.getAttributeValue("name")))
				return param.getAttributeValue("value");
		}
		throw new Exception("Param not found: " + name);
	}

	private Element m_config;
	private File m_syslogDir;
	private DiskLogDatabase m_logDatabase;

	private static Logger sLog = Logger.getLogger(LogSystem.class);
	private static LogSystem m_default;

	public static LogSystem getDefault() throws Exception
	{
		if (m_default == null)		// might not need to synchronize
		{
			synchronized (LogSystem.class)
			{
				if (m_default == null)		// now that we're synchronized, we check again safely
					m_default = new LogSystem();
			}
		}
		return m_default;
	}
}


