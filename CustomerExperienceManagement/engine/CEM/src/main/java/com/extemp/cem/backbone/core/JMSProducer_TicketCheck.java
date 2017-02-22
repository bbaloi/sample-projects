package com.extemp.cem.backbone.core;

import akka.actor.ActorRef;
import akka.camel.javaapi.UntypedProducerActor;

public class JMSProducer_TicketCheck extends UntypedProducerActor {

    private String uri;

    @Override
    public String getEndpointUri() {
        return "activemq:topic:cem.ticket.check.resp.queue";
    }

    public JMSProducer_TicketCheck() {
        this.uri = "activemq:topic:cem.ticket.check.resp.queue";
    }

    public JMSProducer_TicketCheck(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean isOneway() {
        return true;
    }

    @Override
    public Object onTransformOutgoingMessage(Object message) {

        System.out.println("###  Ticket Check Response ");
        
       
        return message;
    }

}
