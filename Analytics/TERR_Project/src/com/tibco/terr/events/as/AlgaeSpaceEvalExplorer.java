package com.tibco.terr.events.as;

import java.util.ArrayList;

import com.tibco.as.space.ASException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.KeyDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.Tuple;
import com.tibco.as.space.FieldDef.FieldType;
import com.tibco.as.space.IndexDef.IndexType;
import com.tibco.as.space.event.PutEvent;
import com.tibco.terr.events.TERRController;

public class AlgaeSpaceEvalExplorer extends SpaceExplorer
{

	public AlgaeSpaceEvalExplorer(Metaspace pMSpace, String pSpaceName,TERRController pTerrController) 
	{
		super(pMSpace, pSpaceName, pTerrController);
		 _columnNames = new String [] {"season","size","speed","mxPH","mn02","C1","NO3","NH4","oP04","PO4","Ch1a"};
		 							  
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init()
	{
		// TODO Auto-generated method stub
		try
		{
			_space = _metaspace.getSpace(_spaceName);
			_buildModel=false;
			//getSpaceContents("");
		}
		catch(ASException pExcp)
		{
			System.out.println(pExcp.getMessage());
			createSpace();    	
			
		}
		
	}

	@Override
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
	   		   	 
	   	 return _dataFrameLine;
	}

	@Override
	protected void generateModel() {
		// TODO Auto-generated method stub
		
	}

	protected  void handlePutEvent(PutEvent pEvent)
	{
			String _spaceName=pEvent.getSpaceName();
			System.out.println("Got PutEvent for space:"+_spaceName);
			 ArrayList _dataFrameData = new ArrayList();   
			 
			if(_spaceName.equals(_spaceName))
			{
				Tuple _tuple = pEvent.getTuple();
				ArrayList _line = buildDataFrameLine(_tuple);				
				_dataFrameData.add(buildDataFrameLine(_tuple));
				String [] _predictions = _terrController.predict(_columnNames,_dataFrameData);
				
				for(int i=0;i<_predictions.length;i++)
				{
					System.out.println(i+") "+_predictions[i]);
				}
			}
	}

	@Override
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

}
