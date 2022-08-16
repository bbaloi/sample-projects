/*
 * LoginHome.java
 *
 * Created on June 28, 2003, 4:04 PM
 */

package com.perpetual.viewer.control.ejb.crud.summary;

import javax.ejb.EJBHome;

/**
 *
 * @author  brunob
 */
public interface SummaryCRUDHome extends EJBHome
{
    SummaryCRUD create()  throws javax.ejb.CreateException,java.rmi.RemoteException;    
}
