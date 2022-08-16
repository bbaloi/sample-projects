package com.perpetual.logserver;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import com.perpetual.logserver.util.InboxListener;


public interface LogDatabase extends java.io.Serializable
{
	public Cursor retrieveLogRecords(LogFilter filter) throws Exception;
	public Iterator iterateLogFiles();
//	Iterator iterateHosts();
	public LogRecordFormat getLogRecordFormat();
        public int getLinesPerPage();
        public Cursor submitQueryRequest(double pUid,LogFilter logFilter) throws Exception;
        public Collection getQueryResults(double pUid,Cursor pDBCursor,int pageNum,int pageSize) throws Exception;
        public boolean isMultithreaded();  
        public void submitQueryRequest(double pUid,LogFilter logFilter,InboxListener pInboxListener) throws Exception;
        public void getQueryResults(double pUid,Cursor pDBCursor,int pageNum,int pageSize,InboxListener pInboxListner) throws Exception;
       
         
}


