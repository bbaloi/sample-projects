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
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.SessionDomainVO;
import com.perpetual.viewer.model.vo.SessionRoleVO;
import com.perpetual.viewer.control.exceptions.ViewerLoginException;

import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserPK;

import com.perpetual.util.Constants;
import com.perpetual.util.ServiceLocator;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.Collection;
import java.util.Iterator;


/**
 *
 *
 * @author  brunob
 */
public class LoginBean implements javax.ejb.SessionBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LoginBean.class );
      
    public void ejbCreate() 
    {
        sLog.debug("Creating LoginBean !");
        
    }
      
   public UserProfileVO login(String pUserId,String pPassword) throws java.rmi.RemoteException
   {
       sLog.debug("In Login Bean !!!!");
       //find the User entity Bean....and get values
       UserProfileVO profileVO=null;
       User lUser=null;
       Object homeObj=null;
        try
        {            
           homeObj = ServiceLocator.getServiceLocatorInstance().getContext().lookup(Constants.jndiName_User);
            sLog.debug("looking for UserHome");
            UserHome lHome = (UserHome) PortableRemoteObject.narrow(homeObj,UserHome.class); 
            sLog.debug("Got home, getting User  !");
            //UserPK pk = new UserPK(pUserId);
            //User user = (User) lHome.findByPrimaryKey(pk);
             Collection userList = (Collection) lHome.findByUserId(pUserId);
             sLog.debug("got user list-size:"+userList.size());
             Iterator userIterator = userList.iterator();
             int i=0;
             while(userIterator.hasNext())
             {
                 i++;
                 sLog.debug("user"+i);
                 lUser = (User) userIterator.next();
                 sLog.debug("User Name:"+lUser.getUserid());
                 if(pUserId.equals(lUser.getUserid()))
                 {
                     sLog.debug("Got User !");     
                     break; 
                 }
             }
              
             if(lUser==null)
             {
                 String errMsg="Invalid user Name !";
                 sLog.error(errMsg);
                 throw new RemoteException(errMsg);
             }
            
            if (pPassword.equals(lUser.getUserpass()))
            {
                sLog.debug("The user password is valid !! - testing to see if user is enabled !");
                if(!lUser.getEnabled())
                {
                    String errMsg="User"+lUser.getUserid()+" has been disabled:-cannot login !";
                    sLog.error(errMsg);
                    throw new RemoteException(errMsg);
                }
                else
                {
                    sLog.debug("User enabled, getting user profile !");
                    UserVO vo= new UserVO(lUser.getUserid(),lUser.getUserpass(),lUser.getServicedomain(),
                            lUser.getPhone(),lUser.getEmail(),lUser.getRealname(),
                            lUser.getRoleid(),lUser.getId(),lUser.getEnabled()); 
               
                         //go get & construct the rest of the Profile i.e. Domain stuff and Role stuff
                     sLog.debug("getting UserProfileBean....");
                    homeObj = ServiceLocator.getServiceLocatorInstance().getContext().lookup(Constants.jndiName_UserProfile);
                    UserProfileHome lProfileHome = (UserProfileHome) PortableRemoteObject.narrow(homeObj,UserProfileHome.class); 
                    sLog.debug("Got UserProfileHome,getting UserProfile interface  !");
                    UserProfile userProfile = (UserProfile)lProfileHome.create();
                    sLog.debug("got UserProfile - getting values !");
                    profileVO = userProfile.getUserProfile(vo);  
                }
                            
            }
            else
            {
                String errMsg= "Password Invalid - could not log in !";
                sLog.error(errMsg);
                throw new java.rmi.RemoteException(errMsg);
            }
           
        }
        catch(javax.ejb.CreateException excp)
        {
            sLog.error("Could not create UserProfile Bean for:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't find:"+Constants.jndiName_UserProfile;
            throw new RemoteException(errMsg,excp);
                       
        }
        catch(javax.naming.NamingException excp)
        {
            sLog.error("Could not login:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't find:"+Constants.jndiName_User;
            throw new RemoteException(errMsg,excp);
                       
        }
        catch(javax.ejb.FinderException excp)
        {
            sLog.error("Could not find User:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't get User";
            throw new RemoteException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            sLog.error("Could not login:"+pUserId);
            String errMsg ="Viewer Login error";
            throw new RemoteException(errMsg,excp);
        }
       
       return profileVO;
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
