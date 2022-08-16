/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.message.SyslogMessage;
/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class BaseAction {
	
	private static final Logger LOGGER =
			Logger.getLogger(BaseAction.class);
	
	private Selector selector;
	private List parameters;
	private String actionText;
	protected Configuration configuration;

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public List getParameters() {
		return parameters;
	}

	public void setParameters(List parameters) {
		this.parameters = parameters;
	}
	
	public BaseAction (String selector, String actionText,
		Configuration configuration) throws Exception
	{
		LOGGER.info("initializing action: selector = "
			+ selector + ", actionText = " + actionText);
		
		this.selector = new Selector(selector);
		this.actionText = actionText;
		this.configuration = configuration;
	}
	
	public boolean isMatch (SyslogMessage message)
	{
		return this.selector.isMatch(message);
	}

	public final void execute(SyslogMessage message) throws Exception
	{
		if (this.selector.isMatch(message)) {
			doAction(message);	
		}
	}
		
	public abstract boolean isAction (String actionText);
	
	public abstract void open() throws Exception;
	
	public abstract void close() throws Exception;
	
	public abstract void doAction(SyslogMessage message) throws Exception;

}
