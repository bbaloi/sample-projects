
package com.tibco.rv.jca.ra.synch;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvRvdTransport;

public class RVConnection implements IRVSender
{
	private String className=null,rbName=null;
	private static Logger sLogger=null;
	private TibrvRvdTransport transport=null;
	private String service=null,network=null,daemon=null;

    private RVManagedConnection mc;

    public RVConnection(RVManagedConnection pMc)
    {        
        mc = pMc;
        init();
        createLocalTransport();
		sLogger.log(Level.INFO,"Initialized RVConnection");
    }
    public RVConnection(RVManagedConnection pMc,RVConnectionRequestInfo info)
    {        
        mc = pMc;
        init();
        if(info !=null)
        {
        	service=info.getService();
        	network=info.getNetwork();
        	daemon=info.getDaemon();
        }
        else
        {
        	sLogger.log(Level.INFO,"No ConnectionRequestInfo object present, will use default RvTransport settings !");
        }       
        createLocalTransport();
		sLogger.log(Level.INFO,"Initialized RVConnection");
    }

    public RVConnection(RVManagedConnection pMc,String pService,String pNetwork,String pDaemon)
    {        
        mc = pMc;
        service=pService;
        network=pNetwork;
        daemon=pDaemon;
        init();
        createLocalTransport();
		sLogger.log(Level.INFO,"Initialized RVConnection");
    }
    private void init()
    {
    	className = RVConnection.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    
    }
    private void createLocalTransport()
	{
    	  try
			{
			 // Open a native rendezvous implementation
		     Tibrv.open(Tibrv.IMPL_NATIVE);
		     // Create a transport with the default settings
		     transport = new TibrvRvdTransport(service, network, daemon);  
			}
			catch(TibrvException excp)
			{
				
				excp.printStackTrace();
				
			}
		
	}
    public void sendRvMsg(TibrvMsg pMsg) throws RV_JCAException
	{
    	
    	try
    	{
    		transport.send(pMsg);
    	}
    	catch(TibrvException excp)
    	{
    		String msg="Could not send RV message!";
    		throw new RV_JCAException(excp,msg);
    	}
    	sLogger.log(Level.INFO,"Sent RV Message !");
	}
	public TibrvMsg sendRequest(TibrvMsg pMsg,double pTimeout) throws RV_JCAException
	{
		TibrvMsg returnMsg=null;
		
    	try
    	{
    		returnMsg=transport.sendRequest(pMsg,pTimeout);
    	}
    	catch(TibrvException excp)
    	{
    		String msg="Could not send Request RV message!";
    		throw new RV_JCAException(excp,msg);
    	}
    	sLogger.log(Level.INFO,"Sent Request RV Message !");
		return returnMsg;
	}
	public void sendReply(TibrvMsg pReplyMsg, TibrvMsg pRequestMsg) throws RV_JCAException
	{
		
    	try
    	{
    		transport.sendReply(pReplyMsg,pRequestMsg);
    	}
    	catch(TibrvException excp)
    	{
    		String msg="Could not send Reply RV message!";
    		throw new RV_JCAException(excp,msg);
    	}
    	sLogger.log(Level.INFO,"Sent Reply RV Message !");
	}
	public void destroy()
	{
		sLogger.log(Level.INFO,"Destroying TibrvRvdTransport !");
		transport.destroy();
	}
	public void  finalize()
	{
		sLogger.log(Level.INFO,"Detroying  Tibrv trasport");
		try
		{
			transport.destroy();
			Tibrv.close();
		}
		catch(TibrvException excp)
		{
			
		}
		
	}

}
