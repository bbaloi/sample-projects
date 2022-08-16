/*
 * ViewLogstoreAction.java
 *
 * Created on July 5, 2003, 1:03 PM
 */

package com.perpetual.viewer.presentation;

import java.util.Collection;
import java.util.Iterator;

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
import com.perpetual.viewer.model.vo.UserVO;


public class PreMaintainUsersAction extends PerpetualAction 
{
    private static PerpetualC2Logger sLog =
    		new PerpetualC2Logger( PreMaintainUsersAction.class );
 
 	private UserCRUDHome crudHome = null;
    
    public PreMaintainUsersAction()
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
			// no form - just retrieve all users

			if (this.crudHome == null) {
				InitialContext context = new InitialContext();

				Object reference = context.lookup("perpetual/UserCRUD");

				this.crudHome = (UserCRUDHome) PortableRemoteObject.narrow(
						reference, UserCRUDHome.class);
			}

			UserCRUD userCrud = crudHome.create();

			Collection userVOCollection = userCrud.retrieveAll();
			
			// remove root user - id = 0
			// don't do maintenance on that from here
	
			removeRootUser(userVOCollection);		
	
			this.session.setAttribute("UserVOCollection", userVOCollection);

			forward = mapping.findForward("preMaintainUsers.success");   	
		}
		catch(Exception ex)
		{
			sLog.error("Couldn't get list of users: " + ex);
			forward = mapping.findForward("preMaintainUsers.failure");
		}

		return forward;
	}

	private void removeRootUser (Collection vos)
	{
		UserVO rootVO = null;
		
		
		for (Iterator i = vos.iterator(); rootVO == null && i.hasNext(); ) {
		
			UserVO vo = (UserVO) i.next();
			
			if (vo.getId() == 0) {
				rootVO = vo;
			}
		}
		
		boolean deleted = vos.remove(rootVO);
		
		sLog.error("removing root user from collection: " + deleted);
	}
}