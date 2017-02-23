// $Id: Sniffer.java,v 1.1 2002/02/18 21:49:49 pcharles Exp $

/***************************************************************************
 * Copyright (C) 2001, Rex Tsai <chihchun@kalug.linux.org.tw>              *
 * Distributed under the Mozilla Public License                            *
 *   http://www.mozilla.org/NPL/MPL-1.1.txt                                *
 ***************************************************************************/

package com.tibco.sip.samples;

import net.sourceforge.jpcap.capture.*;
import net.sourceforge.jpcap.net.*;
import net.sourceforge.jpcap.util.AsciiHelper;

/**
 * jpcap Tutorial - Sniffer example
 *
 * @author Rex Tsai
 * @version $Revision: 1.1 $
 * @lastModifiedBy $Author: pcharles $
 * @lastModifiedAt $Date: 2002/02/18 21:49:49 $
 */
public class Sniffer
{
  private static final int INFINITE = -1;
  private static final int PACKET_COUNT = INFINITE; 
  /*
    private static final String HOST = "203.239.110.20";
    private static final String FILTER = 
      "host " + HOST + " and proto TCP and port 23";
  */

  private static final String FILTER = 
    // "port 23";
    "";

  public static void main(String[] args) {
    try {
      if(args.length == 1){
	Sniffer sniffer = new Sniffer(args[0]);
      } else {
	System.out.println("Usage: java Sniffer [device name]");
	System.out.println("Available network devices on your machine:");
	String[] devs = PacketCapture.lookupDevices();
	for(int i = 0; i < devs.length ; i++)
	  System.out.println("\t" + devs[i]);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public Sniffer(String device) throws Exception {
    // Initialize jpcap
    PacketCapture pcap = new PacketCapture();
    System.out.println("Using device '" + device + "'");
    pcap.open(device, true);
    pcap.setFilter(FILTER, true);
    pcap.addPacketListener(new PacketHandler());

    System.out.println("Capturing packets...");
    pcap.capture(PACKET_COUNT);
  }
}


class PacketHandler implements PacketListener 
{
  public void packetArrived(Packet packet) 
  {
	  //System.out.println("Got packet");
    try {
      // only handle TCP packets

   if(packet instanceof TCPPacket) 
   {
		TCPPacket tcpPacket = (TCPPacket)packet;
		byte[] tcpData = tcpPacket.getTCPData();
		byte [] tcpHeader= tcpPacket.getTCPHeader();
		byte[] packetData = tcpPacket.getData();
		byte [] packetHeader=	tcpPacket.getHeader();
		byte [] etherData = tcpPacket.getEthernetData();
		byte [] etherHeader = tcpPacket.getEthernetHeader();
		
		byte [] ipData= tcpPacket.getIPData();
		byte [] ipHeader = tcpPacket.getIPHeader();
				
		String srcHost = tcpPacket.getSourceAddress();
		String dstHost = tcpPacket.getDestinationAddress();
		String _packetData = new String(packetData, "ISO-8859-1");
		String _packetHeader = new String(packetHeader, "ISO-8859-1");
		String _tcpData = new String(tcpData, "ISO-8859-1");
		String _tcpHeader = new String(tcpHeader, "ISO-8859-1");
		String _etherData = new String(etherData, "ISO-8859-1");
		String _etherHeader = new String(etherHeader, "ISO-8859-1");
				
		//System.out.println("---Packet Header:"+srcHost+" -> " + dstHost + ": " + _packetHeader);
		System.out.println("---Packet Data:"+srcHost+" -> " + dstHost + ": " + AsciiHelper.toText(packetData));
		//System.out.println("---Tcp Header:"+srcHost+" -> " + dstHost + ": " + _tcpHeader);
		//System.out.println("---Tcp Data:"+srcHost+" -> " + dstHost + ": " + _tcpData);
		//System.out.println("---Ether Header:"+srcHost+" -> " + dstHost + ": " + _etherHeader);
		//System.out.println("---Ether Data:"+srcHost+" -> " + dstHost + ": " + _etherData);
		
		
		
      }
   if(packet instanceof UDPPacket) 
   {
		UDPPacket udpPacket = (UDPPacket)packet;
		//byte[] data = tcpPacket.getTCPData();
		byte[] data = udpPacket.getData();
		    
		String srcHost = udpPacket.getSourceAddress();
		String dstHost = udpPacket.getDestinationAddress();
		String isoData = new String(data, "ISO-8859-1");	
		String _data   = AsciiHelper.toText(data);
		System.out.println("UDPPacket:"+srcHost+" -> " + dstHost + ": " + isoData);
      }
    } 
    catch( Exception e ) {
      e.printStackTrace();
    }
  }
  private String getStrFromBytes(byte []pArray)
  {
	  String retStr=new String();
	  for(int i=0;i<pArray.length;i++)
	  {
		  String _b=Byte.toString(pArray[i]);
		  retStr+=_b;		 
	  }
	  
	  return retStr;
	  
  }
 
}