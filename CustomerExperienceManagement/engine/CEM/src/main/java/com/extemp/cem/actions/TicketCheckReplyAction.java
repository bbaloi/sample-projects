package com.extemp.cem.actions;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.camel.javaapi.UntypedConsumerActor;

import com.extemp.cem.akka.kie.UntypedKieSessionActor;
import com.extemp.cem.backbone.core.JMSProducer;
import com.extemp.cem.backbone.core.JMSProducer_TicketCheck;
import com.extemp.cem.backbone.core.KernelTest;
import com.extemp.cem.events.Notification;
import com.extemp.cem.events.payloads.ObjectFactory;

import akka.actor.Props;

//public class SendNotificationAction extends UntypedConsumerActor

public class TicketCheckReplyAction extends UntypedKieSessionActor 
{
	private static ActorRef producer;
		
	public static void sendTicketCheckReply(String pUserName, String pSeatNumber)
	{
		//System.out.println("**** Sending Notification for user "+pNotification.getUserId()+", message="+pNotification.getNotificationMessage()+"  ************");
	
		try
		{
			ObjectFactory _of = new ObjectFactory();
			
			com.extemp.cem.events.payloads.TicketReply _reply = _of.createTicketReply();
			
			_reply.setReplyStatus("Welcome "+pUserName+", you seat is: "+ pSeatNumber);
			
			
			
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(com.extemp.cem.events.payloads.TicketReply.class);   			
			Marshaller m = context.createMarshaller();
			m.marshal(_reply, writer);
			String theXML = writer.toString();	
			//output string
			System.out.println(theXML);			
			
			///send out to AKKA JM channel
			if(producer==null)
				producer = KernelTest.getActorSystem().actorOf(Props.create(JMSProducer_TicketCheck.class), "JMSProducer_TicketChek");
			//ActorRef producer = KernelTest.getActorSystem().actorFor("JMSProducer");
			ActorRef _self = KernelTest.getActorSystem().actorFor("TicketCheckReplyAction");
			//final ActorRef producer = getContext().actorOf(Props.create(JMSProducer.class), "JMSProducer");
			producer.tell(theXML,_self);		
			
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		// output string to console		
	
	}


	@Override
	public void onReceive(Object arg0) throws Exception 
	{
		
		
	}


}
