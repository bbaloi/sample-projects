package com.extemp.cem.events;

import java.util.ArrayList;
import java.util.List;

public class SportsVenueMobileEvent extends MobileEvent
{

	
	private boolean gameWin;
	private String  evenScorePlayer;
	private List <OrderItem> gamePurchaseItemList;
	
	
	public SportsVenueMobileEvent()
	{
		super(null,null,null,null,null,null,null);
		//gamePurchaseItemList = new ArrayList();
		
	}
	public SportsVenueMobileEvent(String pEventId,String pUserId,String pEventName,String pEventSource,String pEventType,String pEventMessage,String pCorrelationId)
	{
		super(pEventId,pUserId,pEventName,pEventSource,pEventType,pEventMessage,pCorrelationId);
	}
	
	private String seatNumber;
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public boolean getGameWin() {
		return gameWin;
	}
	public void setGameWin(boolean gameWin) {
		this.gameWin = gameWin;
	}
	public String getEvenScorePlayer() {
		return evenScorePlayer;
	}
	public void setEvenScorePlayer(String evenScorePlayer) {
		this.evenScorePlayer = evenScorePlayer;
	}
	public List getGamePurchaseItemList() {
		return gamePurchaseItemList;
	}
	public void setGamePurchaseItemList(List gamePurchaseItemList) {
		this.gamePurchaseItemList = gamePurchaseItemList;
	}
}
