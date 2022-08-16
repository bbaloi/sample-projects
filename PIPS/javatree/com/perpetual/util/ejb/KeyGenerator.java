package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;


/**
 * @ejb:bean name="KeyGen" type="Stateless" view-type="remote"
 */

public interface KeyGenerator extends EJBObject
{
	/**
	@ejb.interface-method
	@ejb.transaction type="RequiresNew"
	*/
	public int reserveKeys(String name, int block) throws RemoteException;
}


