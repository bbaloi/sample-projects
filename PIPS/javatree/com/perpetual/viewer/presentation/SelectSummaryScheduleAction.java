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
//import com.perpetual.log.LogSystem;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.logreport.control.ScheduleCRUD;
import com.perpetual.logreport.control.ScheduleCRUDHome;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUD;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUDHome;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.viewer.model.ejb.SummaryScheduleData;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.DomainMessageData;
import com.perpetual.viewer.model.ejb.DomainMessage;
import com.perpetual.util.EJBLoader;

import com.perpetual.util.Constants;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.ejb.MessagePatternData;



public class SelectSummaryScheduleAction extends PreViewGeneralSummaryAction
{
     ArrayList messageList = new ArrayList();
     
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
            sLog.debug("in SelectSummaryScheduleAction() !");
		try
		{
			SummaryScheduleCRUD summaryScheduleCRUD = null;
			ScheduleCRUD scheduleCRUD = null;
			try
			{
				summaryScheduleCRUD = (SummaryScheduleCRUD)EJBLoader.createEJBObject(SummaryScheduleCRUDHome.JNDI_NAME, SummaryScheduleCRUDHome.class);
				scheduleCRUD = (ScheduleCRUD)EJBLoader.createEJBObject(ScheduleCRUDHome.JNDI_NAME, ScheduleCRUDHome.class);

				ReasonForm reasonForm = (ReasonForm)form;
				String reason = reasonForm.getReason();

				SummaryScheduleForm defaultForm = new SummaryScheduleForm();

				if ("add".equals(reason) || "edit".equals(reason))
				{
                                    sLog.debug("This is add|edit");
					//init(request, defaultForm);
                                        init(request,defaultForm);

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
                                                {
							col.add(m_twoNumberFormat.format(i));
                                                }//new Integer(i));
						request.setAttribute("incHourCollection", col);
					}

					{
						Collection col = new ArrayList();
						for (int i = 0; i < 60; i += 5)
                                                {
							col.add(m_twoNumberFormat.format(i));
                                                }//new Integer(i));
						request.setAttribute("incMinuteCollection", col);
					}

					request.setAttribute("defaultForm", defaultForm);
				}

				if ("add".equals(reason))
				{
                                    sLog.debug("This is add !");
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

					SummaryScheduleData summaryScheduleData = summaryScheduleCRUD.retrieveByPrimaryKey(id);
					ScheduleData scheduleData = scheduleCRUD.retrieveByPrimaryKey(summaryScheduleData.getScheduleId());

					if ("delete".equals(reason))
					{
						// only need to pass on id of log schedule to delete
						//

						DeleteSummaryScheduleForm deleteSummaryScheduleForm = new DeleteSummaryScheduleForm();
						deleteSummaryScheduleForm.setId(reasonForm.getId());
						deleteSummaryScheduleForm.setName(summaryScheduleData.getName());
						request.setAttribute("DeleteSummaryScheduleForm", deleteSummaryScheduleForm);
					}
					else
					{
						// can only be edit
						//
                                                sLog.debug("--This is an Edit--");
						defaultForm.setId(reasonForm.getId());
						defaultForm.setName(summaryScheduleData.getName());
						//LogSystem logSystem = LogSystem.getDefault();
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
                                                        boolean facilitesAnd = summaryScheduleData.getAndFacs() != 0;
                                                        boolean severitiesAnd = summaryScheduleData.getAndSevs() != 0;
                                                        boolean facilitiesOrSeverities = summaryScheduleData.getFacsOrSevs() != 0;
                                                        boolean facilitiesOrMessages = summaryScheduleData.getFacsOrMessages() != 0;
                                                        boolean severitiesOrMessages = summaryScheduleData.getSevsOrMessages() != 0;
                                                       
							String incDate = scheduleData.getIncDate();
							defaultForm.setRepeat(repeat);
                                                        /*defaultForm.setFacilitiesAnd(new Integer(summaryScheduleData.getAndFacs()).toString());
                                                        defaultForm.setSeveritiesAnd(new Integer(summaryScheduleData.getAndSevs()).toString());
                                                        defaultForm.setFacilitiesOrSeverities(new Integer(summaryScheduleData.getFacsOrSevs()).toString());                                               
                                                        defaultForm.setFacilitiesOrMessages(new Integer(summaryScheduleData.getFacsOrMessages()).toString());                                               
                                                        defaultForm.setSeveritiesOrMessages(new Integer(summaryScheduleData.getSevsOrMessages()).toString());                                               
                                                        */
                                                        
                                                        defaultForm.setFacilitiesAnd(facilitesAnd);
                                                        defaultForm.setSeveritiesAnd(severitiesAnd);
                                                        defaultForm.setFacilitiesOrSeverities(facilitiesOrSeverities);                                               
                                                        defaultForm.setFacilitiesOrMessages(facilitiesOrMessages);                                               
                                                        defaultForm.setSeveritiesOrMessages(severitiesOrMessages);                                               
                                                
							StringTokenizer tokenizer = new StringTokenizer(incDate);
							defaultForm.setIncYear(tokenizer.nextToken());
							defaultForm.setIncMonth(tokenizer.nextToken());
							defaultForm.setIncDay(tokenizer.nextToken());
							defaultForm.setIncHour(tokenizer.nextToken());
							defaultForm.setIncMinute(tokenizer.nextToken());
						}

						{
							List list = new ArrayList();
							for (StringTokenizer tokenizer = new StringTokenizer(summaryScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
							{
								list.add(tokenizer.nextToken());
							}
							defaultForm.setHostList((String[])list.toArray(new String[list.size()]));
						}
						{
							List list = new ArrayList();
							for (int mask = summaryScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
								if ((mask & 1) != 0)
									list.add(Integer.toString(i));
							defaultForm.setSeverityList((String[])list.toArray(new String[list.size()]));
						}
						{
							List list = new ArrayList();
							for (int mask = summaryScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
								if ((mask & 1) != 0)
									list.add(Integer.toString(i));
							defaultForm.setFacilityList((String[])list.toArray(new String[list.size()]));
						}
                                                {
							List list = new ArrayList();
							for (int mask = summaryScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
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
				//if (summaryScheduleCRUD != null) summaryScheduleCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("problem", e);
			return mapping.findForward("failure");
		}
	}
        private void init(HttpServletRequest request, ViewGeneralSummaryActionForm defaultForm)
	{
		{
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);

			defaultForm.setStartYear(year);
			defaultForm.setStartMonth(cal.get(Calendar.MONTH));
			defaultForm.setStartDay(cal.get(Calendar.DAY_OF_MONTH));
			defaultForm.setStartHour(cal.get(Calendar.HOUR_OF_DAY));
			defaultForm.setStartMinute(cal.get(Calendar.MINUTE) / 5 * 5);

			defaultForm.setEndYear(defaultForm.getStartYear());
			defaultForm.setEndMonth(defaultForm.getStartMonth());
			defaultForm.setEndDay(defaultForm.getStartDay());
			defaultForm.setEndHour(defaultForm.getStartHour());
			defaultForm.setEndMinute(defaultForm.getStartMinute());

			Collection col = new ArrayList();
			for (int i = year - 2, max = i + 9; i <= max; i++)
				col.add(new Integer(i));
			request.setAttribute("yearCollection", col);
		}

		{
			Collection col = new ArrayList();
			for (int i = 1; i <= 31; i++)
				col.add(new Integer(i));
			request.setAttribute("dayCollection", col);
		}

		{
			Collection col = new ArrayList();
			for (int i = 0; i < 24; i++)
				col.add(m_twoNumberFormat.format(i));	//new Integer(i));
			request.setAttribute("hourCollection", col);
		}

		{
			Collection col = new ArrayList();
			for (int i = 0; i < 60; i += 5)
				col.add(m_twoNumberFormat.format(i));	//new Integer(i));
			request.setAttribute("minuteCollection", col);
		}

		{
			Map offsetMap = new TreeMap();
			String[] ids = TimeZone.getAvailableIDs();
			for (int i = 0; i < ids.length; i++)
			{
				TimeZone zone = TimeZone.getTimeZone(ids[i]);
				Integer offset = new Integer(zone.getRawOffset());
				if (zone.getID().length() == 3 && !offsetMap.containsKey(offset))
				{
					//System.out.println(zone.getID() + " " + offset);
					offsetMap.put(offset, zone);
				}
			}
			Map map = new LinkedHashMap();
			for (Iterator i = offsetMap.values().iterator(); i.hasNext(); )
			{
				TimeZone zone = (TimeZone)i.next();
				double offset = zone.getRawOffset() / (1000.0 * 60 * 60);
				String timeZoneName;
				if (offset == 0)
				{
					timeZoneName = zone.getID() + " " + zone.getDisplayName();
				}
				else
				{
					String offsetText = Double.toString(offset);
					if (offset >= 0) offsetText = "+" + offsetText;
					timeZoneName = "(GMT " + offsetText + ") "  + zone.getID() + " " + zone.getDisplayName();
				}
				map.put(zone.getID(), timeZoneName);
			}
			request.setAttribute("timeZoneMap", map);
			defaultForm.setTimeZone(TimeZone.getDefault().getID());
		}

		{
			UserProfileVO userProfile =(UserProfileVO)request.getSession().getAttribute("UserProfile");

                        ArrayList hostCollection = new ArrayList();
                        ArrayList severityCollection = new ArrayList();
                        ArrayList facilityCollection = new ArrayList();
                        ArrayList messageCollection = new ArrayList();
                        hostCollection.add(new HostVO(Constants.NO_SELECTION,"",""));
                        hostCollection.addAll(userProfile.getHostList());
                        severityCollection.add(new SeverityVO(Constants.NO_SELECTION,""));
                        severityCollection.addAll(userProfile.getSeverityList());
                        facilityCollection.add(new FacilityVO(Constants.NO_SELECTION,""));
                        facilityCollection.addAll(userProfile.getFacilityList());
                        messageCollection.add(new MessagePatternData(new Integer(Constants.NO_SELECTION),"","",""));
                        request.setAttribute("hostCollection", hostCollection);
			request.setAttribute("severityCollection", severityCollection);
			request.setAttribute("facilityCollection", facilityCollection);
			//request.setAttribute("hostCollection", userProfile.getHostList());
			//request.setAttribute("severityCollection", userProfile.getSeverityList());
			//request.setAttribute("facilityCollection", userProfile.getFacilityList());
                          ////getting message pattern list
                                //-----GET MESSAGE LIST-----
                                sLog.debug("---getting message patterns---!!!");
                                Collection messagePatternList=null;
                                MessagePatternCRUD messagePatternCRUD;
                                messageList.clear();
                                try
                                {
                                    messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
                                    messagePatternList = messagePatternCRUD.retrieveMessagesFromDomain(userProfile.getDomainId());
                                    
                                     Iterator it = messagePatternList.iterator();
                                     while(it.hasNext())
                                     {
                                        Object obj = it.next(); 
                                        sLog.debug("Entity:"+obj.getClass().getName());
                                        DomainMessage dmd = (DomainMessage) obj;
                                        MessagePatternData mpd = messagePatternCRUD.retrieveByPrimaryKey(dmd.getMessageid().intValue());
                                        messageList.add(mpd);
                                     }
                                  }
                                catch(Exception excp)
                                {
                                    String msg = "Could not instantiate the MessagePatternCRUD Bean !";
                                    sLog.error(msg);
                                    excp.printStackTrace();
                                }
                                messageCollection.addAll(messageList);
                                request.setAttribute("messagePatternCollection", messageCollection);
                               // request.setAttribute("messagePatternCollection", messageList);
                                                               
                }
	}

	private static Logger sLog = Logger.getLogger(SelectSummaryScheduleAction.class);
}






