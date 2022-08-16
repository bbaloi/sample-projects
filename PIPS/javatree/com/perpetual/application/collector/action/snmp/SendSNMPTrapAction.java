/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.snmp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.action.BaseAction;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.ListenerConstants;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.management.util.ManagementConstants;
import com.perpetual.management.PerpetualManagementAgent;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import java.util.Set;
/**
 * @author brunob
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SendSNMPTrapAction extends BaseAction {

	private static final Logger LOGGER = Logger.getLogger(SendSNMPTrapAction.class);
					
        private MBeanServer lServer=null;
	private List forwardHostList = new ArrayList();       
        private ObjectName syslogSenderName=null;     
	//private ForwardQueue queue = null;
	//private ForwardQueueProcessor queueProcessor = null;


	public SendSNMPTrapAction(String selectorString, String actionText, Configuration configuration) throws Exception
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
	
	public void open() throws Exception 
        {
		//intialisse connection to MBeanServer and SyslogMBean
            LOGGER.info("Getting a handle to the SyslogTrapSender(SyslogMBean)");
                    
            try
            {
                syslogSenderName= new ObjectName(ManagementConstants.SyslogObjectName);
            }
            catch(javax.management.MalformedObjectNameException ex)
            {
                ex.printStackTrace();
            }
            lServer = PerpetualManagementAgent.getInstance().getMBeanServer();
            Set syslogMBeanSet = lServer.queryMBeans(syslogSenderName,null);
            if(syslogMBeanSet.isEmpty())
            {
                LOGGER.error("No SyslogMBean registered !");
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

		for (Iterator i = this.forwardHostList.iterator(); i.hasNext();)
                {
                        LOGGER.debug("------invoking SyslogMBean----------");
                        Object [] params = new Object[9];
			SNMPDestination dest = (SNMPDestination) i.next();                      
                        params[0] = dest.getDestHost();
                        params[1] = dest.getDestPort(); 
                        params[2] = (new Date(message.getSystemTimestamp())).toString(); 
                        params[3] = message.getHost(); 
                        params[4] = new Integer(message.getFacility());
                        params[5] = new Integer(message.getSeverity());
                        params[6] = new Integer(message.getProcessId());
                        params[7] = message.getProcessName();
                        params[8] = message.getStringifiedMessage();
                        
                        String [] signature = new String [9];
                        signature[0] = "java.lang.String";
                        signature[1] = "java.lang.Integer"; 
                        signature[2] = "java.lang.String"; 
                        signature[3] = "java.lang.String"; 
                        signature[4] = "java.lang.Integer"; 
                        signature[5] = "java.lang.Integer";
                        signature[6] = "java.lang.Integer"; 
                        signature[7] = "java.lang.String"; 
                        signature[8] = "java.lang.String"; 
                        
                       //try
                       //{
                           lServer.invoke(syslogSenderName,"sendSyslogNotification",params,signature);
                         //}
                        //catch(Throwable ex)
                        //{
                          //  LOGGER.error("couldn't not invoke SyslogMBean",ex);
                            //throw ex;
                        //}
		}
	}
	
	// look to see if the first non-whitespace expression = @<snmp>
	// which indicates an action
	public boolean isAction (String actionText)
        {
		boolean result = false;
                LOGGER.debug("Trying to match:"+actionText);
                int startLabelIndex,endLabelIndex;
                
                startLabelIndex=actionText.indexOf("<");
                endLabelIndex=actionText.indexOf(">");
                String label = actionText.substring(startLabelIndex+1,endLabelIndex);
                //LOGGER.debug("substring:"+label);
                
                if(label.equals("snmp"))
                       result=true;
		
		return result;
	}
	
	public void populateForwardHostList (String actionText)
	{
		// format is <snmp>host1[:port],host2[:port]...
		// pick off first >
		
		String hosts = actionText.substring(actionText.indexOf('>') + 1);
		
		StringTokenizer st = new StringTokenizer(hosts, ",");
		
		while (st.hasMoreTokens()) {
			String hostPortString = st.nextToken();
			String hostString = null;
			String portString = null;
			
                        int port=0;
                        
			int colonIndex = hostPortString.indexOf(':');
						
			if (colonIndex == -1 ) 
                        {	// no port
				hostString = hostPortString;
			} 
                        else if (colonIndex != -1 )
                        {
				// host:port
				hostString = hostPortString.substring(0, colonIndex);
				portString = hostPortString.substring(colonIndex + 1);
			} 
                        			
			if (portString == null) 
                        {
				port = ManagementConstants.DefaultSnmpTargetPort;
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
                        
			LOGGER.info("adding forward for host = "
				+ hostString
				+ ", port = " + port);
			
			SNMPDestination dest = new SNMPDestination(hostString, new Integer(port));
			this.forwardHostList.add(dest);
		}
	}
}