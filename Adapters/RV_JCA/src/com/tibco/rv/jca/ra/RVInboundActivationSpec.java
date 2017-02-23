/*
 * Copyright 2005 Sun TIBCO Inc. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.tibco.rv.jca.ra;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.resource.spi.InvalidPropertyException;

/**
 * This class implements the Activation Spec class 
 * of the Sample inbound connector
 * 
 * @author Bruno Baloi
 *
 */

import javax.resource.*;
import javax.resource.spi.*;

/** 
 * JavaMail Resource Adapter activation spec.
 */

public class RVInboundActivationSpec implements javax.resource.spi.ActivationSpec, java.io.Serializable
{	
	 static final String className = RVInboundActivationSpec.class.getName();
	 private static Logger sLogger;
	 private static final String rbName = null; // No localization is used for logging.
	 
	 static
	 { 
			 try 
			 {
		        sLogger = Logger.getLogger(className, rbName);
			 }
			 catch (Exception e) 
			 {
		        // Problem initializing logging.  Continue anyway.
			 }
	 }
	
	
    private RVInboundResourceAdapter resourceAdapter = null;

    //------------------common values----------------------
    /** daemon property value */
    private java.lang.String daemon;
    
    /** network property value */
    private java.lang.String network ;
    
    /** service property value */
    private java.lang.String service;
    
    /** subject property value */
    private java.lang.String subject;
    
    /** Normally imap or mode -ft/dq */
    private java.lang.String mode ;
    
    //--------------------dq values-----------------------
    /** dq worker weight */
    private String dqWeight;
    
    /**  dq - number of acceptable tasks*/
    private String dqTasks;
    
    /** dq Scheduler Weight */
    private String  dqSchedulerWeight ;
    
    /** dq Scheudler Heartbeat */
    private String dqSchedulerHeartBeat;
    
    /** Dq scheudler activation */
    private String dqSchedulerActivation;    
    
    //--------------------ft values------------------------
    
    /** FT group name**/
    private String ftGroup;
    /** FT weight **/
    private String ftWeight;
    /** is FT server the active one**/
    private String ftActive;
    /** number of FT active members in the group  - default is "1"**/
    private String ftActiveGoal;
    /**FT heart beat**/
    private String ftHeartBeat;
    /**FT prepareation interval**/
    private String ftPrepInterval;
    /**FT activation interval**/
    private String  ftActivationInterval; 
//  -------------------------------------------------------
    private Pattern pattern=null;
    
    
    /** 
     * Creates a new instance of the base activation spec.
     */

    public RVInboundActivationSpec() { }

    /** 
     * Validates the configuration properties
     * TBD: verify that a connection to the mail server can be done
     * @exception    javax.resource.spi.InvalidPropertyException
     */
    public void validate() throws javax.resource.spi.InvalidPropertyException 
    { 
    	try
    	{	
    		sLogger.log(Level.INFO,"validating ActivationSpec !");
    		//check the daemon,network,service params & subject for validity
    		//pattern=Pattern.compile(subject);
    	}
    	catch(PatternSyntaxException excp)
    	{
    		throw new InvalidPropertyException("invalid subject syntax !");
    	}
    	
    }
    public boolean accepts(String pSubjectName) throws javax.resource.spi.InvalidPropertyException
    {
    	if(pattern==null)
    	{
    		validate();
    	}
    	return pattern.matcher(subject).matches();
    }

    public void setResourceAdapter(javax.resource.spi.ResourceAdapter ra)
        throws ResourceException
    {
            
        final String methodName = "setResourceAdapter";
        try {
            sLogger = Logger.getLogger(className, rbName);
        } catch (Exception e) {
            // Problem initializing logging.  Continue anyway.
        }

        if (sLogger.isLoggable(Level.FINER)) {
          sLogger.entering(className, methodName);
        }

        if (resourceAdapter != null) {
            throw new ResourceException("A ResourceAdpater is already associated with this ActivationSpec instance.");
        }
        resourceAdapter = (RVInboundResourceAdapter) ra;
        if (sLogger.isLoggable(Level.FINER)) {
            sLogger.logp(Level.INFO, className, methodName, "Setting Resource Adapter: " + resourceAdapter);
            sLogger.exiting(className, methodName);
        }

    }

    public ResourceAdapter getResourceAdapter()
    {
        return resourceAdapter;
    }

