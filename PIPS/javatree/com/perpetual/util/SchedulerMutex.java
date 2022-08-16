/*
 * SchedulerMutex.java
 *
 * Created on November 12, 2003, 9:12 PM
 */

package com.perpetual.util;

/**
 *
 * @author  brunob
 */
public class SchedulerMutex 
{
    private static SchedulerMutex instance=null;
    private boolean lock=false;
    
    /** Creates a new instance of SchedulerMutex */
    private SchedulerMutex() 
    {
    }
    public synchronized void getLock()
    {        
           while(lock); //wait for lock to be released
            //lock has been release
            lock=true;      
            
    }
    public synchronized void releaseLock()
    {
        lock=false;
        
    }
    public static SchedulerMutex getInstance()
    {
        if (instance==null)
                       instance = new SchedulerMutex();              
        return instance;
    }
    
}
