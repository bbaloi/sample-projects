package com.tibco.adapter.Orders;

import com.tibco.sdk.metadata.MInstance;
import com.tibco.sdk.MException;
import com.tibco.sdk.MApp;

import com.acme.orders.*;

/**
 * Maps AccountInfo and OrderInfo from MInstance to Java and back.
 */
public class DataMapper {

    public static AccountInfo mapAccount(MInstance m) throws MException {
	AccountInfo acct = new AccountInfo();
	acct.id = ((Integer)m.get("id")).intValue();
	acct.name = (String)m.get("name");
	acct.otherInfo = (String)m.get("otherInfo");
	return acct;
    }
    
    public static OrderInfo mapOrder(MInstance m) throws MException {
	OrderInfo order = new OrderInfo();
	order.id = ((Integer)m.get("id")).intValue();
	order.acctId = ((Integer)m.get("acctId")).intValue();
	order.orderItem = (String)m.get("orderItem");
	order.otherInfo = (String)m.get("otherInfo");
	return order;
    }
    
    public static MInstance mapBack(AccountInfo acct, MApp mapp) 
	throws MException {
	MInstance m =
	((mapp.getClassRegistry()).getDataFactory()).
	    newInstance("AccountInfo");
	m.set("id", new Integer(acct.id));
	m.set("name", acct.name);
	m.set("otherInfo", acct.otherInfo);
	return m;
    }
    
    public static MInstance mapBack(OrderInfo order, MApp mapp) 
	throws MException {
	MInstance m =
	((mapp.getClassRegistry()).getDataFactory()).
	    newInstance("OrderInfo");
	m.set("id", new Integer(order.id));
	m.set("acctId", new Integer(order.acctId));
	m.set("orderItem", order.orderItem);
	m.set("otherInfo", order.otherInfo);
	return m;
    }
    
}
