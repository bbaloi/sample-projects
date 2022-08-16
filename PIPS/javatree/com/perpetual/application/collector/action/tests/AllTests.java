/*
 * Created on 26-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author simon
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite =
			new TestSuite("Test for com.perpetual.application.collector.action.tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestActionFactory.class));
		suite.addTest(new TestSuite(TestTextFileParser.class));
		suite.addTest(new TestSuite(TestSelector.class));
		//$JUnit-END$
		return suite;
	}
}
