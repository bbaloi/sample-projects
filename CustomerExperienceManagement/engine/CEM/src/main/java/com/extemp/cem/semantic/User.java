package com.extemp.cem.semantic;

import java.util.List;

public class User 
{
	private String userId;
	private List <Intent> intentList;	
	
		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Intent> getIntentList() {
		return intentList;
	}
	public void setIntentList(List<Intent> intentList) {
		this.intentList = intentList;
	}
	
}
