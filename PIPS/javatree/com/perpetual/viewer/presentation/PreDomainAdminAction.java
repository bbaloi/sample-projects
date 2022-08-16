package com.perpetual.viewer.presentation;

import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.UserVO;


public class PreDomainAdminAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PreMaintainDomainsAction.class );
 
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		ActionForward forward = null;
		
		try
		{
			// examine user id 
			// if user=root, 
			// 		should go to a domain selection screen
			// if user!=root, should go to the enable/disable users screen
			// 		for that domain
			
			UserProfileVO userProfileVO = (UserProfileVO) request
					.getSession().getAttribute("UserProfile");
			
			int userId = userProfileVO.getUserId();
			
			if (userId == 0) {
				// root user				
				DomainCRUD domainCRUD = (DomainCRUD) EJBLoader
						.createEJBObject("perpetual/DomainCRUD",
						DomainCRUDHome.class);
				try
				{
					Collection domainVOCollection = domainCRUD.retrieveAll();

					this.session.setAttribute("DomainVOCollection",
							removeRootDomain(domainVOCollection));
				}
				finally
				{
					domainCRUD.remove();
				}
				
				forward = mapping.findForward("domainselect");		
				
			} else {				
				// non-root user
				// do the same things as SelectDomainAdminAction

				DomainCRUD domainCRUD = (DomainCRUD)EJBLoader
					.createEJBObject("perpetual/DomainCRUD",
						DomainCRUDHome.class);
				try
				{
					UserCRUD userCRUD = (UserCRUD)EJBLoader
							.createEJBObject("perpetual/UserCRUD",
								UserCRUDHome.class);
					try
					{
						int domainId = userProfileVO.getDomainId();
						DomainVO domainVO = domainCRUD.retrieveByPrimaryKey(new DomainVO(domainId));
						request.setAttribute("DomainVO", domainVO);

						Collection userVOs = userCRUD
								.retrieveAllByDomainName(domainVO.getName());
						Collection enableUserList = new ArrayList();
						for (Iterator i = userVOs.iterator(); i.hasNext(); )
						{
							UserVO userVO = (UserVO)i.next();
							if (!userVO.isEnabled())
								enableUserList.add(userVO);
						}
						userVOs.removeAll(enableUserList);
						request.setAttribute("enableUserList", enableUserList);	//domainCRUD.retrieveAllEnabledUsersFor(domainId, false));
						request.setAttribute("disableUserList", userVOs);	//domainCRUD.retrieveAllEnabledUsersFor(domainId, true));
					}
					finally
					{
						userCRUD.remove();
					}
				}
				finally
				{
					domainCRUD.remove();
				}
				
				forward = mapping.findForward("domainadmin");					
			}	   	
		}
		catch(Exception ex)
		{
			sLog.error("Problem with PreMaintainDomainsAction", ex);
			forward = mapping.findForward("failure");
		}

		return forward;
	}

	private Collection removeRootDomain (Collection vos)
	{
		for (Iterator i = vos.iterator(); i.hasNext(); )
		{
			DomainVO vo = (DomainVO) i.next();
			
			if (vo.getId() == 0)
				i.remove();
		}

		return vos;
	}
}



