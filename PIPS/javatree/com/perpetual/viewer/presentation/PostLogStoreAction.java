/*
 * ViewLogAction.java
 *
 * Created on May 18, 2003, 10:08 PM
 */

package com.perpetual.viewer.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;


/**
 *
 * @author  brunob
 */
public class PostLogStoreAction extends PerpetualAction
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LoginAction.class );
    
    /** Creates a new instance of ViewLogAction */
    public PostLogStoreAction() {
    }
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
        
        //here got to get values for Hosts via the HostCRUD session bean....+ 
        //oher type of data like time stamps & crap
        //=====fill in a value bean and put in the HttpRequest object ....
        //then forward to the page
        
      
         sLog.debug("forwarding to Log Store Viewing JSP !");
         return (mapping.findForward("route.success"));
    }
    
}



