package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.struts.action.ActionForm;


public class ViewLogstoreActionForm extends ActionForm
{
	public void setHostList(String[] hostList)
	{
		m_hostList = hostList;
	}
	public String[] getHostList()
	{
		return m_hostList;
	}
        public void setMessagePatternList(String[] messagePatternList)
	{
		m_messagePatternList = messagePatternList;
	}
	public String[] getMessagePatternList()
	{
		return m_messagePatternList;
	}
	public void setFacilities(String[] facilities)
	{
		m_facilities = facilities;
	}
	public String[] getFacilities()
	{
		return m_facilities;
	}
	public void setSeverities(String[] severities)
	{
		m_severities = severities;
	}
	public String[] getSeverities()
	{
		return m_severities;
	}
	public void setStartYear(int startYear)
	{
		m_startYear = startYear;
	}
	public int getStartYear()
	{
		return m_startYear;
	}
	public void setStartMonth(int startMonth)
	{
		m_startMonth = startMonth;
	}
	public int getStartMonth()
	{
		return m_startMonth;
	}
	public void setStartDay(int startDay)
	{
		m_startDay = startDay;
	}
	public int getStartDay()
	{
		return m_startDay;
	}
	public void setStartHour(int startHour)
	{
		m_startHour = startHour;
	}
	public int getStartHour()
	{
		return m_startHour;
	}
	public void setStartMinute(int startMinute)
	{
		m_startMinute = startMinute;
	}
	public int getStartMinute()
	{
		return m_startMinute;
	}
	public void setEndYear(int endYear)
	{
		m_endYear = endYear;
	}
	public int getEndYear()
	{
		return m_endYear;
	}
	public void setEndMonth(int endMonth)
	{
		m_endMonth = endMonth;
	}
	public int getEndMonth()
	{
		return m_endMonth;
	}
	public void setEndDay(int endDay)
	{
		m_endDay = endDay;
	}
	public int getEndDay()
	{
		return m_endDay;
	}
	public void setEndHour(int endHour)
	{
		m_endHour = endHour;
	}
	public int getEndHour()
	{
		return m_endHour;
	}
	public void setEndMinute(int endMinute)
	{
		m_endMinute = endMinute;
	}
	public int getEndMinute()
	{
		return m_endMinute;
	}
	public void setTimeZone(String timeZone)
	{
		m_timeZone = timeZone;
	}
	public String getTimeZone()
	{
		return m_timeZone;
	}
	public void setPage(int page)
	{
		m_page = page;
	}
	public int getPage()
	{
		return m_page;
	}

	public String toString()
	{
		return "ViewLogstoreActionForm" +
				" hostList=" + (m_hostList != null ? Arrays.asList(m_hostList) : null) +
				" facilities=" + (m_facilities != null ? Arrays.asList(m_facilities) : null) +
				" severities=" + (m_severities != null ? Arrays.asList(m_severities) : null) +
				" page=" + m_page +
				" timeZone=" + m_timeZone +
				" startYear=" + m_startYear +
				" startMonth=" + m_startMonth +
				" startDay=" + m_startDay +
				" startHour=" + m_startHour +
				" startMinute=" + m_startMinute +
				" endYear=" + m_endYear +
				" endMonth=" + m_endMonth +
				" endDay=" + m_endDay +
				" endHour=" + m_endHour +
				" endMinute=" + m_endMinute;
	}

    private String[] m_hostList, m_facilities, m_severities,m_messagePatternList;
    private int
			m_startYear, m_startMonth, m_startDay, m_startHour, m_startMinute,
			m_endYear, m_endMonth, m_endDay, m_endHour, m_endMinute;
	private String m_timeZone;
	private int m_page;
}



