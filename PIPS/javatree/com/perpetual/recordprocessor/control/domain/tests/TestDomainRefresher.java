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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.perpetual.recordprocessor.control.domain.DomainData;
import com.perpetual.recordprocessor.control.domain.DomainRefresher;
import com.perpetual.recordprocessor.control.domain.IDomainChangeObservable;
import com.perpetual.recordprocessor.control.domain.IDomainChangeObserver;
import com.perpetual.recordprocessor.control.domain.IDomainLoader;
import com.perpetual.recordprocessor.control.domain.IDomainMapHolder;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;

/**
 * @author simon
 *
 */
public class TestDomainRefresher extends TestCase {

	private class MyObservable implements IDomainChangeObservable
	{
		Map domainChangeObservers = null;
		
		public MyObservable () {
			this.domainChangeObservers = new HashMap();	
		}
		
		public void addObserver (IDomainChangeObserver observer, int eventType)
		{
			Integer event = new Integer(eventType);
			List observerList = null;
		
			if ((observerList = (List) this.domainChangeObservers
					.get(event)) == null) {
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
	}
	
	private class MyObserver implements IDomainChangeObserver
	{
		Map changeMap = null;
		
		public MyObserver ()
		{
			this.changeMap = new HashMap();
		}
		
		// fill up the change map
		public void notify (DomainData domainData, int eventType)
		{
			List domainDataList = null;
			
			System.out.println("got change: change type = "
					+ (eventType == IDomainChangeObserver.ADD ? "ADD"
							: (eventType == IDomainChangeObserver.CHANGE ? "CHANGE" : "DELETE"))
					+ " domain id = " + domainData.getDomainVO().getId());
			
			if ((domainDataList = (List) this.changeMap.get(new Integer(eventType)))
					== null) {
				domainDataList = new ArrayList();
				domainDataList.add(domainData);
				this.changeMap.put(new Integer(eventType), domainDataList);
			} else {
				domainDataList.add(domainData);
			}
			 
		}
		
		public Map getChangeMap() {
			return changeMap;
		}
		
		public void resetChangeMap() {
			this.changeMap.clear();
		}
	}
	
	private class MyDomainLoader implements IDomainLoader, IDomainMapHolder
	{
		private Map domainMap = new HashMap();
		private Map oldDomainMap = new HashMap();
		
		public Map loadDomainInformation () throws Throwable
		{
			return domainMap;
		}
		
		public Map getDomainMap() {
			return oldDomainMap;
		}
	
		public void setDomainMap(Map map) {
			oldDomainMap = domainMap;
			domainMap = map;
		}
	}

	/**
	 * Constructor for TestDomainRefresher.
	 * @param arg0
	 */
	public TestDomainRefresher(String arg0) {
		super(arg0);
	}

	public void testRefresherAllAdds () throws Exception
	{
		MyDomainLoader loader = new MyDomainLoader();
		MyObserver observer = new MyObserver();
		MyObservable observable = new MyObservable();
		
		observable.addObserver(observer, IDomainChangeObserver.ADD);
		observable.addObserver(observer, IDomainChangeObserver.CHANGE);
		observable.addObserver(observer, IDomainChangeObserver.DELETE);
		
		DomainRefresher domainRefresher = new DomainRefresher(500,
				loader, observable, loader);

		loader.setDomainMap(new HashMap());
		
		domainRefresher.start();
		
		Thread.sleep(1000);
		
		// no changes - should have a blank change map
		assertEquals("no changes", 0, observer.getChangeMap().size());		
		
		// create a map with 10 domains
		Map domainMap1 = createDomainMap(10);
		
		loader.setDomainMap(domainMap1);
		
		Thread.sleep(1000);
		
		domainRefresher.shutdown();
		
		// now we should have 10 changes - all ADDs
		
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all ADDs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.ADD)));
				
