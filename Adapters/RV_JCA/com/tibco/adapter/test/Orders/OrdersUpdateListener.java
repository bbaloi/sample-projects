package com.tibco.adapter.test.Orders;

import com.tibco.sdk.events.MEventListener;
import com.tibco.sdk.events.MDataEvent;
import com.tibco.sdk.events.MEvent;
import com.tibco.sdk.metadata.MInstance;

/**
 * This onEvent method in this class is called when a message is
 * as a reply to the a orders event sent out by the tester.
 */
public class OrdersUpdateListener implements MEventListener {

    private OrdersTesterApp mapp;

    public OrdersUpdateListener(OrdersTesterApp mapp) {
        this.mapp = mapp;
    }

    public synchronized void onEvent(MEvent event) {
        try {
	    MDataEvent dataEvent = (MDataEvent)event;
	    MInstance minst =
		(MInstance)((mapp.getClassRegistry()).getDataFactory()).
		newData(dataEvent.getData());
	    // Expect an orders update here.
	    String name = (String)minst.get("name");
	    Integer id = (Integer)minst.get("id");
	    mapp.getTrace().trace("ADTEST-0045", minst.getTrackingInfo(),  
			       name, id.toString());
        } catch (Exception exp) {
            mapp.getTrace().trace("ADTEST-1010", null, "OrdersUpdateListener", exp);
        }
    }
}

