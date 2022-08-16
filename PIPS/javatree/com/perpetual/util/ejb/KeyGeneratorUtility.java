package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

public class KeyGeneratorUtility
{
    public static synchronized int generateKey() throws EJBException		// static synchronized is identical to synchronized(KeyGeneratorUtility.class)
	{
		if (m_keyGen == null)
		{
			InitialContext context = null;
			try
			{
				context = new InitialContext();
				Object reference = context.lookup("perpetual/KeyGenerator");
				KeyGeneratorHome keyGenHome =
					(KeyGeneratorHome)PortableRemoteObject.narrow(reference,
						KeyGeneratorHome.class);
				m_keyGen = keyGenHome.create();
			}
			catch (NamingException ne) {
				throw new EJBException(ne);
			}
			catch (CreateException ce) {
				throw new EJBException(ce);
			}
			catch (RemoteException re) {
				throw new EJBException(re);
			}
			finally
			{
				try {
					context.close();
				} catch (NamingException e) {
				}
			}
		}

		if (--m_nKeys <= 0)
		{
			try
			{
				m_key = m_keyGen.reserveKeys("anonymous", 10);
				m_nKeys = 10;
			}
			catch (Exception ex)
			{              	
				throw new EJBException("KeyGenerator failed: " + ex);
			}
		}
		return m_key++;
    }

	private static int m_nKeys;
	private static int m_key;
	private static KeyGenerator m_keyGen;
	private static InitialContext m_context;
}
