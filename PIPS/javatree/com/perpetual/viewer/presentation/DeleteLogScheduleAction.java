package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.logreport.control.LogScheduleCRUD;
import com.perpetual.logreport.control.LogScheduleCRUDHome;


public class DeleteLogScheduleAction extends PerpetualAction
{
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		DeleteLogScheduleForm deleteLogScheduleForm = (DeleteLogScheduleForm)form;

		try
		{
			LogScheduleCRUD logScheduleCRUD = (LogScheduleCRUD)EJBLoader.createEJBObject(LogScheduleCRUDHome.JNDI_NAME, LogScheduleCRUDHome.class);
			try
			{
				logScheduleCRUD.deleteLogSchedule(deleteLogScheduleForm.getId());

				return mapping.findForward("success");
			}
			finally
			{
				logScheduleCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("Problem deleting log schedule", e);
		}

		return mapping.findForward("failure");
	}

	private static Logger sLog = Logger.getLogger(DeleteLogScheduleAction.class);
}


