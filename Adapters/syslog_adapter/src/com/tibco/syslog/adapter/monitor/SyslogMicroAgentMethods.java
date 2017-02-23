package com.tibco.syslog.adapter.monitor;

import com.tibco.sdk.hawk.MHawkMethod;
import com.tibco.sdk.hawk.MHawkMicroAgent;
import com.tibco.syslog.adapter.operations.SyslogPublisher;

//public class SyslogHawkMicroAgent extends MHawkMicroAgent
//public class SyslogMicroAgentMethods extends MHawkMethod
public class SyslogMicroAgentMethods 
{
	private ISyslogMonitor syslogMonitor=null;
	
	public SyslogMicroAgentMethods()
	{
		
	}
    public Integer getNumberOfMessagesProcessed()
	{
		return new Integer(syslogMonitor.getNumMessages());
		
	}
	public void registerSyslogPublisher(ISyslogMonitor pMonitor)
	{
		syslogMonitor = pMonitor;
	}

}
