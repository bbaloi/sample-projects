/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca;

import com.tibco.tibrv.TibrvMsg;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface RVListener 
{
	public void onMessage(TibrvMsg pRvMsg);
}
