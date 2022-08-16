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

import java.util.Iterator;
import java.util.Map;

import com.perpetual.util.PerpetualC2Logger;

/**
 * @author simon
 *
 */
public class DomainRefresher extends Thread {
	private PerpetualC2Logger sLog = new PerpetualC2Logger(DomainManager.class);
	
	long refreshInterval;
	IDomainMapHolder domainMapHolder = null;
	IDomainLoader domainLoader = null;
	IDomainChangeObservable observable;
	Thread currentThread = null;
	boolean running = true;
	
	public DomainRefresher (long refreshInterval,
				IDomainMapHolder domainMapHolder,
				IDomainChangeObservable domainChangeObservable,
				IDomainLoader domainLoader)
	{
		this.refreshInterval = refreshInterval;
		this.domainMapHolder = domainMapHolder;
		this.observable = domainChangeObservable;
		this.domainLoader = domainLoader;
		this.running = true;
	}
	
	public void run () {
		sLog.info("Starting domain refresher, refresh interval = "
				+ this.refreshInterval);
		
		this.currentThread = Thread.currentThread();
		
		// this has to load up domain info
		// compare it to current Domain map
		// and issue event changes back to the manager
		while (running) {
			try {
				try {
					Thread.sleep(this.refreshInterval);

					Map newDomainMap = this.domainLoader.loadDomainInformation();
				
					compareMapsAndIssueEvents(this.domainMapHolder.getDomainMap(),
							newDomainMap);
	
					// set the old to new
					this.domainMapHolder.setDomainMap(newDomainMap);

				} catch (InterruptedException e) {
				}
				
			} catch (Throwable e) {
				sLog.error("Cannot re-load domain information:" + e);
			}
		}
	}
	
	private void compareMapsAndIssueEvents (Map oldDomainMap, Map newDomainMap)
	{
		// compare maps
		// for each difference fire events back to observers
		// this.observable.notifyAllObservers(domainData, eventType);
		
		// walk through new map 
		
		//System.out.println("Comparing maps, old.size = " + oldDomainMap.size()
	//			+ ", new.size = " + newDomainMap.size());
	
		sLog.debug("Comparing maps, old size = " + oldDomainMap.size()
				+ ", new.size = " + newDomainMap.size());

		for (Iterator i = newDomainMap.keySet().iterator(); i.hasNext(); ) {
			Integer domainId = (Integer) i.next();
			DomainData newDomainData = (DomainData) newDomainMap.get(domainId);
			DomainData oldDomainData;
			
			if ((oldDomainData = (DomainData) oldDomainMap.get(domainId)) != null) {
				
				if (newDomainData.compareTo(oldDomainData) != 0) {
					// there is a difference in old and new domains
					this.observable.notifyAllObservers(newDomainData,
							IDomainChangeObserver.CHANGE);
				} 
			} else {
				// not in old map - we have newly added domain
				this.observable.notifyAllObservers(newDomainData,
						IDomainChangeObserver.ADD); 
			}
		}
		
		// walk through old map and see if there are missing domains
		
		for (Iterator i = oldDomainMap.keySet().iterator(); i.hasNext(); ) {
			Integer domainId = (Integer) i.next();
			DomainData oldDomainData = (DomainData) oldDomainMap.get(domainId);
			
			if (newDomainMap.get(domainId) == null) {
				// old domain no longer exists - must have been deleted
				this.observable.notifyAllObservers(oldDomainData,
						IDomainChangeObserver.DELETE);
			}
		}
	}
	
	public void shutdown()
	{
		this.running = false;
		if (this.currentThread != null) {
			this.currentThread.interrupt();	
		}
	}
}
