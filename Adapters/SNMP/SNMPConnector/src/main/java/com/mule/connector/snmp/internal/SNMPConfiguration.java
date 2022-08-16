package com.mule.connector.snmp.internal;

import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Example;

import com.mule.connector.snmp.internal.source.SNMPListener;
import com.mule.connector.snmp.internal.source.SNMPListenerConnectionProvider;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */

@Configuration(name="snmp-sender-config") 
@ConnectionProviders({SNMPConnectionProvider.class})
@Operations(SNMPOperations.class)


public class SNMPConfiguration {

  @Parameter
  @Example("123")
  private String configId;
  
  public String getConfigId(){
	    return configId;
	  }
  
  //@ParameterGroup(name="SNMPSender")
  //private SNMPSenderConnectionProperties senderProperties;
  
  //@ParameterGroup(name="SNMPReceiver")
  //private SNMPReceiverConnectionProperties receiverProperties;
  
	
	/*
	public SNMPReceiverConnectionProperties getReceiverProperties() {
		return receiverProperties;
	}
	
	
	public void setReceiverProperties(SNMPReceiverConnectionProperties receiverProperties) {
		this.receiverProperties = receiverProperties;
	}
	 */

	
  
  
  
}
