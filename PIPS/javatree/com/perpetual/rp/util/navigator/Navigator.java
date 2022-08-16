/*
 * Navigator.java
 *
 * Created on July 17, 2003, 8:48 PM
 */

package com.perpetual.rp.util.navigator;

import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogFilter;
import com.perpetual.logserver.LogDatabase;
import java.util.Collection;
import com.perpetual.sherlock.gui.beans.Boss;
import java.util.Iterator;
import java.util.Vector;
//import com.perpetual.log.LogSystem;
import com.perpetual.logserver.LogSystem;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class Navigator implements INavigator
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( Navigator.class );    
    
    private static Navigator lInstance=null;
    private LogDatabase lLogstore=null;
    private LogSystem lLogSystem = null;
    private Boss lBoss=null;
    
    //====== I COULD be running each Logstore in its own Thread ???======
    //-----or have background thread to reload the Logstore---
    
    /** Creates a new instance of Navigator */
    private Navigator() 
    {
        try
        {
            //lBoss = new Boss();
            //lLogstore = lBoss.getLogDatabase();
             lLogSystem = LogSystem.getDefault();
             lLogstore = lLogSystem.getLogDatabase();
        }
        catch(Exception excp)
        {
            excp.printStackTrace();
            String msg="Could not instantiate the Boss !";
            sLog.error(msg);
            BasePerpetualException.handle(msg,excp);
            
        }
     }
    public static INavigator getInstance()
    {
        if(lInstance==null)
            lInstance = new Navigator();
        return lInstance;
    }
    public synchronized Cursor retrieveLogRecords(LogFilter logFilter) throws Exception 
    {
        return lLogstore.retrieveLogRecords(logFilter);
    }
     
   /* public synchronized Collection getHosts() throws Exception 
    {
        Vector hostList = new Vector();
        Iterator it = lLogstore.iterateHosts();
        while(it.hasNext())
        {
            String hostName = (String) it.next();
            hostList.add(hostName);
        }
        return hostList;
    }*/
    
    
}
