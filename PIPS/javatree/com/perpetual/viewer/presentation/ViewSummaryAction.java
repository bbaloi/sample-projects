/*
 * ViewSummaryAction.java
 *
 * Created on July 9, 2003, 11:45 AM
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
public class ViewSummaryAction extends PerpetualAction 
{
    
    /** Creates a new instance of ViewSummaryAction */
    public ViewSummaryAction() 
    {
    }
     private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSummaryAction.class );
    
    /** Creates a new instance of ViewLogstoreAction */
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
         return (mapping.findForward("viewsummary.success"));
    }
   
    
}
