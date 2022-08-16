package com.perpetual.viewer.presentation;

import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.host.HostCRUD;
import com.perpetual.viewer.control.ejb.crud.host.HostCRUDHome;
import com.perpetual.viewer.model.vo.HostVO;


public class PreMaintainHostsAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PreMaintainHostsAction.class );
 
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			HostCRUD hostCRUD = (HostCRUD)EJBLoader.createEJBObject("perpetual/HostCRUD", HostCRUDHome.class);
			try
			{
				request.setAttribute("HostVOCollection", hostCRUD.retrieveAll());
			}
			finally
			{
				hostCRUD.remove();
			}
			return mapping.findForward("preMaintainHosts.success");   	
		}
		catch(Exception ex)
		{
			sLog.error("Problem with PreMaintainHostsAction", ex);
		}

		return mapping.findForward("preMaintainHosts.failure");
	}
}



