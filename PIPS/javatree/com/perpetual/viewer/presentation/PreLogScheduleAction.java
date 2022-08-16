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
import com.perpetual.logreport.control.LogScheduleCRUD;
import com.perpetual.logreport.control.LogScheduleCRUDHome;
import com.perpetual.logreport.model.LogScheduleData;


public class PreLogScheduleAction extends PerpetualAction 
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

			LogScheduleCRUD logScheduleCRUD = (LogScheduleCRUD)EJBLoader.createEJBObject(LogScheduleCRUDHome.JNDI_NAME, LogScheduleCRUDHome.class);
			try
			{
				request.setAttribute("LogScheduleDataCollection", logScheduleCRUD.retrieveAll());
			}
			finally
			{
				logScheduleCRUD.remove();
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




