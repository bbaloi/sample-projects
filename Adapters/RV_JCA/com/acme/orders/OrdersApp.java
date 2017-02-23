package com.acme.orders;

import java.util.*;

/**
 * This is the application specific interface to the Orders Application.
 */
public class OrdersApp {

    private int accts;
    private int orders;
    private BillingListener listener;

    /**
     * Initialize orders.
     */
    public OrdersApp(BillingListener listener) {
	System.out.println("Acme Orders constructor");
	this.listener = listener;
    }

  public String getVersion() {
    return new String("2.0");
  }

  public Hashtable getAllOrders() {
    return listener.getAllOrders();
  }
    /**
     * Create a new account. Returns the account id.
     */
    public int createAccount(AccountInfo acct) throws OrdersException {
        //accts++;
	System.out.println("Acme Create Account " + acct.id);
	//acct.id = accts;

	  listener.createAccount(acct);

	System.out.println("Acme Create Account Done " + acct.id);

	return acct.id;
    }

    /**
     * Modify a given account.
     */
    public void modifyAccount(AccountInfo acct) throws OrdersException {
	System.out.println("Acme Modify Account "+ acct.id);
	listener.modifyAccount(acct);
    }

    /**
     * Delete an account, given its id.
     */
    public void deleteAccount(int id) throws OrdersException {
	System.out.println("Acme Delete Account " + id);
	listener.deleteAccount(id);
    }

    /**
     * Create an order.
     */
    public int createOrder(OrderInfo order) throws OrdersException {
      //orders++;
	System.out.println("Acme Create Order " + order.id);
	//order.id = orders;
	listener.createOrder(order);
	//return orders;
	return order.id;
    }
    
    /**
     * Modify an order.
     */
    public void modifyOrder(OrderInfo order) throws OrdersException {
	System.out.println("Acme Modify Order " + order.id);
	listener.modifyOrder(order);
    }
	
    /**
     * Delete an order.
     */
    public void deleteOrder(int id) throws OrdersException {
	System.out.println("Acme Delete Order " + id);
	listener.deleteOrder(id);
    }
}

