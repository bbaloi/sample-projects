package com.tibco.terr.events;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import com.tibco.terr.events.util.ShutdownThread;
import com.tibco.terr.events.util.Utils;

public class EngineStarter 
{
	private MainController _mainController;
	private String _propertyFile;
	private Properties _props;
	private Utils _utils;
	
	public EngineStarter(String [] pArgs)
	{
		init(pArgs);
		_mainController = new MainController();
	}
	
	public static void main(String [] args)
	{
		
		EngineStarter _engine = new EngineStarter(args);
		Runtime.getRuntime().addShutdownHook(new ShutdownThread(_engine));
		_engine.start();		
	}
	public void start()
	{
		//need:
		//1) Data Loader Thread (innitially)
		_mainController.startController();
		//2) Model builder Thread - wakes up x minutes  - or is invoked to generate 
		//3) predict with live data - this is on an event by event basis
	}
	public void stop()
	{
		_mainController.stopController();
	}
	
	private void init(String [] pArgs)
	{
		if(pArgs.length<2)
		{
			System.out.println("Syntax is EngineStarter -props <Porperty File Path>");
			System.exit(1);
		}
		_propertyFile = pArgs[1];
		System.out.println("PropetyFile:"+_propertyFile);
		_utils = Utils.getInstance();
		_props = new Properties();
		try
		{
			_props.load(new FileReader(_propertyFile));
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		_utils.setProperties(_props);		
		
	}

}
