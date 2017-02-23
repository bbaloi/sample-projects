package com.tibco.sip.controller;

import com.tibco.sip.vo.SIPPacketVo;

public interface ISIPMessageQueue 
{
	public void sendMessage(SIPPacketVo pSipMsg);
	public SIPPacketVo getMessage();
	public int getQueueSize();
}
