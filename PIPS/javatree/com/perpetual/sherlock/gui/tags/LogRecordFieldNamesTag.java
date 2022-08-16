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


public class LogRecordFieldNamesTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		try
		{
			JspWriter out = pageContext.getOut();
			return EVAL_BODY_INCLUDE;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}
}




