// Copyright 2000 - TIBCO Software Inc.
// ALL RIGHTS RESERVED
//
//

/*
 * Description:
 *
 * Event Handler function that will print out the data received from
 * the subscriber.
 */
package com.tibco.syslog.adapter;

import java.util.*;

import org.apache.log4j.Logger;

import com.tibco.sdk.*;
import com.tibco.sdk.events.*;

public class SyslogAdvisoryListener extends MAdvisoryListener
{
    MApp    app;
    
    private static final Logger logger = Logger.getLogger( "com.tibco.syslog.adapter.SyslogAdvisoryListener");
	
	
    public SyslogAdvisoryListener(MApp app)
    {
        super(app);
        this.app = app;        
    }

    public synchronized void onEvent(MEvent event)
    {
        System.out.println();
        System.out.println("Advisory received.");

        MAdvisoryEvent advisoryEvent = (MAdvisoryEvent)event;
        
        try {
            MTree data = (MTree)advisoryEvent.getData();
            
            app.getTrace().trace("Syslog-20000", null, "Role: " +
                       advisoryEvent.getRoleName(),null);
            app.getTrace().trace("Syslog-20000", null, "Subject: " + 
                       advisoryEvent.getSubject(),null);
            app.getTrace().trace("Syslog-20000", null, "Data:"+data.toString(),null);
                        
        } catch( Exception ex ) { 
            app.getTrace().trace("Syslog-00999", ex, null);
            ex.printStackTrace(); 
        }
    }
}

