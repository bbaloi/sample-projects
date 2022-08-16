package com.mule.connector.snmp.internal.source;

import org.mule.runtime.api.connection.ConnectionException;


import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mule.connector.snmp.internal.SNMPSenderConnectionProperties;
import com.mule.connector.snmp.internal.util.SnmpUtil;
import org.snmp4j.util.ThreadPool;


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

@DisplayName("SNMP Listener")
@Alias("snmp-listener")


public class SNMPListenerConnectionProvider implements PoolingConnectionProvider<SNMPListenerConnection>
{

  private final Logger LOGGER = LoggerFactory.getLogger(SNMPListenerConnectionProvider.class);
  private SNMPListenerConnection snmpListenerConnection=null;
  private ExecutorService tp;
  
 /**
  * A parameter that is always required to be configured.
  */
 
 /**
  * A parameter that is not required to be configured by the user.
  */
  
  @Parameter
  private int requiredParameter;
  
  @DisplayName("Friendly Name")
  @Parameter
  @Optional(defaultValue = "SMPM Listener")
  private  String optionalParameter;
  
  @ParameterGroup(name="SNMPReceiver")
  private SNMPReceiverConnectionProperties receiverProperties;

  @Override
  public SNMPListenerConnection connect() throws ConnectionException {
	  
	  LOGGER.info("---Conneting to SNMP  Service; listening for SNMP traps ------");
	  snmpListenerConnection = new SNMPListenerConnection(requiredParameter + ":" + optionalParameter,receiverProperties);
	  
	  SnmpUtil.getInstance().setConnection(snmpListenerConnection);
	  
	 // tp = Executors.newFixedThreadPool(3);
	  //tp.execute( snmpListenerConnection);
	  
	  return snmpListenerConnection;
  }

  public SNMPListenerConnection getSnmpListenerConnection() {
	return snmpListenerConnection;
}

public void setSnmpListenerConnection(SNMPListenerConnection snmpListenerConnection) {
	this.snmpListenerConnection = snmpListenerConnection;
}

@Override
  public void disconnect(SNMPListenerConnection connection) {
    try {
    	 	LOGGER.info("---Disconneting to SNMP Service------");
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(SNMPListenerConnection connection) {
    return ConnectionValidationResult.success();
  }
}
