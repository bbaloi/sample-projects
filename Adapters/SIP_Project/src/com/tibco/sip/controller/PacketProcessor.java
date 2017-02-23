package com.tibco.sip.controller;

import com.tibco.sip.hdl.SIPHandler;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import org.apache.log4j.Logger;

public class PacketProcessor  implements Runnable
{
	private final Logger sLogger = Logger.getLogger( "com.tibco.sip.controller.PacketProcessor");
	private JpcapCaptor jpcap;
	private SIPHandler sipHandler;
	private ISIPMessageQueue queue;
	private String _interface;
	
	public PacketProcessor(NetworkInterface pInterface,String pDumpFile,ISIPMessageQueue pQueue)
	{
		queue = pQueue;
		_interface=pInterface.name;
		
		try
		{
			jpcap = JpcapCaptor.openDevice(pInterface, 65535, true, 60);
			sipHandler=new SIPHandler(pInterface.name,pDumpFile,jpcap,queue);	
			//sLogger.debug("Registered a packet Processor for interface:"+pInterface.name);			
			//jpcap.processPacket(-1, sipHandler);
			//jpcap.setFilter("ip proto \\sip", true);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	public void run()
	{
		sLogger.debug("Starting Packet processor for interface:!"+_interface);
		while(true)
		{
			jpcap.loopPacket(-1, sipHandler);
			//Packet packet=jpcap.getPacket();
			//sipHandler.receivePacket(packet);
		}
	}
}
