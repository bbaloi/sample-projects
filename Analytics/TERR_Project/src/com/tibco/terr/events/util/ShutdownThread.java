package com.tibco.terr.events.util;

import com.tibco.terr.events.EngineStarter;

public class ShutdownThread extends Thread
{
	
	private EngineStarter _engineStarter;
	
	public ShutdownThread(EngineStarter pStarter)
	{
		_engineStarter=pStarter;
		
	}
	public void run()
	{
		System.out.println("ShutingDown TerrEngine !");
		_engineStarter.stop();
		System.exit(1);
	}

}
