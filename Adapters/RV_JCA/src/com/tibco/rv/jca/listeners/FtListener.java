/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.tibrv.TibrvFtMember;
import com.tibco.tibrv.TibrvFtMemberCallback;
import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.rv.jca.AbstractListener;

import com.tibco.rv.jca.ICallBack;
import com.tibco.rv.jca.vo.FtVo;
import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.rv.jca.util.Constants;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FtListener extends AbstractListener  implements TibrvFtMemberCallback
{
	private FtVo vo=null;
		
	public FtListener(FtVo pFtVo) throws RV_JCAException
	{
			vo=pFtVo;
			init();
			
	}
	protected void init() throws RV_JCAException
	{		
		className = FtListener.class.getName();

    	try 
    	{
            sLogger = Logger.getLogger(className, rbName);
        } 
    	catch (Exception e)
        {
            // Problem initializing logging.  Continue anyway.
        } 	
		//set internal state vars
		 // Open a native rendezvous implementation
    	try
    	{
	     Tibrv.open(Tibrv.IMPL_NATIVE);
	     // Create a transport with the default settings
	    // sLogger.log(Level.INFO,"FtListener:network,daemon,service:"+vo.getNetwork()+","+vo.getDaemon()+","+vo.getService());
	     transport = new TibrvRvdTransport(vo.getService(), vo.getNetwork(), vo.getDaemon());  
    	}
    	catch(TibrvException excp)
		{
			String msg="Failed to instantiate FtTransport !";
			sLogger.log(Level.SEVERE,msg,excp);
			throw new RV_JCAException(excp,msg);
			//excp.printStackTrace();
		}
	}
	public void registerCallback(ICallBack pMsgCallback)
	{
		callback=pMsgCallback;		
		callback.setTransport(transport);		
		
		try
		{		 
	     // Create a listener to listen for queries
		    listener =  new TibrvListener(Tibrv.defaultQueue(), callback, transport, vo.getSubject(), null);
		 
		  // Register this object as a fault-tolerant member under the group name
		   ftMember = new TibrvFtMember(Tibrv.defaultQueue(), this, transport, ((FtVo)vo).getGroup(),
		       ((FtVo)vo).getWeight(), ((FtVo)vo).getActiveGoal(),
		       ((FtVo)vo).getHeartBeat(),    // heartbeat interval, in seconds
		       ((FtVo)vo).getPrepInterval(),    // preparation interval
		       ((FtVo)vo).getActivationInterval(),    // activation interval, should be greater than heartbeat interval
		       null  // no closure object
		       );
		}
		catch(TibrvException excp)
		{

			String msg="Failed to instantiate FtListener !";
			sLogger.log(Level.SEVERE,msg,excp);
			excp.printStackTrace();
		}
		sLogger.log(Level.INFO,"Server is listening for requests");	 

	}
	 public void onFtAction(TibrvFtMember member, String ftgroupName, int action){
	     if (action == TibrvFtMember.PREPARE_TO_ACTIVATE) {
	       sLogger.log(Level.INFO,"FT Action: Prepare to activate");
	       // nothing we can do about this...
	     }
	     else if (action == TibrvFtMember.ACTIVATE) {
	       sLogger.log(Level.INFO,"FT Action: Activate");
	       callback.activate();
	       //active = true;
	     }
	     else if (action == TibrvFtMember.DEACTIVATE) {
	       sLogger.log(Level.INFO,"FT Action: Deactivate");
	       callback.deactivate();
	       //active = false;
	     }
	   }
		
	public void run()
	{
		sLogger.log(Level.INFO,"Server is listening for FT requests on subject "+vo.getSubject());
		   
		try
		{
			 while(isRunning)
			 {
				    try
				    {
				         Tibrv.defaultQueue().timedDispatch(Constants.FT_TIME_DISPATCH);
				    }
				    catch(InterruptedException e)
				    {

						String msg="Failed to to get message of Ft default Queue !";
						sLogger.log(Level.SEVERE,msg,e);
				    	e.printStackTrace();
				    }
			 }
			 //sLogger.log(Level.INFO,"Exiting,FtRvListenerThread");
		}
		catch(TibrvException pExcp)
		{
			pExcp.printStackTrace();
		}
	}

}
