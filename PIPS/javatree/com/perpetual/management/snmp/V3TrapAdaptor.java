/*
 * V3TrapAdaptor.java
 *
 * Created on March 21, 2003, 12:51 PM
 */

package com.perpetual.management.snmp;

import com.perpetual.util.PerpetualC2Logger;
import java.util.HashMap;



/**
 *
 * @author  brunob
 */
public class V3TrapAdaptor extends TrapAdaptor
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( V3TrapAdaptor.class );
    
    /** Creates a new instance of V3TrapAdaptor */
    public V3TrapAdaptor() 
    {
        super();
    }
    public V3TrapAdaptor(boolean pSyslog)
    {
        super(pSyslog);
    }
    public void sendTrap(HashMap pTrapValues)
    {
        sLog.debug("Sending a V3 trap");
    }
    public void sendSyslogTrap(HashMap pTrapValues)
    {
       
    }
   
}
