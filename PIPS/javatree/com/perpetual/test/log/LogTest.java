package com.perpetual.test.log;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.io.*;
import java.text.*;

import org.jdom.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.DiskLogDatabase;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.log.Cursor;
import com.perpetual.util.ResourceLoader;


public class LogTest extends TestCase
{
	public LogTest(String name) throws Exception
	{
		super(name);

		m_config = ResourceLoader.loadResourceAsJdomElement("config.xml");
		m_logDatabase = new DiskLogDatabase(logDir, m_config.getChild("disklogdatabase"));

		m_dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		m_dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public void testBrunoData() throws Exception
	{
		System.out.println("*** testBrunoData ***");

		Map map = new HashMap();
		map.put("startTime", m_dateFormat.parse("20030726 220000"));
		map.put("maxTime", m_dateFormat.parse("20030726 221000"));
		map.put("Host", new String[] { "10.1.1.250" });		// test8.log is in the test directory
		map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
		map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
		map.put("filetypes", new String[] { "archive" });
		LogFilter filter = new LogFilter(map);

		expectNRecords(m_logDatabase.retrieveLogRecords(filter), 37);
	}

	public void testBasicRetrieval() throws Exception
	{
		System.out.println("*** testBasicRetrieval ***");

		Map map = new HashMap();
//		map.put("startTime", m_dateFormat.parse("20030726 145036"));
//		map.put("maxTime", m_dateFormat.parse("20030726 145036"));
		map.put("startTime", m_dateFormat.parse("20030726 220000"));
		map.put("maxTime", m_dateFormat.parse("20030726 221000"));
//		map.put("startTime", new Date(0));	//m_dateFormat.parse("20030726 145036"));
//		map.put("maxTime", new Date());	//m_dateFormat.parse("20030726 145036"));
		map.put("Host", new String[] { "10.1.1.250" });		// test8.log is in the test directory
		map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
		map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
		LogFilter filter = new LogFilter(map);

		expectNRecords(m_logDatabase.retrieveLogRecords(filter), 37);
	}

	public void testFilterHostSeverityFacility() throws Exception
	{
		System.out.println("*** testFilterHostSeverityFacility ***");

		Map map = new HashMap();
		map.put("startTime", m_dateFormat.parse("20030726 220000"));
		map.put("maxTime", m_dateFormat.parse("20030726 221000"));
		map.put("Host", new String[] { "10.1.1.250" });		// test8.log is in the test directory
		map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
		map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
		LogFilter filter = new LogFilter(map);

		expectNRecords(m_logDatabase.retrieveLogRecords(filter), 37);
	}

	public void testFilterFileTypes() throws Exception
	{
		System.out.println("*** testFilterFileTypes ***");

		{
			Map map = new HashMap();
			map.put("startTime", m_dateFormat.parse("20010726 220000"));
			map.put("maxTime", m_dateFormat.parse("20040726 221000"));
			map.put("Host", new String[] { "10.1.1.250" });		// test8.log is in the test directory
			map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
			map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
			map.put("filetypes", new String[] { "nonexistantintentionally" });
			LogFilter filter = new LogFilter(map);

			expectNRecords(m_logDatabase.retrieveLogRecords(filter), 0);
		}

		{
			Map map = new HashMap();
			map.put("startTime", m_dateFormat.parse("20030726 220000"));
			map.put("maxTime", m_dateFormat.parse("20030726 221000"));
			map.put("Host", new String[] { "10.1.1.250" });		// test8.log is in the test directory
			map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
			map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
			map.put("filetypes", new String[] { "archive" });
			LogFilter filter = new LogFilter(map);

			expectNRecords(m_logDatabase.retrieveLogRecords(filter), 37);
		}
	}

	private void expectNRecords(Cursor cursor, int n) throws Exception
	{
		int lines = 0;
		for (; ; )
		{
			LogRecord logRecord = cursor.nextLogRecord();
			if (logRecord == null)
				break;
			lines++;
			System.out.println(logRecord);
		}
		Assert.assertTrue("lines == " + lines, lines == n);
	}

	private Element m_config;
	private DiskLogDatabase m_logDatabase;
	private DateFormat m_dateFormat;

	private static final File logDir = new File("syslog");

	public static Test suite() throws Exception
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new LogTest("testBasicRetrieval"));
		suite.addTest(new LogTest("testFilterHostSeverityFacility"));
		suite.addTest(new LogTest("testFilterFileTypes"));
		suite.addTest(new LogTest("testBrunoData"));
		return suite;
	}

}


