/*
 * ManagementConstants.java
 *
 * Created on March 18, 2003, 12:22 PM
 */

package com.perpetual.management.util;

import java.util.Properties;
//import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Environment;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author  brunob.
 */
public final class ManagementConstants
{
    private static final Logger sLog = Logger.getLogger(ManagementConstants.class);
    
    public ManagementConstants()
    {
        sLog.debug("ManagementConstants.constructor()");
        setMIBFileName();
    }
    
    
    public static String Domain="PerpetualManagementDomain";
    public static String MBeanServerDelegateName = "JMImplementation:type=MBeanServerDelegate";
    public static String AgentObjectName="PerpetualManagementDomain:type=PerpetualManagementAgent";
    public static String SnmpAdaptorObjectName="PerpetualManagementDomain:type=Adaptor,name=PerpetualSnmpAdaptor";
    public static String SnmpTrapSenderObjectName="PerpetualManagementDomain:name=PerpetualSnmpTrapSenderMBean";
    public static String LoggingObjectName="PerpetualManagementDomain:name=PerpetualLoggerMBean";
    public static String SyslogObjectName="PerpetualManagementDomain:name=PerpetualSyslogMBean";
    public static String NotificationRegistratorObjectName="PerpetualManagementDomain:name=PerpetualNotificationRegistratorMBean";
    //public static String SnmpVersion="V2c";
    public static int SnmpPort=8001;
    public static String SnmpTrapDestPort="snmp.trap.host.port";
    public static String SnmpTrapDestHost="snmp.trap.host";
    public static String SnmpVersion="snmp.version";
    public static String snmpVersion1="v1";
    public static String snmpVersion2="v2";
    public static String snmpVersion3="v3";
       
    public static String LoggingNotificationType = "perpetual.notification.logging";
    public static String SyslogNotificationType =  "perpetual.notification.syslog";
    //-------------------------------------------
    //public String DefaultSnmpTargetAddress="nnm";
    public static String DefaultSnmpTargetAddress="localhost";
    public static int    DefaultSnmpTargetPort=162;
    //------------------------------------------
    //public static String MIBFilenameV1 = "../metadata/PerpetualMIB";
    //public String MIBFilenameV2 = "../metadata/WysdomMIBv2";
    public static String MIBFilenameV2=null;
    public static String MIBFilenameV1 = "../metadata/PerpetualMIB";
       
    public static int LoggingTrapType= 666;
    //MIB definitions
    //public String LogLevelOID="iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.loggerMBeanTable.doLogEntry.level";
    //public String LogMessageOID="iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.loggerMBeanTable.doLogEntry.message";
    public static String LogLevelOID="iso.org.dod.internet.private.enterprises.perpetual.logNotification.loggerMBeanTable.doLogEntry.level";
    public static String LogMessageOID="iso.org.dod.internet.private.enterprises.perpetual.logNotification.loggerMBeanTable.doLogEntry.message";
    public static String SyslogTimeOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.time";
    public static String SyslogHostOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.host";
    public static String SyslogSeverityOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.severity";
    public static String SyslogFacilityOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.facility";
    public static String SyslogProcessOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.process";
    public static String SyslogPidOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.pid";
    public static String SyslogMessageOID="iso.org.dod.internet.private.enterprises.perpetual.syslogNotifier.syslogNotifierTable.sendSyslogNotificationEntry.syslogMessage";
    //------------------notifications-----------------------------------------
    //public String errorOID=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.errorNotification";
    public static String errorNotificationName="errorNotification";
    //public String fatalOID=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.fatalNotification";
     public static String fatalNotificationName="fatalNotification";
    //public String warningOID=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.warningNotification";
    public static String warningNotificationName="warningNotification";
    //public String infoOID=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.infoNotification";
    public static String infoNotificationName="infoNotification";
    //public String unknownLevelOID=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.unknownLevel";
    public static String unknownNotificationName="unknownLevel";   
    public static String syslogNotificationName="syslogNotification";
    
    public static String V2EnterpriseOID="org.private.enterprises";
    public static String V1EnterpriseOID=  ".1.3.6.1.4.1.4.1";
    public static String Community="public";
    public static long TimeTicks=1000;
    //----------------Logging Levels-------
    public static int FATAL=4;
    public static int ERROR=3;
    public static int WARNING=2;
    public static int INFO=1;
    public static int UNKNOWN=999;
    public static int LOGGING_TRAP_LEVEL=INFO;
    //--------------Trap values---------
    public static String TRAP_DESTINATION  ="hostname";
    public static String TRAP_MESSAGE      ="message";
    public static String TRAP_PORT         ="port";
    public static String TRAP_LEVEL        ="level";
    public static String LOGGER_LOG_LEVEL  ="loggerLevel"; 
    //----------Syslolg trap Values--------
    public static String EVENT_TIME="SyslogTimestamp";
    public static String EVENT_SOURCE="SyslogSource";
    public static String EVENT_FACILITY="Facility";
    public static String EVENT_SEVERITY="Severity";
    public static String EVENT_PID="SyslogPID";
    public static String EVENT_PROCESS_NAME="ProcessName";
    public static String EVENT_MESSAGE="SyslogMessage";
    
    public static String getMIBFileV2()
    {
        if(MIBFilenameV2==null)
            setMIBFileName();
        
        return MIBFilenameV2;           
            
    }
    private static void setMIBFileName() 
    {
        
        sLog.debug("getting MIB file name !");
           Properties env = new Properties();
           try 
           {
             MIBFilenameV2 = Environment.getMIBFile();
             sLog.debug("MIBFilename="+MIBFilenameV2);
           }
           catch(Exception ex)
           {
               sLog.error("Couldn't find MIB File !");
           }
    }
   
}