package com.perpetual.viewer.model.vo;



public class DomainVO extends SingleIdVO
{
    private String name = null;
    private Long lastCollectionTime = null;
    private Long collectionInterval = null;
    private boolean summaryProcessingEnabled = false;
    

	public DomainVO(int id) 
	{
		this.id = id;
	}
   

    public DomainVO(int id, String name) 
    {
    	this.id = id;
    	this.name = name;
    	this.lastCollectionTime = null;
    	this.collectionInterval = null;
    	this.summaryProcessingEnabled = false;
    }
    
    public DomainVO (int id, String name, boolean summaryProcessingEnabled,
    		Long lastCollectionTime, Long collectionInterval)
    {
		this.id = id;
		this.name = name;
		this.lastCollectionTime = lastCollectionTime;
		this.collectionInterval = collectionInterval;
		this.summaryProcessingEnabled = summaryProcessingEnabled;
    }

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public Long getCollectionInterval() {
		return collectionInterval;
	}

	public Long getLastCollectionTime() {
		return lastCollectionTime;
	}

	public boolean getSummaryProcessingEnabled() {
		return summaryProcessingEnabled;
	}

	public void setCollectionInterval(Long integer) {
		collectionInterval = integer;
	}

	public void setLastCollectionTime(Long date) {
		lastCollectionTime = date;
	}

	public void setSummaryProcessingEnabled(boolean b) {
		summaryProcessingEnabled = b;
	}
 }
