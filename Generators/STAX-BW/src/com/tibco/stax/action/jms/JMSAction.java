package com.tibco.stax.action.jms;

import com.tibco.stax.action.Action;

public class JMSAction implements Action
{
	private MessageProducer lMsgProducer;
	public JMSAction()
	{
		lMsgProducer=new MessageProducer("tcp://localhost:7222", "admin","");
	}
	public JMSAction(String providerURL, String userName,
            String password)
	{
		System.out.println("JMS-url:"+providerURL+",user:"+userName+",pass:"+password);
		lMsgProducer=new MessageProducer(providerURL, userName,password);
		
	}
	public void sendMessage(String pMsg,String pDestination)
	{
		lMsgProducer.createJmsQueueMessage(pMsg,pDestination);
	}
	
}
