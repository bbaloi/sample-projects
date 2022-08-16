/*
 * MBeanInstantiator.java
 *
 * Created on March 18, 2003, 1:43 PM
 */

package com.perpetual.application.collector.management;

import com.perpetual.management.system.PerpetualLoggerMBean;
import com.perpetual.management.system.PerpetualSyslogMBean;
import javax.management.MBeanServer;
import com.perpetual.util.PerpetualC2Logger;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.adaptors.PerpetualSnmpAdaptor;
import com.perpetual.management.adaptors.PerpetualSyslogSnmpAdaptor;
import com.perpetual.management.system.PerpetualSyslogToSnmpTrapSenderMBean;
import com.perpetual.management.system.PerpetualNotificationRegistratorMBean;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServer;
import com.adventnet.manageengine.configuration.AdaptorConfiguration;
import com.adventnet.manageengine.ruleengine.services.RuleEngineMBean;
import javax.management.ObjectName;
import com.adventnet.utils.agent.DynamicRegistration;
import com.adventnet.snmp.snmp2.agent.SnmpTrapService;
import com.adventnet.snmp.snmp2.SnmpAPI;
import java.util.Set;
import java.util.Vector;
import java.util.Properties;
import java.lang.Integer;

import org.apache.log4j.Logger;
import com.perpetual.util.PerpetualPropertiesHandler;
/**
 *
 * @author  brunob
 */
public class ControllerMBeanLoader 
{
    private static final Logger LOGGER = Logger.getLogger(ControllerMBeanLoader.class);
    private MBeanServer lServer=null;
    private PerpetualSyslogMBean lSyslogSender = null;
    private PerpetualSyslogToSnmpTrapSenderMBean lSnmpTrapSender=null;   
    private PerpetualNotificationRegistratorMBean lNotificationRegistrator=null;
    private PerpetualSyslogSnmpAdaptor lSnmpAdaptor=null;
    private Vector mbeansList = new Vector();
    private int port;
    private String hostName=null;
    private String snmpVersion=null;
    private PerpetualSyslogSnmpAdaptor lSnmpAadaptor=null;
       
    /** Creates a new instance of MBeanInstantiator */
    public ControllerMBeanLoader(MBeanServer pServer) throws Exception
    {
        lServer=pServer;
        mbeansList = new Vector();
        initMBeans();
    }
    private void initMBeans() throws Exception
    {
        //have a hashtable with MBean built from an XML descriptor a la AdventNet
       lSyslogSender = new PerpetualSyslogMBean();   
          mbeansList.add(lSyslogSender);
       lSnmpTrapSender = new PerpetualSyslogToSnmpTrapSenderMBean();
          mbeansList.add(lSnmpTrapSender);
       lNotificationRegistrator = new PerpetualNotificationRegistratorMBean();
            mbeansList.add(lNotificationRegistrator);
       
    }
    public void loadMBeans() throws Exception
    {
        LOGGER.debug("---loading MBeans---");
        //registerMBeans with MBeanServer
        loadApplicationMBeans();
        //loadSNMPAdaptor();
    }
    private void loadSNMPAdaptor() throws Exception
    {
        LOGGER.debug("Initializing SNMPAdaptor !");
        LOGGER.debug("Getting SNMP Properties !");
           
        Properties snmpProps = PerpetualPropertiesHandler.getPropertyFile( ManagementConstants.getMIBFileV2());
        port = new Integer((String)snmpProps.getProperty(ManagementConstants.SnmpTrapDestPort)).intValue();
        hostName = (String) snmpProps.getProperty(ManagementConstants.SnmpTrapDestHost);                    
        snmpVersion = (String) snmpProps.getProperty(ManagementConstants.SnmpVersion);  
	String objectName = ManagementConstants.SnmpAdaptorObjectName;

        try {
                ObjectName snmpAdaptorObjName = new ObjectName(objectName);
                Set mySet = lServer.queryMBeans(snmpAdaptorObjName, null);
                if(mySet.isEmpty()) {
                    SnmpTrapService trapListener = new SnmpTrapService();

                        lSnmpAadaptor= new PerpetualSyslogSnmpAdaptor(new Integer(port));
                        mbeansList.add(lSnmpAdaptor); 
                        AdaptorConfiguration adapConf = lSnmpAdaptor.getAdaptorConfiguration();


                        if (snmpVersion.equalsIgnoreCase("v3")) {
                                adapConf.configureV3ForwardingTable(trapListener, true, true, mbeansList);
                                trapListener.setVersion(SnmpAPI.SNMP_VERSION_3);
                        }
                        else {
                                adapConf.configureForwardingTable(trapListener, true, true, mbeansList);
                        }

                        //lSnmpAdaptor.setSnmpDebugLevel(new Integer(Level.FATAL));
                        lSnmpAdaptor.setSnmpVersion(snmpVersion, false);
                        lSnmpAdaptor.instantiateTrapRequestListener(trapListener);
                         //?????????wat the hellis a TrapHandler??????                                
                        lSnmpAdaptor.setTrapHandler(lSnmpAdaptor);
                        lSnmpAdaptor.setTrapSourceDefault(false);
                        adapConf.enableTraps();

                        lServer.registerMBean(lSnmpAdaptor, snmpAdaptorObjName);
                        lServer.addNotificationListener(snmpAdaptorObjName,lSnmpAdaptor,null,null);
                        //mbeansList.add(new ObjectName(ManagementConstants.SnmpAdapterObjectName));

                        DynamicRegistration dyn = new DynamicRegistration(false, "conf", "ProxyTable.xml");
                        dyn.addRegistrationListener(lSnmpAdaptor, true);
                        dyn.checkSubAgentHeartBeat(true, new Long(60000), null, null, lSnmpAdaptor.getSnmpAgent());

                        //adapConf.configureAclTable(snmpadaptor, true, true, mbeansList);


                }
                //objectNamesTable.put("SNMPAdaptor", snmpAdaptorObjName);
                //mbeansList.add(snmpAdaptorObjName);


        } catch(Exception e) {
                System.out.println("Exception while initializing SNMP Adaptor : "+ e);
        }

    }
   
    private void loadApplicationMBeans()
    {        
        try 
        {               
          
           ObjectName  loggerObjName = new ObjectName(ManagementConstants.SyslogObjectName);
           ObjectName  snmpTrapObjName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
           ObjectName  registratorObjName = new ObjectName(ManagementConstants.NotificationRegistratorObjectName);
                   
          lServer.registerMBean(lSyslogSender,loggerObjName);           
          lServer.registerMBean(lSnmpTrapSender,snmpTrapObjName);
          lServer.registerMBean(lNotificationRegistrator,registratorObjName);     
          
          LOGGER.info("Registering SyslogMBean for notification with the SNMPTrapSenderMBean");
          Object [] nameParams = new Object[2];
          nameParams[0]=new String("PerpetualSyslogMBean");
          nameParams[1]=new String("PerpetualSnmpTrapSenderMBean");
          lServer.invoke(registratorObjName,"registerForNotification",nameParams,null);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
  }
}
