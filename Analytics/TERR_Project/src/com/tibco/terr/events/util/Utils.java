package com.tibco.terr.events.util;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.tibco.as.space.Metaspace;
import com.tibco.as.space.Space;
import com.tibco.terr.events.TERRController;
import com.tibco.terr.events.as.AlgaeSpaceEvalExplorer;
import com.tibco.terr.events.as.AlgaeSpaceExplorer;
import com.tibco.terr.events.as.SpaceExplorer;
import com.tibco.terr.events.as.TradingSpaceExplorer;
import com.tibco.terr.events.util.Constants;
import com.tibco.terr.TerrByte;
import com.tibco.terr.TerrData;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrInteger;
import com.tibco.terr.TerrString;
import com.tibco.terr.TerrDataFrame;

public class Utils 
{
	private static Utils _instance=null;
	private String _asDiscovery;
	private String _asListen;
	private String _asRemoteListen;
	private String _asRemoteDiscovery;
	private String _asMemberName;	
	private String _terrLibPath;
	private String _terrWorkingDir;	
	private String _terrDataCols;	
	private ArrayList _terrDepVars;
	private int _threadPoolSize=0;	
	private HashMap<String, ArrayList<String>> _asMetaSpaceMap;
	private HashMap  <String,String> _spaceEvalMap;

	
	
	public Utils()
	{		
		_asMetaSpaceMap = new HashMap();
		_spaceEvalMap = new HashMap();
	}
	public static Utils getInstance()
	{
		if(_instance==null)
			_instance=new Utils();
		
		return _instance;
	}
	
	public void setProperties(Properties pProps)
	{
		String _metaspaces = pProps.getProperty(Constants.asMetaspaces);
		String _spaces = pProps.getProperty(Constants.asSpaces);
		addToMetaspaceList(_metaspaces,_spaces);
		_asDiscovery = pProps.getProperty(Constants.asDisovery);
		_asListen = pProps.getProperty(Constants.asListen);
		_asRemoteListen = pProps.getProperty(Constants.asRemoteListen);
		_asRemoteDiscovery = pProps.getProperty(Constants.asRemoteDiscovery);
		_asMemberName=pProps.getProperty(Constants.asMemberName);
		_terrLibPath=pProps.getProperty(Constants.terrLibPath);
		_terrWorkingDir = pProps.getProperty(Constants.terrWorkingDir);
		_terrDepVars = getDepVars(pProps);
		_terrDataCols = pProps.getProperty(Constants.terrDataCols);
		
	}
	
