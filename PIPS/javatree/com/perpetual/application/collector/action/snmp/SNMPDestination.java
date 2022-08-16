/*
 * SNMPDestination.java
 *
 * Created on December 9, 2003, 1:40 PM
 */

package com.perpetual.application.collector.action.snmp;

/**
 *
 * @author  brunob
 */
public class SNMPDestination 
{
    private String host=null;
    private Integer port=null;
    /** Creates a new instance of SNMPDestination */
    public SNMPDestination(String pHost,Integer pPort) 
    {
        host = pHost;
        port=pPort;
    }
    public String getDestHost()
    {
        return host;
    }
    public Integer getDestPort()
    {
        return port;
    }
}
