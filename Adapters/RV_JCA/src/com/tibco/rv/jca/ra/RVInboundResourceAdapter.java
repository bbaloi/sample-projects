//
//"This sample program is provided AS IS and may be used, executed, copied and modified without royalty payment by customer (a) for its own 
//instruction and study, (b) in order to develop applications designed to run with an IBM WebSphere product, either for customer's own internal use 
//or for redistribution by customer, as part of such an application, in customer's own products. " 
//
//Product 5724-I63,  (C) COPYRIGHT International Business Machines Corp., 2004
//All Rights Reserved * Licensed Materials - Property of IBM
//
package com.tibco.rv.jca.ra;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkManager;
import javax.transaction.xa.XAResource;
import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.rv.jca.util.Constants;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Iterator;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.tibco.rv.jca.util.RVUtilities;

/**
 * This simple Inbound Resource Adapter receives textual Messages from a Message Provider
 * and sends it to the message listener (End Point).   No transactions are supported.
 */
public class RVInboundResourceAdapter implements javax.resource.spi.ResourceAdapter, java.io.Serializable 
{
    static final String className = RVInboundResourceAdapter.class.getName();

     private static Logger sLogger;
    private static final String rbName = null; // No localization is used for logging.
    static
	{
    	try 
    	{
            sLogger = Logger.getLogger(className, rbName);
        } catch (Exception e)
        {
            // Problem initializing logging.  Continue anyway.
        }    	
	}
    
    private static BootstrapContext bootstrapCtx = null;
    private RVListenerManagerFactory rvListenerMgrFactory=null;
    private InitialContext initialContext=null;
    private WorkManager workManager=null;
    private HashMap jndiPropertyMap=null;
    private HashMap  jndiPropertyListMap=null;
    private MessageEndpointFactory lEndpointActivationFactory=null;
   // private TibrvFtMember ftMember=null;
    private ArrayList listenerMgrList=null;
   

    // Hash table containing all active message endpoints
    private HashMap msgEndpointFactoryList = null;

    // Resource Adapter Configuration Properties
    private String serverName = null;

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    /**
     * The start method of the ResourceAdapter JavaBean is called each time
     * a resource adapter instance is created. This may be during resource
     * adapter deployment, application server startup, or other situations.
     * <P>
     * The application server must use a new ResourceAdapter JavaBean for managing the
     * lifecycle of each resource adapter instance and must discard the ResourceAdapter
     * JavaBean after its stop method has been called. That is, the application server must not
     * reuse the same ResourceAdapter JavaBean object to manage multiple instances of a
     * resource adapter, since the ResourceAdapter JavaBean object may contain resource
     * adapter instance specific state information.
     *
     *  The resource adapter instance is set in the BankMessageProviderImpl class which
     *  uses this instance to send messages to the end point.
     * 
     */
    public InitialContext getInitialContext()
    {
    	return initialContext;
    }
    public WorkManager getWorkManager()
    {
    	return workManager;
    }
    public HashMap getJNDIPropertiesCache()
    {
    	return jndiPropertyMap;
    }
    public HashMap getJNDIPropertiesListCache()
    {
    	return jndiPropertyListMap;
    }
    
    public void start(BootstrapContext serverCtx) throws ResourceAdapterInternalException 
	{
    	sLogger.log(Level.INFO,"+++Starting TIBCO Rendezvous Resource Adapter !+++");
      
        final String methodName = "start";
        sLogger.entering(className, methodName, serverCtx);
        bootstrapCtx = serverCtx;    
        workManager = bootstrapCtx.getWorkManager();
        
        try
        {
        initialContext = new InitialContext();
        buildJNDIPropertyList();
        }
        catch(NamingException excp)
        {
        	excp.printStackTrace();
        	throw new ResourceAdapterInternalException("Invalid InitialContext Lookup");
        	
        }
                         
        msgEndpointFactoryList = new HashMap();  
        listenerMgrList=new ArrayList();
        rvListenerMgrFactory = RVListenerManagerFactory.getInstance();
        sLogger.logp(Level.INFO, className, methodName, "TIBCO Rendezvous Resource Adapter started successfully....");
        sLogger.exiting(className, methodName);
    }

