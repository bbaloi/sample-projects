package com.mule.connector.snmp.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;


import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mule.connector.snmp.api.vo.SNMPEvent;

import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.snmp4j.smi.Address;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class SNMPOperations {
	
	private final Logger LOGGER = LoggerFactory.getLogger(SNMPConnection.class);


  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
	/*
  @MediaType(value = ANY, strict = false)
  public String retrieveInfo(@Config SNMPConfiguration configuration, @Connection SNMPConnection connection){
	 System.out.println("---In retriveInfo() operation...");
    return "Using Configuration [" + configuration.getConfigId() + "] with Connection id [" + connection.getId() + "]";
  }
  */
	
  @MediaType(value = ANY, strict = false)
  public String sendSNMPEventV1(@Config SNMPConfiguration configuration, @Connection SNMPConnection connection,  SNMPEvent pEvent){
 	  
	 LOGGER.info("---In sendSNMPEventV1() operation...");
	 String retVal = null;
	 
	 try {
		 retVal = connection.sendSNMPTrapV1(pEvent);
	 }
	 catch(Exception excp)
	 {
		 excp.printStackTrace();
	 }
    return retVal;
  }
  
  @MediaType(value = ANY, strict = false)
  public String sendSNMPEventV2(@Config SNMPConfiguration configuration, @Connection SNMPConnection connection,  SNMPEvent pEvent){
 	  
	  LOGGER.info("---In sendSNMPEventV2() operation...");
	 String retVal = null;
	 
	 try {
		 retVal = connection.sendSNMPTrapV2(pEvent);
	 }
	 catch(Exception excp)
	 {
		 excp.printStackTrace();
	 }
    return retVal;
  }
  
  @MediaType(value = ANY, strict = false)
  public String sendSNMPEventV3(@Config SNMPConfiguration configuration, @Connection SNMPConnection connection,  SNMPEvent pEvent){
 	  
	 LOGGER.info("---In sendSNMPEventV3() operation...");
	 String retVal = null;
	 
	 try {
		 retVal = connection.sendSNMPTrapV3(pEvent);
	 }
	 catch(Exception excp)
	 {
		 excp.printStackTrace();
	 }
	 
	
	 
	 
	 
	 
    return retVal;
  }

  /**
   * Example of a simple operation that receives a string parameter and returns a new string message that will be set on the payload.
   *//*
  @MediaType(value = ANY, strict = false)
  public String sayHi(String person) {
    return buildHelloMessage(person);
  }
  */

  /**
   * Private Methods are not exposed as operations
   */
  /*
  private String buildHelloMessage(String person) {
    return "Hello " + person + "!!!";
  }
  */
}
