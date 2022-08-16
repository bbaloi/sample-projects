package com.mule.connector.snmp.internal;

import java.io.IOException;

import java.net.InetAddress;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.mule.connector.snmp.api.vo.SNMPEvent;
import com.mule.connector.snmp.internal.util.MuleSnmpConstants;
import com.mule.connector.snmp.internal.util.PDUBuilder;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class SNMPConnection {
	
	private final Logger LOGGER = LoggerFactory.getLogger(SNMPConnection.class);
	
	private TransportMapping<?> transport;
	//private TransportMapping<?> transportUdp = new DefaultUdpTransportMapping();
	//private TransportMapping<?> transport = new DefaultTcpTransportMapping();
	 
	  private   InetAddress inetAddress=null;
	  private   String community = "public";      //SET THIS
	  private   String trapOid = ".1.3.6.1.2.1.1.6";
	  private   String ipAddress = "localhost";     //SET THIS (this is the destination address)
	  private   int port = 162;
    

	  private int id;
	  private String host;
	  private String protocol;
	  private int snmpVersionInt;
	  private String snmpVersion;
	  private CommunityTarget comtarget;
	  private Snmp snmp;
	  private String enterpriseOid;
	  private String username;
	  private String authpassphrase;
	  private String privacypassphrase;
	  
  
  public int getId() {
	return id;
  }

	public void setId(int connectionId) {
		this.id = connectionId;
	}



  public SNMPConnection(int Id,String host,int port,String protocol,String pVersion, String pCommunity,String pUser,String pPassphrase, String pPricayPassPhrase) {
   this.id = Id;
    this.host=host;
    this.port = port;
    this.protocol = protocol;
    this.snmpVersion = pVersion;
    if(pCommunity!=null || ! pCommunity.equals(""))
    	 this.community = pCommunity;
    
    if(pVersion.equals(MuleSnmpConstants.snmpV1))
    	this.snmpVersionInt = 1;
    if(pVersion.equals(MuleSnmpConstants.snmpV2))
    	this.snmpVersionInt = 2;
    if(pVersion.equals(MuleSnmpConstants.snmpV3))
    	this.snmpVersionInt = 3;
    
    if(host==null)
    	ipAddress = PDUBuilder.getInstance().getIpAddress();
    else 
    	ipAddress = host;
    
     username = pUser;
     authpassphrase = pPassphrase;
      privacypassphrase = pPricayPassPhrase;

    LOGGER.info("---Creating Connection SNMPConnection: host="+ipAddress+",port:"+port+",protocol:"+protocol+",Community:"+community);
    init(protocol,snmpVersionInt,ipAddress,port);
    
    
  }

  public void invalidate() {
    // do something to invalidate this connection!
	  try
	  {
		  snmp.close();
	  }
	  catch(Exception excp)
	  {
		  excp.printStackTrace();
	  }
  }
  
  public String sendSNMPTrapV1(SNMPEvent event)
  {
	  String retVal="";
	  
	  LOGGER.info("----In SNMPConnection.sendSNMPTrapV1()");
	  int specificTrap,genericTrap, requestId;
	  
	  
	  if(event.getSpecificTrap()!=null)
		  specificTrap =  Integer.parseInt(event.getSpecificTrap());
	  else
		  specificTrap=0;
	  
	  if(event.getGenericTrap() != null)
		  genericTrap = Integer.parseInt(event.getGenericTrap());
	  else
		  genericTrap = 0;
	  if(event.getRequestId()!=null)
		  requestId = Integer.parseInt(event.getRequestId());
	  requestId=0;
	  
	  
	  PDUBuilder builder = PDUBuilder.getInstance();
	  
	  PDU snmpPDU = builder.createPdu(snmpVersion, community, event.getEnterpriseOid(), event.getTrapOid(), 
			 specificTrap, genericTrap, requestId,ipAddress);

	  						 
	  try
	  {
		// Create Target
	      CommunityTarget comtarget = new CommunityTarget();
	      comtarget.setCommunity(new OctetString(community));
	      LOGGER.info("##### SNMP version: "+SnmpConstants.version1);
	      comtarget.setVersion(SnmpConstants.version1);
	      comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
	      comtarget.setRetries(2);
	      comtarget.setTimeout(5000);
	      
		  snmp.send(snmpPDU, comtarget);
		  
	  }
	  catch(IOException excp)
	  {
		  excp.printStackTrace();
	  }
	  catch(Exception excp)
	  {
		  excp.printStackTrace();
	  }
	  
	  return retVal;
  }
  public String sendSNMPTrapV2(SNMPEvent event)
  {
	  String retVal="";
	  
	  LOGGER.info("----In SNMPConnection.sendSNMPTrapV2()");
	  
	  int specificTrap,genericTrap, requestId;
	  
	  
	  if(event.getSpecificTrap()!=null)
		  specificTrap =  Integer.parseInt(event.getSpecificTrap());
	  else
		  specificTrap=0;
	  
	  if(event.getGenericTrap() != null)
		  genericTrap = Integer.parseInt(event.getGenericTrap());
	  else
		  genericTrap = 0;
	  if(event.getRequestId()!=null)
		  requestId = Integer.parseInt(event.getRequestId());
	  requestId=0;
	  
	  
	  PDUBuilder builder = PDUBuilder.getInstance();
	  
	  PDU snmpPDU = builder.createPdu(snmpVersion, community, event.getEnterpriseOid(), event.getTrapOid(), 
			  Integer.parseInt(event.getSpecificTrap()), Integer.parseInt(event.getGenericTrap()), 
			  Integer.parseInt(event.getRequestId()),ipAddress);
	  
	  
	  
	  try
	  {
		// Create Target
	      CommunityTarget comtarget = new CommunityTarget();
	      comtarget.setCommunity(new OctetString(community));
	      LOGGER.info("##### SNMP version: "+SnmpConstants.version2c);
	      comtarget.setVersion(SnmpConstants.version2c);
	      comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
	      comtarget.setRetries(2);
	      comtarget.setTimeout(5000);
	      
		  snmp.send(snmpPDU, comtarget);
		  
	  }
	  catch(IOException excp)
	  {
		  excp.printStackTrace();
	  }
	  catch(Exception excp)
	  {
		  excp.printStackTrace();
	  }
	  
	  
	  return retVal;
  }
  
  public String sendSNMPTrapV3(SNMPEvent event)
  {
	  String retVal="";
	  Address targetAddress;
	  TransportMapping<?> transport;
	  
	  int specificTrap,genericTrap, requestId;
	  
	  
	  if(event.getSpecificTrap()!=null)
		  specificTrap =  Integer.parseInt(event.getSpecificTrap());
	  else
		  specificTrap=0;
	  
	  if(event.getGenericTrap() != null)
		  genericTrap = Integer.parseInt(event.getGenericTrap());
	  else
		  genericTrap = 0;
	  if(event.getRequestId()!=null)
		  requestId = Integer.parseInt(event.getRequestId());
	  requestId=0;
	  
	  LOGGER.info("----In SNMPConnection.sendSNMPTrap V3()");
	  
	  try
	  {
	  
	//  PDU snmpPDU = PDUBuilder.createPdu(snmpVersion, community, event.getEnterpriseOid(), event.getTrapOid(), Integer.parseInt(event.getSpecificTrap()), Integer.parseInt(event.getGenericTrap()), Integer.parseInt(event.getRequestId()));
		  //ipAddress = PDUBuilder.getInstance().getIpAddress();
			 
	  	if(protocol.equals(MuleSnmpConstants.udp))
	  	{
	  			LOGGER.info("-----UDP for V3 message----");
	  			targetAddress = GenericAddress.parse("udp:" + ipAddress + "/" + port);
	  			//transport = new DefaultUdpTransportMapping();     
	  			
	  	}
	  	else
	  	{
	  		LOGGER.info("-----TCP for V3 Message for ----");
	  		targetAddress = GenericAddress.parse("tcp:" + ipAddress + "/" + port);
	  		//transport = new DefaultTcpTransportMapping();    
	  		
	  	}
	  	
		      //Snmp snmp = new Snmp(transport);
		       
		      USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(),new OctetString(MPv3.createLocalEngineID()), 0);
		      
		      SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES192());
		      SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES256());
		      SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());
		      
		      SecurityModels.getInstance().addSecurityModel(usm);    
		      
		      
		      snmp.getUSM().addUser(    //SET THE USERNAME, PROTOCOLS, PASSPHRASES
		    	        new OctetString(username),
		    	        new UsmUser(new OctetString(username), AuthMD5.ID, new OctetString(
		    	            authpassphrase), PrivAES128.ID, new OctetString(privacypassphrase)));
		      
		 
		      // Create Target
		      UserTarget target = new UserTarget();
		      target.setAddress(targetAddress);
		      target.setRetries(1);
		      target.setTimeout(11500);
		      target.setVersion(SnmpConstants.version3);
		      target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
		      target.setSecurityName(new OctetString(username));

		      
		      PDUBuilder builder = PDUBuilder.getInstance();
		      PDU snmpPDU = builder.createPdu(snmpVersion, community, event.getEnterpriseOid(), event.getTrapOid(),
					  Integer.parseInt(event.getSpecificTrap()), Integer.parseInt(event.getGenericTrap()),
					  Integer.parseInt(event.getRequestId()),ipAddress);
		      

		      // Send the PDU
		      snmp.send(snmpPDU, target);
		      System.out.println("Sending V3 Trap to (IP:Port)=> " + ipAddress + ":"+ port);

		    } 
	      catch(IOException excp)
		  {
			  excp.printStackTrace();
		  }
  			catch (Exception e) {
		      System.err.println("Error in Sending Trap to (IP:Port)=> " + ipAddress
		          + ":" + port);
		      System.err.println("Exception Message = " + e.getMessage());
		    }
		  
		
	  
	  return retVal;
  }
  //-------------
  
  
  public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	private void init(String protocol,int pVersion,String host,int port)
	{
		try
		{
			if(protocol.equals(MuleSnmpConstants.udp))
			{
				LOGGER.info(".....Sending UDP Traps.....");
				 
				transport = new DefaultUdpTransportMapping();
				
			      
			}
			else
			{
				transport = new DefaultTcpTransportMapping();
			}
			
	
			//ipAddress = PDUBuilder.getInstance().getIpAddress();
		    
			  comtarget = new CommunityTarget();
		      comtarget.setCommunity(new OctetString(community));
		      comtarget.setVersion(pVersion);
		      comtarget.setAddress(new UdpAddress(ipAddress + "/" + port));
		      comtarget.setRetries(2);
		      comtarget.setTimeout(5000);
		    
			
			snmp = new Snmp(transport);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
	}
	
}
