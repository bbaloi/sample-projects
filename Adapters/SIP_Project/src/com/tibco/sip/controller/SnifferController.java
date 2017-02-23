package com.tibco.sip.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jpcap.NetworkInterface;
import org.apache.log4j.Logger;

import com.tibco.sip.rv.RVTransformer;
import com.tibco.sip.util.Constants;
import com.tibco.sip.vo.SIPPacketVo;

public class SnifferController implements ISnifferController,ISIPMessageQueue
{
	private final Logger sLogger = Logger.getLogger( "com.tibco.sip.controller.SnifferController");
	private ExecutorService threadPool;
	private String dumpFile;
	private Stack sipMsgQueue;
	private Properties lProps;
	
	public SnifferController(String pDumpFile,Properties pServerProps)
	{
		lProps=pServerProps;
		dumpFile=pDumpFile;
		sipMsgQueue = new Stack();
		  
	}
	public void startPacketProcessors(List pInterfaces)
	{
		threadPool=Executors.newFixedThreadPool(pInterfaces.size()+1);
		Iterator  it= pInterfaces.iterator();
		while(it.hasNext())
		 {
			 NetworkInterface _interface = (NetworkInterface)it.next();
			 PacketProcessor packetProcessor = new PacketProcessor(_interface,dumpFile,this);
			 threadPool.execute(packetProcessor);			
		 }		
	}
	public void startRVTransformer()
	{
		String service=lProps.getProperty(Constants.rv_service);
		String network=lProps.getProperty(Constants.rv_network);
		String daemon=lProps.getProperty(Constants.rv_daemon);
		String subject= lProps.getProperty(Constants.rv_subject);

		RVTransformer transformer = new RVTransformer(service,network,daemon,subject,this);
		 threadPool.execute(transformer);	
	}
	public synchronized void sendMessage(SIPPacketVo pSipMsg)
	{
		
		sipMsgQueue.push(pSipMsg);
		sLogger.debug("published SIPPacketVo !");
	}
	public synchronized SIPPacketVo getMessage()
	{
		
			return (SIPPacketVo)sipMsgQueue.pop();
		
	}
	public int getQueueSize()
	{
		return sipMsgQueue.size();
	}
	
	

}
