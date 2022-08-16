/*
 * UserHome.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

public interface KeyHome extends EJBHome
{
    public Key create(String name, int number) throws CreateException, java.rmi.RemoteException;
    public Key findByPrimaryKey( KeyPK primaryKey ) throws FinderException, RemoteException;
}
