package com.extemp.cem.actions;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;

import org.kie.api.runtime.rule.EntryPoint;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import com.extemp.cem.backbone.core.JMSConsumer;
import com.extemp.cem.backbone.core.RuleSession;
import com.extemp.cem.backbone.persistence.PersistenceController;
import com.extemp.cem.events.AuctionBidNotifyEvent;
import com.extemp.cem.events.AuctionBidSubmitEvent;
import com.extemp.cem.events.BrowseActivityEvent;
import com.extemp.cem.events.EnterpriseActivityEvent;
import com.extemp.cem.events.OfferApprovalResponseEvent;
import com.extemp.cem.events.OrderItem;
import com.extemp.cem.events.SportsVenueMobileEvent;
import com.extemp.cem.events.payloads.AuctionBidSubmit;
import com.extemp.cem.events.payloads.PurchaseItem;
import com.extemp.cem.events.payloads.PurchaseItemList;
import com.extemp.cem.events.payloads.PurchaseRequest;
import com.extemp.cem.events.payloads.TicketRequest;
import com.extemp.cem.events.payloads.VoteRequest;
import com.extemp.cem.profiles.ActivityEvent;
import com.extemp.cem.profiles.MobileEvent;
import com.extemp.cem.profiles.EnterpriseEvent;
import com.extemp.cem.profiles.OfferResponse;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.KBUtil;

public class AcceptEventAction 
{
	// system.actorOf(Props.create(JMSConsumer.class), "JMSConsumer");
	
	private static ActorRef _persistenceController=null;
	
	public static void acceptMobileEvent(String pEvent)
	{
		SportsVenueMobileEvent _mEvent=null;
		MobileEvent _event =null;
		
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(MobileEvent.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _event = (MobileEvent) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		if(_event.getEventType().contains("ARENA") || _event.getEventType().contains("STADIUM") || _event.getEventType().contains("SPORT"))
		{
			 _mEvent = new SportsVenueMobileEvent();
			 
			 _mEvent.setEventId(_event.getEventId());
			 _mEvent.setUserId(_event.getUserIdEvent());
			 _mEvent.setEventName(_event.getEventName());
			 _mEvent.setEventType(_event.getEventType());
			 _mEvent.setDeviceId(_event.getDeviceId());
			 _mEvent.setDevicePhoneNumber(_event.getDevicePhoneNumber());
			 _mEvent.setLocationCoordinate(_event.getGeoLocationCoords());
			 _mEvent.setLocationName(_event.getLocationName());
			 _mEvent.setSeatNumber(_event.getSeatNumber());
			 // "purchaseItemsList",
			 //_ "gameWin",
			 _mEvent.setEvenScorePlayer(_event.getEventScorePlayer());
			 _mEvent.setEventMessage(_event.getEventMessage());
			 _mEvent.setRequestId(_event.getReqIdEvent());
			 _mEvent.setApplicationSource(_event.getApplicationSrc());		
			 if (_event.isGameWin()!=null)
				 _mEvent.setGameWin(_event.isGameWin());
			 
		}
			    	   
		
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "MobileEventChannel");
		
		entryPoint.insert(_mEvent);		
				
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();


        // Close the session
       // KBUtil.getInstance().getKSession().destroy();
        System.out.println("*** MOBILE EVENT TRIGGERED *** ");
        
        
        	//CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController").tell(_mEvent, CEMUtil.getInstance().getRuleSession());
        
        	 if(_persistenceController==null)
        		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
        	 _persistenceController.tell(_mEvent, CEMUtil.getInstance().getRuleSession());
        	 
        		 	
	}
	
	public static void acceptOfferAprovalResponseEvent(String pEvent)
	{
		
		OfferResponse _offerResp=null;
		OfferApprovalResponseEvent _offerRespEvent=null;
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(OfferResponse.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _offerResp = (OfferResponse) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		_offerRespEvent = new OfferApprovalResponseEvent();
		
		_offerRespEvent.setUserId(_offerResp.getUserIdOfferReq());
		_offerRespEvent.setUserName(_offerResp.getUserName());
		_offerRespEvent.setReqId(_offerResp.getReqIdOffer());
		_offerRespEvent.setOfferType(_offerResp.getOfferType());		
		_offerRespEvent.setOffer(_offerResp.getOffer());
		_offerRespEvent.setApplicationSrc(_offerResp.getApplicationSrcOffer());			
		_offerRespEvent.setApproved(_offerResp.isApprove());
	
		//System.out.println(" ***** ReqId="+_offerResp.getReqIdOffer()+",userid="+_offerResp.getUserIdOfferReq()+",Offertype="+_offerResp.getOfferType()+", approved="+_offerResp.isApprove());
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "OfferApprovalEventChannel");
		
		entryPoint.insert(_offerRespEvent);		
				
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();

        if(_persistenceController==null)
   		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
   	 _persistenceController.tell(_offerResp, CEMUtil.getInstance().getRuleSession());
        // Close the session
       // KBUtil.getInstance().getKSession().destroy();
      
       
	}
	
