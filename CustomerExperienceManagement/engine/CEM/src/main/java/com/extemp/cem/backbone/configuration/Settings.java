package com.extemp.cem.backbone.configuration;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtensionIdProvider;
import akka.actor.ExtendedActorSystem;

public class Settings extends AbstractExtensionId<SettingsExtension> implements ExtensionIdProvider {

    public final static Settings SettingsProvider = new Settings();

    private Settings() {}

    public Settings lookup() {
	return Settings.SettingsProvider;
    }

    public SettingsExtension createExtension(ExtendedActorSystem system) {
	return new SettingsExtension(system.settings().config());
    }
}