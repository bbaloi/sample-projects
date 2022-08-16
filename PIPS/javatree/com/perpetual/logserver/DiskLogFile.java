package com.perpetual.logserver;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import com.perpetual.logserver.util.Cache;
import com.perpetual.logserver.util.CacheItem;
import com.perpetual.util.PerpetualC2Logger;
import java.util.LinkedList;

public class DiskLogFile implements LogFile,CacheItem,java.io.Serializable
{
       private static PerpetualC2Logger sLog = new PerpetualC2Logger(DiskLogFile.class);
    
        private RandomAccessFile m_raf;
	private DiskLogDatabase m_logDatabase;
	private LogRecordFormat m_logRecordFormat;
	private DiskLogFileStructure.FilenameProperties m_filenameProperties;
	private LogRecord m_firstLogRecord, m_lastLogRecord;
	private Date m_startTime, m_endTime;
	private Cache m_cache;
	private File m_file;
	private boolean m_open;
	private int m_openCursors;
        private long fileOffsetPointer=0;
        private boolean inUse=false;

	private static int m_nFiles;
	
	private static int m_openFiles;
        
	public DiskLogFile(DiskLogDatabase logDatabase, LogRecordFormat logRecordFormat, DiskLogFileStructure.FilenameProperties filenameProperties, File file, Cache cache) throws Exception
	{
		m_logDatabase = logDatabase;
		m_logRecordFormat = logRecordFormat;
		m_filenameProperties = filenameProperties;
		m_file = file;
		m_cache = cache;		
		m_open = true;
	}
        
	public void close() throws Exception
	{
		if (!m_open)
			throw new Exception("DiskLogFile " + m_file + " can not close, because it is not open");
		if (m_openCursors > 0)
			throw new Exception("DiskLogFile " + m_file + " can not close, because it is " + m_openCursors + " open that should have been closed first");
		//m_cache.remove(m_file, true);
		m_open = false;
                m_openFiles--;
	}
        
	public String toString() { return "file=" + m_file; }

	// LogFile interface
	//
	public String getHost() throws Exception
	{
		return m_filenameProperties.getHost();
	}
        public void reinsertInCache() throws Exception
        {
            sLog.debug("Reinserting File in cache !");
            m_cache.add(this);
        }

	public synchronized Cursor retrieveLogRecords(LogFilter logFilter) throws Exception
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
        public void destroy()
        {
            sLog.debug("Closing file:"+m_file.getName());
            try
            {
                m_raf.close();
                m_open=false;
                m_openFiles--;
            }
            catch(java.io.IOException excp)
            {
                sLog.error("Couldn't close file:"+m_file.getName());
                excp.printStackTrace();
            }
          
            sLog.debug("OpenFiles:"+m_openFiles);
        }

        public String getKey()
        {
            return m_file.getName();
        }

        public void instantiate() throws Exception
        {
            sLog.debug("Opening file:"+m_file.getName());
            try
            {
                
                m_raf = new RandomAccessFile(m_file, "r");
                m_open=true;
                m_openFiles++;
            }
            catch(java.io.FileNotFoundException excp)
            {
                sLog.error("Could not find file:"+m_file.getName());
                excp.printStackTrace();
                m_open=false;
                throw excp;
            }
            
        }
	public DiskLogFileStructure.FilenameProperties getFilenameProperties() { return m_filenameProperties; }
             
        public boolean isOpen()
        {
            return m_open;
        }
        
        public boolean inUse()
        {
            return m_open;
        }
        
       	private class DiskCursor extends Cursor implements Serializable
	{
            
		private LogFilter m_logFilter;
		private LogRecord m_logRecord;
		private boolean m_open;
                private LinkedList recordList = new LinkedList();
                               
		private DiskCursor(LogFilter logFilter) throws Exception
		{
                        sLog.debug("Creating new cursor for file;open cursors:"+m_openCursors);
                       	m_openCursors++;                        
			m_logFilter = logFilter;  
                        //m_open=true;                       
                        //sLog.debug("FilePointer:"+fileOffsetPointer);
                        m_raf.seek(0);
                        
			try
			{
				// seek to first matching line
				//
				//for (String line; (line = raf.readLine()) != null; )
                                for (String line; (line = m_raf.readLine()) != null; )
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
                                                        //recordList.add(logRecord);
                                                   	m_logRecord = logRecord;    
                                                        fileOffsetPointer = m_raf.getFilePointer();
							sLog.debug("found a match;offset="+fileOffsetPointer);
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
				//m_cache.checkIn(m_file);
			}
                      
		}                

                private void repositionCursor() 
                {
                    m_open=true;
                    try
                    {
                        reinsertInCache();
                        sLog.debug("resetting File pointer @:"+fileOffsetPointer);
                        m_raf.seek(fileOffsetPointer);
                        nextLogRecord();
                    }
                    catch(Exception excp)
                    {
                        excp.printStackTrace();
                    }
                }
		public LogRecord nextLogRecord() throws Exception
		{
                        LogRecord logRecord=null;
                        //index++;                        
			m_logRecord = null;                        
			boolean close = false;
                          
                        try
                        {
                                                      
                            while (true)
				{
                                  try
                                  {
					String line;
					do
					{
                                               sLog.debug("reading next line @:"+fileOffsetPointer);
						line = m_raf.readLine();                                                
						if (line == null)
						{
                                                    sLog.debug("Read last line in file !");
							close = true;
                                                        return null;
						}
					} 
                                        while (line.trim().length() == 0);
					try
                                        {
                                            logRecord = new LogRecord(m_logRecordFormat, line);
                                        }
                                        catch(Exception excp)
                                        {
                                            sLog.error(excp.getMessage());
                                            logRecord=null;
                                            sLog.info("Could not parse record:"+line);
                                            sLog.debug("---SHould not discard record: should transform it into something else....---");
                                        }
                                         //System.out.println("! " + logRecord + " " + m_logFilter.matches(logRecord));
                                        if (m_logFilter.matches(logRecord))
                                        {
                                                fileOffsetPointer = m_raf.getFilePointer();
                                                sLog.debug("found a match;offset="+fileOffsetPointer);
                                                return m_logRecord = logRecord;
                                        }
                                    }
                                    catch(java.io.IOException excp)
                                    {
                                        sLog.error("File handle was closed");
                                        //excp.printStackTrace();
                                        this.repositionCursor();
                                     }                                   
				}
			}
			finally
			{
				//m_cache.checkIn(m_file);
				if (close)
                                {   
                                    sLog.debug("Closing cursor");
                                        fileOffsetPointer=0;
					close();
                                }
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
				//m_open = false;
				m_openCursors--;
			}
		}

	}
            
}


