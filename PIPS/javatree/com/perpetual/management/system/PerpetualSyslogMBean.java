/*
 * LoggerMBean.java
 *
 * Created on March 14, 2003, 3:25 PM
 */

package com.perpetual.management.system;

import com.perpetual.management.PerpetualMBean;
import com.perpetual.management.notifications.LoggingNotification;
import com.perpetual.management.notifications.SyslogNotification;
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
public class PerpetualSyslogMBean extends PerpetualMBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualLoggerMBean.class );
    private SyslogNotification lNotification=null;
    
    private String lDescription="Perpetual Syslog MBean";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [1];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [1];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[1];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [9];
    private String [] lNotificationTypes = new String [1];
    /** Creates a new instance of LoggerMBean */
    public PerpetualSyslogMBean() 
    {
        sLog.debug("Constructing LoggerMBean");
        lParameterInfoList[0]=new MBeanParameterInfo("pDestHost","java.util.String","SNMP Trap Receiver");
        lParameterInfoList[1]=new MBeanParameterInfo("pDestHostPort","java.lang.Integer","SNMP Trap Receiver");
        lParameterInfoList[2]=new MBeanParameterInfo("pTimeStamp","java.util.String","Syslog TimeStamp");
        lParameterInfoList[3]=new MBeanParameterInfo("pHostName","java.lang.String","Event Host Name");
        lParameterInfoList[4]=new MBeanParameterInfo("pFacility","java.lang.Integer","Facility");
        lParameterInfoList[5]=new MBeanParameterInfo("pSeverity","java.lang.Integer","Severity");
        lParameterInfoList[6]=new MBeanParameterInfo("pPid","java.lang.Integer","PID");
        lParameterInfoList[7]=new MBeanParameterInfo("pProcess","java.lang.String","Process Name");
        lParameterInfoList[8]=new MBeanParameterInfo("pSyslogMessage","java.lang.String","Syslog Message");
        
        lOperationInfoList[0] = new MBeanOperationInfo("sendSyslogNotification","Sends a Syslog Notification", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lConstructorInfoList[0]= new MBeanConstructorInfo("PerpetualSyslogMBean","PerpetualSyslogMBeanConstructor",null);
        
        lNotificationTypes[0]= ManagementConstants.SyslogNotificationType;        
        lNotificationInfoList[0]= new MBeanNotificationInfo(lNotificationTypes,
                                "com.perpetual.management.notifications.SysloNotification",
                                null);
    }
    public void sendSyslogNotification(String pDestHost,Integer pPort,String pTimeStamp,String pHostName,Integer pFacility,Integer pSeverity,
                Integer pPid, String pProcess,String pSyslogMessage)
    {
        sLog.debug("in sendSyslogNotification()");        
        
        SyslogNotification notification = new SyslogNotification(ManagementConstants.SyslogNotificationType,
                           this,5,(new Date()).getTime(),"This is a SyslogNotification Notification",
                           pDestHost,pPort,pTimeStamp,pHostName,pFacility,pSeverity,pPid,pProcess,pSyslogMessage);
        
       notification.setUserData(new String("EventTimestamp:"+pTimeStamp+",HostName:"+pHostName
                                +",Facility:"+pFacility+",Severity:"+pSeverity+",PID:"+pPid
                                +",ProcessName:"+pProcess+",SyslogMessage:"+pSyslogMessage));
       sLog.debug("Sending a Syslog notification");
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
	else if (operationName.equals("sendSyslogNotification"))
        {
            sendSyslogNotification((String)params[0],(Integer)params[1],(String) params[2],(String)params[3],(Integer)params[4],(Integer)params[5],
                    (Integer)params[6],(String)params[7],(String)params[8]);
        }
        else
            // unrecognized operation name:
	    throw new ReflectionException(new NoSuchMethodException(operationName),
					  "Cannot find the operation " + operationName + " in " + lClassName);
	
            return null;
    }
}
