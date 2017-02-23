package com.extemp.cem.process;

import java.util.HashMap;

import com.extemp.cem.process.utils.Utils;

public class ProcessControllerMain 
{
	private static HashMap _processMap;
	private static String _propsFileLocation;
	private static Controller _controller;
	private static ProcessControllerMain _instance=null;
	
	public ProcessControllerMain()
	{		
		init();
	}
	public ProcessControllerMain(String [] args )
	{		
		init(args);
	}
	public static void main(String [] args)
	{
		try
		{
			ProcessControllerMain _main = new ProcessControllerMain(args);
			//_main.loadConfig(args);
		
			//_controller.startAllProcesses(_processMap);		
			//java.lang.Thread.sleep(40000);			
			//_controller.stopAllProcesses(_processMap);
			while(true);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
	}
	public static void loadConfig(String [] args)
	{
		
		init(args);
	}
	private static void init(String [] pArgs)
	{
		Utils _utils = Utils.getInstance();
		
		if (pArgs.length<2)
		{
			System.out.println(" process execution: ProcessControllerMain -propFile <property file name - full path>");
			System.exit(1);
		}
		else
		{
			_propsFileLocation = pArgs[1];
			
			_processMap = _utils.loadPropertiesFile(_propsFileLocation);
			_utils.displayConfig(_processMap);
		}
		_controller = new Controller(_processMap);
		_utils.get_jms_controller().init();
		_utils.get_jms_controller().registerListener(null);
		_controller.setJMSContorller(_utils.get_jms_controller());
		_utils.get_jms_controller().set_controller(_controller);
	}
	
	private static void init()
	{
		//_controller = new Controller(_processMap);
	}
	public static void startProcessList(String pConfigFile)
	{
		if(_instance==null)
			_instance = new ProcessControllerMain();
		Utils _utils = Utils.getInstance();
		_processMap = _utils.loadPropertiesFile(_propsFileLocation);
			if(_controller==null)
				_controller = new Controller(_processMap);
			
			_controller.startAllProcesses(_processMap);
		
	}
	public static void stopProcesList(String pConfigFile)
	{
		if(_instance==null)
			_instance = new ProcessControllerMain();
		Utils _utils = Utils.getInstance();
		_processMap = _utils.loadPropertiesFile(_propsFileLocation);
		if(_controller==null)
			_controller = new Controller(_processMap);
		
		_controller.stopAllProcesses(_processMap);
		
	}
	public static void restartProcesList(String pConfigFile)
	{
		if(_instance==null)
			_instance = new ProcessControllerMain();
		Utils _utils = Utils.getInstance();
		_processMap = _utils.loadPropertiesFile(_propsFileLocation);
		if(_controller==null)
			_controller = new Controller(_processMap);
		
		_controller.stopAllProcesses(_processMap);
		
	}


}
