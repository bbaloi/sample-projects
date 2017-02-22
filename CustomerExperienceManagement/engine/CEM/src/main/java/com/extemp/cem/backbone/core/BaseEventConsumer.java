package com.extemp.cem.backbone.core;

import akka.actor.ActorRef;
import akka.camel.javaapi.UntypedConsumerActor;

public class BaseEventConsumer extends UntypedConsumerActor
{

	 protected static ActorRef rules=null;
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
