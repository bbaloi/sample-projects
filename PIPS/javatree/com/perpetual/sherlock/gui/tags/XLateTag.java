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


public class XLateTag extends BodyTagSupport
{
	public int doAfterBody() throws JspException
	{
		try
		{
			BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
			BodyContent c = getBodyContent();
			String token = c.getString();
			JspWriter out = c.getEnclosingWriter();
			out.write(bossSession.translateStringToken(token));
			return super.doAfterBody();
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}
}



