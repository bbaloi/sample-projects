package com.extemp.cem.backbone.core;

import akka.actor.ActorRef;
import akka.camel.javaapi.UntypedProducerActor;

public class JMSProducer_Approval extends UntypedProducerActor {

    private String uri;

    @Override
    public String getEndpointUri() {
        return "activemq:topic:cem.req.approval.topic";
    }

    public JMSProducer_Approval() {
        this.uri = "activemq:topic:cem.req.approval.topic";
    }

    public JMSProducer_Approval(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean isOneway() {
        return true;
    }

    @Override
    public Object onTransformOutgoingMessage(Object message) {

        System.out.println("###  Offer Request Approval");
        
       
        return message;
    }

}
