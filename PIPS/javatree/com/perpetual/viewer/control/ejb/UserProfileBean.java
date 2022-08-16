/*
 * SyslogViewerDelegateBean.java
 *
 * Created on May 16, 2003, 3:43 PM
 */

package com.perpetual.viewer.control.ejb;

import javax.ejb.SessionBean;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.RoleVO;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.SessionDomainVO;
import com.perpetual.viewer.model.vo.SessionRoleVO;
import com.perpetual.viewer.model.ejb.Role;
import com.perpetual.viewer.model.ejb.RolePK;
import com.perpetual.viewer.model.ejb.RoleHome;
import com.perpetual.viewer.model.ejb.RoleActionHome;
import com.perpetual.viewer.model.ejb.RoleAction;
import com.perpetual.viewer.model.ejb.Domain;
import com.perpetual.viewer.model.ejb.DomainHome;
import com.perpetual.viewer.model.ejb.DomainHost;
import com.perpetual.viewer.model.ejb.DomainHostHome;
import com.perpetual.viewer.model.ejb.DomainFacility;
import com.perpetual.viewer.model.ejb.DomainFacilityHome;
import com.perpetual.viewer.model.ejb.DomainSeverity;
import com.perpetual.viewer.model.ejb.DomainSeverityHome;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostPK;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.Facility;
import com.perpetual.viewer.model.ejb.FacilityPK;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.Severity;
import com.perpetual.viewer.model.ejb.SeverityPK;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.model.ejb.Action;
import com.perpetual.viewer.model.ejb.ActionPK;
import com.perpetual.viewer.model.ejb.ActionHome;

import com.perpetual.viewer.model.vo.ActionVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;

import com.perpetual.viewer.control.exceptions.ViewerLoginException;
import com.perpetual.exception.BasePerpetualException;

import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserPK;
import com.perpetual.util.Constants;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.util.PrivillegesCache;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector; 
/**
 *
 *
 * @author  brunob
 */
