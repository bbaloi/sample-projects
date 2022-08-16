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
//import com.perpetual.log.LogSystem;
import com.perpetual.logserver.LogSystem;
import com.perpetual.logreport.control.LogScheduleCRUD;
import com.perpetual.logreport.control.LogScheduleCRUDHome;
import com.perpetual.logreport.model.LogScheduleData;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.util.Constants;


public class LogScheduleAction extends ViewLogstoreAction
{
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		LogScheduleForm logScheduleForm = (LogScheduleForm)form;
		sLog.info(logScheduleForm.toString());

		try
		{
			MessageResources res = getResources(request);
			LogSystem logSystem = LogSystem.getDefault();

			HttpSession session = request.getSession();
			Locale locale = getLocale(request);
			TimeZone timeZone = TimeZone.getTimeZone(logScheduleForm.getTimeZone());
			Map filterMap = new HashMap();

			init(logScheduleForm, request);		// ViewLogstoreAction takes care of getting start/end dates, severity/facility/host collections

			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, logScheduleForm.getWhenYear());
			cal.set(Calendar.MONTH, logScheduleForm.getWhenMonth());
			cal.set(Calendar.DAY_OF_MONTH, logScheduleForm.getWhenDay());
			cal.set(Calendar.HOUR_OF_DAY, logScheduleForm.getWhenHour());
			cal.set(Calendar.MINUTE, logScheduleForm.getWhenMinute());
			long whenTime = cal.getTimeInMillis();

//			int logScheduleId = -1;
//			String name = "mike";
			{
//				ScheduleCRUD scheduleCRUD = (ScheduleCRUD)EJBLoader.createEJBObject("perpetual/ScheduleCRUD", ScheduleCRUDHome.class);
//				try
//				{
					LogScheduleCRUD logScheduleCRUD = (LogScheduleCRUD)EJBLoader.createEJBObject("perpetual/LogScheduleCRUD", LogScheduleCRUDHome.class);
					try
					{
						String incDate = logScheduleForm.getIncYear() + " " +
								logScheduleForm.getIncMonth() + " " +
								logScheduleForm.getIncDay() + " " +
								logScheduleForm.getIncHour() + " " +
								logScheduleForm.getIncMinute();

						StringBuffer hosts = new StringBuffer();
						{
							String[] formHosts = logScheduleForm.getHostList();                                                        
							if (formHosts != null)
                                                        {
                                                            String [] _hosts = stripNoSelection(formHosts);
                                                            if(_hosts!=null)
                                                            {
								for (int i = 0; i < _hosts.length; i++)
								{
									hosts.append(_hosts[i]).append(' ');
								}
                                                            }
                                                            else throw new Exception("Please Select a host !");
                                                        }
						}
						int severities = 0, facilities = 0,messagePatterns = 0;
						{
							String[] formSeverities = logScheduleForm.getSeverities();
							if (formSeverities != null)
                                                        {    
                                                            String [] sevs = stripNoSelection(formSeverities);
                                                            if(sevs!=null)
								for (int i = 0; i < sevs.length; i++)
								{
									severities |= 1 << Integer.parseInt(sevs[i]);
								}
                                                        }
							String[] formFacilities = logScheduleForm.getFacilities();
							if (formFacilities != null)
                                                        {
                                                            String [] facs = stripNoSelection(formFacilities);
                                                            if(facs!=null)
								for (int i = 0; i < facs.length; i++)
								{
									facilities |= 1 << Integer.parseInt(facs[i]);
								}
                                                        }
                                                         String [] msgPatternList = logScheduleForm.getMessagePatternList();
                                                         if (msgPatternList != null)
                                                         {
                                                             String [] msgp = stripNoSelection(msgPatternList);
                                                             if(msgp!=null)
								for (int i = 0; i < msgp.length; i++)
								{
									messagePatterns |= 1 << Integer.parseInt(msgp[i]);
								}
                                                         }
                                    
						}
						int id = logScheduleForm.getId();
						LogScheduleData logScheduleData = new LogScheduleData(id == -1 ? null : new Integer(id), logScheduleForm.getName(), -1, hosts.toString(), severities, facilities,messagePatterns);
						ScheduleData scheduleData = new ScheduleData(null, logScheduleForm.getTimeZone(), m_startDate.getTime(), m_endDate.getTime(), whenTime, logScheduleForm.getRepeat() ? 1 : 0, incDate, "perpetual/LogReportTask", -1, 1);
						logScheduleCRUD.setLogSchedule(scheduleData, logScheduleData);
					}                                        
					finally
					{
						//logScheduleCRUD.remove();
					}
				}
				/*finally
				{
					scheduleCRUD.remove();
				}
                                        
			}*/

			return mapping.findForward("success");
		}
		catch(Exception ex)
		{
			sLog.error("problem", ex);
			return mapping.findForward("failure");
		}
	}
        private String [] stripNoSelection(String [] pList)
        {
            String [] finalList=null;
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
            
            return finalList;
        }
	private static Logger sLog = Logger.getLogger(LogScheduleAction.class);
}




