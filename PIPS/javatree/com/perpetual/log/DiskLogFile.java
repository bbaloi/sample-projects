package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import com.perpetual.util.Cache;

public class DiskLogFile implements LogFile
{
	public DiskLogFile(DiskLogDatabase logDatabase, LogRecordFormat logRecordFormat, DiskLogFileStructure.FilenameProperties filenameProperties, File file, Cache cache) throws Exception
	{
		m_logDatabase = logDatabase;
		m_logRecordFormat = logRecordFormat;
		m_filenameProperties = filenameProperties;
		m_file = file;
		m_cache = cache;
		m_cache.add(m_file, new FileCacheItem(m_file), true);
		m_open = true;
	}

	public void close() throws Exception
	{
		if (!m_open)
			throw new Exception("DiskLogFile " + m_file + " can not close, because it is not open");
		if (m_openCursors > 0)
			throw new Exception("DiskLogFile " + m_file + " can not close, because it is " + m_openCursors + " open that should have been closed first");
		m_cache.remove(m_file, true);
		m_open = false;
	}

	public String toString() { return "file=" + m_file; }

	// LogFile interface
	//
	public String getHost() throws Exception
	{
		return m_filenameProperties.getHost();
	}

	public Cursor retrieveLogRecords(LogFilter logFilter) throws Exception
	{
                //sLog.debug("In DiskLogFile.retrieveLogRecords()");
		if (!m_open)
			throw new Exception("DiskLogFile can not retrieve record, because it is not open");
		//sLog.debug("host:"+getHost());
                if (!logFilter.hostMatches(getHost()))
                {
                    sLog.debug("no match for host found, exiting !");
                	return null;
                }
                //sLog.debug("type:"+m_filenameProperties.getType());
		if (!logFilter.typeMatches(m_filenameProperties.getType()))
                {
                    sLog.debug("no match for type found, exisiting");
			return null;
                }
		Date filenameStartTime = m_filenameProperties.getStartTime(), filenameEndTime = m_filenameProperties.getEndTime();
		if (!logFilter.timeRangeMatches(filenameStartTime, filenameEndTime))
                {
                    sLog.debug("no match for time range found, exiting");
			return null;
                }
                
/*
		try
		{
			loadFirstLastLogRecords();		// optimize!
		}
		catch (Throwable ex)
		{
			sLog.error("Log file corrupt: " + m_file + "\n" + ex.getMessage());
			return null;
		}
		if (!logFilter.timeRangeMatches(m_startTime, m_endTime))
			return null;
*/
		DiskCursor cursor = new DiskCursor(logFilter);
		if (cursor.currentLogRecord() == null)
		{
			cursor.close();
			return null;
		}
                sLog.debug("Retrieved Cursor:"+cursor);
		return cursor;
	}

	// DiskLogFile
	//
	public DiskLogFileStructure.FilenameProperties getFilenameProperties() { return m_filenameProperties; }

	private class DiskCursor extends Cursor
	{
		private DiskCursor(LogFilter logFilter) throws Exception
		{
                    //sLog.debug("constructing DiskCursor !");
			m_openCursors++;
			m_logFilter = logFilter;
                        
                        FileCacheItem item = (FileCacheItem)m_cache.checkOut(m_file);
                                                
                        m_open = true;
			RandomAccessFile raf = item.getRandomAccessFile();
			raf.seek(0);
			try
			{
				// seek to first matching line
				//
				for (String line; (line = raf.readLine()) != null; )
				{
                                    //sLog.debug("read line:"+line);
					if (line.trim().length() == 0) continue;

					try
					{
//System.out.println("!!!!!!!!!!!!!!!!!!!!! " + line);
                                                //sLog.debug("constructing new Log Record");
						LogRecord logRecord = new LogRecord(m_logRecordFormat, line);
						if (m_logFilter.matches(logRecord))
						{
                                                    //if message prarser works too----
                                                   	m_logRecord = logRecord;
							break;                                                    
						}
					}
					catch (Throwable ex)
					{
						// ignore parse errors comletely...?
					}
				}
			}
			finally
			{
				m_cache.checkIn(m_file);
			}
		}

		public LogRecord nextLogRecord() throws Exception
		{
			m_logRecord = null;
			FileCacheItem item = (FileCacheItem)m_cache.checkOut(m_file);
			RandomAccessFile raf = item.getRandomAccessFile();
			boolean close = false;
			try
			{
				while (true)
				{
					String line;
					do
					{
						line = raf.readLine();
						if (line == null)
						{
							close = true;
							return null;
						}
					} while (line.trim().length() == 0);
					try
					{
						LogRecord logRecord = new LogRecord(m_logRecordFormat, line);
						//System.out.println("! " + logRecord + " " + m_logFilter.matches(logRecord));
						if (m_logFilter.matches(logRecord))
						{
							return m_logRecord = logRecord;
						}
					}
					catch (Throwable ex)
					{
						// ignore parse errors comletely...?
					}
				}
			}
			finally
			{
				m_cache.checkIn(m_file);
				if (close)
					close();
			}
		}
		public LogRecord currentLogRecord()
		{
			return m_logRecord;
		}

		public void close() throws Exception
		{
			if (m_open)
			{
//				m_cache.remove(m_file, true);
				m_open = false;
				m_openCursors--;
			}
		}

		private LogFilter m_logFilter;
		private LogRecord m_logRecord;
		private boolean m_open;
	}

	private class FileCacheItem implements Cache.Item
	{
		private FileCacheItem(File file)
		{
			m_file = file;
		}
		public synchronized void freeze() throws Exception
		{
			sLog.debug("FileCacheItem freezing " + m_file);
			if (m_raf == null)
				throw new Exception("FileCacheItem can't freeze, already frozen");
			m_pos = m_raf.getFilePointer();
			m_raf.close();
			m_raf = null;
			
			sLog.debug("Open file count is now " + --m_openFiles);
		}
		public synchronized void thaw() throws Exception
		{
			sLog.debug("FileCacheItem thawing " + m_file);
			if (m_raf != null)
				throw new Exception("FileCacheItem can't thaw, already thawed");
			while (true)
			{
				try
				{
					m_raf = new RandomAccessFile(m_file, "r");
					break;
				}
				catch (Throwable ex)
				{
					// something went wrong, why?
					//
					if (!m_file.isFile())
					{
						// file went somewhere, tell disklog db we're panicing and must find out where it went
						//
						m_file = m_logDatabase.panicFindFile(m_file);
						if (m_file == null || !m_file.isFile())
                                                {
                                                    String msg="Can't thaw file " + m_file + ", because it went somewhere and disk log db could not correct it";
                                                    sLog.error(msg);
						    throw new Exception(msg);
                                                }
                                        }
				}
			}
			m_raf.seek(m_pos);
			
			sLog.debug("Open file count is now " + ++m_openFiles);
		}

		public RandomAccessFile getRandomAccessFile() { return m_raf; }

		private File m_file;
		private RandomAccessFile m_raf;
		private long m_pos;
	}

	private DiskLogDatabase m_logDatabase;
	private LogRecordFormat m_logRecordFormat;
	private DiskLogFileStructure.FilenameProperties m_filenameProperties;
	private LogRecord m_firstLogRecord, m_lastLogRecord;
	private Date m_startTime, m_endTime;
	private Cache m_cache;
	private File m_file;
	private boolean m_open;
	private int m_openCursors;

	private static int m_nFiles;
	private static Logger sLog = Logger.getLogger(DiskLogFile.class);
	private static int m_openFiles;
}


