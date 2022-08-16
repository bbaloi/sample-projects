package com.mule.connector.snmp.internal.source;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.api.lifecycle.Lifecycle;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.core.api.lifecycle.LifecycleUtils.initialiseIfNeeded;
import static org.mule.runtime.extension.api.annotation.param.ParameterGroup.ADVANCED;
//import static org.mule.extension.http.internal.HttpConnectorConstants.TLS_CONFIGURATION;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.SECURITY_TAB;


@DisplayName("SNMP Listener")
@Alias("snmp-listener")

public class SNMPListenerProvider implements CachedConnectionProvider<SNMPListenerConnection> {

	private final Logger LOGGER = LoggerFactory.getLogger(SNMPListenerConnectionProvider.class);

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
	  private SNMPReceiverConnectionProperties senderProperties;

	
	
	@Override
	public SNMPListenerConnection connect() throws ConnectionException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void disconnect(SNMPListenerConnection connection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ConnectionValidationResult validate(SNMPListenerConnection connection) {
		// TODO Auto-generated method stub
		return null;
	} 

}
