package com.perpetual.sherlock.gui.beans;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;
import java.text.*;
import javax.servlet.http.*;

import org.jdom.*;

import com.perpetual.util.ResourceLoader;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.DiskLogDatabase;
import com.perpetual.log.DiskLogFileStructure;
import com.perpetual.log.Cursor;


public class BossSession implements HttpSessionBindingListener
{
	public BossSession() throws Exception
	{
		m_timeZone = TimeZone.getTimeZone("GMT");
		m_coarseTimeFormat = new SimpleDateFormat("HHmm");
		m_coarseTimeFormat.setTimeZone(m_timeZone);
		Calendar cal = Calendar.getInstance(m_timeZone);
		m_parameterMap.put("minYear", new String[] { Integer.toString(cal.get(Calendar.YEAR)) });
		m_parameterMap.put("minMonth", new String[] { Integer.toString(cal.get(Calendar.MONTH)) });
		m_parameterMap.put("minDay", new String[] { Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) });
		m_parameterMap.put("minHour", new String[] { Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) });
		m_parameterMap.put("minMinute", new String[] { Integer.toString(cal.get(Calendar.MINUTE) / 5 * 5) });
		cal.add(Calendar.DAY_OF_MONTH, 1);
		m_parameterMap.put("maxYear", new String[] { Integer.toString(cal.get(Calendar.YEAR)) });
		m_parameterMap.put("maxMonth", new String[] { Integer.toString(cal.get(Calendar.MONTH)) });
		m_parameterMap.put("maxDay", new String[] { Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) });
		m_parameterMap.put("maxHour", new String[] { "00" });
		m_parameterMap.put("maxMinute", new String[] { "00" });
//		fixMinTime();
//		setLocale("en");
		m_parameterMap.put("locale", new String[] { "en" });
		m_parameterMap.put("timezone", new String[] { m_timeZone.getID() });
		processParameters();
	}

