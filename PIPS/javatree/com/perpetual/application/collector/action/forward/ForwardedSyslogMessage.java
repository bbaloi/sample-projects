/*
 * Created on 8-Aug-2003
 *
 * NO WARRANTY  
 * -----------  
 * THIS SOFTWARE IS PROVIDED "AS-IS"  WITH ABSOLUTELY NO WARRANTY OF  
 * ANY KIND, EITHER IMPLIED OR EXPRESSED, INCLUDING, BUT NOT LIMITED  
 * TO, THE IMPLIED WARRANTIES OF FITNESS FOR ANY PURPOSE
 * AND MERCHANTABILITY.  
 *
 *    
 * NO LIABILITY  
 * ------------  
 * THE AUTHORS ASSUME ABSOLUTELY NO RESPONSIBILITY FOR ANY  
 * DAMAGES THAT MAY RESULT FROM THE USE,  
 * MODIFICATION OR REDISTRIBUTION OF THIS SOFTWARE, INCLUDING,  
 * BUT NOT LIMITED TO, LOSS OF DATA, CORRUPTION OR DAMAGE TO DATA,  
 * LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF SAVINGS,  
 * BUSINESS INTERRUPTION OR FAILURE TO INTEROPERATE WITH  
 * OTHER SOFTWARE, EVEN IF SUCH DAMAGES ARE FORESEEABLE.  
 */
package com.perpetual.application.collector.action.forward;

import com.perpetual.application.collector.log.util.ILoggableMessage;
import com.perpetual.application.collector.message.SyslogMessage;

/**
 * @author simon
 *
 */
public class ForwardedSyslogMessage implements ILoggableMessage
{
	private SyslogMessage syslogMessage;
	private long forwardTimeStamp;
	private ForwardHost forwardHost;
		
	public ForwardedSyslogMessage (ForwardHost forwardHost, long forwardTimeStamp,
			SyslogMessage syslogMessage)
	{
		this.forwardHost = forwardHost;
		this.forwardTimeStamp = forwardTimeStamp;
		this.syslogMessage = syslogMessage;
	}	
	
	public String getHost() {
		return getForwardHost().getHostName();
	}
	
	public ForwardHost getForwardHost() {
		return forwardHost;
	}
	public long getForwardTimeStamp() {
		return forwardTimeStamp;
	}

	public SyslogMessage getSyslogMessage() {
		return syslogMessage;
	}

	public void setForwardTimeStamp(long l) {
		forwardTimeStamp = l;
	}

}
