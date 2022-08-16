/*
 * LoginHome.java
 *
 * Created on June 28, 2003, 4:04 PM
 */

package com.perpetual.viewer.control.ejb;

import javax.ejb.EJBHome;
import com.perpetual.viewer.control.ejb.Login;
/**
 *
 * @author  brunob
 */
public interface LoginHome extends EJBHome
{
    Login create()  throws javax.ejb.CreateException,java.rmi.RemoteException;
      
}
