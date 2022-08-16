package com.perpetual.viewer.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.SeverityHome;

public class LogoutAction extends Action
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger(
			LogoutAction.class );

    private static HostHome lHostHome = null;
    private static FacilityHome lFacilityHome=null;
    private static SeverityHome lSeverityHome=null;
    private static DomainCRUDHome lDomainCrudHome=null;
    private static DomainCRUD   lDomainCrud=null;
    private static Object homeObj=null;                
            
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) 
	{
		HttpSession sessionObj = request.getSession();
		ActionForward forward = null;
		LogoutActionForm logoutForm = (LogoutActionForm) form;
		
		if ("yes".equals(logoutForm.getReason())) {
			sessionObj.invalidate();
			forward = mapping.findForward("logout.success");
		} else if ("cancel".equals(logoutForm.getReason())) {
			forward = mapping.findForward("logout.cancel");
		}
		
		return forward;
	}
}



