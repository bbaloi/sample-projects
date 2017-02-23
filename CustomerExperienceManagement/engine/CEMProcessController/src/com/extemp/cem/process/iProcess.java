package com.extemp.cem.process;

public interface iProcess 
{
	public int startProcess();
	public int stopProcess();
	public int startProcess(String pProcessName, String pProcessPath, String [] pArgs);
	public int stopProcess(String pProcessName);
	public int stopProcess(long pProcessId);
	public void setController(Controller pController);
	public void setCommand(String pCommand);

}
