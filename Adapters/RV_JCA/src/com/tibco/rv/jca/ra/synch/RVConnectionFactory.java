/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.ra.synch;

import com.tibco.rv.jca.exceptions.RV_JCAException;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface RVConnectionFactory 
{
	public RVConnection getConnection() throws RV_JCAException;
	public RVConnection getConnection(String pService,String pNetwotk,String pDaemon) throws RV_JCAException;
	public RVConnection getConnection(String puserName,String pPassword) throws RV_JCAException;
	public RVConnection getConnection(String pUserName,String pPassword,String pService,String pNetwotk,String pDaemon) throws RV_JCAException;

}
