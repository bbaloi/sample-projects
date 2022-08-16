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
//import com.perpetual.log.LogFile;
//import com.perpetual.sherlock.gui.beans.BossSession;
//
//
//public class HostTag extends BodyTagSupport
//{
//	public int doStartTag() throws JspException
//	{
//		try
//		{
//			JspWriter out = pageContext.getOut();
//			BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
//			Map parameterMap = bossSession.getParameterMap();
//			HostsTag hostsTag = (HostsTag)TagSupport.findAncestorWithClass(this, HostsTag.class);
//			String host = hostsTag.getHost();
//			if (m_html != null)
//			{
//				out.print("value=\"" + host + "\"");
//				Object value = parameterMap.get("host");
//				if (value instanceof String[])
//				{
//					String[] selectedHosts = (String[])value;
//					for (int i = 0; i < selectedHosts.length; i++)
//					{
//						if (host.equals(selectedHosts[i]))
//							out.print(" selected");
//					}
//				}
//			}
//			else
//				out.println(host);
//			return EVAL_BODY_INCLUDE;
//		}
//		catch (Throwable ex)
//		{
//			throw new JspException(ex);
//		}
//	}
//
//	public void setHtml(String html) { m_html = html; }
//	public String getHtml() { return m_html; }
//
//	private String m_html;
//}
//
//
//
