package com.extemp.semantic.handler;

public class ActivityEvent 
{
	private String _activityName;
	private String _activityURL;
	private String _userID;
	private String _item;
	
	
	
	public String get_item() {
		return _item;
	}
	public void set_item(String _item) {
		this._item = _item;
	}
	public ActivityEvent(String pId,String pActivityName,String pActivityURL, String pItem)
	{
		_activityName=pActivityName;
		_activityURL = pActivityURL;
		_userID = pId;
		_item=pItem;
		
		
	}
	public String get_activityName() {
		return _activityName;
	}

	public void set_activityName(String _activityName) {
		this._activityName = _activityName;
	}

	public String get_activityURL() {
		return _activityURL;
	}

	public void set_activityURL(String _activityURL) {
		this._activityURL = _activityURL;
	}

	public String get_userID() {
		return _userID;
	}

	public void set_userID(String _userID) {
		this._userID = _userID;
	}

	
	

}
