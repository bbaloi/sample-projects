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

import org.apache.struts.action.*;
import org.apache.struts.util.*;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.DomainMessageData;
import com.perpetual.viewer.model.ejb.DomainMessage;


public class PreViewSummaryAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping,
			ActionForm     noform,
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		try
		{
                    
                    //get Host List
                    
                    //construct current Date picture
			ViewSummaryActionForm defaultForm = new ViewSummaryActionForm();

			UserProfileVO userProfile =(UserProfileVO)request.getSession().getAttribute("UserProfile");

			{
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);

				defaultForm.setStartYear(year);
				defaultForm.setStartMonth(cal.get(Calendar.MONTH));
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
				defaultForm.setTimeZone(TimeZone.getDefault().getID());
			}

			{
				request.setAttribute("hostCollection", userProfile.getHostList());
				request.setAttribute("severityCollection", userProfile.getSeverityList());
				request.setAttribute("facilityCollection", userProfile.getFacilityList());
                              
			}

			request.setAttribute("defaultForm", defaultForm);
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




