package com.perpetual.sherlock.gui.beans;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.io.File;
import java.util.Iterator;

import org.jdom.Element;

import com.perpetual.log.DiskLogDatabase;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.LogRecordFormat;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ResourceLoader;


public class Boss
{
	public Boss() throws Exception
	{
		m_config = ResourceLoader.loadResourceAsJdomElement("config.xml");
		String logDir = System.getProperty("syslogdir");
		m_syslogDir = logDir != null ? new File(logDir) : new File(System.getProperty("java.io.tmpdir"), "syslog");
		if (!m_syslogDir.exists())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " not found at all");
		if (!m_syslogDir.isDirectory())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " is not a directory");
		m_logDatabase = new DiskLogDatabase(m_syslogDir, m_config.getChild("disklogdatabase"));
	}

	public LogDatabase getLogDatabase() { return m_logDatabase; }
	public LogRecordFormat getLogRecordFormat() { return m_logDatabase.getLogRecordFormat(); }

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

	private File m_syslogDir;
	private DiskLogDatabase m_logDatabase;
	private Element m_config;

	private static PerpetualC2Logger sLog = new PerpetualC2Logger(Boss.class);
}


