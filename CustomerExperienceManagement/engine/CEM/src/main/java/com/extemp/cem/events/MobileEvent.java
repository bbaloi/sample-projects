package com.extemp.cem.events;

public class MobileEvent extends BaseEvent
{
	
	private String locationCoordinate;
	private String deviceId;
	private String devicePhoneNumber;
	private String locationName;
	private String applicationSource;
	private String requestId;
		
	
	public MobileEvent()
	{
		super(null,null,null,null,null,null,null);
	}
	public MobileEvent(String pEventId,String pUserId,String pEventName,String pEventSource,String pEventType,String pEventMessage,String pCorrelationId)
	{
		super(pEventId,pUserId,pEventName,pEventSource,pEventType,pEventMessage,pCorrelationId);
	}
	
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationCoordinate() {
		return locationCoordinate;
	}
	public void setLocationCoordinate(String locationCoordinate) {
		this.locationCoordinate = locationCoordinate;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDevicePhoneNumber() {
		return devicePhoneNumber;
	}
	public void setDevicePhoneNumber(String devicePhoneNumber) {
		this.devicePhoneNumber = devicePhoneNumber;
	}
	public String getApplicationSource() {
		return applicationSource;
	}
	public void setApplicationSource(String mobileApplicationSource) {
		this.applicationSource = mobileApplicationSource;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
