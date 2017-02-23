/*
 * RV JCA synchronous component
 @bbaloi
 */

package com.tibco.rv.jca.ra.synch;

import javax.resource.spi.ConnectionRequestInfo;
import com.tibco.rv.jca.util.Constants;

/**
 * This implementation class enables a resource adapter to pass its own
 * request-specific data structure across connection request flow.
 *
 * It supports the most common ones: user name and password. The
 * sub-classes should extend if there are more. If the sub-class
 * does not use user name or password, then it should not extend
 * from this class.
 *
 */
public class RVConnectionRequestInfo   implements ConnectionRequestInfo 
{
    private String	network;
    private String  daemon;
    private String  service;
    private String  userName;
    private String  password;

    /**
     * Constructs a ConnectionRequestInfoFW object.
     */
    public RVConnectionRequestInfo() {
	    service = Constants.DEFAULT_RV_SERVICE;
    	network = Constants.DEFAULT_RV_NETWORK;
        daemon = Constants.DEFAULT_RV_DAEMON;       
    }
    public RVConnectionRequestInfo(String pService,String pNetwork,String pDaemon) {
	    service = pService;
    	network = pNetwork;
        daemon = pDaemon;       
    }
    public RVConnectionRequestInfo(String pUserName,String pPassword)
    {    	
    	userName=pUserName;
    	password=pPassword;
    	service = Constants.DEFAULT_RV_SERVICE;
    	network = Constants.DEFAULT_RV_NETWORK;
        daemon = Constants.DEFAULT_RV_DAEMON; 
    }
    public RVConnectionRequestInfo(String pUserName,String pPassword,String pService,String pNetwork,String pDaemon) {
	    service = pService;
    	network = pNetwork;
        daemon = pDaemon;       
        userName=pUserName;
        password=pPassword;
    }
   
	/**
	 * @return Returns the daemon.
	 */
	public String getDaemon() {
		return daemon;
	}
	/**
	 * @param daemon The daemon to set.
	 */
	public void setDaemon(String daemon) {
		this.daemon = daemon;
	}
	/**
	 * @return Returns the network.
	 */
	public String getNetwork() {
		return network;
	}
	/**
	 * @param network The network to set.
	 */
	public void setNetwork(String network) {
		this.network = network;
	}
	/**
	 * @return Returns the service.
	 */
	public String getService() {
		return service;
	}
	/**
	 * @param service The service to set.
	 */
	public void setService(String service) {
		this.service = service;
	}
	
    /**
     * Returns whether the given object is equal to this
     * ConnectionRequestInfoFW object.
     *
     * @param	object			the object to be compared
     *
     * @return	true => equal; false => not equal
     */
    public boolean equals(Object object) 
    {
		if (object == this)
		    return true;
	
		if (object == null)
		    return false;
	
		if(object instanceof RVConnectionRequestInfo)
		{
			if(service.equals(((RVConnectionRequestInfo)object).getService()) 
				&& 	network.equals(((RVConnectionRequestInfo)object).getNetwork()) 
				&& daemon.equals(((RVConnectionRequestInfo)object).getDaemon()))
				return true;
		}
				
		return false;
		
    }

    /**
     * Returns the hash code of this ConnectionRequestInfoFW object.
     *
     * @return	the hash code of this ConnectionRequestInfoFW object
     */
    public int hashCode() {
	return (service + network + daemon).hashCode();
    }
}
