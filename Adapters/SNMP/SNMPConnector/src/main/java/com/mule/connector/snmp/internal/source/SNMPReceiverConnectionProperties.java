package com.mule.connector.snmp.internal.source;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;

public class SNMPReceiverConnectionProperties {
	
	//TCP/UDP
	  @Parameter
	  private String receiverProtocol;

	//list of SNMP hosts that are supported: 
	  // '*' = all SNMP emmiters
	  // 123.456.789.001/23,333.222.111.222 -> list of IP sources
	  @Parameter
	  private String source_SNMP_hosts;
	 
	  @Parameter
	  private String snmpVersions;
	  
	  @Parameter
	  @Optional(defaultValue="username")
	  private String username;
	  
	  @Parameter
	  @Optional(defaultValue="authpassphrase")
	  private String passphrase;
	  
	  @Parameter
	  @Optional(defaultValue="privacypassphrase")
	  private String privacypassphrase;
	  
	  
	  
	  
	  public String getUsername() {
			return username;
		}



		public void setUsername(String username) {
			this.username = username;
		}



		public String getPassphrase() {
			return passphrase;
		}



		public void setPassphrase(String password) {
			this.passphrase = password;
		}



		public String getPrivacypassphrase() {
			return privacypassphrase;
		}



		public void setPrivacypassphrase(String privacypassphrase) {
			this.privacypassphrase = privacypassphrase;
		}

	  
	  public String getReceiverProtocol() {
		return receiverProtocol;
		}
		
		
		
		public String getSource_SNMP_hosts() {
			return source_SNMP_hosts;
		}
		
		
		public String getSnmpVersions() {
			return snmpVersions;
		}
		  public void setReceiverProtocol(String protocol) {
				this.receiverProtocol = protocol;
			}



			public void setSource_SNMP_hosts(String source_SNMP_hosts) {
				this.source_SNMP_hosts = source_SNMP_hosts;
			}



			public void setSnmpVersions(String snmpVersions) {
				this.snmpVersions = snmpVersions;
			}


}
