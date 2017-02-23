package com.tibco.xpdl.bw.vo;

public class ExtAttrValsVO 
{
	private String xOffset;
	private String yOffset;
	private String activityType;
	private String eventFlowType;
	
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getEventFlowType() {
		return eventFlowType;
	}
	public void setEventFlowType(String eventFlowType) {
		this.eventFlowType = eventFlowType;
	}
	public String getXOffset() {
		return xOffset;
	}
	public void setXOffset(String offset) {
		xOffset = offset;
	}
	public String getYOffset() {
		return yOffset;
	}
	public void setYOffset(String offset) {
		yOffset = offset;
	}
}
