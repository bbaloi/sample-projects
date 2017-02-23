/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.ra;


import java.lang.reflect.Method;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import com.tibco.rv.jca.RVListener;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import javax.resource.spi.ActivationSpec;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RVEndpoint  implements MessageEndpoint, RVListener
{	
	
	 static final String className = RVEndpoint.class.getName();
	 private static Logger sLogger;
	 private static final String rbName = null; // No localization is used for logging.
	 private MessageEndpointFactory factory=null;
	 private RVInboundActivationSpec activationSpec=null;
	   
	 static
	 {
	 	
			 
			 try 
			 {
		        sLogger = Logger.getLogger(className, rbName);
			 }
			 catch (Exception e) 
			 {
		        // Problem initializing logging.  Continue anyway.
			 }
	 }
	public RVEndpoint(MessageEndpointFactory pFactory, ActivationSpec pSpec)
	{
		activationSpec= (RVInboundActivationSpec) pSpec;
		factory = pFactory;
		
	}
	
	/* (non-Javadoc)
	 * @see javax.resource.spi.endpoint.MessageEndpoint#afterDelivery()
	 */
	public void afterDelivery() throws ResourceException {
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"After delivery");

	}
	/* (non-Javadoc)
	 * @see javax.resource.spi.endpoint.MessageEndpoint#beforeDelivery(java.lang.reflect.Method)
	 */
	public void beforeDelivery(Method arg0) throws NoSuchMethodException,
			ResourceException {
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"before delivery");

	}
	/* (non-Javadoc)
	 * @see javax.resource.spi.endpoint.MessageEndpoint#release()
	 */
	public void release() {
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"release");

	}
	
	/* (non-Javadoc)
	 * @see com.tibco.rv.jca.RVListener#onMessage(com.tibco.tibrv.TibrvMsg)
	 */
	public void onMessage(TibrvMsg pRvMsg)
	{
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"RVEndpoint got a message");

	}	
	
}
