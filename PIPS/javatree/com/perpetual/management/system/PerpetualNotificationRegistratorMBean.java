/*
 * WysdomNotificationRegistratorMBean.java
 *
 * Created on March 26, 2003, 4:32 PM
 */

package com.perpetual.management.system;

import com.perpetual.management.PerpetualMBean; 

import javax.management.Notification;
import javax.management.MBeanInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.RuntimeOperationsException;
import javax.management.MalformedObjectNameException;
import javax.management.ListenerNotFoundException;
import javax.management.ObjectName;
import javax.management.MBeanServerNotification;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class PerpetualNotificationRegistratorMBean extends PerpetualMBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualNotificationRegistratorMBean.class );
    private MBeanServer lServer=null;
       
    private String lDescription="PerpetualNotificationRegistratorMBean";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [2];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [1];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[0];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [2];
    
    /** Creates a new instance of WysdomNotificationRegistratorMBean */
    public PerpetualNotificationRegistratorMBean()
    {
        sLog.debug("Constructing PerpetualNotificationRegistratorMBean");
        lParameterInfoList[0]=new MBeanParameterInfo("pNotificationBroadcasterName","java.lang.String","Notification Broadcaster Name");
        lParameterInfoList[1]=new MBeanParameterInfo("pNotificationReceiverName","java.lang.String","Notification Receiver Name");
        
        lOperationInfoList[0] = new MBeanOperationInfo("registerForNotification"," registers two MBeans for notification", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lOperationInfoList[1] = new MBeanOperationInfo("deregister","deregisters two beans from notifications", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lConstructorInfoList[0]= new MBeanConstructorInfo("PerpetualNotificationRegistratorMBean","PerpetualNotificationRegistratorMBean Constructor",null);
        
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
    public Object invoke(String operationName, Object params[], String signature[])
	throws MBeanException,ReflectionException 
    {
        // Check operationName is not null to avoid NullPointerException later on
	if (operationName == null) {
	    throw new RuntimeOperationsException(new IllegalArgumentException("Operation name cannot be null"),
						 "Cannot invoke a null operation in " + lClassName);
	}
	// Check for a recognized operation name and call the corresponding operation
	else if (operationName.equals("registerForNotification"))
        {
            try
            {
                registerForNotification((String)params[0],(String)params[1]);
            }
            catch(Throwable t)
            {
                sLog.error("Couldn't register for notification !",t);
            }
            }
        else if (operationName.equals("deregister"))
        {
            try
            {
                deregister((String)params[0],(String)params[1]);
            }
            catch(Throwable t)
            {
                sLog.error("Couldn't register for notification !",t);
            }
        }
        
        else
            // unrecognized operation name:
	    throw new ReflectionException(new NoSuchMethodException(operationName),
					  "Cannot find the operation " + operationName + " in " + lClassName);
	
         return null;
    }
    
    private String getBroadcasterName(String pBroadcasterName)
    {
        String broadcasterObjName=null;
        if(pBroadcasterName==null)
             broadcasterObjName=ManagementConstants.MBeanServerDelegateName;
       else 
           broadcasterObjName=ManagementConstants.Domain+":name="+pBroadcasterName;
                  
        return broadcasterObjName;
    }
    public void registerForNotification(String pNotificationBroadcasterName,String pNotificationReceiverName) throws InstanceNotFoundException,MalformedObjectNameException
    {
        sLog.debug("Notification Registration:"+pNotificationReceiverName+" will be notified by "+pNotificationBroadcasterName);
       String receiverObjName=null;
       String broadcasterObjName=null;
       
       //register with the MBeanServer for all server related notification
        broadcasterObjName= getBroadcasterName(pNotificationBroadcasterName);
        receiverObjName=ManagementConstants.Domain+":name="+pNotificationReceiverName;
        sLog.debug("broadcasterObjName:"+broadcasterObjName);
        sLog.debug("receiverObjName:"+receiverObjName);
        ObjectName lEventBroadcaster = new ObjectName(broadcasterObjName);
        ObjectName lEventReceiver = new ObjectName(receiverObjName);
        if(lEventBroadcaster==null)
            sLog.debug("coulnd't create broadcaster:");
        if(lEventReceiver==null)
            sLog.debug("coulnd't create receiver:");
        
          lServer.addNotificationListener(lEventBroadcaster,lEventReceiver,null,null);
        
    }
    public void deregister(String pNotificationBroadcasterName,String pNotificationReceiverName) throws InstanceNotFoundException,MalformedObjectNameException,ListenerNotFoundException
    {
        sLog.debug("Notification Deregistration:Deregistering "+pNotificationReceiverName+" from "+pNotificationBroadcasterName);
       String receiverObjName=null;
       String broadcasterObjName=null;
       
       //register with the MBeanServer for all server related notification
         broadcasterObjName= getBroadcasterName(pNotificationBroadcasterName);
         receiverObjName=ManagementConstants.Domain+":name="+pNotificationReceiverName;
         ObjectName lEventBroadcaster = new ObjectName(broadcasterObjName);
         ObjectName lEventReceiver = new ObjectName(receiverObjName);
       
         lServer.removeNotificationListener(lEventBroadcaster,lEventReceiver);
    
    }
     public ObjectName preRegister(MBeanServer pServer,ObjectName pName) throws java.lang.Exception
    {
        sLog.debug("pre-registering the Notification Registrator");
        lServer = pServer;
        //ObjectName serverNotificationName = new ObjectName(ManagementConstants.MBeanServerDelegate);
        //lServer.addNotificationListener(serverNotificationName,pName,null,null);
               
        return pName;
     }
     public void handleNotification(Notification pNotification,Object pHandback)
    {
        MBeanServerNotification not = (MBeanServerNotification) pNotification;
        String beanName = not.getMBeanName().toString();
        String notificationType = pNotification.getClass().getName();
        
        
        sLog.debug("Got "+notificationType+" notification from "+beanName);
    }
}
