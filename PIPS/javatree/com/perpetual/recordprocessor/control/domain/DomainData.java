/*
 * Created on 27-Aug-2003
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
package com.perpetual.recordprocessor.control.domain;

import java.util.Collection;
import java.util.Iterator;

import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.HostVO;

/**
 * @author simon
 *
 * Convenience class to hold Domain Data.
 */
public class DomainData
{
	private DomainVO domainVO;
	private Collection hosts;			// List of hostVOs
	private Collection severities;	// List of severityVOs
	private Collection facilities;	// List of facilityVOs
	private Collection messagePatterns;	// List of MessagePatternData (which is a VO by another name) 
	
	public DomainData (DomainVO domainVO, Collection hosts,
			Collection facilities, Collection severities, 
			Collection messagePatterns)
	{
		this.domainVO = domainVO;
		this.hosts = hosts;
		this.facilities = facilities;
		this.severities = severities;
		this.messagePatterns = messagePatterns;	
	}
	
	public boolean equals(Object pObject)
	{
		if( pObject instanceof DomainData && this.domainVO != null) {
			 
			return this.domainVO.getId() == ((DomainVO) pObject).getId();
	 	}

	 	return( false );
	}
	/**
	 * @return
	 */
	public DomainVO getDomainVO() {
		return domainVO;
	}

	/**
	 * @return
	 */
	public Collection getFacilities() {
		return facilities;
	}

	/**
	 * @return
	 */
	public Collection getHosts() {
		return hosts;
	}

	/**
	 * @return
	 */
	public Collection getSeverities() {
		return severities;
	}

	/**
	 * @param domainVO
	 */
	public void setDomainVO(DomainVO domainVO) {
		this.domainVO = domainVO;
	}

	public Collection getMessagePatterns() {
		return messagePatterns;
	}

	public int compareTo (Object o)
	{
		int result = 1;  // not equal

try {		

		if (o instanceof DomainData) {
			DomainData otherDomainData = (DomainData) o;
			
			if (vosAreEqual(getDomainVO(), otherDomainData.getDomainVO())
							&& facilitiesAreEqual(getFacilities(),
									otherDomainData.getFacilities())
							&& severitiesAreEqual(getSeverities(),
									otherDomainData.getSeverities())
							&& hostsAreEqual(getHosts(),
									otherDomainData.getHosts())
							&& messagePatternsAreEqual(getMessagePatterns(),
									otherDomainData.getMessagePatterns())) {
					result = 0;
				}
		}
} catch (NullPointerException n) {
	n.printStackTrace();	
}
		return result;
	}
	
	private boolean vosAreEqual (DomainVO dvo1, DomainVO dvo2)
	{
		// do field comparision - ignore last collection time
		return (dvo1.getId() == dvo2.getId()
			&& ((dvo1.getCollectionInterval() == null
				&& dvo2.getCollectionInterval() == null) ||
				 (dvo1.getCollectionInterval() != null &&
				 dvo1.getCollectionInterval().equals(dvo2.getCollectionInterval())))
			&& dvo1.getName().equals(dvo2.getName()));
	}
	
	
	private boolean facilitiesAreEqual (Collection facilities1,
			Collection facilities2)
	{
		// check for mutual containment
		return facilities1.containsAll(facilities2) &&
					facilities2.containsAll(facilities1);
	}
	
	private boolean severitiesAreEqual (Collection severities1,
			Collection severities2)
	{		
		// check for mutual containment
		return severities1.containsAll(severities2) &&
				severities2.containsAll(severities1);
	}
	
	// this one's a little different since we need to look at the
	// host name as well as ids
	private boolean hostsAreEqual (Collection hosts1, Collection hosts2)
	{
		boolean result = true;

		// check for mutual containment and host names match
		if (hosts1.containsAll(hosts2) && hosts2.containsAll(hosts1)) {
			for (Iterator i = hosts1.iterator(); result && i.hasNext(); ) {
				HostVO hostVO1 = (HostVO) i.next();
				
				HostVO hostVO2 = getHostVOFor(hostVO1.getId(), hosts2);
				
				if (hostVO2 != null) {
					result = hostVO1.getName().equals(hostVO2.getName());
				} else {
					result = false;
				}
				
			}
		} else {
			result = false;
		}
		
		return result;
	}

	private HostVO getHostVOFor (int id, Collection hosts)
	{
		boolean found = false;
		HostVO result = null;
		
		for (Iterator i = hosts.iterator(); !found && i.hasNext(); ) {
			HostVO hostVO = (HostVO) i.next();
			
			if (id == hostVO.getId()) {
				found = true;
				result = hostVO;
			}
		}
		
		return result;
	}
	
	// this one's a little different since we need to look at the
	// host name as well as ids
	private boolean messagePatternsAreEqual (Collection messagePatterns1,
			Collection messagePatterns2)
	{
		boolean result = true;

//System.out.println("Comparing pattern1 = " + messagePatterns1);
//System.out.println("Comparing pattern1 = " + messagePatterns2);

		// check for mutual containment and host names match
		if (messagePatterns1.containsAll(messagePatterns2)
				&& messagePatterns2.containsAll(messagePatterns1)) {
			for (Iterator i = messagePatterns1.iterator(); result && i.hasNext(); ) {
				MessagePatternData pattern1 = (MessagePatternData) i.next();
				
				MessagePatternData pattern2 = getMessagePatternDataFor(
						pattern1.getId().intValue(), messagePatterns2);
				
				if (pattern2 != null) {
					result = pattern1.getPattern().equals(pattern2.getPattern());
				} else {
					result = false;
				}
				
			}
		} else {
			result = false;
		}
		
		return result;
	}

	private MessagePatternData getMessagePatternDataFor (int id, Collection hosts)
	{
		boolean found = false;
		MessagePatternData result = null;
		
		for (Iterator i = hosts.iterator(); !found && i.hasNext(); ) {
			MessagePatternData pattern = (MessagePatternData) i.next();
			
			if (id == pattern.getId().intValue()) {
				found = true;
				result = pattern;
			}
		}
		
		return result;
	}
}