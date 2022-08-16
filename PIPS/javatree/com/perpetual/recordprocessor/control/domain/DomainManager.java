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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;

/**
 * @author simon
 *
 */
public class DomainManager implements IDomainMapHolder, IDomainChangeObservable {
	
	private PerpetualC2Logger sLog = new PerpetualC2Logger(DomainManager.class);
	
	private Map domainMap;
	private DomainCRUDHome domainCRUDHome;
	// Map of Lists of observers indexed by event type
	private Map domainChangeObservers = null;
	private DomainLoader domainLoader = null;
	private DomainRefresher domainRefresher = null;
	
	public DomainManager (DomainCRUDHome domainCRUDHome,
			long refreshInterval) throws Throwable
	{
		this.domainCRUDHome = domainCRUDHome;
		this.domainChangeObservers = new HashMap();
		this.domainLoader = new DomainLoader(this.domainCRUDHome);
		this.domainRefresher = new DomainRefresher(refreshInterval, 
				this, this, new DomainLoader(domainCRUDHome));
	}
		
	public void start () throws Throwable
	{
		this.domainMap = this.domainLoader.loadDomainInformation();
		
		startDomainRefreshThread();
	}
	
	private void startDomainRefreshThread () 
	{
		this.domainRefresher.start();
	}
	
	public void addObserver (IDomainChangeObserver observer, int eventType)
	{
		Integer event = new Integer(eventType);
		List observerList = null;
		
		if ((observerList = (List) this.domainChangeObservers.get(event)) == null) {
			// first observer of a certain event type
			observerList = new ArrayList();
			observerList.add(observer);
			this.domainChangeObservers.put(event, observerList);
		} else {
			observerList.add(observer);
		}
	}
	
	public void removeObserver (IDomainChangeObserver observer)
	{
		for (Iterator i = this.domainChangeObservers.keySet().iterator();
					i.hasNext(); ) {
						
			List observerList = (List) this.domainChangeObservers.get(i.next());			
			observerList.remove(observer);
		}
	}
	
	public void notifyAllObservers (DomainData domainData, int eventType)
	{
		List observerList = (List) this.domainChangeObservers.get(
				new Integer(eventType));
				
		if (observerList != null) {
			for (Iterator i = observerList.iterator(); i.hasNext(); ) {
				IDomainChangeObserver observer = (IDomainChangeObserver) i.next();
				
				observer.notify(domainData, eventType);		
			}
		}
	}
	


	/**
	 * @return
	 */
	public Map getDomainMap() {
		return domainMap;
	}

	/**
	 * @param map
	 */
	public void setDomainMap(Map map) {
		domainMap = map;
	}

}
