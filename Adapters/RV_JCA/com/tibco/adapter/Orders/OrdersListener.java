package com.tibco.adapter.Orders;

import com.tibco.sdk.MTrackingInfo;
import com.tibco.sdk.MException;
import com.tibco.sdk.events.*;
import com.tibco.sdk.metadata.MInstance;

import com.acme.orders.*;
/**
 * This onEvent method in this class is called when a message is
 * received on OrderSubscriber.
 */
public class OrdersListener implements MEventListener {

    private TibOrdersApp mapp;
    private String opName;

    public OrdersListener(TibOrdersApp mapp) {
        this.mapp = mapp;
    }

    public synchronized void onEvent(MEvent event) {
      MTrackingInfo minstTrack = ((MDataEvent)event).getData().getTrackingInfo();
        try {
	    MDataEvent dataEvent = (MDataEvent)event;
	    // Expects an MInstance of class OrdersService.
	    MInstance minst =
		(MInstance)((mapp.getClassRegistry()).getDataFactory()).
		newData(dataEvent.getData());

	    minstTrack = minst.getTrackingInfo();

	    MInstance upd = (MInstance)((mapp.getClassRegistry()).getDataFactory()).newInstance("OrdersServiceUpdate");
	    String name = (String)minst.get("name");
	    opName = name;
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EVENT_RECEIVED, 
				  minstTrack, name+" id="+minst.get("id"));
	    int id = 0;
	    if (name.equals("CreateAccount")) {
		id = (mapp.orders).createAccount(DataMapper.
			      mapAccount((MInstance)minst.get("acct")));
	    } else if (name.equals("ModifyAccount")) {
		(mapp.orders).
		modifyAccount(DataMapper.
			      mapAccount((MInstance)minst.get("acct")));
	    } else if (name.equals("DeleteAccount")) {
		(mapp.orders).
		    deleteAccount(((Integer)minst.get("id")).intValue());
	    } else if (name.equals("CreateOrder")) {
		id = (mapp.orders).
		createOrder(DataMapper.
			    mapOrder((MInstance)minst.get("order")));
	    } else if (name.equals("ModifyOrder")) {
		(mapp.orders).
		modifyOrder(DataMapper.
			    mapOrder((MInstance)minst.get("order")));
	    } else if (name.equals("DeleteOrder")) {
		(mapp.orders).
		    deleteOrder(((Integer)minst.get("id")).intValue());
	    }

	    if (id == 0)
	      id = ((Integer)minst.get("id")).intValue();

	    upd.set("name", name);
	    upd.set("id", new Integer(id));

	    minstTrack.addApplicationInfo("OrdersAdapter.pubUpdate."+name);
	    upd.setTrackingInfo(minstTrack);

	    (mapp.orderPublisher).send(upd.serialize());

	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EVENT_UPDATE, 
				  minstTrack, upd );
        } catch (MException exp) {
            (mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, minstTrack, 
				"-Orders Listener-", exp);
        } catch (OrdersException ex) {
	  try { // Exception in Orders listener operation
	  (mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, 
			      minstTrack, "-Orders Listener-", 
			      ex.getMessage());
	    MInstance exInst =((mapp.getClassRegistry()).getDataFactory()).
		newInstance("OrdersExceptionClass");
	    exInst.set("exceptionName", opName);
	    exInst.set("exceptionMessage", ex.getMessage());
	    minstTrack.addApplicationInfo("OrdersAdapter.pubException."+opName);
	    exInst.setTrackingInfo(minstTrack);	    
	    (mapp.orderPublisher).send(exInst.serialize());
	  } catch (MException exp) {
            (mapp.mtrace).trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				"-OdersException-", exp);
        }
	}
	
    }
}

