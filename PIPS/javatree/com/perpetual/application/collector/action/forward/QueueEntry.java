package com.perpetual.application.collector.action.forward;

import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.application.collector.throttle.IQueueEntry;

public class QueueEntry implements IQueueEntry {

	private SyslogMessage syslogMessage;
	private int status;
		
	public QueueEntry (SyslogMessage syslogMessage)
	{
		this.syslogMessage = syslogMessage;
		this.status = UNPROCESSED;
	}
	
	public SyslogMessage getSyslogMessage() {
		return this.syslogMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int i) {
		status = i;
	}
	
	public long getTimeStamp() {
		return getSyslogMessage().getSystemTimestamp();
	}
	
	public String getId() {
		return getSyslogMessage().getPacketId();
	}
}