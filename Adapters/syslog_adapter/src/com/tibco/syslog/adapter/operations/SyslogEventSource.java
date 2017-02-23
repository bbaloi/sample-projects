package com.tibco.syslog.adapter.operations;

import org.apache.log4j.Logger;

import com.tibco.sdk.MApp;
import com.tibco.sdk.MComponentRegistry;
import com.tibco.sdk.events.MEventSource;
import com.tibco.syslog.adapter.test.SyslogSimulator;

public class SyslogEventSource extends MEventSource
{
	private static final Logger logger = Logger.getLogger( "com.tibco.syslog.adapter.operations.SyslogEventSourcce");
	
	private SyslogSimulator simulator;
	private MApp lMapp;
	public SyslogEventSource(MComponentRegistry pRegistry, String pName,MApp pApp)
	{
		super(pRegistry,pName);
		lMapp = pApp;
		
	}
	public void init()
	{
		//start the Syslog Thread
		simulator = new SyslogSimulator(this,lMapp);
	    simulator.start();
		lMapp.getTrace().trace("Syslog-20000", null, "starting Syslog Stack",null);
		
	}
	public void stop()
	{
	   lMapp.getTrace().trace("Syslog-20000", null, "SyslogEventSource: stopping SyslogCollector !",null);
	   System.out.println("-SyslogEventSource: stopping SyslogCollector !-");
		simulator.stopRuning();
		simulator=null;
	}	

}
