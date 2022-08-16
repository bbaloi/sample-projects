package com.mule.connector.snmp.internal.source;

import java.io.IOException;



import java.io.InputStream;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.scheduler.Scheduler;
import org.mule.runtime.api.scheduler.SchedulerService;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.Streaming;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.source.BackPressure;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import static org.slf4j.LoggerFactory.getLogger;
import org.mule.runtime.extension.api.annotation.source.BackPressure;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import org.mule.runtime.extension.api.annotation.source.OnBackPressure;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.BackPressureContext;
import org.mule.runtime.extension.api.runtime.source.BackPressureMode;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.runtime.extension.api.runtime.source.SourceCompletionCallback;
import org.mule.runtime.extension.api.runtime.source.SourceResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.snmp4j.Snmp;

import com.mule.connector.snmp.api.vo.SNMPEvent;
import com.mule.connector.snmp.api.vo.SNMPListenerAttributes;
import com.mule.connector.snmp.internal.SNMPConfiguration;
import com.mule.connector.snmp.internal.util.SnmpUtil;

@Alias("snmpListener")
@EmitsResponse
@Streaming
@MediaType(value = ANY, strict = false)
@BackPressure(defaultMode = BackPressureMode.FAIL, supportedModes = {BackPressureMode.FAIL})
//@Sources(SNMPListener.class)
//@MetadataScope(outputResolver = HttpMetadataResolver.class)


public class SNMPListener extends Source<SNMPEvent, SNMPListenerAttributes> 
//public class SNMPListener extends Source<String, void> 
{
	 private static final Logger LOGGER = getLogger(SNMPListener.class);
	 
	 @Connection
	 private ConnectionProvider<SNMPListenerConnection> snmpServerProvider;
	 //private ConnectionProvider<SNMPListenerConnection> snmpConnection;
	 
	 @Inject
	  private SchedulerService schedulerService;
	 
	  private Scheduler scheduler;
	 
	@Config
    private SNMPConfigurationListener config;


    @Parameter
    private Integer port;
    /**
    * Comma separated list of allowed HTTP methods by this listener.
    */
    
    
    @Override
	public void onStart(SourceCallback<SNMPEvent, SNMPListenerAttributes> sourceCallback) throws MuleException
    {
		// TODO Auto-generated method stub
    	
    		LOGGER.info("----Starting the SNMP Listener----");
		
		//this.scheduler = schedulerService.cpuIntensiveScheduler();
		this.scheduler = schedulerService.cpuLightScheduler();
		
		//LOGGER.info("++++++++ Connection: "+snmpServerProvider.getClass() );
		
		
		SNMPMessageReceiver _mfRcvr =  new SNMPMessageReceiver(sourceCallback);
		 SNMPListenerConnection snmpListenerConnection = SnmpUtil.getInstance().getConnection();
		 
		 if(snmpListenerConnection != null)
		 {
			 Snmp _snmp = snmpListenerConnection.getSnmp();
			 if(_snmp !=null)
			 {
				 _snmp.addCommandResponder(_mfRcvr);
			 }
			 else
				 LOGGER.info("...SNMP object is NULL...");
			 
			
		 }
		 else
			 LOGGER.info("!!!!!! SNMP Listenre Connection NULL !!!");
		
		 _mfRcvr.setConnection(snmpListenerConnection);
		
		try 
		{
			this.scheduler.execute(_mfRcvr);
		}
		
		catch(Throwable t)
		{
			t.printStackTrace();
		}
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
		LOGGER.info("----Stopping the SNMP Listener----");
		this.scheduler.stop();
		
	}

	
	
}
