package com.perpetual.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */


public abstract class Cursor implements Comparable
{
	public int compareTo(Object other)
	{
		return currentLogRecord().compareTo(((Cursor)other).currentLogRecord());
	}
	public abstract LogRecord nextLogRecord() throws Exception;
	public abstract LogRecord currentLogRecord();
	public abstract void close() throws Exception;

	public static class EmptyCursor extends Cursor
	{
		public LogRecord nextLogRecord() throws Exception { return null; }
		public LogRecord currentLogRecord() { return null; }
		public void close() throws Exception { }
	}
}

