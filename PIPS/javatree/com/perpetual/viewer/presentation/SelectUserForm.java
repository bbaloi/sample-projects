package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import org.apache.struts.action.ActionForm;
import java.util.*;


public class SelectUserForm extends ActionForm
{
	public void setUserList(Collection userList)
	{
		m_userList = userList;
	}
	public Collection getUserList()
	{
		return m_userList;
	}
	public void setSelectedUser(int selectedUser)
	{
		m_selectedUser = selectedUser;
	}
	public int getSelectedUser()
	{
		return m_selectedUser;
	}
	public void setReason(String reason)
	{
		m_reason = reason;
	}
	public String getReason()
	{
		return m_reason;
	}

	private Collection m_userList;
	private int m_selectedUser = -1;
	private String m_reason;
}


