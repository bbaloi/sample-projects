package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.struts.action.ActionForm;


public class LogScheduleForm extends ViewLogstoreActionForm
{
	public int getId()
	{
		return m_id;
	}
	public void setId(int id)
	{
		m_id = id;
	}
	public String getName()
	{
		return m_name;
	}
	public void setName(String name)
	{
		m_name = name;
	}
	public void setWhenYear(int whenYear)
	{
		m_whenYear = whenYear;
	}
	public int getWhenYear()
	{
		return m_whenYear;
	}
	public void setWhenMonth(int whenMonth)
	{
		m_whenMonth = whenMonth;
	}
	public int getWhenMonth()
	{
		return m_whenMonth;
	}
	public void setWhenDay(int whenDay)
	{
		m_whenDay = whenDay;
	}
	public int getWhenDay()
	{
		return m_whenDay;
	}
	public void setWhenHour(int whenHour)
	{
		m_whenHour = whenHour;
	}
	public int getWhenHour()
	{
		return m_whenHour;
	}
	public void setWhenMinute(int whenMinute)
	{
		m_whenMinute = whenMinute;
	}
	public int getWhenMinute()
	{
		return m_whenMinute;
	}
	public boolean getRepeat()
	{
		return m_repeat;
	}
	public void setRepeat(boolean repeat)
	{
		m_repeat = repeat;
	}
	public String getIncYear()
	{
		return m_incYear;
	}
	public void setIncYear(String incYear)
	{
		m_incYear = incYear;
	}
	public String getIncMonth()
	{
		return m_incMonth;
	}
	public void setIncMonth(String incMonth)
	{
		m_incMonth = incMonth;
	}
	public String getIncDay()
	{
		return m_incDay;
	}
	public void setIncDay(String incDay)
	{
		m_incDay = incDay;
	}
	public String getIncHour()
	{
		return m_incHour;
	}
	public void setIncHour(String incHour)
	{
		m_incHour = incHour;
	}
	public String getIncMinute()
	{
		return m_incMinute;
	}
	public void setIncMinute(String incMinute)
	{
		m_incMinute = incMinute;
	}

	public String toString()
	{
		return super.toString() +
				" name=" + m_name +
				" whenYear=" + m_whenYear +
				" whenMonth=" + m_whenMonth +
				" whenDay=" + m_whenDay +
				" whenHour=" + m_whenHour +
				" whenMinute=" + m_whenMinute +
				" repeat=" + m_repeat +
				" incYear=" + m_incYear +
				" incMonth=" + m_incMonth +
				" incDay=" + m_incDay +
				" incHour=" + m_incHour +
				" incMinute=" + m_incMinute;
	}

	private int m_id;
	private String m_name;
	private int m_whenYear, m_whenMonth, m_whenDay, m_whenHour, m_whenMinute;
	private boolean m_repeat;
	private String m_incYear, m_incMonth, m_incDay, m_incHour, m_incMinute;
}




