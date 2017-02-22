package com.extemp.cem.actions;

import com.extemp.cem.backbone.core.RuleSession;
import com.extemp.cem.backbone.core.SentimentClassifier;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.camel.javaapi.UntypedConsumerActor;

public class BaseSentimentAction extends UntypedConsumerActor
{

	 protected static ActorRef sentimentClassifier=null;
	 
	 public BaseSentimentAction()
	 {

		//System.out.println(" in Base SentimentAction Construtor !");
		 //sentimentClassifier = getContext().actorOf(Props.create(SentimentClassifier.class));
		   
	 }
	@Override
	public String getEndpointUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
