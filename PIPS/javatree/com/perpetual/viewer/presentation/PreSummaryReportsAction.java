
package com.perpetual.viewer.presentation;

import java.util.Collection;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;


public class PreSummaryReportsAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog =
    		new PerpetualC2Logger( PreSummaryReportsAction.class );
 
 	private DomainCRUDHome crudHome = null;
    
    public PreSummaryReportsAction()
    {
    }
    
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
	{
		return mapping.findForward("success");
	}
}
