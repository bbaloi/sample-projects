package com.perpetual.logserver;

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


public class LogSystem implements java.io.Serializable
{
	private LogSystem() throws Exception
	{
		m_config = ResourceLoader.loadResourceAsJdomElement("config.xml");
		String logDir = System.getProperty("syslogdir");
		m_syslogDir = logDir != null ? new File(logDir) : new File(getParam("SysLogDir"));
		linesPerPage = Integer.parseInt(getParam("LogReportLinesPerPage"));
                                if (!m_syslogDir.exists())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " not found at all");
		if (!m_syslogDir.isDirectory())
			throw new Exception("sherlocklogdir directory " + m_syslogDir + " is not a directory");
                
                 String s_m_multithreaded = m_config.getChild("disklogdatabase").getChild("multithreaded").getAttributeValue("value");
                 if(s_m_multithreaded.equals("yes"))
                     m_multithreaded=true;
                 else
                     m_multithreaded=false;
                 
                if(m_multithreaded)
                {
                    sLog.info("Starting Logstore Database in multi-threaded mode !");
                    m_logDatabase = new DiskLogDatabase(m_syslogDir, m_config.getChild("disklogdatabase"),true,linesPerPage);
                }
                else
                {
                    sLog.info("Starting Logstore Database in single-threaded mode !");                   
                    m_logDatabase = new DiskLogDatabase(m_syslogDir, m_config.getChild("disklogdatabase"));
                }
                m_logDatabase.setAutoRefresh(true);
	}
        public static boolean isMultiThreaded()
        {
            return m_multithreaded;
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
        private static boolean m_multithreaded=false;
        private static int linesPerPage=0;
        

        public static void setMultiThreaded(boolean pMultithreaded)
        {
            m_multithreaded = pMultithreaded;
        }
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
        public static LogSystem getDefault(boolean pMultithreaded) throws Exception
	{
		if (m_default == null)		// might not need to synchronize
		{
			synchronized (LogSystem.class)
			{
				if (m_default == null)
                                {       // now that we're synchronized, we check again safely
                                        if(pMultithreaded)
                                        {
                                            setMultiThreaded(true);
                                            m_default = new LogSystem();
                                        }
                                        else
                                        {
                                             setMultiThreaded(false);
                                            m_default = new LogSystem();
                                        }
                                }
			}
		}
		return m_default;
	}
}


