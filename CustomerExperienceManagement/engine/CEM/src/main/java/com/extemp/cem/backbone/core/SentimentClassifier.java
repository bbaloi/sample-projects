package com.extemp.cem.backbone.core;

import java.io.File;


import akka.actor.UntypedActor;

import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;
import com.extemp.cem.backbone.configuration.Settings;
import com.extemp.cem.backbone.configuration.SettingsExtension;

public class SentimentClassifier extends UntypedActor {

    final SettingsExtension settings = Settings.SettingsProvider.get(context().system());

    @SuppressWarnings("rawtypes")
    LMClassifier lmClassifier;  

    @Override
    @SuppressWarnings("rawtypes")
    public void preStart() throws Exception {
        lmClassifier = (LMClassifier) AbstractExternalizable.readObject(new File(settings.SENTIMENT_CLASSIFIER));  
    }

    @Override
    public void onReceive(Object message) throws Exception {

        try {
            getSender().tell(lmClassifier.classify((String)message).bestCategory(), getSelf());
        } catch (Exception e) {
            getSender().tell(new akka.actor.Status.Failure(e), getSelf());
            throw e;
        }
    }
}
