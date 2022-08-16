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
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;

public class DeleteUserAction extends PerpetualAction 
{
    
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( DeleteUserAction.class );

	private UserCRUDHome crudHome = null;    
    
    public DeleteUserAction()
    {
    }
    
    public ActionForward doAction (ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {

		ActionForward forward;
        
        try
        {
			UserActionForm userForm = (UserActionForm) form;

			if (this.crudHome == null) {
				InitialContext context = new InitialContext();

				Object reference = context.lookup("perpetual/UserCRUD");

				this.crudHome = (UserCRUDHome) PortableRemoteObject.narrow(
						reference, UserCRUDHome.class);
			}

			UserCRUD userCrud = crudHome.create();

			// get the user from the Session
			userCrud.delete(userForm.getId());

			forward = mapping.findForward("deleteuser.success");
        }
        catch(Exception ex)
        {
            forward = mapping.findForward("deleteuser.failure");
        }
        
        return forward;
    }
}
