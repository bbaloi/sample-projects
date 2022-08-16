package com.perpetual.sherlock.gui.tags;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.io.*;
import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.perpetual.log.LogFile;
import com.perpetual.sherlock.gui.beans.BossSession;


public class TimeZoneTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
		m_timeZoneId = (String)bossSession.getFirstParameter("timezone");
//System.out.println("TZ doStartTag");
		Map map = new TreeMap();
		String[] ids = TimeZone.getAvailableIDs();
		for (int i = 0; i < ids.length; i++)
		{
			TimeZone zone = TimeZone.getTimeZone(ids[i]);
			Integer offset = new Integer(zone.getRawOffset());
			if (zone.getID().length() == 3 && !map.containsKey(offset))
			{
//System.out.println(zone.getID() + " " + offset);
				map.put(offset, zone);
			}
		}
		m_iterator = map.values().iterator();
//		return EVAL_BODY_INCLUDE;
		return EVAL_BODY_BUFFERED;
//		return super.doStartTag();
	}

	public void doInitBody()
	{
//		BodyContent c = getBodyContent();
//		System.out.println("!!!!!!!!! " + c.getString());
	}

	public int doAfterBody() throws JspException
	{
//System.out.println("TZ doAfterBody");
		try
		{
/*
			if (true)
			{
				BodyContent c = getBodyContent();
//				JspWriter out = c.getEnclosingWriter();
				JspWriter out = pageContext.getOut();
//				out.write(c.getString());
System.out.println(m_iterator.next());
				return m_iterator.hasNext() ? EVAL_BODY_TAG : SKIP_BODY;
			}
*/


			BodyContent c = getBodyContent();
			String raw = c.getString();
			if (m_raw == null) m_raw = raw;
//			c.clearBody();
			JspWriter out = c.getEnclosingWriter();
//			JspWriter out = pageContext.getOut();
			TimeZone zone = (TimeZone)m_iterator.next();
			int offset = zone.getRawOffset() / (1000 * 60 * 60);
			String timeZoneName;
			if (offset == 0)
			{
				timeZoneName = zone.getID() + " " + zone.getDisplayName();
			}
			else
			{
				String offsetText = Integer.toString(offset);
				if (offset >= 0) offsetText = "+" + offsetText;
				timeZoneName = "(GMT " + offsetText + ") "  + zone.getID() + " " + zone.getDisplayName();
			}
//System.out.println(timeZoneName);
			String body = m_raw.replaceAll("TIMEZONE_ID", zone.getID());
			body = body.replaceAll("TIMEZONE_NAME", timeZoneName);
			body = body.replaceAll("SELECTED", zone.getID().equals(m_timeZoneId) ? "selected" : "");
			out.write(body);
			return m_iterator.hasNext() ? EVAL_BODY_AGAIN : SKIP_BODY;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}

	private Iterator m_iterator;
	private String m_raw;
	private String m_timeZoneId;
}




