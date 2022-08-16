package com.perpetual.viewer.presentation;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.exception.PerpetualException;
import com.perpetual.util.EJBLoader;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUD;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUDHome;
import com.perpetual.viewer.model.vo.RoleVO;


public class SelectRoleAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ReasonForm reasonForm = (ReasonForm)form;
		String reason = reasonForm.getReason();             

		try
		{
			RoleCRUD roleCRUD = (RoleCRUD)EJBLoader.createEJBObject("perpetual/RoleCRUD", RoleCRUDHome.class);
			try
			{
				int roleId = reasonForm.getId();
				RoleVO roleVO;

				if ("add".equals(reason))
				{
					roleVO = new RoleVO(-1, "");
				}
				else
				{
					if (roleId == -1) {
						throw new PerpetualException("no selected role");
					} else {
						roleVO = roleCRUD.retrieveByPrimaryKey(roleId);
					}
					
					
				}

				request.setAttribute("RoleVO", roleVO);

				if ("edit".equals(reason) || "add".equals(reason))
				{
					Collection freeActionList = roleCRUD.retrieveAllActions();
					Collection usedActionList;

					if ("add".equals(reason))
					{
						usedActionList = new ArrayList();
					}
					else
					{
						usedActionList = roleCRUD.retrieveAllActionsForRole(reasonForm.getId());
						freeActionList.removeAll(usedActionList);
					}

					request.setAttribute("freeActionList", freeActionList);
					request.setAttribute("usedActionList", usedActionList);
				}
//				request.setAttribute("id", new Integer(roleId));
			}
			finally
			{
				roleCRUD.remove();
			}
/*
			RoleVO roleVO = null;

			if ("edit".equals(reason) || "delete".equals(reason))
			{
				{
					RoleCRUD roleCRUD = (RoleCRUD)EJBLoader.getEJBObject("perpetual/RoleCRUD", RoleCRUDHome);
					try
					{
						roleVO = roleCRUD.retrieveByPrimaryKey(new RoleVO(reasonForm.getId()));
						request.setAttribute("selectedRole", roleVO);
					}
					finally
					{
						roleCRUD.remove();
					}
				}
				if("edit".equals(reason))
				{                        
					sLog.debug("This is for a Domain Update !");
					//get facilities for this Domain
					Collection takenFacilities = getFacilitiesByDomain(domainVO);
					//get severities for this Domain
					Collection takenSeverities = getSeveritiesByDomain(domainVO);                                   
					//get hosts for this Domain
					Collection takenHosts = getHostsByDomain(domainVO);
					///get facilities not in Domain
					Collection freeFacilities = getFacilitiesNotInDomain(domainVO);
					//get severities not this Domain
					Collection freeSeverities = getSeveritiesNotInDomain(domainVO);
					//get hosts not in Domain
					Collection freeHosts = getHostsNotInDomain(domainVO);

//					HttpSession sessionObj = request.getSession();
					request.setAttribute("DomainVO", domainVO);  
					request.setAttribute("takenFacilityList", takenFacilities);  
					request.setAttribute("takenSeverityList", takenSeverities);  
					request.setAttribute("takenHostList", takenHosts);  
					request.setAttribute("freeFacilityList", freeFacilities);  
					request.setAttribute("freeSeverityList", freeSeverities); 
					request.setAttribute("freeHostList", freeHosts);  
				}
			} 
			else if ("add".equals(reason))
			{
				domainVO = new DomainVO(-1);
				request.setAttribute("DomainVO", domainVO);
				//get list of facilities
				Collection facilityList = getFacilities();
				//get list of severitie
				Collection severityList = getSeverities();
				//get list of hosts
				Collection hostList = getHosts();

				request.setAttribute("facilityList",facilityList);
				request.setAttribute("severityList",severityList);
				request.setAttribute("hostList",hostList);
			}

			if ("add".equals(reason) || "edit".equals(reason)) {

				// grab data for Domain_X tables
			}
*/

			return mapping.findForward(reason);
		}
		catch(Exception e)
		{
			sLog.error("error SelectRoleForm.perform()", e);
		}

		return mapping.findForward("failure");
	}
}




