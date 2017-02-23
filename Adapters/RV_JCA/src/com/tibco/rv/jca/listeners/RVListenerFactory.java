/*
 * Created on May 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.listeners;

import com.tibco.rv.jca.AbstractListener;
import com.tibco.rv.jca.listeners.*;
import com.tibco.rv.jca.vo.*;
import com.tibco.rv.jca.exceptions.*;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RVListenerFactory 
{
	
	private static RVListenerFactory instance=null;
	
	private RVListenerFactory()
	{
		
	}	
	public static RVListenerFactory getInstance()
	{
		if (instance==null)
				instance=new RVListenerFactory();
		return instance;
			
	}
	
	public AbstractListener createBasicListener(BasicVo pVo) throws RV_JCAException
	{
		return new BasicRVListener(pVo);
		
	}
	public AbstractListener createDQListener(DqVo pVo) throws RV_JCAException
	{
		return new DqListener(pVo);
		
	}
	public AbstractListener createFTListener(FtVo pVo) throws RV_JCAException
	{
		return new FtListener(pVo);
		
	}

}