	/**
	 * @return Returns the daemon.
	 */
	public java.lang.String getDaemon() {
		return daemon;
	}
	/**
	 * @param daemon The daemon to set.
	 */
	public void setDaemon(java.lang.String daemon) {
		this.daemon = daemon;
	}
	/**
	 * @return Returns the mode.
	 */
	public java.lang.String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(java.lang.String mode) {
		this.mode = mode;
	}
	/**
	 * @return Returns the network.
	 */
	public java.lang.String getNetwork() {
		return network;
	}
	/**
	 * @param network The network to set.
	 */
	public void setNetwork(java.lang.String network) {
		this.network = network;
	}
	/**
	 * @return Returns the service.
	 */
	public java.lang.String getService() {
		return service;
	}
	/**
	 * @param service The service to set.
	 */
	public void setService(java.lang.String service) {
		this.service = service;
	}
	/**
	 * @return Returns the subject.
	 */
	public java.lang.String getSubject() {
		return subject;
	}
	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(java.lang.String subject) {
		this.subject = subject;
	}
	/**
	 * @return Returns the dqSchedulerActivation.
	 */
	public String getDqSchedulerActivation() {
		return dqSchedulerActivation;
	}
	/**
	 * @param dqSchedulerActivation The dqSchedulerActivation to set.
	 */
	public void setDqSchedulerActivation(String dqSchedulerActivation) {
		this.dqSchedulerActivation = dqSchedulerActivation;
	}
	/**
	 * @return Returns the dqSchedulerHeartBeat.
	 */
	public String getDqSchedulerHeartBeat() {
		return dqSchedulerHeartBeat;
	}
	/**
	 * @param dqSchedulerHeartBeat The dqSchedulerHeartBeat to set.
	 */
	public void setDqSchedulerHeartBeat(String dqSchedulerHeartBeat) {
		this.dqSchedulerHeartBeat = dqSchedulerHeartBeat;
	}
	/**
	 * @return Returns the dqSchedulerWeight.
	 */
	public String getDqSchedulerWeight() {
		return dqSchedulerWeight;
	}
	/**
	 * @param dqSchedulerWeight The dqSchedulerWeight to set.
	 */
	public void setDqSchedulerWeight(String dqSchedulerWeight) {
		this.dqSchedulerWeight = dqSchedulerWeight;
	}
	/**
	 * @return Returns the dqTasks.
	 */
	public String getDqTasks() {
		return dqTasks;
	}
	/**
	 * @param dqTasks The dqTasks to set.
	 */
	public void setDqTasks(String dqTasks) {
		this.dqTasks = dqTasks;
	}
	/**
	 * @return Returns the dqWeight.
	 */
	public String getDqWeight() {
		return dqWeight;
	}
	/**
	 * @param dqWeight The dqWeight to set.
	 */
	public void setDqWeight(String dqWeight) {
		this.dqWeight = dqWeight;
	}
	/**
	 * @return Returns the ftActivationInterval.
	 */
	public String getFtActivationInterval() {
		return ftActivationInterval;
	}
	/**
	 * @param ftActivationInterval The ftActivationInterval to set.
	 */
	public void setFtActivationInterval(String ftActivationInterval) {
		this.ftActivationInterval = ftActivationInterval;
	}
	/**
	 * @return Returns the ftActive.
	 */
	public String getFtActive() {
		return ftActive;
	}
	/**
	 * @param ftActive The ftActive to set.
	 */
	public void setFtActive(String ftActive) {
		this.ftActive = ftActive;
	}
	/**
	 * @return Returns the ftActiveGoal.
	 */
	public String getFtActiveGoal() {
		return ftActiveGoal;
	}
	/**
	 * @param ftActiveGoal The ftActiveGoal to set.
	 */
	public void setFtActiveGoal(String ftActiveGoal) {
		this.ftActiveGoal = ftActiveGoal;
	}
	/**
	 * @return Returns the ftGroup.
	 */
	public String getFtGroup() {
		return ftGroup;
	}
	/**
	 * @param ftGroup The ftGroup to set.
	 */
	public void setFtGroup(String ftGroup) {
		this.ftGroup = ftGroup;
	}
	/**
	 * @return Returns the ftHeartBeat.
	 */
	public String getFtHeartBeat() {
		return ftHeartBeat;
	}
	/**
	 * @param ftHeartBeat The ftHeartBeat to set.
	 */
	public void setFtHeartBeat(String ftHeartBeat) {
		this.ftHeartBeat = ftHeartBeat;
	}
	/**
	 * @return Returns the ftPrepInterval.
	 */
	public String getFtPrepInterval() {
		return ftPrepInterval;
	}
	/**
	 * @param ftPrepInterval The ftPrepInterval to set.
	 */
	public void setFtPrepInterval(String ftPrepInterval) {
		this.ftPrepInterval = ftPrepInterval;
	}
	/**
	 * @return Returns the ftWeight.
	 */
	public String getFtWeight() {
		return ftWeight;
	}
	/**
	 * @param ftWeight The ftWeight to set.
	 */
	public void setFtWeight(String ftWeight) {
		this.ftWeight = ftWeight;
	}
}
