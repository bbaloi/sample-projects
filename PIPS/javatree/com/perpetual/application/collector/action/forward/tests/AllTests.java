/*
 * Created on 25-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.forward.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite =
			new TestSuite("Test for com.perpetual.application.collector.action.forward.tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestForwardAction.class));
		suite.addTest(new TestSuite(TestForwardHost.class));
		//$JUnit-END$
		return suite;
	}
}
