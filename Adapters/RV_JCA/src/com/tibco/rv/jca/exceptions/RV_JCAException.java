/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.exceptions;

import com.tibco.tibrv.TibrvException;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RV_JCAException extends Exception
{
	protected RV_JCAExceptionHandler hdl=null;
	
	
	public RV_JCAException(Throwable pExcp,String pMsg)
	{
		super(pMsg,pExcp);
	}

}
