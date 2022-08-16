/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.forward;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.action.BaseAction;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.ListenerConstants;
import com.perpetual.application.collector.message.SyslogMessage;
/**
 * @author simon
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ForwardAction extends BaseAction {

	private static final Logger LOGGER =
			Logger.getLogger(ForwardAction.class);
					
	private List forwardHostList;
	private ForwardQueue queue = null;
	private ForwardQueueProcessor queueProcessor = null;


	public ForwardAction (String selectorString, String actionText,
			Configuration configuration) throws Exception
	{
		super(selectorString, actionText, configuration);
		this.forwardHostList = new ArrayList();
		populateForwardHostList(actionText);
	}

	public List getForwardHostList() {
		return forwardHostList;
	}

	public void setForwardHostList(List forwardHostList) {
		this.forwardHostList = forwardHostList;
	}
	
	public void open() throws Exception {
		// loop through hosts and do an initialize
		// will avoid doing a lookup later
		
		for (Iterator i = this.forwardHostList.iterator(); i.hasNext();) {
			ForwardHost host = (ForwardHost) i.next();
			host.initialize();
		}
	}
	
	public void close() {} // do nothing
	
	public void doAction (SyslogMessage message) throws Exception {
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("forwarding packet id " + message.getPacketId());
		}
		
		// loop through hosts and do a forward 
		// using a stringified SyslogMessage
		// which is either the raw message or
		// the fixed up one (if there were missing fields)

		for (Iterator i = this.forwardHostList.iterator(); i.hasNext();) {
			ForwardHost host = (ForwardHost) i.next();
			host.forward(message);
		}
	}
	
	// look to see if the first non-whitespace character is a '@'
	// which indicates an action
	public boolean isAction (String actionText) {
		boolean scan = true;
		boolean result = false;
			
		for (int i = 0; scan && i < actionText.length(); i++) {
			if (!Character.isWhitespace(actionText.charAt(i))) {
				result = (actionText.charAt(i) == '@');	
				scan = false;
			}
		}

		
		return result;
	}
	
	public void populateForwardHostList (String actionText)
	{
		// format is @host1[:port][@eps],host2[:port][@eps],...
		// pick off first @
		
		String hosts = actionText.substring(actionText.indexOf('@') + 1);
		
		StringTokenizer st = new StringTokenizer(hosts, ",");
		
		while (st.hasMoreTokens()) {
			String hostPortString = st.nextToken();
			String hostString = null;
			String portString = null;
			String epsString = null;
			
			int port = 0;
			int eps = 0;
			
			int colonIndex = hostPortString.indexOf(':');
			int atSignIndex = hostPortString.indexOf('@');
			
			if (colonIndex == -1 && atSignIndex == -1) {
				// no port, no eps specified
				hostString = hostPortString;
			} else if (colonIndex != -1 && atSignIndex == -1) {
				// host:port
				hostString = hostPortString.substring(0, colonIndex);
				portString = hostPortString.substring(colonIndex + 1);
			} else if (colonIndex == -1 && atSignIndex != -1) {
				// host@eps
				hostString = hostPortString.substring(0, atSignIndex);
				epsString = hostPortString.substring(atSignIndex + 1);
			} else if (colonIndex != -1 && atSignIndex != -1) {
				// host:port@eps
				hostString = hostPortString.substring(0, colonIndex);
				portString = hostPortString.substring(colonIndex + 1, atSignIndex);
				epsString = hostPortString.substring(atSignIndex + 1);
			}
			
			if (portString == null) {
				port = ListenerConstants.SERVER_PORT;
			}
			else {
				try {
					port = Integer.parseInt(portString);
				} catch (NumberFormatException e) {
					port = ListenerConstants.SERVER_PORT;
					LOGGER.error(
						"invalid port specification '"
						+ portString
						+ "', using default port "
						+ ListenerConstants.SERVER_PORT
						+ " instead.");
				}
			}
			
			if (epsString == null) {
				eps = 0;
			}
			else {
				try {
					eps = Integer.parseInt(epsString);
				} catch (NumberFormatException e) {
					eps = 0;
					LOGGER.error(
						"invalid eps specification '"
						+ epsString
						+ "', using default eps "
						+ eps
						+ " instead.");
				}
			}

			
			LOGGER.info("adding forward for host = "
				+ hostString
				+ ", port = " + port);
			
			ForwardHost host = new ForwardHost(hostString, port, eps,
				this.configuration);
			
			this.forwardHostList.add(host);
		}
	}
}