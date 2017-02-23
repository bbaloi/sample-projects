/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Logger;

import com.tibco.tibrv.TibrvFtMemberCallback;
import com.tibco.tibrv.TibrvFtMember;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvRvdTransport;

import com.tibco.rv.jca.AbstractCallBack;
import com.tibco.rv.jca.ra.RVListenerManager;
import java.util.logging.Level;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FtCallBack extends AbstractCallBack
{
	private boolean active=false;
	private TibrvRvdTransport transport=null;
	private String serviceName;
	private String replyToSubject;
	private static int cntr=0;
	
	protected void init()
	{
		className = FtCallBack.class.getName();
		
	    	try 
	    	{
	            sLogger = Logger.getLogger(className, rbName);
	        } 
	    	catch (Exception e)
	        {
	            // Problem initializing logging.  Continue anyway.
	        } 
		
	}
	
	public FtCallBack(boolean pActive,String pServiceName,RVListenerManager pListenerMgr)
	{
		super(pServiceName,pListenerMgr);
		active=pActive;
		serviceName=pServiceName;
		
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
	
	 public void onMsg(TibrvListener listener, TibrvMsg msg)
	 {
	 	cntr++;
	     sLogger.log(Level.INFO,"[" + cntr +"]:Ft-Thread-Received message: "+msg.getSendSubject());
	     // if we're not active, don't respond
	     if(!active)
	     {
	     	sLogger.log(Level.INFO,serviceName+": Not active, can't process message !");
	     	return;
	     }	     
	   
	     	sendMsgToEndpoint(msg);
	    
	     
	   }
	 protected TibrvMsg enhanceMsg(TibrvMsg pMsg)
		{
			TibrvMsg retMsg=pMsg;
			//do any additional data formating in the message - as needed
			return retMsg;
			
		}


}
