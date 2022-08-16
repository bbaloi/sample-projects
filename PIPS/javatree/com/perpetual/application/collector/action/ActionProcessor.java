/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.message.SyslogMessage;
/**
 * @author simon
 *
 */
public class ActionProcessor {
	private static final Logger LOGGER =
			Logger.getLogger(ActionProcessor.class);
			
	private List actionList;

	public ActionProcessor(List actionList) {
		
		this.actionList = actionList == null ? new ArrayList() : actionList;
	}
	
	public void openAllActions() throws Exception
	{
		for (Iterator i = this.actionList.iterator(); i.hasNext(); ) {
			BaseAction action = (BaseAction) i.next();
			action.open();
		}
	}
	
	public void closeAllActions() throws Exception
	{
		for (Iterator i = this.actionList.iterator(); i.hasNext(); ) {
			BaseAction action = (BaseAction) i.next();
			action.close();
		}
	}
	
	public void process(SyslogMessage message)
	{
		for (Iterator i = this.actionList.iterator(); i.hasNext(); ) {
			BaseAction action = (BaseAction) i.next();
			try {
				// base action will check if there is a match
				action.execute(message);
			} catch (Exception e) {
				LOGGER.error("exception while executing command "
					+ action + ": " + e);
			}
		}
		 
	}
}
