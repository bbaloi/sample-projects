package com.extemp.cem.events;

public class AuctionBidSubmitEvent extends BaseEvent
{

	private String auctionObjectId;
	private double currentBid;
	private double runningTotal;
	
	
	
	public AuctionBidSubmitEvent(String pEventId, String pUserId,
			String pEventName, String pEventSource, String pEventType,
			String pEventMessage, String pCorrelationId) {	
		
		super(pEventId, pUserId, pEventName, pEventSource, pEventType, pEventMessage,
				pCorrelationId);
		// TODO Auto-generated constructor stub
	}
	public AuctionBidSubmitEvent()
	{
		super(null, null, null, null, null, null,null);
	}
	
	public String getAuctionObjectId() {
		return auctionObjectId;
	}
	public void setAuctionObjectId(String auctionObjectId) {
		this.auctionObjectId = auctionObjectId;
	}
	public double getCurrentBid() {
		return currentBid;
	}
	public void setCurrentBid(double currentBid) {
		this.currentBid = currentBid;
	}
	public double getRunningTotal() {
		return runningTotal;
	}
	public void setRunningTotal(double runningTotal) {
		this.runningTotal = runningTotal;
	}

}
