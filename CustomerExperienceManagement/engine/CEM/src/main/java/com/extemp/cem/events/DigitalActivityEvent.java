package com.extemp.cem.events;

public class DigitalActivityEvent extends BaseEvent
{
	
	private String userId;
	private String activityType;
	private String tstamp;
	private String browsedURL;
	private String browsedItemName;
	private String browsedItemCategory;
	private String browsedItemId;
	private String browsedItemBrand;
	private double browsedItemPrice;
	
	
	
	
	
	
	public DigitalActivityEvent()
	{
		super(null,null, null, null, null, null,null);
	}

	public DigitalActivityEvent(String pEventId, String pUserId,String pEventName,
			String pEventSource, String pEventType, String pEventMessage,
			String pCorrelationId) {
		super(pEventId, pUserId, pEventName, pEventSource, pEventType, pEventMessage,
				pCorrelationId);
		// TODO Auto-generated constructor stub
	}

}
