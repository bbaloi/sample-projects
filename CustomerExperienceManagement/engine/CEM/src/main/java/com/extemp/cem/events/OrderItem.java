package com.extemp.cem.events;

import javax.xml.bind.annotation.XmlElement;

public class OrderItem 
{
	    protected String itemId;
	    protected String itemName;
	    protected double price;
	    protected int quantity;
	    protected String orderId;
	    
		public OrderItem(String pId, String pName, double pPrice,int pQuant,String pOrderId)
	    {
	    	
	    	itemId = pId;
	    	itemName=pName;
	    	price=pPrice;
	    	quantity=pQuant;
	    	orderId = pOrderId;
	    }
	    
	    public String getItemId() {
			return itemId;
		}
		public void setItemId(String itemId) {
			this.itemId = itemId;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public String getOrderId() {
			return orderId;
		}
	  public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		

}
