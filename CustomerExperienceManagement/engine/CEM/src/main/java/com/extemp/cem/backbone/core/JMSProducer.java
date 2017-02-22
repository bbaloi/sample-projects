package com.extemp.cem.backbone.core;

import akka.actor.ActorRef;

import akka.camel.javaapi.UntypedProducerActor;

public class JMSProducer extends UntypedProducerActor {

    private String uri;

    @Override
    public String getEndpointUri() {
        return "activemq:topic:cem.rcv.notification.queue";
    }

    public JMSProducer() {
        this.uri = "activemq:topic:cem.rcv.notification.queue";
    }

    public JMSProducer(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean isOneway() {
        return true;
    }

    @Override
    public Object onTransformOutgoingMessage(Object message) {

        System.out.println("### Notification Message");
        
       
        return message;
    }

}
