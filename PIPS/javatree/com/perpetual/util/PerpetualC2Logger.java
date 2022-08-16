/**
 * Title:        Log<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Bruno Baloi<p>
 * Company:      Perpetual IP GmbH<p>
 * @author Bruno Baloi
 * @version 1.0
 */
package com.perpetual.util;

import java.io.InputStream;

import java.util.Properties;

// LOG4J 1.0 classes
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

//import org.jboss.logging.Logger;

import com.perpetual.exception.BasePerpetualException;

public class PerpetualC2Logger
{
	public static Priority ERROR	= Priority.ERROR;
	public static Priority WARN	= Priority.WARN;
	public static Priority INFO	= Priority.INFO;
	public static Priority DEBUG	= Priority.DEBUG;
        private  static Logger jbossLogger  = null;
        
	private static final String LOG4J_PROPERTIES_FILENAME = "log4j.properties";

	private static Category		sCategory	= null;
	private static Properties	sProperties	= null;
	private Category category = null;

	static
	{
		if (isRunningInContainer()) {
			jbossLogger = Logger.getLogger(PerpetualC2Logger.class.getName());
			//this is running inside of JBoss do nothing
			jbossLogger.error("This is a JBoss instance !!!");                    
		}
		else {
            System.out.println("This is a standalone JVM !");
            sCategory = Category.getInstance( PerpetualC2Logger.class.getName() );

			// load the properties that log4j should have loaded
            try
            
            {
                System.out.println("getting Log4J properties");
                String propFile = Environment.getLog4JFile();
                System.out.println("Log4JProperty file:"+propFile);
                sProperties = PerpetualPropertiesHandler.getPropertyFile( propFile );
                 configure(sProperties);
            }
            catch( Throwable e )
            {
            	// if called from JUnit - don't do anything
            	if (!isCalledFromJUnit(e.getStackTrace())) {
					System.out.println("Failed to load config file in static block...");
					e.printStackTrace();
            	}
            }
        }
	}
	
	private static boolean isRunningInContainer()
	{
		boolean result = true;
		
		// check if JBoss main class is loadable
		try {
			
			Class jbossMain = Class.forName("org.jboss.Main");
		} catch (ClassNotFoundException e) {
			// class not found - must be running outside
			result = false;
		}
		
		return result;
	}
	
	private static boolean isCalledFromJUnit (StackTraceElement[] stackTrace)
	{
		boolean found = false;
		
		for (int i = 0; !found && i < stackTrace.length; i++) {
			String invokingClassName = stackTrace[i].getClassName();
			
			found = invokingClassName.indexOf("junit.framework.TestCase") != -1;
		}
		
		return found;
	}
	
	public static void configure( Properties pProperties )
		throws BasePerpetualException
	{
		// save last Properties file used to configure log4j
		sProperties = pProperties;

		// reset log4j
		//PropertyConfigurator.resetConfiguration();

		// configure log4j
                System.out.println("trying to configure log4j!");
                //pProperties.list(System.out);
                
		PropertyConfigurator.configure( pProperties );

		sCategory.info( "log4j reinitialized..." );
                
	}
	
	public static Properties getProperties()
	{
		return( sProperties );
	}

	/**
	 * @roseuid 3AAD21CF00C3
	 */
	public PerpetualC2Logger(Class pClass)
	{
		this( pClass.getName() );
	}

	public PerpetualC2Logger(String pClassName)
	{
                jbossLogger = Logger.getLogger(pClassName);    
		//category = Category.getInstance( pClassName );
	}

	/**
	 * @roseuid 3AAD21CF00EB
	 */
	
	/**
	 * @roseuid 3AAD21CF00FF
	 */
	public void error( String pMessage )
	{
            
            if(jbossLogger!= null)
               jbossLogger.error(C2Format(pMessage));
            else
		category.error( C2Format(pMessage));
	}

	/**
	 * @roseuid 3AAD21CF0113
	 */
	public void error( Object pObject )
	{
            //if(jbossLogger !=null)
                jbossLogger.error(pObject);
            //else                
		//category.error( pObject );
	}

	/**
	 * @roseuid 3AAD21CF0127
	 */
	public void error( String pMessage, Throwable pThrowable )
	{
            if(jbossLogger!=null)
                jbossLogger.error(C2Format(pMessage),pThrowable);
            else
                category.error( C2Format(pMessage), pThrowable );
	}

	/**
	 * @roseuid 3AAD21CF0145
	 */
	public void warn( String pMessage )
	{
            if(jbossLogger!=null)
                jbossLogger.warn(C2Format(pMessage));
            else
		category.warn( C2Format(pMessage) );
	}
	
	/**
	 * @roseuid 3AAD21CF0159
	 */
	public void warn( Object pObject )
	{
            if(jbossLogger!=null)
                jbossLogger.warn(pObject);
            else
		category.warn( pObject );
	}
	
	/**
	 * @roseuid 3AAD21CF016D
	 */
	public void warn( String pMessage, Throwable pThrowable )
	{
            if(jbossLogger!=null)
                jbossLogger.warn(C2Format(pMessage),pThrowable);
            else
		category.warn( C2Format(pMessage), pThrowable );
	}

	/**
	 * @roseuid 3AAD21CF0195
	 */
	public void info( String pMessage )
	{
            if(jbossLogger!=null)
                jbossLogger.info(C2Format(pMessage));
            else
		category.info( C2Format(pMessage));
	}

	/**
	 * @roseuid 3AAD21CF01A9
	 */
	public void info( Object pObject )
	{
            if(jbossLogger!=null)
                jbossLogger.info(pObject);
            else
		category.info( pObject );
	}

	/**
	 * @roseuid 3AAD21CF01BD
	 */
	public void info( String pMessage, Throwable pThrowable )
	{
            if(jbossLogger!=null)
                jbossLogger.info(C2Format(pMessage),pThrowable);
            else
		category.info( C2Format(pMessage), pThrowable );
	}

	/**
	 * @roseuid 3AAD21CF01E5
	 */
	public void debug( String pMessage )
	{
            if(jbossLogger!=null)
                jbossLogger.debug(C2Format(pMessage));
           else
		category.debug( C2Format(pMessage) );
	}

	/**
	 * @roseuid 3AAD21CF01F9
	 */
	public void debug( Object pObject )
	{
            if(jbossLogger!=null)
                jbossLogger.debug(pObject);
            else
                category.debug( pObject );
	}
        public void debug( String pMessage, Throwable pThrowable )
	{
            if(jbossLogger!=null)
                jbossLogger.debug(C2Format(pMessage),pThrowable);
            else
		category.debug( C2Format(pMessage), pThrowable );
	}
        public void fatal( Object pObject )
	{
            if(jbossLogger!=null)
                jbossLogger.fatal(pObject);
            else
		category.fatal( pObject );
	}

        public void fatal( String pMessage, Throwable pThrowable )
	{
            if(jbossLogger!=null)
                jbossLogger.fatal(C2Format(pMessage),pThrowable);
            else
		category.fatal( C2Format(pMessage), pThrowable );
	}
	/**
	 * @roseuid 3AAD21CF020D
	 */
		
	public void setPriority( Priority pPriority )
	{
		category.setPriority( pPriority );
	}
        
        private String C2Format(String pMessage)
        {
            String c2msg=null;
            try
            {
                 c2msg = Environment.getUserAccount()+"@"+Environment.getHostName()+":"+pMessage;
            }
            catch(BasePerpetualException excp)
            {
                jbossLogger.error("Couldn't get hostname or user account - Can't construct proper C2 Message !");
            }
            return c2msg;
       }
}
