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
import java.util.Date;
/**
 *
 * @author  brunob
 */
public class V2TrapFilterSyslog extends TrapFilter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( V2TrapFilterSyslog.class );
    private String lTimeStamp=null;
    private String lHostName=null;
    private Integer lFacility=null;
    private Integer lSeverity=null;
    private Integer lPid=null;
    private String lProcessName=null;
    private String lMessage=null;
       
    /** Creates a new instance of V2TrapFilter */
    public V2TrapFilterSyslog()
   {
       super();
       lParamVars = new String [7];
       lParamOids = new String [7];
        
   }
    public String[] getVarBinds()
    {
        lParamVars[0] = lTimeStamp;
        lParamVars[1] = lHostName;
        lParamVars[2] = lSeverity.toString();
        lParamVars[3] = lFacility.toString();        
        lParamVars[4] = lProcessName;
        lParamVars[5] = lPid.toString();        
        lParamVars[6] = lMessage;
        return lParamVars;
    }
    public String[] getOids()
    {
         sLog.debug("Getting Oids");
         lParamOids[0]=ManagementConstants.SyslogTimeOID;
         lParamOids[1]=ManagementConstants.SyslogHostOID;
         lParamOids[2]=ManagementConstants.SyslogSeverityOID;
         lParamOids[3]=ManagementConstants.SyslogFacilityOID;         
         lParamOids[4]=ManagementConstants.SyslogProcessOID;
         lParamOids[5]=ManagementConstants.SyslogPidOID;
         lParamOids[6]=ManagementConstants.SyslogMessageOID;
          
        return lParamOids;
    }
     public String getTrapOid() throws Exception
    {
        String lTrapOid=null;
        sLog.debug("Getting matching trap OID !");
          lTrapOid = (String) lNotificationList.get(ManagementConstants.syslogNotificationName);
          sLog.debug("TrapOid:"+lTrapOid);           
        return lTrapOid;
     }
     public String getTrapDestination()
     {        
        return lTrapDestination;
     }
     public int getTrapDestinationPort()
     {
              return lTrapPort;
     }
     public void setFilterMap(HashMap pMap)
     {
        sLog.debug("In setFilterMap() !!");
        Object portObj = pMap.get(ManagementConstants.TRAP_PORT);
        Object destObj = pMap.get(ManagementConstants.TRAP_DESTINATION);
        lTimeStamp  =   (String)pMap.get(ManagementConstants.EVENT_TIME);            
        lHostName   = (String)pMap.get(ManagementConstants.EVENT_SOURCE);           
        lFacility  = (Integer) pMap.get(ManagementConstants.EVENT_FACILITY);
        lSeverity  = (Integer) pMap.get(ManagementConstants.EVENT_SEVERITY);
        lPid  = (Integer) pMap.get(ManagementConstants.EVENT_PID);
        lProcessName   = (String)pMap.get(ManagementConstants.EVENT_PROCESS_NAME); 
        lMessage   = (String)pMap.get(ManagementConstants.EVENT_MESSAGE);   
        
        if(portObj==null) //set default value
        {
            sLog.debug("No port specified! - setting the default");
            lTrapPort=ManagementConstants.DefaultSnmpTargetPort;
        }
        else 
        {
            lTrapPort = ((Integer) portObj).intValue();
            sLog.debug("Port indicated:"+lTrapPort);
        }
                 
        if(destObj==null)
        {
              sLog.debug("No trap destination specified - setting the default !");
              lTrapDestination=ManagementConstants.DefaultSnmpTargetAddress;
        }
        else
        {
              lTrapDestination=(String) destObj;
              sLog.debug("Trap Destination:"+ lTrapDestination);
        }
     }
   
    
}
