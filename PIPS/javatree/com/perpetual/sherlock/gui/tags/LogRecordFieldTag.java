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
import com.perpetual.log.LogRecord;
import com.perpetual.sherlock.gui.beans.Boss;
import com.perpetual.sherlock.gui.beans.BossSession;


public class LogRecordFieldTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		m_boss = (Boss)pageContext.getAttribute("boss", PageContext.APPLICATION_SCOPE);
		m_bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
		m_logRecordFormat = m_boss.getLogRecordFormat();
		m_logRecordFieldsTag = (LogRecordFieldsTag)TagSupport.findAncestorWithClass(this, LogRecordFieldsTag.class);
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
		JspWriter out = pageContext.getOut();
		LogRecord logRecord = m_logRecordFieldsTag.getLogRecord();
		LogRecordFormat.Field field = logRecord.getField(m_fieldName);

		if ("Time".equals(m_fieldName))
		{
			Date maxTimeSeen = ((LogRecordFormat.TimeField)field).getTime();
			m_bossSession.setMaxTimeSeen(maxTimeSeen);
			out.print(m_bossSession.dateToCompactString(maxTimeSeen));
		}
		else
			out.print(field);
	}

	private Boss m_boss;
	private BossSession m_bossSession;
	private LogRecordFormat m_logRecordFormat;
	private LogRecordFieldsTag m_logRecordFieldsTag;
	private Iterator m_fieldNameIterator;
	private String m_fieldName;
}


