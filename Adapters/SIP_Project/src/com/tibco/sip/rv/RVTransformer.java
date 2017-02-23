package com.tibco.sip.rv;

import java.util.Date;

import com.tibco.sip.controller.ISIPMessageQueue;
import com.tibco.sip.util.Constants;
import com.tibco.sip.vo.SIPPacketVo;
import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvTransport;

import org.apache.log4j.Logger;

public class RVTransformer implements Runnable
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.sip.rv.RVTransformer");
	private String service;
	private String daemon;
	private String network;
	private String subject;
	private ISIPMessageQueue queue;
	private TibrvTransport rvTransport=null;
	
	public RVTransformer(String pService,String pNetwork,String pDaemon,String pSubject,ISIPMessageQueue pQueue)
	{
		
		service =pService;
		daemon=pDaemon;
		network=pNetwork;
		subject=pSubject;
		queue=pQueue;		
		init();
	}
	public void run()
	{
		sLogger.debug("Starting RV Transformer thread !");
		while(true)
		{
			try
			{
				if(queue.getQueueSize()>0)
				{
					SIPPacketVo vo = queue.getMessage();
					sLogger.debug("---got SIP Message");
					TibrvMsg msg=convertToRV(vo);
					sendtoRV(msg);
				}
			}
			catch(java.util.EmptyStackException excp)
			{
			}
		}
	}
	private void init()
	{
		sLogger.debug("Initializing RV stack");
		
		 try
	        {
	            Tibrv.open(Tibrv.IMPL_NATIVE);
	            sLogger.debug("Started Tibrv !");
	        }
	        catch (TibrvException e)
	        {
	            System.err.println("Failed to open Tibrv in native implementation:");
	            e.printStackTrace();
	            System.exit(0);
	        }

	        try
	        {
	        	
	            rvTransport = new TibrvRvdTransport(service,network,daemon);
	            sLogger.debug("Started TibrvTrasnaport");
	        }
	        catch (TibrvException e)
	        {
	            System.err.println("Failed to create TibrvRvdTransport:");
	            e.printStackTrace();	          
	        }

	}
	private TibrvMsg convertToRV(SIPPacketVo pVo)
	{
		//sLogger.debug("Converting to RVMessage");
		TibrvMsg msg=null;
		try
		{
			msg = new TibrvMsg();
			msg.add(Constants.rvTstamp, pVo.getDate());
			msg.add(Constants.rvOperation, pVo.getOperation());
			msg.add(Constants.rvFrom,pVo.getFrom());
			msg.add(Constants.rvTo,pVo.getTo());
			msg.add(Constants.rvCallerId, pVo.getCallerId());
			msg.add(Constants.rvSrcIP, pVo.getSrcIp());
			msg.add(Constants.rvSrcPort, pVo.getSrcPort());
			msg.add(Constants.rvDestIP, pVo.getDestIp());
			msg.add(Constants.rvDestPort, pVo.getDestPort());
		}
		catch (TibrvException e) {
            System.err.println("Failed to set RV message data !");
            e.printStackTrace();	            
        }
	
		return msg;

	}
	private void sendtoRV(TibrvMsg msg)
	{
		sLogger.debug("Sending out RV Message");
		 try
	        {
	            msg.setSendSubject(subject);
	            rvTransport.send(msg);
	        }
	        catch (TibrvException e) {
	            System.err.println("Failed to send message to subject:"+subject);
	            e.printStackTrace();	            
	        }


	}
}
