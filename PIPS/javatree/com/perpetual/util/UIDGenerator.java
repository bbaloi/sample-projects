/*
 * UIDGenerator.java
 *
 * Created on November 14, 2003, 2:54 PM
 */

package com.perpetual.util;

/**
 *
 * @author  brunob
 */
public final class UIDGenerator 
{
    private static double uid=0;
    private static UIDGenerator instance=null;
    
    /** Creates a new instance of UIDGenerator */
    private UIDGenerator() 
    {
    }
    public static UIDGenerator getInstance()
    {
        if(instance==null)
            instance=new UIDGenerator();
        return instance;
    }
    public double getUID()
    {
        return uid++;
    }
}
