/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Logger;

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

import com.tibco.rv.jca.AbstractListener;
import com.tibco.rv.jca.vo.DqVo;

import javax.resource.spi.work.Work;
import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.rv.jca.ICallBack;

import java.util.logging.Level;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DqListener extends AbstractListener
{
    private TibrvCmQueueTransport dqTransport;
    private TibrvCmListener certListener;
    private DqVo vo;
   	
	public DqListener(DqVo pVo) throws RV_JCAException
	{
		vo=pVo;
		init();
		
	}
	protected void init()
	{
		className = DqListener.class.getName();

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
	     // Create a transport with the default settings
	     transport = new TibrvRvdTransport(vo.getService(), vo.getNetwork(), vo.getDaemon());
	     // Wrap in a certified transport with its name and set to request old messages
 
	     if(((DqVo)vo).getSchedulerWeight()!=0)
	     	dqTransport  = new TibrvCmQueueTransport(transport, ((DqVo)vo).getCmName());
	     else 	     
	     	dqTransport  = new TibrvCmQueueTransport(transport, ((DqVo)vo).getCmName(),((DqVo)vo).getWeight(),
	     					((DqVo)vo).getTasks(),((DqVo)vo).getSchedulerWeight(),1.0,3.5);
	     	     
	    
		}
		catch(TibrvException excp)
		{
			String msg="Failed to instantiate DqTransport !";
			sLogger.log(Level.SEVERE,msg,excp);
			//excp.printStackTrace();
		}
	  
		
	}
	public void registerCallback(ICallBack pMsgCallback)
	{
		callback = pMsgCallback;
		callback.setTransport(transport);
		 // Create a listener to listen for queries
		try
		{	
	     certListener = new TibrvCmListener(
	       Tibrv.defaultQueue(), callback, dqTransport,vo.getSubject(), null);
		}
		catch(TibrvException excp)
		{
			excp.printStackTrace();
		}
	}
	public void run()
	{
		try
		{
			isRunning=true;
		
		    sLogger.log(Level.INFO,"Server is listening for DQ reqeusts on subject "+vo.getSubject());
		     // create a dispatcher thread for the queue
		   dispatcher =  new TibrvDispatcher(Tibrv.defaultQueue());		   
		   while (isRunning);
		   //sLogger.log(Level.INFO,"Exiting,DqRvListenerThread");
		   // Tibrv.defaultQueue().timedDispatch(999);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
			
		}
	}
}
