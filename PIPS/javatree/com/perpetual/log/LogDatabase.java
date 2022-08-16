package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;


public interface LogDatabase
{
	Cursor retrieveLogRecords(LogFilter filter) throws Exception;
	Iterator iterateLogFiles();
//	Iterator iterateHosts();
	LogRecordFormat getLogRecordFormat();
}


