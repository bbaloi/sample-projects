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


public class BossSessionDebug extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();
			BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
			Map parameterMap = bossSession.getParameterMap();

			out.println("bosslogdir=" + System.getProperty("bosslogdir") + "<br>");
			for (Iterator i = parameterMap.entrySet().iterator(); i.hasNext(); )
			{
				Map.Entry entry = (Map.Entry)i.next();
				String name = (String)entry.getKey();
				Object value = entry.getValue();
				out.print("name=" + name + " value=");
				if (value instanceof Object[])
				{
					Object[] values = (Object[])value;
					for (int j = 0; j < values.length; j++)
					{
						if (j > 0)
							out.print(", ");
						out.print(values[j]);
					}
				}
				else
					out.print(value);
				out.println("<br>");
			}
			return SKIP_BODY;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}
}


