/*
 * LoginHome.java
 *
 * Created on June 28, 2003, 4:04 PM
 */

package com.perpetual.viewer.control.ejb;

import javax.ejb.EJBHome;

/**
 *
 * @author  brunob
 */
public interface UserProfileHome extends EJBHome
{
    UserProfile create()  throws javax.ejb.CreateException,java.rmi.RemoteException;
      
}
