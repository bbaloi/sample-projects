/*
 * ViewLogstoreAction.java
 *
 * Created on July 5, 2003, 1:03 PM
 */

package com.perpetual.viewer.presentation;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.perpetual.viewer.control.LoginDelegate;
import com.perpetual.viewer.control.LoginDelegateImpl;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.util.ViewerGlobals;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.exception.BasePerpetualException;
import java.util.Vector;

import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.util.ServiceLocator;
import com.perpetual.util.Constants;

import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.model.ejb.FacilityHome;

/**
 *
 * @author  brunob
 */
public class DomainMaintenanceAction extends PerpetualAction 
{
    
     private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewLogstoreAction.class );
     private static Object homeObj=null;
     private static SeverityHome lSeverityHome=null;
     private static FacilityHome lFacilityHome=null;
     private static HostHome     lHostHome=null;
     
     static 
     {
         /*
          homeObj=ServiceLocator.findHome(Constants.jndiName_Host);
          lHostHome = (HostHome) PortableRemoteObject.narrow(homeObj,HostHome.class);
          sLog.debug("got HostHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_Facility);
          lFacilityHome = (HostHome) PortableRemoteObject.narrow(homeObj,FacilityHome.class);
          sLog.debug("got FacilityHome");         
          homeObj = ServiceLocator.findHome(Constants.jndiName_Severity);
          lFacilityHome = (HostHome) PortableRemoteObject.narrow(homeObj,SeverityHome.class);
          sLog.debug("got SeverityHome");  
          **/
     }
        
     
    /** Creates a new instance of ViewLogstoreActio
     */
     public DomainMaintenanceAction()
    {
    }
    public ActionForward doAction(ActionMapping mapping,
                                    ActionForm     form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) 
    {
	    try
        {
            sLog.debug("I'm in DomainMaintenanceAction...preparing the lists to display");
                                        
            //should go to DomainCrud but since none is available - I will build the list statically
                  
           /*Collection hostList =    (Collection) lHostHome.findAll();
            Collection facilityList = (Collection) lFacilityHome.findAll();
            Collection severityList = (Collection)lSeverityHome.findAll();
            **/               
            
            Vector hostList = new Vector();
            hostList.add(new HostVO(0,"testbox1", "test box 1 description"));
            hostList.add(new HostVO(1,"testbox2", "da test boxo dos amigo"));
            hostList.add(new HostVO(2,"testbox3", "boxa 3 notta working no more"));
            Vector facilityList = new Vector();
            facilityList.add(new FacilityVO(0,"daemon"));
            facilityList.add(new FacilityVO(1,"kernell"));
            facilityList.add(new FacilityVO(2,"looser"));
            facilityList.add(new FacilityVO(3,"security"));
            
            Vector severityList = new Vector();
            severityList.add(new SeverityVO(0,"critical"));
            severityList.add(new SeverityVO(0,"warning"));
            severityList.add(new SeverityVO(0,"error"));
            severityList.add(new SeverityVO(0,"info"));
            
            request.setAttribute("hostList",hostList);  
            request.setAttribute("facilityList",severityList);  
            request.setAttribute("severityList",facilityList);                  
            
                           
        }
        catch(Exception ex)
        {
            sLog.error("Couldn't query the Syslog LogStore !");
            return (mapping.findForward("generic.failure"));
        }

		sLog.debug("forwarding to userMaintenance.jsp");
		return (mapping.findForward("domain_maintenance.success"));
	}
   
}
