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

import org.jdom.*;

import com.perpetual.util.ResourceLoader;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.sherlock.gui.beans.Boss;
import com.perpetual.sherlock.gui.beans.BossSession;


public class LogRecordFieldsTag extends BodyTagSupport
{
	public int doStartTag() throws JspException
	{
		try
		{
			m_maxLines = Integer.parseInt(ResourceLoader.loadResourceAsJdomElement("config.xml").getChild("settings").getChild("fields").getAttributeValue("maxlines"));
			m_boss = (Boss)pageContext.getAttribute("boss", PageContext.APPLICATION_SCOPE);
			BossSession bossSession = (BossSession)pageContext.getAttribute("bossSession", PageContext.SESSION_SCOPE);
			m_logDatabase = m_boss.getLogDatabase();
			m_cursor = m_logDatabase.retrieveLogRecords(new LogFilter(bossSession.getParameterMap()));
			bossSession.holdCursor(m_cursor);
			if (m_cursor == null || m_cursor.nextLogRecord() == null)
				return SKIP_BODY;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doAfterBody() throws JspException
	{
		try
		{
			if (--m_maxLines <= 0) return SKIP_BODY;
//System.out.println(m_maxLines);
			return m_cursor != null && m_cursor.nextLogRecord() != null ? EVAL_BODY_AGAIN : SKIP_BODY;
		}
		catch (Throwable ex)
		{
			throw new JspException(ex);
		}
	}

	public LogRecord getLogRecord() { return m_cursor.currentLogRecord(); }

	private Boss m_boss;
	private LogDatabase m_logDatabase;
	private Cursor m_cursor;
	private int m_maxLines;
}


