package com.extemp.cem.actions;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.camel.javaapi.UntypedConsumerActor;

import com.extemp.cem.akka.kie.UntypedKieSessionActor;
import com.extemp.cem.backbone.core.JMSProducer;
import com.extemp.cem.backbone.core.KernelTest;
import com.extemp.cem.events.Notification;
import com.extemp.cem.profiles.CustomerProfileCBO;
import com.extemp.cem.profiles.ObjectFactory;
import com.extemp.cem.util.KBUtil;

import akka.actor.Props;

//public class SendNotificationAction extends UntypedConsumerActor

public class SendNotificationAction extends UntypedKieSessionActor 
{
	private static ActorRef producer;
		
	public static void sendNotification(Notification pNotification)
	{
		//System.out.println("**** Sending Notification for user "+pNotification.getUserId()+", message="+pNotification.getNotificationMessage()+"  ************");
	
		try
		{
			ObjectFactory _of = new ObjectFactory();
			
			com.extemp.cem.profiles.Notification _notification = _of.createNotification();
			
			_notification.setApplicationSrcNotification(pNotification.getApplicationSource());
			_notification.setId(pNotification.getNotificationId());
			_notification.setName(pNotification.getNotificationName());
			_notification.setNotificationMessage(pNotification.getNotificationMessage());
			_notification.setReqIdNotification(pNotification.getRequestId());
			_notification.setSource(pNotification.getSource());
			_notification.setType(pNotification.getNotificationType());
			_notification.setUserIdNotification(pNotification.getUserId());
			_notification.setPayload(pNotification.getNotificationPaylod());
			
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(com.extemp.cem.profiles.Notification.class);   			
			Marshaller m = context.createMarshaller();
			m.marshal(_notification, writer);
			String theXML = writer.toString();	
			//output string
			System.out.println(theXML);			
			
			///send out to AKKA JM channel
			if(producer==null)
				producer = KernelTest.getActorSystem().actorOf(Props.create(JMSProducer.class), "JMSProducer_1");
			//ActorRef producer = KernelTest.getActorSystem().actorFor("JMSProducer");
			ActorRef _self = KernelTest.getActorSystem().actorFor("NotificationAtion");
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
		// TODO Auto-generated method stub
		
		Notification pNotification = (Notification) arg0;
		try
		{
			ObjectFactory _of = new ObjectFactory();
			
			com.extemp.cem.profiles.Notification _notification = _of.createNotification();
			
			_notification.setApplicationSrcNotification(pNotification.getApplicationSource());
			_notification.setId(pNotification.getNotificationId());
			_notification.setName(pNotification.getNotificationName());
			_notification.setNotificationMessage(pNotification.getNotificationMessage());
			_notification.setReqIdNotification(pNotification.getRequestId());
			_notification.setSource(pNotification.getSource());
			_notification.setType(pNotification.getNotificationType());
			_notification.setUserIdNotification(pNotification.getUserId());
			
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(com.extemp.cem.profiles.Notification.class);   			
			Marshaller m = context.createMarshaller();
			m.marshal(_notification, writer);
			String theXML = writer.toString();	
			//output string
			System.out.println(theXML);			
			
			///send out to AKKA JM channel
			final ActorRef producer = getContext().actorOf(Props.create(JMSProducer.class), "JMSProducer");
			producer.tell(theXML, getSelf());		
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
		
		
	}
	
	public static List getCustomerList()
	{
		ArrayList <CustomerProfileCBO>_custList = new ArrayList();
		
		QueryResults results = KBUtil.getInstance().getKieSession().getQueryResults( "getCustomerListQuery" ); 
		for ( QueryResultsRow row : results ) {
			CustomerProfileCBO cust = ( CustomerProfileCBO ) row.get( "$customer" ); //you can retrieve all the bounded variables here
		    //do whatever you want with classA
			_custList.add(cust);
		}
		
		//Collection<Object> myfacts = KBUtil.getInstance().getKieSession().getObjects( new org.drools.core.ClassObjectFilter(CustomerProfileCBO.class) );
		
		return _custList;
	}



}
