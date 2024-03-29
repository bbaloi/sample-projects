/*
 * WysdomSNMPTrapSenderAdaptor.java
 *
 * Created on March 20, 2003, 10:41 AM
 */

package com.perpetual.management.system;

import com.perpetual.management.notifications.LoggingNotification;
import com.perpetual.management.notifications.SyslogNotification;
import javax.management.Notification;
import javax.management.MBeanInfo;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerNotification;
import javax.management.ObjectName;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.MBeanServer;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.notifications.LoggingNotification;
import com.perpetual.management.snmp.TrapAdaptor;
import com.perpetual.management.snmp.V1TrapAdaptor;
import com.perpetual.management.snmp.V2TrapAdaptor;
import com.perpetual.management.snmp.V3TrapAdaptor;

import com.perpetual.management.PerpetualMBean;
import com.perpetual.util.PerpetualC2Logger;

import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.util.Properties;
import java.util.Date;
import com.perpetual.util.PerpetualPropertiesHandler;
import com.perpetual.util.Environment;


/**
 *
 * @author  brunob
 */
public class PerpetualSyslogToSnmpTrapSenderMBean extends PerpetualMBean 
{
    //private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualSnmpTrapSenderMBean.class );
    private static Logger sLog = Logger.getLogger( PerpetualSnmpTrapSenderMBean.class );
    