    /**
     * The application server calls the stop method on the ResourceAdapter JavaBean to notify
     * the resource adapter instance to stop functioning so that it can be safely unloaded. This is
     * a shutdown notification from the application server, and this method is called by an
     * application server thread.
     * <P>
     * The ResourceAdapter JavaBean is responsible for performing an orderly shutdown of the
     * resource adapter instance during the stop method call. This may involve closing network
     * endpoints, relinquishing threads, releasing all active Work instances, and flushing any
     * cached data to the EIS.
     * 
     */
    public void stop() {

        final String methodName = "stop";
        sLogger.entering(className, methodName);

        bootstrapCtx = null;
        msgEndpointFactoryList = null;
        rvListenerMgrFactory.clean();
        rvListenerMgrFactory=null;        
        //eliminate all RV components
        
	/*
	 * Iterator it = listenerMgrList.iterator();
        while(it.hasNext())
        {
            RVListenerManager listenerMgr= (RVListenerManager)it.next();
            listenerMgr.stop();
        }
        */
        sLogger.logp(Level.INFO, className, methodName, "TIBCO Rendezvous Resource Adapter stopped.");
        sLogger.exiting(className, methodName);
    }

    /**
     * @see javax.resource.spi.ResourceAdapter#endpointActivation(MessageEndpointFactory, ActivationSpec)
     */
    public void endpointActivation(MessageEndpointFactory pMsgEndpointFactory, ActivationSpec pActivationSpec) throws ResourceException 
	{
    	RVListenerManager rvListenerMgr=null;
       	RVInboundActivationSpec rvActSpec=null;
      	lEndpointActivationFactory=pMsgEndpointFactory;

        final String methodName = "endpointActivation";
        sLogger.entering(className, methodName);
        
        if(!(pActivationSpec instanceof RVInboundActivationSpec))
        {
        	throw new ResourceException("Invalid ActivationSpec !");
        }
        else
        {
           rvActSpec = (RVInboundActivationSpec) pActivationSpec;
        }
        if(	lEndpointActivationFactory!=null)
        {        	
        	sLogger.log(Level.INFO,"endpoint factory is not NULL");
	        msgEndpointFactoryList.put(pActivationSpec,	lEndpointActivationFactory);       
	        
	        //instantiate the corresponding RV listeners 
	        //+++++based on the specification Class invoke the appropriate method++++
	       
	     	      sLogger.log(Level.INFO,"Creating Listener Manager !");	      
			       rvListenerMgr = rvListenerMgrFactory.createBaseManager(this);	      
			       try
			       {
			        sLogger.log(Level.INFO,"Ading RV Listeners !");
			        rvListenerMgr.addRvListeners(rvActSpec,	lEndpointActivationFactory);
			       }
			       catch(RV_JCAException excp)
			       {
			       	String msg="Could not create RV Listeners !";
			       	sLogger.log(Level.SEVERE,msg);
			       	throw new ResourceException(excp);
			       	
			       }
			       catch(Exception excp)
			       {
			       	String msg="Nasty error occured while trying to create RV Lsteners !";
			       	sLogger.log(Level.SEVERE,msg);
			       	throw new ResourceException(excp);
			       }
			       sLogger.logp(Level.INFO, className, methodName, "TIBCO Rendezvous Resource Adapter Endpoint Activated successfully.");
			       sLogger.exiting(className, methodName);	
                               listenerMgrList.add(rvListenerMgr);
                               
                               
        }
        else
        {
        	sLogger.log(Level.SEVERE,"EndpointFactory was NULL; cannot activate endpoint, or start the RV listeners !");
        	sLogger.exiting(className, methodName);
        }
      }

    /**
     * @see javax.resource.spi.ResourceAdapter#endpointDeactivation(MessageEndpointFactory, ActivationSpec)
     */
    public void endpointDeactivation(MessageEndpointFactory pMmsgEndpointFac, ActivationSpec pActivationSpec) {

        final String methodName = "endpointDeactivation";
        if (sLogger.isLoggable(Level.FINER)) {
            Object[] parms = { pMmsgEndpointFac, pActivationSpec };
            sLogger.entering(className, methodName, parms);
        }
        
        msgEndpointFactoryList.remove(pActivationSpec);

        sLogger.logp(Level.INFO, className, methodName, "TIBCO Rendezvous Resource Adapter Endpoint deactivated.");
        if (sLogger.isLoggable(Level.FINER)) {
            sLogger.exiting(className, methodName);
        }
    }

