package com.extemp.cem.events;

import java.io.Serializable;

public abstract class BaseEvent implements Serializable
{
	private String eventId;
	private String eventName;
	private String eventSource;	
	private String eventType;	
	private String eventMessage;	
	private String correlationId;
	private String userId;
	



	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public BaseEvent(String pEventId,String pUserId,String pEventName,String pEventSource,String pEventType,String pEventMessage,String pCorrelationId)
	{
			this.eventId = pEventId;
			this.eventName=pEventName;
			this.eventSource = pEventSource;		
			this.eventType=pEventType;
			this.eventMessage=pEventMessage;
			this.correlationId=pCorrelationId;
			this.userId = pUserId;
	}
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public String getUserId() {
		return userId;
	}
	
	

}
