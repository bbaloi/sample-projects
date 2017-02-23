package com.tibco.adapter.test.Orders;

import com.tibco.sdk.rpc.MOperationReplyListener;
import com.tibco.sdk.tools.MTrace;
import com.tibco.sdk.rpc.MClientRequest;
import com.tibco.sdk.rpc.MClientReply;
import com.tibco.sdk.MException;
import com.tibco.sdk.MTimeoutException;

/**
 * Listens for replies to order operations.
 */
public class OrdersReplyListener implements MOperationReplyListener {
    
    private OrdersTesterApp mapp;
    private MTrace trace;
    
    public OrdersReplyListener(OrdersTesterApp mapp) {
	this.mapp = mapp;
	trace = mapp.getTrace();
    }

    public void onReply(MClientRequest req) {
	try {
	    MClientReply rep = req.getReply();
	    String op = (req.getOperationDescription()).getOperationName();
	    if (rep.hasException())
	      trace.trace("ADTESTER-0070", rep.getTrackingInfo(),  "onReply:"+op );
	    else {
	    Object returned = rep.getReturnValue();
	    if (returned instanceof Boolean) 
		trace.trace("ADTESTER-0060", rep.getTrackingInfo(),  
			    "onReply:"+ op + " succeeded: " +
			   ((Boolean)returned).booleanValue());
	    else if (returned instanceof Integer) 
		trace.trace("ADTESTER-0060", rep.getTrackingInfo(), 
			    "onReply:"+op + " succeeded, id: " +
			   ((Integer)returned).intValue());
	    else
		trace.trace("ADTESTER-0060", rep.getTrackingInfo(), 
			    "onReply:"+op + " Return: "+ returned);
	    }
	} catch (MTimeoutException ex) {
	    trace.trace("ADTESTER-1020", req.getTrackingInfo(), ex.getMessage());
	} catch (Exception ex) {
	    trace.trace("ADTESTER-1010", req.getTrackingInfo(), 
			"ReplyListner" + ex.getMessage());
	}
    }
}
