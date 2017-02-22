package com.extemp.cem.events;

public class Notification extends BaseEvent
{
	private String notificationId;	
	private String notificationType;
	private String notificationName;
	private String notificationMessage;
	private String applicationSource;
	private String requestId;
	private String source;
	private String notificationPaylod;
		
	
	
	public Notification()
	{
		super(null,null,null,null,null,null,null);
	}
	public Notification(String pEventId,String pUserId,String pEventName,String pEventSource,String pEventType,String pEventMessage,String pCorrelationId)
	{
		super(pEventId,pUserId,pEventName,pEventSource,pEventType,pEventMessage,pCorrelationId);
	}
	
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getNotificationName() {
		return notificationName;
	}
	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}
	public String getNotificationMessage() {
		return notificationMessage;
	}
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	public String getApplicationSource() {
		return applicationSource;
	}
	public void setApplicationSource(String applicationSource) {
		this.applicationSource = applicationSource;
	}
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getNotificationPaylod() {
		return notificationPaylod;
	}
	public void setNotificationPaylod(String notificationPaylod) {
		this.notificationPaylod = notificationPaylod;
	}

}
