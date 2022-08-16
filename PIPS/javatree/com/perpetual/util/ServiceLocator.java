/*
 * ServiceLocator.java
 *
 * Created on June 28, 2003, 8:02 PM
 */

package com.perpetual.util;

import java.io.File;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.InitialContext;
/**
 *
 * @author  brunob
 */
public class ServiceLocator 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( ServiceLocator.class );    
    
    private static ServiceLocator locator=null;
    
        protected static InitialContext lContext=null;
        protected String lContextFactory="org.jnp.interfaces.NamingContextFactory";
        //protected String lContextFactory = "org.jbos.naming.HttpNamingContextFactory";
        protected String lURL="jnp://localhost:1099";
        //protected String lURL="http://localhost:8080/invoker/JNDIFactory";
        protected String lPrefixes = "org.jboss.naming:org.jnp.interfaces";
        
        protected Properties lProps = null;
        protected PerpetualResourceLoader rl=null;
        protected String jndiFile = "jndi.properties"; 
        protected static Hashtable jndiNameTable = null;
        
    /** Creates a new instance of ServiceLocator */
    private ServiceLocator() 
    {
        jndiNameTable = new Hashtable();
        loadProperties();
          //load up values
        //if(sLog.isEnabledFor(Priority.DEBUG))  
        //sLog.debug("Setting up JNDI Properties !");
                 //lProps = new Properties();	
                //lProps.put(Context.INITIAL_CONTEXT_FACTORY,lContextFactory);
                //lProps.put(Context.PROVIDER_URL,lURL);
                //lProps.put(Context.URL_PKG_PREFIXES,lPrefixes);
          try
          {
                    if(lProps !=null)
                    {
                        lProps.list(System.out);
                        lContext = new InitialContext(lProps);
                        sLog.debug("Created Initial Context from properties !");
                    }
                    else
                    {
                        lContext = new InitialContext();
                        sLog.debug("Created Initial Context from container !");
                    }
                   
                     
                }
                catch(Throwable t)
                {
                    sLog.error("couldn't get a handle on the PJVTProducerSession bean !");
                    t.printStackTrace();
                }
    }
    
    protected void loadProperties()
    {
            rl = PerpetualResourceLoader.getDefault();
            try
            {
             String propFile = Environment.getJndiPath();
             File file = new File(propFile);
		if (file.isFile())
                {
                    lProps=rl.getProperties(propFile); 
                }             
             
            }
            catch(Throwable t)
            {
                sLog.error("Couldn't get properties !");
            }
    }
    
    public static ServiceLocator getServiceLocatorInstance()
    {
        if(locator==null)
            locator = new ServiceLocator();
        return locator;
    }
    public  InitialContext getContext()
    {
        return lContext;
    }
    
    public static Object findHome(String pJndiName) throws javax.naming.NamingException
    {
		Object homeObj=null;
        
        //make sure everything is initialized
        if (locator == null) {
        	getServiceLocatorInstance();
        }
        
		homeObj = jndiNameTable.get(pJndiName);
		
		if (homeObj==null) {
			sLog.debug("Creating instance of "+pJndiName+" and adding it to cache !"); 
			homeObj = lContext.lookup(pJndiName);
			jndiNameTable.put(pJndiName,homeObj);
		}
		else
			sLog.debug("got Home from Cache !");
       
		return homeObj;
    }
           
    
}
