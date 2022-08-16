package com.perpetual.viewer.model.vo;


public class HostVO extends SingleIdVO
{
    private String name, description;

    public HostVO(int id, String name, String description)
    {
    	this.id = id;
    	this.name = name;
		this.description = description;
    }

	public String getName() { return name; } 
	public void setName(String string) { name = string; }

	public void setDescription(String description) { this.description = description; }
	public String getDescription() { return description; }
 }
