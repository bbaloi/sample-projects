package com.perpetual.util;

/**
 *	Utility class for loading resources.
 *	@author			Bruno Baloi
 *      Company:      Perpetual IP GmbH
  */


import java.util.*;
import java.net.*;
import java.io.*;
import javax.naming.*;

import com.perpetual.exception.BasePerpetualException;

public final class PerpetualResourceLoader
{
	private static ClassLoader sClassLoader = null;

	private static final int ERROR_WIDTH = 64, ERROR_HEIGHT = 24;

	private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualResourceLoader.class );
	private static Object sLookupLock = new Object();	// class to lock lookups on InitialContexts

	private static HashMap sHomeInterfaceHashtable = new HashMap(), sInitialContextHashMap = new HashMap();

	private static PerpetualResourceLoader	m_default = new PerpetualResourceLoader();


	static
	{
		sClassLoader = PerpetualResourceLoader.class.getClassLoader();
	}
	
	// static methods
	//
	public static PerpetualResourceLoader getDefault()
	{
		return m_default;
	}

	public static InputStream getResourceAsStream( String pResourceFilename )
		throws BasePerpetualException
	{
		return( getResourceAsStream( pResourceFilename, true ) );
	}

	public static InputStream getResourceAsStream( String pResourceFilename, boolean pVerbose )
		throws BasePerpetualException
	{
		InputStream lInputStream;
		String		lFilename;

		try
		{
			if ( pVerbose )
				sLog.info("ResourceLoader.getResourceAsStream " + pResourceFilename);

			lFilename = pResourceFilename;
			lInputStream = Object.class.getResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "Object.class.getResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}

			lFilename = "/" + pResourceFilename;
			lInputStream = Object.class.getResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "Object.class.getResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}

			lFilename = pResourceFilename;
			lInputStream = sClassLoader.getResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "ResourceLoader.class.getClassLoader().getResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}

			lFilename = "/" + pResourceFilename;
			lInputStream = sClassLoader.getResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "ResourceLoader.class.getClassLoader().getResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}

			lFilename = pResourceFilename;
			lInputStream = sClassLoader.getSystemResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "ResourceLoader.class.getClassLoader().getSystemResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}

			lFilename = "/" + pResourceFilename;
			lInputStream = sClassLoader.getSystemResourceAsStream( lFilename );
			if ( lInputStream != null )
			{
				if ( pVerbose ) sLog.info( "ResourceLoader.class.getClassLoader().getSystemResourceAsStream( " + lFilename + " ):  " + lInputStream );
				return( lInputStream );
			}
                        
                        
                        lFilename = pResourceFilename;
                        sLog.debug("found file...:"+lFilename);
			lInputStream = new FileInputStream(lFilename);
			return lInputStream;

//			throw new PropertiesHandlerException( "Could not get resource:  " + pResourceFilename );
		}
		catch (IOException e)
		{
                        sLog.error("Couldn't get resource file");
			BasePerpetualException.handle("Could not get resource:  " + pResourceFilename);
			return null;	// can't happen
		}

//		return null;	 MP- can never get here, good
	}

	public static URL getResource( String pResourceFilename )
		throws BasePerpetualException
	{
		return( getResource( pResourceFilename, true ) );
	}

	public static URL getResource( String pResourceFilename, boolean pVerbose )
		throws BasePerpetualException
	{
		URL		lURL		= null;
		String	lFilename	= null;

		try
		{
			try
			{
				if ( pVerbose ) {
					sLog.info( PerpetualResourceLoader.class.getName() + ":  public static URL getResource( " + pResourceFilename + " )" );
				}

				lFilename = pResourceFilename;
				lURL = Object.class.getResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "Object.class.getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				lFilename = "/" + pResourceFilename;
				lURL = Object.class.getResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "Object.class.getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				lFilename = pResourceFilename;
				lURL = sClassLoader.getResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "ResourceLoader.class.getClassLoader().getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				lFilename = "/" + pResourceFilename;
				lURL = sClassLoader.getResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "ResourceLoader.class.getClassLoader().getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				lFilename = pResourceFilename;
				lURL = sClassLoader.getSystemResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "ResourceLoader.class.getClassLoader().getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				lFilename = "/" + pResourceFilename;
				lURL = sClassLoader.getSystemResource( lFilename );
				if ( pVerbose ) {
					sLog.info( "ResourceLoader.class.getClassLoader().getResource( " + lFilename + " ):  " + lURL );
				}
				if ( lURL != null ) {
					return( lURL );
				}

				throw new PropertyHandlerException( "Could not get resource:  " + pResourceFilename );
			}
			catch( Exception e )
			{
				sLog.error( "Could not get resource:  " + pResourceFilename );
				BasePerpetualException.handle( "Could not get resource:  " + pResourceFilename);
			}
		}
		catch( Exception e )
		{
			BasePerpetualException.handle( PerpetualResourceLoader.class.getName() + ":  public static URL getResource( " + pResourceFilename + ", " + pVerbose + " )");
		}

		return( null );
	}

	/*
	 These methods cache InitialContexts and Home interfaces.
	 They take care of invalid InitialContexts, the clients must ensure that the
	 returned Home interfaces are valid, and have a way of requesting a new one if
	 they determine it is not.
	 Currently there is only one InitialContext per url.  This could be easily
	 modified to use an InitialContextPool if needed.
	 */
	private static InitialContext getInitialContext( Hashtable pHashtable )
		throws BasePerpetualException
	{
		return( getInitialContext( pHashtable, false ) );
	}

	private static InitialContext getInitialContext( Hashtable pHashtable, boolean pReload )
		throws BasePerpetualException
	{
		InitialContext	lInitialContext		= null;
		InitialContext	lOldInitialContext	= null;

		synchronized( sInitialContextHashMap )
		{
			// Use the url for a key for now.  Could use a composite key later.
			// Have to use pHashtable as key now, since PropertiesHandler cannot
			// find jndi.properties with ResourceBundle
			lInitialContext = (InitialContext)sInitialContextHashMap.get( pHashtable );

                            sLog.debug( "Looking up InitialContext:  " + pHashtable );

				if ( pReload ) {
					sLog.debug( "Reloading Initial Context" );
				}
				else {
					if ( lInitialContext != null ) {
						sLog.debug( "Have cached InitialContext:  " + lInitialContext );
					}
				}
			
			if ( pReload || ( lInitialContext == null ) ) {
				try
				{
					lInitialContext = new InitialContext( pHashtable );
					lOldInitialContext = (InitialContext)sInitialContextHashMap.put( pHashtable, lInitialContext );
					if ( lOldInitialContext != null ) {
						try
						{
							lOldInitialContext.close();
						}
						catch( Exception e )
						{
							// do nothing
						}
					}
				}
				catch( Throwable e )
				{
					sLog.error( "Unable to load InitialContext" );
					BasePerpetualException.handle( "Unable to load InitialContext");
				}
			}
		}

		return( lInitialContext );
	}

	public static Object lookup( Hashtable pHashtable, String pLookupName )
		throws BasePerpetualException
	{
		return( lookup( pHashtable, pLookupName, false ) );
	}

	public static Object lookup( Hashtable pHashtable, String pLookupName, boolean pReload )
		throws BasePerpetualException
	{
		InitialContext	lInitialContext	= null;
		Object			lHomeObject		= null;

		synchronized( sHomeInterfaceHashtable )
		{
			lHomeObject = sHomeInterfaceHashtable.get( pHashtable + pLookupName );

			
				sLog.debug( "Looking up:  " + pLookupName );

				if ( pReload ) {
					sLog.debug( "Reloading JNDI Object" );
				}
				else {
					if ( lHomeObject != null ) {
						sLog.debug( "Have cached JNDI Object:  " + lHomeObject );
					}
				}
			

			if ( pReload || ( lHomeObject == null ) ) {
				try
				{
					lInitialContext = getInitialContext( pHashtable, pReload );
					synchronized( sLookupLock )
					{
						lHomeObject = lInitialContext.lookup( pLookupName );
					}
					sHomeInterfaceHashtable.put( pHashtable + pLookupName, lHomeObject );
				}
				catch( Throwable e )
				{
					sLog.error( "Unable to lookup JNDI Object:  '" + pLookupName + "'.  Will retry" );

					try
					{
						lInitialContext = getInitialContext( pHashtable, true );
						synchronized( sLookupLock )
						{
							lHomeObject = lInitialContext.lookup( pLookupName );
						}
						sHomeInterfaceHashtable.put( pHashtable + pLookupName, lHomeObject );
					}
					catch( Throwable e2 )
					{
						sLog.error( "Unable to lookup JNDI Object:  '" + pLookupName + "'" );
						BasePerpetualException.handle( "Unable to lookup JNDI Object:  '" + pLookupName + "'");
					}
				}
			}
		}

		return( lHomeObject );
	}


	// members
	//
	private Map
			m_resourceBundleMap = new HashMap(),
			m_propertiesMap = new HashMap(),
			m_propertiesModifiedMap = new HashMap();
	private ResourceBundle m_strings;
	private Locale m_locale;
	private Map m_objectMap = new HashMap();

	// constructors
	//
	private PerpetualResourceLoader()	// prevent public instanstiation
	{
	}

	// methods
	//
	public String getString(String key)
	{
		// dynamic locale switching for prefs
		//
		if (!Locale.getDefault().equals(m_locale))
		{
			m_resourceBundleMap.clear();
			m_locale = Locale.getDefault();
			m_strings = null;
		}

		if (m_strings == null)
		{
			try
			{
				m_strings = getResourceBundle("strings");
			}
			catch (Throwable ex)
			{
				sLog.error ("", ex);
			}
		}

		try
		{
			return m_strings.getString(key);
		}
		catch (Throwable ex)
		{
			sLog.error("string resource not found: " + key);
		}
		return key;
	}

       	public Properties getProperties(String src) throws BasePerpetualException
	{
		File file = new File(src);
		if (file.isFile())
		{
			Long mod = (Long)m_propertiesModifiedMap.get(src);
			if (mod != null && file.lastModified() > mod.longValue())
			{
				flushPropertiesCache(src);
				mod = null;
			}
			if (mod == null)
				m_propertiesModifiedMap.put(src, new Long(file.lastModified()));
		}

		Properties props = (Properties)m_propertiesMap.get(src);
		if (props == null)
		{
                    System.out.println("loading " + src);
			try
			{
				InputStream is = getResourceAsStream(src);
				try
				{
					props = new Properties();
					props.load(is);
					m_propertiesMap.put(src, props);
				}
				finally
				{
					is.close();
				}
			}
			catch (Throwable ex)
			{
				BasePerpetualException.handle("Couldn't load: " + src);
			}
		}
		return props;
	}

	public void saveProperties(String src) throws BasePerpetualException
	{
		Properties props = (Properties)m_propertiesMap.get(src);
		if (props == null) return;
		try
		{
			OutputStream os = new FileOutputStream(src);
			try
			{
				props.store(os, "Written by ResourceLoader");
			}
			finally
			{
				os.close();
			}
		}
		catch (Throwable ex)
		{
			BasePerpetualException.handle("Couln't save: " + src);
		}
	}

	public void flushPropertiesCache(String src)
	{
System.out.println("flushing properties cache: " + src);
		m_propertiesMap.clear();
		m_propertiesModifiedMap.clear();
	}

	public String getProperty(String src, String key) throws BasePerpetualException
	{
		Properties props = getProperties(src);
//System.out.println("getProperty " + src + " " + key + " " + props.getProperty(key));
		return props.getProperty(key);
	}

	public void setProperty(String src, String key, String value) throws BasePerpetualException
	{
//System.out.println("setProperty " + src + " " + key + " " + value);
		Properties props = getProperties(src);
		props.put(key, value);
	}

	public ResourceBundle getResourceBundle(String src) throws BasePerpetualException
	{
		try
		{
			ResourceBundle res = (ResourceBundle)m_resourceBundleMap.get(src);
			if (res == null)
			{
				InputStream is = getResourceAsStream(src + ".properties");
				try
				{
					res = new PropertyResourceBundle(is);
					m_resourceBundleMap.put(src, res);
				}
				finally
				{
					is.close();
				}
			}
			return res;
		}
		catch (Throwable ex)
		{
			BasePerpetualException.handle("Couldn't get resource bundle: " + src);
			return null;	// can't happen
		}
	}

	public void setObject(Object key, Object value)
	{
		m_objectMap.put(key, value);
	}

	public Object getObject(Object key)
	{
		return m_objectMap.get(key);
	}
}


