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

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.host.HostCRUD;
import com.perpetual.viewer.control.ejb.crud.host.HostCRUDHome;
import com.perpetual.viewer.model.vo.HostVO;


public class DeleteHostAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ReasonForm reasonForm = (ReasonForm)form;

		try
		{
			HostCRUD hostCRUD = (HostCRUD)EJBLoader.createEJBObject("perpetual/HostCRUD", HostCRUDHome.class);
			try
			{
				hostCRUD.delete(reasonForm.getId());

				return mapping.findForward("success");
			}
			finally
			{
				hostCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("Problem deleting host", e);
		}

		return mapping.findForward("failure");
	}
}







