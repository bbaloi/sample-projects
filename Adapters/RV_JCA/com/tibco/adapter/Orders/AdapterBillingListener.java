package com.tibco.adapter.Orders;

import java.util.*;
import com.tibco.sdk.rpc.MClientRequest;
import com.tibco.sdk.rpc.MClientReply;
import com.tibco.sdk.metadata.MInstance;
import com.tibco.sdk.tools.MTrace;
import com.tibco.sdk.MException;

import com.acme.orders.*;

/**
 * A listener interface for billing events coming out of the
 * orders application.
 */
public class AdapterBillingListener implements BillingListener {

    private TibOrdersApp mapp;
    private static Hashtable orderList = new Hashtable();
    private static Hashtable accountList = new Hashtable();

    public AdapterBillingListener(TibOrdersApp mapp) {
	this.mapp = mapp;
    }

  public Hashtable getAllOrders() {
    return orderList;
  }
    public void createAccount(AccountInfo acct) throws OrdersException {
	try {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_CREATE, 
				  null, new Integer(acct.id).toString()); 
	    
	    MInstance acctData = DataMapper.mapBack(acct, mapp);

	    Integer id = (Integer)acctData.get("id");
	    if (accountList.containsKey(id)) {
	      //System.out.println("throw create exception!");
	      throw new OrdersException("Account id="+id.toString()+" exist already");
	    }
	    else
	      accountList.put(id, acctData);


	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, null, 
				  new Integer(acct.id).toString());
	} catch (MException ex) {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				  "Listener:createAccount", ex.getMessage());
	}
    }

    public void modifyAccount(AccountInfo acct)throws OrdersException {
      try {
	mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_MODIFY,
				  null, new Integer(acct.id).toString());
	    
	    MInstance acctData = DataMapper.mapBack(acct, mapp);
	    Integer id = (Integer)acctData.get("id");
	    if (accountList.containsKey(id)) {
	      accountList.put(id, acctData);
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
				  null, new Integer(acct.id).toString());
	    }
	    else {
	      //System.out.println("Failed to modify account"+id);
	      throw new OrdersException("Order id="+id.toString()+" does not exist");
	    }
	} catch (MException ex) {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				  "Listener:createAccount", ex.getMessage());
	}

    }

    public void deleteAccount(int id) throws OrdersException {
     
	  Integer ID = new Integer(id);
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_DELETE,
				  null, ID.toString());
	   
	    if (accountList.containsKey(ID))
	      accountList.remove(ID);
	    else {
	      System.out.println("Failed to delete account "+ID);
	      throw new OrdersException("Order id="+ID.toString()+" does not exist");
	    }
  
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
				  null, new Integer(id).toString());
	   
    }

    public void createOrder(OrderInfo order)throws OrdersException {
	try {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_CREATE, 
				  null, new Integer(order.id).toString());
	    MInstance orderData = DataMapper.mapBack(order, mapp);

	    Integer id = (Integer)orderData.get("id");
	    if (orderList.containsKey(id)) {
	      System.out.println("throw create exception!");
	      throw new OrdersException("Order id#"+id.toString()+" exist already");
	    }
	    else
	      orderList.put(id, orderData);

	    // Create an order billing
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
				  null, new Integer(order.id).toString());
	} catch (Exception ex) {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				  "Listener:createOrder",ex.getMessage());
	}
    }

    public void modifyOrder(OrderInfo order)throws OrdersException {
	try {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_MODIFY, 
				  null, new Integer(order.id).toString());
	    MInstance orderData = DataMapper.mapBack(order, mapp);
	    // Modify existing order billing
	    Integer id = (Integer)orderData.get("id");
	    if (orderList.containsKey(id)) {
	      orderList.put(id, orderData);
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
				  null, new Integer(order.id).toString());
	    }
	    else {
	      System.out.println("Failed to modify order"+id);
	      throw new OrdersException("Order id="+id.toString()+" does not exist");
	    }


	} catch (MException ex) {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null,
				  "Listener:modifyOrder", ex.getMessage());
	}
    }

    public void deleteOrder(int id)throws OrdersException {
	try {
	  Integer ID = new Integer(id);
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OP_DELETE, 
				  null, ID.toString());
	    
	    if (orderList.containsKey(ID))
	      orderList.remove(ID);
	    else {
	      //System.out.println("Failed to delete order "+ID);
	      throw new OrdersException("Order id="+ID.toString()+" does not exist");
	    }

	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_BILLING_OPERATION, 
				  null, new Integer(id).toString());
	} catch (Exception ex) {
	    mapp.getTrace().trace(ORDERS_AeErrors_en_US.ADORDERS_EXCEPTION, null, 
				  "Listener:deleteOrder",ex.getMessage());
	}
    }
}
