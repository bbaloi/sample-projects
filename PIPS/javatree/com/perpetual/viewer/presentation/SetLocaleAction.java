package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.servlet.http.*;
import javax.rmi.*;

import org.apache.struts.action.*;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;


public class SetLocaleAction extends Action
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewSyslogAction.class );

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		SetLocaleForm setLocaleForm = (SetLocaleForm)form;
		try
		{
			Locale locale = new Locale(setLocaleForm.getLanguage());
			sLog.debug("changing locale to " + locale);
//			request.getSession().setAttribute(Action.LOCALE_KEY, newLocale);		constant now in the non oop Globals class
			setLocale(request, locale);
		}
		catch(Exception e)
		{
			sLog.error("error SelectUserForm.perform()", e);
			return (mapping.findForward("system.error"));
		}
//System.out.println("!!!!!!!! " + selectedUser.getReason());

		ActionForward forward = mapping.findForward("success");
		sLog.debug("formarding to " + forward);
		return forward;
	}
}




