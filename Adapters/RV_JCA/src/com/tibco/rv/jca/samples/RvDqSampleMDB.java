/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.samples;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.tibco.rv.jca.RVListener;
import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvListener;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RvDqSampleMDB implements javax.ejb.MessageDrivenBean, RVListener
{
	/* (non-Javadoc)
	 * @see com.tibco.rv.jca.RVListener#onMessage(com.tibco.tibrv.TibrvMsg)
	 */
	 private static final String rbName = null; // No localization is used for logging.

		private static String className = RvDqSampleMDB.class.getName();
		private static Logger sLogger;
		
		static 
		{
		     sLogger = Logger.getLogger(className, rbName);
		 
		}
		
		public void onMessage(TibrvMsg pRvMsg) {
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"In Dq MDB...");
		sLogger.log(Level.INFO,"Receiveved Message:"+pRvMsg.toString()+", on subject:"+pRvMsg.getSendSubject());

	}
	/* (non-Javadoc)
	 * @see com.tibco.tibrv.TibrvMsgCallback#onMsg(com.tibco.tibrv.TibrvListener, com.tibco.tibrv.TibrvMsg)
	 */
	public void ejbCreate() throws CreateException, EJBException
	{
		
	}
	public void ejbRemove()
	{
		
	}
	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException
	{
		
	}
		

}
