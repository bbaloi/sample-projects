package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class UpdateRoleForm extends ActionForm
{
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
	public void setAddActionList(String[] addActionList)
	{
		m_addActionList = addActionList;
	}
	public String[] getAddActionList()
	{
		return m_addActionList;
	}
	public void setRemoveActionList(String[] removeActionList)
	{
		m_removeActionList = removeActionList;
	}
	public String[] getRemoveActionList()
	{
		return m_removeActionList;
	}

	private int m_id;
	private String m_roleName;
	private String[] m_addActionList, m_removeActionList;
}




