package com.tibco.syslog.adapter.test;

import java.util.Date;

import org.apache.log4j.Logger;

import com.tibco.sdk.MApp;
import com.tibco.sdk.events.MEventSource;
import com.tibco.syslog.adapter.AdapterSyslogVo;
import com.tibco.syslog.adapter.operations.SyslogEvent;

public class SyslogSimulator extends Thread
{

	private static int cntr=0;
	private MEventSource syslogEventSource=null;
	private boolean run;
	private MApp lApp;
	
	
	private static final Logger logger = Logger.getLogger( "com.tibco.syslog.adapter.test.SyslogSimulator");
	
	
	
	public SyslogSimulator(MEventSource pEventSource,MApp pApp)
	{
		syslogEventSource=pEventSource;
		run=true;
		lApp = pApp;
		
		
	}
	public void run()
	{
		while(run)
		{
			try
			{
				this.sleep(2000);
				cntr++;
				//create Syslog Internal message & publish it			
				SyslogEvent syslogEvent = new SyslogEvent(new AdapterSyslogVo((new Date()).toString(),"123",456,789,cntr+") syslog msg","localhost","localhost"));
				
				syslogEventSource.notify(syslogEvent);
				logger.debug("generated Syslog message:"+cntr);
			}
			catch(InterruptedException excp)
			{
				excp.printStackTrace();
				
			}
		}
		
	}
	public synchronized void stopRuning()
	{
		run=false;
		lApp.getTrace().trace("Syslog-20000", null, "Stopping Syslog Stack !",null);
		
	}
	
}
