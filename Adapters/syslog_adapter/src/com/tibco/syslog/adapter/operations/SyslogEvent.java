package com.tibco.syslog.adapter.operations;

import com.tibco.sdk.events.MEvent;
import com.tibco.syslog.adapter.AdapterSyslogVo;

public class SyslogEvent extends MEvent
{
	private AdapterSyslogVo lVo;

	public SyslogEvent(AdapterSyslogVo pVo)
	{
		super(pVo);
		lVo=pVo;
	}
	public AdapterSyslogVo getSyslogVo()
	{
		return lVo;
	}
}
