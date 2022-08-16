package com.perpetual.util.patternmatcher.sample;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import java.util.Properties;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.PerpetualResourceLoader;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;

import com.perpetual.util.ServiceLocator;
import com.perpetual.util.Constants;
import com.perpetual.util.patternmatcher.IPatternMatcher;
import com.perpetual.util.patternmatcher.RecordPatternMatcherEngine;
import com.perpetual.util.patternmatcher.IMessagePatternInfo;

import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;


/**
 * Some simple tests.
 *
 */
public class PatternMatcherTestCase extends TestCase 
{
    //private static PerpetualC2Logger sLog = new PerpetualC2Logger( PatternMatcherTestCase.class );
                  
    private String configFileName= "/home/brunob/perpetual_viewer/config/recordformat.xml";
    private String syslogFileName = "/var/log/logstore/archive/10.1.1.250/10.1.1.250_20030727-154556-203_20030727-164556-275.log";
    private String ciscoFileName = "/var/log/logstore/archive/10.2.2.1/10.2.2.1_20030827-170817-762_20030827-170956-134.log";
    private BufferedReader ciscoFile=null,syslogFile=null;
    
    private IPatternMatcher pmEngine=null;
    private IMessagePatternInfo patternInfo=null;
    
        public static void main (String[] args) 
        {
               PatternMatcherTestCase pt = new PatternMatcherTestCase();
               junit.textui.TestRunner.run (suite());
	}
                
        public PatternMatcherTestCase()
        {
        }
	protected void setUp() 
        {
            System.out.println("Setting up PatternMatcher Test !");
            try
            {
                System.out.println("Instantiating RecordPatternMatcher:");
                pmEngine = new RecordPatternMatcherEngine(configFileName);    
                syslogFile = new BufferedReader(new FileReader(new File(syslogFileName)));
                ciscoFile = new BufferedReader(new FileReader(new File(ciscoFileName))); 
                                
            }           
            catch(Exception t)
            {
                //sLog.error("Couldn't get properties !");
                t.printStackTrace();
            }
              
	}
	public static Test suite()
        {
           /* TestSuite suite = new TestSuite();
                suite.addTest(new UserTest("testGetAllUsers"));
            
            return suite;*/
            return new TestSuite(PatternMatcherTestCase.class);
	}
        public void testMatchPattern()
        {
            String line=null;
            try
            {
                System.out.println("Reading syslog test file !!! ");
                while(true)
                {
                    line=syslogFile.readLine();
                    if(line==null)
                        break;
                    else
                    {
                        if(!line.equals(""))
                        {                            
                            patternInfo=pmEngine.findFirstMatch(line);
                            System.out.println("Message:"+line);
                            System.out.println("pattern detected:"+patternInfo.getPatternName());
                        }
                    }
                    
               
                }
                System.out.println("Reading cisco test file !!! ");
                while(true)
                {
                    line=ciscoFile.readLine();
                    if(line==null)
                        break;
                    else
                    {
                        if(!line.equals(""))
                        {     
                        patternInfo=pmEngine.findFirstMatch(line);
                        System.out.println("Message:"+line);
                        System.out.println("pattern detected:"+patternInfo.getPatternName());
                        }
                    }
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
}