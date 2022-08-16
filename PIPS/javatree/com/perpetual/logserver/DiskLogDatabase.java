package com.perpetual.logserver;

/**
 * 
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.apache.log4j.Logger;

import com.perpetual.logserver.util.Cache;
import com.perpetual.logserver.util.CacheItem;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Constants;
import com.perpetual.util.threadpool.SimpleThreadPoolManager;
import com.perpetual.logserver.model.vo.QueryRequestVO;
import com.perpetual.util.UIDGenerator;
import com.perpetual.logserver.util.LogserverConstants;
import java.util.Iterator;
import com.perpetual.logserver.util.InboxListener;

// some of these methods and fields should be migrated to LogDatabase, you'll see once other types of databases materialize

public class DiskLogDatabase implements LogDatabase, Runnable,ILogstoreDBMgr,java.io.Serializable
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(DiskLogDatabase.class);
    
        private DiskLogFileStructure m_diskLogFileStructure;
	private LogRecordFormat m_logRecordFormat;
	private File m_baseDir;
	private List m_logFileList = new ArrayList();
	private Cache m_cache;
	private int m_refreshInterval;
	private Thread m_thread;
	private boolean m_refreshing, m_skipRefreshOnce;
	private Date m_initDate;
	private boolean m_open;
	private int m_openCursors;
	private Element m_config;
	private boolean cacheCreate=true;
        private boolean createThreadPool=true;
        private int initSize=0;
        private Object logListMutex = new Object();
        private SimpleThreadPoolManager m_threadPoolMgr=null;
        private boolean multithreaded=false;
        private boolean exit=false;
        //------------for multithreaded prupose------------
        private LinkedList lRequestQueue=new LinkedList(); 
        private LinkedList lResultsQueue=new LinkedList();
        private LinkedList lCursorQueue=new LinkedList();        
        private LinkedList submissionTokenQueue=new LinkedList();
        private LinkedList resultsTokenQueue=new LinkedList();
        private Object submissionSynchPoint=new Object();
        private Object resultSynchPoint=new Object();
        //--------------------------------------------------
        private int linesPerPage=0;
                            
	public DiskLogDatabase(File baseDir, Element config) throws Exception
	{
		m_baseDir = baseDir;
		m_config = config;
		m_logRecordFormat = new LogRecordFormat(m_config.getChild("logrecordformat"));
		m_diskLogFileStructure = new DiskLogFileStructure(m_config.getChild("disklogfilestructure"));
		m_refreshInterval = Integer.parseInt(m_config.getAttributeValue("refreshinterval"));
	        multithreaded=false;
                init();
		m_open = true;
	}
        public DiskLogDatabase(File baseDir, Element config,boolean pMultithreaded,int pLinesPerPage) throws Exception
	{
		m_baseDir = baseDir;
		m_config = config;
		m_logRecordFormat = new LogRecordFormat(m_config.getChild("logrecordformat"));
		m_diskLogFileStructure = new DiskLogFileStructure(m_config.getChild("disklogfilestructure"));
		m_refreshInterval = Integer.parseInt(m_config.getAttributeValue("refreshinterval"));
	        multithreaded = pMultithreaded;
                linesPerPage=pLinesPerPage;
                init();
		m_open = true;
	}
        private synchronized void init()
	{	
                 if(cacheCreate)
                {
                    sLog.debug("Creating cache !");
                    m_cache = new Cache(Integer.parseInt(m_config.getChild("cache").getAttributeValue("maxentries")),
                                        Constants.PERCENTAGE_LRU_BATCH);                   
                    //m_cache.startLRU_gc();
                    cacheCreate=false;
                }
                if(multithreaded)
                {
                    if(createThreadPool)
                    {
                        try
                        {
                            Object [] paramList = new Object [1];
                            Class [] paramTypeList = new Class [1];
                            paramList[0]=this;
                            paramTypeList[0]=Class.forName("com.perpetual.logserver.ILogstoreDBMgr");
                            m_threadPoolMgr = new SimpleThreadPoolManager(Integer.parseInt(m_config.getChild("threadpool").getAttributeValue("initial_size")),
                                                        Integer.parseInt(m_config.getChild("threadpool").getAttributeValue("max_size")),
                                                        Class.forName("com.perpetual.logserver.QueryProcessor"),paramList,paramTypeList);
                        }
                        catch(Exception excp)
                        {
                            sLog.error("Could not instantiate Thread Pool !");
                            excp.printStackTrace();
                        }
                        
                    }
                    createThreadPool=false;
                  
                }
                initSize=m_logFileList.size();
                sLog.debug("Load/Refreshing logstore image ! current size:"+initSize);   
                synchronized (logListMutex)
		{
                    m_logFileList.clear();
                    load(m_baseDir, m_logFileList);
                    sLog.debug("Discovered "+(m_logFileList.size()-initSize)+" new files!");
                    sLog.debug("Read in "+m_logFileList.size()+" files !");
                    m_initDate = new Date();
		}                        
	}
	/**
	Attempt to find out where the file was moved to.  It interpretes the file name, searches the hierarchy for new files, and tries to guess which new file could be where it was moved/renamed to.
	*/
        public int getLinesPerPage()
        {
            return linesPerPage;
        }
	public File panicFindFile(File file)
	{
            sLog.debug("Means collector rolled "+file.getName()+"over to archive directory...looking for file there !!!");
            File fptr = searchArchiveForFile(file);
		return fptr;	// ha!  not yet.  Not sure if this trick will work.
	}
        private File searchArchiveForFile(File pFile)
        {
            sLog.debug("Should be searchin' archive dir for file:"+pFile.getName()+"...but for now just return null !!!");
            
            File fptr=null;
            //.......look for file in archive directory.....
            
                           
            return fptr;
        }

	public void close() throws Exception
	{
                //clean + close cache         
                sLog.debug("---Closing DiskLogDatabase---");
                m_thread.destroy();
                if(multithreaded)
                   m_threadPoolMgr.shutdownAllThreads();
                m_cache.close();              
                
                sLog.debug( "closing DiskLogDatabase..." );		
		exit = true;

		synchronized( lRequestQueue )
		{
			lRequestQueue.notifyAll();
		}
                synchronized(lResultsQueue)
                {
                    lResultsQueue.notifyAll();
                }
                synchronized(this)
                {
                    this.notifyAll();
                }
	}
        protected  void finalize()
        {
            try
            {
                
                m_thread.destroy();
                if(multithreaded)
                    m_threadPoolMgr.shutdownAllThreads();
                m_cache.close();
            }
            catch(Exception excp)
            {
                sLog.error("Could not close DiskLogDatabase !");
                excp.printStackTrace();
            }
        }
	public void setAutoRefresh(boolean auto)
	{
		if (!auto)
		{
			sLog.debug("DiskLogDatabase turning off auto refresh, stopping thread");
			Thread t = m_thread;
			if (t != null)
			{
				t.interrupt();
				try
				{
					t.join();
				}
				catch (Throwable ex)
				{
					sLog.error("Could not wait for refresh thread to end, *my* own self thread was actually interrupted");
				}
			}
			m_thread = null;
		}
		else
		{
			if (m_thread == null)
			{
				sLog.debug("DiskLogDatabase turning on auto refresh, starting thread");
				m_thread = new Thread(this);
				m_thread.start();
			}
		}
	}

	public void run()
	{
		try
		{
			while (!exit)
			{
				synchronized (this)
				{
					wait(m_refreshInterval);
					m_refreshing = true;
				}
				if (m_skipRefreshOnce)
					m_skipRefreshOnce = false;
				else
					init();

				m_refreshing = false;
			}
		}
		catch (InterruptedException ex)
		{
			sLog.debug("DiskLogDatabase refresh thread interrupted");
		}
		catch (Throwable ex)
		{
			sLog.error("Error in DiskLogDatabase refresh thread", ex);
                        ex.printStackTrace();
		}
	}

	public LogRecordFormat getLogRecordFormat()
	{
		return m_logRecordFormat;
	}

	
	private void load(File file, List list)
	{
            int initialSize = list.size();
            
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++)
				load(files[i], list);
		}
		else if (file.isFile())
		{
			try
			{
				DiskLogFileStructure.FilenameProperties props = m_diskLogFileStructure.parseFileName(file);
				if (props != null)
				{
					DiskLogFile logFile = new DiskLogFile(this, m_logRecordFormat, props, file, m_cache);
				        list.add(logFile);                                        
				}
			}
			catch (Throwable ex)
			{
				ex.printStackTrace();
				System.out.println(ex);
			}
		}
	}

	// LogDatabase interface
	//
	public Iterator iterateLogFiles()
	{
		return m_logFileList.iterator();
	}

