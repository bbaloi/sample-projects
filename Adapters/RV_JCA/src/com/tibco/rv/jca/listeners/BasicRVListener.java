/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Logger;

import com.tibco.rv.jca.AbstractListener;
import com.tibco.rv.jca.ICallBack;
import com.tibco.rv.jca.ra.RVInboundActivationSpec;
import com.tibco.rv.jca.vo.BasicVo;

import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsgCallback;

import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvCmListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvCmMsg;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvCmQueueTransport;
import com.tibco.tibrv.TibrvDispatcher;

import com.tibco.rv.jca.exceptions.RV_JCAException;
import java.util.logging.Level;



/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BasicRVListener extends AbstractListener 
{	
    private TibrvListener listener;
    private BasicVo  vo=null;
   	
	public BasicRVListener(BasicVo pVo) throws RV_JCAException
	{
		vo=pVo;
		init();
		
	}
	protected void init() throws RV_JCAException
	{
		className = BasicRVListener.class.getName();

    	try 
    	{
            sLogger = Logger.getLogger(className, rbName);
        } 
    	catch (Exception e)
        {
            // Problem initializing logging.  Continue anyway.
        } 	
				
		try
		{
		  // Open a native rendezvous implementation
	     Tibrv.open(Tibrv.IMPL_NATIVE);
	     // Create a transport with the porvideed settings
	     transport = new TibrvRvdTransport(vo.getService(), vo.getNetwork(), vo.getDaemon());	     
	    
		}
		catch(TibrvException excp)
		{
			String msg="Could not create Transport";			
			throw new RV_JCAException(excp,msg);
			
		}	  
		
	}
	public void registerCallback(ICallBack pMsgCallback)
	{
		callback = pMsgCallback;
		callback.setTransport(transport);
		 // Create a listener to listen for queries
		try
		{
			listener = new TibrvListener(Tibrv.defaultQueue(), callback, transport, vo.getSubject(), null);
		
		}
		catch(TibrvException excp)
		{
			excp.printStackTrace();
		}
	}
	public void run()
	{
		isRunning=true;
		
		   /* while(true){
		       try{
		         Tibrv.defaultQueue().timedDispatch(999);
		       }
		       catch(InterruptedException e){
		       	
		       }
		     }*/
		   
		   // create a dispatcher thread for the queue
		    dispatcher =  new TibrvDispatcher(Tibrv.defaultQueue());
			//set internal state vars
		    while (isRunning);
		
	}
	public void finalize()
	{
		//cleanup();
	}	
	
}
