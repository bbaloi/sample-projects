package com.tibco.syslog.adapter;

import java.util.*;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.lang.reflect.Constructor;

import com.tibco.sdk.*;
import com.tibco.sdk.hawk.*;
import com.tibco.sdk.events.*;
import com.tibco.sdk.metadata.MInstance;
import com.tibco.sdk.tools.*;
import com.tibco.sdk.rpc.*;
import com.tibco.syslog.adapter.monitor.SyslogMicroAgentMethods;
import com.tibco.syslog.adapter.operations.SyslogEventSource;
import com.tibco.syslog.adapter.operations.SyslogPublisher;
import com.tibco.syslog.adapter.util.Constants;
import com.tibco.syslog.adapter.util.GlobalProperties;
import com.tibco.syslog.synch.TestReqResp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class TibcoSyslogAdapter extends MApp 
//implements IDiameterAdapter
{	
	private static String lPropertiesFileName=null;
	private static Properties lAdapterProps=null;
	private static MAppProperties lAppProps=null;
	private static HashMap appMap=new HashMap();
	private MHostInfo hostInfo=null;
	private MAdapterServiceInfo serviceInfo=null;
	private static FileHandler fh;
	private MComponentRegistry componentRegistry=null;
	private MHawkRegistry hawkRegistry=null;
	//public static Object diameterClientImpl;	
	private static SyslogEventSource syslogSource=null;
	private static TibcoSyslogAdapter app;
	private SyslogPublisher  pub;

	private static final Logger logger = Logger.getLogger( "com.tibco.syslog.adapter.TibcoSyslogAdapter");
		
	
	public TibcoSyslogAdapter(MAppProperties appProperties)
	{
		super(appProperties);
		init();
		
	}
	/** Hook to perform application-specific behavior during initialization
	 *  onInitialization is called by SDK during start()
	 * */
	protected void onInitialization() throws MException 
	{
		MHostInfo hostInfo = this.getHostInfo();  
		hostInfo.setAppState(MUserApplicationState.RUNNING);  
		setHostInfo(hostInfo);  
		serviceInfo = new MAdapterServiceInfo();
		if(logger.isDebugEnabled())
			logger.debug("#Tibco Syslog Adapter Initializing !");	
		getTrace().trace("Syslog-20000", null, "Starting Syslog Adapter",null);
		componentRegistry = getComponentRegistry();
		hawkRegistry = getHawkRegistry();
		loadSyslogStack();
		loadApps();		
		if(logger.isDebugEnabled())
			logger.debug("#Syslog Listener Loaded!");
		getTrace().trace("Syslog-20000", null, "Apps loaded !",null);
				
		//---------registering Adapter Service endpoints	
		
		
	}
	public MAdapterServiceInfo getServiceInfo()
	{
		return serviceInfo;
	}

	/** Hook to perform application-specific behavior during shut-down
	 *  onTermination is called by SDK when the used calls stop()
	 * */

	protected void onTermination() throws MException 
	{
//		** Adapter is shutting down
		MHostInfo hostInfo = getHostInfo();	
		hostInfo.setAppState(MUserApplicationState.STOPPING);
		this.setHostInfo(hostInfo);
		
		getTrace().trace("Syslog-20000", null, "onTermination() - stopping syslog adapter",null);
			  
	     try
	        {
		    	 syslogSource.removeListener(pub);
		    	 syslogSource.stop();	    	 
	        	 app.stop();
	        	  
	        } 
	        catch(Exception ex)
	        {
	        	 getTrace().trace("Syslog-0999", null, "shutdown: failed to stop SyslogAdapter",ex.toString());

	        }      
		hostInfo = getHostInfo();	
		 hostInfo.setAppState(MUserApplicationState.STOPPED);
	     this.setHostInfo(hostInfo);  
	  
	     getTrace().trace("Syslog-20000", null, "shutdown: stopped SyslogAdapter",null);
	
	
	}
	public void shutdown() throws MException
    {           // set service state to stopped
		  
		/*
		MHostInfo hostInfo = getHostInfo();	
	     hostInfo.setAppState(MUserApplicationState.STOPPED);
	     this.setHostInfo(hostInfo);  
	    
	     getTrace().trace("Syslog-20000", null, "shutdown: stopped SyslogAdapter",null);

		
		getTrace().trace("Syslog-20000", null, "in shutdown-stopping SyslogAdpater !",null);
	    
	     try
        {
	    	 syslogSource.removeListener(pub);
	    	 syslogSource.stop();	    	 
        	 app.stop();
        	  
        } 
        catch(Exception ex)
        {
        	 getTrace().trace("Syslog-0999", null, "shutdown: failed to stop SyslogAdapter",ex.toString());

        }      
         
        */
    }

	public static void main(String[] args) 
	{
		try
		{
			validateInput(args);
			initProps(args);			
			// Create the adpater and pass the MAppProperties
			app = new TibcoSyslogAdapter(lAppProps);
			MHostInfo hostInfo = new MHostInfo(app);
	        hostInfo.setAppState(MUserApplicationState.INITIALIZING);
	        app.setHostInfo(hostInfo);	
	        // load application resource file	
	        MMessageBundle.addResourceBundle("Syslog", "SyslogAdapterResBundle");
	
	          // create user defined advisory listener	
	        app.setAdvisoryListener(new SyslogAdvisoryListener(app));
	        //app.loadApps();
	        app.start();	       
	        
		} 
		catch(MException pExcp)
		{
			pExcp.printStackTrace();
		}
		catch(Exception fatal) 
		{
			fatal.printStackTrace();
		}
	}
		
	public static void validateInput(String args[])
	 {
		 //System.out.println("Num parms:"+args.length);
		if(args.length<2 )
		{
			System.out.println("Invalid number of arguments !");
			System.out.println("Proper arguments are: -properties_file <PropertiesFileName>");
			System.exit(1);
		}
		else
		{
			lPropertiesFileName= (String) args[1];
			
		}			
	 }
	private static void initProps(String [] args)
	{
		lAdapterProps = new Properties();
		try
		{
			lAdapterProps.load(new FileInputStream(lPropertiesFileName));
			GlobalProperties gp = GlobalProperties.getInstance();
			gp.setProperties(lAdapterProps);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
			
		try
		{
			String repoUrl = lAdapterProps.getProperty(Constants.AdapterRepository);
			//logger.debug("repoURL:"+repoUrl+",AppName:"+Constants.AppName+",AppVersion:"+Constants.AppVersion+",AppInfo:"+Constants.AppInfo+",configUrl:"+Constants.ConfigURL);
			
			lAppProps = new MAppProperties(Constants.AppName,
                    Constants.AppVersion,Constants.AppInfo,Constants.ConfigURL, 
                    repoUrl,args);	
			//logger.debug("---ConfigURL:"+lAppProps.getConfigURL()+",repoURL:"+lAppProps.getRepoURL());
			
		}
		catch(MException pExcp)
		{
			pExcp.printStackTrace();
		}		
		
	}
	public Properties getProperties()
	{
		return lAdapterProps;
	}
	private void loadApps() throws MException
	{
		if(logger.isDebugEnabled())
			logger.debug("Loading Services !");	

	      MPublisher syslogPub = componentRegistry.getPublisher("SyslogPublicationServiceEndPoint");

	        if(syslogPub==null)
	            throw new MException("Can't find publisher by name \"pub\", " +
	                                 "check config info in the repository");

	        pub = new SyslogPublisher(this, syslogPub);
	        	        
	        //Create theSyslogEvent Source;
	        syslogSource = new SyslogEventSource(componentRegistry,Constants.SyslogEventSource,this);
	    	syslogSource.addListener(pub);    	    	
	    	syslogSource.init();		    		
	    	initMonitor(pub);       
	    	
	    	Enumeration classEnum = syslogPub.getClassNames();
	    	while(classEnum.hasMoreElements())
	    	{
	    		String schemaName = (String) classEnum.nextElement();
	    		serviceInfo.set("SyslogPublicationServiceEndPoint",syslogPub.getName(), schemaName);
		    	
	    		
	    	}	    	
	    	setAdapterServiceInfo(serviceInfo);
	    	
	    	loadReqResp();
	}
	
	private void loadReqResp()
	{
		try
		{
			new TestReqResp(this);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
	}
	private void loadSyslogStack()
	{
		if(logger.isDebugEnabled())
				logger.debug("Loading Syslog Stack");	
		
			
	
	}
	private void init()
	{

		 //componentRegistry = getComponentRegistry();
	}
	private void initMonitor(SyslogPublisher pub)
	{
		try
		{
			SyslogMicroAgentMethods microAgent = new SyslogMicroAgentMethods();
			microAgent.registerSyslogPublisher(pub);
			MHawkMicroAgent hawkAgent = hawkRegistry.getHawkMicroAgent(Constants.MicroagentName);
			if(hawkAgent==null)			
			{
				if(logger.isDebugEnabled())
					logger.error("No Micro agent '!"+Constants.MicroagentName+"' exists !");	
	
					 getTrace().trace("Syslog-0999", null, "Microagent '"+Constants.MicroagentName+"' does not exist !",null);
					
			}
			else 
			{
				hawkAgent.setMonitoredObject(microAgent);
				hawkAgent.activate();
				getTrace().trace("Syslog-20000", null, "Hawk Microagent '"+Constants.MicroagentName+"' started !",null);
				
			}
		}
		catch(MException pExcp)
		{
			 getTrace().trace("Syslog-0999", null, "Microagent '"+Constants.MicroagentName+"' could not be started !",null);
				
		}
		
		
	}
	 
}
