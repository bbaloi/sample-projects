package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class ReasonForm extends ActionForm
{
	public void setId(int id)
	{
		m_id = id;
	}
	public int getId()
	{
		return m_id;
	}
	public void setReason(String reason)
	{
		m_reason = reason;
	}
	public String getReason()
	{
		return m_reason;
	}

	private int m_id = -1;
	private String m_reason;
}



