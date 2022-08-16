package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Collection;

import org.apache.struts.action.ActionForm;


public class UpdateHostForm extends ActionForm
{
	public void setId(int id)
	{
		m_id = id;
	}
	public int getId()
	{
		return m_id;
	}
	public void setName(String name)
	{
		m_name = name;
	}
	public String getName()
	{
		return m_name;
	}
	public void setDescription(String description)
	{
		m_description = description;
	}
	public String getDescription()
	{
		return m_description;
	}

	private int m_id;
	private String m_name, m_description;
}





