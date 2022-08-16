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
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUD;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUDHome;
import com.perpetual.viewer.model.vo.RoleVO;


public class UpdateRoleAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		UpdateRoleForm updateRoleForm = (UpdateRoleForm)form;

		try
		{
			RoleCRUD roleCRUD = (RoleCRUD)EJBLoader.createEJBObject("perpetual/RoleCRUD", RoleCRUDHome.class);
			try
			{
				int roleId = updateRoleForm.getId();

				RoleVO roleVO;
				if (roleId < 0)
				{
					roleVO = new RoleVO(-1, updateRoleForm.getRoleName());
					roleVO = roleCRUD.create(roleVO);
					roleId = roleVO.getId();
				}
				else
				{
					roleVO = roleCRUD.retrieveByPrimaryKey(roleId);
					roleVO.setRoleName(updateRoleForm.getRoleName());
					roleCRUD.update(roleVO);
				}

				if (updateRoleForm.getAddActionList() != null)
					roleCRUD.addActionsToRole(updateRoleForm.getAddActionList(), roleId);
				if (updateRoleForm.getRemoveActionList() != null)
					roleCRUD.removeActionsFromRole(updateRoleForm.getRemoveActionList(), roleId);

				return mapping.findForward("success");
			}
			finally
			{
				roleCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("Problem updating/creating role", e);
		}

		return mapping.findForward("failure");
	}
}





