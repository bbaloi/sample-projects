/*
 * SnmpTrapSenderAppener.java
 *
 * Created on April 3, 2003, 12:56 PM
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

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.management.util.ManagementConstants; 
import com.perpetual.management.system.PerpetualSnmpTrapSenderMBean;

import org.apache.log4j.Level;

/**
 *
 * @author  brunob
 */
public class SnmpTrapSenderAppender extends AppenderSkeleton
{
     //private static Log sLog = new Log( SnmpTrapSenderAppender.class );
     private PerpetualSnmpTrapSenderMBean lTrapSender=null;
    
    /** Creates a new instance of SnmpTrapSenderAppener */
    public SnmpTrapSenderAppender() 
    {
        super();
        init();
    }
    private void init()
    {
        System.out.println("SnmpTrapSenderAppender.init()");
        lTrapSender = new PerpetualSnmpTrapSenderMBean();
    }
    public void append(LoggingEvent pEvent)
    {
         System.out.println("SnmptrapSenderAppender.append()");
         Level logLevel=null;
         String logMessage=null;
         String loggerName=null;
         Level loggerLevel = null;
         Category cat=null;
         
            //Level level = pEvent.getLevel();
            Priority pLevel = pEvent.level;
            logLevel = Level.toLevel(pLevel.toInt());            
            //cat = pEvent.logger;
            //String loggerName = pEvent.getLoggerName();
            if(cat ==null)
                  System.out.println("NULL category");
            
            loggerName = cat.getName();       
            //loggerLevel = cat.getLevel();
            loggerLevel = cat.getEffectiveLevel();
            String msg =  pEvent.getRenderedMessage();
            LocationInfo locationInfo = pEvent.getLocationInformation();
            
            System.out.println("Log Level:"+logLevel.toInt());
            System.out.println("Logger Name:"+loggerName);
            System.out.println("Logger Level:"+loggerLevel.toInt());
            System.out.println("Log message:"+msg);
            System.out.println("ClassName:"+locationInfo.getClassName());
            System.out.println("method name:"+locationInfo.getMethodName());
           
            if(logLevel.equals(Level.WARN))
            {
                  System.out.println("This is a WARNING message !");
                  String className=locationInfo.getClassName();
                  String methodName=locationInfo.getMethodName();
                  logMessage = "Logger "+loggerName+" reported a warning at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(logLevel.equals(Level.ERROR) )
            {
                  System.out.println("This is an ERROR message !");
                  String className=locationInfo.getClassName();
                  String methodName=locationInfo.getMethodName();
                  logMessage = "Logger "+loggerName+" reported an error at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(logLevel.equals(Level.FATAL))
            {
                System.out.println("This is a FATAL message !");
                String className=locationInfo.getClassName();
                String methodName=locationInfo.getMethodName();
                logMessage = "Logger "+loggerName+" reported a fatal error at-"+className+"."+methodName+"-:Msg="+msg;
            }
            else if(logLevel.equals(Level.INFO))
            {
                System.out.println("This is a INFO message !");
                logMessage = "Src="+loggerName+":Msg="+msg;
            }
            else if(logLevel.equals(Level.DEBUG))
            {
                System.out.println("This is a DEBUG message !");
                logMessage = "Src="+loggerName+":Msg="+msg;
            }
            
            try
            {
                System.out.println("Invoking the WysdomSnmpTrapSenderBean");
                lTrapSender.setLoggerLevel(loggerLevel);
                int intLogLevel = logLevel.toInt();
                lTrapSender.sendV2Trap(ManagementConstants.DefaultSnmpTargetAddress,new Integer(ManagementConstants.DefaultSnmpTargetPort), new Integer(intLogLevel),logMessage);
             }
            catch(Throwable ex)
            {
                System.out.println("could not invoke WysdomLoggerMBean");
                ex.printStackTrace();
            }
    }
    public void close()
    {
    }
    public boolean requiresLayout()
    {
        return true;
    }
}
