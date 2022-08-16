package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;


/**
 * @ejb:bean name="KeyGen" type="Stateless" view-type="remote"
 */

public class KeyGeneratorBean implements SessionBean
{
	private KeyHome m_keyHome;

	/**
	  @ejb.create-method
	 */
	public void ejbCreate() throws CreateException
	{
		
		InitialContext context = null;
		 
		try
		{
			// cache the home interface
			context = new InitialContext();
			Object reference = context.lookup("perpetual/Key");
			this.m_keyHome = (KeyHome) PortableRemoteObject.narrow(reference,
					KeyHome.class);
		}
		catch (NamingException ne) {
			throw new CreateException(ne.getMessage());
		}
		finally
		{
			try {
				context.close();
			} catch (NamingException e) {
			}
		}
	}

	/**
	@ejb.interface-method
	@ejb.transaction type="RequiresNew"
	*/
	public int reserveKeys(String name, int block)
	{
		try
		{
			Key key;
			try
			{				
				key = this.m_keyHome.findByPrimaryKey(new KeyPK(name));
			}
			catch (Throwable ex)
			{
				key = this.m_keyHome.create(name, 1);
			}

			int i = key.getNumber();
			key.setNumber(i + block);
			
			return i;
		}
		catch (Throwable ex)
		{
			throw new EJBException(ex.getMessage());
		}
	}
	
	 public void setSessionContext(SessionContext sessionContext)  throws EJBException,RemoteException
	 {
	 }
	 
	 public void ejbRemove() throws RemoteException
	 {
	 }
	 
	 public void ejbActivate() throws RemoteException
	 {
	 }
	 
	 public void ejbPassivate()  throws RemoteException
	 {
	 }
}


