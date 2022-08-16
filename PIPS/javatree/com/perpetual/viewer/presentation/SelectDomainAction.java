package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com, others
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.model.vo.DomainVO;

import com.perpetual.util.ServiceLocator;
import com.perpetual.util.Constants;

import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.MessagePatternHome;
import com.perpetual.viewer.model.ejb.SeverityHome;


import java.util.Collection;

public class SelectDomainAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	private static HostHome lHostHome;
	private static FacilityHome lFacilityHome;
	private static SeverityHome lSeverityHome;
	private static MessagePatternHome lMessagePatternHome;
	private static DomainCRUDHome lDomainCrudHome;
	private static DomainCRUD lDomainCrud;
	private static Object homeObj;                
                
	public ActionForward doAction(ActionMapping mapping,
			ActionForm     form,
			HttpServletRequest request,
			HttpServletResponse response) 
	{
		ActionForward forward = null;
		SelectDomainForm selectDomainForm = (SelectDomainForm)form;
		String reason = selectDomainForm.getReason();             

		try
		{
			DomainVO domainVO = null;

			if ("edit".equals(reason) || "delete".equals(reason))
			{
				DomainCRUD domainCRUD = (DomainCRUD)EJBLoader.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
				try
				{
					int domainId = selectDomainForm.getSelectedDomain();
					domainVO = domainCRUD.retrieveByPrimaryKey(new DomainVO(domainId));
					request.setAttribute("selectedDomain", domainVO);
					if("edit".equals(reason))
					{                        
						sLog.debug("This is for a Domain Update !");
						//get facilities for this Domain
						Collection takenFacilities = getFacilitiesByDomain(domainVO);
						//get severities for this Domain
						Collection takenSeverities = getSeveritiesByDomain(domainVO);                                   
						//get hosts for this Domain
						Collection takenHosts = getHostsByDomain(domainVO);
						// get hosts for this Domain
						Collection takenMessagePatterns =
								getMessagePatternsByDomain(domainVO);
						
						///get facilities not in Domain
						Collection freeFacilities = getFacilitiesNotInDomain(domainVO);
						//get severities not this Domain
						Collection freeSeverities = getSeveritiesNotInDomain(domainVO);
						//get hosts not in Domain
						Collection freeHosts = getHostsNotInDomain(domainVO);
						// get hosts for this Domain
						Collection freeMessagePatterns =
								getMessagePatternsNotInDomain(domainVO);

						HttpSession sessionObj =request.getSession();
						sessionObj.setAttribute("DomainVO",domainVO);  
						
						sessionObj.setAttribute("takenFacilityList",takenFacilities);  
						sessionObj.setAttribute("takenSeverityList",takenSeverities);  
						sessionObj.setAttribute("takenHostList",takenHosts);
						sessionObj.setAttribute("takenMessagePatternList",takenMessagePatterns);
						  
						sessionObj.setAttribute("freeFacilityList",freeFacilities);  
						sessionObj.setAttribute("freeSeverityList",freeSeverities); 
						sessionObj.setAttribute("freeHostList",freeHosts);
						sessionObj.setAttribute("freeMessagePatternList", freeMessagePatterns);

//						request.setAttribute("takenUserList", domainCRUD.retrieveAllUsersFor(domainId));
//						request.setAttribute("freeUserList", domainCRUD.retrieveAllUsersNotIn(domainId));
					}
				}
				finally
				{
					domainCRUD.remove();
				}
			} 
			else if ("add".equals(reason))
			{
				UserCRUD userCRUD = (UserCRUD)EJBLoader.createEJBObject("perpetual/UserCRUD", UserCRUDHome.class);
				try
				{
					domainVO = new DomainVO(-1);
					request.setAttribute("DomainVO", domainVO);
					//get list of facilities
					Collection facilityList = getFacilities();
					//get list of severitie
					Collection severityList = getSeverities();
					//get list of hosts
					Collection hostList = getHosts();
					
					Collection messagePatternList = getMessagePatterns();

					request.setAttribute("facilityList",facilityList);
					request.setAttribute("severityList",severityList);
					request.setAttribute("hostList", hostList);
					request.setAttribute("messagePatternList", messagePatternList);
					request.setAttribute("userList", userCRUD.retrieveAll());
				}
				finally
				{
					userCRUD.remove();
				}

			}

			if ("add".equals(reason) || "edit".equals(reason)) {

				// grab data for Domain_X tables
			}

			forward = mapping.findForward(reason);

			sLog.debug("formarding to " + forward);

		}
		catch(Exception e)
		{
			sLog.error("error SelectUserForm.perform()", e);
			forward = mapping.findForward("failure");
		}

		return forward;
	}
        
        private static Collection getHosts()
        {
             Collection hostList=null;
             if(lHostHome==null)
             {
                try
                {
                    homeObj = ServiceLocator.findHome(Constants.jndiName_Host);
                    lHostHome = (HostHome) PortableRemoteObject.narrow(homeObj,HostHome.class); 
                    sLog.debug("got HostHome");            
                }
                catch(javax.naming.NamingException excp)
                {
                    String errMsg ="Could not instantiate Host Home interfaces:";
                    sLog.error(errMsg);
                }
             }
             try
             {
                hostList = lHostHome.findAll();
             }
             catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not getHosts";
                sLog.error(errMsg);
             }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not fget Hosts";
                sLog.error(errMsg);
             }
                return hostList;
        }
        private static Collection getFacilities()
        {
            Collection facilityList=null;
             if(lFacilityHome==null)
             {
                try
                {
                    homeObj = ServiceLocator.findHome(Constants.jndiName_Facility);
                    lFacilityHome = (FacilityHome) PortableRemoteObject.narrow(homeObj,FacilityHome.class); 
                    sLog.debug("got FacilityHome");            
                }
                catch(javax.naming.NamingException excp)
                {
                    String errMsg ="Could not instantiate Facility Home interfaces:";
                    sLog.error(errMsg);
                }
             }
            try
            {
             facilityList = lFacilityHome.findAll();            
            }
             catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not getHosts";
                sLog.error(errMsg);
             }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not fget Hosts";
                sLog.error(errMsg);
             }
             return facilityList;
        }
        private static Collection getSeverities()
        {
            Collection severityList=null;
             if(lSeverityHome==null)
             {
                try
                {
                    homeObj = ServiceLocator.findHome(Constants.jndiName_Severity);
                    lSeverityHome = (SeverityHome) PortableRemoteObject.narrow(homeObj,SeverityHome.class); 
                    sLog.debug("got SeverityHome");            
                }
                catch(javax.naming.NamingException excp)
                {
                    String errMsg ="Could not instantiate Severity Home interfaces:";
                    sLog.error(errMsg);
                }
             }
            try
            {
             severityList = lSeverityHome.findAll();
            }
             catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not getHosts";
                sLog.error(errMsg);
             }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not fget Hosts";
                sLog.error(errMsg);
             }
             return severityList;
        }
        
	private static Collection getMessagePatterns()
	{

		Collection messagePatternList = null;
		 
		if (lMessagePatternHome == null)
		{
			try
			{
				homeObj = ServiceLocator.findHome(Constants.jndiName_MessagePattern);
				lMessagePatternHome = (MessagePatternHome)
						PortableRemoteObject.narrow(homeObj, MessagePatternHome.class); 
				sLog.debug("got Message Pattern Home");            
			}
			catch(javax.naming.NamingException excp)
			{
				String errMsg ="Could not instantiate Message Pattern Home interfaces:";
				sLog.error(errMsg);
			}
		}
		
		try
		{
			messagePatternList = lMessagePatternHome.findAll();
		}
		catch(javax.ejb.FinderException excp)
		{
			String errMsg="Could not get message patterns";
			sLog.error(errMsg);
		 }
		catch(java.rmi.RemoteException excp)
		{
			String errMsg="Could not fget message patterns";
			sLog.error(errMsg);
		 }
		 
		 return messagePatternList;
	}

        private Collection getFacilitiesByDomain(DomainVO pDomain)
        {
            sLog.debug("getting Facilities By Domain");
            Collection facilityList=null;
            setDomainCrud();
            try
            {
                facilityList = lDomainCrud.retrieveAllFacilitiesFor(pDomain);           
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
            return facilityList;
            
        }
        private Collection getSeveritiesByDomain(DomainVO pDomain)
        {
            sLog.debug("getting Severities By Domain");
            Collection severityList=null;
            setDomainCrud();
            try
            {
                severityList = lDomainCrud.retrieveAllSeveritiesFor(pDomain);   
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
            
            return severityList;
        }
        private Collection getHostsByDomain(DomainVO pDomain)
        {
            sLog.debug("getting Hosts By Domain");
            Collection hostList=null;
            setDomainCrud();
            try
            {
                hostList = lDomainCrud.retrieveAllHostsFor(pDomain);   
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
            
            return hostList;
        }
        
		private Collection getMessagePatternsByDomain (DomainVO pDomain)
		{
			sLog.debug("getting Message Patterns By Domain");
			Collection messagePatternsList = null;
			setDomainCrud();
			
			try
			{
				messagePatternsList = lDomainCrud.retrieveAllMessagePatternsFor(pDomain);   
			}
			catch(javax.ejb.FinderException excp)
			{
				String errMsg="Could not find Domain Messages for "
						+ pDomain.getName();
				sLog.error(errMsg);                
			}
			catch(java.rmi.RemoteException excp)
			{
				String errMsg="Could not find Domain Messages for "
					+ pDomain.getName();
				sLog.error(errMsg,excp);          
			}
		        
			return messagePatternsList;
		}
        
        
         private Collection getFacilitiesNotInDomain(DomainVO pDomain)
        {
            sLog.debug("getting Facilities Not in Domain");
            Collection facilityList=null;
           setDomainCrud();
            try
            {
                facilityList = lDomainCrud.retrieveAllFacilitiesNotIn(pDomain);           
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
             return facilityList;
            
        }
        private Collection getSeveritiesNotInDomain(DomainVO pDomain)
        {
            sLog.debug("getting Severities Not in Domain");
            Collection severityList=null;
           
            setDomainCrud();
            try
            {
                severityList = lDomainCrud.retrieveAllSeveritiesNotIn(pDomain);   
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
            return severityList;
        }
        
        private Collection getHostsNotInDomain(DomainVO pDomain)
        {
            sLog.debug("getting Hosts Not in Domain");
            Collection hostList=null;
            
            setDomainCrud();
            try
            {
                hostList = lDomainCrud.retrieveAllHostsNotIn(pDomain);   
            }
            catch(javax.ejb.FinderException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg);                
            }
            catch(java.rmi.RemoteException excp)
            {
                String errMsg="Could not find Domain Facilities for "+pDomain.getName();
                sLog.error(errMsg,excp);          
            }
            return hostList;
        }
        
		private Collection getMessagePatternsNotInDomain(DomainVO pDomain)
		{
			sLog.debug("getting Domain Messages Not in Domain");
			
			Collection hostList = null;
		        
			setDomainCrud();
			try
			{
				hostList = lDomainCrud.retrieveAllMessagePatternsNotIn(pDomain);   
			}
			catch(javax.ejb.FinderException excp)
			{
				String errMsg="Could not find Domain Messages for "+pDomain.getName();
				sLog.error(errMsg);                
			}
			catch(java.rmi.RemoteException excp)
			{
				String errMsg="Could not find Domain Messages for "+pDomain.getName();
				sLog.error(errMsg,excp);          
			}
			return hostList;
		}
        
       private static void setDomainCrud()
       {
           sLog.debug("Setting the Domain CRUD !");
           if(lDomainCrudHome==null)
                 {
                    try
                    {
                        homeObj = ServiceLocator.findHome(Constants.jndiName_DomainCRUD);
                        lDomainCrudHome = (DomainCRUDHome) PortableRemoteObject.narrow(homeObj,DomainCRUDHome.class); 
                        sLog.debug("got DomainCRUDHome");      
                        lDomainCrud = lDomainCrudHome.create();
                        sLog.debug("Created Domain CRUD");
                    }
                    catch(javax.naming.NamingException excp)
                    {
                        String errMsg ="Could not find DomainCrud Home";
                        sLog.error(errMsg);
                    }
                    catch(javax.ejb.CreateException excp)
                    {
                        String errMsg ="Could not instantiate DomainCRUD ";
                        sLog.error(errMsg);
                    }
                    catch(java.rmi.RemoteException excp)
                    {
                        String errMsg ="Could not instantiate DomainCRUD ";
                        sLog.error(errMsg);
                    }
                }
       }
                
}



