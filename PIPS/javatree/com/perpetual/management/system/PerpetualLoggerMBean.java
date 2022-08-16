/*
 * LoggerMBean.java
 *
 * Created on March 14, 2003, 3:25 PM
 */

package com.perpetual.management.system;

import com.perpetual.management.PerpetualMBean;
import com.perpetual.management.notifications.LoggingNotification;
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
import javax.management.ObjectName;

import com.perpetual.management.util.ManagementConstants;
import java.lang.Integer;
import java.util.Date;

import com.perpetual.util.PerpetualC2Logger;
/**
 *
 * @author  brunob
 */
public class PerpetualLoggerMBean extends PerpetualMBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualLoggerMBean.class );
    LoggingNotification lNotification=null;
    
    private String lDescription="Wysdom LoggerMBean";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [1];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [1];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[1];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [2];
    private String [] lNotificationTypes = new String [1];
    /** Creates a new instance of LoggerMBean */
    public PerpetualLoggerMBean() 
    {
        sLog.debug("Constructing LoggerMBean");
        lParameterInfoList[0]=new MBeanParameterInfo("pLevel","java.lang.Integer","Log Level");
        lParameterInfoList[1]=new MBeanParameterInfo("pMessage","java.lang.String","Log Message");
        lOperationInfoList[0] = new MBeanOperationInfo("doLog","Sends a Logging Notification", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lConstructorInfoList[0]= new MBeanConstructorInfo("WysdomLoggerMBean","WysdomLoggerMBeanConstructor",null);
        
        lNotificationTypes[0]= ManagementConstants.LoggingNotificationType;        
        lNotificationInfoList[0]= new MBeanNotificationInfo(lNotificationTypes,
                                "com.wysdom.management.notifications.LoggingNotification",
                                null);
    }
    public void doLog(Integer pLevel,String pMessage)
    {
        sLog.debug("in doLog():params:pLevel="+pLevel.intValue()+";message="+pMessage);        
        
        LoggingNotification notification = new LoggingNotification(ManagementConstants.LoggingNotificationType,
                           this,5,(new Date()).getTime(),"This is a Logging Notification",pLevel,pMessage);
        //Notification notification = new Notification(ManagementConstants.LoggingNotificationType,
          //                  this,5,(new Date()).getTime(),"This is a Logging Notification");
        notification.setUserData(new String("Log level:"+pLevel+" - Message:"+pMessage));
        sLog.debug("sending a Logging notification");
        sendNotification(notification);
    }
    public void handleNotification(Notification pNotification,Object pHandback)
    {
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
	else if (operationName.equals("doLog"))
        {
            doLog((Integer)params[0],(String)params[1]);
        }
        else
            // unrecognized operation name:
	    throw new ReflectionException(new NoSuchMethodException(operationName),
					  "Cannot find the operation " + operationName + " in " + lClassName);
	
            return null;
    }
}
