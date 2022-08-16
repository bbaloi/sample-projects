package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;


/**
 * @ejb:bean name="Key" type="CMP" view-type="local" primkey-field="name" local-jndi-name="local/Key"
 * @ejb:pk generate="false" class="java.lang.String"
 */

public abstract class KeyBean implements EntityBean
{
	public KeyBean() {}
	/**
	  @ejb.create-method
	 */
	public KeyPK ejbCreate(String name, int number) throws CreateException
	{
		setName(name);
		setNumber(number);
		KeyPK keyPK = null;
         
		keyPK = new KeyPK(name);
    	
		return keyPK;
	}

	public void ejbPostCreate(String name, int number) throws CreateException
	{
	}

	/**
	  @ejb.pk-field
	  @ejb.persistent-field
	  @ejb.interface-method
	 */
	public abstract String getName();
	public abstract void setName(String id);

	/**
	  @ejb.persistent-field
	  @ejb.interface-method
	 */
	public abstract int getNumber();
	/**
	  @ejb.interface-method
	 */
	public abstract void setNumber(int number);
	
	public void ejbRemove() throws RemoteException
	{
	}
	public void ejbActivate() throws RemoteException
	{
	}
	public void ejbPassivate()  throws RemoteException
	{
	}
	public void setEntityContext(EntityContext entityContext)  throws EJBException,RemoteException
	{
	}
	public void unsetEntityContext() throws EJBException,RemoteException
	{
	}
	public void ejbLoad() throws EJBException,RemoteException
	{
	}
	public void ejbStore() throws EJBException,RemoteException
	{
	}
}



