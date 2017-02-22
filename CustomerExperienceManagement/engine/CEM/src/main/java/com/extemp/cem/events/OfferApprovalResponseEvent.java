package com.extemp.cem.events;

import javax.xml.bind.annotation.XmlElement;

public class OfferApprovalResponseEvent extends BaseEvent
{

	    protected String userId;
	    protected String reqId;
	    protected String userName;
	    protected String offer;
	    protected String offerType;
	    protected String applicationSrc;  
	    protected boolean approved;
	  
	    
	   

		public OfferApprovalResponseEvent()
		{
			super(null,null,null,null,null,null,null);
		}
	
	    public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getReqId() {
			return reqId;
		}

		public void setReqId(String reqId) {
			this.reqId = reqId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getOffer() {
			return offer;
		}

		public void setOffer(String offer) {
			this.offer = offer;
		}

		public String getOfferType() {
			return offerType;
		}

		public void setOfferType(String offerType) {
			this.offerType = offerType;
		}

		public String getApplicationSrc() {
			return applicationSrc;
		}

		public void setApplicationSrc(String applicationSrc) {
			this.applicationSrc = applicationSrc;
		}
	
		public boolean getApproved() {
			return approved;
		}

		public void setApproved(boolean approved) {
			this.approved = approved;
		}
	
	
}
