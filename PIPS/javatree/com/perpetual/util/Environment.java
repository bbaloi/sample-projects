/*
 * Environment.java
 *
 * Created on April 8, 2003, 5:20 PM
 */

package com.perpetual.util;

import java.util.Properties;
import java.lang.System;
import java.net.InetAddress;
import org.apache.log4j.Priority;

import com.perpetual.util.PerpetualC2Logger;
import java.net.UnknownHostException;
import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */
public final class Environment 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( Environment.class );    
   
    private static String lLog4jFile=null;
    private static String lMibFile=null;
    private static String hostName=null;
    private static String userAccount=null;
    private static String runtimeDirectory=null;
    private static String jndiPath=null;
    private static String dbPath=null;
    private static String perpetualHome=null;
    private static String lRecordPatternFile=null;
    private static String lMessagePatternFile=null;
    private static String lExportConfigFile=null;
    private static String lSnmpConfigFile=null;
    /** Creates a new instance of Environment */
    public Environment() 
    {      
    }
    /*
    private static String getAppServerHome()
    {
        String serverHome=null;
          Properties env = new Properties();
           try 
           {
            env.load(Runtime.getRuntime().exec("env").getInputStream());
            serverHome = (String) env.get("APP_SERVER_HOME");
               System.out.println("AppServerHome="+serverHome);
            
           }
           catch(Exception ex)
           {
               ex.printStackTrace();
           }
           return serverHome;
    }*/
    public static String getMIBFile()
    {
        if(lMibFile==null)
        {
            if(perpetualHome==null)
            {
              perpetualHome=System.getProperty("perpetualhome");   
            }
            lMibFile = perpetualHome+"/config/PerpetualMIB";
            
            
        }
        return lMibFile;
    }
    public static String getSNMPConfigFile()
    {
        if(lSnmpConfigFile==null)
        {
            if(perpetualHome==null)
            {
              perpetualHome=System.getProperty("perpetualhome");   
            }
            lSnmpConfigFile = perpetualHome+"/config/snmp.properties";
            
        }
        return lSnmpConfigFile;
    }
    public static String getLog4JFile()
    {
        if(lLog4jFile==null)
        {
            String perpetualHome=System.getProperty("perpetualhome");
            //Properties sysProps = System.getProperties();
            //lLog4jFile = getRuntimeDirectory() + "/perpetual_viewer/config/log4j.properties";            
            lLog4jFile=perpetualHome+"/config/log4j.properties";
            System.out.println("log4Jfile path: "+lLog4jFile);
        }
        return lLog4jFile;
    }
    public static String getHostName() throws BasePerpetualException
    {
        if(hostName==null)
        {
            try
            {
                InetAddress netAddress = InetAddress.getLocalHost();
                hostName = netAddress.getHostName();
                if(hostName==null)
                    hostName = netAddress.getHostAddress();
            }
            catch(java.net.UnknownHostException excp)
            {
                sLog.error("COuldn't get network interface name");
                BasePerpetualException.handle("Can't get host name!");
            }
        }
        return hostName;
    }
    public static String getUserAccount()
    {
        if(userAccount==null)
        {
            Properties sysProps=System.getProperties();
            userAccount = sysProps.getProperty("user.name");            
        }
       
        return userAccount;
    }
    public static String getRuntimeDirectory()
    {
        if(runtimeDirectory==null)
        {
            Properties sysProps=System.getProperties();
            //runtimeDirectory = sysProps.getProperty("user.dir"); 
            //System.out.println("Runtime dir:"+runtimeDirectory);
            runtimeDirectory = sysProps.getProperty("user.home"); 
            System.out.println("User Home dir:"+runtimeDirectory);
        }
        return runtimeDirectory;
    }
    public static String getJndiPath()
    {
        if(jndiPath==null)
        {
            if(perpetualHome==null)
                perpetualHome=System.getProperty("perpetualhome");
            jndiPath=perpetualHome+"/config/jndi.properties";
            //jndiPath="jndi.properties";
        }
        return jndiPath;
    }
    public static String getDBPath() 
    {
        
        if(dbPath==null)
        {            
            if(perpetualHome==null)
                perpetualHome=System.getProperty("perpetualhome");
            sLog.debug("PerpetualHome:"+perpetualHome);
          dbPath=perpetualHome+"/config/db.properties";
        }
        
        return dbPath;
    }
    public static String getRecordPatternConfigPath()
    {
        if(lRecordPatternFile==null)
        {            
            if(perpetualHome==null)
                perpetualHome=System.getProperty("perpetualhome");
            //sLog.debug("PerpetualHome:"+perpetualHome);
          lRecordPatternFile=perpetualHome+"/config/recordformat.xml";
        }
        
        return lRecordPatternFile;
    }
    public static String getMessagePatternConfigPath()
    {
         if(lMessagePatternFile==null)
        {            
            if(perpetualHome==null)
                perpetualHome=System.getProperty("perpetualhome");
            //sLog.debug("PerpetualHome:"+perpetualHome);
          lMessagePatternFile=perpetualHome+"/config/messageformat.xml";
        }
        
        return lRecordPatternFile;
    }
    public static String getExportsConfigPath()
    {
         if(lExportConfigFile==null)
        {            
            if(perpetualHome==null)
                perpetualHome=System.getProperty("perpetualhome");
            //sLog.debug("PerpetualHome:"+perpetualHome);
          lExportConfigFile=perpetualHome+"/config/exportsformat.xml";
        }
        
        return lExportConfigFile;
    }
}
