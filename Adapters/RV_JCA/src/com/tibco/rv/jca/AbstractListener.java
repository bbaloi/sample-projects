/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvDispatcher;
import com.tibco.tibrv.TibrvFtMember;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.rv.jca.ra.RVInboundActivationSpec;
import javax.resource.spi.work.Work;

import com.tibco.rv.jca.vo.BasicVo;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractListener implements Work
{
	 protected  String className;
	 protected  Logger sLogger;
	 protected  String rbName = null; // No localization is used for logging.
	 
	protected TibrvRvdTransport transport;
	protected TibrvDispatcher dispatcher;
        protected TibrvFtMember ftMember;
	protected TibrvListener listener;
	protected ICallBack callback;	
	protected boolean isRunning=false;
	
	
	public AbstractListener() throws RV_JCAException
	{			
				
	}
	public void release()
	{
		sLogger.log(Level.INFO,"AbstractListener.release() - Cleaning up RV components...");
		cleanup();
		isRunning=false;
		
	}
	protected abstract void init() throws RV_JCAException;
	
	public abstract void registerCallback(ICallBack pMsgCallback);
	
	public void cleanup()
        {
            if(transport!=null)
            {  
                    transport.destroy();
		    transport=null;
		    sLogger.log(Level.INFO,"Destroyed Transport");
           }
            if(dispatcher!=null) 
	    {	    
              	   dispatcher.destroy();
     		   dispatcher=null;		   
		    sLogger.log(Level.INFO,"Destroyed Dispatcher");
	    }
	    if(ftMember!=null)
	    {	    
                ftMember.destroy();
		ftMember=null;		
		    sLogger.log(Level.INFO,"Destroyed ftMember");
	    }
    	    try
	    {	    
	    	Tibrv.close();
    	    }
	    catch(TibrvException excp)
	    {
		    excp.printStackTrace();
	    }	    
        }
	
	public void run()
	{
		
	}
	
	

}
