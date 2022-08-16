
/*
 * RecordProcessorManager.java
 *
 * Created on July 17, 2003, 10:23 PM
 */

package com.perpetual.recordprocessor.control;

import java.util.Iterator;
import java.util.Map;

import javax.rmi.PortableRemoteObject;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.recordprocessor.control.domain.DomainData;
import com.perpetual.recordprocessor.control.domain.DomainManager;
import com.perpetual.recordprocessor.control.domain.IDomainChangeObserver;
import com.perpetual.recordprocessor.control.domain.IDomainMapHolder;
import com.perpetual.rp.util.RPConstants;
import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.util.threadpool.IThreadManager;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;

/**
 *
 * @author  brunob
 */
public final class RecordProcessorManager
		implements IDomainMapHolder, IDomainChangeObserver
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( RecordProcessorManager.class );    
    
    INavigator lNavigator=null;
    RPConfigManager lConfigMgr=null;
    IThreadManager lThreadPoolMgr=null;
    private Map domainMap;
    private DomainManager domainManager;
	private DomainCRUDHome domainCRUDHome = null;

    
    /** Creates a new instance of RecordProcessorManager */
    public RecordProcessorManager(IThreadManager pThreadPoolMgr,
    					RPConfigManager pConfigMgr,
                        INavigator pNavigator) throws Throwable
    {
        lNavigator=pNavigator;
        lConfigMgr = pConfigMgr;
        lThreadPoolMgr = pThreadPoolMgr;
        
        init();
    }
    
    private void init() throws Throwable
    {
        sLog.debug("RecordProcessorManager - initialising Threads With Domains !");

		loadHomes();
		this.domainManager = new DomainManager(this.domainCRUDHome,
				RPConstants.RefreshInterval);
		this.domainManager.addObserver(this, IDomainChangeObserver.ADD);
		this.domainManager.addObserver(this, IDomainChangeObserver.DELETE);
		
		this.domainManager.start();
		
        //for every DOmain in the Domain Manager :
        //1) strip out a Domain entity - construct a DomainMap
        if(lThreadPoolMgr!=null)
        {
            Map domainMap = this.domainManager.getDomainMap();
            
            for (Iterator i = domainMap.keySet().iterator(); i.hasNext();) {

                Integer domainId = (Integer) i.next();
                DomainData domainData = (DomainData) domainMap.get(domainId);

				Map counterMap;
				RecordProcessor rp;
				
				startUpProcessingThread(domainData);
            }
        }
        
        sLog.debug("Finished Initialising the Record Porcessor Manger !");   
    }

	private void startUpProcessingThread(DomainData domainData) {   
		 
		//2) Censtruct a new SyslogRecorProcessor load passing it its map
		RecordProcessor rp = new SyslogRecordProcessor(domainData, this, lNavigator);
		
		this.domainManager.addObserver((IDomainChangeObserver) rp,
				IDomainChangeObserver.CHANGE);
				
		//3) addNewThread to Thread pool Manager.....
		try
		{
		     lThreadPoolMgr.addNewThread(
		     		String.valueOf(domainData.getDomainVO().getId()), rp);
		}
		catch(BasePerpetualException excp)
		{
		    sLog.error("Could not Add A new Record Processor Thread !");
		 	excp.printStackTrace();
		}
	}
 
 
    
	public void notify (DomainData domainData, int eventType)
	{
		String event = 	(eventType == IDomainChangeObserver.ADD ? "ADD"
				: (eventType == IDomainChangeObserver.CHANGE ? "CHANGE" : "DELETE"));
				
		sLog.info("Got a domain " + event + " notification from DB, domain ID = "
			// changed by michael@perpetual-ip.com 09/29/2003
			// reason: getId does not give the proper visual information
				+ domainData.getDomainVO().getName());
		
		switch (eventType) {
			case IDomainChangeObserver.ADD:
				addNewDomain(domainData);
				break;
			case IDomainChangeObserver.DELETE:
				deleteDomain(domainData);
				break;
			case IDomainChangeObserver.CHANGE:
				changeDomain(domainData);
				break;		
		}
	}
	
	private void addNewDomain (DomainData domainData) 
	{
		startUpProcessingThread(domainData);
	}
	
	private void deleteDomain (DomainData domainData)
	{
		// find domain and remove from pool 
		sLog.info("Shutting down processing for domain "
			+ domainData.getDomainVO().getName());
			
		try {
			lThreadPoolMgr.shutdownThread(
				String.valueOf(domainData.getDomainVO().getId()));	
		} catch (BasePerpetualException e) {
			sLog.error("Error closing down processing thread for "
			// changed by michael@perpetual-ip.com 09/29/2012
			// reason: getId does not give proper visual information
				+ domainData.getDomainVO().getName());			
		}
	}
	
	// let each thread handle its own change
	private void changeDomain (DomainData domainData)
	{
	}
	
	
	  
	private void loadHomes () throws Exception
	{
		Object homeObject = ServiceLocator.findHome(Constants.jndiName_DomainCRUD);
				
		sLog.debug("Got domain home object");
		
		this.domainCRUDHome = (DomainCRUDHome) PortableRemoteObject
				.narrow(homeObject, DomainCRUDHome.class); 			
	}
	
	/**
	 * @return
	 */
	public DomainCRUDHome getDomainCRUDHome() {
		return domainCRUDHome;
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
