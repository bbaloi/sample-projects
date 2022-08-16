/*
 * LoginHome.java
 *
 * Created on June 28, 2003, 4:04 PM
 */

package com.perpetual.viewer.control.ejb.crud.host;

import javax.ejb.EJBHome;

/**
 *
 * @author  brunob
 */
public interface HostCRUDHome extends EJBHome
{
    HostCRUD create()  throws javax.ejb.CreateException,java.rmi.RemoteException;    
}
