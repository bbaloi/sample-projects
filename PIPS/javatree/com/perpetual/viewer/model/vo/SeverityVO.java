package com.perpetual.viewer.model.vo;


public class SeverityVO extends SingleIdVO
{
    private String name = null;

    /** Creates a new instance of UserProfileVO */
    public SeverityVO(int id, String name) 
    {
    	this.id = id;
    	this.name = name;
    }

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

 }
