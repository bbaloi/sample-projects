package com.mule.connector.snmp.internal.source;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.snmp4j.util.WorkerTask;

import com.mule.connector.snmp.internal.util.MuleSnmpConstants;
import com.mule.connector.snmp.internal.util.SnmpUtil;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class SNMPListenerConnection implements Runnable
{

	final Logger LOGGER = LoggerFactory.getLogger(SNMPListenerConnection.class);
	  
	 
  private final String id;
  private String sourceSnmpHosts;
  private String snmpRcvProtocol;
  private String snmpVersion;
  private String user;
  private String passphrase;
  private String privacypassphrase;
  
	private MultiThreadedMessageDispatcher dispatcher;
	private Snmp snmp = null;
	
	private Address listenAddress;
	private ThreadPool threadPool;
	private int n = 0;
	private long start = -1;

  

  public SNMPListenerConnection(String id,SNMPReceiverConnectionProperties pProps) {
    this.id = id;
    sourceSnmpHosts = pProps.getSource_SNMP_hosts();
    snmpRcvProtocol = pProps.getReceiverProtocol();
    snmpVersion = pProps.getSnmpVersions();
    user = pProps.getUsername();
    passphrase = pProps.getPassphrase();
    privacypassphrase = pProps.getPrivacypassphrase();
    
   
    try  
    {
    	init();
    }
    catch(Exception pExcp)
    {
    	pExcp.printStackTrace();
    }

  }

  public String getId() {
    return id;
  }

  public void invalidate() {
    // do something to invalidate this connection!
  }
 
  
  
  private void init() throws UnknownHostException, IOException {
	  
		threadPool = ThreadPool.create("Trap", 10);
		dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
		
		//TRANSPORT
		listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:0.0.0.0/162"));  //SET THIS
		
		TransportMapping<?> transport;
		if (listenAddress instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping(
					(UdpAddress) listenAddress);
				LOGGER.info("+++SNMP Listener Transport is UDP+++; listening on -  udp:0.0.0.0/162");
		} else {
			transport = new DefaultTcpTransportMapping(
					(TcpAddress) listenAddress);
			LOGGER.info("+++SNMP Listener Transport is TCP+++; listening on -  udp:0.0.0.0/162");
		}
		
		//V3 SECURITY
		USM usm = new USM(
				SecurityProtocols.getInstance().addDefaultProtocols(),
				new OctetString(MPv3.createLocalEngineID()), 0);
		
		SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES192());
	    SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES256());
	    SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

		usm.setEngineDiscoveryEnabled(true);
		
		SecurityModels.getInstance().addSecurityModel(usm);

		snmp = new Snmp(dispatcher, transport);
		
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));

	      
	      snmp.getUSM().addUser(    // SET THE SECURITY PROTOCOLS HERE
	    	        new OctetString(user),
	    	        new UsmUser(new OctetString(user),AuthMD5.ID, new OctetString(
	    	        		passphrase), PrivAES128.ID, new OctetString(privacypassphrase)));
		
	      //snmp.listen();
	}
  
  
  /*
  private void init() throws UnknownHostException, IOException 
  {
	  
	  
	  TransportMapping<?> transport;
	  
		threadPool = ThreadPool.create("Trap", 10);
		dispatcher = new MultiThreadedMessageDispatcher(threadPool,new MessageDispatcherImpl());
		
		//TRANSPORT
		
		if (snmpRcvProtocol.equals(MuleSnmpConstants.udp))
		{
			LOGGER.info("---SNMPConection is UDP !");
			listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:0.0.0.0/162"));  //SET THIS
			
		}
		else
		{
			LOGGER.info("---SNMPConection is TCP !");
			//create a TCP Based Listener
			listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "tcp:0.0.0.0/162"));  //SET THIS
			
		}
		
		
		if (listenAddress instanceof UdpAddress) {
			LOGGER.info("+++SNMP Listener Transport is UDP+++");
			transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
		} else {
			LOGGER.info("+++SNMP LIstener Transport is TCP +++");
			transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
		}
		
		
		//V3 SECURITY
		
		if (snmpVersion.equals(MuleSnmpConstants.snmpV3 ))
		{
			 LOGGER.info("+++It's a V3 listener instance+++");
			
			USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(),new OctetString(MPv3.createLocalEngineID()), 0);
			
			SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES192());
		    SecurityProtocols.getInstance().addPrivacyProtocol(new PrivAES256());
		    SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

			usm.setEngineDiscoveryEnabled(true);
			
			SecurityModels.getInstance().addSecurityModel(usm);

			
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
			snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3(usm));
			
		      
		      snmp.getUSM().addUser(    // SET THE SECURITY PROTOCOLS HERE
		    	        new OctetString(user),
		    	        new UsmUser(new OctetString(user),AuthMD5.ID, new OctetString(
		    	        		passphrase), PrivAES128.ID, new OctetString(privacypassphrase))); 
		      
		}
		
		 snmp = new Snmp(dispatcher, transport);
		
		LOGGER.info("----SNMP Connection established: listening----");
		snmp.listen();
  	}
  */
  
  	public String getSourceSnmpHosts() {
		return sourceSnmpHosts;
	}

	public void setSourceSnmpHosts(String sourceSnmpHosts) {
		this.sourceSnmpHosts = sourceSnmpHosts;
	}

	public String getSnmpRcvProtocol() {
		return snmpRcvProtocol;
	}

	public void setSnmpRcvProtocol(String snmpRcvProtocol) {
		this.snmpRcvProtocol = snmpRcvProtocol;
	}

	public String getSnmpVersion() {
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	public Snmp getSnmp() {
		return snmp;
	}

	public void setSnmp(Snmp snmp) {
		this.snmp = snmp;
	}

	public Address getListenAddress() {
		return listenAddress;
	}

	public void setListenAddress(Address listenAddress) {
		this.listenAddress = listenAddress;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		LOGGER.info("....SNMPListenerConnection Thread running ---");
		while(true)
		{
			/*
			try
			{
				
				//snmp.listen();
			}
			catch(IOException excp)
			{
				excp.printStackTrace();
			}
			*/
		}
		
	}



}