	private void addToMetaspaceList(String pMetaspaces, String pSpaces)
	{
		StringTokenizer _metaspaceTokens=null;
		StringTokenizer _spaceTokens=null;
	
		int _threadCounter=0;
		
		if(!pMetaspaces.contains("|"))
		{
			ArrayList<String> _spaceList = new ArrayList<String>();
				//you only have one metaspace
				String _metaspace = pMetaspaces;			
				if(!pSpaces.contains(","))
				{
					//you only hve one space
					String _space = pSpaces;
					_spaceList.add(_space);
				}
				else
				{
					
					//you have one metaspaces with mulitple spaces
					_spaceTokens = new StringTokenizer(pSpaces,",");
					while(_spaceTokens.hasMoreTokens())
					{
						String _space =_spaceTokens.nextToken();
						_spaceList.add(_space);
						_threadCounter++;
						updateSpaceEvalMap(_space);
					}				
				_asMetaSpaceMap.put(_metaspace, _spaceList);	
			}
			
		}
		else
		{
				_metaspaceTokens = new StringTokenizer(pMetaspaces,"|");		
				_spaceTokens = new StringTokenizer(pSpaces,"|");				
				
				while(_metaspaceTokens.hasMoreTokens() && _spaceTokens.hasMoreTokens())
				{
					String _metaspace = _metaspaceTokens.nextToken();
					String _spaces = _spaceTokens.nextToken();
					StringTokenizer _spaceTokens2 = new StringTokenizer(_spaces,",");
					ArrayList<String> _list = new ArrayList<String>();
					while(_spaceTokens2.hasMoreTokens())
					{
						String _space =_spaceTokens2.nextToken();
						_list.add(_space);
						_threadCounter++;
					}
					_asMetaSpaceMap.put(_metaspace, _list);		
				}
			
		}		
		_threadPoolSize = _threadCounter;
		
	}
	public synchronized SpaceExplorer createSpaceFactory(Metaspace pMs, String _spaceName)
	{
		SpaceExplorer _explorer=null;
		TERRController _terrCtrl = new TERRController(pMs.getName(),_spaceName);
		
		if(_spaceName.equals(Constants.spaceAlgae))
		{
			System.out.println("Building a Algae Space explorer !");
			_explorer = new AlgaeSpaceExplorer(pMs, _spaceName,_terrCtrl);
			_explorer.registerEventListener();
			
		}
		else if(_spaceName.equals(Constants.spaceAlgaeEval))
		{
			System.out.println("Building a Algae Space Eval explorer !");
			_explorer = new AlgaeSpaceEvalExplorer(pMs, _spaceName,_terrCtrl);
			_explorer.registerEventListener();
			
		}
		else if(_spaceName.equals(Constants.spaceTrading))
		{
			System.out.println("Building a Trading Space explorer !");
			_explorer = new TradingSpaceExplorer(pMs, _spaceName,_terrCtrl);
			//_explorer.registerEventListener();
		}			
		else
		{
			System.out.println("....Warning space '"+_spaceName+"' is not supported ! It will not be created !");
		}
		
		return _explorer;
	}
	private ArrayList getDepVars(Properties pProps)
	{
		ArrayList _retVal=new ArrayList();
		String _vars = pProps.getProperty(Constants.terrDepVars);
		StringTokenizer _strTkn = new StringTokenizer(_vars,",");
		while(_strTkn.hasMoreTokens())
		{
			_retVal.add(_strTkn.nextToken());
		}
		
		
		return _retVal;
	}
	public void displaySpace(ArrayList pList)
	{
		Iterator _it = pList.iterator();
		int _cntr=0;
		
		System.out.println("++++++++Space Data+++++++++");
		
		while(_it.hasNext())
		{
			ArrayList _cols = (ArrayList) _it.next();
			Iterator _colIt = _cols.iterator();
			String _row = new String();
			while(_colIt.hasNext())
			{
				Object _oval = _colIt.next();
				if(_oval instanceof String)
					_row+=(String) _oval+",";
				if(_oval instanceof Double)
					_row+=((Double)_oval).toString();
				if(_oval instanceof Integer)
					_row+=((Integer)_oval).toString();
				if(_oval instanceof Byte)
					_row+=((Byte)_oval).toString();
				
			}
			_cntr++;
			System.out.println(_cntr+")"+_row);
		}
		
	}
	public void displayDataFrame(TerrDataFrame pDF)
	{
		int len = pDF.length;
		System.out.println("DF has "+len+" records !");
		TerrData [] _terrData = pDF.data;
		TerrData _tmpObj=_terrData[0];
		String [] names = _tmpObj.names;
		String _outNames = new String();
		for(int x=0;x<names.length;x++)
			_outNames+=names[x]+",";
		System.out.println(_outNames);
		
		for (int i=0;i<_terrData.length;i++)
		{
			TerrData _obj = _terrData[i];		
			
		}
	}
	private void updateSpaceEvalMap(String pSpace)
	{
		if(!pSpace.contains("-"))
		{
			_spaceEvalMap.put(pSpace, "");
		}
		else
		{
			String _key = pSpace.substring(0, pSpace.indexOf("_"));
			String _val=_spaceEvalMap.get(_key);
			if(_val!="" || _val != null)
				_val = pSpace;
		}
			
	}
	
	public HashMap<String, ArrayList<String>> get_asMetaSpaceMap() {
		return _asMetaSpaceMap;
	}
	public String get_asListen() {
		return _asListen;
	}
	public String get_asDiscovery() {
		return _asDiscovery;
	}
	public String get_asRemoteListen() {
		return _asRemoteListen;
	}
	public String get_asRemoteDiscovery() {
		return _asRemoteDiscovery;
	}
	public String get_asMemberName() {
		return _asMemberName;
	}
	public int get_threadPoolSize() {
		System.out.println("# thread = "+_threadPoolSize);
		return _threadPoolSize;
	}
	
	public List get_terrDepVars() {
		return _terrDepVars;
	}
	public String get_terrLibPath() {
		return _terrLibPath;
	}
	public String get_terrWorkingDir() {
		return _terrWorkingDir;
	}
	public String get_terrDataCols() {
		return _terrDataCols;
	}
	public HashMap<String, String> get_spaceEvalMap() {
		return _spaceEvalMap;
	}
}
