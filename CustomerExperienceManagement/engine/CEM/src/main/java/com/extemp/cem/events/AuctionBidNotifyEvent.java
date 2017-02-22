package com.extemp.cem.events;

public class AuctionBidNotifyEvent extends BaseEvent
{

	private String auctionObjectId;
	private String userId;
	private String winner;
	private double runningTotal;
	private String status;
	
	
	
	public AuctionBidNotifyEvent(String pEventId, String pUserId,
			String pEventName, String pEventSource, String pEventType,
			String pEventMessage, String pCorrelationId) {
		super(pEventId, pUserId, pEventName, pEventSource, pEventType, pEventMessage,
				pCorrelationId);
		// TODO Auto-generated constructor stub
	}
	public AuctionBidNotifyEvent()
	{
		super(null, null, null, null, null, null,null);
	}
	
	public String getAuctionObjectId() {
		return auctionObjectId;
	}
	public void setAuctionObjectId(String auctioObjectId) {
		this.auctionObjectId = auctioObjectId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public double getRunningTotal() {
		return runningTotal;
	}
	public void setRunningTotal(double runningTotal) {
		this.runningTotal = runningTotal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
