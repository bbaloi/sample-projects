package com.extemp.cem.backbone.core;

import com.extemp.cem.backbone.configuration.Settings;

import com.extemp.cem.backbone.configuration.SettingsExtension;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.camel.CamelExtension;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;

//public class JMSConsumer_Approval extends UntypedConsumerActor {
public class JMSConsumer_TicketCheck extends BaseEventConsumer
{
    final SettingsExtension settings = Settings.SettingsProvider.get(context().system());
    private String uri;

    public boolean autoAck() {
	return true;
    }

    @Override
    public String getEndpointUri() {
	return "activemq:topic:cem.ticket.check.req.queue";
    }

    public JMSConsumer_TicketCheck() {
	//System.out.println("Settings - " + settings.DB_URI);
	this.uri = "activemq:topic:cem.ticket.check.req.queue";
    }

    public JMSConsumer_TicketCheck(String uri) {
	this.uri = uri;
    }

    @Override
    public void onReceive(Object message) {
    	
	if (message instanceof CamelMessage) {
		
	   CamelMessage msg = (CamelMessage) message;

	    String receivedMessage = msg.getBodyAs(String.class, CamelExtension.get(getContext().system()).context());
	    System.out.println("+++Received Ticket Check Message - " + receivedMessage);

	    if(rules==null)
	    	rules = getContext().actorOf(Props.create(RuleSession.class));
	    rules.tell(receivedMessage, getSelf());
	    
	}
	else {
	    unhandled(message);
	}
    }
}
