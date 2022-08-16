package com.perpetual.viewer.model.vo;


public class RoleVO extends SingleIdVO
{
	public RoleVO(int id, String roleName)
	{
		m_id = id;
		m_roleName = roleName;
	}
	public void setId(int id)
	{
		m_id = id;
	}
	public int getId()
	{
		return m_id;
	}
	public void setRoleName(String roleName)
	{
		m_roleName = roleName;
	}
	public String getRoleName()
	{
		return m_roleName;
	}

	private int m_id;
	private String m_roleName;
}


