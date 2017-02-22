package com.extemp.cem.backbone.core;

import java.util.ArrayList;


import java.util.Collection;
import java.util.List;

import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import com.extemp.cem.backbone.configuration.Settings;
import com.extemp.cem.backbone.configuration.SettingsExtension;
import com.extemp.cem.profiles.CustomerProfileCBO;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.KBUtil;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.extemp.cem.actions.AcceptEventAction;
import com.extemp.cem.akka.kie.*;

public class RuleSession extends UntypedKieSessionActor 
{

	private AcceptEventAction _acceptEvent;
	private String uri="KieRuleSession";
	
	
	
	public RuleSession()
	{
		_acceptEvent = new AcceptEventAction();
	}
	public RuleSession(String pUri)
	{
		uri=pUri;
		_acceptEvent = new AcceptEventAction();
	}
	
	
	
	public String getEndpointUri() {
			return uri;
	}
    @Override
    public void onReceive(Object arg0) throws Exception 
    {
	// TODO Auto-generated method stub
    	
    	String _msg = (String) arg0;    	
    	if(_msg.contains("MobileEvent"))  
    	{
    		_acceptEvent.acceptMobileEvent(_msg);	
    		
    		//CEMUtil.getInstance().getAkkaSystem().actorSelection("PersistenceController").tell(arg0, getSelf());
    	}
    	if(_msg.contains("EnterpriseEvent"))
    		_acceptEvent.acceptEnterpriseEvent(_msg);
    	if(_msg.contains("OfferResponse"))
    		_acceptEvent.acceptOfferAprovalResponseEvent(_msg);
    	if(_msg.contains("TicketRequest"))
    		_acceptEvent.acceptTicketCheckValidateEvent(_msg);
    	if(_msg.contains("AuctionBidSubmit"))
    		_acceptEvent.acceptAuctionBidSubmitEvent(_msg);
    	if(_msg.contains("PurchaseRequest"))
    		_acceptEvent.acceptFBPurchaseRequestEvent(_msg);
    	if(_msg.contains("ActivityEvent"))
    		_acceptEvent.acceptBrowseActivityEvent(_msg);
    	if(_msg.contains("VoteRequest"))
    		_acceptEvent.acceptVoteEvent(_msg);
    	
	
    }

}
