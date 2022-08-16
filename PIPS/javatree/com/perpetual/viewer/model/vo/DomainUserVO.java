package com.perpetual.viewer.model.vo;


public class DomainUserVO implements java.io.Serializable
{
    private int id = -1;
    private Integer domainId = null;
    private Integer userId = null;

    public DomainUserVO(int id, Integer domainId, Integer userId) 
    {
    	this.id = id;
    	this.domainId = domainId;
    	this.userId = userId;
    }

	public int getId() {
		return id;
	}

	public void setId(int i) {
		id = i;
	}
	
	public Integer getDomainId() {
		return domainId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setDomainId(Integer integer) {
		domainId = integer;
	}

	public void setUserId(Integer integer) {
		userId = integer;
	}

}
