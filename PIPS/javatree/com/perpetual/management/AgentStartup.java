/*
 * Startup.java
 *
 * Created on March 18, 2003, 1:41 PM
 */

package com.perpetual.management;

import com.perpetual.util.PerpetualC2Logger;
import javax.management.MBeanServer;
import javax.management.QueryExpSupport;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.Set;

import com.perpetual.management.system.PerpetualLoggerMBean;
import com.perpetual.management.util.ManagementConstants;
import org.apache.log4j.Level;
/**
 *
 * @author  brunob
 */
public class AgentStartup
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( AgentStartup.class );
    private PerpetualManagementAgent managementAgent=null;
    
    /** Creates a new instance of Startup */
    public AgentStartup() {
    }
    
    public static void main(String [] args)
    {
        
        try
        {
            sLog.debug("------Beginnig SNMP Test !------");            
            AgentStartup lAgentStartup = new AgentStartup();
            ManagementConstants consts = new ManagementConstants();
            lAgentStartup.initManagementAgent();
            lAgentStartup.initManagementComponents(PerpetualManagementAgent.getInstance().getMBeanServer());
            //waste a few cyscles
            for(int i=0;i<100000;i++);
            lAgentStartup.simulateTrap();
            for(int i=0;i<100000;i++);
            //System.exit(0);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
  
    }
        public void initManagementAgent()
        {
            try
            {
                sLog.debug("------Starting up the Agent !------");
                managementAgent = PerpetualManagementAgent.getInstance();
                managementAgent.initialize(null,null);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void initManagementComponents(MBeanServer pMBeanServer)
        {
            sLog.debug("------Loading up the MBeans !------");
            //MBeanServer lServer = WysdomManagementAgent.getInstance().getMBeanServer();
            MBeanLoader lBeanLoader = new MBeanLoader(pMBeanServer);
            lBeanLoader.loadMBeans();
        }
    public void simulateTrap()
    {
        sLog.debug("------Simulating Logging Notification !------");
        ObjectName loggerName=null,registratorName=null,trapSenderName=null;
        MBeanServer lServer = PerpetualManagementAgent.getInstance().getMBeanServer();
           
        // get a reference to the MBean server and to the LoggerMBean and do -> doLog()
        try
        {
         loggerName= new ObjectName(ManagementConstants.LoggingObjectName);
         registratorName = new ObjectName(ManagementConstants.NotificationRegistratorObjectName);
         trapSenderName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
        }
        catch(javax.management.MalformedObjectNameException ex)
        {
            ex.printStackTrace();
        }
        
        
        
        //QueyExpSupport qExp = new QueryExpSupport();
        Set loggerMBeanSet = lServer.queryMBeans(loggerName,null);
        Set registratorMBeanSet = lServer.queryMBeans(registratorName,null);
        Set trapSenderMBeanSet = lServer.queryMBeans(trapSenderName,null);
        
        if(loggerMBeanSet.isEmpty() || registratorMBeanSet.isEmpty() || trapSenderMBeanSet.isEmpty())
        {
            sLog.error("No MBean registered !");
        }
        else
        {
            sLog.debug("-------------Registering TrapSender with Logger-----------");
           try
            {
               Object [] nameParams = new Object[2];
               nameParams[0]=new String("WysdomLoggerMBean");
               nameParams[1]=new String("WysdomSnmpTrapSenderMBean");
               lServer.invoke(registratorName,"registerForNotification",nameParams,null);
             }
            catch(Throwable ex)
            {
                sLog.error("couldn not invoke NotificationRegsitratorMBean",ex);
            }
            //...........get LoggingMBean
            sLog.debug("------invoking LoggerMBean----------");
            Object [] params = new Object[2];
            params[0] = new Integer(Level.INFO.toInt());
            params[1] = "Logging Level 1 - This is just info"; 
            String [] signature = new String [2];
            signature[0] = "java.lang.Integer";
            signature[1] = "java.lang.String"; 
            try
            {
                sLog.debug("Invoking the WysdomLoggerMBean");
                lServer.invoke(loggerName,"doLog",params,null);
             }
            catch(Throwable ex)
            {
                sLog.error("couldn't not invoke LoggerMBean",ex);
            }
            
        }
        
        
    }
   
}
