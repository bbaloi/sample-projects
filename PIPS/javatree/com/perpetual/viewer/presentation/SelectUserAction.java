package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.servlet.http.*;
import javax.rmi.*;

import org.apache.struts.action.*;

import com.perpetual.exception.PerpetualException;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.vo.RoleVO;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUDHome;
import com.perpetual.viewer.control.ejb.crud.role.RoleCRUD;


public class SelectUserAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( SelectUserAction.class );

	public ActionForward doAction(ActionMapping mapping,
			ActionForm     form,
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		SelectUserForm selectUserForm = (SelectUserForm)form;
		String reason = selectUserForm.getReason();
		try
		{
			UserVO userVO = null;

			if ("edit".equals(reason) || "delete".equals(reason))
			{
				{
					int id = selectUserForm.getSelectedUser();

					if (id == -1) {
						throw new PerpetualException("no selected user");
					} else {
						
						UserCRUDHome home = (UserCRUDHome)PortableRemoteObject.narrow(ServiceLocator.getServiceLocatorInstance().getContext().lookup("perpetual/UserCRUD"),  UserCRUDHome.class);
						UserCRUD userCRUD = home.create();
						try
						{
							userVO = userCRUD.retrieveByPrimaryKey(new UserVO(id));
							request.setAttribute("selectedUser", userVO);
						}
						finally
						{
							userCRUD.remove();
						}
					}
				}
			} else if ("add".equals(reason)){
				userVO = new UserVO(-1);
				request.setAttribute("selectedUser", userVO);
			}
			
			if (!"delete".equals(reason)) {

				RoleCRUDHome home = (RoleCRUDHome)PortableRemoteObject.narrow(
					ServiceLocator.getServiceLocatorInstance().
					getContext().lookup("perpetual/RoleCRUD"),  RoleCRUDHome.class);
				
				RoleCRUD roleCRUD = home.create();
				
				try
				{
					request.setAttribute("roleCollection", removeRootRole(roleCRUD.retrieveAll()));
					
					if (!"add".equals(reason)) {
						RoleVO roleVO = roleCRUD.retrieveByPrimaryKey(userVO.getRoleId());
						request.setAttribute("selectedRole", roleVO);
					}
				}
				finally
				{
					roleCRUD.remove();
				}
				
				DomainCRUDHome domainCRUDHome = (DomainCRUDHome)PortableRemoteObject.narrow(
					ServiceLocator.getServiceLocatorInstance().
					getContext().lookup("perpetual/DomainCRUD"),  DomainCRUDHome.class);
				
				DomainCRUD domainCRUD = domainCRUDHome.create();
				
				try
				{
					request.setAttribute("domainCollection", removeRootDomain(domainCRUD.retrieveAll()));
					
					if (!"add".equals(reason) && (userVO.getServiceDomain() != null
								&& userVO.getServiceDomain().length() > 0)) {							
							DomainVO domainVO = domainCRUD.retrieveByDomainName(
									userVO.getServiceDomain());
							request.setAttribute("selectedDomain", domainVO);
					} else {
						// there is no domain for this user
						DomainVO domainVO = new DomainVO(-1);
						request.setAttribute("selectedDomain", domainVO);
					}
				}
				finally
				{
					domainCRUD.remove();
				}
			}
		}
		catch(Exception e)
		{
			sLog.error("error SelectUserForm.perform()", e);
			return (mapping.findForward("failure"));
		}
//System.out.println("!!!!!!!! " + selectedUser.getReason());

		ActionForward forward = mapping.findForward(reason);
		sLog.debug("formarding to " + forward);
		return forward;
	}
	
	
	private Collection removeRootRole (Collection vos)
	{
		RoleVO rootVO = null;
		
		
		for (Iterator i = vos.iterator(); rootVO == null && i.hasNext(); ) {
		
			RoleVO vo = (RoleVO) i.next();
			
			if (vo.getId() == 0) {
				rootVO = vo;
			}
		}
		
		boolean deleted = vos.remove(rootVO);
		
		sLog.info("removing root role from collection: " + deleted);
		
		return vos;
	}
	
	private Collection removeRootDomain (Collection vos)
	{
		DomainVO rootVO = null;
		
		
		for (Iterator i = vos.iterator(); rootVO == null && i.hasNext(); ) {
		
			DomainVO vo = (DomainVO) i.next();
			
			if (vo.getId() == 0) {
				rootVO = vo;
			}
		}
		
		boolean deleted = vos.remove(rootVO);
		
		sLog.info("removing root domain from collection: " + deleted);
		
		return vos;
	}

}



