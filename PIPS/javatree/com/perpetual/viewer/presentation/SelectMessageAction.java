package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.text.*;
import javax.servlet.http.*;
import javax.rmi.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;
import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
//import com.perpetual.log.LogSystem;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;

import com.perpetual.util.PerpetualC2Logger;


public class SelectMessageAction extends PerpetualAction
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(SelectMessageAction.class);
    
    private MessagePatternCRUD messagePatternCRUD = null;
    private MessagePatternData messageData=null;
    
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
            sLog.debug("in SelectMessageAction() !");
                        
		try
		{
						
			try
			{
				messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
				
				ReasonForm reasonForm = (ReasonForm)form;
				String reason = reasonForm.getReason();
                                MaintainMessageForm defaultForm = new MaintainMessageForm();

				if ("add".equals(reason) )
				{
                                        sLog.debug("This is Add !");
					defaultForm.setName("");
					defaultForm.setId(-1);
                                        request.setAttribute("defaultForm", defaultForm);
				}
                                else if ("edit".equals(reason))
                                {
                                    sLog.debug("This is Edit !");
                                    
                                    int id = reasonForm.getId();
					if (id == -1)
						throw new Exception("No Message selected !");
                                    
                                   messageData = messagePatternCRUD.retrieveByPrimaryKey(id);
					
                                    sLog.debug("msgId:"+id);                         
                                    defaultForm.setId(id);
                                    String pName = messageData.getName();
                                    sLog.debug("PatternName:"+pName);
                                    defaultForm.setName(pName);	
                                    String p = messageData.getPattern();
                                    sLog.debug("Pattern:"+p);
                                    defaultForm.setPattern(p);	
                                    String desc=messageData.getDescription();
                                    defaultForm.setDescription(desc);	
                                    sLog.debug("Desc:"+desc);
                                    request.setAttribute("defaultForm", defaultForm);
				
                                }
                               	else
				{
					// delete require something selected
					 sLog.debug("This is Delete !");
					int id = reasonForm.getId();
					if (id == -1)
						throw new Exception("No Message selected !");

					messageData = messagePatternCRUD.retrieveByPrimaryKey(id);
					
						DeleteMessageForm deleteMessageForm = new DeleteMessageForm();
						deleteMessageForm.setId(reasonForm.getId());
						deleteMessageForm.setName(messageData.getName());
						request.setAttribute("DeleteMessageForm", deleteMessageForm);
                            }

				return mapping.findForward(reason);
			}
			finally
			{
				// remove will not fail, or else things are so far gone that it'll be game over because of other reasons, don't wrap in try/catch
				//
				if (messagePatternCRUD != null) messagePatternCRUD.remove();
				
			}
		}
		catch(Exception e)
		{
			sLog.error("problem", e);
			return mapping.findForward("failure");
		}
	}
}






