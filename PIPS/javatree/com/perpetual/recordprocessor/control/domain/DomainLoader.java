/*
 * Created on 29-Aug-2003
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.model.vo.DomainVO;

/**
 * @author simon
 *
 */
public class DomainLoader implements IDomainLoader {
	private PerpetualC2Logger sLog = new PerpetualC2Logger(DomainManager.class);
	
	private DomainCRUDHome domainCRUDHome;
	
	public DomainLoader (DomainCRUDHome domainCRUDHome)
	{
		this.domainCRUDHome = domainCRUDHome;
	}
	
	// load domain information and associated hosts, severities and facilities 
	 public Map loadDomainInformation ()
			throws Throwable
	 {
		 Map domainMap = new HashMap();
		
		 DomainCRUD domainCRUD = this.domainCRUDHome.create();
		
		 try {
			 Collection domains = domainCRUD.retrieveAll();
			
			 sLog.debug("retrieved " + domains.size() + " domains.");
			
			 for (Iterator i = domains.iterator(); i.hasNext(); ) {
				 DomainVO dvo = (DomainVO) i.next();
				
				 if (dvo.getSummaryProcessingEnabled()) {
					 sLog.debug("Domain '" + dvo.getName() +
							 "' is enabled for summary processing.");
										
					 // get the severities, facilities and hosts
					
					 Collection hosts = null;
					 Collection facilities = null;
					 Collection severities = null;
					 Collection messagePatterns = null;
			
					 severities = domainCRUD.retrieveAllSeveritiesFor(dvo);
					 facilities = domainCRUD.retrieveAllFacilitiesFor(dvo);
					 hosts = domainCRUD.retrieveAllHostsFor(dvo);
					 messagePatterns = domainCRUD.retrieveAllMessagePatternsFor(dvo);

sLog.debug("got " + messagePatterns.size() +  " message patterns");
			
					 DomainData domainData = new DomainData(dvo, hosts,
							 facilities, severities, messagePatterns);	
			
					 domainMap.put(new Integer(dvo.getId()), domainData);						
							
				 } else {
					 sLog.debug("Domain '" + dvo.getName()
							 + "' is not enabled for summary processing - skipping.");
				 }
			 }
		 } finally {
			 domainCRUD.remove();
		 }
		
		 return domainMap;
	 }

}
