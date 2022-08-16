package com.perpetual.viewer.model.vo;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.struts.action.ActionForm;


public class ViewGeneralSummaryActionVO extends ActionForm
{
    private Collection m_hostList, m_facilities, m_severities;
    private int	m_startYear, m_startMonth, m_startDay, m_startHour, m_startMinute,
			m_endYear, m_endMonth, m_endDay, m_endHour, m_endMinute;
    private String m_timeZone;
    private int m_page;

    public void setHostList(Collection hostList)
	{
		m_hostList = hostList;
	}
	public Collection getHostList()
	{
		return m_hostList;
	}
	public void setFacilities(Collection facilities)
	{
		m_facilities = facilities;
	}
	public Collection getFacilities()
	{
		return m_facilities;
	}
	public void setSeverities(Collection severities)
	{
		m_severities = severities;
	}
	public Collection getSeverities()
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
	{/*
		return "ViewLogstoreActionForm" +
				" hostList=" + (m_hostList != null ? Arrays.asList(m_hostList) : null) +
				" facilities=" + (m_facilities != null ? Arrays.asList(m_facilities) : null) +
				" severities=" + (m_severities != null ? Arrays.asList(m_severities) : null) +
				" page=" + m_page;
          **/
            return null;
	}

    
}



