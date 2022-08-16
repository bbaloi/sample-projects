/*
 * LogServer.java
 *
 * Created on November 9, 2003, 7:05 PM
 */

package com.perpetual.logserver;
import java.util.Vector;
import java.util.ArrayList;
/**
 *
 * @author  brunob
 */

/*
 *This is the remote server that contains:
 *
 *-LogSystem which has a pool of DiskLogDatabses.
 *It is the remote server that presents an executable interface i.e.
 *
 *
 *
 *
 */
public final class LogServer implements ILogQuery
{
    private ArrayList lDiskLogDBPool=null;
    private int lPoolSize = 3;
    /** Creates a new instance of LogServer */
    public LogServer() 
    {
        init();
    }
     public LogServer(int pPoolSize) 
    {
        lPoolSize = pPoolSize;
        init();
    }
    private void init()
    {
        lDiskLogDBPool = new ArrayList(lPoolSize);
       // for(int i=0;i<lPoolSize;i++)
         //   lDiskLogDBPool.add(new DiskLogDatabase());
        
    }
    public synchronized Cursor retrieveLogRecords(LogFilter logFilter) throws Exception
    {
        return null;
        
    }
    
    
}
