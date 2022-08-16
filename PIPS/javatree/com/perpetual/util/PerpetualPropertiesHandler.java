/**
 * Title:        PropertieHandler<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Bruno Baloi<p>
 * Company:      Perpetual IP GmbH
 * @author Bruno Baloi
  */

package com.perpetual.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.*;
import com.perpetual.exception.BasePerpetualException;
/**
 */
public final class PerpetualPropertiesHandler
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualPropertiesHandler.class );
	
	/**
	 * @roseuid 3AAD21D00006
	 */
	private PerpetualPropertiesHandler() {}

	/**
	 * @roseuid 3AAD21D00007
	 */
	public static Properties getPropertyFile( String pPropertiesFilename )
		throws Exception
	{
		Properties  lProperties;
		InputStream lInputStream;
		String		lPropertiesLocation;
		String		lFilename;

//		try
//		{
			try
			{

				// MP- try using the ResourceBundle first to benefit from the locale-overlap mechanism.
				// This is an attempt to get reduce the amount of specialized 'secret' properties files.
				//
				int i = pPropertiesFilename.lastIndexOf('.');
				if (i > 0)
				{
					try
					{
//System.out.println("?ResourceBundle " + pPropertiesFilename.substring(0, i));
						String name = pPropertiesFilename.substring(0, i);
						ResourceBundle res = ResourceBundle.getBundle(name);
//						sLog.info("Using ResourceBundle for " + name);
//System.out.println("!ResourceBundle");
						lProperties = new Properties();
						for (Enumeration e = res.getKeys(); e.hasMoreElements(); )
						{
							String key = (String)e.nextElement(), value = res.getString(key);
//System.out.println("! " + key + " = " + value);
							lProperties.put(key, value);
						}
						return lProperties;
					}
					catch (MissingResourceException ex)
					{
						//this is an expected exception - Dr. Pot says; will keep trying with alternative method
					}
				}

				lInputStream = PerpetualResourceLoader.getResourceAsStream( pPropertiesFilename );	//, sLog.isEnabledFor( Log.DEBUG ) );
				try
				{
					// craete a new Properties object and populate it
					lProperties = new Properties();
					lProperties.load( lInputStream );
				}
				finally
				{
					lInputStream.close();		// MP- guarantee closing the stream.  Was leaky before.
				}
				return lProperties;
			}
			catch( IOException e )
			{
				sLog.error( "Could not read properties file:  " + pPropertiesFilename );
				BasePerpetualException.handle( "Could not read properties file:  " + pPropertiesFilename);
			}
//		}
//		catch( Exception e )
//		{
//			PerpetualException.processException( Properties.class.getName() + ":  public static Properties getPropertyFile( " + pPropertiesFilename + " )", e );
//		}

		throw new BasePerpetualException("Error loading " + pPropertiesFilename);
	}

	/**
	 * @roseuid 3AAD21D00010
	 */
	public static String getPropertyString( Properties pProperties, String pPropertyName, String pCallingClass )
		throws BasePerpetualException
	{
		return( getPropertyString( pProperties, pPropertyName, pCallingClass, true ) );
	}

	public static String getPropertyString( Properties pProperties, String pPropertyName, String pCallingClass, boolean pVerbose )
		throws BasePerpetualException
	{
		String returnValue = null;

		try
		{
			try
			{
				returnValue = pProperties.getProperty( pPropertyName );

				if ( returnValue == null ) {
					throw new PropertyHandlerException( "Could not read property:  " + pPropertyName );
				}
			}
			catch( Exception e )
			{
				sLog.error( "Could not read property:  " + pPropertyName, e );
				BasePerpetualException.handle( "Could not read property:  " + pPropertyName, e );
			}

		}
		catch( Exception e )
		{
			BasePerpetualException.handle( Properties.class.getName() + ":  public static String getPropertyString( " + pProperties + ", " + pPropertyName + ", " + pCallingClass + ", " + pVerbose + " )", e );
		}

		if ( pVerbose ) {
			sLog.info( pCallingClass + ":  " + pPropertyName + ":  " + returnValue );
		}

		return( returnValue );
	}

	/**
	 * @roseuid 3AAD21D0001C
	 */
	public static boolean getPropertyBoolean( Properties pProperties, String pPropertyName, String pCallingClass )
		throws BasePerpetualException
	{
		return( getPropertyBoolean( pProperties, pPropertyName, pCallingClass, true ) );
	}

	public static boolean getPropertyBoolean( Properties pProperties, String pPropertyName, String pCallingClass, boolean pVerbose )
		throws BasePerpetualException
	{
		String	stringReturnValue	= null;
		boolean	returnValue			= false;

		try
		{
			try
			{
				stringReturnValue = pProperties.getProperty( pPropertyName );

				if ( stringReturnValue == null ) {
					throw new PropertyHandlerException( "Could not read property:  " + pPropertyName );
				}

				returnValue = Boolean.valueOf( stringReturnValue ).booleanValue();
			}
			catch( Exception e )
			{
				sLog.error( "Could not read property:  " + pPropertyName );
				BasePerpetualException.handle( "Could not read property:  " + pPropertyName, e );
			}
		}
		catch( Exception e )
		{
			BasePerpetualException.handle( Properties.class.getName() + ":  public static boolean getPropertyBoolean( " + pProperties + ", " + pPropertyName + ", " + pCallingClass + ", " + pVerbose + " )", e );
		}

		if ( pVerbose ) {
			sLog.info( pCallingClass + ":  " + pPropertyName + ":  " + returnValue );
		}

		return( returnValue );
	}

	/**
	 * @roseuid 3AAD21D0002F
	 */
	public static int getPropertyInt( Properties pProperties, String pPropertyName, String pCallingClass )
		throws BasePerpetualException
	{
		return( getPropertyInt( pProperties, pPropertyName, pCallingClass, true ) );
	}

	public static int getPropertyInt( Properties pProperties, String pPropertyName, String pCallingClass, boolean pVerbose )
		throws BasePerpetualException
	{
		String	stringReturnValue	= null;
		int		returnValue			= 0;

		try
		{
			try
			{
				stringReturnValue = pProperties.getProperty( pPropertyName );

				if ( stringReturnValue == null ) {
					throw new PropertyHandlerException( "Could not read property:  " + pPropertyName );
				}

				returnValue = Integer.parseInt( stringReturnValue );
			}
			catch( Exception e )
			{
				sLog.error( "Could not read property:  " + pPropertyName );
				BasePerpetualException.handle( "Could not read property:  " + pPropertyName, e );
			}
		}
		catch( Exception e )
		{
			BasePerpetualException.handle( Properties.class.getName() + ":  public static int getPropertyInt( " + pProperties + ", " + pPropertyName + ", " + pCallingClass + ", " + pVerbose + " )", e );
		}

		if ( pVerbose ) {
			sLog.info( pCallingClass + ":  " + pPropertyName + ":  " + returnValue );
		}

		return( returnValue );
	}

	public static long getPropertyLong( Properties pProperties, String pPropertyName, String pCallingClass )
		throws BasePerpetualException
	{
		return( getPropertyLong( pProperties, pPropertyName, pCallingClass, true ) );
	}

	public static long getPropertyLong( Properties pProperties, String pPropertyName, String pCallingClass, boolean pVerbose )
		throws BasePerpetualException
	{
		String	stringReturnValue	= null;
		long	returnValue			= 0L;

		try
		{
			try
			{
				stringReturnValue = pProperties.getProperty( pPropertyName );

				if ( stringReturnValue == null ) {
					throw new PropertyHandlerException( "Could not read property:  " + pPropertyName );
				}

				returnValue = Long.parseLong( stringReturnValue );
			}
			catch( Exception e )
			{
				sLog.error( "Could not read property:  " + pPropertyName );
				BasePerpetualException.handle( "Could not read property:  " + pPropertyName, e );
			}
		}
		catch( Exception e )
		{
			BasePerpetualException.handle( Properties.class.getName() + ":  public static long getPropertyLong( " + pProperties + ", " + pPropertyName + ", " + pCallingClass + ", " + pVerbose + " )", e );
		}

		if ( pVerbose ) {
			sLog.info( pCallingClass + ":  " + pPropertyName + ":  " + returnValue );
		}

		return( returnValue );
	}
}


