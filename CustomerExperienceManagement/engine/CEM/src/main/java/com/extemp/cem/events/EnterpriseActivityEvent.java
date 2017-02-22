package com.extemp.cem.events;

public class EnterpriseActivityEvent extends BaseEvent
{

	
	private String enterpriseId;
	private String activityName;
	private String activityObject;
	private String activityObjectURI;
	private String activityObjectCategory;
	private String activityContextCategory;
	private String activityContextContent;
	
	
	public EnterpriseActivityEvent() {
		super(null, null, null, null, null, null,null);
		// TODO Auto-generated constructor stub
	}
	
	public String getEnterpriseId() {
		return enterpriseId;
	}


	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}


	public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


	public String getActivityObject() {
		return activityObject;
	}


	public void setActivityObject(String activityObject) {
		this.activityObject = activityObject;
	}


	public String getActivityObjectURI() {
		return activityObjectURI;
	}


	public void setActivityObjectURI(String activityObjectURI) {
		this.activityObjectURI = activityObjectURI;
	}


	public String getActivityObjectCategory() {
		return activityObjectCategory;
	}


	public void setActivityObjectCategory(String activityCategory) {
		this.activityObjectCategory = activityCategory;
	}
	public String getActivityContextCategory() {
		return activityContextCategory;
	}

	public void setActivityContextCategory(String activityContextCategory) {
		this.activityContextCategory = activityContextCategory;
	}

	public String getActivityContextContent() {
		return activityContextContent;
	}

	public void setActivityContextContent(String activityContextContent) {
		this.activityContextContent = activityContextContent;
	}

	

}
