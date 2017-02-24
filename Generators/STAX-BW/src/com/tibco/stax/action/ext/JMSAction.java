package com.tibco.stax.action.ext;

import java.util.HashMap;
import java.util.StringTokenizer;
import com.tibco.stax.action.jms.MessageProducer;

public class JMSAction extends Action
{
	private String host="",user="",pass="";	
	private int tokenCntr=0;
	private MessageProducer lMsgProducer;
	
	public JMSAction()
	{
		//super();
	}
	
	public void setProps(String pProps)
	{		
		tokenizer = new StringTokenizer(pProps,delimiter);
		System.out.println("Props:"+pProps);
		while(tokenizer.hasMoreElements())
		{
			String item = (String) tokenizer.nextElement();
			switch(tokenCntr) 
			{				
				case 0:
					host=item;					
					;
					
				case 1:
					user=item;
					;
				
				case 2:				
					pass=item;
			
			};
							
			tokenCntr++;
		}
		if(pass.equals(user))
			pass="";
		System.out.println("numtries="+tokenCntr+";host:"+host+",user:"+user+",pass:"+pass);
		
		lMsgProducer=new MessageProducer(host, user,pass);
		//lMsgProducer=new MessageProducer("tcp://esbsvr2:7222", "admin","");
	}
	public void execute(String pMessage)
	{		
		if(type.equals("queue"))
			lMsgProducer.createJmsQueueMessage(pMessage,destination);
		else
			lMsgProducer.createJmsTopicMessage(pMessage,destination);
	}
	/*
	public void setDestination(String pDest)
	{
		tokenizer = new StringTokenizer(pDest,delimiter);
		System.out.println("Destination:"+pDest);
		String type = (String) tokenizer.nextElement();
		String destination = (String) tokenizer.nextElement();
			
		System.out.println("numt_="+tokenCntr+";type:"+type+",dest:"+destination);
		
	}*/
}
