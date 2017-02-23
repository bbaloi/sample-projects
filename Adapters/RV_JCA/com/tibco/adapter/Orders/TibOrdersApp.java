package com.tibco.adapter.Orders;

import java.util.*;

import com.tibco.sdk.*;
import com.tibco.sdk.hawk.*;
import com.tibco.sdk.events.*;
import com.tibco.sdk.metadata.MInstance;
import com.tibco.sdk.tools.*;

import com.acme.orders.*;

/**
 * This is the extension of the MApp class. This is where the
 * event manager gets started and where you hook up event managers
 * to the listeners and setup the timers (if any).
 */
public class TibOrdersApp extends MApp {

    // For request and reply interactions
    public AdapterBillingListener listener; // Listens to billing events
    // from the orders application
    public OrdersApp orders;

    public MPublisher orderPublisher;
    public MPublisher billingPublisher;
    public MSubscriber orderSubscriber;
    public OrdersListener orderListener;

    public MTrace mtrace;

    public TibOrdersApp(MAppProperties prop) {
        super(prop);
    }

    /**
     * Hook to perform application-specific behavior during initialization.
     * This method is called automatically when the class is started.
     */
    protected void onInitialization() throws MException {
	mtrace = getTrace();

        mtrace.trace(ORDERS_AeErrors_en_US.ADORDERS_DEBUG_INITIALIZING, 
		     null, ""); // Debug trace in init
	//** Create Host information
	  MHostInfo hostInfo = new MHostInfo(this);//getHostInfo();
	  hostInfo.setAppState(MUserApplicationState.RUNNING);
	  setHostInfo(hostInfo);

	// Initialize publishers and subscribers.
	MComponentRegistry reg = getComponentRegistry();
	
	MAdapterServiceInfo appInfo = new MAdapterServiceInfo();
	
	orderPublisher = reg.getPublisher("OrderPublisher");
	Enumeration classEnum = orderPublisher.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("OrderPublish", orderPublisher.getName(), schemaname);
	}


	billingPublisher = reg.getPublisher("BillingPublisher");
	classEnum = billingPublisher.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("BillingPublish", billingPublisher.getName(), schemaname);
	}

	orderSubscriber = reg.getSubscriber("OrderSubscriber");
	classEnum = orderSubscriber.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("OrderSubscriber", orderSubscriber.getName(), schemaname);
	}

	mtrace.trace(ORDERS_AeErrors_en_US.ADORDERS_SUBJECT_USED, 
		     null, orderSubscriber.getSubjectName());
	orderListener = new OrdersListener(this);
	orderSubscriber.addListener(orderListener);
	orders = new OrdersApp(new AdapterBillingListener(this));
	
	// aesend stop message
	MSubscriber stopSub = reg.getSubscriber("StopSubscriber1");
	stopSub.addListener(new StopListener(this));

	//** Set adapter service info
	setAdapterServiceInfo(appInfo);

	MHawkRegistry hr = getHawkRegistry(); 

	// create instance of class which contains the 
	// implmentation of our hawkMehod

	Object mobj = new myHawkAgent(this); 
	MHawkMicroAgent hma = hr.getHawkMicroAgent("OrdersAgent");
	if(null == hma)
	  mtrace.trace(ORDERS_AeErrors_en_US.ADORDERS_NO_MICROAGENT, 
		       null, "OrdersAgent");
	else
	  hma.setMonitoredObject( mobj );

	//** Add some extra information about the Orders Listener
	hostInfo.setExtendedInfo("OrdersListener", "AdapterBillingListener");
	hostInfo.setExtendedInfo("ACME", orders.getVersion());
	setHostInfo(hostInfo);

        mtrace.trace(ORDERS_AeErrors_en_US.ADORDERS_DEBUG_INITIALIZED, 
		     null, ""); // Debug trace finish init
    }

    /**
     *  Hook to perform application-specific behavior during shut-down
     *  of the Adapter
     */
    protected void onTermination() throws MException {
        mtrace.trace(ORDERS_AeErrors_en_US.ADORDERS_STOPPED, null, "");
	MHostInfo hostInfo = getHostInfo();
	hostInfo.setAppState(MUserApplicationState.STOPPING);
	setHostInfo(hostInfo);

    }
}
