package com.extemp.cem.backbone.core;

import com.extemp.cem.backbone.configuration.Settings;
import com.extemp.cem.backbone.configuration.SettingsExtension;
import com.extemp.cem.backbone.persistence.PersistenceController;
import com.extemp.cem.util.CEMUtil;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.camel.CamelExtension;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;

//public class JMSConsumer_Approval extends UntypedConsumerActor {
public class JMSConsumer_Vote extends BaseEventConsumer
{
    final SettingsExtension settings = Settings.SettingsProvider.get(context().system());
    private String uri;

    public boolean autoAck() {
	return true;
    }

    @Override
    public String getEndpointUri() {
	return "activemq:topic:cem.vote.queue";
    }

    public JMSConsumer_Vote() {
	//System.out.println("Settings - " + settings.DB_URI);
	this.uri = "activemq:topic:cem.vote.queue";
    }

    public JMSConsumer_Vote(String uri) {
	this.uri = uri;
    }

    @Override
    public void onReceive(Object message) {
    	
	if (message instanceof CamelMessage) {
		
	   CamelMessage msg = (CamelMessage) message;

	    String receivedMessage = msg.getBodyAs(String.class, CamelExtension.get(getContext().system()).context());
	    System.out.println("+++Received Vote Message - " + receivedMessage);

	 
	    if(rules==null)
	    	rules = getContext().actorOf(Props.create(RuleSession.class));
	    rules.tell(receivedMessage, getSelf());
	    
	    
	    
	    
	}
	else {
	    unhandled(message);
	}
    }
}
