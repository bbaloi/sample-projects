package com.extemp.cem.backbone.core;

import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class SentimentTester extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        
        Timeout timeout = new Timeout(Duration.create(2, "seconds"));
        Future<Object> future = Patterns.ask(getContext().actorOf(Props.create(SentimentClassifier.class)), "my beer is hot and hotdog cold...", timeout);
        String result = (String) Await.result(future, timeout.duration());
        System.out.println("Sentiment - " + result);
    }
}
