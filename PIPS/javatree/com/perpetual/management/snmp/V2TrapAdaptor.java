/*
 * V2TrapAdaptor.java
 *
 * Created on March 21, 2003, 12:50 PM
 */

package com.perpetual.management.snmp;

import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.usm.*;
import com.adventnet.snmp.mibs.MibModule;
import com.adventnet.snmp.mibs.MibOperations;
import com.adventnet.snmp.mibs.MibNode;

import com.perpetual.util.PerpetualC2Logger;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;
import com.perpetual.management.util.ManagementConstants;

/**
 *
 * @author  brunob
 */
public class V2TrapAdaptor extends TrapAdaptor
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( V2TrapAdaptor.class );
    
    
    /** Creates a new instance of V2TrapAdaptor */
    public V2TrapAdaptor() 
    {
        super();
    }
    public V2TrapAdaptor(boolean pSyslog) 
    {
        super(pSyslog);
    }
    protected void init()
    {
        sLog.debug("SnmpTrapAppender.init() - V2 Trap Adaptor initialisation !");  
        try
        {
            sLog.debug("setting SNMPTrap Handler Address:"+lTargetHost+":"+lTargetPort);
            lTarget.setTargetHost(lTargetHost);
            lTarget.setTargetPort(lTargetPort);
            lTarget.setDebug(true);
            lTarget.setCommunity(ManagementConstants.Community );
            lTarget.setSnmpVersion(lTarget.VERSION2C);
            lTarget.setLoadFromCompiledMibs(true);
            sLog.debug("MIB file:"+ManagementConstants.getMIBFileV2());
            lTarget.loadMibs(ManagementConstants.getMIBFileV2());         
            if(syslogMode)
                lFilter = new V2TrapFilterSyslog();
            else
                lFilter = new V2TrapFilter();
         }
        catch(Exception ex)
        {
            sLog.error("Error in SNMP Trap Adaptor in instantiation");
            ex.printStackTrace();
        }
    }
    public void sendSyslogTrap(HashMap pTrapValues)
    {
       
        sLog.debug("SnmpTrapAdapter.sendSyslogTrap() - Converting Syslog Event into a V2 SNMP Trap !");
        String trapOIDStr=null;        
        //Set the oids for snmpTrapOID and sysUpTime       
            
            try
            {   
                sLog.debug("Setting FilterMap");
                lFilter.setFilterMap(pTrapValues);
                trapOIDStr = lFilter.getTrapOid();  
                sLog.debug("TrapOID selected:"+trapOIDStr);
                                
                if(!trapOIDStr.equals(""))
                {
                     sLog.debug("Sending trap to:"+lFilter.getTrapDestination()+":"+lFilter.getTrapDestinationPort());
                   
                   lTarget.setTargetHost(lFilter.getTrapDestination());
                   lTarget.setTargetPort(lFilter.getTrapDestinationPort());                    
                   String [] paramOids = lFilter.getOids();
                   String []values = lFilter.getVarBinds();
                    //SnmpOID trapOID= new SnmpOID(trapOIDStr);
                    //sLog.debug("trapOid:"+trapOID.toString());

                    //Send the SNMP v2 Trap
                     lTarget.setObjectIDList(paramOids);
                     lTarget.snmpSendNotification(ManagementConstants.TimeTicks,trapOIDStr, values);
                }
                else
                {
                    sLog.debug("No OID available");
                }
         }
        catch(Throwable t)
        {
            sLog.debug("Error in preparing the SNMP Trap");
            t.printStackTrace();
        }  
    }
     public void sendTrap(HashMap pTrapValues)
    {
        sLog.debug("SnmpTrapAdapter.sendTrap() - Sending V2 Trap !");
        String trapOIDStr=null;
        
        //Set the oids for snmpTrapOID and sysUpTime       
            
            try
            {   
                sLog.debug("Setting FilterMap");
                lFilter.setFilterMap(pTrapValues);
                trapOIDStr = lFilter.getTrapOid();  
                sLog.debug("TrapOID selected:"+trapOIDStr);
                                
                if(!trapOIDStr.equals(""))
                {
                     sLog.debug("Sending trap to:"+lFilter.getTrapDestination()+":"+lFilter.getTrapDestinationPort());
                   
                  lTarget.setTargetHost(lFilter.getTrapDestination());
                   lTarget.setTargetPort(lFilter.getTrapDestinationPort());                    
                   String [] paramOids = lFilter.getOids();
                   String []values = lFilter.getVarBinds();
                    //SnmpOID trapOID= new SnmpOID(trapOIDStr);
                    //sLog.debug("trapOid:"+trapOID.toString());

                    //Send the SNMP v2 Trap
                     lTarget.setObjectIDList(paramOids);
                     lTarget.snmpSendNotification(ManagementConstants.TimeTicks,trapOIDStr, values);
                }
                else
                {
                    sLog.debug("No OID available");
                }
         }
        catch(Throwable t)
        {
            sLog.debug("Error in preparing the SNMP Trap");
            t.printStackTrace();
        }
    }
    
}
