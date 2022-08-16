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

import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.DomainMessageData;
import com.perpetual.viewer.model.ejb.DomainMessage;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.util.Constants;

public class PreViewLogStoreAction extends PerpetualAction
{
    
    
	public ActionForward doAction(ActionMapping mapping, ActionForm noform, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			ViewLogstoreActionForm defaultForm = new ViewLogstoreActionForm();

			init(request, defaultForm);
			request.setAttribute("defaultForm", defaultForm);

			return mapping.findForward("success");
		}
		catch(Exception e)
		{
			sLog.error("problem", e);
			return mapping.findForward("failure");
		}
	}

	protected void init(HttpServletRequest request, ViewLogstoreActionForm defaultForm) throws Exception
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
                                                 
			//request.setAttribute("hostCollection", userProfile.getHostList());
			//request.setAttribute("severityCollection", userProfile.getSeverityList());
			//request.setAttribute("facilityCollection", userProfile.getFacilityList());
                        request.setAttribute("hostCollection", hostCollection);
			request.setAttribute("severityCollection",severityCollection);
			request.setAttribute("facilityCollection", facilityCollection);
                        
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
                        
		}
	}
	private static Logger sLog = Logger.getLogger(PreViewLogStoreAction.class);
	protected NumberFormat m_twoNumberFormat = new DecimalFormat("00");
}




