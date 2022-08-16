// many tests are omitted - duplicates of log.tests - use common 
// logging subsystem
package com.perpetual.application.collector.log.relay.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author simon
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite =
			new TestSuite("Test for com.perpetual.application.collector.log.relay.tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestRelayRolloverTimerTask.class));
		//$JUnit-END$
		return suite;
	}
}
