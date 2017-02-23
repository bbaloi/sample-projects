/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.ra;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.tibco.rv.jca.util.Constants;
import com.tibco.rv.jca.util.RVUtilities;
import java.util.List;
import java.util.Iterator;
import com.tibco.rv.jca.listeners.*;
import com.tibco.rv.jca.vo.*;

import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.Work;
import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.rv.jca.vo.DqVo;
import com.tibco.rv.jca.vo.FtVo;
import com.tibco.rv.jca.listeners.FtCallBack;
import com.tibco.rv.jca.listeners.FtListener;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseRVListenerManager extends RVListenerManager
{
	
	private String lSubject,lDaemon,lNetwork,lMode,lService;
	private String lDqWeight,lDqTasks,lDqSchedulerWeight,lDqSchedulerHeartBeat,lDq,lDqSchedulerActivation;
	private String lFtGroup,lFtWeight,lFtActive,lFtActiveGoal,lFtHeartBeat,lFtPrepInterval,lFtActivationInterval;
	private double dqSchedulerHeartBeat,dqSchedulerActivation,ftHeartBeat,ftPrepInterval,ftActivationInterval;
	private  int dqWeight,dqTasks,dqSchedulerWeight,ftWeight,ftActiveGoal;
	private String service,daemon,mode,network;
	private boolean ftActive;
	
	public BaseRVListenerManager(RVInboundResourceAdapter pAdapter)
	{
		super(pAdapter);
		init();
		
	}
	
	protected void init()
	{
		className = BaseRVListenerManager.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    	
		
	}
	protected void startupRvListeners(List pRvListenerList) throws RV_JCAException
	{
		//this is where we startup the RVListener
		//+++=we chose the number of subjects available as the standard for looping
		
		sLogger.log(Level.INFO,"Attempting to startup RV Listeners !");
		if(subjectList!=null)
		{
			int numSubjects = subjectList.size();
			
				for(int i=0;i<numSubjects;i++)
				{
					lSubject = (String) subjectList.get(i);
					sLogger.log(Level.INFO,"Preparing listener for subject:"+lSubject);
					if(isValidRVSyntax(lSubject))
					{	
						if(modeList!=null && modeList.size()>=i)
						{
								lMode = (String) modeList.get(i);
								if(lMode.equals("_"))
									mode=Constants.NORMAL_MODE;	
								else
									mode=lMode;
									
						}
						else
								mode=Constants.NORMAL_MODE;	
						
					
						setBasicTempVars(i);		
					
						// 1.construct the appropriate RVListener
						//2. create & register callback class
						//3. Send him to the WorkManager for exectution
						
					
						if(mode.equals(Constants.NORMAL_MODE))
						{													
							pRvListenerList.add(createBasicListener());
		
						}
						if(mode.equals(Constants.FT_MODE))
						{
							setFtTempVars(i);
							pRvListenerList.add(createFtListener());				
						}
						if(mode.equals(Constants.DQ_MODE))
						{		
							setDqTempVars(i);
							pRvListenerList.add(createDqListener());
						}
					}
					else
						sLogger.log(Level.WARNING,"Invalid RV syntax for subject; will not create listener !");
					
			}
							
		}
		else
			sLogger.log(Level.WARNING,"No subjects mentioned, therefore no RV listeners will be created !");
		
	}
	protected void setBasicTempVars(int i)
	{		
		sLogger.log(Level.INFO,"Setting basic vars !");
		if(networkList!=null && networkList.size()>=i)
		{
				lNetwork = (String) networkList.get(i);
				if(lNetwork.equals("_"))
					network=Constants.DEFAULT_RV_NETWORK;	
		}
			else
				network=Constants.DEFAULT_RV_NETWORK;				
		
		if(daemonList!=null && daemonList.size()>=i)
		{
					lDaemon = (String) daemonList.get(i);
					if(lDaemon.equals("_"))
						daemon=Constants.DEFAULT_RV_DAEMON;	
		}
			else
				daemon=Constants.DEFAULT_RV_DAEMON;
		
		if(serviceList!=null && serviceList.size()>=i)
		{
				lService = (String) serviceList.get(i);
				if(lService.equals("_"))
					service=Constants.DEFAULT_RV_SERVICE;	
		}
			else
				service=Constants.DEFAULT_RV_SERVICE;	
		
		sLogger.log(Level.INFO,"Network Vars:daemon,service,network:"+lDaemon+","+lService+","+lNetwork);
		
		
	}
	protected void setDqTempVars(int i)
	{
		sLogger.log(Level.INFO,"Setting Dq vars !");
		if(dqWeightList !=null && dqWeightList.size()>=i)
			lDqWeight = (String) dqWeightList.get(i);			
		else
			lDqWeight="";
		if(dqTasksList!=null && dqTasksList.size()>=i)
			lDqTasks = (String) dqTasksList.get(i);
		else
			lDqTasks="";
		if(dqSchedulerWeightList!=null && dqSchedulerWeightList.size()>=i)
			lDqSchedulerWeight = (String) dqSchedulerWeightList.get(i);
		else
			lDqSchedulerWeight="";
		if(dqSchedulerHeartBeatList!=null && dqSchedulerHeartBeatList.size()>=i)
			lDqSchedulerHeartBeat = (String) dqSchedulerHeartBeatList.get(i);
		else
			lDqSchedulerHeartBeat="";
		if(dqSchedulerActivationList !=null && dqSchedulerActivationList.size()>=i)
			lDqSchedulerActivation = (String) dqSchedulerActivationList.get(i);
		else
			lDqSchedulerActivation="";
		//------------------set number values-------------------
		if(!lDqWeight.equals(""))
		{
			if(lDqWeight.equals("_"))
				dqWeight=Constants.DEFAULT_DQ_WEIGHT;
			else
				dqWeight = Integer.parseInt(lDqWeight);
				
		}
		else
			dqWeight=Constants.DEFAULT_DQ_WEIGHT;
		if(!lDqSchedulerWeight.equals(""))
		{
			if(lDqSchedulerWeight.equals("_"))
				dqSchedulerWeight=Constants.DEFAULT_DQ_SCHEDULER_WEIGHT;
			else
				dqSchedulerWeight = Integer.parseInt(lDqSchedulerWeight);
		}
		else
			dqSchedulerWeight=Constants.DEFAULT_DQ_SCHEDULER_WEIGHT;
		if(!lDqTasks.equals(""))
		{
			if(lDqTasks.equals("_"))
				dqTasks=Constants.DEFAULT_DQ_TASKS;
			else
				dqTasks = Integer.parseInt(lDqTasks);
		}
		else
			dqTasks=Constants.DEFAULT_DQ_TASKS;
		if(!lDqSchedulerHeartBeat.equals(""))
		{
			if(lDqSchedulerHeartBeat.equals("_"))
				dqSchedulerHeartBeat=Constants.DEFAULT_DQ_SCHEDULER_HEARTBEAT;
			else
				dqSchedulerHeartBeat = Double.parseDouble(lDqSchedulerHeartBeat);
		}
		else
			dqSchedulerHeartBeat=Constants.DEFAULT_DQ_SCHEDULER_HEARTBEAT;
		if(!lDqSchedulerActivation.equals(""))
		{
			if(lDqSchedulerActivation.equals("_"))
				dqSchedulerActivation=Constants.DEFAULT_DQ_SCHEDULER_ACTIVATION;
			else
				dqSchedulerActivation = Double.parseDouble(lDqSchedulerActivation);
		}
		else
			dqSchedulerActivation=Constants.DEFAULT_DQ_SCHEDULER_ACTIVATION;
		
		
	}
	protected void setFtTempVars(int i)
	{
		sLogger.log(Level.INFO,"Setting Ft vars !");
		
		if(ftGroupList!=null && ftGroupList.size()>=i)
			lFtGroup = (String) ftGroupList.get(i);
		else
			lFtGroup="";
		if(ftWeightList!=null && ftWeightList.size()>=i)
			lFtWeight = (String) ftWeightList.get(i);
		else
			lFtWeight="";
		if(ftActiveList!=null && ftActiveList.size()>=i)
			lFtActive = (String) ftActiveList.get(i);
		else
			lFtActive="";
			sLogger.log(Level.INFO,"FtActive:"+lFtActive);
		if(ftActiveGoalList!=null && ftActiveGoalList.size()-1>=i)
			lFtActiveGoal = (String) ftActiveGoalList.get(i);
		else
			lFtActiveGoal="";
		if(ftHeartBeatList!=null && ftHeartBeatList.size()>=i)
			lFtHeartBeat = (String) ftHeartBeatList.get(i);
		else
			lFtHeartBeat="";
		if(ftPrepIntervalList!=null && ftPrepIntervalList.size()>=i)
			lFtPrepInterval = (String) ftPrepIntervalList.get(i);
		else
			lFtPrepInterval="";
		if(ftActivationIntervalList!=null && ftActivationIntervalList.size()>=i)
			lFtActivationInterval = (String) ftActivationIntervalList.get(i);
		else
			lFtActivationInterval="";
		//------------------set number values-------------------
		if(!lFtActive.equals(""))
		{			
			if(lFtActive.equals(Constants.FT_ACTIVE_TRUE))
				ftActive = true;
			else
				ftActive=false;
		}
		else
			ftActive=Constants.DEFAULT_FT_ACTIVE;
			sLogger.log(Level.INFO,"ftActive:"+ftActive);
		if(!lFtWeight.equals(""))
		{
			if(lFtWeight.equals("_"))
				ftWeight=Constants.DEFAULT_FT_WEIGHT;
			else
				ftWeight = Integer.parseInt(lFtWeight);
		}
		else
			ftWeight=Constants.DEFAULT_FT_WEIGHT;
		if(!lFtActiveGoal.equals(""))
		{
			if(lFtActiveGoal.equals("_"))
				ftActiveGoal=Constants.DEFAULT_FT_ACTIVE_GOAL;
			else
				ftActiveGoal = Integer.parseInt(lFtActiveGoal);
		}
		else
			ftActiveGoal=Constants.DEFAULT_FT_ACTIVE_GOAL;	
		if(!lFtHeartBeat.equals(""))
		{
			if(lFtHeartBeat.equals("_"))
				ftHeartBeat=Constants.DEFAULT_FT_HEARTBEAT;
			else
				ftHeartBeat=Double.parseDouble(lFtHeartBeat);
		}
		else
			ftHeartBeat=Constants.DEFAULT_FT_HEARTBEAT;
		if(!lFtPrepInterval.equals(""))
		{
			if(lFtPrepInterval.equals("_"))
				ftPrepInterval=Constants.DEFAULT_FT_PREP_INTERVAL;
			else
				ftPrepInterval=Double.parseDouble(lFtPrepInterval);
		}
		else
			ftPrepInterval=Constants.DEFAULT_FT_PREP_INTERVAL;
		if(!lFtActivationInterval.equals(""))
		{
			if(lFtActivationInterval.equals("_"))
				ftActivationInterval=Constants.DEFAULT_FT_ACTIVATION_INTERVAL;
			else
				ftActivationInterval=Double.parseDouble(lFtActivationInterval);
		}
		else
			ftActivationInterval=Constants.DEFAULT_FT_ACTIVATION_INTERVAL;

	}

	protected BasicRVListener createBasicListener() throws RV_JCAException
	{

		sLogger.log(Level.INFO,"Creating a Basic RV listener");
		BasicVo vo = new BasicVo(lSubject,service,daemon,network);
		BasicRVListener brv =  (BasicRVListener) RVListenerFactory.getInstance().createBasicListener(vo);
		BasicCallBack callback  = new BasicCallBack(lService,this);
		brv.registerCallback(callback);					
		try
		{
			resourceAdapter.getWorkManager().startWork((Work)brv);
		}
		catch(WorkException excp)
		{
			String msg="Could not start WorkerThread";
			sLogger.log(Level.SEVERE,msg);
			throw new RV_JCAException(excp,msg);
		}
		sLogger.log(Level.INFO,"Started Basic RV listener !");
		return brv;
		
	}
	protected DqListener createDqListener() throws RV_JCAException
	{


		sLogger.log(Level.INFO,"Creating a DQ  listener");
		//cmName=DQ name i.e. subject name - all the extensions i.e. the root of the subject name - no *|>
						
		DqVo dqvo = new DqVo(lSubject,service,daemon,					
				network,getCmName(lSubject),dqWeight,dqTasks,dqSchedulerWeight,
				dqSchedulerHeartBeat,dqSchedulerActivation);
	
		DqListener dql =  (DqListener) RVListenerFactory.getInstance().createDQListener(dqvo);
		DqCallBack dqCallBack  = new DqCallBack(lService,this);
		dql.registerCallback(dqCallBack);					
		try
		{
			resourceAdapter.getWorkManager().startWork((Work)dql);
		}
		catch(WorkException excp)
		{
			String msg="Could not start WorkerThread";
			sLogger.log(Level.SEVERE,msg);
			throw new RV_JCAException(excp,msg);
		}
		sLogger.log(Level.INFO,"Started DQ listener !");
		return dql;
		
	}
	protected FtListener createFtListener() throws RV_JCAException
	{
		FtVo ftVo=null;
		FtListener ftListener=null;
		
		sLogger.log(Level.INFO,"Creating a FT  listener");
		if(ftActive)
		{
			sLogger.log(Level.INFO,"Creating an Active FT instance !");
			 ftVo = new FtVo(daemon, network, service,lFtGroup,lSubject,
			 		Constants.FT_ACTIVE_VALUE,ftActiveGoal,
					ftHeartBeat,ftPrepInterval,ftActivationInterval);
			 ftListener = new FtListener(ftVo);
			 ftListener.registerCallback(new FtCallBack(true,lService,this));
			 	
		}
		else
		{
			sLogger.log(Level.INFO,"Creating an InActive FT instance !");
			  ftVo =new FtVo(daemon, network, service,lFtGroup,lSubject,
			 		ftWeight,ftActiveGoal,
					ftHeartBeat,ftPrepInterval,ftActivationInterval);
			     ftListener = new FtListener(ftVo);
				 ftListener.registerCallback(new FtCallBack(false,lService,this));
		}		
			
		try
		{
			resourceAdapter.getWorkManager().startWork((Work)ftListener);
		}
		catch(WorkException excp)
		{
			String msg="Could not start WorkerThread";
			sLogger.log(Level.SEVERE,msg);
			throw new RV_JCAException(excp,msg);
		}
		sLogger.log(Level.INFO,"Started FT listener !");
		return ftListener;
	}
	

}
