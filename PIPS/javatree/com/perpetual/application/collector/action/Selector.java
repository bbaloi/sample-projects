/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.application.collector.syslog.SyslogConstants;
import com.perpetual.exception.PerpetualException;
/**
 * @author simon
 *
 * Handle the parsing of message selector string.
 * 
 * The grammar for this format is:
 * 
 * 		facility := "kern" | "mail" | .. | "*"
 * 		severity := "emerg" | "alert" | "crit" | .. | "none"
 * 		facilities := facility | facility "," facilities
 * 		filter := facilities.severity
 * 		filters := filter | filter ";" filters
 * 
 * The various values for facilty and severity are in syslog(3C).
 * 
 */
public class Selector {
	private static final Logger LOGGER = Logger.getLogger(Selector.class);
	
	private class FacilitySeveritySelector {
		private int[] facilityMatch;
		private int severityMatch;
		
		public FacilitySeveritySelector (int[] facilityMatch, int severityMatch)
		{
			this.facilityMatch = facilityMatch;
			this.severityMatch = severityMatch;
		}
		
		public int[] getFacilityMatch() {
			return facilityMatch;
		}

		public int getSeverityMatch() {
			return severityMatch;
		}
	}

	private FacilitySeveritySelector selectorArray[];
	private Set noneFacilitySet;
	private BaseAction baseAction;
	private String selectorString;
	private List selectorSet;

	public BaseAction getBaseAction() {
		return baseAction;
	}

	public void setBaseAction(BaseAction baseAction) {
		this.baseAction = baseAction;
	}
	
	public boolean isMatch(SyslogMessage message) {
		boolean matched = false; 
		
		int facility = message.getFacility();
		int severity = message.getSeverity();
		
		// first check to see if facility was excluded due to "none" indicator
		if (this.noneFacilitySet.contains(new Integer(facility))) {
			matched = false;
		}
		else {		
			// loop through each selector and test for a match
			for (int i = 0; !matched && i < this.selectorArray.length; i++) {
	
				FacilitySeveritySelector selector = this.selectorArray[i];
				
				matched = severityMatch(selector.getSeverityMatch(), severity)
					&& facilityMatch(selector.getFacilityMatch(), facility);
			}
		}
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("result of match (facility, severity) = ("
				+ facility + ", " + severity + ") to "
				+ this.selectorString + " is " + matched);
		}
				
		return matched;
	}

	public Selector(String selectorString) throws Exception {
		this.selectorString = selectorString;
		parse();
	}
	
	private boolean severityMatch (int severityMatch, int severity)
	{		
		return severity <= severityMatch;
	}
	
	private boolean facilityMatch (int[] facilityMatch, int facility)
	{
		boolean matched = false;
		
		for (int i = 0; !matched && i < facilityMatch.length; i++) {			
			matched = (facilityMatch[i] == Integer.MAX_VALUE) || facilityMatch[i] == facility;
		}
		
		return matched;
	}

	
	private void parse () throws Exception
	{
		// look for filters first - separated by ";"
		StringTokenizer selectors = new StringTokenizer(this.selectorString, ";");
		List selectorList = new ArrayList();
		this.noneFacilitySet = new HashSet();
		
		while (selectors.hasMoreTokens()) {
			String selector = selectors.nextToken();
			
			// now have basically facility.severity
			StringTokenizer facilitySeverity = new StringTokenizer(selector, ".");
			
			String facilityMatchString = facilitySeverity.nextToken();
			String severityMatchString = facilitySeverity.nextToken();
			
			// facility match is in form facility, facility, ...
			int facilityMatch[] = parseFacilityMatchString(facilityMatchString);
			int severityMatch = parseSeverityMatchString(severityMatchString);
			
			// handle "<facility>.none"
			if (severityMatch == -1) {
				for (int i = 0; i < facilityMatch.length; i++) {
					noneFacilitySet.add(new Integer(facilityMatch[i]));
				}
			} else {
				// add to selector list
				selectorList.add(new FacilitySeveritySelector(facilityMatch, severityMatch));
			}
		}

		// construct an array for matching purposes - will be better performance
		this.selectorArray = 
			new FacilitySeveritySelector[selectorList.size()];
		
		int j = 0;
		for (Iterator i = selectorList.iterator(); i.hasNext(); j++) {
			this.selectorArray[j] = (FacilitySeveritySelector) i.next();
		}
	}

	private int[] parseFacilityMatchString(String facilityMatchString)
			throws PerpetualException {
		
		List matchedFacilities = new ArrayList();
		StringTokenizer facilities = new StringTokenizer(facilityMatchString, ",");
		
		
		while (facilities.hasMoreTokens()) {
			String facilityName = facilities.nextToken();
			int facilityId;
			
			if ("*".equals(facilityName)) {
				facilityId = Integer.MAX_VALUE;		// match all
			} else {
				// look up against known facilities
				facilityId = SyslogConstants.getFacilityIdByName(facilityName);
						
				if (facilityId == -1) {
					// couldn't find a match
					throw new PerpetualException("facility '"
							+ facilityName + "' is not known.");	 
				}
			}
			
			matchedFacilities.add(new Integer(facilityId));
		}
		
		// construct an array for matching purposes - will be better performance
		int matchedFacilityArray[] = new int[matchedFacilities.size()];
		
		int j = 0;
		for (Iterator i = matchedFacilities.iterator(); i.hasNext(); j++) {
			matchedFacilityArray[j] = ((Integer) i.next()).intValue();
		}
		
		return matchedFacilityArray;
	}

	private int parseSeverityMatchString(String severityMatchString)
			throws PerpetualException {

		int severityId = -1;
		
		if ("none".equals(severityMatchString)) {
			severityId = -1;  // won't match anything
		}
		else if ("*".equals(severityMatchString)) {
			severityId = SyslogConstants.DEBUG;
		} else {
			severityId = SyslogConstants.getSeverityIdByName(severityMatchString);
			
			if (severityId == -1) {
				// couldn't find a match
				throw new PerpetualException("severity '"
						+ severityMatchString + "' is not known.");	 
			}
		}
		
		return severityId;
	}
	
	public int getNumberOfSelectors()
	{
		return this.selectorArray.length;
	}
	
	public int[] getFacilitiesForSelector(int selectorIndex)
	{
		return this.selectorArray[selectorIndex].getFacilityMatch();
	}
	
	public int getSeverityForSelector(int selectorIndex)
	{
		return this.selectorArray[selectorIndex].getSeverityMatch();
	}
	
	public Set getNoneFacilitySet() {
		return noneFacilitySet;
	}

}
