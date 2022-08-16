package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.text.*;
import javax.servlet.http.*;
import javax.rmi.*;
import javax.servlet.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;
import org.apache.log4j.Logger;

import com.perpetual.util.ServiceLocator;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogSystem;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUD;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUDHome;
import com.perpetual.viewer.model.ejb.SummaryScheduleData;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.util.Constants;


//public class SummaryScheduleAction extends ViewGeneralSummaryAction
public class SummaryScheduleAction extends PerpetualAction
{
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
            
            sLog.debug("In SummaryScheduleAction....");
		SummaryScheduleForm summaryScheduleForm = (SummaryScheduleForm)form;
		sLog.info(summaryScheduleForm.toString());

		try
		{
			MessageResources res = getResources(request);
			LogSystem logSystem = LogSystem.getDefault();

			HttpSession session = request.getSession();
			Locale locale = getLocale(request);
			TimeZone timeZone = TimeZone.getTimeZone(summaryScheduleForm.getTimeZone());
			Map filterMap = new HashMap();

			init(summaryScheduleForm, request);		// ViewLogstoreAction takes care of getting start/end dates, severity/facility/host collections

			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, summaryScheduleForm.getWhenYear());
			cal.set(Calendar.MONTH, summaryScheduleForm.getWhenMonth());
			cal.set(Calendar.DAY_OF_MONTH, summaryScheduleForm.getWhenDay());
			cal.set(Calendar.HOUR_OF_DAY, summaryScheduleForm.getWhenHour());
			cal.set(Calendar.MINUTE, summaryScheduleForm.getWhenMinute());
			long whenTime = cal.getTimeInMillis();
                        sLog.debug("When Date:"+new Date(whenTime));

//			int logScheduleId = -1;
//			String name = "mike";
			{
//				ScheduleCRUD scheduleCRUD = (ScheduleCRUD)EJBLoader.createEJBObject("perpetual/ScheduleCRUD", ScheduleCRUDHome.class);
//				try
//				{
					SummaryScheduleCRUD summaryScheduleCRUD = (SummaryScheduleCRUD)EJBLoader.createEJBObject("perpetual/SummaryScheduleCRUD", SummaryScheduleCRUDHome.class);
					try
					{
						String incDate = summaryScheduleForm.getIncYear() + " " +
								summaryScheduleForm.getIncMonth() + " " +
								summaryScheduleForm.getIncDay() + " " +
								summaryScheduleForm.getIncHour() + " " +
								summaryScheduleForm.getIncMinute();

						StringBuffer hosts = new StringBuffer();
						{
							String[] formHosts = summaryScheduleForm.getHostList();                                                         
                                                        if(formHosts!=null)
                                                        {
                                                            String [] _hosts = stripNoSelection(formHosts);
                                                            if(_hosts!=null)
                                                            for(int i = 0; i < _hosts.length; i++)
                                                                {
                                                                hosts.append(_hosts[i]).append(' ');
								}
                                                        }
                                                        else throw new Exception("Please Select a host !");
						}
						int severities = 0, facilities = 0, mp=0;
						{
							String[] formSeverities = summaryScheduleForm.getSeverityList();
                                                         if(formSeverities!=null)
                                                         {
                                                                String [] sevs = stripNoSelection(formSeverities);
                                                                if(sevs!=null)
                                                                    for (int i = 0; i < sevs.length; i++)
                                                                    {
                                                                            severities |= 1 << Integer.parseInt(sevs[i]);
                                                                    }
                                                         }
							String[] formFacilities = summaryScheduleForm.getFacilityList();
                                                        if(formFacilities!=null)
                                                        {
                                                                String [] facs = stripNoSelection(formFacilities);
                                                                if(facs!=null)
                                                                    for (int i = 0; i < facs.length; i++)
                                                                    {
                                                                            facilities |= 1 << Integer.parseInt(facs[i]);
                                                                    }
                                                        }
                                                        String[] messagePatterns = summaryScheduleForm.getMessagePatternList();
                                                        if(messagePatterns!=null)
                                                        {
                                                            String [] msgp = stripNoSelection(messagePatterns);
                                                            if(msgp!=null)
                                                             for (int i = 0; i < msgp.length; i++)
								{
									mp |= 1 << Integer.parseInt(msgp[i]);
								}
                                                        }
						}
						int id = summaryScheduleForm.getId();
                                                
                                                int severitiesAnd = summaryScheduleForm.getSeveritiesAnd() ? 1 : 0;
                                                int facilitiesAnd = summaryScheduleForm.getFacilitiesAnd() ? 1 : 0;
                                                int facilitiesOrSeverities = summaryScheduleForm.getFacilitiesOrSeverities() ? 1 : 0;
                                                int facilitiesOrMessages = summaryScheduleForm.getFacilitiesOrMessages() ? 1 : 0;
                                                int severitiesOrMessages = summaryScheduleForm.getSeveritiesOrMessages() ? 1 : 0;
                                               
                                                                                                                                               
                                                //SummaryScheduleData summaryScheduleData = new SummaryScheduleData(id == -1 ? null : new Integer(id), summaryScheduleForm.getName(), -1, hosts.toString(), severities,facilities,mp,_andSevs,_andFacs,_facsOrSevs,_facsOrMsgs,_sevsOrMsgs);
						SummaryScheduleData summaryScheduleData = new SummaryScheduleData(id == -1 ? null : new Integer(id), summaryScheduleForm.getName(), -1, hosts.toString(), severities,facilities,mp,severitiesAnd,facilitiesAnd,facilitiesOrSeverities,facilitiesOrMessages,severitiesOrMessages);
						ScheduleData scheduleData = new ScheduleData(null, summaryScheduleForm.getTimeZone(), m_startDate.getTime(), m_endDate.getTime(), whenTime, summaryScheduleForm.getRepeat() ? 1 : 0, incDate, "perpetual/SummaryReportTask", -1, 1);
						summaryScheduleCRUD.setLogSchedule(scheduleData, summaryScheduleData);
					}
					finally
					{
						summaryScheduleCRUD.remove();
					}
//				}
//				finally
//				{
//					scheduleCRUD.remove();
//				}
			}

