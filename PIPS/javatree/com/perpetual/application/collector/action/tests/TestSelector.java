/*
 * Created on 29-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.tests;

import java.util.Random;

import junit.framework.TestCase;

import com.perpetual.application.collector.action.Selector;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.application.collector.syslog.SyslogConstants;

/**
 * @author simon
 *
 */
public class TestSelector extends TestCase {



	public static final String constructPRIString (int facility, int severity)
	{
		int priority = 8*facility + severity;
		
		return "<" + String.valueOf(priority) + ">";
	}
	
	private static String TIMESTAMP = "Jun 21 14:45:14";
	private static String STRING_HOST = "ahost";
	private static String TAG_PROCESS_PID = "processname[456]:";	
	private static String CONTENT = "this is some content";
	
	private static String REST_OF_SYSLOG_MESSSAGE = TIMESTAMP
			 + " "
			 + STRING_HOST
			 + " "
			 + TAG_PROCESS_PID
			 + CONTENT;
	
	private QueueEntry queueEntry;

	public TestSelector(String arg0) {
		super(arg0);
	}
	
	public void setUp() throws Exception
	{
		// create a queue entry so we can create a syslog message object
		
		this.queueEntry = new QueueEntry("", "host",
				System.currentTimeMillis(), 0);
	}
	
	public void testSelectorStarDotStar () throws Exception
	{
		int x = SyslogConstants.DEBUG;
		Selector selector = new Selector("*.*");

		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				Integer.MAX_VALUE,
				selector.getFacilitiesForSelector(0)[0]);	
		assertEquals("severities match", SyslogConstants.DEBUG,
			selector.getSeverityForSelector(0));
		
		// now throw some matches at it - everything should match
		
		this.queueEntry.setRawMessage(constructPRIString(0, 0) + 
				REST_OF_SYSLOG_MESSSAGE);
		assertTrue("matches 0-0",
			selector.isMatch(new SyslogMessage(this.queueEntry)));
		
