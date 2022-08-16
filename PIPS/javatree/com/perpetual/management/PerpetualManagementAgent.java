/*
 * agent.java
 *
 * Created on March 18, 2003, 12:05 PM
 */

package com.perpetual.management;

//import com.perpetual.util.PerpetualC2Logger;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.adaptors.PerpetualSnmpAdaptor;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import com.adventnet.manageengine.configuration.AdaptorConfiguration;

import com.adventnet.manageengine.ruleengine.services.RuleEngineMBean;
import com.adventnet.manageengine.plugins.AgentStartup;

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
import javax.management.Notification;
import javax.management.MBeanInfo;

/**
 *
 * @author  brunob
 */
public class PerpetualManagementAgent extends PerpetualMBean
{
    //private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualManagementAgent.class );
    private static Logger sLog = Logger.getLogger(ManagementConstants.class);
    
    
    private static PerpetualManagementAgent lAgent=null;
    private MBeanServer lMBeanServer=null;
       
    private ObjectName startupMBeanName=null;
    private String lDescription="Perpetual Management Agent";
    private String lMethodDescription="";    
    private String lClassName=null;
    
    private MBeanAttributeInfo [] lAttributeInfoList = new MBeanAttributeInfo [0];
    private MBeanOperationInfo [] lOperationInfoList = new MBeanOperationInfo [0];
    private MBeanConstructorInfo [] lConstructorInfoList = new MBeanConstructorInfo [0];
    private MBeanNotificationInfo [] lNotificationInfoList = new MBeanNotificationInfo[0];
    private MBeanParameterInfo [] lParameterInfoList = new MBeanParameterInfo [0];
    private String [] lNotificationTypes = new String [1];
    /** Creates a new instance of agent */

    public static PerpetualManagementAgent getInstance()
    {
        if(lAgent==null)
            lAgent = new PerpetualManagementAgent();
        
        return lAgent;
        
    }
    public PerpetualManagementAgent() 
    {
        init();
    }
    
    private void init()
    {
        try
        {
            sLog.debug("Initializing PerpetualManagementAgent !");
            initMBeanServer();            
        }
        catch(Exception excp)
        {
        }
            
    }
    
    public MBeanServer getMBeanServer()
    {
        return lMBeanServer;
    }
    
    private void initMBeanServer()
    {
        sLog.debug("Initializing MBean Server !");
        lMBeanServer= MBeanServerFactory.createMBeanServer();
		try 
                {
			startupMBeanName = new ObjectName(ManagementConstants.AgentObjectName);
			lMBeanServer.registerMBean(this, startupMBeanName);
		} 
                catch(Exception e) 
                {
			System.out.println("Exception while registering Agent as an MBean : "+ e);
                        e.printStackTrace();
                }
    }
     public String initialize(String str, java.util.Hashtable hashtable) throws java.lang.Exception 
    {
                sLog.debug("Reading in parameter files !");                
                init();    	
		return "OK";
    }
    
     public void handleNotification(javax.management.Notification notification, Object obj) {
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
