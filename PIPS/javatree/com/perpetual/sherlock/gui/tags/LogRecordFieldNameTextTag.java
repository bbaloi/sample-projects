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


public class LogRecordFieldNameTextTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		try
		{
			LogRecordFieldNameTag tag = (LogRecordFieldNameTag)TagSupport.findAncestorWithClass(this, LogRecordFieldNameTag.class);
			tag.printMeat();
			return EVAL_BODY_INCLUDE;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}
}


