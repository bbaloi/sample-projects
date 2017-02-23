/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.tibco.rv.jca.ra.synch;

import com.tibco.rv.jca.exceptions.RV_JCAException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.rv.jca.exceptions.RV_JCAException;
/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IRVSender 
{
	public void sendRvMsg(TibrvMsg pMsg) throws RV_JCAException;
	public TibrvMsg sendRequest(TibrvMsg pMsg,double pTimeout) throws RV_JCAException;
	public void sendReply(TibrvMsg pReplyMsg, TibrvMsg pRequestMsg) throws RV_JCAException;

}
