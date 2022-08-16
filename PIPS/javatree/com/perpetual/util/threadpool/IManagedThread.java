/*
 * IManagedThread.java
 *
 * Created on July 18, 2003, 2:12 PM
 */

package com.perpetual.util.threadpool;

/**
 *
 * @author  brunob
 */
public interface IManagedThread extends Runnable
{
    public void  stop();
    
}
