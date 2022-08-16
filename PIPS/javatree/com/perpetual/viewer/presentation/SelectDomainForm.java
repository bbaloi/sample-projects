package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class SelectDomainForm extends ActionForm
{
	public void setDomainList(Collection domainList)
	{
		m_domainList = domainList;
	}
	public Collection getDomainList()
	{
		return m_domainList;
	}
	public void setSelectedDomain(int selectedDomain)
	{
		m_selectedDomain = selectedDomain;
	}
	public int getSelectedDomain()
	{
		return m_selectedDomain;
	}
	public void setReason(String reason)
	{
		m_reason = reason;
	}
	public String getReason()
	{
		return m_reason;
	}

	private Collection m_domainList;
	private int m_selectedDomain = -1;
	private String m_reason;
}