    /**
     * @see javax.resource.spi.ResourceAdapter#getXAResources(ActivationSpec[])
     */
    public XAResource[] getXAResources(ActivationSpec[] arg0) throws ResourceException 
	{

        final String methodName = "getXAResources";
        sLogger.entering(className, methodName);

        sLogger.exiting(className, methodName);
        return null;
    }
    private void buildJNDIPropertyList()
    {
    	String subject=null,service=null,network=null,daemon=null,mode=null;
    	String dqWeight=null,dqTasks=null,dqSchedulerWeight=null,dqSchedulerHeartBeat=null,dqSchedulerActivation=null;
    	String ftGroup=null,ftWeight=null,ftActive=null,ftActiveGoal=null,ftHeartBeat=null,ftPrepInterval=null,ftActivationInterval=null;
    	
    	sLogger.log(Level.INFO,"Searching for JNDI properties !!!");
    	jndiPropertyMap = new HashMap();
		jndiPropertyListMap = new HashMap();  
    	    	
		try{
			subject= (String)initialContext.lookup(Constants.RV_SUBJECT);
			}
		catch(javax.naming.NamingException excp)
		{			
			//do nothing
		}		
		try{		
			service= (String)initialContext.lookup(Constants.RV_SERVICE);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{	
		    network= (String)initialContext.lookup(Constants.RV_NETWORK);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{		
			daemon= 	(String)initialContext.lookup(Constants.RV_DAEMON);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			mode= 	(String)initialContext.lookup(Constants.RV_MODE);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			dqWeight= (String)initialContext.lookup(Constants.RV_DQ_WEIGHT);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{		
			dqTasks= (String)initialContext.lookup(Constants.RV_DQ_TASKS);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{		
			dqSchedulerWeight= (String)initialContext.lookup(Constants.RV_DQ_SCHEDULER_WEIGHT);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			dqSchedulerHeartBeat= (String)initialContext.lookup(Constants.RV_DQ_SCHEDULER_HEARTBEAT);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			dqSchedulerActivation = (String)initialContext.lookup(Constants.RV_DQ_SCHEDULER_ACTIVATION);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftGroup= (String)initialContext.lookup(Constants.RV_FT_GROUP);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftWeight= (String)initialContext.lookup(Constants.RV_FT_WEIGHT);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftActive= (String)initialContext.lookup(Constants.RV_FT_ACTIVE);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
	    	ftActiveGoal= (String)initialContext.lookup(Constants.RV_FT_ACTIVE_GOAL);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftHeartBeat= (String)initialContext.lookup(Constants.RV_FT_HEARTBEAT);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftPrepInterval= (String)initialContext.lookup(Constants.RV_FT_PREP_INTERVAL);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
		try{
			ftActivationInterval= (String)initialContext.lookup(Constants.RV_FT_ACTIVATION_INTERVAL);
		}catch(javax.naming.NamingException excp)
		{
			//do nothing
		}
    	//build string map
    	
    	jndiPropertyMap.put(Constants.RV_SUBJECT,subject);
    	jndiPropertyMap.put(Constants.RV_SERVICE,service);
    	jndiPropertyMap.put(Constants.RV_NETWORK,network);
    	jndiPropertyMap.put(Constants.RV_DAEMON,daemon);
    	jndiPropertyMap.put(Constants.RV_MODE,mode);
    	jndiPropertyMap.put(Constants.RV_DQ_WEIGHT,dqWeight);
    	jndiPropertyMap.put(Constants.RV_DQ_TASKS,dqTasks);
    	jndiPropertyMap.put(Constants.RV_DQ_SCHEDULER_WEIGHT,dqSchedulerWeight);
    	jndiPropertyMap.put(Constants.RV_DQ_SCHEDULER_HEARTBEAT,dqSchedulerHeartBeat);
    	jndiPropertyMap.put(Constants.RV_DQ_SCHEDULER_ACTIVATION,dqSchedulerActivation);
    	jndiPropertyMap.put(Constants.RV_FT_GROUP,ftGroup);
    	jndiPropertyMap.put(Constants.RV_FT_WEIGHT,ftWeight);
        jndiPropertyMap.put(Constants.RV_FT_ACTIVE,ftActive);
    	jndiPropertyMap.put(Constants.RV_FT_ACTIVE_GOAL,ftActiveGoal);
    	jndiPropertyMap.put(Constants.RV_FT_HEARTBEAT,ftHeartBeat);
    	jndiPropertyMap.put(Constants.RV_FT_PREP_INTERVAL,ftPrepInterval);
    	jndiPropertyMap.put(Constants.RV_FT_ACTIVATION_INTERVAL,ftActivationInterval);
    	
    	//build list map
    	if(subject!=null)
    		jndiPropertyListMap.put(Constants.RV_SUBJECT,RVUtilities.getPropertyList(subject,sLogger));
    	if(service!=null)
    		jndiPropertyListMap.put(Constants.RV_SERVICE,RVUtilities.getPropertyList(service,sLogger));
       if(network!=null)
       		jndiPropertyListMap.put(Constants.RV_NETWORK,RVUtilities.getPropertyList(network,sLogger));
    	if(daemon!=null)
    		jndiPropertyListMap.put(Constants.RV_DAEMON,RVUtilities.getPropertyList(daemon,sLogger));
    	if(mode!=null)
    		jndiPropertyListMap.put(Constants.RV_MODE,RVUtilities.getPropertyList(mode,sLogger));
    	if(dqWeight!=null)
    		jndiPropertyListMap.put(Constants.RV_DQ_WEIGHT,RVUtilities.getPropertyList(dqWeight,sLogger));
    	if(dqTasks!=null)
    		jndiPropertyListMap.put(Constants.RV_DQ_TASKS,RVUtilities.getPropertyList(dqTasks,sLogger));
    	if(dqSchedulerWeight!=null)
    		jndiPropertyListMap.put(Constants.RV_DQ_SCHEDULER_WEIGHT,RVUtilities.getPropertyList(dqSchedulerWeight,sLogger));
    	if(dqSchedulerHeartBeat!=null)
    		jndiPropertyListMap.put(Constants.RV_DQ_SCHEDULER_HEARTBEAT,RVUtilities.getPropertyList(dqSchedulerHeartBeat,sLogger));
    	if(dqSchedulerActivation!=null)
    		jndiPropertyListMap.put(Constants.RV_DQ_SCHEDULER_ACTIVATION,RVUtilities.getPropertyList(dqSchedulerActivation,sLogger));
    	if(ftGroup!=null)    	
    		jndiPropertyListMap.put(Constants.RV_FT_GROUP,RVUtilities.getPropertyList(ftGroup,sLogger));
    	if(ftWeight!=null)
    		jndiPropertyListMap.put(Constants.RV_FT_WEIGHT,RVUtilities.getPropertyList(ftWeight,sLogger));
        if(ftActive!=null)
        	jndiPropertyListMap.put(Constants.RV_FT_ACTIVE,RVUtilities.getPropertyList(ftActive,sLogger));
    	if(ftActiveGoal!=null)
    		jndiPropertyListMap.put(Constants.RV_FT_ACTIVE_GOAL,RVUtilities.getPropertyList(ftActiveGoal,sLogger));
    	if(ftHeartBeat!=null)
    		jndiPropertyListMap.put(Constants.RV_FT_HEARTBEAT,RVUtilities.getPropertyList(ftHeartBeat,sLogger));
    	if(ftPrepInterval!=null)
    		jndiPropertyListMap.put(Constants.RV_FT_PREP_INTERVAL,RVUtilities.getPropertyList(ftPrepInterval,sLogger));
    	if(ftActivationInterval!=null)
    		jndiPropertyListMap.put(Constants.RV_FT_ACTIVATION_INTERVAL,RVUtilities.getPropertyList(ftActivationInterval,sLogger));
        
        
    }
     
}
