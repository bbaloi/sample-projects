/*
 * Created on 30-Aug-2003
 *
 * NO WARRANTY  
 * -----------  
 * THIS SOFTWARE IS PROVIDED "AS-IS"  WITH ABSOLUTELY NO WARRANTY OF  
 * ANY KIND, EITHER IMPLIED OR EXPRESSED, INCLUDING, BUT NOT LIMITED  
 * TO, THE IMPLIED WARRANTIES OF FITNESS FOR ANY PURPOSE
 * AND MERCHANTABILITY.  
 *
 *    
 * NO LIABILITY  
 * ------------  
 * THE AUTHORS ASSUME ABSOLUTELY NO RESPONSIBILITY FOR ANY  
 * DAMAGES THAT MAY RESULT FROM THE USE,  
 * MODIFICATION OR REDISTRIBUTION OF THIS SOFTWARE, INCLUDING,  
 * BUT NOT LIMITED TO, LOSS OF DATA, CORRUPTION OR DAMAGE TO DATA,  
 * LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF SAVINGS,  
 * BUSINESS INTERRUPTION OR FAILURE TO INTEROPERATE WITH  
 * OTHER SOFTWARE, EVEN IF SUCH DAMAGES ARE FORESEEABLE.  
 */
package com.perpetual.recordprocessor.control.domain.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author simon
 *
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite =
			new TestSuite("Test for com.perpetual.recordprocessor.control.domain.tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestDomainRefresher.class));
		//$JUnit-END$
		return suite;
	}
}
