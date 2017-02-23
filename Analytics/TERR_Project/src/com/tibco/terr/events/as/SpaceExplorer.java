package com.tibco.terr.events.as;

import java.util.ArrayList;

import com.tibco.as.space.ASException;

import com.tibco.as.space.Metaspace;
import com.tibco.as.space.Space;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.Tuple;
import com.tibco.as.space.browser.Browser;
import com.tibco.as.space.browser.BrowserDef;
import com.tibco.as.space.event.PutEvent;
import com.tibco.as.space.event.SeedEvent;
import com.tibco.as.space.listener.PutListener;
import com.tibco.as.space.listener.SeedListener;
import com.tibco.terr.events.TERRController;
import com.tibco.terr.events.util.Utils;

public abstract class SpaceExplorer implements Runnable,PutListener,SeedListener
{
	protected Metaspace _metaspace;
	protected String _metaspaceName;
	protected String _spaceName;
	protected Space _space;
	protected Browser _browser;
	protected BrowserDef _browserDef;
	protected boolean _firstLoad=true;
	protected TERRController _terrController;
	protected ArrayList _dataFrameData;
	protected String [] _columnNames;
	protected boolean _buildModel;
	
	
	public SpaceExplorer(Metaspace pMSpace, String pSpaceName,TERRController pTerrController)
	{
		_metaspace = pMSpace;
		_spaceName=pSpaceName;
		_metaspaceName=_metaspace.getName();
		_terrController = pTerrController;
		init();
	}
			
	protected abstract void init();
	protected abstract ArrayList buildDataFrameLine(Tuple tuple);
	protected abstract void generateModel();
	protected abstract void handlePutEvent(PutEvent arg0);
	protected abstract void createSpace();
	

	public void run()
	{
			if(_firstLoad)
			{
				if(_buildModel)
				{
					//1.load the innitial
					createDataFrame();				
					//2.generate model	
					generateModel();
				}
				_firstLoad=false;
			}
			else
			{
				//wait top be notified by Listener that there is a new reocrd
				//read of a queue for mesages to arrive
				while(true)
				{
					//1) wait on new record to arrive from AS
					//2) predict value of record based on model
				}
			}				
	}
	 public void getSpaceContents(String pQuery)
	    {
	        try
	        {
	          
	            if(pQuery.equals(""))
	            	_browser = _space.browse(BrowserDef.BrowserType.GET,
		            		BrowserDef.create().setTimeScope(BrowserDef.TimeScope.ALL).setPrefetch(BrowserDef.PREFETCH_ALL));

	            else
	            	_browser = _space.browse(BrowserDef.BrowserType.GET,
	            		BrowserDef.create().setTimeScope(BrowserDef.TimeScope.ALL).setPrefetch(BrowserDef.PREFETCH_ALL), pQuery);
	            
	        }
	        catch (ASException as)
	        {
	            as.printStackTrace();
	        }
	 }
	protected void createDataFrame()
	{		
			long  _spaceSize;
			int _cntr;
	         Tuple _tuple;
	         _dataFrameData = new ArrayList();         
	         
	         try
	         {
	        	
	        	 _spaceSize = _space.size();
	        	 System.out.println("Space size="+_spaceSize);
	        	 getSpaceContents("");
	        		        	
	        	 for(_cntr=1;_cntr<_spaceSize;_cntr++)
	        	 {
	        		 Tuple _key = Tuple.create();
	        		 _key.put("key", Integer.toString(_cntr));
	        		  _tuple = _space.get(_key);
	        		  _dataFrameData.add(buildDataFrameLine(_tuple));
	        		  
	        	 }
	        	 
	        	 /*
		         while ((_tuple=_browser.next()) != null)		        	 
		         {
		        	 	 	 
		        	  _dataFrameData.add(buildDataFrameLine(_tuple));			        	  
		             	             
		         }
		         */
		         
		         //Utils.getInstance().displaySpace(_dataFrameData);
		         
		         _terrController.loadDataFrame(_columnNames,_dataFrameData);

	         }
	         catch(ASException pExcp)
	         {
	        	 pExcp.printStackTrace();
	         }
	         
	         
		}
	public void registerEventListener()
	{
		try
		{
			_space.listen(this);
		}
		catch(ASException pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	
	public void onPut(PutEvent arg0) 
	{
		// TODO Auto-generated method stub
		//System.out.println("In onPut for event on space "+_space.getName());
		
		handlePutEvent(arg0);
		
	}

	@Override
	public void onSeed(SeedEvent arg0) 
	{
		// TODO Auto-generated method stub
		System.out.println("In onSeed for event on space "+_space.getName());
		
	}
}
