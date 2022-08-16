package com.perpetual.systeminit;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import com.perpetual.logreport.scheduler.Scheduler;
import com.perpetual.logserver.LogSystem;


public class StartServlet extends HttpServlet
{
    private LogSystem m_logSystem=null;
    
	public void init() throws ServletException
	{
		super.init();
		sLog.info("Initialisation Servlet is starting default LogBOSS coponents...");
		try
		{
                        sLog.debug("1) Intialising LogStore Database...");
                        //m_logSystem = LogSystem.getDefault(true);
                        m_logSystem = LogSystem.getDefault();
                        sLog.debug("2) Intialising Scheduler...");
			m_scheduler = new Scheduler();                       
			m_scheduler.start();
		}
		catch (Throwable ex)
		{
			throw new ServletException("Could not start scheduler", ex);
		}
	}

	public void destroy()
	{
		sLog.debug("StartServlet is shutting down the log scheduler");
		try
		{
			m_scheduler.stop();
			m_scheduler = null;
		}
		catch (Throwable ex)
		{
			sLog.error("Could not stop scheduler", ex);
		}
		super.destroy();
	}

	private Scheduler m_scheduler;

	private Logger sLog = Logger.getLogger(StartServlet.class);
}


