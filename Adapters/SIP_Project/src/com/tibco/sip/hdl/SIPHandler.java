package com.tibco.sip.hdl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

import net.sourceforge.jpcap.util.AsciiHelper;

import org.apache.log4j.Logger;

import com.tibco.sip.base64.Base64Coder;
import com.tibco.sip.controller.ISIPMessageQueue;
import com.tibco.sip.util.SIPPacketVoCreator;
import com.tibco.sip.vo.SIPPacketVo;
import com.tibco.sip.vo.SIPPacketVo;


public class SIPHandler implements PacketReceiver
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.sip.hdl.SIPHandler");
	
	private String interfaceName;
	private JpcapWriter writer;
	private SIPPacketVoCreator voCreator;
	private ISIPMessageQueue queue;
	
  
  public SIPHandler(String pInterfaceName, String pDumpFile,JpcapCaptor pCaptor,ISIPMessageQueue pQueue)
  {
	  queue=pQueue;
	  interfaceName=pInterfaceName;
	  voCreator = new SIPPacketVoCreator(this);
	  
	  try
	  {
		  writer = JpcapWriter.openDumpFile(pCaptor, pDumpFile);
	  }
	  catch(java.io.IOException pExcp)
	  {
		  pExcp.printStackTrace();
	  }
  }
	
  public void receivePacket(Packet packet)
  {
	  String pktType=new String();
	  String _data;
	  String srcIp,destIp;
	  int srcPort,destPort;
		  
	  sLogger.debug("+++Packet data:"+voCreator.bytesToString(packet.data));
		
	  try
	  {
		  if(packet!=null)
		  {
			  			  
			  if(packet instanceof TCPPacket || packet instanceof UDPPacket)
			  {
				  if(packet instanceof TCPPacket)
					  pktType="TCP";
				  if(packet instanceof UDPPacket)
					  pktType="UDP";
				  //TCPPacket pkt = (TCPPacket) packet;
				  sLogger.debug("---received "+pktType+"Packet from Interface "+interfaceName);
				   voCreator.createSIPPacket(packet);						 
				  
			  }			
		  }
		  else
			  sLogger.debug("Got an empty packet !");
	  }
	  catch(Exception excp)
	  {
		  excp.printStackTrace();
	  }
		
  }
 public synchronized void  sendSIPPacket(SIPPacketVo pktVo)
  {
	// sLogger.debug("Sending SIP VO with operation:"+pktVo.getOperation());
	 queue.sendMessage(pktVo);
  }
 	

}

