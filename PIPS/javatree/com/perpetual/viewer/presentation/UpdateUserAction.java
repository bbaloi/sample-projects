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
import com.perpetual.util.MD5Utils;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.model.vo.UserVO;

public class UpdateUserAction extends PerpetualAction 
{
    
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( UpdateUserAction.class );

	private UserCRUDHome crudHome = null;    
    
    public UpdateUserAction()
    {
    }
    
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {

		ActionForward forward;
		UserCRUD userCrud = null;
        
        try
        {
			if (this.crudHome == null)
			{
				InitialContext context = new InitialContext();

				Object reference = context.lookup("perpetual/UserCRUD");

				this.crudHome = (UserCRUDHome) PortableRemoteObject.narrow(reference, UserCRUDHome.class);
			}

			userCrud = crudHome.create();
			try
			{
				UserActionForm userForm = (UserActionForm) form;

				sLog.debug("UpdateUserAction ...");

				String password = userForm.getPassword();
				if ("***".equals(password))
					password = null;
				else
					password = MD5Utils.encodePassword(password);

				UserVO vo = new UserVO(userForm.getUserId(),
						password,						// UserCRUD will not set password it it's null
						userForm.getServiceDomain(),
						userForm.getPhone(),
						userForm.getEmail(),
						userForm.getRealname(),
						userForm.getRoleId(),
						userForm.getId(),
						userForm.getEnabled());
	  
				// add the user
				userCrud.update(vo);

				return mapping.findForward("updateuser.success");
			}
			finally
			{
				userCrud.remove();
			}
        }
        catch(Exception ex)
        {
			sLog.error("Problem", ex);
            return mapping.findForward("updateuser.failure");
        }
    }
}