		List addList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.ADD));
		
		assertEquals("got the right number of changes", 10, addList.size());		
	}
	
	public void testRefresherAllDeletes () throws Exception
	{
		MyDomainLoader loader = new MyDomainLoader();
		MyObserver observer = new MyObserver();
		MyObservable observable = new MyObservable();
		
		observable.addObserver(observer, IDomainChangeObserver.ADD);
		observable.addObserver(observer, IDomainChangeObserver.CHANGE);
		observable.addObserver(observer, IDomainChangeObserver.DELETE);
		
		DomainRefresher domainRefresher = new DomainRefresher(500,
				loader, observable, loader);

		// start off with 10 domains
		loader.setDomainMap(createDomainMap(10));
		
		domainRefresher.start();
		
		Thread.sleep(1000);

		// now we should have 10 changes - all ADDs
		
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all ADDs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.ADD)));
				
		List addList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.ADD));
		
		assertEquals("got the right number of changes", 10, addList.size());
		
		// now empty domains
		observer.resetChangeMap();
		
		loader.setDomainMap(new HashMap());
		
		Thread.sleep(500);	
		
		// now we should have 10 changes - all DELETEs
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all DELETEs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.DELETE)));
				
		List delList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.DELETE));
		
		assertEquals("got the right number of changes", 10, delList.size());
	}
	
	public void testRefresherAllChanges () throws Exception
	{
		MyDomainLoader loader = new MyDomainLoader();
		MyObserver observer = new MyObserver();
		MyObservable observable = new MyObservable();
		
		observable.addObserver(observer, IDomainChangeObserver.ADD);
		observable.addObserver(observer, IDomainChangeObserver.CHANGE);
		observable.addObserver(observer, IDomainChangeObserver.DELETE);
		
		DomainRefresher domainRefresher = new DomainRefresher(500,
				loader, observable, loader);

		// start off with 10 domains
		loader.setDomainMap(createDomainMap(10));
		
		domainRefresher.start();
		
		Thread.sleep(1500);

		// now we should have 10 changes - all ADDs
		
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all ADDs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.ADD)));
				
		List addList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.ADD));
		
		assertEquals("got the right number of changes", 10, addList.size());
		
		observer.resetChangeMap();
		
		// now made some changes
		Map domainMap = createDomainMap(10);
		// for first Domain in map, change host names
		DomainData data1 = (DomainData) domainMap.get(new Integer(9));		

		for (Iterator i = data1.getHosts().iterator(); i.hasNext(); ) {
			HostVO hostVO = (HostVO) i.next();
			hostVO.setName(hostVO.getName() + "_X");
		}

		// for second Domain in map - delete a host
		DomainData data2 = (DomainData) domainMap.get(new Integer(1));		

		HostVO deleteHost = null;
		
		for (Iterator i = data2.getHosts().iterator(); i.hasNext(); ) {
			deleteHost = (HostVO) i.next();
			break;
		}
		data2.getHosts().remove(deleteHost);

		// for third Domain in map - delete a severity
		DomainData data3 = (DomainData) domainMap.get(new Integer(2));		

		SeverityVO deleteSeverity = null;
		
		for (Iterator i = data3.getSeverities().iterator(); i.hasNext(); ) {
			deleteSeverity = (SeverityVO) i.next();
			break;
		}
		data3.getSeverities().remove(deleteSeverity);
		
		// for fourth Domain in map - delete a facility
		DomainData data4 = (DomainData) domainMap.get(new Integer(3));		

		FacilityVO deleteFacility = null;
		
		for (Iterator i = data4.getFacilities().iterator(); i.hasNext(); ) {
			deleteFacility = (FacilityVO) i.next();
			break;
		}
		data4.getFacilities().remove(deleteFacility);
	
		loader.setDomainMap(domainMap);
	
		Thread.sleep(2000);
		domainRefresher.shutdown();
		
		// now we should have 4 changes - all CHANGESs
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all CHANGEs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.CHANGE)));
				
		List changeList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.CHANGE));
		
		assertEquals("got the right number of changes", 4, changeList.size());
	}
	
	public void testSomeOfEach () throws Exception
	{
		MyDomainLoader loader = new MyDomainLoader();
		MyObserver observer = new MyObserver();
		MyObservable observable = new MyObservable();
		
		observable.addObserver(observer, IDomainChangeObserver.ADD);
		observable.addObserver(observer, IDomainChangeObserver.CHANGE);
		observable.addObserver(observer, IDomainChangeObserver.DELETE);
		
		DomainRefresher domainRefresher = new DomainRefresher(500,
				loader, observable, loader);

		// start off with 10 domains
		loader.setDomainMap(createDomainMap(20));
		
		domainRefresher.start();
		
		Thread.sleep(1000);

		// now we should have 10 changes - all ADDs
		
		assertEquals("got some changes", 1, observer.getChangeMap().size());
		assertNotNull("changes are all ADDs",
				observer.getChangeMap().get(new Integer(IDomainChangeObserver.ADD)));
				
		List addList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.ADD));
		
		assertEquals("got the right number of changes", 20, addList.size());
		
		observer.resetChangeMap();
