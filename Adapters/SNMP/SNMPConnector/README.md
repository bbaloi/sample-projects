Created By: Bruno Baloi


# SNMP Connector/Extension

This is a prototypal Mule Connector for SNMP. It is bi-directional in that it supports both the sending and reception of SNMP Traps (v1,v2c, v3).

### NOTE: 
This is not a production ready artifact. It is a POC that can be used to test out how SNMP traps can for instance be ingested and then forwarded upstream via a wide array of messaging mechanism (see accompanying Mule project that illustrates the forwarding of SNMP Traps over Kafka).

In order to test out the connector perform the following steps:

1) copy the current project into your Mule Studio Workspace

2) Import the Project into Studio
  Note: do not import it as an Anypoint project but rather as a Eclipse project (Import -> General -> Existing Projet into 
  workspace)

3) Install the Connector project into the local Maven repo

   Got to the root of the Mule Connector project (i.e. SNMPConnector), where the POM file exists and execute

    mvn clean install -DskipTests









...


...


Add this dependency to your application pom.xml

```
<groupId>SNMPConnectorGroup</groupId>
<artifactId>SNMPConnector</artifactId>
<version>1.0.0</version>
<classifier>mule-plugin</classifier>
```
