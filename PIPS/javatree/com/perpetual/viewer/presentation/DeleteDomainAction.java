/*
 * ViewLogstoreAction.java
 *
 * Created on July 5, 2003, 1:03 PM
 */

package com.perpetual.viewer.presentation;

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
import com.perpetual.viewer.model.vo.DomainVO;

public class DeleteDomainAction extends PerpetualAction 
{
    
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( DeleteDomainAction.class );

	private DomainCRUDHome crudHome = null;    
    
    public DeleteDomainAction()
    {
    }
    
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
		ActionForward forward;
	        
        try
        {
			DeleteDomainForm domainForm = (DeleteDomainForm) form;

			if (this.crudHome == null) {
				InitialContext context = new InitialContext();

				Object reference = context.lookup("perpetual/DomainCRUD");

				this.crudHome = (DomainCRUDHome) PortableRemoteObject.narrow(
						reference, DomainCRUDHome.class);
			}
			
			DomainCRUD domainCrud = crudHome.create();

			domainCrud.delete(new DomainVO(domainForm.getId()));

			forward = mapping.findForward("deletedomain.success");
        }
        catch(Exception ex)
        {
            forward = mapping.findForward("deletedomain.failure");
        }

        return forward;
    }
}
