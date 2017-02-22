package com.extemp.cem.akka.kie;



import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;

import com.extemp.cem.util.KBUtil;

import akka.actor.UntypedActor;

public abstract class UntypedKieSessionActor extends UntypedActor {

    protected KieSession kSession = null;
    
    @Override
    public void preStart() throws Exception {
        super.preStart();
        
        kSession = Kie.KieProvider.get(getContext().system()).getKieContainer().newKieSession("ksession-rules");
        KBUtil.getInstance().setKieSession(kSession);
     
        
        
    }
}
