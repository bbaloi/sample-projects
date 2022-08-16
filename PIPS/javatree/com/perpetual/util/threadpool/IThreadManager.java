/*
 * IThreadManager.java
 *
 * Created on July 18, 2003, 1:42 PM
 */

package com.perpetual.util.threadpool;

import java.lang.Runnable;
import com.perpetual.exception.BasePerpetualException;

/**
 *
 * @author  brunob
 */
public interface IThreadManager 
{
    public void addNewThread(String pThreadName) throws BasePerpetualException;
    public void addNewThread(String pThreadName, Runnable pRunnable)  throws BasePerpetualException;
    public void addLoad(Runnable pRunnable)  throws BasePerpetualException;
    public void removeThread(String pThreadName) throws BasePerpetualException;  
    public void addLoadToThread(String pThreadName, Runnable pRunnable)  throws BasePerpetualException;   
    public void removeLoadFromThread(String pThreadName)  throws BasePerpetualException;
    public void replaceLoad(String pThreadName, Runnable pRunnable)  throws BasePerpetualException;
    public void shutdownThread(String pThreadName)  throws BasePerpetualException;
    public void shutdownAllThreads()  throws BasePerpetualException; 
    public int getNumberOfRunningThreads();
    
       
}
