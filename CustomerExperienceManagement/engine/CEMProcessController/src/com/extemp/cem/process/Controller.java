package com.extemp.cem.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.extemp.cem.process.utils.Constants;

public class Controller implements iProcessController
{
	HashMap _processMap;
	private ExecutorService _threadPool;
	private JMSController _jms_controller;
	
	
	
	public Controller(HashMap pProcessMap)
	{
		_processMap = pProcessMap;
	}
	@Override
	public int startAllProcesses(HashMap pProcessMap) {
		// TODO Auto-generated method stub
		
		Set _keySet =  _processMap.keySet();				
		Iterator _it = _keySet.iterator();
		int _procLen = _keySet.size();
		
		_threadPool = Executors.newFixedThreadPool(_procLen);
		
		while(_it.hasNext())
		{
			String _key = (String)_it.next();
			ControlProcess _proc = (ControlProcess) _processMap.get(_key);
			_proc.setCommand(Constants.start_command);
			_proc.setController(this);			
			_threadPool.execute(_proc);
		}		
		return 0;
	}
	@Override
	public int stopAllProcesses(HashMap pProcessMap) {
		// TODO Auto-generated method stub
		
		Set _keySet =  _processMap.keySet();				
		Iterator _it = _keySet.iterator();
		int _procLen = _keySet.size();
		
		_threadPool = Executors.newFixedThreadPool(_procLen);
		
		while(_it.hasNext())
		{
			String _key = (String)_it.next();
			ControlProcess _proc = (ControlProcess) _processMap.get(_key);
			_proc.setCommand(Constants.stop_command);						
			_threadPool.execute(_proc);
		}		
		return 0;
	}
	@Override
	public synchronized int notifyProcessStarted(String pProcessName,String pPid) {
			
		//send EMS message notification
		String _msg=pProcessName+"(pid:"+pPid+") has been started !";
		_jms_controller.sendMessage(_msg);
		
		return 0;
	}
	@Override
	public synchronized int notifyProcessStopped(String pProcessName,String pPid) {
		// TODO Auto-generated method stub
		String _msg=pProcessName+"(pid:"+pPid+") has been stopped !";
		_jms_controller.sendMessage(_msg);
		//send EMS message notification	
		return 0;
	}
	public synchronized int notifyProcessRestarted(String pProcessName,String pPid) {
		// TODO Auto-generated method stub
		String _msg=pProcessName+"(pid:"+pPid+") has been restarted !";
		_jms_controller.sendMessage(pProcessName);
		return 0;
	}
	

	@Override
	public int restartAllProcesses(HashMap pProcessMap) {
		// TODO Auto-generated method stub
		
		Set _keySet =  _processMap.keySet();				
		Iterator _it = _keySet.iterator();
		int _procLen = _keySet.size();
		
		_threadPool = Executors.newFixedThreadPool(_procLen);
		
		while(_it.hasNext())
		{
			String _key = (String)_it.next();
			ControlProcess _proc = (ControlProcess) _processMap.get(_key);
			_proc.setCommand(Constants.restart_command);						
			_threadPool.execute(_proc);
		}		
		
		return 0;
	}
	
	
	public void setJMSContorller(JMSController pCtrl)
	{
		_jms_controller=pCtrl;
	}

}
