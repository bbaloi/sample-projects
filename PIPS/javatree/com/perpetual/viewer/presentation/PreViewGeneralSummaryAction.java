package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.servlet.http.*;
import javax.rmi.*;
import java.util.Iterator;
import org.apache.struts.action.*;
import org.apache.struts.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.ViewGeneralSummaryActionVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.FacilityVO;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.DomainMessageData;
import com.perpetual.viewer.model.ejb.DomainMessage;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.util.Constants;




public class PreViewGeneralSummaryAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( PreViewGeneralSummaryAction.class );
        protected NumberFormat m_twoNumberFormat = new DecimalFormat("00");

         public ActionForward doAction(ActionMapping mapping,
			ActionForm     noform,
			HttpServletRequest request,
			HttpServletResponse response) 
	{
            
		try
                {
                    
                       ViewGeneralSummaryActionVO summaryVO = new ViewGeneralSummaryActionVO();

			UserProfileVO userProfile =(UserProfileVO)request.getSession().getAttribute("UserProfile");

			{
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);

				summaryVO.setStartYear(year);
				summaryVO.setStartMonth(cal.get(Calendar.MONTH));
				summaryVO.setStartMonth(cal.get(Calendar.MONTH));
				summaryVO.setStartDay(cal.get(Calendar.DAY_OF_MONTH));
				summaryVO.setStartHour(cal.get(Calendar.HOUR_OF_DAY));
				summaryVO.setStartMinute(cal.get(Calendar.MINUTE) / 5 * 5);

				summaryVO.setEndYear(summaryVO.getStartYear());
				summaryVO.setEndMonth(summaryVO.getStartMonth());
				summaryVO.setEndDay(summaryVO.getStartDay());
				summaryVO.setEndHour(summaryVO.getStartHour());
				summaryVO.setEndMinute(summaryVO.getStartMinute());

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
					col.add(new Integer(i));
				request.setAttribute("hourCollection", col);
			}
			
			{
				Collection col = new ArrayList();
				for (int i = 0; i < 60; i += 5)
					col.add(new Integer(i));
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
				summaryVO.setTimeZone(TimeZone.getDefault().getID());
			}

			{
                            
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
                                
                                 //-----GET MESSAGE LIST-----
                                sLog.debug("---getting message patterns---!!!");
                                ArrayList messageList = new ArrayList();
                                MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
                                Collection messagePatternList = messagePatternCRUD.retrieveMessagesFromDomain(userProfile.getDomainId());
                                Iterator it = messagePatternList.iterator();
                                while(it.hasNext())
                                {
                                    Object obj = it.next(); 
                                    sLog.debug("Entity:"+obj.getClass().getName());
                                    DomainMessage dmd = (DomainMessage) obj;
                                    MessagePatternData mpd = messagePatternCRUD.retrieveByPrimaryKey(dmd.getMessageid().intValue());
                                    messageList.add(mpd);
                                }
                                messageCollection.addAll(messageList);
                                request.setAttribute("messagePatternCollection", messageCollection);
                                //request.setAttribute("messagePatternCollection", messageList);
                                
                                /*
                                String [] sevList = new String [8];
                                Iterator sit = userProfile.getSeverityList().iterator();
                                int i=0;
                                while(sit.hasNext())
                                {
                                    SeverityVO svo = (SeverityVO) sit.next();
                                    sevList[i]=svo.getName();
                                    i++;
                                    
                                }
                                String [] facList = new String [24];
                                 Iterator fit = userProfile.getFacilityList().iterator();
                                int x=0;
                                while(fit.hasNext())
                                {
                                    FacilityVO fvo = (FacilityVO) fit.next();
                                    facList[i]=fvo.getName();
                                    x++;
                                    
                                }
                                request.setAttribute("tmpFacilityList",facList);
                                request.setAttribute("tmpSeverityList",sevList);
                                 **/
			
                                
                                //summaryVO.setHostList(userProfile.getHostList());
                                //summaryVO.setSeverities(userProfile.getSeverityList());
                                //summaryVO.setFacilities(userProfile.getFacilityList());
                        }

			request.setAttribute("summaryVO", summaryVO);
                        request.getSession().setAttribute("summaryCollection",null);
			return mapping.findForward("success");
		}
		catch(Exception e)
		{
			sLog.error("problem", e);
			return mapping.findForward("failure");
		}
	}

	public static class IntLabel
	{
		public IntLabel(int id, String label)
		{
			m_id = id;
			m_label = label;
		}
		public void setId(int id)
		{
			m_id = id;
		}
		public int getId()
		{
			return m_id;
		}
		public void setLabel(String label)
		{
			m_label = label;
		}
		public String getLabel()
		{
			return m_label;
		}
		private int m_id;
		private String m_label;
	}
	public static class Label
	{
		public Label(String id, String label)
		{
			m_id = id;
			m_label = label;
		}
		public void setId(String id)
		{
			m_id = id;
		}
		public String getId()
		{
			return m_id;
		}
		public void setLabel(String label)
		{
			m_label = label;
		}
		public String getLabel()
		{
			return m_label;
		}
		private String m_id, m_label;
	}
}




