/*
 * RecordProcessor.java
 *
 * Created on July 25, 2003, 9:40 AM
 */

package com.perpetual.recordprocessor.control;

import java.util.List;

import com.perpetual.util.threadpool.IManagedThread;

/**
 *
 * @author  brunob
 */
public abstract class RecordProcessor implements Runnable,IManagedThread
{
    
    /** Creates a new instance of RecordProcessor */
    public RecordProcessor() {
    }
    public void run() 
    {
    }
    public void  stop()
    {
    }
    public List getSummaryRecords()
    {
        return null;
    }
     
}
