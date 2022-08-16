/*
 * Created on 6-Aug-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.throttle.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author simon
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite =
			new TestSuite("Test for com.perpetual.application.collector.throttle.tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestMessageProcessorThrottleControl.class));
		//$JUnit-END$
		return suite;
	}
}
