package com.extemp.cem.process;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.extemp.cem.process.utils.Constants;
import com.extemp.cem.process.utils.Utils;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;


public class ControlProcess implements iProcess,Runnable
{
	private String _processName;
	private String _processExecutable;
	private String _processExecutableStop;
	public String get_processExecutableStop() {
		return _processExecutableStop;
	}
	public void set_processExecutableStop(String _processExecutableStop) {
		this._processExecutableStop = _processExecutableStop;
	}
	private String _status;
	private HashMap _attributeList;
	private String _start_phrase,_end_phrase;
	private Utils _utils;
	private java.lang.Process _process;
	private iProcessController _controller;
	private String _pid;
	private String _command=null;
	private String _endPhrase_1 = "SUCCESS: The process with PID ";
	private String _endPhrase_2 = " has been terminated.";
	private boolean _firstTime=true;
	private String OS=null;
	private long _wait_for_end;
	
	//private static Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);
	
	
	
	public ControlProcess()
	{
		init();
	}
	public ControlProcess(String pProcessName, String pExec, String pStatus)
	{
		_processName=pProcessName;
		_processExecutable = pExec;
		_status = pStatus;
		
		init();
	}
	
	private void init()
	{
		_attributeList = new HashMap();		
	}
	
	public void setCommand(String pCommand) {
		// TODO Auto-generated method stub
		_command=pCommand;
		
	}
	@Override
	public int startProcess() {
		// TODO Auto-generated method stub
		String _executable = _processExecutable+" "+buildParamList();
		System.out.println("---Executing Process: "+_executable+" -----");
		
		try
		{
			_process = Runtime.getRuntime().exec(_executable);
			//InputStream _inputStream = _process.getInputStream();
			//InputStream _errorStream  = _process.getErrorStream();
			
			BufferedReader stdInput = new BufferedReader(new 
		             InputStreamReader(_process.getInputStream()),20000);

		     BufferedReader stdError = new BufferedReader(new 
		             InputStreamReader(_process.getErrorStream()));

		        // read the output from the command
		       // System.out.println("Here is the standard output of the Start process command:\n");
		        String s = null;
		      		        
		        while ((s = stdInput.readLine()) != null) {
		        	
		        	if(Utils.getInstance().is_log_to_console())
		        		System.out.println(s);
		           
		            if(s.contains(_start_phrase))
		            {
		            	if(_start_phrase.equals("") &&  _firstTime)
		            	//if(_start_phrase.equals(""))
		            	{
		            		System.out.println("waiting for process "+_processName+" to finish !");
		            		//java.lang.Thread.sleep(Constants.wait_time);
		            		java.lang.Thread.sleep(Utils.getInstance().get_wait_for_process_end_milis());
		            	}
		            	if(_firstTime)
		            	{
			            	_status = Constants.status_started; 
			            	 _pid = getPid(_process);
					        _controller.notifyProcessStarted(_processName,_pid);
					        System.out.println("@@@@@@ "+_processName +"(pid="+_pid+") has Started @@@@@@ !");
					        _firstTime=false;
		            	}
				      
				       //break;		            	
		            }
		        }
		       
		        // read any errors from the attempted command
		       
		        System.out.println("Here is the standard error of the command for "+_processName+"(if any):\n");
		        while ((s = stdError.readLine()) != null) {
		            System.out.println("### "+s);
		        }
		        
			
					
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		return 0;
	}
	
	public int stopProcess() {
		// TODO Auto-generated method stub
		String _killCommand=null;
		String _endPhrase=null;
		
		try
		{
			if(_processExecutableStop !=null && ! _processExecutableStop.equals(""))
			{
				_killCommand = _processExecutableStop;			
			}
			else
			{
			
				if(OS.equals("Win"))				
					_killCommand = "taskkill /F /PID "+_pid;
				if(OS.equals("UX"))
					_killCommand = "kill -9 "+_pid;			
			}
						
				
			System.out.println("Executing: "+_killCommand);
			Process _killProcess = Runtime.getRuntime().exec(_killCommand);
			
			if(_end_phrase==null || _end_phrase.equals("") || _end_phrase.equals("NA"))
			{				
				try
				{
					System.out.println("Waiting to terminate "+_processName+" @ PID: "+_pid);
					Thread.sleep(_wait_for_end);
					
				}
				catch(InterruptedException excp)
				{
					excp.printStackTrace();
				}
				_controller.notifyProcessStopped(_processName,_pid);
			}
			
			BufferedReader stdInput = new BufferedReader(new 
	             InputStreamReader(_killProcess.getInputStream()));	
									
			BufferedReader stdError = new BufferedReader(new 
	             InputStreamReader(_killProcess.getErrorStream()));

	        // read the output from the command
	       // System.out.println("Here is the standard output of the Stop process command:\n");
							
		    String s = null;
	        while ((s = stdInput.readLine()) != null) {
	        	
	        	if(Utils.getInstance().is_log_to_console())
	        	{
	        		System.out.println(s);
	        	}
	            
	            if(s.contains(_end_phrase))
	            {
	            	
	            	//_status = Constants.status_stopped; 
			        _controller.notifyProcessStopped(_processName,_pid);
			        System.out.println("@@@@@@ "+_processName +"(pid="+_pid+") has Stopped @@@@@@ !");
			        //break;		
	            }
	        }
	        _status = Constants.status_stopped; 
	        // read any errors from the attempted command
	        /*
	        System.out.println("Here is the standard error of the command (if any):\n");
	        while ((s = stdError.readLine()) != null) {
	            System.out.println("### "+s);
	        }
	        */

		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		return 0;
	}
	
	private String buildParamList()
	{
		String _retValue="";
		
		Set _keySet = _attributeList.keySet();
		Iterator<String> _it = _keySet.iterator();
		while(_it.hasNext())
		{
			String _parm = _it.next();
				_retValue+=_parm+" "+(String)_attributeList.get(_parm)+" ";
		}
		
		return _retValue;
	}
	
	public void run() 
	{
		_firstTime=true;
		try
		{
			if(_command.equals(Constants.start_command))			
			{
					if(_status.equals(Constants.status_stopped))
					{
						System.out.println("---Starting Process: "+_processName);
						startProcess();				
						_pid=getPid(_process);
					}
					else
					{
						System.out.println("process "+_pid+" is already started !");
						_controller.notifyProcessStarted(_processName, _pid);
					}
					java.lang.Thread.sleep(Constants.wait_time);
			}
			else if(_command.equals(Constants.stop_command))			
			{
				if(_status.equals(Constants.status_started))
				{
					System.out.println("---Stopping Process: "+_processName);
					stopProcess();
				}
				else
				{
					System.out.println("process "+_pid+" is already stopped !");	
					_controller.notifyProcessStopped(_processName, _pid);
				}
			}
			else if(_command.equals(Constants.restart_command))
			{
				if(_status.equals(Constants.status_stopped))
				{
					startProcess();				
					_pid=getPid(_process);
				}
				else
				{
					stopProcess();
					startProcess();				
					_pid=getPid(_process);
				}
				java.lang.Thread.sleep(Constants.wait_time);
			}
			
		}
		catch(Exception pExcp)
		{
				pExcp.printStackTrace();
		}
		
		
	}
	private String  getPid(Process proc) 
	{
	    Field f;
	    String _pid=null;

	    if (Platform.isWindows()) {
	        try {
	        	OS="Win";
	            f = _process.getClass().getDeclaredField("handle");
	            f.setAccessible(true);
	            f.get(proc);
	            int pid = Kernel32.INSTANCE.GetProcessId(new HANDLE(new Pointer((Long) f.get(proc))));	           
	   	        _pid= Integer.toString(pid);
	            System.out.println("Child process pid:"+pid);
	        } catch (Exception ex) {	
	            ex.printStackTrace();
	        }
	    } else if (Platform.isLinux()) {
	        try {
	        	OS="UX";
	        	f = proc.getClass().getDeclaredField("pid");	           
	            f.setAccessible(true);
	            int pid = (Integer) f.get(proc);
	            _pid= Integer.toString(pid);
	            System.out.println("Child process pid:"+pid);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    else{}
	    return _pid;
	}
	//------------------------------------
	public String get_processName() {
		return _processName;
	}

	public void set_processName(String _processName) {
		this._processName = _processName;
	}

	public String get_processExecutable() {
		return _processExecutable;
	}

	public void set_processExecutable(String _processExecutable) {
		this._processExecutable = _processExecutable;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}
	
	@Override
	public int startProcess(String pProcessName, String pProcessPath,
			String[] pArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int stopProcess(String pProcessName) {
		// TODO Auto-generated method stub
		return 0;
	}
	public HashMap get_attributeList() {
		return _attributeList;
	}
	public void set_attributeList(HashMap _attributeList) {
		this._attributeList = _attributeList;
	}
	public String get_start_phrase() {
		return _start_phrase;
	}
	public void set_start_phrase(String _start_phrase) {
		this._start_phrase = _start_phrase;
	}

	public String get_end_phrase() {
		return _end_phrase;
	}
	public void set_end_phrase(String _end_phrase) {
		this._end_phrase = _end_phrase;
	}
	@Override
	public int stopProcess(long pProcessId) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setController(Controller pController) {
		// TODO Auto-generated method stub
		_controller = pController;
		
	}
	public long get_wait_for_end() {
		return _wait_for_end;
	}
	public void set_wait_for_end(long _wait_for_end) {
		this._wait_for_end = _wait_for_end;
	}
	
	
	
	
	
	

}
