package com.mule.connector.snmp.internal;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SNMPSenderConnectionProperties {
	
	@Parameter
	  private String destination_SNMP_host;
	  
	@Parameter
	  @Optional(defaultValue="162")
	  private Integer destination_SNMP_port;
	
	@Parameter
	  private String senderSnmpVersion;
	
	//TCP/UDP
	  @Parameter
	  private String senderProtocol;
	  


	public String getCommunity() {
		return community;
	}


	public void setCommunity(String community) {
		this.community = community;
	}


	public void setSenderProtocol(String senderProtocol) {
		this.senderProtocol = senderProtocol;
	}


	@Parameter
	  private String community;
	
	public String getSenderProtocol() {
		return senderProtocol;
	}


	public void setProtocol(String protocol) {
		this.senderProtocol = protocol;
	}

		public String getSenderSnmpVersion() {
		return senderSnmpVersion;
	}


	public void setSenderSnmpVersion(String snmpVersion) {
		this.senderSnmpVersion = snmpVersion;
	}


		public String getDestination_SNMP_host() {
			return destination_SNMP_host;
		}
		
		
		public Integer getDestination_SNMP_port() {
			return destination_SNMP_port;
		}
		  public void setDestination_SNMP_host(String destination_SNMP_host) {
				this.destination_SNMP_host = destination_SNMP_host;
			}


			public void setDestination_SNMP_port(Integer destination_SNMP_port) {
				this.destination_SNMP_port = destination_SNMP_port;
			}
		

}
