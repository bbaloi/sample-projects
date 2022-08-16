/*
 * ViewLogAction.java
 *
 * Created on May 18, 2003, 10:08 PM
 */

package com.perpetual.viewer.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.MD5Utils;
import com.perpetual.viewer.control.LoginDelegate;
import com.perpetual.viewer.control.LoginDelegateImpl;
import com.perpetual.viewer.control.exceptions.ViewerLoginException;
import com.perpetual.viewer.model.vo.UserProfileVO;
/**
 *
 * @author  brunob
 */

// This a little different from the other actions in that there is no session established
// and all users can perform this action.
// There provide our own implementation of execute() with a null doAction().

public class LoginAction extends Action
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LoginAction.class );
    
    /** Creates a new instance of ViewLogAction */
    public LoginAction() {
    }
    public ActionForward execute(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
               
        try
		{
            
          /*
           *  Creating a Story Data Access Object and using it to retrieve
           *  all of the top stories.
           */
            //if(sLog.isEnabledFor(Priority.DEBUG))
                sLog.debug("******Attempting to Log in !********");
            //here we need to determine whether this is impl or ejb from some properties file
            LoginDelegate login = new LoginDelegateImpl(); 
            
            String userId = ((LoginForm)form).getUserId();
            String password  = ((LoginForm)form).getPassword();
			password = MD5Utils.encodePassword(password);
            //if(sLog.isEnabledFor(Priority.DEBUG))
            //{
              sLog.debug("Uid:"+userId);
            //}
            //get query Parameters
            
            UserProfileVO userProfile = login.login(userId,password);
            
         /*
           *  Putting the collection containing the top stories into the
           *  requestd
           */
            HttpSession session = request.getSession(true);
            session.setAttribute("UserProfile", userProfile);
            //request.setAttribute("UserProfile", userProfile);
            
        } 
        catch(Exception e) 
        {
            sLog.error("error inViewSyslogAction.perform()");
            e.printStackTrace();
            return (mapping.findForward("login.failure"));
        }
         sLog.debug("formarding to main.jsp");
         return (mapping.findForward("login.success"));
    }
    
}
