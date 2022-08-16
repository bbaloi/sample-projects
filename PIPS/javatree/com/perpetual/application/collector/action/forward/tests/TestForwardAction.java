/*
 * Created on 25-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.forward.tests;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.perpetual.application.collector.action.forward.ForwardAction;
import com.perpetual.application.collector.action.forward.ForwardHost;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.ListenerConstants;

/**
 * @author simon
 *
 */
public class TestForwardAction extends TestCase {
	
	/**
	 * Constructor for TestForwardAction.
	 * @param arg0
	 */
	public TestForwardAction(String arg0) {
		super(arg0);
	}
	
	public void setUp () throws Exception
	{
	}
	
	public void tearDown () throws Exception
	{
	}
	
	public void testConstructor() throws Exception
	{
		String fowardHostsString = "@host1,host2,host3";
	
		ForwardAction forwardAction = new ForwardAction("*.*", fowardHostsString,
			new Configuration());
		
		// check forward hosts list
		List forwardHosts = forwardAction.getForwardHostList();

		int j = 1;		
		for (Iterator i = forwardHosts.iterator(); i.hasNext(); j++ ) {
			ForwardHost forwardHost = (ForwardHost) i.next();

			assertEquals("host name", "host" + j, forwardHost.getHostName());
			assertEquals("port", ListenerConstants.SERVER_PORT,
				forwardHost.getPort());
		}
	}

	public void testOpen() throws Exception
	{
		String fowardHostsString = "@localhost,127.0.0.1";
	
		ForwardAction forwardAction = new ForwardAction("*.*", fowardHostsString,
				new Configuration());

		forwardAction.open();
				
		// check forward hosts list
		List forwardHosts = forwardAction.getForwardHostList();

		int j = 1;		
		for (Iterator i = forwardHosts.iterator(); i.hasNext(); j++ ) {
			ForwardHost forwardHost = (ForwardHost) i.next();

			assertNotNull("address was resolved", forwardHost.getAddress());
		}
	}
	
	public void testIsAction() throws Exception
	{
		String fowardHostsString = "@localhost,127.0.0.1";
	
		ForwardAction forwardAction = new ForwardAction("*.*", fowardHostsString,
				new Configuration());

		assertTrue("forward action", forwardAction.isAction(fowardHostsString));
	}
	
	public void testIsActionWithPorts() throws Exception
	{
		String fowardHostsString = "@localhost:6000";
	
		ForwardAction forwardAction = new ForwardAction("*.*", fowardHostsString,
			new Configuration());

		assertTrue("forward action", forwardAction.isAction(fowardHostsString));
	}
	
	public void testConstructorWithPorts() throws Exception
	{
		String fowardHostsString = "@host1:6001,host2:6002,host3:6003";

		ForwardAction forwardAction = new ForwardAction("*.*", fowardHostsString,
			new Configuration());
	
		// check forward hosts list
		List forwardHosts = forwardAction.getForwardHostList();

		int j = 1;		
		for (Iterator i = forwardHosts.iterator(); i.hasNext(); j++ ) {
			ForwardHost forwardHost = (ForwardHost) i.next();

			assertEquals("host name", "host" + j, forwardHost.getHostName());
			assertEquals("port", 6000 + j, forwardHost.getPort());
		}
	}
}
