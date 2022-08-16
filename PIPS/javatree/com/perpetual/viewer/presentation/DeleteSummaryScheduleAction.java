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
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUD;
import com.perpetual.viewer.control.ejb.crud.summaryschedule.SummaryScheduleCRUDHome;


public class DeleteSummaryScheduleAction extends PerpetualAction
{
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		DeleteSummaryScheduleForm deleteSummaryScheduleForm = (DeleteSummaryScheduleForm)form;

		try
		{
			SummaryScheduleCRUD summaryScheduleCRUD = (SummaryScheduleCRUD)EJBLoader.createEJBObject(SummaryScheduleCRUDHome.JNDI_NAME, SummaryScheduleCRUDHome.class);
			try
			{
				summaryScheduleCRUD.deleteSummarySchedule(deleteSummaryScheduleForm.getId());

				return mapping.findForward("success");
			}
			finally
			{
				summaryScheduleCRUD.remove();
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