/*
	public Iterator iterateHosts()
	{
		return m_logFileMap.keySet().iterator();
	}
*/
                
        
        public void submitQueryRequest(double pUid, LogFilter logFilter, InboxListener pInboxListener) throws Exception 
        {
            QueryRequestVO voResult=null;
            Collection list=null;
                       
                synchronized (logListMutex)
                {
                    list = new ArrayList((Collection)m_logFileList);

                }
            QueryRequestVO vo = new QueryRequestVO(pUid,logFilter,this,list,logFilter,linesPerPage,pInboxListener);
            synchronized( lRequestQueue )
             {
                            lRequestQueue.add( vo );
                            lRequestQueue.notifyAll();
              }
            sLog.debug("Submitted Cursor Request for:"+vo);  
        }
        public Cursor submitQueryRequest(double pUid, LogFilter logFilter) throws Exception
        {
            QueryRequestVO voResult=null;
            Collection list=null;
            Cursor cursor=null;
            
                synchronized (logListMutex)
                {
                    list = new ArrayList((Collection)m_logFileList);

                }
            QueryRequestVO vo = new QueryRequestVO(pUid,logFilter,this,list,logFilter,linesPerPage);
            sLog.debug("Submitted query for:"+vo);  
              synchronized( lRequestQueue )
              {
                            lRequestQueue.add( vo );
                            lRequestQueue.notifyAll();
              }

              while(true)
              {
                  sLog.debug("Waiting for Cursor...");
                  synchronized (lCursorQueue)
                  {              
                        lCursorQueue.wait();

                  }
                  voResult = (QueryRequestVO) lCursorQueue.removeFirst();
                  if(vo.getUID()==pUid)
                  {
                      sLog.debug("Got our cursor !");
                      cursor = (Cursor) vo.getPayload();
                      break;
                  }
                  else
                  {
                        synchronized (lCursorQueue)
                        {              
                            lCursorQueue.add(voResult);
                        }
                  }

            }
              return cursor;
        }
        /*
        public void submitQueryRequest(double pUid, LogFilter logFilter) throws Exception
        {
            Collection list=null;
            synchronized (logListMutex)
            {
                list = new ArrayList((Collection)m_logFileList);
                
            }
          QueryRequestVO vo = new QueryRequestVO(pUid,logFilter,this,list,logFilter,linesPerPage);
          sLog.debug("Submitted query for:"+vo);  
          synchronized( lRequestQueue )
	  {
			lRequestQueue.add( vo );
			lRequestQueue.notifyAll();
          }
        }*/
       public void getQueryResults(double pUid, Cursor pDBCursor, int pageNum, int pageSize, InboxListener pInboxListener) throws Exception 
       {
            Collection pageSet=null;
            QueryRequestVO voRequest=null;
                         
              voRequest = new QueryRequestVO(pUid,pDBCursor,this,null,null,pageSize,pageNum,pInboxListener);
              voRequest.setState(LogserverConstants.STATE_2); 
              synchronized( lRequestQueue )
              {
                            lRequestQueue.add( voRequest );
                            lRequestQueue.notifyAll();
              }
       }
        public Collection getQueryResults(double pUid,Cursor pDBCursor,int pageNum,int pageSize) throws Exception
        {
            Collection pageSet=null;
              QueryRequestVO voRequest=null;
              QueryRequestVO voResult=null;
              
              voRequest = new QueryRequestVO(pUid,pDBCursor,this,null,null,pageSize,pageNum);
              voRequest.setState(LogserverConstants.STATE_2);   
              
              synchronized( lRequestQueue )
              {
                            lRequestQueue.add( voRequest );
                            lRequestQueue.notifyAll();
              }
              while(true)
              {
                    synchronized( lResultsQueue )
                    {
                        lResultsQueue.wait();                        
                    }
                    voResult = (QueryRequestVO) lResultsQueue.removeFirst();
                    if(voResult.getUID()==pUid)
                    {
                        sLog.debug("Got Page Set - returning to user !");
                        pageSet = (Collection) voResult.getPayload();
                        voResult=null;
                        break;
                        
                    }
                    else
                    {
                        synchronized (lResultsQueue)
                        {              
                            lResultsQueue.add(voResult);
                        }
                    }
                    
           }
              return pageSet;
        }
        public synchronized Collection getQueryResults(double pUid)
        {
            Collection result=null;
            QueryRequestVO vo=null;
            sLog.debug("waiting for results for:"+pUid);
            
            while(true)
            {
                    synchronized( lResultsQueue )
                    {
                        try
                        {
                            vo = (QueryRequestVO) lResultsQueue.removeFirst();
                        }
                        catch(java.util.NoSuchElementException excp)
                        {
                            sLog.error("No more elements on QueryRequestQueue!");
                        }
                    }

                    if(vo.getUID()==pUid)
                    {
                        if(vo.getState()==LogserverConstants.STATE_1)
                        {
                            sLog.debug("Got DiskCursor, republishing on RequestQueue !");
                            vo.setState(LogserverConstants.STATE_2);
                            synchronized( lRequestQueue )
                            {

                                lRequestQueue.add( vo );
                                lRequestQueue.notifyAll();
                             }
                        }
                        if(vo.getState()==LogserverConstants.STATE_3)
                        {
                            sLog.debug("Got Result Collection..");
                            result = (Collection) vo.getPayload();
                            vo=null;
                            break;
                        }
                    }
           }
            
            return result;
        }

	public synchronized Cursor retrieveLogRecords(LogFilter logFilter) throws Exception
	{
//		init();			// optimize!		thread does it, shouldn't be called here
		return new DatabaseCursor(logFilter);
	}
        public synchronized Cursor retrieveLogRecords(LogFilter logFilter, Collection pLogFileList)
	{
//		init();			// optimize!		thread does it, shouldn't be called here
		return new DatabaseCursor(logFilter,pLogFileList);
	}

        public QueryRequestVO threadWaiting() 
        {
            QueryRequestVO vo = null;

		synchronized( lRequestQueue )
		{
			while( lRequestQueue.size() == 0 ) {

				if ( exit ) {
					return( null );
				}
                                    sLog.debug( "Query Processing Thread waiting..." );				

				try
				{
					lRequestQueue.wait();
				}
				catch( InterruptedException e )
				{
					sLog.debug( "Thread interrupted...", e );
				}
			}

			// remove & get a handle on the next Runnable
			vo = (QueryRequestVO)lRequestQueue.removeFirst();
		}

		sLog.debug( "Removed Runnable from queue:  " + vo.toString() );
                return vo;
        }
        
        public boolean getExit()
        {
            return exit;
        }
        
        public synchronized void publishResult(QueryRequestVO vo) 
        {
            synchronized(lResultsQueue)
            {
                lResultsQueue.add(vo);
                lResultsQueue.notifyAll();
            }
        }
               
        public void publishCursor(QueryRequestVO vo)
        {
            synchronized(lCursorQueue)
            {
                lCursorQueue.add(vo);
                lCursorQueue.notifyAll();
            }
        }        
        public boolean isMultithreaded()
        {
            return multithreaded;
        }
       
        
        //private class DatabaseCursor extends Cursor implements Serializable
        public class DatabaseCursor extends Cursor implements Serializable
	{
		public DatabaseCursor(LogFilter logFilter)
		{
                        sLog.debug("DatabaseCursors open:"+m_openCursors);
                        m_openCursors++;
			m_logFilter = logFilter;
                        
                        synchronized (logListMutex)
                        {
                            for (Iterator i = m_logFileList.iterator(); i.hasNext(); )
                            {
                                    DiskLogFile logFile = (DiskLogFile)i.next();
                                    try
                                    {
                                            //get from cache. If not in cache add.
                                            if(!m_cache.contains(logFile.getKey()))
                                                m_cache.add(logFile);
                                            Cursor cursor = logFile.retrieveLogRecords(m_logFilter);
                                            if (cursor != null)
                                                    m_cursorList.add(cursor);		// list is sorted automatically
                                    }
                                    catch(java.io.FileNotFoundException excp)
                                    {
                                        sLog.error("File is no longer availbale, removing from list");
                                        m_logFileList.remove(logFile);
                                    }
                                    catch (Throwable ex)
                                    {
                                            sLog.error("Problem retrieving log records from " + logFile);
                                            ex.printStackTrace();
                                    }
                            }
                        }
		}

                public DatabaseCursor(LogFilter logFilter,Collection logFileList)
		{
                        sLog.debug("DatabaseCursors open:"+m_openCursors);
                        m_openCursors++;
			m_logFilter = logFilter;
                                                
                            for (Iterator i = logFileList.iterator(); i.hasNext(); )
                            {
                                    DiskLogFile logFile = (DiskLogFile)i.next();
                                    try
                                    {
                                            //get from cache. If not in cache add.
                                            if(!m_cache.contains(logFile.getKey()))
                                                m_cache.add(logFile);
                                            Cursor cursor = logFile.retrieveLogRecords(m_logFilter);
                                            if (cursor != null)
                                                    m_cursorList.add(cursor);		// list is sorted automatically
                                    }
                                    catch(java.io.FileNotFoundException excp)
                                    {
                                        sLog.error("File is no longer availbale, removing from list");
                                        logFileList.remove(logFile);
                                    }
                                    catch (Throwable ex)
                                    {
                                            sLog.error("Problem retrieving log records from " + logFile);
                                            ex.printStackTrace();
                                    }
                            }
                        
		}
		public synchronized LogRecord nextLogRecord() throws Exception
		{
			if (m_cursorList.isEmpty())
				return m_logRecord = null;
			Cursor cursor = (Cursor)m_cursorList.removeFirst();
			m_logRecord = cursor.currentLogRecord();
			if (cursor.nextLogRecord() != null)
				m_cursorList.add(cursor);
			else
				cursor.close();
			return m_logRecord;
		}
		public synchronized LogRecord currentLogRecord()
		{
			return m_logRecord;
		}
		public synchronized void close() throws Exception
		{
                    //		System.out.println("DiskLogDatabase closing cursor");
			Exception error = null;
			for (Iterator i = m_cursorList.iterator(); i.hasNext(); )
			{
				try
				{
					((Cursor)i.next()).close();
				}
				catch (Exception ex)
				{
					error = ex;
				}
			}
			m_cursorList.clear();
			if (error != null)
				throw error;
			m_openCursors--;
		}

		private LogFilter m_logFilter;
		private LinkedList m_cursorList = new HalfAssedSortedList();
		private LogRecord m_logRecord;
	}
        
	private static class HalfAssedSortedList extends LinkedList implements java.io.Serializable		// why the frig did Sun not add a SortedList...
	{
		public boolean add(Object object)
		{
			for (int len = size(), i = 0; i < len; i++)
				if (((Comparable)get(i)).compareTo(object) > 0)
				{
					add(i, object);
					return true;
				}
			return super.add(object);
		}
	}
}


