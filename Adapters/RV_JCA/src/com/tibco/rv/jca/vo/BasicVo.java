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
public class BasicVo implements java.io.Serializable
{
	protected String subject;
	protected String service;
	protected String network;
	protected String daemon;
	
	public BasicVo(String pSubjectName, String pServiceName,String pDaemon,String pNetwork)
	{
		subject=pSubjectName;		
		service=pServiceName;
		daemon=pDaemon;
		network=pNetwork;
		
	}

		/**
	 * @return Returns the dqName.
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @return Returns the serviceName.
	 */
	public String getService() {
		return service;
	}
	/**
	 * @return Returns the daemon.
	 */
	public String getDaemon() {
		return daemon;
	}
	/**
	 * @return Returns the network.
	 */
	public String getNetwork() {
		return network;
	}
	
}