			return mapping.findForward("success");
		}
		catch(Exception ex)
		{
			sLog.error("problem", ex);
			return mapping.findForward("failure");
		}
	}

	private static Logger sLog = Logger.getLogger(SummaryScheduleAction.class);
        
        private void init(SummaryScheduleForm summaryScheduleForm, HttpServletRequest request)
	{
		Locale locale = getLocale(request);
		m_timeZone = TimeZone.getTimeZone(summaryScheduleForm.getTimeZone());
		m_fullDateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
		m_fullDateFormat.setTimeZone(m_timeZone);
		m_compactDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		m_compactDateFormat.setTimeZone(m_timeZone);

		{
			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, summaryScheduleForm.getStartYear());
			cal.set(Calendar.MONTH, summaryScheduleForm.getStartMonth());
			cal.set(Calendar.DAY_OF_MONTH, summaryScheduleForm.getStartDay());
			cal.set(Calendar.HOUR_OF_DAY, summaryScheduleForm.getStartHour());
			cal.set(Calendar.MINUTE,summaryScheduleForm.getStartMinute());
			m_startDate = cal.getTime();
		}
		{
			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, summaryScheduleForm.getEndYear());
			cal.set(Calendar.MONTH, summaryScheduleForm.getEndMonth());
			cal.set(Calendar.DAY_OF_MONTH, summaryScheduleForm.getEndDay());
			cal.set(Calendar.HOUR_OF_DAY, summaryScheduleForm.getEndHour());
			cal.set(Calendar.MINUTE, summaryScheduleForm.getEndMinute());
			m_endDate = cal.getTime();
		}
		sLog.debug("startDate=" + m_startDate + " endDate=" + m_endDate);
		{
			Map map = new LinkedHashMap();
			for (Iterator i = userProfile.getSeverityList().iterator(); i.hasNext(); )
			{
				SeverityVO vo = (SeverityVO)i.next();
				map.put(new Integer(vo.getId()), vo.getName());
			}
			m_severityCollection = new ArrayList();
			String[] severities = summaryScheduleForm.getSeverityList();
			if (severities != null)
			{
				for (int i = 0; i < severities.length; i++)
				{
					m_severityCollection.add(map.get(new Integer(severities[i])));
				}
			}
		}
		{
			Map map = new LinkedHashMap();
			for (Iterator i = userProfile.getFacilityList().iterator(); i.hasNext(); )
			{
				FacilityVO vo = (FacilityVO)i.next();
				map.put(new Integer(vo.getId()), vo.getName());
			}
			m_facilityCollection = new ArrayList();
			String[] facilities = summaryScheduleForm.getFacilityList();
			if (facilities != null)
			{
				for (int i = 0; i < facilities.length; i++)
				{
					m_facilityCollection.add(map.get(new Integer(facilities[i])));
				}
			}
		}
		{
			String[] hostList = summaryScheduleForm.getHostList();
			m_hostCollection = hostList != null ? Arrays.asList(hostList) : new ArrayList();
		}
	}
        
        private String [] stripNoSelection(String [] pList)
        {
            String [] finalList=null;
            if(pList !=null)
            {
                    if(Constants.NO_SELECTION_STR.equals(pList[0]))
                    {
                        int ctr=0;
                        if(pList.length>1)
                        {
                            sLog.debug("list contains no_selection +!");
                            finalList= new String [pList.length-1];  
                            for (int i = 0; i < pList.length; i++)
                            {                                    
                                String id = pList[i];
                                sLog.debug("selected item:"+id);
                                if(id.equals(Constants.NO_SELECTION_STR))
                                    sLog.debug("No selection item !");
                                else
                                {
                                    finalList[ctr]=pList[i];
                                    ctr++;                            
                                }
                            }                        
                        }
                        else
                        {
                            sLog.debug(" no selection was made!");
                        }
                    }
                    else
                    {
                          finalList= pList;                                
                    }
            }
                    return finalList;
        }
        private DateFormat m_fullDateFormat, m_compactDateFormat;
	private int m_line;
	protected TimeZone m_timeZone;
	protected Date m_startDate, m_endDate;
	protected Collection m_severityCollection, m_facilityCollection, m_hostCollection;
}




