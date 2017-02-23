package com.tibco.adapter.test.Orders;

import com.tibco.sdk.events.MEventListener;
import com.tibco.sdk.events.MDataEvent;
import com.tibco.sdk.events.MEvent;
import com.tibco.sdk.metadata.MInstance;

/**
 * This onEvent method in this class is called when a message is
 * received on BillingSubscriber.
 */
public class BillingListener implements MEventListener {

    private OrdersTesterApp mapp;

    public BillingListener(OrdersTesterApp mapp) {
        this.mapp = mapp;
    }

    public synchronized void onEvent(MEvent event) {
        try {
	    MDataEvent dataEvent = (MDataEvent)event;
	    // Expects an MInstance of class BillingService.
	    MInstance minst =
		(MInstance)((mapp.getClassRegistry()).getDataFactory()).
		newData(dataEvent.getData());
	    String name = (String)minst.get("name");
	    mapp.getTrace().trace("ADTESTER-0080", minst.getTrackingInfo(), name);
        } catch (Exception exp) {
            mapp.getTrace().trace("ADTESTER-1010", null, "BillingListener", 
			       exp.getMessage());
        }
    }
}