public class UserProfileBean implements javax.ejb.SessionBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( UserProfileBean.class );
    
    private static UserHome            lUserHome=null;
    private static RoleHome            lRoleHome=null;
    private static ActionHome          lActionHome=null; 
    private static RoleActionHome      lRoleActionHome=null;
    private static DomainHome          lDomainHome=null;
    private static DomainHostHome      lDomainHostHome=null;
    private static DomainFacilityHome  lDomainFacilityHome=null;
    private static DomainSeverityHome  lDomainSeverityHome=null;
    private static HostHome            lHostHome=null;
    private static FacilityHome        lFacilityHome=null;
    private static SeverityHome        lSeverityHome=null;
    private static Object              homeObj=null;
         
    static
    {
        try
        {
         sLog.debug("In UserProfileBean !");   
         sLog.debug("---Initializing Home interfaces---");
         homeObj = ServiceLocator.findHome(Constants.jndiName_User);
         lUserHome = (UserHome) PortableRemoteObject.narrow(homeObj,UserHome.class); 
         sLog.debug("got UserHome");
         homeObj = ServiceLocator.findHome(Constants.jndiName_Role);
         lRoleHome = (RoleHome) PortableRemoteObject.narrow(homeObj,RoleHome.class); 
         sLog.debug("got RoleHome");
         homeObj = ServiceLocator.findHome(Constants.jndiName_RoleAction);
         lRoleActionHome = (RoleActionHome) PortableRemoteObject.narrow(homeObj,RoleActionHome.class); 
         sLog.debug("got RoleActionHome");
         homeObj = ServiceLocator.findHome(Constants.jndiName_Action);
         lActionHome = (ActionHome) PortableRemoteObject.narrow(homeObj,ActionHome.class); 
         sLog.debug("got ActionHome");        
         homeObj = ServiceLocator.findHome(Constants.jndiName_Domain);
         lDomainHome = (DomainHome) PortableRemoteObject.narrow(homeObj,DomainHome.class); 
         sLog.debug("got DomainHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_DomainHost);
         lDomainHostHome = (DomainHostHome) PortableRemoteObject.narrow(homeObj,DomainHostHome.class); 
         sLog.debug("got DomainHostHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_DomainFacility);
         lDomainFacilityHome = (DomainFacilityHome) PortableRemoteObject.narrow(homeObj,DomainFacilityHome.class); 
         sLog.debug("got DomainFacilityHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_DomainSeverity);
         lDomainSeverityHome = (DomainSeverityHome) PortableRemoteObject.narrow(homeObj,DomainSeverityHome.class); 
         sLog.debug("got DomainSeverityHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_Host);
         lHostHome = (HostHome) PortableRemoteObject.narrow(homeObj,HostHome.class); 
         sLog.debug("got HostHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_Facility);
         lFacilityHome = (FacilityHome) PortableRemoteObject.narrow(homeObj,FacilityHome.class); 
         sLog.debug("got FacilityHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_Severity);
         lSeverityHome = (SeverityHome) PortableRemoteObject.narrow(homeObj,SeverityHome.class); 
         sLog.debug("got SeverityHome");
        }
        catch(javax.naming.NamingException excp)
        {
            String errMsg ="Could not instantiate Home interfaces:";
            sLog.error(errMsg);
            //throw new RemoteException(errMsg,excp);
                       
        }
    }
    
    public void ejbCreate() 
    {
        sLog.debug("Creating UserProfileBean !");
        init();   
    }
    private void init()
    {
       
        
    }
    public UserProfileVO getUserProfile(UserVO pUserVO) throws java.rmi.RemoteException
    {
        SessionRoleVO lRoleVO = null;
        SessionDomainVO lDomainVO=null;
        UserProfileVO lProfileVO=null;
        
        try
        {            
            lRoleVO = getRole(pUserVO.getRoleId());
            lDomainVO = getDomain(pUserVO.getServiceDomain());
            lProfileVO = new UserProfileVO(pUserVO,lDomainVO ,lRoleVO);
            
                        
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get User profile for:"+pUserVO.getUserId();
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
       
        return lProfileVO;
    }
    
     public UserProfileVO getUserProfile(int pUserId) throws java.rmi.RemoteException
     {
       sLog.debug("In userprofile Bean !!!!");
       //find the User entity Bean....and get values
        UserVO lUserVO = null;
        SessionRoleVO lRoleVO = null;
        SessionDomainVO lDomainVO=null;
        UserProfileVO lProfileVO=null;
              
        try
        {            
            lUserVO = getUser(pUserId);
            lRoleVO = getRole(lUserVO.getRoleId());
            lDomainVO = getDomain(lUserVO.getServiceDomain());
            lProfileVO = new UserProfileVO(lUserVO,lDomainVO ,lRoleVO);
            
                        
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get User profile for:"+pUserId;
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
       
       return lProfileVO;
   }
     
     private UserVO getUser(int pUserId) throws java.rmi.RemoteException
     {         
         UserVO lUserVO = null;
         User lUser=null;
        try
        {            
            sLog.debug("Got home, getting User  !");
            UserPK pk = new UserPK(pUserId);
            User user = (User) lUserHome.findByPrimaryKey(pk);
                    
            if(user!=null)
            {
                sLog.debug("Got me a user !");
                lUserVO= new UserVO(lUser.getUserid(),lUser.getUserpass(),lUser.getServicedomain(),
                            lUser.getPhone(),lUser.getEmail(),lUser.getRealname(),
                            lUser.getRoleid(),lUser.getId(),lUser.getEnabled()); 
            }
            
        }
        catch(javax.ejb.FinderException excp)
        {
            String errMsg="Could not find user:"+pUserId;
            sLog.error(errMsg);
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg="Could not find user:"+pUserId;
            sLog.error(errMsg);
            throw new RemoteException(errMsg,excp);
        }
        return lUserVO;
     }
     private SessionRoleVO getRole(int pRoleId) throws java.rmi.RemoteException
     {
         sLog.debug("getting Role Info");
         RoleVO lRoleVO=null;
         SessionRoleVO lSessionRoleVO=null;
         Object homeObj=null;   
         Collection lActionList;
         Vector builtActionList=new Vector();
         
         try
         {            
             sLog.debug("Getting Role info !");
             RolePK rolePk = new RolePK(pRoleId);
             Role lRole = (Role) lRoleHome.findByPrimaryKey(rolePk);      
             sLog.debug("Got Role - createing RoleVO");
             lRoleVO = new RoleVO(lRole.getId(),lRole.getRoleName());    
         }
         catch(javax.ejb.FinderException excp)
        {
           String errMsg ="Could not find Role:"+Constants.jndiName_Role;
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
             
         try
         {
            sLog.debug("Attempting to get Role-Action mapping from the Privilleges Cache !");
            lActionList  = PrivillegesCache.getInstance().getActionList(pRoleId);
            if (lActionList !=null)
            {
                sLog.debug("Privilleges Cache has the data required!");
                lSessionRoleVO = new SessionRoleVO(lRoleVO,lActionList);
            }
            else
            {
                sLog.debug("Privilleges Cache doesn't the data required-going after the RoleActionEntity Bean!");
                Collection lActionListRemote = (Collection) lRoleActionHome.findByRoleId(pRoleId);
             
                Iterator it = lActionListRemote.iterator();
                sLog.debug("constructing ActionVO list !");
                while(it.hasNext())
                {
                    RoleAction roleAction = (RoleAction) it.next();
                    int actionId = (roleAction.getActionId()).intValue();                    
                    Action lAction = (Action) lActionHome.findByPrimaryKey(new ActionPK(actionId));
                    ActionVO actionVo = new ActionVO(lAction.getId(),lAction.getName());
                    builtActionList.add(actionVo);
                }
                lActionList = (Collection) builtActionList;
                lSessionRoleVO = new SessionRoleVO(lRoleVO,lActionList);
            
            }       
            
            
        }
         catch(BasePerpetualException excp)
        {
           String errMsg ="Validation exception !";
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
         catch(javax.ejb.FinderException excp)
        {
            String errMsg ="Viewer Login error-Couldn't get User";
            sLog.error(errMsg);
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get Role profile !";
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
         
        
         return lSessionRoleVO;
     }
     private SessionDomainVO getDomain(String pDomainName) throws java.rmi.RemoteException
     {
         sLog.debug("getting Domain Info");
         DomainVO lDomainVO = null;
         SessionDomainVO lSessionDomainVO=null;
         Object homeObj=null;
         try
         {            
             Domain lDomain = (Domain) lDomainHome.findByDomainName(pDomainName);      
             sLog.debug("Getting Domain - creating DomainVO");
             lDomainVO = new DomainVO(lDomain.getId(),lDomain.getName());    
             //-------------------------------------
             Collection hostList = getDomainHosts(lDomain.getId());
             Collection severityList = getDomainSeverities(lDomain.getId());
             Collection facilityList = getDomainFacilities(lDomain.getId());
            
             
             lSessionDomainVO = new SessionDomainVO(lDomainVO,hostList,facilityList,severityList);
             
         }
         catch(javax.ejb.FinderException excp)
        {
           String errMsg ="Could not find Domain:"+Constants.jndiName_Domain;
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get Domain value!";
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
         
         return lSessionDomainVO;
     }    
     private Collection getDomainHosts(int pDomainId) throws java.rmi.RemoteException
     {
         Collection lHostList=new Vector();
         Collection lHostListRemote=null;
         sLog.debug("getting Domain Info");
         DomainVO lDomainVO = null;
         HostVO   lHostVO   =null;
        
         try
         {            
             lHostListRemote = (Collection) lDomainHostHome.findByDomainId(pDomainId);      
             Iterator it = lHostListRemote.iterator();
             sLog.debug("constructing HostVO list !");
                while(it.hasNext())
                {
                    DomainHost domainHost = (DomainHost) it.next();
                    int hostId = (domainHost.getHostId()).intValue();                    
                    Host lHost = (Host) lHostHome.findByPrimaryKey(new HostPK(hostId));
                    HostVO hostVo = new HostVO(lHost.getId(),lHost.getName(), lHost.getDescription());
                    lHostList.add(hostVo);
                }
                
                                
         }
         catch(javax.ejb.FinderException excp)
        {
           String errMsg ="Could not find Domain:"+Constants.jndiName_Domain;
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get Domain value!";
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
         
         return lHostList;
     }
     private Collection getDomainFacilities(int pDomainId) throws java.rmi.RemoteException
     {
          Collection lFacilityList=new Vector();
          
         Collection lFacilityListRemote=null;
         FacilityVO lFacilityVO = null;
               
         try
         {            
             lFacilityListRemote = (Collection) lDomainFacilityHome.findByDomainId(pDomainId);      
             Iterator it = lFacilityListRemote.iterator();
             sLog.debug("constructing FacilityVO list !");
                while(it.hasNext())
                {
                    DomainFacility domainFacilty = (DomainFacility) it.next();
                    int facilityId = (domainFacilty.getFacilityId()).intValue();                    
                    Facility lFacility = (Facility) lFacilityHome.findByPrimaryKey(new FacilityPK(facilityId));
                    FacilityVO facilityVo = new FacilityVO(lFacility.getId(),lFacility.getName());
                    lFacilityList.add(facilityVo);
                }
                
                                
         }
         catch(javax.ejb.FinderException excp)
        {
           String errMsg ="Could not find Facility:"+Constants.jndiName_Facility;
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get Facilty value!";
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
         return lFacilityList;
     }
     private Collection getDomainSeverities(int pDomainId) throws java.rmi.RemoteException
     {
          Collection lSeverityList=new Vector();
         
         Collection lSeverityListRemote=null;
         SeverityVO lSeverityVO = null;
               
         try
         {            
             lSeverityListRemote = (Collection) lDomainSeverityHome.findByDomainId(pDomainId);      
             Iterator it = lSeverityListRemote.iterator();
             sLog.debug("constructing SeverityVO list !");
                while(it.hasNext())
                {
                    DomainSeverity domainSeverity = (DomainSeverity) it.next();
                    int severityId = (domainSeverity.getSeverityId()).intValue();                    
                    Severity lSeverity = (Severity) lSeverityHome.findByPrimaryKey(new SeverityPK(severityId));
                    SeverityVO severityVo = new SeverityVO(lSeverity.getId(),lSeverity.getName());
                    lSeverityList.add(severityVo);
                }
                
                                
         }
         catch(javax.ejb.FinderException excp)
        {
           String errMsg ="Could not find Severity:"+Constants.jndiName_Severity;
            sLog.error(errMsg);   
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Could not get Severity value!";
            sLog.error(errMsg);            
            throw new RemoteException(errMsg,excp);
        }
         return lSeverityList;
     }
          
      
    public void setSessionContext(SessionContext sessionContext)  throws EJBException,RemoteException
    {
    }
    public void ejbRemove() throws RemoteException
    {
    }
    public void ejbActivate() throws RemoteException
    {
    }
    public void ejbPassivate()  throws RemoteException
    {
    }
   
}
