package com.perpetual.viewer.model.vo;


public class DomainHostVO implements java.io.Serializable
{
    private int id = -1;
    private Integer domainId = null;
    private Integer hostId = null;

    public DomainHostVO(int id, Integer domainId, Integer hostId) 
    {
    	this.id = id;
    	this.domainId = domainId;
    	this.hostId = hostId;
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

	public Integer getHostId() {
		return hostId;
	}

	public void setDomainId(Integer integer) {
		domainId = integer;
	}

	public void setHostId(Integer integer) {
		hostId = integer;
	}

}
