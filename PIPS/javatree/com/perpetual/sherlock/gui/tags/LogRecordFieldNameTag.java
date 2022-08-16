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

import com.perpetual.log.LogRecordFormat;
import com.perpetual.sherlock.gui.beans.Boss;
import com.perpetual.sherlock.gui.beans.BossSession;


public class LogRecordFieldNameTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		m_boss = (Boss)pageContext.getAttribute("boss", PageContext.APPLICATION_SCOPE);
		m_bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);

		m_logRecordFormat = m_boss.getLogRecordFormat();
		m_fieldNameIterator = m_logRecordFormat.iterateFieldNames();
		m_fieldName = (String)m_fieldNameIterator.next();
		return EVAL_BODY_INCLUDE;
	}

	public int doAfterBody() throws JspException
	{
		if (m_fieldNameIterator.hasNext())
		{
			m_fieldName = (String)m_fieldNameIterator.next();
			return EVAL_BODY_TAG;
		}
		return SKIP_BODY;
	}

	public void printMeat() throws Exception
	{
		pageContext.getOut().println(m_bossSession.translateStringToken(m_fieldName));
	}

	private Boss m_boss;
	private BossSession m_bossSession;
	private LogRecordFormat m_logRecordFormat;
	private Iterator m_fieldNameIterator;
	private String m_fieldName;
}





