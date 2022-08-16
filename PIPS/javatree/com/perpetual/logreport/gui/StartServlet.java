package com.perpetual.logreport.gui;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import com.perpetual.logreport.scheduler.Scheduler;


public class StartServlet extends HttpServlet
{
	public void init() throws ServletException
	{
		super.init();
		/*sLog.debug("StartServlet is starting the log scheduler");
		try
		{
			m_scheduler = new Scheduler();
			m_scheduler.start();
		}
		catch (Throwable ex)
		{
			throw new ServletException("Could not start scheduler", ex);
		}
                 */
	}

	public void destroy()
	{
		/*sLog.debug("StartServlet is shutting down the log scheduler");
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
                 */
	}

	private Scheduler m_scheduler;

	private Logger sLog = Logger.getLogger(StartServlet.class);
}


