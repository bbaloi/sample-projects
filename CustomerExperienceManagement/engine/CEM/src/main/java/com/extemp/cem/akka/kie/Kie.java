package com.extemp.cem.akka.kie;

import akka.actor.*;

public class Kie extends AbstractExtensionId<KieExtension> implements ExtensionIdProvider {

    public final static Kie KieProvider = new Kie();

    private Kie() {}

    public KieExtension createExtension(ExtendedActorSystem extActorSystem) {
        return new KieExtension();
    }

    public ExtensionId<? extends Extension> lookup() {
        return Kie.KieProvider;
    }

}
