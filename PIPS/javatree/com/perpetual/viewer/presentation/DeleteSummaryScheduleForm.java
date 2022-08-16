package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import org.apache.struts.action.ActionForm;


public class DeleteSummaryScheduleForm extends ActionForm
{
	public int getId()
	{
		return m_id;
	}
	public void setId(int id)
	{
		m_id = id;
	}
	public String getName()
	{
		return m_name;
	}
	public void setName(String name)
	{
		m_name = name;
	}

	private int m_id;
	private String m_name;
}



