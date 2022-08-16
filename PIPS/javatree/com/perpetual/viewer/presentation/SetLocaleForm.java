package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import org.apache.struts.action.ActionForm;
import java.util.*;


public class SetLocaleForm extends ActionForm
{
	public void setLanguage(String language)
	{
		m_language = language;
	}
	public String getLanguage()
	{
		return m_language;
	}

	private String m_language;
}



