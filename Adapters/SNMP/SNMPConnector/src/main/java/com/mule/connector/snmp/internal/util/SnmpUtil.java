package com.mule.connector.snmp.internal.util;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.mule.connector.snmp.internal.SNMPConnection;
import com.mule.connector.snmp.internal.source.SNMPListener;
import com.mule.connector.snmp.internal.source.SNMPListenerConnection;

public class SnmpUtil 
{
	
	private static final Logger LOGGER = getLogger(SnmpUtil.class);
	private static SnmpUtil instance=null;
	
	private SNMPListenerConnection connection;
	
	private void SnmpUtil()
	{
		
	}
	
	public static SnmpUtil getInstance()
	{
		if (instance==null)
			instance = new SnmpUtil();
		return instance;
	}

	public synchronized SNMPListenerConnection getConnection() {
		return connection;
	}

	public synchronized  void setConnection(SNMPListenerConnection connection) 
	{
		LOGGER.info("+++Added SNMPConnection to shared Object ++++");
		this.connection = connection;
	}

}
