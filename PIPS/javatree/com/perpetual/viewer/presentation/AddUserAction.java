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


public class AddUserAction extends PerpetualAction 
{
    
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( AddUserAction.class );

	private UserCRUDHome crudHome = null;    
    
    public AddUserAction()
    {
    }
    
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {

		ActionForward forward = null;
		
        try
        {
			if (this.crudHome == null) {
				InitialContext context = new InitialContext();

				Object reference = context.lookup("perpetual/UserCRUD");

				this.crudHome = (UserCRUDHome) PortableRemoteObject.narrow(
						reference, UserCRUDHome.class);
			}

			UserCRUD userCrud = crudHome.create();

        	UserActionForm userForm = (UserActionForm) form;
				 	
            sLog.debug("AddUserAction ...");
            
            // by default only users in the root role
            // and domain admin roles are enabled
            // others are enabled via domain admin screen
            boolean isEnabled = (userForm.getRoleId() == 0
            		|| userForm.getRoleId() == 1); 
            
            UserVO vo = new UserVO(userForm.getUserId(),
            		MD5Utils.encodePassword(userForm.getPassword()),
            		userForm.getServiceDomain(),
            		userForm.getPhone(),
            		userForm.getEmail(),
            		userForm.getRealname(),
            		userForm.getRoleId(),
            		-1,
            		isEnabled);
  
  			// add the user
  			userCrud.create(vo);
  			
			forward = mapping.findForward("adduser.success");
        }
        catch(Exception ex)
        {
            forward = mapping.findForward("adduser.failure");
        }
        
        return forward;
    }
}
