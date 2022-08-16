package com.mule.connector.snmp.internal;

import org.mule.runtime.api.connection.ConnectionException;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
//import com.mule.connector.snmp.internal.SNMPSenderConnectionProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mule.connector.snmp.internal.util.SnmpUtil;


/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */

@DisplayName("SNMP Sender")
@Alias("snmp-sender")


public class SNMPConnectionProvider implements PoolingConnectionProvider<SNMPConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(SNMPConnectionProvider.class);

 /**
  * A parameter that is always required to be configured.
  */
  @Parameter
  private int requiredConnectionId;

 /**
  * A parameter that is not required to be configured by the user.
  */
  @DisplayName("Friendly Name")
  @Parameter
  @Optional(defaultValue = "SNMPSender")
  private String optionalParameter;
  
  @ParameterGroup(name="SNMPSender")
  private SNMPSenderConnectionProperties senderProperties;
  
  @Parameter
  @Optional(defaultValue = "username")
  private String username;
  
  @Parameter
  @Optional(defaultValue = "authpassphrase")
  private String authpassphrase;
  
  @Parameter
  @Optional(defaultValue = "privacypassphrase")
  private String privacypassphrase;
 
  

  public int getRequiredConnectionId() {
	return requiredConnectionId;
}

public void setRequiredConnectionId(int requiredConnectionId) {
	this.requiredConnectionId = requiredConnectionId;
}

  
  @Override
  public SNMPConnection connect() throws ConnectionException {
	  
	  LOGGER.info("---In SNMPConnectionProvider.connect()----");
	  SNMPConnection connection = new SNMPConnection(requiredConnectionId,senderProperties.getDestination_SNMP_host(), 
	    		senderProperties.getDestination_SNMP_port(),senderProperties.getSenderProtocol(),
	    		senderProperties.getSenderSnmpVersion(),senderProperties.getCommunity(),username,authpassphrase,privacypassphrase);
    
	  
	  
	  return connection;
  
  }

  @Override
  public void disconnect(SNMPConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(SNMPConnection connection) {
    return ConnectionValidationResult.success();
  }
  
  //-------
  public SNMPSenderConnectionProperties getSenderProperties() {
		return senderProperties;
	}
	
	public void setSenderProperties(SNMPSenderConnectionProperties senderProperties) {
		this.senderProperties = senderProperties;
	}
  
 
}