	public static void acceptTicketCheckValidateEvent(String pEvent)
	{
		TicketRequest _ticketEvent =null;
		SportsVenueMobileEvent _mEvent=null;
		
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(TicketRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _ticketEvent = (TicketRequest) unmarshaller.unmarshal(reader);
			 
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
			 _mEvent = new SportsVenueMobileEvent();
			 
			 _mEvent.setUserId(_ticketEvent.getUserIdTicket());
			 _mEvent.setEventName("GAME_ON_TICKET_CHECK");
			 _mEvent.setEventType("IN_ARENA");
			 _mEvent.setSeatNumber(_ticketEvent.getSeatNum());			
			 _mEvent.setApplicationSource(_ticketEvent.getApplicationSrcTicket());
			 _mEvent.setRequestId(_ticketEvent.getReqIdTicket());			
		    
			 
			 System.out.println("TicketUID="+_mEvent.getUserId()+",eventname="+_mEvent.getEventName()+", seat="+_mEvent.getSeatNumber()+",reqid="+_mEvent.getRequestId());
		    
			//System.out.println(" ***** ReqId="+_offerResp.getReqIdOffer()+",userid="+_offerResp.getUserIdOfferReq()+",Offertype="+_offerResp.getOfferType()+", approved="+_offerResp.isApprove());
			EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "MobileEventChannel");
			
			entryPoint.insert(_mEvent);		
					
			  // Fire all Rules
	        KBUtil.getInstance().getKSession().fireAllRules();
	        
	        if(_persistenceController==null)
	      		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
	      	 _persistenceController.tell(_mEvent, CEMUtil.getInstance().getRuleSession());

		
	}
	
	public static void acceptEnterpriseEvent(String pEvent)
	{
		EnterpriseActivityEvent _entActEvent=null;
		EnterpriseEvent _event =null;
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(EnterpriseEvent.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _event = (EnterpriseEvent) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
			 _entActEvent = new EnterpriseActivityEvent();
			 
			 _entActEvent.setEventId(_event.getEntEventId());
			 _entActEvent.setUserId(_event.getEntUserId());
			 _entActEvent.setEventName(_event.getEntEventName());
			 _entActEvent.setEventType(_event.getEntEventType());
			 _entActEvent.setActivityObjectCategory(_event.getEntActivityCategory());
			 _entActEvent.setActivityName(_event.getEntActivity());
			 _entActEvent.setActivityObject(_event.getEntActivityObject());
			 _entActEvent.setActivityObjectURI(_event.getEntActivityObjectUri());
			 _entActEvent.setEnterpriseId(_event.getEntId());
			 _entActEvent.setEventSource(_event.getEntAppSource());			 
			 _entActEvent.setActivityContextCategory(_event.getEntActivityContextCategory());
			 _entActEvent.setActivityContextContent(_event.getEntActivityContextContent());
			    	   
		
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "EntepriseEventChannel");
		
		entryPoint.insert(_entActEvent);		
				
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();


        // Close the session
       // KBUtil.getInstance().getKSession().destroy();
        System.out.println("*** ENTERPRISE EVENT TRIGGERED *** ");
				
	}
	
	public static void acceptAuctionBidSubmitEvent(String pEvent)
	{
		AuctionBidSubmit _event=null;
		AuctionBidSubmitEvent _bidEvent =null;
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(AuctionBidSubmit.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _event = (AuctionBidSubmit) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
			 _bidEvent = new AuctionBidSubmitEvent();	
			 _bidEvent.setEventId("347");
			 _bidEvent.setEventName("GAME_ON_IN_GAME_AUCTION_BID");
			 _bidEvent.setEventType("IN_ARENA");
			 _bidEvent.setUserId(_event.getUserIdAuction());
			 _bidEvent.setAuctionObjectId(_event.getAuctionObjectId());
			 _bidEvent.setEventSource(_event.getApplicationSrcAuction());
			 _bidEvent.setCurrentBid(_event.getCurrentBid());
			 _bidEvent.setRunningTotal(_event.getRunningTotal());
			    	   
		
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "AuctionEventChannel");
		
		entryPoint.insert(_bidEvent);		
				
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();


        // Close the session
       // KBUtil.getInstance().getKSession().destroy();
        System.out.println("*** AUCTION BID SUBMITTED *** ");
        
        if(_persistenceController==null)
     		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
     	 _persistenceController.tell(_bidEvent, CEMUtil.getInstance().getRuleSession());
        
		
	}
	