		Random random = new Random(System.currentTimeMillis());
		// throw some messages with random facs, sevs at it
		for (int i = 0; i < 100; i++) {
			int facility = random.nextInt(SyslogConstants.NUMBER_OF_FACILITIES);
			int severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
			
			this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
							REST_OF_SYSLOG_MESSSAGE);
			assertTrue("matches " + facility + "-" + severity,
					selector.isMatch(new SyslogMessage(this.queueEntry)));
		}			
	}
	
	public void testSelectorKernDotDebug () throws Exception
	{
		int x = SyslogConstants.DEBUG;
		Selector selector = new Selector("kern.debug");

		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				0, selector.getFacilitiesForSelector(0)[0]);	
		assertEquals("severities match", SyslogConstants.DEBUG,
			selector.getSeverityForSelector(0));
		
		// now throw some matches at it - everything should match with fac=kern
				
		Random random = new Random(System.currentTimeMillis());
		// throw some messages with fixed facs, random sevs at it
		int facility = SyslogConstants.getFacilityIdByName("kern");
		int severity;
		for (int i = 0; i < 100; i++) {
			severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
			
			this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
							REST_OF_SYSLOG_MESSSAGE);
							
			assertTrue("matches " + facility + "-" + severity,
					selector.isMatch(new SyslogMessage(this.queueEntry)));
		}	
		
		// now try for some non-matches
		facility = SyslogConstants.getFacilityIdByName("ftp");
		severity = 0;
		
		this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
						REST_OF_SYSLOG_MESSSAGE);
							
		assertFalse("non-match " + facility + "-" + severity,
				selector.isMatch(new SyslogMessage(this.queueEntry)));		
	}
	
	public void testSelectorLocal5DotStar () throws Exception
	{
		int x = SyslogConstants.DEBUG;
		Selector selector = new Selector("local5.*");
		
		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				SyslogConstants.LOCAL5, selector.getFacilitiesForSelector(0)[0]);	
		assertEquals("severities match", SyslogConstants.DEBUG,
			selector.getSeverityForSelector(0));
	
	
		// now throw some matches at it - everything should match with fac=kern
			
		Random random = new Random(System.currentTimeMillis());
		// throw some messages with fixed facs, random sevs at it
		int facility = SyslogConstants.getFacilityIdByName("local5");
		int severity;
		for (int i = 0; i < 100; i++) {
			severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
		
			this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
							REST_OF_SYSLOG_MESSSAGE);
						
			assertTrue("matches " + facility + "-" + severity,
					selector.isMatch(new SyslogMessage(this.queueEntry)));
		}	
	
		// now try for some non-matches
		facility = SyslogConstants.getFacilityIdByName("ftp");
		severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
	
		this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
						REST_OF_SYSLOG_MESSSAGE);
						
		assertFalse("non-match " + facility + "-" + severity,
				selector.isMatch(new SyslogMessage(this.queueEntry)));

		
	}
	
	public void testSelectorLocal5DotAlert () throws Exception
	{
		Selector selector = new Selector("local5.alert");
		
		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				SyslogConstants.LOCAL5, selector.getFacilitiesForSelector(0)[0]);	
		assertEquals("severities match", SyslogConstants.ALERT,
			selector.getSeverityForSelector(0));

		// now throw some matches at it - everything should match with fac=kern
		
		Random random = new Random(System.currentTimeMillis());
		// throw some messages with fixed facs, random sevs at it
		testForMatch(selector, "local5", "alert");
		int facility = SyslogConstants.getFacilityIdByName("local5");
		int severity = SyslogConstants.getSeverityIdByName("alert");
		
		// now try for some non-matches
		facility = SyslogConstants.getFacilityIdByName("local6");

		this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
						REST_OF_SYSLOG_MESSSAGE);
					
		assertFalse("non-match " + facility + "-" + severity,
				selector.isMatch(new SyslogMessage(this.queueEntry)));
	}
	
	public void testSelectorMultipleFacsDotAlert () throws Exception
	{
		Selector selector = new Selector("kern,mail,local5.alert");
		
		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 3,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				SyslogConstants.KERNEL, selector.getFacilitiesForSelector(0)[0]);
		assertEquals("facilities match",
					SyslogConstants.MAIL, selector.getFacilitiesForSelector(0)[1]);
		assertEquals("facilities match",
					SyslogConstants.LOCAL5, selector.getFacilitiesForSelector(0)[2]);						
		assertEquals("severities match", SyslogConstants.ALERT,
			selector.getSeverityForSelector(0));
			
		// throw some messages with each fac
		testForMatch(selector, "local5", "alert");
		testForMatch(selector, "kern", "alert");
		testForMatch(selector, "mail", "alert");				
	}
	
	public void testMultipleSelectors () throws Exception
	{
		Selector selector = new Selector(
				"kern,mail,local5.alert;ftp.info;authpriv.crit");
				
		assertEquals("number of selectors", 3, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 3,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("number of facilities", 1,
				selector.getFacilitiesForSelector(1).length);
		assertEquals("number of facilities", 1,
				selector.getFacilitiesForSelector(2).length);

		
		assertEquals("facilities match",
				SyslogConstants.KERNEL, selector.getFacilitiesForSelector(0)[0]);
		assertEquals("facilities match",
					SyslogConstants.MAIL, selector.getFacilitiesForSelector(0)[1]);
		assertEquals("facilities match",
					SyslogConstants.LOCAL5, selector.getFacilitiesForSelector(0)[2]);						
		assertEquals("severities match", SyslogConstants.ALERT,
			selector.getSeverityForSelector(0));

		assertEquals("facilities match",
				SyslogConstants.FTP, selector.getFacilitiesForSelector(1)[0]);
		assertEquals("facilities match",
				SyslogConstants.AUTHPRIV, selector.getFacilitiesForSelector(2)[0]);

						
		assertEquals("severities match", SyslogConstants.ALERT,
			selector.getSeverityForSelector(0));
		assertEquals("severities match", SyslogConstants.INFO,
			selector.getSeverityForSelector(1));
		assertEquals("severities match", SyslogConstants.CRIT,
				selector.getSeverityForSelector(2));

		testForMatch(selector, "local5", "alert");
		testForMatch(selector, "kern", "alert");
		testForMatch(selector, "mail", "alert");

		testForMatch(selector, "ftp", "info");
		testForMatch(selector, "authpriv", "crit");
		
		testForNonMatch(selector, "ntp", "crit");
		testForNonMatch(selector, "news", "err");
	}
	
	public void testNoneSeverity () throws Exception
	{
		// exclude "kern" message but include all others
		Selector selector = new Selector("*.*;kern.none");
				
		assertEquals("number of selectors", 1, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				Integer.MAX_VALUE, selector.getFacilitiesForSelector(0)[0]);
		assertEquals("severities match", SyslogConstants.DEBUG,
			selector.getSeverityForSelector(0));

		assertEquals("size of 'none' facilities", 1,
				selector.getNoneFacilitySet().size());
		assertTrue("'none' facilities match", 
				selector.getNoneFacilitySet()
					.contains(new Integer(SyslogConstants.KERNEL)));

		Random random = new Random(System.currentTimeMillis());
		
		int facility;
		int severity;
		
		// test for matches - everything should match except "kern" messages
		for (int i = 0; i < 100; i++) {

			while ((facility = 
						random.nextInt(SyslogConstants.NUMBER_OF_FACILITIES))
						== SyslogConstants.KERNEL)
			{
				// keep looking;
				continue;
			}
					
			severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
			
			testForMatch(selector, SyslogConstants.getFacilityNameById(facility),
					 SyslogConstants.getSeverityNameById(severity));
		}			

		// test for non-matches - no "kern" messages should match
		facility = SyslogConstants.KERNEL;
		
		for (int i = 0; i < 100; i++) {
				
			severity = random.nextInt(SyslogConstants.NUMBER_OF_SEVERITIES);
			
			testForNonMatch(selector, SyslogConstants.getFacilityNameById(facility),
					 SyslogConstants.getSeverityNameById(severity));
		}					
	}
	
	public void testSelectorMiscellaneous () throws Exception
	{
		Selector selector = new Selector("mail.emerg;*.crit");
		
		assertEquals("number of selectors", 2, selector.getNumberOfSelectors());
		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(0).length);
		assertEquals("facilities match",
				SyslogConstants.MAIL, selector.getFacilitiesForSelector(0)[0]);
		assertEquals("severities match", SyslogConstants.EMERG,
			selector.getSeverityForSelector(0));

		assertEquals("number of facilities", 1,
			selector.getFacilitiesForSelector(1).length);
		assertEquals("facilities match",
				Integer.MAX_VALUE, selector.getFacilitiesForSelector(1)[0]);
		assertEquals("severities match", SyslogConstants.CRIT,
			selector.getSeverityForSelector(1));
			
		// throw some messages with each fac
		testForMatch(selector, "kern", "emerg");  // should match since emerg higher than crit
		testForMatch(selector, "mail", "emerg");				
	}
	
	private void testForMatch(Selector selector, String facilityName,
			String severityName) {
		int facility;
		int severity;
		facility = SyslogConstants.getFacilityIdByName(facilityName);
		severity = SyslogConstants.getSeverityIdByName(severityName);
		
		this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
							REST_OF_SYSLOG_MESSSAGE);
					
		assertTrue("match " + facility + "-" + severity,
					selector.isMatch(new SyslogMessage(this.queueEntry)));
	}
	
	private void testForNonMatch(Selector selector, String facilityName,
			String severityName) {
		int facility;
		int severity;
		facility = SyslogConstants.getFacilityIdByName(facilityName);
		severity = SyslogConstants.getSeverityIdByName(severityName);
		
		this.queueEntry.setRawMessage(constructPRIString(facility, severity) + 
							REST_OF_SYSLOG_MESSSAGE);
					
		assertFalse("non-match " + facility + "-" + severity,
					selector.isMatch(new SyslogMessage(this.queueEntry)));
	}
	
}
