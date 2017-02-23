package com.extemp.semantic.vo;

import java.util.ArrayList;
import java.util.List;

public class EventVO 
{
	private String userID;
	private List eventList = new ArrayList();
	
	
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public List getEventList() {
		return eventList;
	}
	public void setEventList(List eventList) {
		this.eventList = eventList;
	}
	
	

}
