package com.mule.connector.snmp.internal.source;

import static org.slf4j.LoggerFactory.getLogger;

import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.runtime.extension.api.runtime.source.SourceCompletionCallback;
import org.mule.runtime.extension.api.runtime.source.SourceResult;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.message.Message.Builder;
import org.mule.runtime.api.message.Message.CollectionBuilder;
import org.mule.runtime.api.metadata.TypedValue;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.slf4j.Logger;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.PDUv1;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
//-------
import com.mule.connector.snmp.api.vo.SNMPEvent;
import com.mule.connector.snmp.api.vo.SNMPListenerAttributes;
import com.mule.connector.snmp.internal.util.MuleSnmpConstants;
import com.mule.connector.snmp.internal.util.SnmpUtil;
import com.mule.connector.snmp.api.vo.SNMPEventVariable;


public class SNMPMessageReceiver   implements Runnable,CommandResponder 
{
	
	 private static final Logger LOGGER = getLogger(SNMPMessageReceiver.class);
	 private SourceCallback sourceCallback;
	 private SNMPListenerConnection snmpListenerConnection;
	 private SNMPListenerConnectionProvider snmpListenerConnectionProvider;
	 

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		LOGGER.info("Got SNMP Message:");
		//sourceCallback.
		InputStream is ;
		
		/*
		SNMPEvent event= new SNMPEvent();
		event.setMessageId("123");
		event.setPayload("----SNMP Test Message-----");
		event.setSnmpVersion("v1");
		SNMPListenerAttributes attr = new SNMPListenerAttributes();
		Result<SNMPEvent, SNMPListenerAttributes> result =  Result.<SNMPEvent, SNMPListenerAttributes>builder().output(event).attributes(attr).build(); 
        this.sourceCallback.handle(result);
        */
        
	        try
	        {
	        	if(snmpListenerConnection == null)
	        		LOGGER.info("?????SNMP Connection is NULL");
	        	Snmp _snmp = snmpListenerConnection.getSnmp();
	        	if(_snmp ==null)
	        		LOGGER.info(" ??????SNMP is NULL");
	        	
	        	
	        	_snmp.addCommandResponder(this);
	        	
	        	_snmp.listen();
	        	
	        	LOGGER.info("?????SNMP LIstening !!!!!");
	        	//snmpListenerConnection.getSnmp().listen();
	        	
	        	
	        }
	        catch(IOException excp)
	        {
	        	excp.printStackTrace();
	        }
        