System.out.println("reset change map");		
		// now made some changes
		Map domainMap = createDomainMap(20);
		
		// remove a Domain		
		domainMap.remove(new Integer(9));

		// for second Domain in map - make a change - add a host
		DomainData data2 = (DomainData) domainMap.get(new Integer(1));		
		data2.getHosts().add(new HostVO(100, "myhost", ""));
		
	
		// for third Domain in map - make a change - delete a severity
		DomainData data3 = (DomainData) domainMap.get(new Integer(2));		

		SeverityVO deleteSeverity = null;
		
		for (Iterator i = data3.getSeverities().iterator(); i.hasNext(); ) {
			deleteSeverity = (SeverityVO) i.next();
			break;
		}
		data3.getSeverities().remove(deleteSeverity);
		
		// for fourth Domain in map - make a change - delete a facility
		DomainData data4 = (DomainData) domainMap.get(new Integer(3));		

		FacilityVO deleteFacility = null;
		
		for (Iterator i = data4.getFacilities().iterator(); i.hasNext(); ) {
			deleteFacility = (FacilityVO) i.next();
			break;
		}
		data4.getFacilities().remove(deleteFacility);
		
		// add a couple of new Domains
		
		domainMap.put(new Integer(999), new DomainData(new DomainVO(999, ""),
				new ArrayList(), new ArrayList(),
				new ArrayList(), new ArrayList()));

		domainMap.put(new Integer(1000), new DomainData(new DomainVO(1000, ""),
				new ArrayList(), new ArrayList(),
				new ArrayList(), new ArrayList()));


		loader.setDomainMap(domainMap);
	
		Thread.sleep(1000);
		domainRefresher.shutdown();
		
		// now we should have 4 changes - all CHANGESs
		assertEquals("got all types of changes", 3, observer.getChangeMap().size());

		List changeList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.CHANGE));
		
		assertNotNull("got some CHANGEs", changeList);
		assertEquals("number of changes", 3, changeList.size());
		
		addList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.ADD));
		
		assertNotNull("got some ADDs", addList);
		assertEquals("number of adds", 2, addList.size());		
				
		List delList = (List) observer.getChangeMap()
				.get(new Integer(IDomainChangeObserver.DELETE));
		
		assertNotNull("got some DELETEs", delList);
		assertEquals("number of changes", 1, delList.size());
//				

//		
//		assertEquals("got the right number of changes", 4, changeList.size());
	}
	
	
	
	private Map createDomainMap (int numberOfDomains)
	{
		Map result = new HashMap();
			
		for (int i = 0; i < numberOfDomains; i++) {
			DomainData domainData = createDomainData(i, i, 2*i, 3*i);
			result.put(new Integer(domainData.getDomainVO().getId()), domainData);
		}
		
		return result;			
	}
	
	private DomainData createDomainData (int domainId, int numberOfHosts,
			int numberOfSeverities, int numberOfFacilties)
	{
		DomainVO dvo = new DomainVO(domainId, "domain");

		// create hosts
		List hosts = new ArrayList();
		for (int i = 0; i < numberOfHosts; i++) {
			HostVO hostVO = new HostVO(-i, "host" + i, "");
			hosts.add(hostVO);
		}

		// create severities
		List severities = new ArrayList();
		for (int i = 0; i < numberOfSeverities; i++) {
			SeverityVO severityVO = new SeverityVO(i, "Severity" + i);
			severities.add(severityVO);
		}

		// create facilities
		List facilities = new ArrayList();
		for (int i = 0; i < numberOfFacilties; i++) {
			FacilityVO facilityVO = new FacilityVO(i, "Facility" + i);
			facilities.add(facilityVO);
		}
		
		// create patterns
		List patterns = new ArrayList();
		for (int i = 0; i < numberOfFacilties; i++) {
			MessagePatternData pattern = new MessagePatternData(new Integer(i),
					"PatternName" + i, "pattern" + i, "description");
			patterns.add(pattern);
		}
		
		return new DomainData (dvo, hosts, facilities, severities, patterns);	
	}
}
