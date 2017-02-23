/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.ra;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.resource.spi.ActivationSpec;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.tibco.rv.jca.RVListener;
import com.tibco.rv.jca.util.Constants;
import com.tibco.rv.jca.util.RVUtilities;
import java.util.List;
import com.tibco.rv.jca.util.Constants;

import com.tibco.rv.jca.exceptions.RV_JCAException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.tibco.rv.jca.AbstractListener;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class RVListenerManager 
{	
	protected String className=null;
	protected  Logger sLogger;
	protected static String rbName = null; // No localization is used for logging.
	protected ArrayList rvListenersList;
	protected HashMap jndiProperties;
	protected HashMap jndiPropertiesList;
	
	protected RVInboundResourceAdapter resourceAdapter=null;
	protected InitialContext initialContext=null;
	protected WorkManager    workManager=null;
	protected MessageEndpointFactory endpointFactory=null;
	protected RVListener endpoint=null;
	protected RVInboundActivationSpec activationSpec=null;
			
	protected abstract void  init();
	protected abstract void startupRvListeners(List rvListenerslist) throws RV_JCAException;
	
	//protected String daemon,service,network,subject,mode, ftGroup;
	//protected int dqTasks, dqSchedulerWeight,dqWeight,ftWeigt,ftActiveGoal;
	//protected double dqScheudlerHeartBeat,dqScheudlerActivation,ftHeartBeatInterval,ftPrepInterval,ftActivationInterval;
	protected List daemonList=null,serviceList=null,networkList=null,subjectList=null,modeList=null;
	protected List dqWeightList=null,dqTasksList=null,dqSchedulerWeightList=null, dqSchedulerHeartBeatList=null,dqSchedulerActivationList=null;
	protected List ftGroupList=null,ftWeightList=null,ftActiveList=null,ftActiveGoalList=null,ftHeartBeatList=null,ftPrepIntervalList=null,ftActivationIntervalList=null;
	
	
	public RVListenerManager(RVInboundResourceAdapter pAdapter)
	{
		rvListenersList=new ArrayList();
		resourceAdapter = pAdapter;
		setInitialContext(pAdapter.getInitialContext());
		setWorkManager(pAdapter.getWorkManager());
		setJNDIProperties(pAdapter.getJNDIPropertiesCache());
		setJNDIPropertiesList(pAdapter.getJNDIPropertiesListCache());
		
	}
	
	public void setInitialContext(InitialContext pContext)
	{
		initialContext=pContext;
	}
	public void setWorkManager(WorkManager pManager)
	{
		workManager=pManager;
	}
	public void addRvListeners(RVInboundActivationSpec pSpec, MessageEndpointFactory pFactory) throws RV_JCAException 
	{
		activationSpec=pSpec;
		endpointFactory=pFactory;			
		
	    setListenerParams();
		startupRvListeners(rvListenersList);		
	      
	}
	protected void setListenerParams() throws RV_JCAException
	{
		sLogger.log(Level.INFO,"Setting the operation RV Listeners Params !");
		
		subjectList=(List)jndiPropertiesList.get(Constants.RV_SUBJECT);
		serviceList=(List)jndiPropertiesList.get(Constants.RV_SERVICE);
		networkList=(List)jndiPropertiesList.get(Constants.RV_NETWORK);
		daemonList=(List)jndiPropertiesList.get(Constants.RV_DAEMON);
		modeList=(List)jndiPropertiesList.get(Constants.RV_MODE);
		dqWeightList=(List)jndiPropertiesList.get(Constants.RV_DQ_WEIGHT);
		dqTasksList=(List)jndiPropertiesList.get(Constants.RV_DQ_TASKS);
		dqSchedulerWeightList=(List)jndiPropertiesList.get(Constants.RV_DQ_SCHEDULER_WEIGHT);
		dqSchedulerHeartBeatList=(List)jndiPropertiesList.get(Constants.RV_DQ_SCHEDULER_HEARTBEAT);
		dqSchedulerActivationList=(List)jndiPropertiesList.get(Constants.RV_DQ_SCHEDULER_ACTIVATION);
		ftGroupList=(List)jndiPropertiesList.get(Constants.RV_FT_GROUP);
		ftWeightList=(List)jndiPropertiesList.get(Constants.RV_FT_WEIGHT);
		ftActiveList=(List)jndiPropertiesList.get(Constants.RV_FT_ACTIVE);
		ftActiveGoalList=(List)jndiPropertiesList.get(Constants.RV_FT_ACTIVE_GOAL);
		ftHeartBeatList=(List)jndiPropertiesList.get(Constants.RV_FT_HEARTBEAT);
		ftPrepIntervalList=(List)jndiPropertiesList.get(Constants.RV_FT_PREP_INTERVAL);
		ftActivationIntervalList=(List)jndiPropertiesList.get(Constants.RV_FT_ACTIVATION_INTERVAL);
     
		try{
			
			if(subjectList==null)
			{
				String lSubject=activationSpec.getSubject();
				sLogger.log(Level.INFO,"Subject value:"+lSubject);
				if(lSubject!=null)
					subjectList = RVUtilities.getPropertyList(lSubject,sLogger);
			}
			if(serviceList==null)
			{
				String lService=activationSpec.getService();
				sLogger.log(Level.INFO,"Service value:"+lService);
				if(lService!=null)
					serviceList = RVUtilities.getPropertyList(lService,sLogger);
			}
			if(networkList==null)
			{
				String lNetwork=activationSpec.getNetwork();
				sLogger.log(Level.INFO,"Network value:"+lNetwork);
				if(lNetwork!=null)
					networkList = RVUtilities.getPropertyList(lNetwork,sLogger);
			}
			if(daemonList==null)
			{
				String lDaemon=activationSpec.getDaemon();
				sLogger.log(Level.INFO,"Daemonk value:"+lDaemon);
				if(lDaemon!=null)
					daemonList = RVUtilities.getPropertyList(lDaemon,sLogger);
			}
			if(modeList==null)
			{
				String lMode=activationSpec.getMode();
				sLogger.log(Level.INFO,"Mode value:"+lMode);
				if(lMode!=null)
					modeList = RVUtilities.getPropertyList(lMode,sLogger);
			}
			if(dqWeightList==null)
			{
				String lDqWeight=activationSpec.getDqWeight();
				sLogger.log(Level.INFO,"DqWeight value:"+lDqWeight);
				if(lDqWeight!=null)
					dqWeightList = RVUtilities.getPropertyList(lDqWeight,sLogger);
			}
			if(dqTasksList==null)
			{
				String lDqTasks=activationSpec.getDqTasks();
				sLogger.log(Level.INFO,"DqTasks value:"+lDqTasks);
				if(lDqTasks!=null)
					dqTasksList = RVUtilities.getPropertyList(lDqTasks,sLogger);
			}
			if(dqSchedulerWeightList==null)
			{
				String lDqSchedulerWeight=activationSpec.getDqSchedulerWeight();
				sLogger.log(Level.INFO,"DqSchedulerWeight value:"+lDqSchedulerWeight);
				if(lDqSchedulerWeight!=null)
					dqSchedulerWeightList = RVUtilities.getPropertyList(lDqSchedulerWeight,sLogger);
			}
			if(dqSchedulerHeartBeatList==null)
			{
				String lDqSchedulerHeartBeat=activationSpec.getDqSchedulerHeartBeat();
				sLogger.log(Level.INFO,"DqSchedulerHeartBeat value:"+lDqSchedulerHeartBeat);
				if(lDqSchedulerHeartBeat!=null)
					dqSchedulerHeartBeatList = RVUtilities.getPropertyList(lDqSchedulerHeartBeat,sLogger);
			}
			if(dqSchedulerActivationList==null)
			{
				String lDqSchedulerActivation=activationSpec.getDqSchedulerActivation();
				sLogger.log(Level.INFO,"DqScheudlerAcitvation value:"+lDqSchedulerActivation);
				if(lDqSchedulerActivation!=null)
				dqSchedulerActivationList = RVUtilities.getPropertyList(lDqSchedulerActivation,sLogger);
			}
			if(ftGroupList==null)
			{
				String lFtGroup=activationSpec.getFtGroup();
				sLogger.log(Level.INFO,"FtGroup value:"+lFtGroup);
				if(lFtGroup!=null)
					ftGroupList = RVUtilities.getPropertyList(lFtGroup,sLogger);
			}
			if(ftWeightList==null)
			{
				String lFtWeight=activationSpec.getFtWeight();
				sLogger.log(Level.INFO,"FtWeight value:"+lFtWeight);
				if(lFtWeight!=null)
					ftWeightList = RVUtilities.getPropertyList(lFtWeight,sLogger);
			}
			if(ftActiveList==null)
			{
				String lFtActive=activationSpec.getFtActive();
				sLogger.log(Level.INFO,"FtActive value:"+lFtActive);
				if(lFtActive!=null)
					ftActiveList = RVUtilities.getPropertyList(lFtActive,sLogger);
			}
			if(ftActiveGoalList==null)
			{
				String lFtActiveGoal=activationSpec.getFtActiveGoal();
				sLogger.log(Level.INFO,"FtActiveGoal value:"+lFtActiveGoal);
				if(lFtActiveGoal!=null)
					ftActiveGoalList = RVUtilities.getPropertyList(lFtActiveGoal,sLogger);
			}
			if(ftHeartBeatList==null)
			{
				String lFtHeartBeat=activationSpec.getFtHeartBeat();
				sLogger.log(Level.INFO,"FtHeartBeat value:"+lFtHeartBeat);
				if(lFtHeartBeat!=null)
					ftHeartBeatList = RVUtilities.getPropertyList(lFtHeartBeat,sLogger);
			}
			if(ftPrepIntervalList==null)
			{
				String lFtPrepInterval=activationSpec.getFtPrepInterval();
				sLogger.log(Level.INFO,"FtPrepInterval value:"+lFtPrepInterval);
				if(lFtPrepInterval!=null)
					ftPrepIntervalList = RVUtilities.getPropertyList(lFtPrepInterval,sLogger);
			}
			if(ftActivationIntervalList==null)
			{
				String lFtActivationInterval=activationSpec.getFtActivationInterval();
				sLogger.log(Level.INFO,"FtActivationInterval value:"+lFtActivationInterval);
				if(lFtActivationInterval!=null)
					ftActivationIntervalList = RVUtilities.getPropertyList(lFtActivationInterval,sLogger);
			}
		}
		catch(Exception excp)
		{
			String msg="Error parsing Activation Spec variables - from deployment descriptor";
			sLogger.log(Level.SEVERE,msg);
			throw new RV_JCAException(excp,msg);
		}
	
		
	}
	public RVInboundActivationSpec getActivationSpec()
	{
		return activationSpec;
		
	}
	public MessageEndpointFactory getEndpointFactory()
	{
		return endpointFactory;
		
	}
	public RVListener getEndPoint()
	{
		return endpoint;
	}
	protected String getCmName(String pSubjectName)
	{
		//	trim subject name of any wildcards
		int gt,star,last=0;
		String cmName=null;
				
		gt=pSubjectName.indexOf(">");		
		star = pSubjectName.indexOf("*");
		
		if(gt==-1 && star ==-1)
		{
			//there are no wild cards - use subject name 
			cmName=pSubjectName;
		}
		else
		{
			if(gt==-1 && star!=-1) //we have * 
				cmName=pSubjectName.substring(0,star);
			else if(gt!=-1 && star==-1)
				cmName=pSubjectName.substring(0,gt);	
			
		}
		
		return cmName;
		
	}
	public void setJNDIProperties(HashMap pJNDIProps)
	{
		jndiProperties = pJNDIProps;
		
	}
	public void setJNDIPropertiesList(HashMap pJNDIProps)
	{
		jndiPropertiesList = pJNDIProps;
		
	}
	public synchronized RVListener createEndpoint() throws RV_JCAException
	{
		
		try
	 	{
		  sLogger.log(Level.INFO,"Attempting to create endpoint !");
	 	  endpoint = (RVListener) endpointFactory.createEndpoint(null);
	 	}
	 	catch(javax.resource.spi.UnavailableException excp)
	 	{
	 		String msg = "Couldn't create Endpoint !";
	 		sLogger.log(Level.SEVERE,msg);
	 		throw new RV_JCAException(excp,msg);
	 	}
	 	 	
		return endpoint;
	}
	public void stop()
	{
		//kill all the threads & release all RV resources
		Iterator it = rvListenersList.iterator();
		while(it.hasNext())
		{
			AbstractListener listener = (AbstractListener)it.next();
			listener.release();
		}
		
	}
	protected boolean isValidRVSyntax(String pSubjectName)
	{
		boolean valid=true;
		String asteriskStr = "*";
		String dotStr=".";
		String gtStr=">";
		Character asterisk = new Character(asteriskStr.charAt(0));
		Character dot = new Character(dotStr.charAt(0));
		Character gt = new Character(gtStr.charAt(0));		
		
		//do some RV syntax validation i.e. standard format - pttaer - regex....
		int strLen = pSubjectName.length();
		for(int i=0;i<strLen;i++)
		{
			char _ch=pSubjectName.charAt(i);
			Character ch=new Character(_ch);
			
			if(Character.isLetterOrDigit(_ch) || ch.equals(gt) || ch.equals(asterisk) || ch.equals(dot))
			{
				
			}
			else
			{
				valid=false;
				break;
			}
		}
		
		return valid;
		
	}

}
