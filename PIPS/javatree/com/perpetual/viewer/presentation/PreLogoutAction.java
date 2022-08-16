package com.perpetual.viewer.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;


public class PreLogoutAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog =
    		new PerpetualC2Logger( PreLogoutAction.class );

    public PreLogoutAction()
    {
    }
    
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
	{
		ActionForward forward = null;
		// stubbed out - nothing really to do - just forward onto
		// logout JSP 

		forward = mapping.findForward("preLogout.success");   	

		return forward;
	}
}
