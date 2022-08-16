/*
 * V1TrapAdaptor.java
 *
 * Created on March 21, 2003, 12:50 PM
 */

package com.perpetual.management.snmp;

import com.adventnet.snmp.mibs.MibModule;
import com.adventnet.snmp.mibs.MibOperations;
import com.adventnet.snmp.mibs.MibNode;
import com.adventnet.snmp.mibs.AgentCapabilities;
import com.adventnet.snmp.mibs.LeafSyntax;
import com.adventnet.snmp.beans.*;
import com.adventnet.snmp.snmp2.SnmpException;
import com.adventnet.snmp.snmp2.SnmpOID;
import com.adventnet.snmp.snmp2.usm.*;
import java.util.Vector;
import java.util.HashMap;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.management.util.ManagementConstants;

import java.util.Enumeration;


/**
 *
 * @author  brunob
 */
public class V1TrapAdaptor extends TrapAdaptor
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( V1TrapAdaptor.class );
    /** Creates a new instance of V1TrapAdaptor */
    public V1TrapAdaptor() 
    {
        super();
    }
    public V1TrapAdaptor(boolean pSyslog) 
    {
        super(pSyslog);
    }
    /*private void init()
    {
        lTargetHost = ManagementConstants.DefaultSnmpTarget;
        lTargetPort = ManagementConstants.DefaultSnmpTargetPort;
        
    }*/
    public void sendSyslogTrap(HashMap pTrapValues)
    {
       
    }
    public void sendTrap(HashMap pTrapValues)
    {
        sLog.debug("Sending V1 Trap !!!");
        // target.setLoadFromCompiledMibs(true);	
        int generic_type=-1, specific_type=ManagementConstants.LoggingTrapType;         
        long time=10000;
                 
         /*try 
         {  
            sLog.debug("Loading MIBs: ");
            //load MIB and set the variables for the OIDS
            MibOperations mo = new MibOperations();
            mo.loadMibModules(ManagementConstants.MIBFilenameV1);
            //mo.loadMibModules(ManagementConstants.MIBFilenameV2);
             Enumeration enum= mo.getMibModules();
             
             int i=0;
             while(enum.hasMoreElements() )
             {
                 i++;
                MibModule module = (MibModule) enum.nextElement();
                String moduleName = module.getName();
                sLog.debug("Module Name:"+moduleName);
                MibNode rootNode = module.getRootNode();
                sLog.debug("Root Node:"+rootNode);
                Enumeration capabilities = module.getDefinedAgentCapabilities();
                sLog.debug("Got capabilities list");
                                         
                int cap=0;
                while(capabilities.hasMoreElements() )
                {
                    cap++;
                    sLog.debug("Capability:"+cap);
                    AgentCapabilities agp = (AgentCapabilities) capabilities.nextElement();
                }
                               
                sLog.debug("Sifting through the nodes in the module"); 
                //recurse through tree
                MibNode tmpNode = rootNode;
                int nodeCtr=0;
                while(true)
                {
                    nodeCtr++;
                    //determineif last node
                    MibNode node = tmpNode.getNextLeafNode();
                    if(node==null)      
                    {
                        sLog.debug("Last Node !");
                        //get the objects
                        break;
                    }
                    else
                    {
                        sLog.debug("Node:"+nodeCtr);
                        String name = node.toString();
                        sLog.debug("Node Name:"+name);
                        String oid = node.getOIDString();
                        sLog.debug("Node Oid:"+oid);
                        LeafSyntax lt = module.getLeafSyntax(new SnmpOID(oid));
                        String description = node.getDescription();
                        sLog.debug("Node Desc:"+description);
                        if(node.isLeaf())
                           sLog.debug("This is a Leaf Node:");
                    
                        Vector objects = node.getObjects();
                        int objCtr;
                         for(objCtr=0;objCtr<objects.size();objCtr++)
                        sLog.debug("Obj:"+(String)objects.elementAt(objCtr));
                  }
                }//end while           
                                         
             }//end bigger while
           
         } //end try
         catch (Exception ex)
         {
            System.err.println("Error loading MIBs: "+ex);
            System.exit(1);
         }
            */
        
        try
        {   sLog.debug("Loading Mibs into targetObject");
            lTarget.loadMibs(ManagementConstants.MIBFilenameV1);
            sLog.debug("MIB Loaded");
            SnmpOID [] oidList = lTarget.getSnmpOIDList();
            if(oidList ==null)
                sLog.error("Null OID List");
            else{
                    sLog.debug("# oids:"+oidList.length);
                    for(int x=0;x<oidList.length;x++)
                    {
                        sLog.debug("OID:"+x+"-"+oidList[x]);
                    }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Error loading MIBs: "+ex);
            System.exit(1);
        }
    // Put together OID and variable value lists from command line
    String oids[] = null, var_values[] = null;  // trap oids and values

    int num_varbinds = 2;
    /*for (int i=6;i<opt.remArgs.length;i+=2) { // add Variable Bindings
        if (opt.remArgs.length < i+2) //need "{OID type value}"
        opt.usage_error(); 
        num_varbinds++;
    }*/
    sLog.debug("setting varbinds");
    oids = new String[num_varbinds];
    var_values = new String[num_varbinds];
    oids[0]=ManagementConstants.LogLevelOID;
    oids[1]=ManagementConstants.LogMessageOID;
    var_values[0]=(String) pTrapValues.get("level");
    var_values[1]=(String) pTrapValues.get("message");
    /*for (int i=0;i<num_varbinds;i++) { // add Variable Bindings
        oids[i] = opt.remArgs[(2*i)+6];
        var_values[i] = opt.remArgs[(2*i)+7];
     }*/

    try {  // use SnmpTarget methods to send trap w/ specified OIDs/values
        lTarget.setObjectIDList(oids);
        lTarget.snmpSendTrap(ManagementConstants.V1EnterpriseOID,
                            ManagementConstants.DefaultSnmpTargetAddress,generic_type,
                            specific_type, time, var_values);

        // allow time to send trap before exiting
        //Thread.sleep(500);

         } catch (Exception e) {
        System.err.println("Error Sending Trap: "+e.getMessage());
         }         
      
    }
    
}
