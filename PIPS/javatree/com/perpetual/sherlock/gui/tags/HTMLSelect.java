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

import com.perpetual.sherlock.gui.beans.BossSession;


public class HTMLSelect extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();
			BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
			Map parameterMap = bossSession.getParameterMap();

			out.print("value=\"" + m_value + "\"");
			Object value = parameterMap.get(m_attribute);
			if (value instanceof String[])
			{
				if (m_value.equals(((String[])value)[0]))
					out.print(" " + m_selected);
			}
			return EVAL_BODY_INCLUDE;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}

	public void setAttribute(String attribute) { m_attribute = attribute; }
	public String getAttribute() { return m_attribute; }
	public void setValue(String value) { m_value = value; }
	public String getValue() { return m_value; }
	public void setSelected(String selected) { m_selected = selected; }
	public String getSelected() { return m_selected; }

	private String m_attribute, m_value, m_selected = "selected";
}



