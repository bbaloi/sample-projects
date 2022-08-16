package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import org.apache.struts.action.ActionForm;


public class DeleteDomainForm extends ActionForm
{
	public int getId() {
		return id;
	}

	public int getName() {
		return name;
	}

	public void setId(int i) {
		id = i;
	}

	public void setName(int i) {
		name = i;
	}

	private int id;
	private int name;	// int ???
}


