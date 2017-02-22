package com.extemp.cem.backbone.persistence;

import com.extemp.cem.events.BrowseActivityEvent;

import java.sql.*;

import com.extemp.cem.events.MobileEvent;
import com.extemp.cem.events.SportsVenueMobileEvent;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.camel.javaapi.UntypedProducerActor;

public class FileClient extends UntypedActor {

    private String uri="jdbc:cem_db[?options]";

    
    public String getEndpointUri() {
        return uri;
    }

    public FileClient() {
        this.uri = uri;
    }

    public FileClient(String uri) {
        this.uri = "jdbc:cem_db[?options]";
    }

   
    public boolean isOneway() {
        return true;
    }

    

	@Override
	public void onReceive(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
