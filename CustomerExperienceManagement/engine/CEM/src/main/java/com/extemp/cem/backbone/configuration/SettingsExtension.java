package com.extemp.cem.backbone.configuration;

import akka.actor.Extension;
import com.typesafe.config.Config;

public class SettingsExtension implements Extension {

    /*public final String DB_URI;

    public SettingsExtension(Config config) {

	DB_URI = config.getString("myapp.db.uri");
    }
    */
    public final String SENTIMENT_CLASSIFIER;

    public SettingsExtension(Config config) {

        SENTIMENT_CLASSIFIER = config.getString("cem.classifiers.sentiment");
    }

}