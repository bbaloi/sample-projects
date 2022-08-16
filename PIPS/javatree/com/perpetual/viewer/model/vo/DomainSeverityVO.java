package com.perpetual.viewer.model.vo;


public class DomainSeverityVO implements java.io.Serializable
{
    private int id = -1;
    private Integer domainId = null;
    private Integer severityId = null;

    public DomainSeverityVO(int id, Integer domainId, Integer severityId) 
    {
    	this.id = id;
    	this.domainId = domainId;
    	this.severityId = severityId;
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

	public Integer getSeverityId() {
		return severityId;
	}

	public void setDomainId(Integer integer) {
		domainId = integer;
	}

	public void setSeverityId(Integer integer) {
		severityId = integer;
	}

}
