package com.tibco.sip.util;

import java.io.StringWriter;
import java.net.InetAddress;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

import com.tibco.sip.hdl.SIPHandler;
import com.tibco.sip.vo.SIPPacketVo;
import org.apache.log4j.Logger;

public class SIPPacketVoCreator 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.sip.util.SIPPacketVoCreator");
	
	private String srcIp,destIp;
	private int srcPort,destPort;
	private long captureTime;
	private String operation;
	private String toUri;
	private String fromUri;
	private String callId;
	private SIPHandler sipHandler;
	private Date tstamp;
	
	public SIPPacketVoCreator(SIPHandler pHdl)
	{
		sipHandler=pHdl;
	}
	public void createSIPPacket(Packet pkt)
	{
		 try
		 {
			 if(pkt instanceof TCPPacket)
			 { 
				  TCPPacket tcpPkt = (TCPPacket)pkt;
				  InetAddress srcAddr=tcpPkt.src_ip;				  
				  InetAddress destAddr = tcpPkt.dst_ip;
				  srcIp = srcAddr.getHostAddress();
				  destIp = destAddr.getHostAddress();
				  srcPort=tcpPkt.src_port;
				  destPort=tcpPkt.dst_port;
				  captureTime = tcpPkt.sec;
				  tstamp = new Date(captureTime);				
			 }
			 if(pkt instanceof UDPPacket)
			 { 
				  UDPPacket udpPkt = (UDPPacket)pkt;
				  InetAddress srcAddr=udpPkt.src_ip;				  
				  InetAddress destAddr = udpPkt.dst_ip;
				  srcIp = srcAddr.getHostAddress();
				  destIp = destAddr.getHostAddress();
				  srcPort=udpPkt.src_port;
				  destPort=udpPkt.dst_port;
				  captureTime = udpPkt.sec;
				  tstamp = new Date(captureTime);		
			 }
			  boolean result=parsePacketData(bytesToString(pkt.data));
			  if(result==true)  		  
		  	      sipHandler.sendSIPPacket(new SIPPacketVo(fromUri,toUri,callId,operation,tstamp,srcIp,srcPort,destIp,destPort));
		  
		 }
		 catch(Throwable t)
		 {
			 t.printStackTrace();
		 }		 
		
	}
	
   private synchronized boolean parsePacketData(String data)
   {	   	   
	   boolean isSipPacket=false;
		     
	   if(data !=null)		   
	   {
		 		   
		   StringTokenizer tokenizer = new StringTokenizer(data,"\n");
		   int cntr=0;
		   while(tokenizer.hasMoreTokens())
		   {
			   String sipSection=(String)tokenizer.nextToken();
			   switch(cntr)
			   {
				   case 0: //Request Line
				   {
					   
					   if(isSIPPacket(sipSection))
					   {
						   //1. get operation INVITE/OPTIONS etc.						  
						   sLogger.debug("---Found a SIP message, processing !---");
						   StringTokenizer k = new StringTokenizer(sipSection," ");
						   operation=k.nextToken();			
						   sLogger.debug("Operation:"+operation);
						   isSipPacket=true;
						   break;
					   }
					   else 
					   {
						   sLogger.debug("Not a SIP message, discarding !");
						   isSipPacket=false;
						   //break;
						   return isSipPacket;
						}
				   }
				   case 1: // Via
				   {
					   sLogger.debug("Via Section="+sipSection);
					   break;
					   
				   }
				   case 2://From/To
				   {
					   if(isFromSection(sipSection))
					   {
						   //sLogger.debug("From Section="+sipSection);
						   fromUri=getFrom(sipSection);
						   sLogger.debug("From:"+fromUri);
					   }
					   if(isToSection(sipSection))
					   {
						  // sLogger.debug("To Section="+sipSection);
						   toUri=getFrom(sipSection);
						   sLogger.debug("To:"+toUri);
					   }
					  break;
					   
				   }
				   case 3: //To/From
				   {
					   if(isFromSection(sipSection))
					   {
						   //sLogger.debug("From Section="+sipSection);
						   fromUri=getFrom(sipSection);
						   sLogger.debug("From="+fromUri);
					   }
					   if(isToSection(sipSection))
					   {
						 //  sLogger.debug("To Section="+sipSection);
						   toUri=getFrom(sipSection);
						   sLogger.debug("To="+toUri);
					   }
					   break;
				   }
				   case 4: //CallId
				   {
					   //sLogger.debug("CallId Section="+sipSection);
					   callId=getCallId(sipSection);
					   sLogger.debug("CallId:"+callId);
					   break;
				   }
				   case 5: //CSeq
				   {
					   sLogger.debug("CSeq Section="+sipSection);
					   break;
				   }
				   case 6: // Contact
				   {
					   sLogger.debug("ContactSection="+sipSection);
					   break;
				   }
			   };		   
		   
		   cntr++;
	   }
	   }
	   else
		   sLogger.debug("No data in packet");
	   //sLogger.debug("isSipPacket="+isSipPacket);	  
	   return isSipPacket;
   }
   public static String bytesToString(byte bytes[])
   {
       StringWriter sw = new StringWriter();
       int length = bytes.length;
       if(length > 0)
       {
           for(int i = 0; i < length; i++)
           {
               byte b = bytes[i];
               if((b > 31 && b < 127) || (b>9 && b<14) )
                   sw.write((char)b);
           }

       }
       return sw.toString();
   }
   private boolean isSIPPacket(String pReqLine)
   {
	  // sLogger.debug("RequestLine:"+pReqLine);
	   boolean b = pReqLine.contains(Constants.sip_def);
	   //Pattern pattern=Pattern.compile(Constants.sip_def);
	   //Matcher m = pattern.matcher(pReqLine);
	   //boolean b = m.matches();
	   return b;
	   
   }
   private boolean isFromSection(String pReqLine)
   {
	   //Pattern pattern=Pattern.compile(Constants.fromSection);
	  // Matcher m = pattern.matcher(pReqLine);
	  // boolean b = m.matches();
	   boolean b = pReqLine.contains(Constants.fromSection);
	   return b;
   }
   private boolean isToSection(String pReqLine)
   {
	   //Pattern pattern=Pattern.compile(Constants.toSection);
	   //Matcher m = pattern.matcher(pReqLine);
	   //boolean b = m.matches();
	   boolean b = pReqLine.contains(Constants.toSection);
	   return b;
   }
   private String getFrom(String pSection)
   {
	   String from;
	   StringTokenizer tokenizer=new StringTokenizer(pSection," ");
	   String _from=tokenizer.nextToken();
	   String address=tokenizer.nextToken();
	   StringTokenizer t2= new StringTokenizer(address,";");
	   from=t2.nextToken();   
	   return from;
   }
   private String getTo(String pSection)
   {
	   String to;
	   StringTokenizer tokenizer=new StringTokenizer(pSection," ");
	   String _from=tokenizer.nextToken();
	   String address=tokenizer.nextToken();
	   to=address;	   
	   return to;
   }
   private String getCallId(String pSection)
   {
	   StringTokenizer tokenizer = new StringTokenizer(pSection," ");
	   String title = tokenizer.nextToken();
	   String callId = tokenizer.nextToken();
	   return callId;
   }
}
