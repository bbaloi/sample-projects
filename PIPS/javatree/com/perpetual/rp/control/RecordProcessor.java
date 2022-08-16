/*
 * RecordProcessor.java
 *
 * Created on July 25, 2003, 9:40 AM
 */

package com.perpetual.rp.control;

import com.perpetual.util.threadpool.IManagedThread;
import com.perpetual.rp.control.query.ISummaryCollectionQuery;
import com.perpetual.rp.control.query.SummaryQueryTemplate;
import com.perpetual.exception.BasePerpetualException;

import java.util.List;

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
