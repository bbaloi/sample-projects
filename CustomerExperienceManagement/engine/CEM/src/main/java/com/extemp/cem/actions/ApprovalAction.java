package com.extemp.cem.actions;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.extemp.cem.akka.kie.UntypedKieSessionActor;
import com.extemp.cem.backbone.core.JMSProducer;
import com.extemp.cem.backbone.core.JMSProducer_Approval;
import com.extemp.cem.backbone.core.KernelTest;
import com.extemp.cem.backbone.persistence.PersistenceController;
import com.extemp.cem.events.OfferApprovalRequestEvent;
import com.extemp.cem.profiles.ObjectFactory;
import com.extemp.cem.util.CEMUtil;

public class ApprovalAction extends UntypedKieSessionActor 
{
	private static ActorRef producer,_persistenceController;

	public static void sendRequestForAproval(OfferApprovalRequestEvent pEvent)
	{
		try
		{
			ObjectFactory _of = new ObjectFactory();
			
			com.extemp.cem.profiles.OfferRequest _req = _of.createOfferRequest();
			
			_req.setApplicationSrcOffer(pEvent.getApplicationSrc());
			_req.setOffer(pEvent.getOffer());
			
			_req.setOfferType(pEvent.getOfferType());
			_req.setReqIdOffer(pEvent.getReqId());
			_req.setUserIdOfferReq(pEvent.getUserId());
			_req.setUserName(pEvent.getUserName());
						
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(com.extemp.cem.profiles.OfferRequest.class);   			
			Marshaller m = context.createMarshaller();
			m.marshal(_req, writer);
			String theXML = writer.toString();	
			//output string
			System.out.println(theXML);			
			
			///send out to AKKA JM channel
			if(producer==null)
				producer = KernelTest.getActorSystem().actorOf(Props.create(JMSProducer_Approval.class), "JMSProducer_Approval");
			//ActorRef producer = KernelTest.getActorSystem().actorFor("JMSProducer");
			ActorRef _self = KernelTest.getActorSystem().actorFor("ApprovalAtion");
			//final ActorRef producer = getContext().actorOf(Props.create(JMSProducer.class), "JMSProducer");
			producer.tell(theXML,_self);		
			
			
			System.out.println(" offer message = "+_req.getOffer());
			
			
			if(CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController_1")==null)
				 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
				
			CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController_1").tell(_req, CEMUtil.getInstance().getRuleSession());
	        
			/*
			  if(_persistenceController==null)
			   		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
			   	 _persistenceController.tell(_offerResp, CEMUtil.getInstance().getRuleSession());
			   	 */
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
	}
	public static void handleApprovalRequest_Rejected()
	{
		
	}
	public static void handleApprovalRequest_Approved()
	{
		
	}
	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
