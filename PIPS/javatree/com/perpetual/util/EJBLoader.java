package com.perpetual.util;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.rmi.*;
import javax.rmi.*;
import javax.naming.*;
import javax.ejb.*;

import org.apache.log4j.Logger;


public class EJBLoader
{
	/**
	Method to load config from jndi.properties.  In a JBoss environment you do not need to do this, as JBoss already provides the environment preconfigured for you. 
	If you want to create a standalone client, you'll need to call this method before you do any j2ee stuff.
	As an added bonus, if jndi.properties is not found, it fills in the details from the usual defaults (localhost:1099 etc).
	*/
	public static void configureFromPropertiesFile()
	{
		sLog.debug("Configuring from jndi.properties file");
		try
		{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("jndi.properties");
			if (is != null)
			{
				try
				{
					Properties props = new Properties();
					props.load(is);
					m_context = new InitialContext(props);
				}
				finally
				{
					is.close();
				}
			}
			else
			{
				configureFromDefaults();
			}
		}
		catch (Throwable ex)
		{
			sLog.error("Trouble configuring InitialContext using jndi.properties", ex);
		}
	}

	public static void configureFromDefaults()
	{
		sLog.debug("Filling in default jndi values");
		Hashtable props = new Hashtable();		// Hashtable is lameville
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		props.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		props.put(Context.URL_PKG_PREFIXES, "org.jbos.naming:org.jnp.interfaces");
		try
		{
			m_context = new InitialContext(props);
		}
		catch (Throwable ex)
		{
			sLog.error("Trouble configuring InitialContext using default values", ex);
		}
	}

	public static EJBObject createEJBObject(String jndiName, Class homeClass)
	{
		EJBHome home = getEJBHome(jndiName, homeClass);
		try
		{
			Method create = home.getClass().getMethod("create", null);
			return (EJBObject)create.invoke(home, null);
		}
		catch (Exception ex)
		{
			throw new EJBException("Could not create " + jndiName, ex);
		}
		catch (Throwable ex)
		{
			sLog.error("Caught non-Exception", ex);
			throw new EJBException("Could not create: " + jndiName);
		}
	}

	public static EJBHome getEJBHome(String jndiName, Class homeClass) //throws RemoteException
	{
		Object home = m_homeMap.get(jndiName);
		if (home == null)
		{
			try
			{
				Object ref = getInitialContext().lookup(jndiName);
				m_homeMap.put(jndiName, home = PortableRemoteObject.narrow(ref, homeClass));
			}
			catch (Exception ex)
			{
				throw new EJBException("Could not lookup home " + jndiName, ex);
			}
			catch (Throwable ex)
			{
				sLog.error("Caught non-Exception", ex);
				throw new EJBException("Could not lookup home: " + jndiName);
			}
		}
		return (EJBHome)home;
	}

	public static EJBLocalHome getEJBLocalHome(String jndiLocalName)
	{
		try
		{
			return (EJBLocalHome)getInitialContext().lookup(jndiLocalName);
		}
		catch (Exception ex)
		{
			throw new EJBException("Could not lookup local home: " + jndiLocalName, ex);
		}
		catch (Throwable ex)
		{
			sLog.error("Caught non-Exception", ex);
			throw new EJBException("Could not lookup local home: " + jndiLocalName);
		}
	}

	public static InitialContext getInitialContext()		// throws EJBException	 - is RuntimeException, don't have to declare
	{
		try
		{
			if (m_context == null)
				m_context = new InitialContext();
			return m_context;
		}
		catch (Exception ex)
		{
			throw new EJBException("Couldn not get initial context", ex);
		}
		catch (Throwable ex)
		{
			sLog.error("Caught non-Exception", ex);
			throw new EJBException("Could not get initial context");
		}
	}

	private static InitialContext m_context;
	private static Map m_homeMap = new HashMap();
	private static Logger sLog = Logger.getLogger(EJBLoader.class);
}


