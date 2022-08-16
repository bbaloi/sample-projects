/*
 * LoginDelegateImpl.java
 *
 * Created on June 28, 2003, 4:00 PM
 */

package com.perpetual.viewer.control;

import org.apache.log4j.Priority;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;

import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.control.ejb.Login;
import com.perpetual.viewer.control.ejb.LoginHome;
import com.perpetual.viewer.control.exceptions.ViewerLoginException;
import com.perpetual.util.Constants;

import javax.naming.Context;
import javax.rmi.PortableRemoteObject;


/**
 *
 * @author  brunob
 */
public class LoginDelegateImpl implements LoginDelegate
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LoginDelegateImpl.class );    
    private UserProfileVO lUserProfile=null;
    /** Creates a new instance of LoginDelegateImpl */
    public LoginDelegateImpl() 
    {
    }
    
    public UserProfileVO login(String pUserId, String pPassword) throws ViewerLoginException 
    {
       //invoke the login Bean
         //lSessionHome = (PJVTProducerSessionHome) lIC.lookup(lJndiName);
        sLog.debug("In LoginDelegateImpl.login()");
        try
        {
            
           Object homeObj = ServiceLocator.getServiceLocatorInstance().getContext().lookup(Constants.jndiName_Login);
          //if(sLog.isEnabledFor(Priority.DEBUG))          
            sLog.debug("looking for LoginHome");
            LoginHome lHome = (LoginHome) PortableRemoteObject.narrow(homeObj,LoginHome.class); 
           //if(sLog.isEnabledFor(Priority.DEBUG))          
                sLog.debug("Got home creating ProducerSession !");
            Login login = (Login) lHome.create();
            //if(sLog.isEnabledFor(Priority.DEBUG))          
                  sLog.debug("Login is  created !");                       
            lUserProfile = login.login(pUserId,pPassword); 
        }
        catch(javax.naming.NamingException excp)
        {
            sLog.error("Could not login:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't find:"+Constants.jndiName_Login;
            throw new ViewerLoginException(errMsg,excp);
                       
        }
        catch(javax.ejb.CreateException excp)
        {
            sLog.error("Could not login:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't create Login Bean";
            throw new ViewerLoginException(errMsg,excp);
        }
        catch(java.rmi.RemoteException excp)
        {
            sLog.error("Could not login:"+pUserId);
            String errMsg ="Viewer Login error-Couldn't create Login Bean";
            throw new ViewerLoginException(errMsg,excp);
        }
        
        return lUserProfile;
    }
    
}
