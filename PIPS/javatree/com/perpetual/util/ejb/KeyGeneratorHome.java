package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


/**
 * @ejb:bean name="KeyGen" type="Stateless" view-type="remote"
 */

public interface KeyGeneratorHome extends EJBHome
{
	/**
	  @ejb.create-method
	 */
	KeyGenerator create() throws CreateException, RemoteException;
}


