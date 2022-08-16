package com.perpetual.viewer.model.vo;


public class RoleActionVO implements java.io.Serializable
{
    private int id = -1;
    private Integer roleId = null;
    private Integer actionId = null;


    /** Creates a new instance of UserProfileVO */
    public RoleActionVO(int id, Integer roleId, Integer actionId) 
    {
    	this.id = id;
    	this.roleId = roleId;
    	this.actionId = actionId;
    }
	/**
	 * @return
	 */
	public Integer getActionId() {
		return actionId;
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
	public Integer getRoleId() {
		return roleId;
	}

	/**
	 * @param integer
	 */
	public void setActionId(Integer integer) {
		actionId = integer;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param integer
	 */
	public void setRoleId(Integer integer) {
		roleId = integer;
	}

}
