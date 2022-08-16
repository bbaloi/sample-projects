/*
 * Mutex.java
 *
 * Created on November 11, 2003, 4:27 PM
 */

package com.perpetual.util;

/**
 *
 * @author  brunob
 */
public final class Mutex
{    
    private static Mutex instance=null;
    private boolean lock=false;
    
    /** Creates a new instance of Mutex */
    private Mutex() 
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
    public static Mutex getInstance()
    {
        if (instance==null)
                       instance = new Mutex();              
        return instance;
    }
    
}