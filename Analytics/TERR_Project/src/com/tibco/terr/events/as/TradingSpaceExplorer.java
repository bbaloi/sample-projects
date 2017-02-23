package com.tibco.terr.events.as;

import java.util.ArrayList;

import com.tibco.as.space.ASException;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.Tuple;
import com.tibco.as.space.event.PutEvent;
import com.tibco.terr.events.TERRController;

public class TradingSpaceExplorer extends SpaceExplorer
{

	public TradingSpaceExplorer(Metaspace pMSpace, String pSpaceName,TERRController pTerrController) {
		super(pMSpace, pSpaceName,pTerrController);
		// TODO Auto-generated constructor stub
	}

	protected void init()
	{
		try
		{
			_space = _metaspace.getSpace(_spaceName);
			_buildModel=true;	  			
			
			//getSpaceContents("");
		}
		catch(ASException pExcp)
		{
			System.out.println(pExcp.getMessage());
			createSpace();  
		}
	}

	protected void createSpace()
	{
		System.out.println("Creating space: "+_spaceName);
		
	}
	@Override
	protected void createDataFrame() 
	{
		// TODO Auto-generated method stub
		
	}
	protected ArrayList buildDataFrameLine(Tuple tuple)
	{
		return null;
	}
	protected void generateModel()
	{
		
	}
	protected  void handlePutEvent(PutEvent arg0)
	{
		
	}
}
