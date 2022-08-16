package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot - http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;

import org.apache.struts.action.ActionForm;


public class MaintainMessageForm extends ActionForm
{
        private int m_id;
	private String m_name,m_Pattern,m_Description;

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
        public String getPattern()
        {
            return m_Pattern;
        }
        public void setPattern(String pPattern)
        {
            m_Pattern=pPattern;
        }
        public String getDescription()
        {
            return m_Description;
        }
        public void setDescription(String pDescription)
        {
            m_Description = pDescription;
        }
	
	
}




