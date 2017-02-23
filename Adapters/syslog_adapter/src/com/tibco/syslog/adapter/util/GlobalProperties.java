package com.tibco.syslog.adapter.util;

import java.util.Properties;


public class GlobalProperties 
{
	private Properties props=null;
	private static GlobalProperties instance=null;
	
	private GlobalProperties()
	{
		
	}
	public static GlobalProperties getInstance()
	{
		if(instance==null)
			instance= new GlobalProperties();
		return instance;
	}
	public void setProperties(Properties pProps)
	{
		props=pProps;
	}
	public Properties getProperties()
	{
		return props;
	}

}
