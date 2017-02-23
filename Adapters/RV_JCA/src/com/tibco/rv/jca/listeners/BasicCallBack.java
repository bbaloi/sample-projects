/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.tibrv.TibrvFtMemberCallback;
import com.tibco.tibrv.TibrvFtMember;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.rv.jca.ICallBack;
import com.tibco.rv.jca.ra.BaseRVListenerManager;
import com.tibco.rv.jca.ra.RVInboundResourceAdapter;
import com.tibco.rv.jca.ra.RVListenerManager;


import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;

import com.tibco.rv.jca.AbstractCallBack;
import com.tibco.rv.jca.ra.RVListenerManager;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BasicCallBack extends AbstractCallBack
{
		
	public BasicCallBack(String pServiceName,RVListenerManager pListenerMgr)
	{
		super(pServiceName,pListenerMgr);		
	}
	
	 protected void init()
	 {		
		className = BasicCallBack.class.getName();
		
	    	try 
	    	{
	            sLogger = Logger.getLogger(className, rbName);
	        } 
	    	catch (Exception e)
	        {
	            // Problem initializing logging.  Continue anyway.
	        } 
		
	 }


}
