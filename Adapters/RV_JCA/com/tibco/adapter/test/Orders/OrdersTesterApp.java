package com.tibco.adapter.test.Orders;

import java.util.*;
import java.io.IOException;

import com.tibco.sdk.*;
import com.tibco.sdk.events.*;
import com.tibco.sdk.tools.MTrace;
import com.tibco.sdk.metadata.*;
import com.tibco.sdk.rpc.MClientRequest;
import com.tibco.sdk.rpc.MClientReply;

/**
 * This class tests the orders adapter.
 * All interactions are initiated from the tester, however,
 * the tester may receive billing events as a result of some
 * operation.
 *
 * <p>The class needs a timer event to start querying the user
 * for which messages to send.
 */
public class OrdersTesterApp extends MApp {

    public MTrace mTrace;
    private MTimer timer;
    private MEventListener timerListener;

    private boolean pubsub = false; // Whether to use pub/sub or
    // request reply to invoke the adapter

    private boolean synchInvoke = true; // If pubsub is false, use
    // synchronous request/reply if synchInvoke is true and use
    // asynchronous request/reply if synchInvoke is false.

    public MPublisher orderPublisher;
    public MSubscriber orderSubscriber;
    public MSubscriber billingSubscriber;
    public BillingListener billingListener;
    public OrdersUpdateListener orderListener;
    public Random ran;
  public String orderName[] = {"TV", "VCR", "DVD", "STEREO", "LAPTOP", "DESKTOP", "MONITOR", "DISK", "LCD", "PHONE", "PVR"};
  public String accName[] = {"NY", "CA", "MN", "WA", "TX", "LA", "FL", "GA", "SC", "NC", "OR"};
    

    public OrdersTesterApp(MAppProperties p) {
	super(p);
	ran = new Random();
	
    }

    public void onInitialization() {
	mTrace = getTrace();

	mTrace.trace("ADTESTER-2010", null, "");
	
	//** Create Host information
	  MHostInfo hostInfo = new MHostInfo(this);//
	  hostInfo.setAppState(MUserApplicationState.RUNNING);
	  hostInfo.setExtendedInfo("Orders", "tester");
	  setHostInfo(hostInfo);
	  
	  MAdapterServiceInfo appInfo = new MAdapterServiceInfo();

	// get the registry so we can retrieve the sub/pub objects
	MComponentRegistry reg = getComponentRegistry();
	orderPublisher = reg.getPublisher("OrderPublisher");

	Enumeration classEnum = orderPublisher.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("BillingPublish", orderPublisher.getName(), schemaname);
	}
	orderSubscriber = reg.getSubscriber("OrderSubscriber");
	orderListener = new OrdersUpdateListener(this);
	orderSubscriber.addListener(orderListener);

