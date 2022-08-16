/*
 * ViewLogAction.java
 *
 * Created on May 18, 2003, 10:08 PM
 */

package com.perpetual.viewer.presentation;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.perpetual.viewer.control.SyslogViewerDelegate;
import com.perpetual.viewer.control.SyslogViewerDelegateImpl;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.util.ViewerGlobals;
import java.util.Collection;
import java.util.HashMap;

import com.perpetual.viewer.model.vo.SyslogFileVO;
/**
 *
 * @author  brunob
 */
public class ViewSyslogAction extends Action
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );
    
    /** Creates a new instance of ViewLogAction */
    public ViewSyslogAction() {
    }
    public ActionForward execute(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
        HashMap lQueryParameters=null;
        
        try{
            
          /*
           *  Creating a Story Data Access Object and using it to retrieve
           *  all of the top stories.
           */
            sLog.debug("******I AM IN ViewSyslogAction********");
            //here we need to determine whether this is impl or ejb from some properties file
            SyslogViewerDelegate syslogViewer = new SyslogViewerDelegateImpl();
            
            String fileName = ((ViewSyslogForm)form).getFileName();
            sLog.debug("The LogFile requested is:"+fileName);
            //get query Parameters
            
            Collection logFile = syslogViewer.readSyslog(fileName,lQueryParameters);
            
         /*
           *  Putting the collection containing the top stories into the
           *  request
           */
            SyslogFileVO fileVO = new SyslogFileVO(logFile,fileName);
            request.setAttribute("logFile", fileVO);
            
        } catch(Exception e) {
            sLog.error("error inViewSyslogAction.perform()");
            e.printStackTrace();
            return (mapping.findForward("system.error"));
        }
        
        sLog.debug("formarding to logContent.jsp");
        return (mapping.findForward("viewlog.success"));
    }
    
}
