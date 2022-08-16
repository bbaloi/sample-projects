package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.util.EJBLoader;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;


//public class SummaryScheduleAction extends ViewGeneralSummaryAction
public class MaintainMessageAction extends PerpetualAction
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( MaintainMessageAction.class );
	
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
        ActionForward forward = null;
            
        sLog.debug("In MaintainMessageAction....");
		MaintainMessageForm maintainMessageForm = (MaintainMessageForm)form;
		sLog.info(maintainMessageForm.toString());

		try
		{
			
			HttpSession session = request.getSession();
			Locale locale = getLocale(request);
	
			{

	
					MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
					try
					{
					
						int id = maintainMessageForm.getId();    
						String patternName = maintainMessageForm.getName();
						String pattern = maintainMessageForm.getPattern();
						String description = maintainMessageForm.getDescription();
						
						// try to compile the pattern
						// if there's an error then force them to go back and re-edit
						Pattern.compile(pattern);
                                                                                                
						MessagePatternData messageData = new MessagePatternData(
								id == -1 ? null : new Integer(id), patternName,pattern,description);
						messagePatternCRUD.addMessagePattern(messageData);
					}
					finally
					{
						messagePatternCRUD.remove();
					}
//				}
//				finally
//				{
//					scheduleCRUD.remove();
//				}
			}

			forward = mapping.findForward("success");
		}
		catch (PatternSyntaxException pe) {
			sLog.error("Error compiling regex ", pe);
			forward = mapping.findForward("invalidRE");
		}
		catch(Exception ex)
		{
			sLog.error("problem", ex);
			forward = mapping.findForward("failure");
		}
		
		return forward;
	}	
}




