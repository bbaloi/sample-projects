package com.tibco.terr.events.as;

import java.util.ArrayList;
import java.util.HashMap;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.space.KeyDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.Tuple;
import com.tibco.as.space.browser.BrowserDef;
import com.tibco.as.space.event.PutEvent;
import com.tibco.terr.events.TERRController;
import com.tibco.terr.events.util.Constants;

public class AlgaeSpaceExplorer extends SpaceExplorer
{
	
	
	public AlgaeSpaceExplorer(Metaspace pMSpace, String pSpaceName,TERRController pTerrController)
	{
		
		super(pMSpace, pSpaceName,pTerrController);	
		 _columnNames = new String [] {"season","size","speed","mxPH","mn02","C1","NO3","NH4","oP04","PO4","Ch1a","a1","a2","a3","a4","a5","a6","a7"};
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
		
		try 
		{
			System.out.println("Creating space: "+_spaceName);
			SpaceDef 	spaceDef = SpaceDef.create(_spaceName);
	        // Put the field definitions into the space definition
	        spaceDef.putFieldDef(FieldDef.create("key", FieldType.STRING));
	        spaceDef.putFieldDef(FieldDef.create("season", FieldType.STRING).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("size", FieldType.STRING).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("speed", FieldType.STRING).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("mxPH", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("mn02", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("C1", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("NO3", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("NH4", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("oP04", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("PO4", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("Ch1a", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a1", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a2", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a3", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a4", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a5", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a6", FieldType.DOUBLE).setNullable(true));
	        spaceDef.putFieldDef(FieldDef.create("a7", FieldType.DOUBLE).setNullable(true));
	        
	               
	        // defines the key fields, by default the key index is of type HASH add setIndexType(IndexType.TREE) to the KeyDef below to change it to TREE type
	        spaceDef.setKeyDef(KeyDef.create().setFieldNames("key").setIndexType(IndexType.HASH));
	        // If you want to create indexes on fields, add them using: spaceDef.addIndexDef(IndexDef.create("index_name").setFieldNames("field_name","field_name2").setIndexType(IndexType.TREE or HASH));
	        // If you want fault-tolerance through replication, add .setReplicationCount(count) to the SpaceDef
	        spaceDef.setReplicationCount(1);
	        _metaspace.defineSpace(spaceDef);	
	                
	        _space = _metaspace.getSpace(_spaceName);
		}
		catch(ASException pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	protected ArrayList buildDataFrameLine(Tuple tuple)
	{
		 ArrayList<Object> _dataFrameLine = new ArrayList<Object>();
		 
		 String _season = tuple.getString("season");
    	 String _size = tuple.getString("size");
    	 String _speed = tuple.getString("speed");
    	 Double _mxPH = tuple.getDouble("mxPH");
    	 Double _mn02 = tuple.getDouble("mn02");	        	 
    	 Double _C1 = tuple.getDouble("C1");
    	 Double _NO3 = tuple.getDouble("NO3");
    	 Double _NH4 = tuple.getDouble("NH4");
    	 Double _oP04 = tuple.getDouble("oP04");
    	 Double _PO4 = tuple.getDouble("PO4");
    	 Double _Ch1a = tuple.getDouble("Ch1a");
    	 Double _a1 = tuple.getDouble("a1");
    	 Double _a2 = tuple.getDouble("a2");
    	 Double _a3 = tuple.getDouble("a3");
    	 Double _a4 = tuple.getDouble("a4");
    	 Double _a5 = tuple.getDouble("a5");
    	 Double _a6 = tuple.getDouble("a6");
    	 Double _a7 = tuple.getDouble("a7");    	 
    	
    	 _dataFrameLine.add(_season);
    	 _dataFrameLine.add(_size);
    	 _dataFrameLine.add(_speed);
    	 _dataFrameLine.add(_mxPH);
    	 _dataFrameLine.add(_mn02);
    	 _dataFrameLine.add(_C1);
    	 _dataFrameLine.add(_NO3);
    	 _dataFrameLine.add(_NH4);
    	 _dataFrameLine.add(_oP04);
    	 _dataFrameLine.add(_PO4);
    	 _dataFrameLine.add(_Ch1a);
    	 _dataFrameLine.add(_a1);
    	 _dataFrameLine.add(_a2);	        	 
    	 _dataFrameLine.add(_a3);
    	 _dataFrameLine.add(_a4);
    	 _dataFrameLine.add(_a5);
    	 _dataFrameLine.add(_a6);
    	 _dataFrameLine.add(_a7);
    	 
    	// String _retStr = _season+","+_size+","+_speed+","+_mxPH+","+_mn02+","+_C1+","+_NO3+","+_NH4+","+_oP04+","+_PO4+","+_Ch1a+","+_a1+","+_a2+","+_a3+","+_a4+","+_a5+","+_a6+","+_a7;
    	 //System.out.println(pRowNum+")"+_retStr);
    	 return _dataFrameLine;
		
	}
	
	protected void generateModel()
	{
		_terrController.generateModel();
		
	}
	protected  void handlePutEvent(PutEvent pEvent)
	{
		String _spaceName=pEvent.getSpaceName();
		System.out.println("Got PutEvent for space:"+_spaceName);
		
		
	}
	
	
}
