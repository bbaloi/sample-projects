package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com, others
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;


public class SelectDomainAdminAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		SelectDomainForm selectDomainForm = (SelectDomainForm)form;

		try
		{
			DomainCRUD domainCRUD = (DomainCRUD)EJBLoader.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
			try
			{
				UserCRUD userCRUD = (UserCRUD)EJBLoader.createEJBObject("perpetual/UserCRUD", UserCRUDHome.class);
				try
				{
					int domainId = selectDomainForm.getSelectedDomain();
					DomainVO domainVO = domainCRUD.retrieveByPrimaryKey(new DomainVO(domainId));
					request.setAttribute("DomainVO", domainVO);

					Collection userVOs = userCRUD.retrieveAllByDomainName(domainVO.getName());
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

			return mapping.findForward("success");
		}
		catch(Exception e)
		{
			sLog.error("error SelectUserForm.perform()", e);
		}

		return mapping.findForward("failure");
	}
}