//session.setAttribute( org.apache.struts.Globals.LOCALE_KEY ,locale);		strutifying in progress
	public void setLocale(String language) throws Exception
	{
		if (m_locale != null && m_locale.getLanguage().equals(language))
			return;
		m_locale = new Locale(language);
		Element config = ResourceLoader.loadResourceAsJdomElement("locales.xml", "UTF-8");
		for (Iterator i = config.getChildren("locale").iterator(); i.hasNext(); )
		{
			Element elem = (Element)i.next();
			if ("true".equals(elem.getAttributeValue("default")))
				m_defaultLocaleElem = elem;
			if (language != null && language.equals(elem.getAttributeValue("language")))
				m_localeElem = elem;
		}
		setDateFormats();
	}

	private void setDateFormats()
	{
		m_fullDateFormat = new SimpleDateFormat(translateStringToken("fullDateTimeFormat"), m_locale);
		m_fullDateFormat.setTimeZone(m_timeZone);
		m_compactDateFormat = new SimpleDateFormat(translateStringToken("compactDateTimeFormat"), m_locale);
		m_compactDateFormat.setTimeZone(m_timeZone);
	}

	public String translateStringToken(String token)
	{
		if (m_localeElem != null)
			for (Iterator i = m_localeElem.getChildren("string").iterator(); i.hasNext(); )
			{
				Element string = (Element)i.next();
				if (string.getAttributeValue("token").equals(token))
				{
					String translated = string.getText();	//.trim();
					return translated.length() > 0 ? translated : token;
				}
			}
		if (m_defaultLocaleElem != null)
			for (Iterator i = m_defaultLocaleElem.getChildren("string").iterator(); i.hasNext(); )
			{
				Element string = (Element)i.next();
				if (string.getAttributeValue("token").equals(token))
				{
					String translated = string.getText();	//.trim();
					return translated.length() > 0 ? translated : token;
				}
			}
		return token;
	}

	public void setSessionAttributes(HttpServletRequest request) throws Exception
	{
		m_parameterMap.putAll(request.getParameterMap());
		processParameters();

		if (request.getParameter("nextPage") != null)
			m_parameterMap.put("startTime", m_maxTimeSeen);
		else
			m_parameterMap.put("startTime", m_parameterMap.get("minTime"));

System.out.println("startTime = " + m_parameterMap.get("startTime"));

/*
		Map map = request.getParameterMap();
		boolean fixMinTime = true;
		for (Iterator i = map.entrySet().iterator(); i.hasNext(); )
		{
			Map.Entry entry = (Map.Entry)i.next();
			String name = (String)entry.getKey(), values[] = (String[])entry.getValue();
			if ("minTime".equals(name))
			{
				if (values[0].length() > 0)
					m_parameterMap.put(name, m_coarseTimeFormat.parse(values[0]));
				else
				{
					fixMinTime = false;
					m_parameterMap.put("Time", m_maxTimeSeen);
				}
			}
			else if ("locale".equals(name))
			{
				setLocale(values[0]);
			}
			else
				m_parameterMap.put(name, values);
		}

		if (fixMinTime)
			fixMinTime();
*/
	}

	private void processParameters() throws Exception
	{
		String[] locale = (String[])m_parameterMap.get("locale");
		if (locale != null)
			setLocale(locale[0]);

		String[] minYear = (String[])m_parameterMap.get("minYear"), minMonth = (String[])m_parameterMap.get("minMonth"), minDay = (String[])m_parameterMap.get("minDay"),
				minHour = (String[])m_parameterMap.get("minHour"), minMinute = (String[])m_parameterMap.get("minMinute"),
				maxYear = (String[])m_parameterMap.get("maxYear"), maxMonth = (String[])m_parameterMap.get("maxMonth"), maxDay = (String[])m_parameterMap.get("maxDay"),
				maxHour = (String[])m_parameterMap.get("maxHour"), maxMinute = (String[])m_parameterMap.get("maxMinute");

		String[] timezone = (String[])m_parameterMap.get("timezone");
		if (timezone != null)
		{
			m_timeZone = TimeZone.getTimeZone(timezone[0]);
			setDateFormats();
		}

		Calendar cal = Calendar.getInstance(m_timeZone);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (minYear != null && minMonth != null && minDay != null && minHour != null && minMinute != null)
		{
			cal.set(Calendar.YEAR, Integer.parseInt(minYear[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(minMonth[0]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(minDay[0]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(minHour[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(minMinute[0]));

			m_parameterMap.put("minTime", cal.getTime());
System.out.println("!! minTime=" + cal.getTime());
		}
		if (maxYear != null && maxMonth != null && maxDay != null && maxHour != null && maxMinute != null)
		{
			cal.set(Calendar.YEAR, Integer.parseInt(maxYear[0]));
			cal.set(Calendar.MONTH, Integer.parseInt(maxMonth[0]));
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(maxDay[0]));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(maxHour[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(maxMinute[0]));

			m_parameterMap.put("maxTime", cal.getTime());
System.out.println("!! maxTime=" + cal.getTime());
		}
	}

	public void setMaxTimeSeen(Date maxTimeSeen)
	{
		m_maxTimeSeen = maxTimeSeen;
	}

/*
	private void fixMinTime()
	{
		Calendar cal2 = Calendar.getInstance(m_timeZone);
//		Calendar cal2 = Calendar.getInstance();

		String[] year = (String[])m_parameterMap.get("year"), month = (String[])m_parameterMap.get("month"), day = (String[])m_parameterMap.get("day");
		if (year != null && month != null && day != null)
			cal2.set(Integer.parseInt(year[0]), Integer.parseInt(month[0]), Integer.parseInt(day[0]));

		Date time = (Date)m_parameterMap.get("minTime");
//System.out.println("mintime was " + time);
		if (time != null)
		{
//System.out.println("!!!!!!! " + time);
			Calendar cal = Calendar.getInstance(m_timeZone);
//			Calendar cal = Calendar.getInstance();	//TimeZone.getTimeZone("GMT"));
			cal.setTime(time);
//			cal.setTimeZone(TimeZone.getTimeZone("GMT"));
//System.out.println(cal.get(Calendar.HOUR_OF_DAY));
			cal2.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
			cal2.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		}
		else
		{
			cal2.set(Calendar.HOUR_OF_DAY, 0);
			cal2.set(Calendar.MINUTE, 0);
		}
		cal2.set(Calendar.SECOND, 0);
		cal2.set(Calendar.MILLISECOND, 0);

//		cal2.setTimeZone(TimeZone.getTimeZone("GMT"));
//System.out.println(cal2.getTime());

		m_parameterMap.put("Time", cal2.getTime());
//System.out.println("mintime becomes " + cal2.getTime());
	}
*/

	public Map getParameterMap() { return m_parameterMap; }

	public Object getFirstParameter(String name)
	{
		Object value = m_parameterMap.get(name);
		return value instanceof Object[] ? ((Object[])value)[0] : value;
	}

	public void holdCursor(Cursor cursor)
	{
		if (m_cursor != null)
		{
//			System.out.println("Closing previous cursor");
			try
			{
				m_cursor.close();
			}
			catch (Throwable ex)
			{
				System.out.println("Could not close cursor " + m_cursor);
				ex.printStackTrace();
			}
		}
		m_cursor = cursor;
	}

	public String dateToFullString(Date date)
	{
		return date != null ? m_fullDateFormat.format(date) : "";
	}
	public String dateToCompactString(Date date)
	{
		return date != null ? m_compactDateFormat.format(date) : "";
	}

	public void valueBound(HttpSessionBindingEvent event)
	{
		System.out.println("valueBound " + this);
	}
	public void valueUnbound(HttpSessionBindingEvent event)
	{
		System.out.println("valueUnound " + this);
		holdCursor(null);
	}

	private Map m_parameterMap = new HashMap();
	private DateFormat m_coarseTimeFormat;
	private Date m_maxTimeSeen;
	private Cursor m_cursor;
	private Element m_localeElem, m_defaultLocaleElem;
	private DateFormat m_fullDateFormat;
	private DateFormat m_compactDateFormat;
	private Locale m_locale;
	private TimeZone m_timeZone;
}