    private TrapAdaptor lTrapAdaptor=null;
    private String lDescription="PerpetualSnmpTrapSenderMBean";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [3];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [1];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[0];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [9];
    private int port;
    private String destTrapReceiver;
    private String snmpVersion;
    
    
    /** Creates a new instance of WysdomSNMPTrapSenderAdaptor */
    public PerpetualSyslogToSnmpTrapSenderMBean() throws Exception
    {
        sLog.debug("Constructing PerpetualSnmpTrapSenderMBean");
        lParameterInfoList[0]=new MBeanParameterInfo("pDestHostName","java.lang.String","Trap Receiver IP/DNS Name");
        lParameterInfoList[1]=new MBeanParameterInfo("pPort","java.lang.Integer","Port of trap receiver");
        lParameterInfoList[2]=new MBeanParameterInfo("pTimeStamp","java.util.Date","Syslog TimeStamp");
        lParameterInfoList[3]=new MBeanParameterInfo("pHostName","java.lang.String","Event Host Name");
        lParameterInfoList[4]=new MBeanParameterInfo("pFacility","java.lang.Integer","Facility");
        lParameterInfoList[5]=new MBeanParameterInfo("pSeverity","java.lang.Integer","Severity");
        lParameterInfoList[6]=new MBeanParameterInfo("pPid","java.lang.Integer","PID");
        lParameterInfoList[7]=new MBeanParameterInfo("pProcess","java.lang.String","Process Name");
        lParameterInfoList[8]=new MBeanParameterInfo("pSyslogMessage","java.lang.String","Syslog Message");
        
        lOperationInfoList[0] = new MBeanOperationInfo("sendV1Trap","Sends a V1 SNMP Trap", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lOperationInfoList[1] = new MBeanOperationInfo("sendV2Trap","Sends a V2 SNMP Trap", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        lOperationInfoList[2] = new MBeanOperationInfo("sendV3Trap","Sends a V3 SNMP Trap", 
                                        lParameterInfoList,"void",MBeanOperationInfo.ACTION);
        
        lConstructorInfoList[0]= new MBeanConstructorInfo("WysdomSnmpTrapSenderMBean","SNMP Trap Sender Constructor",null);
        loadSnmpAdaptor();
     }
    
    private void loadSnmpAdaptor() throws Exception
    {
        //determine what type of SNMP version we return dealing with (from a file)
        //and set lTrapAdapter to the right instance
        //-----default is V2-----
        //lTrapAdaptor = new V1TrapAdaptor();
        sLog.debug("Initializing SNMPAdaptor !");
        sLog.debug("Getting SNMP Properties !");
           
        Properties snmpProps = PerpetualPropertiesHandler.getPropertyFile( Environment.getSNMPConfigFile());
        port = new Integer((String)snmpProps.getProperty(ManagementConstants.SnmpTrapDestPort)).intValue();
        destTrapReceiver = (String) snmpProps.getProperty(ManagementConstants.SnmpTrapDestHost);                    
        snmpVersion = (String) snmpProps.getProperty(ManagementConstants.SnmpVersion);  
        if(snmpVersion.equals(ManagementConstants.snmpVersion1))
        {
            sLog.info("Constructing SNMP V1 Adaptor !");
            lTrapAdaptor = new V1TrapAdaptor(true);        
        }
        if(snmpVersion.equals(ManagementConstants.snmpVersion2))  
        {
            sLog.info("Constructing SNMP V2 Adaptor !");
            lTrapAdaptor = new V2TrapAdaptor(true);
        }
        if(snmpVersion.equals(ManagementConstants.snmpVersion3))  
        {
            sLog.info("Constructing SNMP V3 Adaptor !");
            lTrapAdaptor = new V3TrapAdaptor(true);
        }
        
    }
    public void handleNotification(Notification pNotification,Object pHandback)
    {
        sLog.debug("<---Got a notification--->");
        sLog.debug("Notification Type:"+pNotification.getType());
        sLog.debug("Notification Source:"+pNotification.getSource());
        sLog.debug("Message is:"+pNotification.getMessage());
        sLog.debug("Time Stamp:"+pNotification.getTimeStamp());
                
        try
        {
            if (pNotification instanceof LoggingNotification)
            {
                 sLog.debug("This is a Logging Notification");
                 
                    //Need to determine what type of trap v1,v2,v3 to send                      
                 lTrapAdaptor.sendTrap(buildTrapMessage(pNotification));
             }
            if (pNotification instanceof SyslogNotification)
            {
                 sLog.debug("This is a Logging Notification");
                 
                    //Need to determine what type of trap v1,v2,v3 to send                      
                 lTrapAdaptor.sendSyslogTrap(buildSyslogTrapMessage(pNotification));
             }
            else if(pNotification instanceof MBeanServerNotification)
            {
                MBeanServerNotification not = (MBeanServerNotification) pNotification;
                ObjectName name = not.getMBeanName();
                sLog.debug("Bean that Caused notification:"+name.toString());  
            
                 if(name.getKeyProperty("name").equals("LoggerMBean"))
                {
                    //1) get handle to MBeanServer....
                    if(lServer!=null)
                    {
                        sLog.debug("It is a LoggerMBean that registered- registering for future events");
                         //2) register with LoggerMBean for events
                        lServer.addNotificationListener(new ObjectName(ManagementConstants.LoggingObjectName),new ObjectName(ManagementConstants.SnmpTrapSenderObjectName),null,null);
                    }                    
                }
            }
       
        }
        catch(Throwable ex)
        {
            sLog.error("Notification Handling Error",ex);
        }                 
       
    }
    private HashMap buildTrapMessage(Notification pNotification)
    {
        LoggingNotification notification = (LoggingNotification) pNotification;
        sLog.debug("building message from notification");
        
        HashMap result = new HashMap();
        result.put(ManagementConstants.TRAP_DESTINATION,null);
        result.put(ManagementConstants.TRAP_PORT,null);
        result.put(ManagementConstants.TRAP_LEVEL,notification.getLevel());
        result.put(ManagementConstants.TRAP_MESSAGE,notification.getMessage());
        
        return result;
    }
    private HashMap buildSyslogTrapMessage(Notification pNotification)
    {
        SyslogNotification notification = (SyslogNotification) pNotification;
        sLog.debug("building message from notification");
        
        HashMap result = new HashMap();
        result.put(ManagementConstants.TRAP_DESTINATION,notification.getDestHost());
        result.put(ManagementConstants.TRAP_PORT,notification.getDestPort());
        result.put(ManagementConstants.EVENT_TIME,notification.getSyslogTimestamp());
        result.put(ManagementConstants.EVENT_SOURCE,notification.getHostname());
        result.put(ManagementConstants.EVENT_FACILITY,notification.getFacility());
        result.put(ManagementConstants.EVENT_SEVERITY,notification.getSeverity());
        result.put(ManagementConstants.EVENT_PID,notification.getPid());
        result.put(ManagementConstants.EVENT_PROCESS_NAME,notification.getProcess());
        result.put(ManagementConstants.EVENT_MESSAGE,notification.getMessage());
        
        return result;
    }
    private HashMap buildSyslogTrapMessage(String pDestHostName,Integer pPort,Date pTimeStamp, String pHostName,Integer pFacility,
                    Integer pSeverity,Integer pPid,String pProcess,String pMessage)
    {
              
        HashMap result = new HashMap();
        result.put(ManagementConstants.TRAP_DESTINATION,pDestHostName);
        result.put(ManagementConstants.TRAP_PORT,pPort);
        result.put(ManagementConstants.EVENT_TIME,pTimeStamp);
        result.put(ManagementConstants.EVENT_SOURCE,pHostName);
        result.put(ManagementConstants.EVENT_FACILITY,pFacility);
        result.put(ManagementConstants.EVENT_SEVERITY,pSeverity);
        result.put(ManagementConstants.EVENT_PID,pPid);
        result.put(ManagementConstants.EVENT_PROCESS_NAME,pProcess);
        result.put(ManagementConstants.EVENT_MESSAGE,pMessage);
        
        return result;
    }
    private HashMap buildTrapMessage(String pHostName,Integer pPort,Integer pTrapLevel,String pMessage,Level pLevel)
    {
        HashMap result = new HashMap();
        result.put(ManagementConstants.TRAP_DESTINATION,pHostName);
        result.put(ManagementConstants.TRAP_PORT,pPort);
        result.put(ManagementConstants.TRAP_LEVEL,pTrapLevel);
        result.put(ManagementConstants.TRAP_MESSAGE,pMessage);
        result.put(ManagementConstants.LOGGER_LOG_LEVEL,pLevel);
        
        return result;
    }
    public void sendV1Trap(String pDestHostName,Integer pPort, Date pTimeStamp,String pHostName,
                            Integer pFacility,Integer pSeverity,Integer pPid,String pProcess,
                            String pMessage ) throws Exception
    {
        sLog.debug("Sending a V1 SNMP Trap !");
        
        if(lTrapAdaptor instanceof com.perpetual.management.snmp.V1TrapAdaptor)
             lTrapAdaptor.sendSyslogTrap(buildSyslogTrapMessage(pDestHostName,pPort,pTimeStamp,
                                pHostName,pFacility,pSeverity,pPid,pProcess,pMessage));
        else
        {
              throw new Exception("Invalid SNMP Adaptor");
         }
          
    }
    public void sendV2Trap(String pDestHostName,Integer pPort,Date pTimeStamp,String pHostName,Integer pFacility,Integer pSeverity,Integer pPid,String pProcess,
                            String pMessage ) throws Exception
    {
         sLog.debug("Sending a V2 SNMP Trap !");
          if(lTrapAdaptor instanceof com.perpetual.management.snmp.V2TrapAdaptor)       
                lTrapAdaptor.sendSyslogTrap(buildSyslogTrapMessage(pDestHostName,pPort,pTimeStamp,
                                pHostName,pFacility,pSeverity,pPid,pProcess,pMessage));
          else
          {
              throw new Exception("Invalid SNMP Adaptor");
          }
         
    }
    public void sendV3Trap(String pDestHostName,Integer pPort,Date pTimeStamp,String pHostName,
                            Integer pFacility,Integer pSeverity,Integer pPid,String pProcess,
                            String pMessage ) throws Exception
    {
         sLog.debug("Sending a V3 SNMP Trap !");
         if(lTrapAdaptor instanceof com.perpetual.management.snmp.V3TrapAdaptor)          
             lTrapAdaptor.sendSyslogTrap(buildSyslogTrapMessage(pDestHostName,pPort,pTimeStamp,
                                pHostName,pFacility,pSeverity,pPid,pProcess,pMessage));
         else
         {
             throw new Exception("Invalid SNMP Adaptor");
              
         }
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
	else if (operationName.equals("sendV1Trap"))
        {
            try
            {
                sendV1Trap((String)params[0],(Integer)params[1],(Date) params[2],(String)params[3],
                                (Integer)params[4],(Integer)params[5],(Integer)params[6],
                                (String)params[7],(String)params[8]);
            }
            catch(Exception ex)
            {
                sLog.error(ex);
            }
            }
        else if (operationName.equals("sendV2Trap"))
        {
            try
            {
                sendV2Trap((String)params[0],(Integer)params[1],(Date) params[2],(String)params[3],
                                (Integer)params[4],(Integer)params[5],(Integer)params[6],
                                (String)params[7],(String)params[8]);
            }
            catch(Exception ex)
            {
                sLog.error(ex);
            }
        }
        else if (operationName.equals("sendV3Trap"))
        {
            try
            {
                sendV3Trap((String)params[0],(Integer)params[1],(Date) params[2],(String)params[3],
                                (Integer)params[4],(Integer)params[5],(Integer)params[6],
                                (String)params[7],(String)params[8]);
            }
            catch(Exception ex)
            {
                sLog.error(ex);
            }
        }
        else
            // unrecognized operation name:
	    throw new ReflectionException(new NoSuchMethodException(operationName),
					  "Cannot find the operation " + operationName + " in " + lClassName);
	
            return null;
    }
    public void postRegister(Boolean registrationDone)
    {
       /* if (registrationDone.booleanValue()==true)
        {
            sLog.debug("Find LoggerMBean and register with it");
            
        
        try
        {        
         ObjectName loggerObjName = new ObjectName(ManagementConstants.LoggingObjectName);
         ObjectName  snmpTrapObjName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
         lServer.addNotificationListener(loggerObjName,snmpTrapObjName,null,null);
         sLog.debug("Registered with the LoggerMBean");
        }
        catch(Throwable t)
        {
            
            sLog.error("Failed to register with the Logger MBean",t);
            t.printStackTrace();
        }
        }*/
    }
    public ObjectName preRegister(MBeanServer pServer,ObjectName pName) throws java.lang.Exception
    {
        sLog.debug("pre registering the Trap Sender");
        lServer = pServer;
        
        
        return pName;
    }
   
}
