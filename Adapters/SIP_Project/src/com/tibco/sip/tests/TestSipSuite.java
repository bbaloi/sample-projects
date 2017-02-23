package com.tibco.sip.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestSipSuite
{
	public static Test suite() 
	{

        TestSuite suite = new TestSuite();
        suite.addTestSuite(TestSimpleSIPSession.class);
	     //suite.addTestSuite(TestNoProxy.class);
        //suite.addTestSuite(TestTwoSipStacksNoProxy.class);
        //suite.addTestSuite(TestTwoOrMoreSipStacksNoProxy.class);
        	    	          
        
        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) 
    {
        junit.textui.TestRunner.run(suite());
    }


}
