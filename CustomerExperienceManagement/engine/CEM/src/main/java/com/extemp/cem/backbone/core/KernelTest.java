package com.extemp.cem.backbone.core;

import org.apache.activemq.camel.component.ActiveMQComponent;

import org.apache.camel.CamelContext;

import akka.kernel.Bootable;

import com.extemp.cem.actions.SendNotificationAction;
import com.extemp.cem.actions.SentimentDetectionAction;
import com.extemp.cem.akka.kie.Kie;
import com.extemp.cem.akka.kie.KieExtension;
import com.extemp.cem.backbone.persistence.PersistenceController;
import com.extemp.cem.util.CEMUtil;

import akka.routing.RoundRobinPool;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;

public class KernelTest implements Bootable {

    final static ActorSystem system = ActorSystem.create("core");
    ActiveMQComponent activemqComponent = null;

    public void startup() {

        //activemqComponent = ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
        activemqComponent = ActiveMQComponent.activeMQComponent(CEMUtil.getInstance().getCEMProperty("jms.url"));
        activemqComponent.setUserName("admin");
        activemqComponent.setPassword("admin");
        activemqComponent.setDeliveryPersistent(false);

        Camel camel = CamelExtension.get(system);
        CamelContext camelContext = camel.context();
        camelContext.addComponent("activemq", activemqComponent);
        
        //ActorRef workRouter = system.actorOf(new RoundRobinPool(3).props(Props.create(JMSConsumer.class)), "workRouter");
        system.actorOf(Props.create(JMSConsumer.class), "JMSConsumer");
        //system.actorOf(Props.create(PersistenceController.class), "PersistenceController");
        system.actorOf(Props.create(JMSConsumer_Approval.class), "JMSConsumer_Approval");
        system.actorOf(Props.create(JMSConsumer_TicketCheck.class), "JMSConsumer_TicketCheck");
        system.actorOf(Props.create(JMSConsumer_Vote.class), "JMSConsumer_Vote");
        system.actorOf(Props.create(JMSConsumer_Auction.class), "JMSConsumer_Auction");
        system.actorOf(Props.create(JMSConsumer_FBPurchase.class), "JMSConsumer_FBPurchase");
        system.actorOf(Props.create(JMSConsumer_Tweet.class), "JMSConsumer_Tweet");
        system.actorOf(Props.create(JMSConsumer_EnterpriseEvent.class), "JMSConsumer_EnterpriseEvent");
        system.actorOf(Props.create(JMSConsumer_BrowseActivity.class), "JMSConsumer_BrowseActivity");
        //system.actorOf(Props.create(JMSProducer.class), "JMSProducer");
        system.actorOf(Props.create(SendNotificationAction.class), "NotificationAction");
        //system.actorOf(Props.create(SentimentDetectionAction.class), "SentimentDetectionAction");
        system.actorOf(Props.create(RuleSession.class,"KieRuleSession"));
        
        CEMUtil.getInstance().setAkkaSystem(system);
      
       
    }

    public void shutdown() {
        system.shutdown();
    }
    
    public static ActorSystem getActorSystem()
    {
    	return system;
    }
}
