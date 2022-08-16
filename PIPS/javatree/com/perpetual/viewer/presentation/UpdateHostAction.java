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
import com.perpetual.viewer.model.ejb.DomainHostHome;
import com.perpetual.viewer.model.vo.DomainHostVO;
import com.perpetual.viewer.model.vo.HostVO;


public class UpdateHostAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		UpdateHostForm updateHostForm = (UpdateHostForm)form;

		try
		{
			HostCRUD hostCRUD = (HostCRUD)EJBLoader.createEJBObject("perpetual/HostCRUD", HostCRUDHome.class);
			DomainHostHome domainHostHome = (DomainHostHome)
						EJBLoader.getEJBHome("perpetual/DomainHost",
							DomainHostHome.class);	
			try
			{
				int hostId = updateHostForm.getId();

				HostVO hostVO;
				if (hostId < 0)
				{
					hostVO = new HostVO(-1, updateHostForm.getName(), updateHostForm.getDescription());
					hostCRUD.create(hostVO);
					
					// add to root domain - domain '0'
					domainHostHome.create(new DomainHostVO(-1, new Integer(0),
							new Integer(hostVO.getId())));
				}
				else
				{
					hostVO = hostCRUD.retrieveByPrimaryKey(hostId);
					hostVO.setName(updateHostForm.getName());
					hostVO.setDescription(updateHostForm.getDescription());
					hostCRUD.update(hostVO);
				}

				return mapping.findForward("success");
			}
			finally
			{
				hostCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("Problem updating/creating host", e);
		}

		return mapping.findForward("failure");
	}
}






