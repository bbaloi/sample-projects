package com.extemp.cem.backbone.core;

import akka.actor.ActorRef;
import akka.camel.javaapi.UntypedProducerActor;

public class JMSProducer_Auction extends UntypedProducerActor {

    private String uri;

    @Override
    public String getEndpointUri() {
        return "activemq:topic:cem.auction.bid.notification.topic";
    }

    public JMSProducer_Auction() {
        this.uri = "activemq:cem.auction.bid.notification.topic";
    }

    public JMSProducer_Auction(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean isOneway() {
        return true;
    }

    @Override
    public Object onTransformOutgoingMessage(Object message) {

        System.out.println("###  Auction Bid Reply ");
        
       
        return message;
    }

}
