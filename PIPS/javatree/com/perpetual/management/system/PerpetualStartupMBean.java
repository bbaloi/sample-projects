/*
 * StartupMBean.java
 *
 * Created on April 4, 2003, 4:03 PM
 */

package com.perpetual.management.system;

import com.perpetual.management.PerpetualMBean;
import com.perpetual.management.util.ManagementConstants;
import com.perpetual.util.PerpetualC2Logger;

import javax.management.MBeanInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.Notification;
import javax.management.MBeanServer;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.management.ObjectName;

import java.util.Set;

/**
 *
 * @author  brunob
 */
public class PerpetualStartupMBean extends PerpetualMBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualStartupMBean.class );
    private MBeanServer lServer=null;
    
    private String lDescription="Wysdom Startup MBean";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [0];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [1];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[0];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [0];
        
    /** Creates a new instance of StartupMBean */
    public PerpetualStartupMBean() 
    {
        sLog.debug("Constructing StartupMBean");
        lConstructorInfoList[0]= new MBeanConstructorInfo("WysdomStartupMBean","WysdomStartupMBeanConstructor",null);
    }
    
    public void postRegister(Boolean registrationDone)
    { 
        sLog.debug("Regsitration of StartupMBean completed ! ");
        ObjectName loggerName=null,trapSenderName=null,registratorName=null;
                
            try
            {
                loggerName= new ObjectName(ManagementConstants.LoggingObjectName);
                trapSenderName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
                registratorName = new ObjectName(ManagementConstants.NotificationRegistratorObjectName);
       
            }
             catch(javax.management.MalformedObjectNameException ex)
            {
                sLog.error("Couldn't cerate object names !!!",ex);
                ex.printStackTrace();
            }
            sLog.debug("Querying MBean server for the beans to regsiter for notification !");
            Set loggerMBeanSet = lServer.queryMBeans(loggerName,null);
            Set registratorMBeanSet = lServer.queryMBeans(registratorName,null);
            Set trapSenderMBeanSet = lServer.queryMBeans(trapSenderName,null);
        
             if(loggerMBeanSet.isEmpty() || registratorMBeanSet.isEmpty() || trapSenderMBeanSet.isEmpty())
            {
                sLog.error("No MBean registered !");
            }
            else
            {
                sLog.debug("-------------Regsitering TrapSender with Logger-----------");
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
             }
                        
    }
    public void handleNotification(Notification pNotification,Object pHandback)
    {
     }
    public ObjectName preRegister(MBeanServer pServer,ObjectName pName) throws java.lang.Exception
    {
        sLog.debug("pre registering the StartupMBean Sender");
        lServer = pServer;
               
        return pName;
    }
    public MBeanInfo getMBeanInfo()
    {
        if (lBeanInfo == null)
        {
            //construct the MBeanInfo
            lClassName = this.getClass().getName();
            
            lBeanInfo = new MBeanInfo(lClassName,lDescription,lAttributeInfoList,
                                        lConstructorInfoList, lOperationInfoList, 
                                        lNotificationInfoList);
        }
            
        return lBeanInfo;
        
    }
}
