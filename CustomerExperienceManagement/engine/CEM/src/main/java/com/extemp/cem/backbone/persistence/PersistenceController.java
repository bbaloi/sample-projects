package com.extemp.cem.backbone.persistence;

import com.extemp.cem.backbone.configuration.Settings;
import com.extemp.cem.backbone.configuration.SettingsExtension;
import com.extemp.cem.backbone.core.RuleSession;
import com.extemp.cem.events.BrowseActivityEvent;

import java.sql.*;

import com.extemp.cem.events.MobileEvent;
import com.extemp.cem.events.SportsVenueMobileEvent;
import com.extemp.cem.util.CEMUtil;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.camel.javaapi.UntypedProducerActor;

public class PersistenceController extends UntypedActor {

	 final SettingsExtension settings = Settings.SettingsProvider.get(context().system());
     private String uri="PersistenceController";
     private ActorRef _jdbcClient;
     private ActorRef _fileClient;
     private ActorRef _cacheClient;
     
     private boolean persistenceModel_db,persistenceModel_file,persistenceModel_cache;

  
    public String getEndpointUri() {
        return uri;
    }
    private void init()
    {
    	//System.out.println("----In init---");
    	if(CEMUtil.getInstance().getCEMProperty("cem.persistence.db").equals("true"))
    		persistenceModel_db= true;
    	else
    		persistenceModel_db=false;
    	if(CEMUtil.getInstance().getCEMProperty("cem.persistence.file").equals("true"))
    		persistenceModel_file= true;
    	else
    		persistenceModel_file= false;
    	if(CEMUtil.getInstance().getCEMProperty("cem.persistence.cache").equals("true"))
    		persistenceModel_cache= true;   	
    	else
    		persistenceModel_cache= false;  
    	initDB();
    	initFile();
    	initCache();
    	//System.out.println(persistenceModel_db+","+persistenceModel_file+","+persistenceModel_cache);
    	
    }
    public PersistenceController() {       
    	init();

    }

    public PersistenceController(String uri) {
        this.uri = uri;
        init();
    }

   
    public boolean isOneway() {
        return true;
    }

    
    private void initDB()
    {
    	if(persistenceModel_db)
    	{
    		
    		_jdbcClient = getContext().actorOf(Props.create(JDBCClient.class));
    			
    		
    	}
    }
    private void initFile()
    {
    	if(persistenceModel_file)
    	{
    		_fileClient = getContext().actorOf(Props.create(FileClient.class));
    	}
    }
    private void initCache()
    {
    	if(persistenceModel_cache)
    	{
    		_cacheClient = getContext().actorOf(Props.create(CacheClient.class));
    	}
    }
	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
		if(persistenceModel_db)
	     {
	    	 _jdbcClient.tell(arg0, getSelf());
	     }
	     if(persistenceModel_file)
	     {
	    	 _fileClient.tell(arg0, getSelf());
	     }
	     if(persistenceModel_cache)
	     {
	    	 _cacheClient.tell(arg0, getSelf());
	     }
		
	}

}
