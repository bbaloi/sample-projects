package com.extemp.cem.akka.kie;

import akka.actor.Extension;


import org.kie.api.KieServices;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.conf.ClockTypeOption;

public class KieExtension implements Extension {

    //Since this Extension is a shared instance
    // per ActorSystem we need to be threadsafe
    private final KieServices ks = KieServices.Factory.get();
    private final KieContainer kContainer = ks.getKieClasspathContainer();  
    
    
    public KieExtension()
    {
    	KieModuleModel kieModuleModel = ks.newKieModuleModel(); 
        
        // Base Model from the module model
        KieBaseModel kieBaseModel = kieModuleModel.newKieBaseModel( "KBase" )
                .setDefault( true )
                .setEqualsBehavior( EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode( EventProcessingOption.STREAM );                

        // Create session model for the Base Model
        KieSessionModel ksessionModel = kieBaseModel.newKieSessionModel( "KSession" )
                .setDefault( true )
                .setType( KieSessionModel.KieSessionType.STATEFUL )
                .setClockType( ClockTypeOption.get("realtime") );  
        
        
        
       
    }
   
    public KieContainer getKieContainer() {
    	    	
    	
    	
        return kContainer;
    }
}
