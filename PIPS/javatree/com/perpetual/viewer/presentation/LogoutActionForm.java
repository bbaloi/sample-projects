package com.perpetual.viewer.presentation;
import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class LogoutActionForm extends ActionForm
{
	public void setReason(String reason)
	{
		m_reason = reason;
	}
	
	public String getReason()
	{
		return m_reason;
	}

	private Collection m_domainList;
	private int m_selectedDomain;
	private String m_reason;
}


