package com.perpetual.viewer.presentation;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUD;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUDHome;
import com.perpetual.viewer.model.ejb.SummaryScheduleData;


public class PreSummaryScheduleAction extends PerpetualAction 
{
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;

		try
		{
/*
test stuff - conclusion - yet again another Jakarta fuckup: struts screws up the classloader upon redeployment, making multiple ear files totally suck - am I surprised?  nope.  Am I pissed off that Jakarta software wasted so much of my time, yet again, and again, and again?  hell yes.
			Object obj = EJBLoader.createEJBObject(LogScheduleCRUDHome.JNDI_NAME, LogScheduleCRUDHome.class);
			System.out.println("!!!!!!!!! " + obj + " " + obj.getClass());
			obj = PortableRemoteObject.narrow(obj, LogScheduleCRUD.class);
			System.out.println("!!!!!!!!! " + obj + " " + obj.getClass());
			System.out.println("!! " + (LogScheduleCRUD)obj);
*/

			SummaryScheduleCRUD summaryScheduleCRUD = (SummaryScheduleCRUD)EJBLoader.createEJBObject(SummaryScheduleCRUDHome.JNDI_NAME, SummaryScheduleCRUDHome.class);
			try
			{
				request.setAttribute("SummaryScheduleDataCollection", summaryScheduleCRUD.retrieveAll());
			}
			finally
			{
				summaryScheduleCRUD.remove();
			}

			return mapping.findForward("success");
		}
		catch (Exception ex)
		{
			sLog.error("Problem", ex);
		}

		return mapping.findForward("failure");
	}

	private static Logger sLog = Logger.getLogger(PreLogScheduleAction.class);
}




