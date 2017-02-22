package com.extemp.cem.auction;

public class AuctionBidder 
{
	private String bidderID;
	private String bidderName;
	private double bid;
	private String bidObject;
	private String bidEvent;
	
	public AuctionBidder()
	{
		
	}
	
	public String getBidderID() {
		return bidderID;
	}
	public void setBidderID(String bidderID) {
		this.bidderID = bidderID;
	}
	public String getBidderName() {
		return bidderName;
	}
	public void setBidderName(String bidderName) {
		this.bidderName = bidderName;
	}
	public double getBid() {
		return bid;
	}
	public void setBid(double bid) {
		this.bid = bid;
	}
	public String getBidObject() {
		return bidObject;
	}
	public void setBidObject(String bidObject) {
		this.bidObject = bidObject;
	}
	public String getBidEvent() {
		return bidEvent;
	}
	public void setBidEvent(String bidEvent) {
		this.bidEvent = bidEvent;
	}
	
	

}
