package com.perpetual.viewer.presentation;

import java.util.Collection;
import java.util.Iterator;

import javax.naming.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;


import com.perpetual.util.PerpetualC2Logger; 


public class PreMaintainMessageAction extends PerpetualAction 
{
    
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(PreMaintainMessageAction.class);
    
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ActionForward forward = null;
                sLog.debug("In PreMaintainMessageAction!!!");

		try
		{
                    //get message crud
                    MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
			
			try
			{
				request.setAttribute("MessageCollection", messagePatternCRUD.retrieveAll());
			}
			finally
			{
				//summaryScheduleCRUD.remove();
			}

			return mapping.findForward("success");
		}
		catch (Exception ex)
		{
			sLog.error("Problem", ex);
		}

		return mapping.findForward("failure");
	}

	
}




