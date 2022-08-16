package com.perpetual.viewer.control.ejb.logstore;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;
import java.rmi.*;
import java.text.*;
import javax.ejb.*;

import org.apache.log4j.Logger;
import org.jdom.*;

import com.perpetual.util.ResourceLoader;
import com.perpetual.logserver.LogDatabase;
import com.perpetual.logserver.DiskLogDatabase;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogFilter;
import com.perpetual.logserver.LogRecordFormat;
import com.perpetual.logserver.LogRecord;
import com.perpetual.logserver.LogSystem;
import com.perpetual.logserver.DiskLogFile;
import com.perpetual.util.UIDGenerator;
import java.util.LinkedList;
import com.perpetual.logserver.util.InboxFactory;
import com.perpetual.logserver.util.InboxListener;


/**
 * @ejb:bean name="LogSession" type="Stateful" view-type="remote" 
   @ejb.transaction="NotSupported"
 * @jboss.container-configuration name="SessionBeanStateful_Pool200"
 */
public abstract class LogSessionBean implements SessionBean
{    
        private static Logger sLog = Logger.getLogger(LogSession.class);
        private Cursor m_cursor;
	private Map m_params;
	private int m_line;
	private LogSystem m_logSystem;
        private boolean gotCursor=false;
        private Cursor cursor=null;
        private Object inboxSynchPoint=null;
        private LinkedList localInbox=null;
        private InboxListener inboxListener=null;
       
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		sLog.debug("ejbCreate");
		try
		{
			m_logSystem = LogSystem.getDefault();
		}
		catch (Throwable ex)
		{
			sLog.error("Could not get log system", ex);
			throw new CreateException("Could not initialize LogSessionBean");
		}
                inboxSynchPoint=new Object();
	}

	public void ejbRemove() throws RemoteException
	{
		sLog.debug("ejbRemove");
		if (m_cursor != null)
		{
			try
			{
				m_cursor.close();
			}
			catch (Throwable ex)
			{
				throw new RemoteException("Could not close cursor", ex);
			}
			m_cursor = null;
		}
	}

	/** @ejb.interface-method */
	public Collection getLogRecords(Map params, int pageLength, int page) throws Exception
	{
		Cursor cursor = m_logSystem.getLogDatabase().retrieveLogRecords(new LogFilter(params));

		int minLine = page * pageLength, maxLine = (page + 1) * pageLength;
		sLog.debug("getLogRecords " + params);

		if (m_cursor == null || m_line > minLine || !params.equals(m_params))
		{
			sLog.debug("(re)querying " + params);
			if (m_cursor != null)
				m_cursor.close();
			m_params = params;
			m_cursor = m_logSystem.getLogDatabase().retrieveLogRecords(new LogFilter(m_params));
			m_line = 0;
		}

		TimeZone timeZone = (TimeZone)params.get("timeZone");
		if (timeZone == null) timeZone = TimeZone.getDefault();

		Locale locale = (Locale)params.get("locale");
		if (locale == null) locale = Locale.getDefault();

		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

		Collection col = new ArrayList();
		for (LogRecord logRecord; m_line < maxLine && (logRecord = m_cursor.nextLogRecord()) != null; m_line++)
		{
			if (m_line >= minLine)
			{
				Collection col2 = new ArrayList();
				for (Iterator i = logRecord.iterateFields(); i.hasNext(); )
				{
					LogRecordFormat.Field field = (LogRecordFormat.Field)i.next();
					if (field instanceof LogRecordFormat.TimeField)
						col2.add(dateFormat.format(((LogRecordFormat.TimeField)field).getTime()));
					else if (field instanceof LogRecordFormat.DateField)
						col2.add(dateFormat.format(((LogRecordFormat.DateField)field).getDate()));
					else
						col2.add(field.toString());
				}
				col.add(col2);
                                sLog.debug("added record!");
			}
		}
		return col;
	}
        /** @ejb.interface-method */
	public Collection getQueryRecordSet(Map params, int pageLength,int page,double pUid) throws Exception
	//public Collection getQueryRecordSet(Map params, int pageLength,int page) throws Exception
	{            
           // double pUid = UIDGenerator.getInstance().getUID();                                       
            Collection resultSet=null;
            TimeZone timeZone = (TimeZone)params.get("timeZone");
            if (timeZone == null) timeZone = TimeZone.getDefault();

            Locale locale = (Locale)params.get("locale");
            if (locale == null) locale = Locale.getDefault();

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
            if(!gotCursor)
            {
                //cursor = m_logSystem.getLogDatabase().submitQueryRequest(pUid,new LogFilter(params));
                localInbox=InboxFactory.getInstance().createInbox(pUid);
                inboxListener = new InboxListener(localInbox);
                //m_logSystem.getLogDatabase().submitQueryRequest(pUid,new LogFilter(params),localInbox);
                m_logSystem.getLogDatabase().submitQueryRequest(pUid,new LogFilter(params),inboxListener);
               
                cursor = (Cursor) inboxListener.getItem();
                
                /*synchronized(localInbox)
                {
                    sLog.debug("Waiting for Cursor !"); 
                    localInbox.wait();
                }
                sLog.debug("Message arrived in in box !");
                cursor = (Cursor)localInbox.removeFirst();                
                 **/               
                sLog.debug("Got Cursor");
                gotCursor=true;
            }
            //resultSet = m_logSystem.getLogDatabase().getQueryResults(pUid,cursor,page,pageLength);
            
            m_logSystem.getLogDatabase().getQueryResults(pUid,cursor,page,pageLength,inboxListener);
            resultSet = (Collection) inboxListener.getItem();
            /*synchronized(localInbox)
            {
                sLog.debug("Waiting for ResultSet #"+page);
                localInbox.wait();
                resultSet = (Collection)localInbox.removeFirst();                                 
               
            }*/
             sLog.debug("Got Result Set !");
             
              if(resultSet.size()==0)
              {
                  sLog.debug("Last Record Set, removing inbox");
                  inboxListener=null;
                  InboxFactory.getInstance().removeInbox(pUid);
              }
            return resultSet;
	}
       
	/** @ejb.interface-method */
	public Collection getFieldNames() throws Exception
	{
		Collection col = new ArrayList();
		for (Iterator i = m_logSystem.getLogDatabase().getLogRecordFormat().iterateFieldNames(); i.hasNext(); )
		{
			col.add(i.next());
		}
		return col;
	}

	/** @ejb.interface-method */
	public StringBuffer test() throws RemoteException
	{
		return new StringBuffer("HELLO THERE");
	}
	
}


