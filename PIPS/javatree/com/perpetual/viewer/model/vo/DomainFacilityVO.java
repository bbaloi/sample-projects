package com.perpetual.viewer.model.vo;


public class DomainFacilityVO implements java.io.Serializable
{
    private int id = -1;
    private Integer domainId = null;
    private Integer facilityId = null;


    /** Creates a new instance of UserProfileVO */
    public DomainFacilityVO(int id, Integer domainId, Integer facilityId) 
    {
    	this.id = id;
    	this.domainId = domainId;
    	this.facilityId = facilityId;
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

	public Integer getFacilityId() {
		return facilityId;
	}

	public void setDomainId(Integer integer) {
		domainId = integer;
	}

	public void setFacilityId(Integer integer) {
		facilityId = integer;
	}

}
