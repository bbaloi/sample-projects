/*
 * MBeanInstantiator.java
 *
 * Created on March 18, 2003, 1:43 PM
 */

package com.perpetual.management;

import com.perpetual.management.system.PerpetualLoggerMBean;
import javax.management.MBeanServer;
import com.perpetual.util.PerpetualC2Logger;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.adaptors.PerpetualSnmpAdaptor;
import com.perpetual.management.system.PerpetualSnmpTrapSenderMBean;
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
import com.perpetual.util.PerpetualPropertiesHandler;
/**
 *
 * @author  brunob
 */
public class MBeanLoader 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( MBeanLoader.class );
    private MBeanServer lServer=null;
    private PerpetualLoggerMBean lLogger = null;
    private PerpetualSnmpTrapSenderMBean lSnmpTrapSender=null;   
    private PerpetualNotificationRegistratorMBean lNotificationRegistrator=null;
    private PerpetualSnmpAdaptor lSnmpAdaptor=null;
    private Vector mbeansList = new Vector();
    
       
    /** Creates a new instance of MBeanInstantiator */
    public MBeanLoader(MBeanServer pServer) 
    {
        lServer=pServer;
        mbeansList = new Vector();
        initMBeans();
    }
    private void initMBeans()
    {
        //have a hashtable with MBean built from an XML descriptor a la AdventNet
       lLogger = new PerpetualLoggerMBean();   
          mbeansList.add(lLogger);
       lSnmpTrapSender = new PerpetualSnmpTrapSenderMBean();
          mbeansList.add(lSnmpTrapSender);
       lNotificationRegistrator = new PerpetualNotificationRegistratorMBean();
            mbeansList.add(lNotificationRegistrator);
       
    }
    public void loadMBeans()
    {
        sLog.debug("---loading MBeans---");
        loadAdaptors();
        //registerMBeans with MBeanServer
        loadRulesEngine();
        loadApplicationMBeans();
    }
    private void loadAdaptors()
    {
        //loadSNMPAdaptor();
    }
    private void loadSNMPAdaptor() throws Exception
    {
        sLog.debug("Initializing SNMPAdaptor !");
        Properties snmpProps = PerpetualPropertiesHandler.getPropertyFile( ManagementConstants.getMIBFileV2());
        int port = new Integer((String)snmpProps.getProperty(ManagementConstants.SnmpTrapDestPort)).intValue();
        String hostName = (String) snmpProps.getProperty(ManagementConstants.SnmpTrapDestHost);                    
        String version = (String) snmpProps.getProperty(ManagementConstants.SnmpVersion);  
       
	String objectName = ManagementConstants.SnmpAdaptorObjectName;

        //or we could get objectName, version and port from a XML config file
        /*
		if (snmpConf != null) {
			String portStr = (String)snmpConf.get("port");
			try {
				port = Integer.parseInt(portStr);
			} catch(NumberFormatException nfe) {
			}
			if (port < 0 || port > 65535) {
			port = 8001;
			}

			version = (String)snmpConf.get("version");
			if (version == null) {
			version = "V2c";
			}

			String objectNameStr = (String)snmpConf.get("objectName");
			if (objectNameStr != null) {
				objectName = objectNameStr;
			}
		}
         **/

		try {
			ObjectName snmpAdaptorObjName = new ObjectName(objectName);
			Set mySet = lServer.queryMBeans(snmpAdaptorObjName, null);
			if(mySet.isEmpty()) {
                            SnmpTrapService trapListener = new SnmpTrapService();
                                
                                PerpetualSnmpAdaptor lSnmpAadaptor= new PerpetualSnmpAdaptor(new Integer(port));
                                mbeansList.add(lSnmpAdaptor); 
                                AdaptorConfiguration adapConf = lSnmpAdaptor.getAdaptorConfiguration();
                                
				
				if (version.equalsIgnoreCase("v3")) {
					adapConf.configureV3ForwardingTable(trapListener, true, true, mbeansList);
					trapListener.setVersion(SnmpAPI.SNMP_VERSION_3);
				}
				else {
					adapConf.configureForwardingTable(trapListener, true, true, mbeansList);
				}

				//lSnmpAdaptor.setSnmpDebugLevel(new Integer(Level.FATAL));
                                lSnmpAdaptor.setSnmpVersion(version, false);
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
    private void loadRulesEngine()
    {
        sLog.debug("Initializing the Rules Engine !");
    }
    private void loadApplicationMBeans()
    {        
        try 
        {   
            
          /*sLog.debug("Registering LoggerMBean");
          ObjectName loggerObjName = new ObjectName(ManagementConstants.LoggingObjectName);
          lServer.registerMBean(lLogger,loggerObjName);                  
          sLog.debug("Registering SnmpTrapSenderMBean");
          ObjectName  snmpTrapObjName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
          //lServer.createMBean("com.wysdom.management.system.WysddomSnmpTrapSenderMBean",snmpTrapObjName);
          lServer.registerMBean(lSnmpTrapSender,snmpTrapObjName);
          ObjectName serverNotificationName = new ObjectName(ManagementConstants.MBeanServerDelegate);
          lServer.addNotificationListener(serverNotificationName,lSnmpTrapSender,null,null);
          lServer.addNotificationListener(loggerObjName,lSnmpTrapSender,null,null);*/
            
            
           ObjectName  loggerObjName = new ObjectName(ManagementConstants.LoggingObjectName);
           ObjectName  snmpTrapObjName = new ObjectName(ManagementConstants.SnmpTrapSenderObjectName);
           ObjectName  registratorObjName = new ObjectName(ManagementConstants.NotificationRegistratorObjectName);
                   
          lServer.registerMBean(lLogger,loggerObjName);           
          lServer.registerMBean(lSnmpTrapSender,snmpTrapObjName);
          lServer.registerMBean(lNotificationRegistrator,registratorObjName);        
         
         
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
  }
}
