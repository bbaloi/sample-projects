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
import com.perpetual.viewer.model.vo.DomainVO;


public class PreMaintainDomainsAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PreMaintainDomainsAction.class );
 
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			// no form - just retrieve all users

			DomainCRUD domainCRUD = (DomainCRUD)EJBLoader.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
			try
			{
				Collection domainVOCollection = domainCRUD.retrieveAll();

				this.session.setAttribute("DomainVOCollection", removeRootDomain(domainVOCollection));
			}
			finally
			{
				domainCRUD.remove();
			}

			return mapping.findForward("preMaintainDomains.success");   	
		}
		catch(Exception ex)
		{
			sLog.error("Problem with PreMaintainDomainsAction", ex);
		}

		return mapping.findForward("preMaintainDomains.failure");
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


