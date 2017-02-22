package com.extemp.cem.util;

import java.io.File;


import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.EntryPoint;


public class KBUtil
{
	private static KBUtil instance=null;
	private KieSession kSession;
	
	public void setKieSession(KieSession pSession)
	{
		kSession=pSession;
	}
	public KieSession getKieSession()
	{
		return kSession;
	}
	
	public static KBUtil getInstance()
	{
		if(instance==null)
				instance= new KBUtil();
		return instance;
	}
	public KieSession getKSession()
	{
		return kSession;
	}
	public EntryPoint getMobileEventEntryPoint(String pSessionName,String pEntryPoint)
	{
		KieServices ks = KieServices.Factory.get();		
  	    KieContainer kContainer = ks.getKieClasspathContainer();
      //	KieSession kSession = kContainer.newKieSession("CEMSportsRulesKS");
  	    if(kSession==null)
  	    	kSession = kContainer.newKieSession(pSessionName);
      	KieBaseConfiguration config = ks.newKieBaseConfiguration();
      	config.setOption( EventProcessingOption.STREAM );
       
      // Listeners
        kSession.addEventListener( new DebugAgendaEventListener() );
        kSession.addEventListener( new DebugRuleRuntimeEventListener() );

        // To setup a file based audit logger, uncomment the next line 
        // KieRuntimeLogger loggerKie = ks.getLoggers().newFileLogger( kSession, "./logger" );
        // KieRuntimeLogger consoleLogger = ks.getLoggers().newConsoleLogger(kSession); 

        // Each Event is Inserted into WorkingMemory through an *EntryPoint*
        //EntryPoint entryPoint = kSession.getEntryPoint( "MobileEventChannel" );
        EntryPoint entryPoint = kSession.getEntryPoint( pEntryPoint );
       
        return entryPoint;
	}
	
    public static KieContainer createKieContainerForProject() 
    {
    	
        KieServices kieServices = KieServices.Factory.get();
        // Create a module model
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
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

        // Create File System services
        KieFileSystem kFileSystem = kieServices.newKieFileSystem();

        File file = new File("/opt/brmsrepo/src/main/resources/rules/Test.drl");
        Resource resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
        kFileSystem.write( resource );       

        KieBuilder kbuilder = kieServices.newKieBuilder( kFileSystem );
        // kieModule is automatically deployed to KieRepository if successfully built.
        kbuilder.buildAll();

        if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
        }
        KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        
        return kContainer;
    }
  

}
