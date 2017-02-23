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
public class FtVo extends BasicVo
{
	private int weight;
	private int activeGoal;
	private String group;
	private double heartBeat;
	private double prepInterval;
	private double activationInterval;
		
	public FtVo(String pDaemon, String pNetwork, String pService,String pGroup,
				String pSubject,int pWeight,int pActiveGoal,
				double pHeartBeat,double pPrepInterval,double pActivationInterval)
	{
		super(pSubject,pService,pDaemon,pNetwork);
		weight=pWeight;
		activeGoal=pActiveGoal;
		group=pGroup;
		heartBeat=pHeartBeat;
		prepInterval=pPrepInterval;
		activationInterval=pActivationInterval;
		
	}

	
	/**
	 * @return Returns the activationInterval.
	 */
	public double getActivationInterval() {
		return activationInterval;
	}
	/**
	 * @return Returns the activeGoal.
	 */
	public int getActiveGoal() {
		return activeGoal;
	}
	/**
	 * @return Returns the group.
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @return Returns the heartBeat.
	 */
	public double getHeartBeat() {
		return heartBeat;
	}
	/**
	 * @return Returns the prepInterval.
	 */
	public double getPrepInterval() {
		return prepInterval;
	}
	/**
	 * @return Returns the weight.
	 */
	public int getWeight() {
		return weight;
	}
}