	        while(true);
        
        
	}

	public SNMPMessageReceiver(SourceCallback pSourceCallback)
	//public SNMPMessageReceiver(SourceCallback pSourceCallback,SNMPListenerConnectionProvider pSnmpListenerConnectionProvider)
	{
		LOGGER.info("###----Staring the SNMP Message Recevier on TCP/UDP-----");
		this.sourceCallback = pSourceCallback;
		
		
			snmpListenerConnection = SnmpUtil.getInstance().getConnection();
			if(snmpListenerConnection == null)
			{
				LOGGER.info("---SNMPConnection not created --- waiting ! ");
				while(SnmpUtil.getInstance().getConnection() == null)
				{
					try
					{
						Thread.sleep(1000);
					}
					catch(Exception excp)
					{
						excp.printStackTrace();
					}
				}
			}
			
		 LOGGER.info("---SNMPConnection created---- ! ");
		 
		
	}

	@Override
	public void processPdu(CommandResponderEvent pSnmpEvent) {
		
		SNMPEvent snmpEvent=null;
		ArrayList varList = new ArrayList();
		
		// TODO Auto-generated method stub
		LOGGER.info("---Got SNMP Trap: "+ pSnmpEvent.getPDU().toString());
		
		   PDU pdu = pSnmpEvent.getPDU();
		   
		   
		   int secLevel = pSnmpEvent.getSecurityLevel();
		   int secModel = pSnmpEvent.getSecurityModel();
		   LOGGER.info("---PDU Type: "+pdu.getType()+", Security Model:"+secModel+"Security Level:"+secLevel+ "----");
		   
		    if (pdu.getType() == PDU.V1TRAP && secModel == 1) {
		    	
		    	snmpEvent = buildSNMPEventV1(pdu,new String(pSnmpEvent.getSecurityName()));

		    } else if (pdu.getType() == PDU.TRAP && secModel == 2) {
		    	
		    	snmpEvent = buildSNMPEventV2(pdu,new String(pSnmpEvent.getSecurityName()));
		    	
		    }
		    else if (pdu.getType() == ScopedPDU.NOTIFICATION && secModel==3)
		    	snmpEvent = buildSNMPEventV3(pdu,new String(pSnmpEvent.getSecurityName()));
		    else
		    	LOGGER.info("---INVALID SNMP TYPE----"); 

		    Vector<? extends VariableBinding> varBinds = pdu.getVariableBindings();
		    
		    if (varBinds != null && !varBinds.isEmpty()) {
		      Iterator<? extends VariableBinding> varIter = varBinds.iterator();

		      StringBuilder resultset = new StringBuilder();
		      resultset.append("-----");
		      
		      while (varIter.hasNext()) {
		        VariableBinding vb = varIter.next();

		        String syntaxstr = vb.getVariable().getSyntaxString();
		        int syntax = vb.getVariable().getSyntax();
		        LOGGER.info( "OID: " + vb.getOid());
		        LOGGER.info("Value: " +vb.getVariable());	        
		        LOGGER.info("syntaxstring: " + syntaxstr );
		        LOGGER.info("syntax: " + syntax);
		        LOGGER.info("------");
		        
		        SNMPEventVariable snmpVar = new SNMPEventVariable();
		        snmpVar.setOid(vb.getOid().format());
		        snmpVar.setValue(vb.getVariable().toString());
		        snmpVar.setSyntaxstring(syntaxstr);
		        snmpVar.setSyntax(syntax);
		        
		        varList.add(snmpVar);
		        
		      }
		      
		      snmpEvent.setVariablList(varList);

		      
		    }
		    LOGGER.info("==== TRAP END ===");
		   
		    
			SNMPListenerAttributes attr = new SNMPListenerAttributes();
			
			Result<SNMPEvent, SNMPListenerAttributes> result =  Result.<SNMPEvent, SNMPListenerAttributes>builder().output(snmpEvent).attributes(attr).build(); 
			
			
		    this.sourceCallback.handle(result);
		}
		
	
	private SNMPEvent buildSNMPEventV1(PDU pdu, String pCommunity)
	{
		
		PDUv1 pduV1 = (PDUv1) pdu;
		
	      
		 SNMPEvent event= new SNMPEvent();
		 UUID uuid = UUID.randomUUID();
		 
			event.setMessageId(uuid.toString());
			event.setPayload("SNMP V1 TRAP");
			event.setAgentAddr( pduV1.getAgentAddress().toString());
			event.setEnterprise(pduV1.getEnterprise().toString());
			event.setTstamp(String.valueOf(pduV1.getTimestamp()));
			event.setGenericTrap(String.valueOf(pduV1.getGenericTrap()));
			event.setSpecificTrap(String.valueOf(pduV1.getSpecificTrap()));
			event.setSnmpVersion(MuleSnmpConstants.snmpV1);
			event.setPduType(String.valueOf(PDU.V1TRAP));
			event.setCommunity(pCommunity);
			event.setRequestId(String.valueOf(pduV1.getRequestID()));
			//pduV1.getType()
			
		
		  LOGGER.info("agentAddr " + pduV1.getAgentAddress().toString());
	      LOGGER.info("enterprise " + pduV1.getEnterprise().toString());
	      LOGGER.info("timeStamp" + String.valueOf(pduV1.getTimestamp()));
	      LOGGER.info("genericTrap"+ String.valueOf(pduV1.getGenericTrap()));
	      LOGGER.info("specificTrap " + String.valueOf(pduV1.getSpecificTrap()));
	      //LOGGER.info("snmpVersion " + String.valueOf(PDU.V1TRAP));
	      //LOGGER.info("communityString " + new String(pSnmpEvent.getSecurityName()));
	      LOGGER.info("communityString " + pCommunity);
	      LOGGER.info("type:  " + pdu.getType());
		      
			
		return event;
	}
	private SNMPEvent buildSNMPEventV2(PDU pdu,String pCommunity)
	{
		
		 SNMPEvent event= new SNMPEvent();
		 UUID uuid = UUID.randomUUID();
		 
		event.setMessageId(uuid.toString());
		event.setPayload("SNMP V2 TRAP");
		event.setSnmpVersion(MuleSnmpConstants.snmpV2);
		event.setPduType(String.valueOf(PDU.TRAP));
		event.setErrorStatus(String.valueOf(pdu.getErrorStatus()));
		event.setErrorIndex(String.valueOf(pdu.getErrorIndex()));
		event.setRequestId(String.valueOf(pdu.getRequestID()));
		event.setCommunity(pCommunity);
	
		LOGGER.info("");
    	LOGGER.info("===== NEW SNMP 2 TRAP RECEIVED ====");
    	LOGGER.info("errorStatus " + String.valueOf(pdu.getErrorStatus()));
    	LOGGER.info("errorIndex "+ String.valueOf(pdu.getErrorIndex()));
    	LOGGER.info("requestID " +String.valueOf(pdu.getRequestID()));
    	//LOGGER.info("snmpVersion " + String.valueOf(PDU.TRAP));
    	LOGGER.info("communityString " + pCommunity);
    	LOGGER.info("type:  " + pdu.getType());

		
		return event;
	}
	
	private SNMPEvent buildSNMPEventV3(PDU pdu,String pCommunity)
	{
		
		 SNMPEvent event= new SNMPEvent();
		 UUID uuid = UUID.randomUUID();
		 
		event.setMessageId(uuid.toString());
		event.setPayload("----SNMP V3 Message-----");
		event.setSnmpVersion(MuleSnmpConstants.snmpV3);
		event.setPduType(String.valueOf(ScopedPDU.NOTIFICATION));
		event.setErrorStatus(String.valueOf(pdu.getErrorStatus()));
		event.setErrorIndex(String.valueOf(pdu.getErrorIndex()));
		event.setRequestId(String.valueOf(pdu.getRequestID()));
		event.setCommunity(pCommunity);
	
		LOGGER.info("");
    	LOGGER.info("SNMP V3 TRAP");
    	LOGGER.info("errorStatus " + String.valueOf(pdu.getErrorStatus()));
    	LOGGER.info("errorIndex "+ String.valueOf(pdu.getErrorIndex()));
    	LOGGER.info("requestID " +String.valueOf(pdu.getRequestID()));
    	//LOGGER.info("snmpVersion " + String.valueOf(PDU.NOTIFICATION));
    	LOGGER.info("communityString " + pCommunity);
    	LOGGER.info("type:  " + pdu.getType());

    	
    	
		
		return event;
	}
	
	public void setConnection(SNMPListenerConnection connection)
	{
		this.snmpListenerConnection = connection;
	}

	

	
}
