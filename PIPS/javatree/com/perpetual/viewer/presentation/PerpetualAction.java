package com.perpetual.viewer.presentation;

import java.rmi.RemoteException;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.EJBLoader;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.util.ValidationEngine;


public abstract class PerpetualAction extends Action
{
    private static PerpetualC2Logger sLog =
    		new PerpetualC2Logger( PerpetualAction.class );
 
 	protected HttpSession session = null;
 	protected UserProfileVO userProfile = null;

    public PerpetualAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) 
    {
    	ActionForward forward = null;
    	
		sLog.debug("in execute() for " + this.getClass().getName());    	
        //embed security verification in this base class - prior to execution
		if (validateSession(request)) {
			if (validate(userProfile)) {
				forward = doAction(mapping, form, request, response);
			} else {			
				sLog.error("User '" + userProfile.getUserName()
					+ "' attempted to perform an invalid action.");
				sLog.error("  Disallowed action was " + getClass().getName()
					+ " role was " + userProfile.getRoleId());
				forward = mapping.findForward("validation.failure");   
			}
		} else {
			sLog.error("Invalid session - user must log in again.");
			forward = mapping.findForward("invalid.session");
		}
		
		return forward;
    }
    
    
    // subclasses override this method
    
    protected abstract ActionForward doAction (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response);
    
    protected boolean validateSession (HttpServletRequest request)
    {   
    	boolean result = false;
	
	 	sLog.debug("Validating session ...");    	
    	this.session = request.getSession();
    	
    	if (this.session != null) {
				sLog.debug("Validating session, session = " + session);    		
				this.userProfile = (UserProfileVO)
					this.session.getAttribute("UserProfile");
				
				result = (userProfile != null);
				sLog.debug("Validating session, user profile = " + userProfile);
    	}
    	
		sLog.debug("Validating session, valid = " + result);    	

    	return result;
    }
    
    private boolean validate (UserProfileVO userProfileVO) 
    {
    	boolean validated = false;
    	boolean isEnabled = false;
    	int roleId = userProfileVO.getRoleId();


		sLog.debug("Validating permission to execute action for role id = "
				+ roleId);
		
		// root id = 0, it can do everything
				
		if (roleId == 0) {
			// root is always enabled and always validated
			sLog.debug("Root user validated");
	    	validated = true;
	    	isEnabled = true;
		} else {
			// see if user is enabled - may have been disabled due to a
			// domain deletion or some other action that has occured after
			// this user has logged in
			
			UserCRUD userCRUD = null;
			UserVO userVO = null;
				
			try {
				userCRUD = (UserCRUD) EJBLoader
						.createEJBObject("perpetual/UserCRUD", UserCRUDHome.class);

				userVO = userCRUD.retrieveByPrimaryKey(
						userProfileVO.getUserId());
						
				isEnabled = userVO.isEnabled();			
				sLog.debug("User enabled = " + isEnabled);	
			} catch (RemoteException e) {
				isEnabled = false;
			} catch (FinderException e) {
				isEnabled = false;
			} finally {
				try {
					if (userCRUD == null) { 
						userCRUD.remove();
					}
				} catch (RemoteException e1) {
				} catch (RemoveException e1) {
				}
			}

			if (isEnabled) {			
				String actionName=null;
		        //here do the crazy ass validation of useris to role and action !!!        
		        //.....check the Privillegies cache...
		        String fullActionName = this.getClass().getName();
		         sLog.debug("validating role:"+roleId+" in Action:"+fullActionName);
		        int index = fullActionName.lastIndexOf(".");
		        actionName = fullActionName.substring(index+1,fullActionName.length());
		        sLog.debug("stripped ActionName:"+actionName);
		        try
		        {
		            validated = ((ValidationEngine.getInstance())
		            		.isValid(roleId,actionName));
		        }
		        catch(BasePerpetualException excp)
		        {
		            sLog.error("Invalid action for user !");
		        }
			}
		}  
		
		return isEnabled && validated;
    }
}
