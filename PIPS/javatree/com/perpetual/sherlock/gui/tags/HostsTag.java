//package com.perpetual.sherlock.gui.tags;
//
///**
// * @author Mike Pot - http://www.mikepot.com
// *
// * Use is subject to license terms.
// * This file is provided with no warranty whatsoever.
// */
//
//import java.io.*;
//import java.util.*;
//import javax.servlet.jsp.*;
//import javax.servlet.jsp.tagext.*;
//
//import com.perpetual.log.LogDatabase;
//import com.perpetual.log.LogFile;
//import com.perpetual.sherlock.gui.beans.Boss;
//
//
//public class HostsTag extends BodyTagSupport
//{
//	public int doStartTag() throws JspException
//	{
//		try
//		{
//			m_boss = (Boss)pageContext.getAttribute("boss", PageContext.APPLICATION_SCOPE);
//			m_logDatabase = m_boss.getLogDatabase();
//			m_hostIterator = m_logDatabase.iterateHosts();
//			if (!m_hostIterator.hasNext())
//				return SKIP_BODY;
//			m_host = (String)m_hostIterator.next();
//			return EVAL_BODY_INCLUDE;
//		}
//		catch (Throwable ex)
//		{
//			throw new JspException(ex);
//		}
//	}
//
//	public int doAfterBody() throws JspException
//	{
//		try
//		{
//			if (!m_hostIterator.hasNext())
//				return SKIP_BODY;
//			m_host = (String)m_hostIterator.next();
//			return EVAL_BODY_TAG;
//		}
//		catch (Throwable ex)
//		{
//			throw new JspException(ex);
//		}
//	}
//
//	public String getHost() { return m_host; }
//
//	private Boss m_boss;
//	private LogDatabase m_logDatabase;
//	private Iterator m_hostIterator;
//	private String m_host;
//}
//
//
//
