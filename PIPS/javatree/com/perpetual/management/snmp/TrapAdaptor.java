/*
 * TrapAdaptor.java
 *
 * Created on March 21, 2003, 12:49 PM
 */

package com.perpetual.management.snmp;

import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.usm.*;

import com.perpetual.management.util.ManagementConstants;
import com.perpetual.util.PerpetualC2Logger;
import java.util.HashMap;

/**
 *
 * @author  brunob
 */
public abstract class TrapAdaptor 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( TrapAdaptor.class );
    protected SnmpTarget lTarget=null; 
    protected String     lTargetHost=null;
    protected int        lTargetPort;
    protected TrapFilter lFilter=null;
    protected boolean syslogMode=false;
    /** Creates a new instance of TrapAdaptor */
    public TrapAdaptor() 
    {
        lTarget = new SnmpTarget();
        lTargetHost = ManagementConstants.DefaultSnmpTargetAddress;
        lTargetPort = ManagementConstants.DefaultSnmpTargetPort;   
        //lFilter = new TrapFilter();
        init();
        //load up some file conatining settings
       
    }
    public TrapAdaptor(boolean pSyslog) 
    {
        lTarget = new SnmpTarget();
        lTargetHost = ManagementConstants.DefaultSnmpTargetAddress;
        lTargetPort = ManagementConstants.DefaultSnmpTargetPort;   
        syslogMode = pSyslog;
        //lFilter = new TrapFilter();
        init();
        //load up some file conatining settings
       
    }
    
    public void sendTrap(HashMap pTrapValues)
    {
       
    }
    public void sendSyslogTrap(HashMap pTrapValues)
    {
       
    }
    protected void init()
    {
        sLog.debug("Base Trap Adaptor initialisation !");        
        lTarget.setTargetHost(lTargetHost);
        lTarget.setTargetPort(lTargetPort);
        lTarget.setLoadFromCompiledMibs(true);
        lTarget.setDebug(true);
        lTarget.setCommunity(ManagementConstants.Community);
       	
    }
}
