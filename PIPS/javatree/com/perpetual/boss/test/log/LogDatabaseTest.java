package com.perpetual.boss.test.log;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;

import org.jdom.*;

import com.perpetual.util.ResourceLoader;
import com.perpetual.log.LogFile;
import com.perpetual.log.DiskLogFile;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.DiskLogDatabase;
import com.perpetual.log.DiskLogFileStructure;
import com.perpetual.log.LogRecord;
import com.perpetual.log.LogFilter;


public class LogDatabaseTest
{
	public static void main(String[] args) throws Exception
	{
		System.setErr(System.out);
		System.out.println("FileLog " + new Date());

		Element config = ResourceLoader.loadResourceAsJdomElement("config.xml");
		LogRecordFormat logRecordformat = new LogRecordFormat(config.getChild("logrecordformat"));
		DiskLogFileStructure diskLogFileStructure = new DiskLogFileStructure(config.getChild("disklogfilestructure"));

		//DiskLogDatabase db = new DiskLogDatabase(diskLogFileStructure, logRecordformat, new File(args[0]));
//
//		System.out.println("\nhosts");
//		for (Iterator i = db.iterateLogFiles(); i.hasNext(); )
//		{
//			System.out.println(i.next());
//		}

		System.out.println("\nlog records");
//		Cursor cursor = db.retrieveLogRecords(new LogFilter());
//		for (LogRecord logRecord; (logRecord = cursor.nextLogRecord()) != null; )
//		{
//			System.out.println(logRecord);
//		}

/*
		LogRecordFormat logRecordFormat = new LogRecordFormat(ResourceLoader.loadResourceAsJdomElement("logrecordformat.xml"));
		LogFile logFile = new DiskLogFile(logRecordFormat, new File(args[0]));
		for (Cursor cursor = logFile.retrieveLogRecords(null); cursor.next(); )
		{
			System.out.println(cursor.getLogRecord().getLine() + " | " + cursor.getLogRecord());
		}
*/
	}
}


