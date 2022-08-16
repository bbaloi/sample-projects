//package com.perpetual.test.logserver;
//
///**
// * @author Mike Pot http://www.mikepot.com
// *
// * Use is subject to license terms.
// * This file is provided with no warranty whatsoever.
// */
//
//import java.util.*;
//import java.io.*;
//import java.text.*;
//
//import org.jdom.*;
//import junit.framework.Assert;
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
////import com.perpetual.log.LogRecordFormat;
//import com.perpetual.log.DiskLogDatabase;
//import com.perpetual.log.LogFilter;
//import com.perpetual.log.LogRecord;
//import com.perpetual.log.Cursor;
//import com.perpetual.util.ResourceLoader;
//import com.perpetual.util.EJBLoader;
//import com.perpetual.logserver.control.LogSessionHome;
//import com.perpetual.logserver.control.LogSession;
//
//
//public class LogServerTest extends TestCase
//{
//	public LogServerTest(String name) throws Exception
//	{
//		super(name);
//
//		EJBLoader.configureFromDefaults();
//		m_dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
//	}
//
///*
//	public void testPages() throws Exception
//	{
//
//		expectNRecords(m_logDatabase.retrieveLogRecords(filter), 3);
//	}
//*/
//
//	public void testBasicRetrieval() throws Exception
//	{
//		System.out.println("*** testBasicRetrieval ***");
//
//		LogSession logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
//		try
//		{
//			Map map = new HashMap();
//			map.put("startTime", m_dateFormat.parse("20030726 145036"));
//			map.put("endTime", m_dateFormat.parse("20030726 145036"));
//			map.put("Host", new String[] { "test8" });		// test8.log is in the test directory
//			map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
//			map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
//
//			{
//				Collection rows = logSession.getLogRecords(map, 10, 0);
//				expectNRecords(rows, 10);
//			}
//
//			// skipping a page
//			{
//				Collection rows = logSession.getLogRecords(map, 20, 1);
//				expectNRecords(rows, 20);
//			}
//
//			// going backwards, causing a new cursor inside the LogSession
//			{
//				Collection rows = logSession.getLogRecords(map, 15, 1);
//				expectNRecords(rows, 15);
//			}
//		}
//		finally
//		{
//			logSession.remove();
//		}
//	}
//
///*
//	public void testFilterHostSeverityFacility() throws Exception
//	{
//		System.out.println("*** testFilterHostSeverityFacility ***");
//
//		Map map = new HashMap();
//		map.put("startTime", m_dateFormat.parse("20030726 145036"));
//		map.put("maxTime", m_dateFormat.parse("20030726 145036"));
//		map.put("Host", new String[] { "test8" });		// test8.log is in the test directory
//		map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
//		map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
//		LogFilter filter = new LogFilter(map);
//
//		expectNRecords(m_logDatabase.retrieveLogRecords(filter), 4);
//	}
//
//	public void testFilterFileTypes() throws Exception
//	{
//		System.out.println("*** testFilterFileTypes ***");
//
//		{
//			Map map = new HashMap();
//			map.put("startTime", m_dateFormat.parse("20010726 145036"));
//			map.put("maxTime", m_dateFormat.parse("20040726 145036"));
//			map.put("Host", new String[] { "test8" });		// test8.log is in the test directory
//			map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
//			map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
//			map.put("filetypes", new String[] { "nonexistantintentionally" });
//			LogFilter filter = new LogFilter(map);
//
//			expectNRecords(m_logDatabase.retrieveLogRecords(filter), 0);
//		}
//
//		{
//			Map map = new HashMap();
//			map.put("startTime", m_dateFormat.parse("20030726 145036"));
//			map.put("maxTime", m_dateFormat.parse("20030726 145036"));
//			map.put("Host", new String[] { "test8" });		// test8.log is in the test directory
//			map.put("Severity", new String[] { "0", "1", "2", "3", "4", "5", "6", "7" });		// all of them I think
//			map.put("Facility", new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });		// all of them I think
//			map.put("filetypes", new String[] { "archive" });
//			LogFilter filter = new LogFilter(map);
//
//			expectNRecords(m_logDatabase.retrieveLogRecords(filter), 4);
//		}
//	}
//*/
//
//	private void expectNRecords(Collection col, int n) throws Exception
//	{
//		int lines = 0;
//		for (Iterator y = col.iterator(); y.hasNext(); lines++)
//		{
//			int z = 0;
//			for (Iterator x = ((Collection)y.next()).iterator(); x.hasNext(); z++)
//			{
//				if (z > 0)
//					System.out.print(", ");
//				System.out.print(x.next());
//			}
//			System.out.println();
//		}
//		Assert.assertTrue(lines + " != " + n, lines == n);
//	}
//
//	private Element m_config;
//	private DiskLogDatabase m_logDatabase;
//	private DateFormat m_dateFormat;
//
//	private static final File logDir = new File("syslog");
//
//	public static Test suite() throws Exception
//	{
//		TestSuite suite = new TestSuite();
//		suite.addTest(new LogServerTest("testBasicRetrieval"));
////		suite.addTest(new LogServerTest("testFilterHostSeverityFacility"));
////		suite.addTest(new LogServerTest("testFilterFileTypes"));
////		suite.addTest(new LogServerTest("testBrunoData"));
//		return suite;
//	}
//
//}
//
//
//
