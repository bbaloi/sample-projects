package com.mule.connector.snmp.internal;

import org.mule.runtime.extension.api.annotation.Extension;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;

import static org.mule.runtime.api.meta.Category.COMMUNITY;

import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import com.mule.connector.snmp.internal.source.SNMPConfigurationListener;
import com.mule.connector.snmp.internal.source.SNMPListener;
import com.mule.connector.snmp.internal.source.SNMPListenerConnectionProvider;
//import com.mule.connector.snmp.internal.source.SNMPListenerProvider;



/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "snmp")
@Extension(name = "SNMP", vendor = "MuleSoft Inc.", category = COMMUNITY)
@Configurations({SNMPConfiguration.class,SNMPConfigurationListener.class})
//@Sources(SNMPListener.class)
//@ConnectionProviders({SNMPConnectionProvider.class, SNMPListenerConnectionProvider.class})
//@ConnectionProviders({SNMPConnectionProvider.class})

//@Operations({SNMPOperations.class})

public class SNMPExtension {

}
