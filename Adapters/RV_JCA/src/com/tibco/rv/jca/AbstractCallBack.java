/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.tibrv.TibrvFtMemberCallback;
import com.tibco.tibrv.TibrvFtMember;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.rv.jca.ra.RVInboundResourceAdapter;


import javax.resource.spi.ActivationSpec;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import com.tibco.rv.jca.ra.RVListenerManager;

import com.tibco.rv.jca.exceptions.RV_JCAException;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractCallBack implements ICallBack
{
 	protected String className;	 	
	protected Logger sLogger;
    protected String rbName = null; // No localization is used for logging.
   	
	protected boolean active=false;
	protected String serviceName=null;
	protected TibrvRvdTransport transport=null;
	protected RVListenerManager listenerManager=null;
	protected RVListener lEndpoint=null;
		
	protected abstract void init();
	
	public AbstractCallBack(String pServiceName,RVListenerManager pListenerMgr)
	{
		serviceName=pServiceName;
		listenerManager = pListenerMgr;
		init();
	}
	public void activate()
	{
		active=true;
	}
	public void deactivate()
	{
		active=false;
	}
	
	public void setTransport(TibrvRvdTransport pTransport)
	{
		transport=pTransport;
	}
	
	 public void onMsg(TibrvListener listener, TibrvMsg pMsg)
	 {
	    sLogger.log(Level.INFO,"Basic-Thread-Received message from: "+pMsg.getSendSubject());
	     //put RV message on a internal queue	     
	    sendMsgToEndpoint(pMsg);
	   
	   
	  }
	protected TibrvMsg enhanceMsg(TibrvMsg pMsg)
	{
		TibrvMsg retMsg=pMsg;
		//do any additional data formating in the message - as needed
		return retMsg;
		
	}
	protected synchronized void sendMsgToEndpoint(TibrvMsg pMsg)
	{
		 
		//1. get EndpointFactory
	 
	 	//2. create Endpoint
	    //lEndpoint=listenerManager.getEndPoint();
	    if(lEndpoint==null)
	    {
	    	try
	    	{
	    		lEndpoint=listenerManager.createEndpoint();	    		
	    	}
	    	catch(RV_JCAException excp)
	    	{
	    		excp.printStackTrace();
	    	}
	    		
	    }	    
	    
	    	lEndpoint.onMessage(enhanceMsg(pMsg));
		
	 	//3. send Message to Endpoin & implicetly to MDB
	}
	
}


