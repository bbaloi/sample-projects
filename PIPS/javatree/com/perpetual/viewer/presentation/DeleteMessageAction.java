package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.util.PerpetualC2Logger;


public class DeleteMessageAction extends PerpetualAction
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(DeleteMessageAction.class);
    
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		DeleteMessageForm deleteMessageForm = (DeleteMessageForm)form;

		try
		{
			MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
			try
			{
				messagePatternCRUD.deleteMessagePattern(deleteMessageForm.getId());

				return mapping.findForward("success");
			}
			finally
			{
				messagePatternCRUD.remove();
			}
		}
		catch(Exception e)
		{
			sLog.error("Problem deleting log schedule", e);
		}

		return mapping.findForward("failure");
	}
	
}


