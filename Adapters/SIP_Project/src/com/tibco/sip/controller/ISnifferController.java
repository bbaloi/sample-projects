package com.tibco.sip.controller;

import java.util.List;

public interface ISnifferController 
{
	public void startPacketProcessors(List pInterfaces);
	public void startRVTransformer();
}
