package com.extemp.cem.auction;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.extemp.cem.backbone.core.JMSProducer;
import com.extemp.cem.backbone.core.JMSProducer_Auction;
import com.extemp.cem.backbone.core.KernelTest;
import com.extemp.cem.events.AuctionBidNotifyEvent;
import com.extemp.cem.profiles.CustomerProfileCBO;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.events.payloads.ObjectFactory;

public class AuctionManager
{
	// key = auction object name, value is recipient List
	private static HashMap auctionMap;
	private static ActorRef producer;
	
	
	private static AuctionManager instance=null;
	
	public static AuctionManager getInstance()
	{
		if(instance==null)
		{
			instance = new AuctionManager();
			auctionMap = new HashMap();
		}
		
		
		
		return instance;
	}
	

	public static void notifyParticipants(CustomerProfileCBO pCust,String pActivity,String pObject, String pObjectURI, String pContextCat, String pContextContent)
	{
		System.out.println("Notifying "+pCust.getUserId()+ " of "+pActivity+" for "+pObject+", for "+pContextCat+" for "+pContextContent+" : "+pObjectURI);
		
	}
	public static void notifyAllParticipants(AuctionBidNotifyEvent pEvent, ArrayList pList)
	{
		getInstance();
		
		Iterator _it = pList.iterator();
		while(_it.hasNext())
		{
			CustomerProfileCBO _cust = (CustomerProfileCBO) _it.next();			
			try
			{
				ObjectFactory _of = new ObjectFactory();
				
				com.extemp.cem.events.payloads.AuctionBidReply _notification = _of.createAuctionBidReply();
				
				_notification.setAuctionObjectId(pEvent.getAuctionObjectId());
				_notification.setRunningTotal(pEvent.getRunningTotal());
				_notification.setUserIdAuction(_cust.getUserId());
				_notification.setWinner(pEvent.getWinner());
				_notification.setStatus(pEvent.getStatus());
				
				
				StringWriter writer = new StringWriter();
				JAXBContext context = JAXBContext.newInstance(com.extemp.cem.events.payloads.AuctionBidReply.class);   			
				Marshaller m = context.createMarshaller();
				m.marshal(_notification, writer);
				String theXML = writer.toString();	
				//output string
				System.out.println(theXML);			
				
				///send out to AKKA JM channel
				if(producer==null)
					producer = KernelTest.getActorSystem().actorOf(Props.create(JMSProducer_Auction.class), "JMSProducer_Auction");
				//ActorRef producer = KernelTest.getActorSystem().actorFor("JMSProducer");
				ActorRef _self = KernelTest.getActorSystem().actorFor("AuctionBidReply");
				//final ActorRef producer = getContext().actorOf(Props.create(JMSProducer.class), "JMSProducer");
				producer.tell(theXML,_self);		
				
				
			}
			catch(Exception excp)
			{
				excp.printStackTrace();
			}
			
			
		}
		
	}
	public static void startAuctionSessionCounter(String pAuctionObjectId)
	{
		 AuctionSessionTimer _timer = new AuctionSessionTimer(pAuctionObjectId);
		Thread _thread = new Thread(_timer);
		_thread.start();
	}
	

}