	classEnum = orderSubscriber.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("BillingPublish", orderSubscriber.getName(), schemaname);
	}

	billingSubscriber = reg.getSubscriber("BillingSubscriber");
	billingListener = new BillingListener(this);
	billingSubscriber.addListener(billingListener);

	classEnum = billingSubscriber.getClassNames();
	//** Retrieve all class schema specified for this endpoint
	while ( classEnum.hasMoreElements() ){
	  String schemaname = (String) classEnum.nextElement();
	  appInfo.set("BillingPublish", 
		      billingSubscriber.getName(), schemaname);
	}

	timer = reg.getTimer("OrdersTesterTimer");
	timerListener = new TimerListener(this);
	timer.addListener(timerListener);

	//** Set adapter service info
	appInfo.set("OrderClient", "Client", "OrderOperations");
	setAdapterServiceInfo(appInfo);

	mTrace.trace("ADTESTER-2020", null, ""); // Debug finish init
	mTrace.trace("ADTESTER-0010", null, ""); // Info Adapter started
    }

    public void onTermination() {
	mTrace.trace("ADTESTER-0011", null, "");
    }

    private Integer readInt(int base) {
      return new Integer(ran.nextInt(10)+base);
    }

    private MInstance makeAcct()
	throws MConstructionException, MException {
	MInstance acct =
	(getClassRegistry().getDataFactory()).newInstance(
	     "AccountInfo");
	
	acct.set("id", readInt(1));
	acct.set("name", accName[readInt(1).intValue()]);
	acct.set("otherInfo", "otherAcct");
	return acct;
    }
	
    private MInstance makeOrder()
	throws MConstructionException, MException{
	MInstance ord =
	(getClassRegistry().getDataFactory()).newInstance(
	     "OrderInfo");

	ord.set("id", readInt(1));

	ord.set("acctId", readInt(100));

	ord.set("orderItem", orderName[readInt(1).intValue()]);
	ord.set("otherInfo", "otherOrder");

	return ord;
    }
    
    private void sendCreateAccount() 
	throws MConstructionException, MException {
      MTrackingInfo track = new MTrackingInfo();
      track.addApplicationInfo("ordersTest.create.account");
	if (pubsub) {
	    MInstance msg =
		(getClassRegistry().getDataFactory()).newInstance(
					  "OrdersService");
	    msg.set("name", "CreateAccount");
	    msg.set("acct", makeAcct());
	    msg.setTrackingInfo(track);
	    orderPublisher.send(msg.serialize());
	    mTrace.trace("ADTESTER-0050", track, "Sent message...", msg.get("acct"));
	    return;
	}
	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "CreateAccount", // operation name
			       "Client"); // client name
	req.set("acct", makeAcct());
	req.setTrackingInfo(track);
	mTrace.trace("ADTESTER-0030", track, "Create", req.get("acct"));
	if (synchInvoke) {
	  try {
	    req.syncInvoke(5000); // 5 sec timeout
	    
	    MClientReply rep = req.getReply();
	    
	    Integer id = (Integer)rep.getReturnValue();
	    if (rep.hasException())
	      mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), "Create Account!");
	    else
	mTrace.trace("ADTESTER-0050", rep.getTrackingInfo(), "Account", req.get("acct"));
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
	      mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	    }

	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); //
	}
    }

    private void sendModifyAccount()
	throws MConstructionException, MException{

       MTrackingInfo track = new MTrackingInfo();
       track.addApplicationInfo("ordersTest.modify.account");


	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "ModifyAccount", // operation name
			       "Client"); // client name

	req.set("acct", makeAcct());
	req.setTrackingInfo(track);

	mTrace.trace("ADTESTER-0030", track, "Modify", req.get("acct"));
	if (synchInvoke) {
	    try {
	    req.syncInvoke(5000); // 5 sec timeout
	    MClientReply rep = req.getReply();
	    
	    if (!rep.hasException()) 
		mTrace.trace("ADTESTER-0060", rep.getTrackingInfo(), "Account modified");
	    else {
		mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), "Account not modify!");
	    }
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
	      mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	      // else
	      //throw ex;
	    }

	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); // no timeout
	}
    }

    private void sendDeleteAccount()
	throws MConstructionException, MException{

       MTrackingInfo track = new MTrackingInfo();
       track.addApplicationInfo("ordersTest.modify.account");

	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "DeleteAccount", // operation name
			       "Client"); // client name

	req.set("id", readInt(1));
	req.setTrackingInfo(track);

	mTrace.trace("ADTESTER-0030", track, "Delete", req.get("id"));
	if (synchInvoke) {
	    try {
	    req.syncInvoke(5000); // 5 sec timeout
	    MClientReply rep = req.getReply();
	    if (!rep.hasException())
		mTrace.trace("ADTESTER-0060", rep.getTrackingInfo(), "Account deleted");
	    else
		mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), "Account delete");
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
		mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	    }

	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); // 5 sec timeout
	}
    }

    private void sendCreateOrder()
	throws MConstructionException, MException {

       MTrackingInfo track = new MTrackingInfo();
       track.addApplicationInfo("ordersTest.modify.account");

	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "CreateOrder", // operation name
			       "Client"); // client name
	req.set("order", makeOrder());
	req.setTrackingInfo(track);
	
	mTrace.trace("ADTESTER-0040", track, "Create", req.get("order"));
	if (synchInvoke) {
	    try {
	    req.syncInvoke(5000); // 5 sec timeout
	    MClientReply rep = req.getReply();
	    Integer id = (Integer)rep.getReturnValue();
    	    if (rep.hasException())
	      mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), 
			  "Order"+id+" not Created!");
	    else
		mTrace.trace("ADTESTER-0060", rep.getTrackingInfo(), "Order Created");
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
	      mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	    }
	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); // 5 sec timeout
	}
    }

    private void sendModifyOrder()
	throws MConstructionException, MException{

       MTrackingInfo track = new MTrackingInfo();
       track.addApplicationInfo("ordersTest.modify.account");

	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "ModifyOrder", // operation name
			       "Client"); // client name
	req.set("order", makeOrder());
	req.setTrackingInfo(track);

	mTrace.trace("ADTESTER-0040", track, "Modify", req.get("order"));
	if (synchInvoke) {
	    try {
	      req.syncInvoke(5000); // 5 sec timeout
	      MClientReply rep = req.getReply();
	   
	    if (!rep.hasException())
	      //if (success)
		mTrace.trace("ADTESTER-0060", rep.getTrackingInfo(), "Order modified");
	    else 
	      mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), "modify failed!!!!");
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
	      mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	    }

	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); // 5 sec timeout
	}
    }

    private void sendDeleteOrder()
	throws MConstructionException, MException {

       MTrackingInfo track = new MTrackingInfo();
       track.addApplicationInfo("ordersTest.modify.account");

	MClientRequest req =
	    new MClientRequest(this,
			       "OrderOperations", // class name
			       "DeleteOrder", // operation name
			       "Client"); // client name
	req.set("id", readInt(1));
	req.setTrackingInfo(track);
	mTrace.trace("ADTESTER-0040", track, "Delete", req.get("id"));
	if (synchInvoke) {
	    try {
	    req.syncInvoke(5000); // 5 sec timeout
	    MClientReply rep = req.getReply();
	    if (!rep.hasException()) //if (success)
		mTrace.trace("ADTESTER-0060", rep.getTrackingInfo(), "Order delete");
	    else
		mTrace.trace("ADTESTER-0070", rep.getTrackingInfo(), "Order deleted");
	    } catch (MException ex) {
	      if (ex instanceof MTimeoutException)
	      mTrace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	    }

	} else {
	    req.asyncInvoke(new OrdersReplyListener(this), 5000); // 5 sec timeout
	}
    }

    public void doTest() {

      try {
	int method=3;
	// Choose invocation method
	method = ran.nextInt(3)+1; 

	  // Choose operation 
	int op = 6;
	op = ran.nextInt(20)+1; // 8-20 are no-op, creates random interval
	if (op < 7) {
	  System.out.println("event: " + op);

	  switch(method) {
	    case 0:
	    case 1:
		pubsub = false;
		synchInvoke = true;
		mTrace.trace("ADTESTER-0020", null, "Synch Invoke mode");
		break;
	    case 2:
		pubsub = false;
		synchInvoke = false;
		mTrace.trace("ADTESTER-0020", null, "Asynch Invoke mode");
		break;
	    case 3:
	        pubsub = true;
	        mTrace.trace("ADTESTER-0020", null, "Publish/Subscribe mode");
	        break;
	    default:
	        break;
	  } // switch method

	switch (op) {
		case 1:
		    sendCreateAccount();
		    break;
		case 2:
		    sendModifyAccount();
		    break;
		case 3:
		    sendDeleteAccount();
		    break;
		case 4:
		    sendCreateOrder();
		    break;
		case 5:
		    sendModifyOrder();
		    break;
		case 6:
		    sendDeleteOrder();
		    break;
		default:		   
		    break;
	  } // switch op
	}// endif
	    
      } catch (MException e) {
		mTrace.trace("ADTESTER-1010", null, "doTest()", e.getMessage());
	    }
	}

  public void shutdown() {
    try {
	MHostInfo hostInfo = new MHostInfo(this);
	hostInfo.setAppState(MUserApplicationState.STOPPED);
	setHostInfo(hostInfo);
	stop();
    } catch (Exception ex) { ex.printStackTrace(); }

  }

    public static void main(String args[]) throws Exception {
	MAppProperties prop = null;
        String app_name = "OrdersTester";
        String app_version = "1.0";
        String app_info = "Orders Tester";
	MAppProperties p =
	    new MAppProperties(app_name,
			       app_version,
			       app_info,
			       null, // config URL on command line
			       args);
	OrdersTesterApp mapp = new OrdersTesterApp(p);

	MMessageBundle.addResourceBundle(app_name, "OrdersTest");

	MHostInfo hostInfo = new MHostInfo(mapp);
	//** set hostInfo service state
	hostInfo.setAppState(MUserApplicationState.INITIALIZING);


	mapp.start();

    }

}
