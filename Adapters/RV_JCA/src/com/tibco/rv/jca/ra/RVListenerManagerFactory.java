/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.ra;

import java.util.HashMap;
import javax.resource.spi.ResourceAdapter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RVListenerManagerFactory
{
	private static RVListenerManagerFactory instance=null;
	
	private ArrayList mgrList = null;
	
	private RVListenerManagerFactory()
	{
		init();
	}
	
	public static RVListenerManagerFactory getInstance()
	{
		if(instance==null)
			instance = new RVListenerManagerFactory();
		return instance;
		
	}
	private void init()
	{
		mgrList = new ArrayList();
	}
	
	public RVListenerManager createBaseManager(RVInboundResourceAdapter pAdapter)
	{
		
		BaseRVListenerManager baseMgr = new BaseRVListenerManager(pAdapter);		
		mgrList.add(baseMgr);
		
		return baseMgr;
	}
	public void clean()
	{
		//remove all RVListenerManger instances from memory
		Iterator it = mgrList.iterator();
		while(it.hasNext())
		{
			RVListenerManager mgr = (RVListenerManager) it.next();
			mgr.stop();			
			it.remove();
		}
		
	}
	public RVListenerManager createAdvnacedManager(RVInboundResourceAdapter pAdapter)
	{
		//mgrList.add(advancedMgr);
		return null;
	}

}
