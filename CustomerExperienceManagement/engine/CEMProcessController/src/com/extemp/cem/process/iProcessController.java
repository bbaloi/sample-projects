package com.extemp.cem.process;

import java.util.HashMap;

public interface iProcessController
{
	public int startAllProcesses(HashMap pProcessMap);	
	public int stopAllProcesses(HashMap pProcessMap);
	public int restartAllProcesses(HashMap pProcessMap);
	public int notifyProcessStarted(String pProcessName,String pPid);
	public int notifyProcessStopped(String pProcessName,String pPid);
	public int notifyProcessRestarted(String pProcessName,String pPid);
	
}
