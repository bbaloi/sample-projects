package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;

import org.jdom.*;
import org.apache.log4j.Logger;

import com.perpetual.util.Cache;


// some of these methods and fields should be migrated to LogDatabase, you'll see once other types of databases materialize

public class DiskLogDatabase implements LogDatabase, Runnable
{
    private static Logger sLog = Logger.getLogger(DiskLogDatabase.class);
    
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
	private Collection m_openCaches = new ArrayList();
              
	public DiskLogDatabase(File baseDir, Element config) throws Exception
	{
		m_baseDir = baseDir;
		m_config = config;
		m_logRecordFormat = new LogRecordFormat(m_config.getChild("logrecordformat"));
		m_diskLogFileStructure = new DiskLogFileStructure(m_config.getChild("disklogfilestructure"));
		m_refreshInterval = Integer.parseInt(m_config.getAttributeValue("refreshinterval"));
		init();
		m_open = true;
	}
        private void init()
	{
//		sLog.debug("DiskLogDatabase.init");
		if (m_cache != null && m_cache.getThawedItemCount() > 0)
		        m_openCaches.add(m_cache);                
		{
                    //sLog.debug("DiskLogFile.init() called from run()");
			int n = 0;
			for (Iterator i = m_openCaches.iterator(); i.hasNext(); )
			{
				Cache cache = (Cache)i.next();
				int count = cache.getItemCount();
				if (count > 0)
					n++;
				else
					i.remove();
			}
//			sLog.debug("Number of caches with allocated items that are being tracked is " + n);
		}
		m_cache = new Cache(Integer.parseInt(m_config.getChild("cache").getAttributeValue("maxentries")));
		List list = new ArrayList();
		load(m_baseDir, list);
		synchronized (this)
		{
			m_logFileList = list;
			m_initDate = new Date();
		}
	}
	/**
	Attempt to find out where the file was moved to.  It interpretes the file name, searches the hierarchy for new files, and tries to guess which new file could be where it was moved/renamed to.
	*/
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
		if (!m_open)
			throw new Exception("DiskLogDatabase " + m_baseDir + " can not close, because it is not open");
		if (m_openCursors > 0)
			throw new Exception("DiskLogDatabase " + m_baseDir + " can not close, because there are " + m_openCursors + " cursors open which should have been closed");
		for (Iterator i = m_logFileList.iterator(); i.hasNext(); )
		{
			DiskLogFile diskLogFile = (DiskLogFile)i.next();
			diskLogFile.close();
		}
		for (Iterator i = m_openCaches.iterator(); i.hasNext(); )
		{
			Cache cache = (Cache)i.next();
			int count = cache.getThawedItemCount();
			if (count > 0)
			{
				sLog.info("BAD!  Cache that was created " + cache.getDate() + " still had " + count + " items.");
			}
			else
				i.remove();
		}
		m_open = false;
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
			while (true)
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
		}
	}

	public LogRecordFormat getLogRecordFormat()
	{
		return m_logRecordFormat;
	}

	
	private void load(File file, List list)
	{
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

	public Cursor retrieveLogRecords(LogFilter logFilter) throws Exception
	{
//		init();			// optimize!		thread does it, shouldn't be called here
		return new DatabaseCursor(logFilter);
	}

	private class DatabaseCursor extends Cursor
	{
		private DatabaseCursor(LogFilter logFilter)
		{
			m_openCursors++;
			m_logFilter = logFilter;
			for (Iterator i = m_logFileList.iterator(); i.hasNext(); )
			{
				LogFile logFile = (LogFile)i.next();
				try
				{
					Cursor cursor = logFile.retrieveLogRecords(m_logFilter);
					if (cursor != null)
						m_cursorList.add(cursor);		// list is sorted automatically
				}
				catch (Throwable ex)
				{
					sLog.error("Problem retrieving log records from " + logFile);
					ex.printStackTrace();
				}
			}
		}

		public LogRecord nextLogRecord() throws Exception
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
		public LogRecord currentLogRecord()
		{
			return m_logRecord;
		}
		public void close() throws Exception
		{
//			System.out.println("DiskLogDatabase closing cursor");
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

	

	private static class HalfAssedSortedList extends LinkedList		// why the frig did Sun not add a SortedList...
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


