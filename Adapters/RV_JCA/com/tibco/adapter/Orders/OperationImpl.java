package com.tibco.adapter.Orders;

import com.tibco.sdk.*;
import com.tibco.sdk.rpc.*;
import com.tibco.sdk.tools.MTrace;
import com.tibco.sdk.MException;
import com.tibco.sdk.metadata.MInstance;

import com.acme.orders.*;

/**
 * This class implements all account and order operations.
 */
public class OperationImpl extends MOperationImpl {

    TibOrdersApp mapp;
    MTrace trace;
    String serverName;
    String className;
    String operationName;

    public OperationImpl(TibOrdersApp mapp,
			 String className,
			 String operationName,
			 String serverName) throws Exception {
	super(mapp, className, operationName, serverName);
	this.className = className;
	this.operationName = operationName;
	this.serverName = serverName;
	this.mapp = mapp;
	trace = mapp.getTrace();
    }

    public void onInvoke(MServerRequest req,
			 MServerReply rep) {
      MTrackingInfo reqTrack = req.getTrackingInfo();
	try {
	  
	    if (operationName.equals("CreateAccount")) {
	      AccountInfo info =
		    DataMapper.mapAccount((MInstance)req.get("acct"));
  	        trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ACCOUNT_OP, 
			    reqTrack, operationName, 
			    new Integer(info.id).toString());

		int id = (mapp.orders).createAccount(info);

		rep.setReturnValue(new Integer(id));
	    } else if (operationName.equals("ModifyAccount")) {
	      MInstance acc = (MInstance)req.get("acct");

	      trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ACCOUNT_OP, reqTrack, 
			  operationName, (Integer)acc.get("id"));

	      AccountInfo info =  DataMapper.mapAccount(acc);
	      (mapp.orders).modifyAccount(info);

		rep.setReturnValue(new Boolean(true));
	    } else if (operationName.equals("DeleteAccount")) {
		Integer id = (Integer)req.get("id");

       	        trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ACCOUNT_OP, 
			    reqTrack, operationName, id.toString());

		(mapp.orders).deleteAccount(id.intValue());

		rep.setReturnValue(new Boolean(true));

	    } else if (operationName.equals("CreateOrder")) {
		OrderInfo info =
		    DataMapper.mapOrder((MInstance)req.get("order"));
       	        trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ORDERS_OP, reqTrack,
			    operationName, new Integer(info.id).toString());
		int id = (mapp.orders).createOrder(info);
		rep.setReturnValue(new Integer(id));
	    } else if (operationName.equals("ModifyOrder")) {
		OrderInfo info =
		    DataMapper.mapOrder((MInstance)req.get("order"));
       	        trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ORDERS_OP, reqTrack,
			    operationName, new Integer(info.id).toString());
	
		(mapp.orders).modifyOrder(info);
		rep.setReturnValue(new Boolean(true));
	    } else if (operationName.equals("DeleteOrder")) {
		Integer id = (Integer)req.get("id");
       	        trace.trace(ORDERS_AeErrors_en_US.ADORDERS_ORDERS_OP, reqTrack,
			    operationName, id.toString());

		(mapp.orders).deleteOrder(id.intValue());
		rep.setReturnValue(new Boolean(true));
	    }
	    reqTrack.addApplicationInfo("OrdersAdapter.Server."+operationName);
	    rep.setTrackingInfo(reqTrack);
	    rep.reply();
	    trace.trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
			rep.getTrackingInfo(), 
			"-OperationImpl-"+operationName );
	} catch (MException ex) {
	    trace.trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, 
			reqTrack, "-OperationImpl-", 
			ex.getMessage());
	} catch (OrdersException ex) {
	  try {
	    trace.trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, 
			reqTrack, "-OperationImpl-", 
			ex.getMessage());
    	    MInstance exInst =((mapp.getClassRegistry()).getDataFactory()).
		newInstance("OrdersExceptionClass");
	    exInst.set("exceptionName", operationName);
	    exInst.set("exceptionMessage", ex.getMessage());

	    reqTrack.addApplicationInfo("OrdersAdapter.Exception."+operationName);
	    rep.setTrackingInfo(reqTrack);
	    
	    rep.setException("OrdersException", exInst);
	    //if (operationName.indexOf("Create") > 0)
	    if(operationName.equals("CreateAccount")||operationName.equals("CreateOrder"))
	      rep.setReturnValue(new Integer(0));
	    else
	      rep.setReturnValue( new Boolean(false));
	    rep.reply();
	  } catch (MException inEx) {
	    trace.trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, 
			null, "-Exception-", 
			inEx.getMessage());
	  }
	}
    }
}

