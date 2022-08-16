/*
 * WysdomSNMPAdaptor.java
 *
 * Created on March 18, 2003, 1:10 PM
 */
package com.perpetual.management.adaptors;

import com.adventnet.adaptors.snmp.SnmpAdaptor;
import javax.management.Notification;
import com.adventnet.adaptors.snmp.SnmpTrapHelper;
import com.adventnet.snmp.snmp2.agent.SnmpTrapService;
import javax.management.NotificationListener;
import java.lang.Integer;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import com.adventnet.manageengine.configuration.AdaptorConfiguration;
import com.adventnet.snmp.snmp2.agent.TrapRequestEvent;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.lang.Long;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.management.PerpetualManagementAgent;
import com.perpetual.management.util.ManagementConstants;

/**
 *
 * @author  brunob
 */
public class PerpetualSnmpAdaptor extends SnmpAdaptor  
//public class WysdomSnmpAdaptor extends WysdomMBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PerpetualSnmpAdaptor.class );
    private SnmpTrapService lTrapListener=null; //used to send SNMP traps
    private SnmpTrapHelper lTrapHelper=null;
    private MBeanServer lServer=null;
    private Vector lMBeansList=null;
    private AdaptorConfiguration lAdaptorConfig=null;
    private ObjectName lAdaptorObjectName=null; 
    
    private Hashtable notifTypeTable=null;
    /** Creates a new instance of WysdomSNMPAdaptor */
    public PerpetualSnmpAdaptor(Integer port) 
    {
        super(port);
        lServer=PerpetualManagementAgent.getInstance().getMBeanServer();
        init();
    }
    public PerpetualSnmpAdaptor(Integer port, MBeanServer pServer) 
    {        
        super(port,pServer);
        lServer=pServer;
        init();
    }
    private void init()
    {
        lMBeansList= new Vector();
        lAdaptorConfig = new AdaptorConfiguration(lServer);
        notifTypeTable = new Hashtable();
        try
        {
        lAdaptorObjectName = new ObjectName(ManagementConstants.SnmpAdaptorObjectName);
        }
        catch(javax.management.MalformedObjectNameException ex)
        {
            ex.printStackTrace();
        }
    }
    public AdaptorConfiguration getAdaptorConfiguration()
    {
        return lAdaptorConfig;
    }
    public List getMBeanList()
    {
        return lMBeansList;
    }    
    
/*    public void handleNotification(Notification pNotification,Object pHandback)
    {
        //if notification is of logger type send a logging trap 
        //also need to set up the MIB table and trap forwarding table with the startup 
        //and shutdown of component
        
        
        sLog.debug("In WysdomSnmpAdaptor - notification Handler");
        System.out.println("Notification type:"+pNotification.getType());
        System.out.println(pNotification);
        if(pNotification.getType().equals("JMX.mbean.registered"))
        {
               System.out.println("This is a bean registration notification");
               //find out if it is a Logger bean and invoke it and register with it for further notifications
               //determine the type of bean and add to notifyTable
        
        }
        sendSNMPTrap(null,"my fuking trap");
              
    }*/
    public void instantiateTrapRequestListener(SnmpTrapService pService)
    {
        lTrapListener = pService;
        this.addTrapRequestListener(lTrapListener);
    }
    private void sendSNMPTrap(Vector varbindVector, String type) 
	{
		String oid = (String)notifTypeTable.get(type);
		if(oid == null) {
			oid = (String)notifTypeTable.get("DefaultNotification");
		}
		int dot = oid.lastIndexOf(".");
		if(dot == -1) {
			return;
		}
		String trapoid = oid.substring(0, dot);
		int trapindex = Integer.parseInt(oid.substring(dot+1));
		TrapRequestEvent te = new TrapRequestEvent(this, varbindVector,
						TrapRequestEvent.TFTABLE, 
						trapoid, trapindex);
		try {
			Long upTime = (Long)(lServer.invoke(lAdaptorObjectName,"getUpTime", new Object[0], new String[0]));
			te.setTimeTicks(upTime.longValue());
		} catch (Exception ee) {
		}
		if(lTrapListener != null) 
                {
			lTrapListener.sendTrap(te);
		}
	}
}
