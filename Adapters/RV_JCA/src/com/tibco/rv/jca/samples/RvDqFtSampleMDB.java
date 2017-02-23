/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.samples;

import java.util.logging.Logger;

import com.tibco.rv.jca.RVListener;
import com.tibco.rv.jca.ra.BaseRVListenerManager;

import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvException;
import java.util.logging.Level;

import com.tibco.rv.jca.ra.synch.RVConnectionFactory;
import com.tibco.rv.jca.ra.synch.RVConnection;
import com.tibco.rv.jca.exceptions.RV_JCAException;

import javax.naming.InitialContext;


/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RvDqFtSampleMDB implements javax.ejb.MessageDrivenBean, RVListener
{
	/* (non-Javadoc)
	 * @see com.tibco.tibrv.TibrvMsgCallback#onMsg(com.tibco.tibrv.TibrvListener, com.tibco.tibrv.TibrvMsg)
	 */
	private static final String rbName = null; // No localization is used for logging.

	private static String className = RvDqFtSampleMDB.class.getName();
	private static Logger sLogger;
	
	private RVConnection connection=null;
	
	static 
	{
	     sLogger = Logger.getLogger(className, rbName);
	 
	}
	
	public void ejbCreate() throws CreateException, EJBException
	{
		
	}
	public void ejbRemove()
	{
		
	}
	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException
	{
		
	}
	/* (non-Javadoc)
	 * @see com.tibco.rv.jca.RVListener#onMessage(com.tibco.tibrv.TibrvMsg)
	 */
	public void onMessage(TibrvMsg pRvMsg)
	{
		// TODO Auto-generated method stub
		sLogger.log(Level.INFO,"In DqFt MDB...");
		String subject =pRvMsg.getSendSubject();
		if(subject.equals("ft1.subject"))
			sLogger.log(Level.INFO,"Receiveved FT Message:"+pRvMsg.toString()+", on subject:"+subject);
		if(subject.equals("dq1.subject"))
			sLogger.log(Level.INFO,"Receiveved  DQMessage:"+pRvMsg.toString()+", on subject:"+subject);
		
				
		sendReplyMessage(pRvMsg);

	}
	private void sendReplyMessage(TibrvMsg pRvMsg)
	{
		TibrvMsg msg=null;
		try
		{
			msg = new TibrvMsg();
			String destSubject=pRvMsg.getReplySubject();
			if(destSubject==null)
			   msg.setSendSubject("dq.ft.subject");
		  	   msg.add("Message:", "send msg from DqFt Message Driven Bean");
		  	   msg.add("Msg from sender:",pRvMsg.toString());
	       
		}
		catch(TibrvException excp)
		{
			excp.printStackTrace();
			
		}
		try
		{
			if(connection==null)
			{
				InitialContext ic = new InitialContext();
				RVConnectionFactory connectionFactory = (RVConnectionFactory) ic.lookup("RVConnectionFactory");
				connection = connectionFactory.getConnection();
			}
			connection.sendRvMsg(msg);
		}
		catch(RV_JCAException excp)
		{
			excp.printStackTrace();
			
		}
		catch(javax.naming.NamingException excp)
		{
			excp.printStackTrace();
		}
		
		
		      
		  
	}
}
