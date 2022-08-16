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

import org.apache.struts.action.*;
import org.apache.struts.util.*;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.logserver.LogSystem;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.logreport.control.ScheduleCRUD;
import com.perpetual.logreport.control.ScheduleCRUDHome;
import com.perpetual.logreport.control.LogScheduleCRUD;
import com.perpetual.logreport.control.LogScheduleCRUDHome;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.logreport.model.LogScheduleData;


public class SelectLogScheduleAction extends PreViewLogStoreAction
{
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			LogScheduleCRUD logScheduleCRUD = null;
			ScheduleCRUD scheduleCRUD = null;
			try
			{
				logScheduleCRUD = (LogScheduleCRUD)EJBLoader.createEJBObject(LogScheduleCRUDHome.JNDI_NAME, LogScheduleCRUDHome.class);
				scheduleCRUD = (ScheduleCRUD)EJBLoader.createEJBObject(ScheduleCRUDHome.JNDI_NAME, ScheduleCRUDHome.class);

				ReasonForm reasonForm = (ReasonForm)form;
				String reason = reasonForm.getReason();

				LogScheduleForm defaultForm = new LogScheduleForm();

				if ("add".equals(reason) || "edit".equals(reason))
				{
					init(request, defaultForm);

					{
						Calendar cal = Calendar.getInstance();
						defaultForm.setWhenYear(cal.get(Calendar.YEAR));
						defaultForm.setWhenMonth(cal.get(Calendar.MONTH));
						defaultForm.setWhenDay(cal.get(Calendar.DAY_OF_MONTH));
						defaultForm.setWhenHour(cal.get(Calendar.HOUR_OF_DAY));
						defaultForm.setWhenMinute(cal.get(Calendar.MINUTE) / 5 * 5);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i <= 9; i++)
							col.add(new Integer(i));
						request.setAttribute("incYearCollection", col);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i <= 11; i++)
							col.add(new Integer(i));
						request.setAttribute("incMonthCollection", col);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i <= 31; i++)
							col.add(new Integer(i));
						request.setAttribute("incDayCollection", col);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i < 24; i++)
							col.add(m_twoNumberFormat.format(i));	//new Integer(i));
						request.setAttribute("incHourCollection", col);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i < 60; i += 5)
							col.add(m_twoNumberFormat.format(i));	//new Integer(i));
						request.setAttribute("incMinuteCollection", col);
					}

					request.setAttribute("defaultForm", defaultForm);
				}

				if ("add".equals(reason))
				{
					defaultForm.setName("");
					defaultForm.setId(-1);
				}
				else
				{
					// edit or delete, both require something selected
					//
					int id = reasonForm.getId();
					if (id == -1)
						throw new Exception("No schedule selected");

					LogScheduleData logScheduleData = logScheduleCRUD.retrieveByPrimaryKey(id);
					ScheduleData scheduleData = scheduleCRUD.retrieveByPrimaryKey(logScheduleData.getScheduleId());

					if ("delete".equals(reason))
					{
						// only need to pass on id of log schedule to delete
						//

						DeleteLogScheduleForm deleteLogScheduleForm = new DeleteLogScheduleForm();
						deleteLogScheduleForm.setId(reasonForm.getId());
						deleteLogScheduleForm.setName(logScheduleData.getName());
						request.setAttribute("DeleteLogScheduleForm", deleteLogScheduleForm);
					}
					else
					{
						// can only be edit
						//
						defaultForm.setId(reasonForm.getId());
						defaultForm.setName(logScheduleData.getName());

						LogSystem logSystem = LogSystem.getDefault();

						{
							Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(scheduleData.getTimeZone()));

							cal.setTimeInMillis(scheduleData.getStartDate());
							defaultForm.setStartYear(cal.get(Calendar.YEAR));
							defaultForm.setStartMonth(cal.get(Calendar.MONTH));
							defaultForm.setStartDay(cal.get(Calendar.DAY_OF_MONTH));
							defaultForm.setStartHour(cal.get(Calendar.HOUR_OF_DAY));
							defaultForm.setStartMinute(cal.get(Calendar.MINUTE));

							cal.setTimeInMillis(scheduleData.getEndDate());
							defaultForm.setEndYear(cal.get(Calendar.YEAR));
							defaultForm.setEndMonth(cal.get(Calendar.MONTH));
							defaultForm.setEndDay(cal.get(Calendar.DAY_OF_MONTH));
							defaultForm.setEndHour(cal.get(Calendar.HOUR_OF_DAY));
							defaultForm.setEndMinute(cal.get(Calendar.MINUTE));
							defaultForm.setTimeZone(scheduleData.getTimeZone());

							cal.setTimeInMillis(scheduleData.getWhenDate());
							defaultForm.setWhenYear(cal.get(Calendar.YEAR));
							defaultForm.setWhenMonth(cal.get(Calendar.MONTH));
							defaultForm.setWhenDay(cal.get(Calendar.DAY_OF_MONTH));
							defaultForm.setWhenHour(cal.get(Calendar.HOUR_OF_DAY));
							defaultForm.setWhenMinute(cal.get(Calendar.MINUTE) / 5 * 5);
						}

						{
							boolean repeat = scheduleData.getRepeat() != 0;
							String incDate = scheduleData.getIncDate();
							defaultForm.setRepeat(repeat);
							StringTokenizer tokenizer = new StringTokenizer(incDate);
							defaultForm.setIncYear(tokenizer.nextToken());
							defaultForm.setIncMonth(tokenizer.nextToken());
							defaultForm.setIncDay(tokenizer.nextToken());
							defaultForm.setIncHour(tokenizer.nextToken());
							defaultForm.setIncMinute(tokenizer.nextToken());
						}

						{
							List list = new ArrayList();
							for (StringTokenizer tokenizer = new StringTokenizer(logScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
							{
								list.add(tokenizer.nextToken());
							}
							defaultForm.setHostList((String[])list.toArray(new String[list.size()]));
						}
						{
							List list = new ArrayList();
							for (int mask = logScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
								if ((mask & 1) != 0)
									list.add(Integer.toString(i));
							defaultForm.setSeverities((String[])list.toArray(new String[list.size()]));
						}
						{
							List list = new ArrayList();
							for (int mask = logScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
								if ((mask & 1) != 0)
									list.add(Integer.toString(i));
							defaultForm.setFacilities((String[])list.toArray(new String[list.size()]));
						}
                                                {
							List list = new ArrayList();
							for (int mask = logScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
								if ((mask & 1) != 0)
									list.add(Integer.toString(i));
                                                        
							defaultForm.setMessagePatternList((String[])list.toArray(new String[list.size()]));
						}

						sLog.info(defaultForm);
					}
				}

				return mapping.findForward(reason);
			}
			finally
			{
				// remove will not fail, or else things are so far gone that it'll be game over because of other reasons, don't wrap in try/catch
				//
				//if (scheduleCRUD != null) scheduleCRUD.remove();
				//if (logScheduleCRUD != null) logScheduleCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("problem", e);
			return mapping.findForward("failure");
		}
	}

	private static Logger sLog = Logger.getLogger(SelectLogScheduleAction.class);
}






