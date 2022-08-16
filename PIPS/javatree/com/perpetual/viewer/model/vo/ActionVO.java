package com.perpetual.viewer.model.vo;


public class ActionVO implements java.io.Serializable
{
    private int id = -1;
    private String name = null;

    /** Creates a new instance of UserProfileVO */
    public ActionVO(int id, String name) 
    {
    	this.id = id;
    	this.name = name;
    }

	public boolean equals(Object other)
	{
		return other instanceof ActionVO && ((ActionVO)other).id == id;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

 }
