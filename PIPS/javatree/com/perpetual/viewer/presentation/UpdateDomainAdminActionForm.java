package com.perpetual.viewer.presentation;

/**
 * @author mikep
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import org.apache.struts.action.ActionForm;


public class UpdateDomainAdminActionForm extends ActionForm
{
	public void setEnableUsers(String[] enableUsers)
	{
		m_enableUsers = enableUsers;
	}
	public String[] getEnableUsers()
	{
		return m_enableUsers;
	}
	public void setDisableUsers(String[] disableUsers)
	{
		m_disableUsers = disableUsers;
	}
	public String[] getDisableUsers()
	{
		return m_disableUsers;
	}

	private String[] m_enableUsers, m_disableUsers;
	private Long 	collectionInterval = null;
	private boolean summaryProcessingEnabled = false;
	private int id;
	/**
	 * @return
	 */
	public Long getCollectionInterval() {
		return collectionInterval;
	}

	/**
	 * @return
	 */
	public boolean getSummaryProcessingEnabled() {
		return summaryProcessingEnabled;
	}

	/**
	 * @param long1
	 */
	public void setCollectionInterval(Long long1) {
		collectionInterval = long1;
	}

	/**
	 * @param b
	 */
	public void setSummaryProcessingEnabled(boolean b) {
		summaryProcessingEnabled = b;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

}


