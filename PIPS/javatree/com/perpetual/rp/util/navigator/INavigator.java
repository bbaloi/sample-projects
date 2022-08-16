/*
 * INavigator.java
 *
 * Created on July 17, 2003, 8:47 PM
 */

package com.perpetual.rp.util.navigator;

import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogFilter;
import java.util.Collection;


/**
 *
 * @author  brunob
 */
public interface INavigator 
{
    //public Collection getHosts() throws Exception;
    public Cursor retrieveLogRecords(LogFilter logFilter) throws Exception;
    //public Cursor retrieveArchiveRecords(LogFilter)
	
}
