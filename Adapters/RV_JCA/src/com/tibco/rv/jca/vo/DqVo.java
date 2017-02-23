/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.vo;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DqVo extends BasicVo
{
	private String cmName;
	private int weight;
	private int tasks;
	private double schedulerHeartBeat;
	private double schedulerActivation;
	private int schedulerWeight;
	
	public DqVo(String pSubjectName, String pServiceName, String pDaemon,
				String pNetwork, String pCMName,int pWeight,
				int pTasks,int pSchedulerWeight,double pSchedulerHeartBeat,double pSchedulerActivation)
	{
		super(pSubjectName,pServiceName,pDaemon,pNetwork);		
		cmName=pCMName;
		weight=pWeight;
		tasks = pTasks;
		schedulerWeight=pSchedulerWeight;
		schedulerHeartBeat=pSchedulerHeartBeat;
		schedulerActivation=pSchedulerActivation;
		
	}

	/**
	 * @return Returns the cmName.
	 */
	public String getCmName() {
		return cmName;
	}
	/**
	 * @return Returns the weight.
	 */
	public int getWeight() {
		return weight;
	}
	/**
	 * @return Returns the tasks.
	 */
	public int getTasks() {
		return tasks;
	}
	/**
	 * @return Returns the schedulerWeight.
	 */
	public int getSchedulerWeight() {
		return schedulerWeight;
	}
	/**
	 * @return Returns the schedulerActivation.
	 */
	public double getSchedulerActivation() {
		return schedulerActivation;
	}
	/**
	 * @return Returns the schedulerHeartBeat.
	 */
	public double getSchedulerHeartBeat() {
		return schedulerHeartBeat;
	}
}
