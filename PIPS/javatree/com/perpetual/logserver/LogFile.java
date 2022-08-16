package com.perpetual.logserver;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;


public interface LogFile extends java.io.Serializable
{
	public Cursor retrieveLogRecords(LogFilter filter) throws Exception;
	public String getHost() throws Exception;		// this will migrate to a base LogFileProperties class.  there is a FilenameProperties but it suggests DiskLogFile use only, we need to make it more generic.
        public String getKey() ;
        public boolean isOpen();
}