	public static void acceptFBPurchaseRequestEvent(String pEvent)
	{
		
		PurchaseRequest _purchaseReq =null;
		SportsVenueMobileEvent _mEvent=null;
		
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(PurchaseRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _purchaseReq = (PurchaseRequest) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		
			 _mEvent = new SportsVenueMobileEvent();
			 
			 _mEvent.setEventId("fb_purchase");
			 _mEvent.setUserId(_purchaseReq.getUserIdOrder());
			 _mEvent.setEventName("GAME_ON_FOOD_PURCHASE");
			 _mEvent.setEventType("IN_ARENA");
			 _mEvent.setSeatNumber(_purchaseReq.getSeatNumber());		 
			 _mEvent.setApplicationSource(_purchaseReq.getApplicationSrcOrder());				 
			 PurchaseItemList _itemList =  _purchaseReq.getPurchaseItemList();				 
			 ArrayList _iList = new ArrayList();
			 List _list = _itemList.getPurchaseItem();
			 Iterator _it = _list.iterator();
			 while(_it.hasNext())
			 {
				 PurchaseItem _item = (PurchaseItem) _it.next();
				 OrderItem _oi = new OrderItem(_item.getItemId(),_item.getItemName(),_item.getPrice().doubleValue(),_item.getQuantity().intValue(),_purchaseReq.getOrderId());				 
				 _iList.add(_oi);
				 
			 }
			 
			 _mEvent.setGamePurchaseItemList(_iList);
	  
		
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "MobileEventChannel");
		
		entryPoint.insert(_mEvent);		
				
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();


        // Close the session
       // KBUtil.getInstance().getKSession().destroy();
        System.out.println("*** MOBILE EVENT TRIGGERED *** ");
     //  CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController").tell(_purchaseReq, CEMUtil.getInstance().getRuleSession());
        
        if(_persistenceController==null)
   		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
   	 _persistenceController.tell(_purchaseReq, CEMUtil.getInstance().getRuleSession());
		
	}
    
	public void acceptBrowseActivityEvent(String pEvent)
	{
		ActivityEvent _inEvent = null;
		BrowseActivityEvent _outEvent=null;
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(ActivityEvent.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _inEvent = (ActivityEvent) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		

		_outEvent = new BrowseActivityEvent();
		
		_outEvent.setUserID(_inEvent.getUserID());		
		_outEvent.setEventName("BROWSE_ACTIVITY_EVENT");
		_outEvent.setEventType("MOBILE_ACTIVITY_EVENT");
		_outEvent.setActivityType(_inEvent.getActivityType());
		_outEvent.setBrowsedItemBrand(_inEvent.getBrowsedItemBrand());
		_outEvent.setBrowsedItemCategory(_inEvent.getBrowsedItemCategory());
		_outEvent.setBrowsedItemGender(_inEvent.getBrowsedItemGender());		
		_outEvent.setBrowsedItemID(_inEvent.getBrowsedItemID());
		_outEvent.setBrowsedItemName(_inEvent.getBrowsedItemName());		
		if(_inEvent.getBrowsedItemPrice()!=null)
			_outEvent.setBrowsedItemPrice(_inEvent.getBrowsedItemPrice());
		_outEvent.setBrowsedItemSize(_inEvent.getBrowsedItemSize());
		_outEvent.setBrowsedURL(_inEvent.getBrowsedURL());
		_outEvent.setEventID(_inEvent.getEventID());
		_outEvent.setExpiryDate(_inEvent.getExpiryDate());
		_outEvent.setPublishedContentCategory(_inEvent.getPublishedContentCategory());
		_outEvent.setPublishedContentData(_inEvent.getPublishedContentData());
		_outEvent.setPublishedContentID(_inEvent.getPublishedContentID());
		_outEvent.setPublishedContentTopic(_inEvent.getPublishedContentTopic());
		_outEvent.setPublishedContentURL(_inEvent.getPublishedContentURL());
	
		//System.out.println(" ***** ReqId="+_offerResp.getReqIdOffer()+",userid="+_offerResp.getUserIdOfferReq()+",Offertype="+_offerResp.getOfferType()+", approved="+_offerResp.isApprove());
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "BrowseActivityEventChannel");
		
		entryPoint.insert(_outEvent);						
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();
        
       // CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController").tell(_outEvent, CEMUtil.getInstance().getRuleSession());
        if(_persistenceController==null)
   		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
   	 _persistenceController.tell(_outEvent, CEMUtil.getInstance().getRuleSession());
	}

	public void acceptVoteEvent(String pEvent)
	{
		System.out.println("Vote event......");
		
		VoteRequest _req=null;
		
		try
		{
		
			JAXBContext jaxbContext = JAXBContext.newInstance(VoteRequest.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(pEvent);
			 _req = (VoteRequest) unmarshaller.unmarshal(reader);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		
		  if(_persistenceController==null)
		   		 _persistenceController=CEMUtil.getInstance().getAkkaSystem().actorOf(Props.create(PersistenceController.class), "PersistenceController_1");
		   	 _persistenceController.tell(_req, CEMUtil.getInstance().getRuleSession());
	}
	    
    
}
