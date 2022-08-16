/*
 * ILogQuery.java
 *
 * Created on November 9, 2003, 7:08 PM
 */

package com.perpetual.logserver;

/**
 *
 * @author  brunob
 */
public interface ILogQuery extends java.io.Serializable
{
    
 public Cursor retrieveLogRecords(LogFilter logFilter) throws Exception;
	
}
