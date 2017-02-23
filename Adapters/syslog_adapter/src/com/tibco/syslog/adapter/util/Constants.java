package com.tibco.syslog.adapter.util;

public interface Constants 
{
	public String AppName 			= "SyslogAdapterApp";	
	public String AppVersion 		= "1.0";
	public String AppInfo 			= "TIBCO Adapter SDK Syslog Implementation ";
	public String ConfigURL			= "SyslogAdapterService/SyslogAdapterInstance";
	//public String ConfigURL		= "/tibco/private/adapter/SyslogAdapterService/SyslogAdapterInstance";
	public String AdapterRepository = "RepositoryURL";
	public String SyslogMessage = 	"/tibco/public/class/ae/SyslogSchema/SyslogMessage";
	public String SyslogEventSource = "SyslogEventSource";
	public String MicroagentName	= "SyslogHawkMicroAgent";
}
