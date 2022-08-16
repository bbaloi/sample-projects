/*
 * SnmpTrapAppender.java
 *
 * Created on March 27, 2003, 12:22 PM
 */

package com.perpetual.management.appenders;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.Category;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.spi.LocationInfo;


import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.MBeanServer;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.management.util.ManagementConstants; 
import java.util.Set;

/**
 *
 * @author  brunob
 */
public class SnmpTrapAppender extends AppenderSkeleton
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SnmpTrapAppender.class );
    private ObjectName lLoggerMBean=null;
    private MBeanServer lServer=null;
    /** Creates a new instance of SnmpTrapAppender */
    
    public SnmpTrapAppender(MBeanServer pServer)
    {
         lServer=pServer;
      try
        {
            lLoggerMBean = new ObjectName(ManagementConstants.LoggingObjectName);
        }
        catch(MalformedObjectNameException ex)
        {
            sLog.error("Could not create name for WysdomLoggerMBean",ex);
        }
       
    }
    
    public SnmpTrapAppender() 
    {
        try
        {
            lLoggerMBean = new ObjectName(ManagementConstants.LoggingObjectName);
        }
        catch(MalformedObjectNameException ex)
        {
            sLog.error("Could not create name for WysdomLoggerMBean",ex);
        }
        lServer=getMBeanServer();
    }
    private MBeanServer getMBeanServer()
    {
        //get a handle to the MBeanServer.....
        return null;
    }
    public void append(LoggingEvent pEvent)
    {
        int logLevel=0;
        String logMessage=null;
        Category cat=null;
        
        Set mbeanSet = lServer.queryMBeans(lLoggerMBean,null);
        if(mbeanSet.isEmpty())
        {
            sLog.error("No Logging MBean registered !");
        }
            sLog.debug("Found LoggerMBean !!! - set Size="+mbeanSet.size());
            //Level level = pEvent.getLevel();
            Priority level = pEvent.level;
            //cat = pEvent.logger;
            String loggerName = cat.getName();
            //String loggerName = pEvent.getLoggerName();
            String msg =  pEvent.getRenderedMessage();
            LocationInfo locationInfo = pEvent.getLocationInformation();
            
            if(level.WARN!=null)
            {
                  logLevel=ManagementConstants.WARNING;   
                  String className=locationInfo.getClassName();
                  String methodName=locationInfo.getMethodName();
                  logMessage = "Logger "+loggerName+" reported a warning at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(level.ERROR !=null)
            {
                  logLevel=ManagementConstants.ERROR;  
                  String className=locationInfo.getClassName();
                  String methodName=locationInfo.getMethodName();
                  logMessage = "Logger "+loggerName+" reported an error at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(level.FATAL!=null)
            {
                logLevel=ManagementConstants.FATAL;
                String className=locationInfo.getClassName();
                String methodName=locationInfo.getMethodName();
                logMessage = "Logger "+loggerName+" reported a fatal error at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(level.INFO!=null)
            {
                logLevel=ManagementConstants.INFO; 
                logMessage = "Src="+loggerName+":Msg="+msg;
            }
                      
            Object [] params = new Object[2];
            params[0] = new Integer(logLevel);
            params[1] = logMessage; 
            String [] signature = new String [2];
            signature[0] = "java.lang.Integer";
            signature[1] = "java.lang.String"; 
            try
            {
                sLog.debug("Invoking the WysdomLoggerMBean");
                lServer.invoke(lLoggerMBean,"doLog",params,signature);
             }
            catch(Throwable ex)
            {
                sLog.error("couldn not invoke WysdomLoggerMBean",ex);
            }
        
       
    }
    
    public void close()
    {
    }
    
    public boolean requiresLayout()
    {
        return false;
    }
}
