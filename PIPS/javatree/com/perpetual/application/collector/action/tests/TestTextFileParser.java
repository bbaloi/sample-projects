/*
 * Created on 26-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.tests;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import com.perpetual.application.collector.action.TextFileParser;
import com.perpetual.application.collector.action.forward.ForwardAction;
import com.perpetual.application.collector.action.forward.ForwardHost;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.ListenerConstants;

/**
 * @author simon
 *
 */
public class TestTextFileParser extends TestCase {

	private static String NEWLINE = System.getProperty("line.separator");
	
	private static String NICE_FILE =
		"#This is a comment"
		+ NEWLINE + "#another comment.."
		+ NEWLINE + "*.*		@host1" 
		+ NEWLINE + "*.*		@host2";

	private static String MULTI_HOSTS_FILE =
		"#This is a comment"
		+ NEWLINE + "#another comment.."
		+ NEWLINE + "*.*		@host1" 
		+ NEWLINE + "*.*		@host2,host3";

	private static String NICE_FILE_WITH_EPS =
		"#This is a comment"
		+ NEWLINE + "#another comment.."
		+ NEWLINE + "*.*		@host1@1000" 
		+ NEWLINE + "*.*		@host2:6000@1500";
		
	/**
	 * Constructor for TestTextFileParser.
	 * @param arg0
	 */
	public TestTextFileParser(String arg0) {
		super(arg0);
	}
	
	public void testNiceFile () throws Exception
	{
		BufferedReader reader = new BufferedReader(new StringReader(NICE_FILE));
		
		TextFileParser parser = new TextFileParser(reader, new Configuration());
		
		List actionList = parser.parse();
		
		assertEquals("have two actions", 2, actionList.size());
		
		ForwardAction action1 = (ForwardAction) actionList.get(0);
		List hostList1 = action1.getForwardHostList();
		assertEquals("have one host", 1, hostList1.size());
		ForwardHost host1 = (ForwardHost) hostList1.get(0);
		
		ForwardAction action2 = (ForwardAction) actionList.get(1);
		List hostList2 = action2.getForwardHostList();
		assertEquals("have one host", 1, hostList2.size());
		ForwardHost host2 = (ForwardHost) hostList2.get(0);
		
		assertEquals("host1 name", "host1", host1.getHostName());
		assertEquals("host2 name", "host2", host2.getHostName());
	}

	public void testMultihostsFile () throws Exception
	{
		BufferedReader reader = new BufferedReader(new StringReader(MULTI_HOSTS_FILE));
		
		TextFileParser parser = new TextFileParser(reader, new Configuration());
		
		List actionList = parser.parse();
		
		assertEquals("have two actions", 2, actionList.size());
		
		ForwardAction action1 = (ForwardAction) actionList.get(0);
		List hostList1 = action1.getForwardHostList();
		assertEquals("have one host", 1, hostList1.size());
		ForwardHost host1 = (ForwardHost) hostList1.get(0);
		
		ForwardAction action2 = (ForwardAction) actionList.get(1);
		List hostList2 = action2.getForwardHostList();
		assertEquals("have two hosts", 2, hostList2.size());
		ForwardHost host21 = (ForwardHost) hostList2.get(0);
		ForwardHost host22 = (ForwardHost) hostList2.get(1);
		
		assertEquals("host1 name", "host1", host1.getHostName());
		assertEquals("host21 name", "host2", host21.getHostName());
		assertEquals("host22 name", "host3", host22.getHostName());
	}
	
	public void testNiceFileWithEPS () throws Exception
	{
		BufferedReader reader = new BufferedReader(
				new StringReader(NICE_FILE_WITH_EPS));
		
		TextFileParser parser = new TextFileParser(reader, new Configuration());
		
		List actionList = parser.parse();
		
		assertEquals("have two actions", 2, actionList.size());
		
		ForwardAction action1 = (ForwardAction) actionList.get(0);
		List hostList1 = action1.getForwardHostList();
		assertEquals("have one host", 1, hostList1.size());
		ForwardHost host1 = (ForwardHost) hostList1.get(0);
		
		ForwardAction action2 = (ForwardAction) actionList.get(1);
		List hostList2 = action2.getForwardHostList();
		assertEquals("have one host", 1, hostList2.size());
		ForwardHost host2 = (ForwardHost) hostList2.get(0);
		
		assertEquals("host1 name", "host1", host1.getHostName());
		assertEquals("host1 port", ListenerConstants.SERVER_PORT, host1.getPort());
		assertEquals("host1 eps", 1000, host1.getEpsRating());
		
		assertEquals("host2 name", "host2", host2.getHostName());
		assertEquals("host2 port", 6000, host2.getPort());
		assertEquals("host2 eps", 1500, host2.getEpsRating());
	}
}
