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
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUD;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUDHome;
import com.perpetual.viewer.model.vo.RoleVO;


public class PreMaintainRolesAction extends PerpetualAction 
{
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;

		try
		{
			RoleCRUD roleCRUD = (RoleCRUD)EJBLoader.createEJBObject("perpetual/RoleCRUD", RoleCRUDHome.class);
			try
			{
				request.setAttribute("RoleVOCollection", roleCRUD.retrieveAll());
			}
			finally
			{
				roleCRUD.remove();
			}

			return mapping.findForward("preMaintainRoles.success");
		}
		catch (Exception ex)
		{
			sLog.error("Problem in PreMaintainRolesAction ...", ex);
		}

		return mapping.findForward("preMaintainRoles.failure");
	}

	private static Logger sLog = Logger.getLogger(PreLogScheduleAction.class);
}



