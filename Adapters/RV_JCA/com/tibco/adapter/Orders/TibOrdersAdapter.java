package com.tibco.adapter.Orders;

//Tibco SDK imports
import com.tibco.sdk.*;
import com.tibco.sdk.MAppProperties;
import com.tibco.sdk.MException;

/**
 * This file has the main class for the Orders Adapter.
 */
public class TibOrdersAdapter {

    TibOrdersApp mapp;

    public TibOrdersAdapter(String[] args) throws Exception {
	// Inialize MAppProperties
        MAppProperties prop = null;
        String app_name = "TibOrdersAdapter";
        String app_version = "1.0.4";
        String app_info = "TIBCO Orders Adapter";
        prop = new MAppProperties(app_name,
                                  app_version,
                                  app_info,
				  null, // config URL on command line
                                  args);

        MMessageBundle.addResourceBundle(app_name, "ORDERS_AeErrors_en_US");
        // This creates the MApp derived object for this adapter.
        mapp = new TibOrdersApp(prop);

	MHostInfo hostInfo = new MHostInfo(mapp);
	//** set hostInfo service state
	hostInfo.setAppState(MUserApplicationState.INITIALIZING);


	// Start the adapter. This calls initialize on the mapp.
	mapp.start(); // new thread started
        // Back to main thread
	// Instantiate all operation implementations.
	// ** Adapter Server service 
	MAdapterServiceInfo appInfo = new MAdapterServiceInfo();

	new OperationImpl(mapp, "OrderOperations", "CreateAccount", "Server");
	new OperationImpl(mapp, "OrderOperations", "ModifyAccount", "Server");
	new OperationImpl(mapp, "OrderOperations", "DeleteAccount", "Server");
	new OperationImpl(mapp, "OrderOperations", "CreateOrder", "Server");
	new OperationImpl(mapp, "OrderOperations", "ModifyOrder", "Server");
	new OperationImpl(mapp, "OrderOperations", "DeleteOrder", "Server");

	appInfo.set("BillingPublish", "Server", "OrdersOperations");

	(mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_STARTED, null, "");
	// Info app started
    }

    public static void main(String[] args) throws Exception {
      try {
        TibOrdersAdapter adapter = new TibOrdersAdapter(args);
      } // catch anything that didn't get caught
        catch( Exception fatal ) { fatal.printStackTrace(); }

    }

  public void shutdown() {
    try {
      if (mapp != null){
	MHostInfo hostInfo = new MHostInfo(mapp);
	hostInfo.setAppState(MUserApplicationState.STOPPED);
	mapp.setHostInfo(hostInfo);
	mapp.stop();
      }
      else
	System.exit(1);
    } catch (Exception ex) { ex.printStackTrace(); }

  }
}
