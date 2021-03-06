package com.acme.orders;

import java.util.*;

/**
 * A listener interface for billing events generated by accounts
 * and orders.
 */
public interface BillingListener {

  public Hashtable getAllOrders();

    public void createAccount(AccountInfo acct) throws OrdersException;

    public void modifyAccount(AccountInfo acct) throws OrdersException;

    public void deleteAccount(int id) throws OrdersException;

    public void createOrder(OrderInfo order) throws OrdersException;

    public void modifyOrder(OrderInfo order) throws OrdersException;

    public void deleteOrder(int id) throws OrdersException;
}
