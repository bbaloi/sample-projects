/*
 * QueryThread.java
 *
 * Created on November 14, 2003, 4:37 PM
 */

package com.perpetual.logserver;

import com.perpetual.util.threadpool.IManagedThread;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.logserver.model.vo.QueryRequestVO;
import java.util.Collection;
import com.perpetual.logserver.util.LogserverConstants;
import com.perpetual.logserver.DiskLogDatabase;
import com.perpetual.logserver.DiskLogDatabase.DatabaseCursor;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogRecord;
import com.perpetual.logserver.LogFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Locale;
import java.text.DateFormat;
import java.util.LinkedList;

import com.perpetual.logserver.util.InboxFactory;
import com.perpetual.logserver.util.InboxListener;
/**
 *
 * @author  brunob
 */
public class QueryProcessor implements IManagedThread 
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger( QueryProcessor.class );
    private boolean run=true;
    private ILogstoreDBMgr dbMgr=null;
    private Collection logFileList=null;
    private QueryRequestVO vo = null;
    private Cursor lDBCursor=null;
    private Cursor lFileCursor = null;
    private LogFilter logFilter=null;
    private Collection recordSet=null;
    private InboxListener inboxListener=null;
    /** Creates a new instance of QueryThread */
    public QueryProcessor()
    {
    }
    public QueryProcessor(ILogstoreDBMgr pDbMgr)
    {
        dbMgr=pDbMgr;
    }
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     *
     */
    public void run() 
    {
        while(run)
        {                
            // wait inside the ThreadPool for the next message to arrive
            vo = dbMgr.threadWaiting();
            
            
            // check if the lThreadPoolObject is null and then check mananager to see if I should exit - if so 'break'
            if ( ( vo == null ) && ( dbMgr.getExit() ) ) 
            {
                    break;
            }
            // run the object
            try
            {
                logFileList = vo.getLogFileList();
                logFilter = vo.getFilter();
                inboxListener=vo.getInboxListener();
                //inbox = InboxFactory.getInstance().getInbox(vo.getUID());
                //here do the funky chicken business    
                if(vo.getState()==LogserverConstants.STATE_0)
                {
                    sLog.debug("This is the first query - getting DiskCursor handle !");
                     //process & get DiskCursor +add
                    lDBCursor = getLogstoreDBCursor(logFileList,logFilter);
                    vo.setPayload(lDBCursor);
                    sLog.debug("Got Cursor, about to notify the receiver !");
                    //vo.setState(LogserverConstants.STATE_1);      
                    //sLog.debug("Setting result after state:"+LogserverConstants.STATE_1);
                    if(inboxListener==null)
                    {
                        sLog.debug("No Inbox provided, using the Default DBManager Cursor Queue!");
                        dbMgr.publishCursor(vo);
                    }
                    else
                    {
                        sLog.debug("Notifying the Job that the Cursor is retrieved !");
                        inboxListener.addItem(lDBCursor);
                        inboxListener.notifyInbox();
                            
                        /*synchronized(inbox)
                        {
                            inbox.add(lDBCursor);
                            inbox.notifyAll();
                        }*/
                    }
                }
                if(vo.getState()==LogserverConstants.STATE_2)
                {
                    sLog.debug("This is the getting next Records in DisckCursor - getting Record Collection");
                    recordSet = getRecordSet((Cursor) vo.getPayload(),vo.getLinesPerPage(),vo.getPageNumber());
                    vo.setPayload(recordSet);
                    //vo.setState(LogserverConstants.STATE_3);  
                    //sLog.debug("Setting result after state:"+LogserverConstants.STATE_3);
                    if(inboxListener==null)
                    {
                      sLog.debug("No Inbox provided, using the Default DBManager Results Queue!");
                      dbMgr.publishResult(vo);
                    }
                    else
                    {
                        sLog.debug("Notifying the Job that a Result Set was  retrieved !");
                         inboxListener.addItem(recordSet);
                        inboxListener.notifyInbox();
                        /*synchronized(inbox)
                        {
                            inbox.add(recordSet);
                            inbox.notifyAll();
                        }*/
                    }
                }
                
                
            }
            catch( Throwable e )
            {
                    // Handle exceptions thrown by 'Runnable's.
                    // Continue to allow the thread to enter the pool again
                    sLog.error( "Caught exception:  " + e.toString() + " while running:  " + vo.toString(), e );
                    e.printStackTrace();
            }
        }
        
            sLog.debug( " Query Processor Closing up shop..." );  
        
    }
    private Collection getRecordSet(Cursor pCursor,int linesPerPage,int pageNumber) throws Exception
    {
        Collection recordSet=new ArrayList();
        Collection pageSet = null;
        String sPage=null;
        int minLine,maxLine;
        int m_line=0;
        Locale locale=null;
	
       sLog.debug("++++Right now always getting default Locale, will need to pass through from GUI....+++++++++");
        if (locale == null) locale = Locale.getDefault();

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        
            minLine = pageNumber * linesPerPage;
            maxLine = (pageNumber + 1) * linesPerPage;
           
            sPage = new Integer(pageNumber).toString();        
            pageSet = new ArrayList();
        
            m_line=minLine;
            for (LogRecord logRecord; m_line < maxLine && (logRecord = pCursor.nextLogRecord()) != null; m_line++)
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
                        
                        pageSet.add(col2);
                        sLog.debug("added record!");
                }
            }
            sLog.debug("Page"+pageNumber+"-# records found:"+ pageSet.size());
            
       return pageSet;
    }
    private  Cursor getLogstoreDBCursor(Collection pLogFileList,LogFilter pLogFilter) throws Exception
    {
        
        //construct DatabaseCursor -- add new constructot to take 2 params.       
        return dbMgr.retrieveLogRecords(pLogFilter,pLogFileList);
        
        
    }    
    public synchronized void stop() 
    {
        run=false;
        this.notify();
    }
    
}
