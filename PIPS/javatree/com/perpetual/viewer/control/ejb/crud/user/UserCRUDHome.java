/*
 * LoginHome.java
 *
 * Created on June 28, 2003, 4:04 PM
 */

package com.perpetual.viewer.control.ejb.crud.user;

import javax.ejb.EJBHome;

/**
 *
 * @author  brunob
 */
public interface UserCRUDHome extends EJBHome
{
    UserCRUD create()  throws javax.ejb.CreateException,java.rmi.RemoteException;    
}
