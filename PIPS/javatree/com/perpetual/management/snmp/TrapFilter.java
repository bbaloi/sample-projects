/*
 * TrapFilter.java
 *
 * Created on March 31, 2003, 10:58 AM
 */

package com.perpetual.management.snmp;

import com.perpetual.util.PerpetualC2Logger;

import com.adventnet.snmp.mibs.MibModule;
import com.adventnet.snmp.mibs.MibOperations;
import com.adventnet.snmp.mibs.MibNode;

import java.util.HashMap;
import java.util.Enumeration;
import java.util.Vector;
import com.perpetual.management.util.ManagementConstants;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author  brunob
 */
public abstract class TrapFilter 
//public class TrapFilter 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( TrapFilter.class );
    protected String [] lParamVars;
    protected String [] lParamOids;
    protected int lTrapPort=0;
    protected String lTrapDestination=null;
    protected String lTrapOid=null;
    protected HashMap lNotificationList=null;
    protected int lTrapLevel=0;
    protected String lTrapMessage=null;
    protected Level lLoggerLevel=null;
       
    /** Creates a new instance of TrapFilter */
    public TrapFilter()
    {
        sLog.debug("Instantiating TrapFilter !");
        //lParamVars = new String [2];
        //lParamOids = new String [2];
        
        lNotificationList = new HashMap();
        init();
    }
    private void init()
    {
        sLog.debug("Loading the MIB Tree in memory");
        
        String notificationName=null;
        String notificationOid=null;
        try
        {
            MibOperations mo = new MibOperations();
            mo.loadMibModules(ManagementConstants.getMIBFileV2());
            Enumeration modules= mo.getMibModules();
            MibModule root = (MibModule) modules.nextElement();
            sLog.debug(" Main Root node:"+root.toString());
            Enumeration notifications = root.getDefinedNotifications();
            /*Vector nodes=root.getRootNodes();
            for(int i=0;i<nodes.size();i++)
            {
             MibNode node = (MibNode)nodes.elementAt(i);
                sLog.debug("RootNode:"+i+"-"+node.toString());
                 sLog.debug("Node OID:"+node.getOIDString());
            }*/
            
            sLog.debug("Constructing Trap Table");                     
            while(notifications.hasMoreElements())
            {
                MibNode notification=(MibNode)notifications.nextElement();
                notificationName = notification.toString();
                notificationOid=notification.getOIDString();
                sLog.debug("Notification Name:"+notificationName);
                sLog.debug("Notification OID:"+notificationOid);                   
                lNotificationList.put(notificationName,notificationOid);
            }
        }
        catch(Throwable t)
        {
            sLog.error("Error in loading MIB Tree",t);
        }
    }
    public String[] getVarBinds()
    {
        lParamVars[0] = new Integer(lTrapLevel).toString();
        lParamVars[1] = lTrapMessage;           
        return lParamVars;
    }
    public String[] getOids()
    {
         sLog.debug("Getting Oids");
        //lParamOids[0]=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.loggerMBeanTable.doLogEntry.level";
        //lParamOids[1]=".iso.org.dod.internet.private.enterprises.moduleIdentity.system.logging.loggerMBeanTable.doLogEntry.message";
          lParamOids[0]=ManagementConstants.LogLevelOID;
          lParamOids[1]=ManagementConstants.LogMessageOID;
          
        return lParamOids;
    }
    public String getTrapOid() throws Exception
    {
          sLog.debug("Getting matching trap OID !");
          
        //determine upper and lower limits of comaprison
          Level logLevel = Level.toLevel(lTrapLevel);
          
          // if(lTrapLevel < ManagementConstants.INFO || lTrapLevel> ManagementConstants.FATAL)
         if(logLevel.toInt() < Level.INFO.toInt() || logLevel.toInt() > Level.FATAL.toInt())
            throw new Exception("Invalid Trap Level - Cannot send out SNMP trap !");
            
        //if(lTrapLevel>=ManagementConstants.LOGGING_TRAP_LEVEL)
          if(logLevel.isGreaterOrEqual(lLoggerLevel))
        {
            sLog.debug("Trap Level >= than system log level - sending trap!");
            
            if(logLevel.equals(Level.INFO))
                lTrapOid = (String) lNotificationList.get(ManagementConstants.infoNotificationName);
                 //lTrapOid=ManagementConstants.infoOID;
            else if(logLevel.equals(Level.ERROR))
                lTrapOid = (String) lNotificationList.get(ManagementConstants.errorNotificationName);
                   //lTrapOid=ManagementConstants.errorOID;
            else if(logLevel.equals(Level.WARN))
                lTrapOid = (String) lNotificationList.get(ManagementConstants.warningNotificationName);
                 //lTrapOid=ManagementConstants.warningOID;
            else if(logLevel.equals(Level.FATAL))
                lTrapOid = (String) lNotificationList.get(ManagementConstants.fatalNotificationName);
                 //lTrapOid=ManagementConstants.fatalOID;
            else 
                lTrapOid = (String) lNotificationList.get(ManagementConstants.unknownNotificationName);
                 //lTrapOid=ManagementConstants.unknownLevelOID;            
        }
        else if(logLevel.toInt()<lLoggerLevel.toInt())
        {
            sLog.debug("trap level < than system log level - not sending trap");
            lTrapOid = new String("");
        }
                      
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
        Object levelObj = pMap.get(ManagementConstants.TRAP_LEVEL);            
        Object msgObj   = pMap.get(ManagementConstants.TRAP_MESSAGE);           
        Object loggerLevelObj = pMap.get(ManagementConstants.LOGGER_LOG_LEVEL);
        
        
        if(loggerLevelObj==null) //set default value
        {
            sLog.debug("No logger level indicated! - setting the INFO default");
            lLoggerLevel=Level.INFO;
        }
        else 
        {
            lLoggerLevel = (Level) loggerLevelObj;
            sLog.debug("Logger Level indicated:"+lLoggerLevel.toString());
        }
        
        
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
        
         if(levelObj==null)
         {
             sLog.debug("No trap level specified - setting to 'unknown'");
             lTrapLevel = ManagementConstants.UNKNOWN;
         }
         else
         {
            lTrapLevel=((Integer)levelObj).intValue();
            sLog.debug("Trap level:"+lTrapLevel);
         }        
        if(msgObj==null)
        {
            sLog.debug("No message to send - creating a blank string !");
            lTrapMessage= new String("");
        }
        else 
        {
            lTrapMessage = (String) msgObj;
            sLog.debug("Trap Message:"+lTrapMessage);
        }
     }
    
}
