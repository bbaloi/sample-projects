package com.tibco.syslog.adapter.operations;

import java.util.Date;

import org.apache.log4j.Logger;

import com.tibco.sdk.MApp;
import com.tibco.sdk.MException;
import com.tibco.sdk.MTrackingInfo;
import com.tibco.sdk.MTree;
import com.tibco.sdk.events.MDataEvent;
import com.tibco.sdk.events.MEvent;
import com.tibco.sdk.events.MEventListener;
import com.tibco.sdk.events.MExceptionEvent;
import com.tibco.sdk.events.MPublisher;
import com.tibco.sdk.metadata.MClassRegistry;
import com.tibco.sdk.metadata.MInstance;
import com.tibco.syslog.adapter.AdapterSyslogVo;
import com.tibco.syslog.adapter.monitor.ISyslogMonitor;

public class SyslogPublisher implements MEventListener,ISyslogMonitor
{
	private MApp lMapp;
	private MPublisher lPublisher;
	
	private static final String SYSLOG_MESSAGE_CLASS = "/tibco/public/class/ae/SyslogSchema/SyslogMessage";
	
	private static final Logger logger = Logger.getLogger( "com.tibco.syslog.adapter.operations.SyslogPublisher");
	
	private static int msgCntr=0;
	
	
	public SyslogPublisher(MApp pMapp,MPublisher pPub)
	{
		lMapp=pMapp;
		lPublisher=pPub;
	}
	
	public synchronized void onEvent(MEvent event) 
	{
		if(logger.isDebugEnabled())
			logger.debug("got event");
		try 
		{
			if (event instanceof MExceptionEvent) 
			{
				processExceptionEvent(event);
			} 
			else 
			{
				processDataEvent(event);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Unexpected exception occurred:");
			e.printStackTrace();
		}
	}
	
	private void processExceptionEvent(MEvent event) 
	{
		MExceptionEvent exceptionEvent = (MExceptionEvent) event;
		System.out.println("Exception event occurred unexpectedly:");
		System.out.println(" description: " + exceptionEvent.getException().getMessage());
		System.out.println(" data: " + exceptionEvent.getData().toString());
	}
	private void processDataEvent(MEvent event) 
	{
		try {
			
			if(logger.isDebugEnabled())
				logger.debug("processing event");
			
			
			SyslogEvent sysEvent = (SyslogEvent) event;
			AdapterSyslogVo vo = sysEvent.getSyslogVo();
			
			MClassRegistry classRegistry = lMapp.getClassRegistry();
			MInstance oInstance = classRegistry.getDataFactory().newInstance(SYSLOG_MESSAGE_CLASS);
			oInstance.set("CreationDate", vo.getTime());
			oInstance.set("PID", vo.getPID());
			oInstance.set("Severity", Integer.toString(vo.getSeverity()));
			oInstance.set("Facility",Integer.toString(vo.getFacility()));
			oInstance.set("Source", vo.getSourceAddress());
			oInstance.set("CapturePoint", vo.getCapturePoint());
			oInstance.set("Message", vo.getMessage());
			
			lMapp.getTrace().trace("Syslog-20000", null, "got SyslgMessage"+vo.getMessage(),null);
			
			
//			 demonstrate use of tracing info. Only tracking ID is
//			 traced
			//MTrackingInfo trackingInfo = new MTrackingInfo();
			//trackingInfo.addApplicationInfo("Publish event at " + cnt);
			//oInstance.setTrackingInfo(trackingInfo);
			//Object substituteMessage = null;
			//lMapp.getTrace().trace("PUBSUB-0004", oInstance.getTrackingInfo(), substituteMessage);


			if(logger.isDebugEnabled())
				logger.debug("Publishing data...");
			
			lPublisher.send(oInstance);
			msgCntr++;
			
			} catch (Exception ex) {
			ex.printStackTrace();
			}
	
	}
	public int getNumMessages()
	{
		return msgCntr;
	}
}
