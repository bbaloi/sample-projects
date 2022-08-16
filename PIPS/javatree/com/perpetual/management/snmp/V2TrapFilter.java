/*
 * V2TrapFilter.java
 *
 * Created on March 31, 2003, 11:11 AM
 */

package com.perpetual.management.snmp;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.management.util.ManagementConstants;

import java.util.HashMap;
import java.util.Vector;
/**
 *
 * @author  brunob
 */
public class V2TrapFilter extends TrapFilter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( V2TrapFilter.class );
    
    /** Creates a new instance of V2TrapFilter */
   public V2TrapFilter()
   {
       super();
       lParamVars = new String [2];
       lParamOids = new String [2];
       
        
   }
   
    
}
