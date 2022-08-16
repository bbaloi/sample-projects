/*
 * Created on 26-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.tests;

import junit.framework.TestCase;

import com.perpetual.application.collector.action.ActionFactory;
import com.perpetual.application.collector.action.forward.ForwardAction;
import com.perpetual.application.collector.configuration.Configuration;

/**
 * @author simon
 *
 */
public class TestActionFactory extends TestCase {

	/**
	 * Constructor for TestActionFactory.
	 * @param arg0
	 */
	public TestActionFactory(String arg0) {
		super(arg0);
	}
	
	public void testCreateForwardAction () throws Exception
	{
		ActionFactory factory = new ActionFactory();
		
		ForwardAction action = (ForwardAction) factory.createAction("*.*",
			"@localhost", new Configuration());
		
		// no exception - we must have a ForwardAction
		assertNotNull("forward action", action);
		
		action.open();
		
		assertEquals("number of forward hosts", 1, action.getForwardHostList().size());
	}
}
