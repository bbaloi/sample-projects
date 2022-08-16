package com.mule.connector.snmp.internal.util;

import java.net.InetAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import com.mule.connector.snmp.internal.SNMPConnection;

public class PDUBuilder
{
	
	  private final static Logger LOGGER = LoggerFactory.getLogger(PDUBuilder.class);
	
	  private  static InetAddress inetAddress=null;
	  private  static String community = "public";      //SET THIS
	  private  static String trapOid = ".1.3.6.1.2.1.1.6";
	  private  static String ipAddress = "localhost";     //SET THIS (this is the destination address)
	  private 	static final int port = 162;
	  
	  
	  private static PDUBuilder instance =null;
	  
	  public static PDUBuilder getInstance()
	  {
		  if(instance==null)
			  instance = new PDUBuilder();
		  return instance;
	  }
	  
	  public static InetAddress getInetAddress() {
		return inetAddress;
	}

	public static void setInetAddress(InetAddress inetAddress) {
		PDUBuilder.inetAddress = inetAddress;
	}

	public static String getIpAddress() {
		return ipAddress;
	}

	public static void setIpAddress(String ipAddress) {
		PDUBuilder.ipAddress = ipAddress;
	}

	public static int getPort() {
		return port;
	}

	private void PDUBuilder()
	  {
		  try
			{
			
				inetAddress = InetAddress.getLocalHost();
				ipAddress = inetAddress.getHostAddress();
				
				LOGGER.info("+++IP Address:- " + inetAddress.getHostAddress() + " , Host Name:- " + inetAddress.getHostName());
				
			}
			catch(Exception excp)
			{
				excp.printStackTrace();
			}
	  }
	
	 public PDU createPdu(String snmpVersion, String pCommunity,String pEntepriseOid,String  pTrapOid,  int pSpecificTrap,
			 int pGenericTrap, int pRequestId,String pAddress) 
	 {
		    
		PDU pdu=null;
		
		String _ipAddr=null;
		
		if(pCommunity !=null)
			community = pCommunity;
		if(pTrapOid != null)
			trapOid = pTrapOid;
		
		if(ipAddress==null)
			_ipAddr = ipAddress;
		else
			_ipAddr=pAddress;
		
	
		LOGGER.info("+++Creating PDU with vars: snmpVersion="+snmpVersion+",Community:"+pCommunity+",EntOid:"+pEntepriseOid+
				",TrapOid:"+pTrapOid+",SpecificTrap:"+pSpecificTrap+",GenericTrap:"+pGenericTrap+",ReqId:"+pRequestId);
		
		
		
	    if (snmpVersion.equals(MuleSnmpConstants.snmpV1)) 
	    {
	      
	    	LOGGER.info("***Creating PDU V1");
	    	PDUv1 pdu1 = new PDUv1();
	    	//LOGGER.info("=====new pdu1=====");
	    	pdu1.setType(PDU.V1TRAP);
	    	//pdu1.setEnterprise(new OID("1.3.6.1.4.1.1824"));
	    	pdu1.setEnterprise(new OID(pEntepriseOid));    	
	    	//pdu1.setAgentAddress(new IpAddress(ipAddress)); 
	    	pdu1.setAgentAddress(new IpAddress(_ipAddr)); 
	    	//pdu1.setSpecificTrap(5);
	        //pdu1.setGenericTrap(23);
	    	pdu1.setSpecificTrap(pSpecificTrap);
	    	pdu1.setGenericTrap(pGenericTrap);
	    	pdu = pdu1;
	      
	    } 
	    else  if (snmpVersion.equals(MuleSnmpConstants.snmpV2)) 
	    {
	    	
	    LOGGER.info("***Creating PDU V2");
	      PDU pdu2 = new PDU();
	    
	     // ScopedPDU pdu2 = new ScopedPDU();
	      //pdu2.setType(ScopedPDU.NOTIFICATION);
	      pdu2.setType(PDU.TRAP);
	      pdu2.setRequestID(new Integer32(pRequestId));
	      
	     // pdu2.setType(PDU.NOTIFICATION);

	      
	      pdu= pdu2;
	    }
	    else
	    {
	    	LOGGER.info("***Creating PDU V3");
		      //PDU pdu3 = new PDU();
		    
		      ScopedPDU pdu3 = new ScopedPDU();
		      pdu3.setType(ScopedPDU.NOTIFICATION);
		      pdu3.setRequestID(new Integer32(pRequestId));
		      
		      pdu= pdu3;
	    }
	   
	    pdu.add(new VariableBinding(SnmpConstants.sysUpTime,new OctetString(new Date().toString())));
	    pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
	    pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));
	    pdu.add(new VariableBinding(new OID(trapOid), new OctetString("TrapOid - Major")));
	    
	    
	    
	    return pdu;
	  }

}
