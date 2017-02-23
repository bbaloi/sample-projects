package com.tibco.adapter.Orders;

import com.tibco.sdk.*;
import com.tibco.sdk.MException;
import com.tibco.sdk.events.*;
import com.tibco.sdk.metadata.MInstance;

import com.acme.orders.*;
/**
 * This onEvent method in this class is called when a message is
 * received on OrderSubscriber.
 */
public class StopListener implements MEventListener {

    private TibOrdersApp mapp;
    private String opName;

    public StopListener(TibOrdersApp mapp) {
        this.mapp = mapp;
    }

    public synchronized void onEvent(MEvent event) {
      MTrackingInfo minstTrack = ((MDataEvent)event).getData().getTrackingInfo();
        try {
	    MDataEvent dataEvent = (MDataEvent)event;
	    MTree msg = dataEvent.getData();
	    String val = (String) msg.getValue("data");
	    (mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_STOP_RECEIVED, 
				minstTrack, val);
	    if (val.equals("now"))
	      mapp.stop();
	    else {
	      Integer timeout = new Integer(val); //(Integer)msg.getValue("data");
	      Thread.sleep(timeout.intValue());
	      mapp.stop();
	    }


        } catch (Exception exp) {
            (mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, 
				minstTrack, "-Stop Listener-", exp);
        }
	
    }
}

