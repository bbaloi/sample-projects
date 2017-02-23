
// Copyright 2002 - TIBCO Software Inc.
// ALL RIGHTS RESERVED
//
//


/*
 * Description:
 *
 * This is the hawk monitored object.
 *
 */
package com.tibco.adapter.Orders;

import java.util.*;
import com.tibco.sdk.*;

// My hawk agent stores the current Greeting value
// that can be changed through the hawk display
// The hawk method implementation are done through reflection mechanism of Java
// So the method name and parameters must match the definition in repository
//

import com.tibco.sdk.metadata.*;
import com.tibco.sdk.tools.*;

import com.tibco.sdk.tools.MTrace;
import com.tibco.sdk.events.*;

// Implements greetingAgent
public class myHawkAgent 
{
    TibOrdersApp app;
    // Our default greeting message, save to file to persist across runtime instance
    // rather than using "static String".
    static String currStatus="Connected";
        
  public myHawkAgent(TibOrdersApp app)
    {
      this.app = app;
        	  
    }
     
    // Hawk method greetingAgent::setConnectionStatus() ** not defined 
    // We could also declare this as 
    // or public String setConnectionStatus( MTree input) 
    public String setConnectionStatus(String newStatus)
     {
	try{
	    currStatus = newStatus;

	    //If implemented as one single MTree in parameter use following
	    //currStatus = (String ) input.getValue("newStatus");

	}
	catch(Exception e) {
	  app.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				"HMA.setConnectionState", e);	    
	}
	return currStatus;

    }

    // Hawk method greetingAgent::getConnectionStatus()
   public String getConnectionStatus()
    {
      return currStatus;
    }

    // Hawk method greetingAgent::getAllStatuss()
    // Returns a table output
    public MTree getOrdersTable()
    {
	// debug trace 
        app.getTrace().debug("getAllOrders method invoked", 
			     (MTrackingInfo)null);
	Hashtable orders = app.orders.getAllOrders();
	// debug trace
	app.getTrace().debug("orders="+orders, (MTrackingInfo)null);

	// MTree to represent the serialized table.
        MTree t = new MTree( "reply" ); // "reply" is a arbitrary dummy name
	try {
	  int i = 0;
	  if (orders.size() == 0) {
	      MTree t2 = new MTree( "row"+i ); // "row+i" is also dummny name
		// Index column of unique id, doesn't have to be int
 		t2.append( "OrderNumber", new Integer(i));
		// Column Status
                t2.append( "AccountNumber", new Integer(i)  );
                t2.append( "OrderInfo", "No Orders"  );
                t.append( t2 );
	  } else {
	    Enumeration enum = orders.keys();
	    while (enum.hasMoreElements()) {
	      i++;
	      Integer key = (Integer)enum.nextElement();
	      MInstance order = (MInstance)orders.get(key);
	      // MTree of individual row, 
	      MTree t2 = new MTree( "row"+i ); // "row+i" is also dummny name
		// Index column of unique id, doesn't have to be int
 		t2.append( "OrderNumber", order.get("id"));
		// Column Status
                t2.append( "AccountNumber", order.get("acctId")  );
                t2.append( "OrderInfo", order.get("orderItem")  );
                t.append( t2 );
	    }
	  }
	} catch (MException ex) {
	  app.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				"HMA.getOrdersTable", ex);
	  ex.printStackTrace();
	}
	// debug trace
        app.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_HAWK_METHOD, null,
			     "return value from getOrdersTable method:"+t);
        return t;
	
    }
} // end of class myHawkAgent
