package com.extemp.cem;

import org.kie.api.runtime.rule.EntryPoint;

import com.extemp.cem.events.SportsVenueMobileEvent;
import com.extemp.cem.tests.EventGeneratorTestHarness;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.KBUtil;

public class CemMain
{
	private static String _propsFileName;
	
	public CemMain(String [] args)
	{		
		processArgs(args);
		init();
	}
	
	public static final void main(String [] args)
	{
		CemMain _main = new CemMain(args);
		
		//CEMUtil.getInstance().simulateBaiscEvents();
		EventGeneratorTestHarness _testHarness = new EventGeneratorTestHarness();
		
		_testHarness.loadEvents();
		
	
        
     
		
	}
	private static void  processArgs(String [] args)
	{
		
		if(args.length<2)
		{
			System.out.println("command line is cem.sh -properties <propertye file name>");
			System.exit(0);
		}
		
		_propsFileName = args[1];
		//System.out.println("Props filename:"+_propsFileName);
	
	}
	private void init()
	{
		CEMUtil.getInstance().loadProperties(_propsFileName);
	
	}
	

	
}
