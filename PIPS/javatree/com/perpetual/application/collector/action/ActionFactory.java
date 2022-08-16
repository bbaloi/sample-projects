/*
 * Created on 26-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.action.forward.ForwardAction;
import com.perpetual.application.collector.action.snmp.SendSNMPTrapAction;
import com.perpetual.application.collector.configuration.Configuration;

/**
 * @author simon
 *
 * Create a concrete instance of an action class that corresponds to the 
 * action text that would be found in a configuratio file.
 */
public class ActionFactory {

	private static final Logger LOGGER =
			Logger.getLogger(ActionFactory.class);
	
	public static final Class[] ACTIONS = {ForwardAction.class,SendSNMPTrapAction.class};
	
	public static final Class[] CONSTRUCTOR_PARAMETERS = {String.class, String.class,
				Configuration.class};
	
	public BaseAction createAction (String messageSelector, String actionText,
			Configuration configuration)
	{
		BaseAction result = null;
		Object[] parameters = {messageSelector, actionText, configuration};
		
		// try each known action type
		for (int i = 0; result == null && i < ACTIONS.length; i++) {
			try {
				Constructor constructor = (Constructor) ACTIONS[i]
						.getConstructor(CONSTRUCTOR_PARAMETERS);
				
				BaseAction action = (BaseAction) constructor.newInstance(parameters);
				
				if (action.isAction(actionText)) {
					result = action;
				}
			} catch (InvocationTargetException e) {
				LOGGER.error("error constructing action:" + e);
			} catch (Exception e) {
				//	do nothing - try the next one
			}
		}

		return result;
	}
}
