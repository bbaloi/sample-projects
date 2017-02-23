package com.tibco.adapter.test.Orders;

import com.tibco.sdk.events.MEventListener;
import com.tibco.sdk.events.MEvent;
import com.tibco.sdk.events.MTimerEvent;
import com.tibco.sdk.events.MExceptionEvent;
import com.tibco.sdk.MException;

/**
 * This timer is needed only to start the tests.
 * The timer only "fires" once.
 */
public class TimerListener implements MEventListener {

    private OrdersTesterApp mapp;

    public TimerListener(OrdersTesterApp mapp) {
	this.mapp = mapp;
    }

    public void onEvent(MEvent ev) {
	if (ev instanceof MExceptionEvent) {

	    MException ex = ((MExceptionEvent)ev).getException();
	    (mapp.getTrace()).trace("ADTESTER-1030", 
				    ((MExceptionEvent)ev).getData().getTrackingInfo(), 
				    ex.getMessage());

	} else if (ev instanceof MTimerEvent) {
	    mapp.doTest();
	} else {
	  (mapp.getTrace()).trace("ADTESTER-1040", null, 
				    (ev.getClass()).getName());
	}	    
	    
    }
}
