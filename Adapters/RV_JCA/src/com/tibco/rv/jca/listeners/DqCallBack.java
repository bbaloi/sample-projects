/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Logger;

import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvCmListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvCmMsg;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvCmQueueTransport;
import com.tibco.tibrv.TibrvException;

import com.tibco.rv.jca.AbstractCallBack;
import com.tibco.rv.jca.ra.RVListenerManager;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DqCallBack extends AbstractCallBack
{
	private boolean active;
	
	public DqCallBack(String pServiceName,RVListenerManager pListenerMgr)
	{
		super(pServiceName,pListenerMgr);
		serviceName=pServiceName;
		
	}
	 protected void init()
	 {		
		className = DqCallBack.class.getName();
		
	    	try 
	    	{
	            sLogger = Logger.getLogger(className, rbName);
	        } 
	    	catch (Exception e)
	        {
	            // Problem initializing logging.  Continue anyway.
	        } 
		
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
	    sLogger.log(Level.INFO,"DQ Thread - Received message from: "+pMsg.getSendSubject());
	     	     
		sendMsgToEndpoint(pMsg);
	   
	  }
	protected TibrvMsg enhanceMsg(TibrvMsg pMsg)
	{
		TibrvMsg retMsg=pMsg;
		//do any additional data formating in the message - as needed
		return retMsg;
		
	}
	
}
