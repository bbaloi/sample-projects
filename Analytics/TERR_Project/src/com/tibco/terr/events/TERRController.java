package com.tibco.terr.events;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.tibco.terr.TerrByte;
import com.tibco.terr.TerrData;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrFactor;
import com.tibco.terr.TerrInteger;
import com.tibco.terr.TerrString;
import com.tibco.terr.TerrDataFrame;
import com.tibco.terr.TerrJava;
import com.tibco.terr.events.util.Constants;
import com.tibco.terr.events.util.Utils;


public class TERRController implements ITERRController
{
	private TerrData _terrData;
	private TerrDataFrame _terrDataFrame;
	private Utils _utils=Utils.getInstance();
	private String _spaceName,_metaspaceName;
	private String _dfName;
	
	public TERRController(String pMetaspace,String pSpace)
	{
		_spaceName=pSpace;
		_metaspaceName=pMetaspace;		
		 _dfName = "df."+_metaspaceName+"."+_spaceName;
		init();
	}
	
	private void init() 
	{
			
		try
		{
			if(! TerrJava.isEngineRunning())
			{
				System.out.println("+++Starting TERR Engine++++ !");
				TerrJava.startEngine("FFInterface.SignalHandlersEnabled=TRUE");
				TerrJava.engineExecute(_utils.get_terrLibPath(),true);
				TerrJava.engineExecute("setwd('"+_utils.get_terrWorkingDir()+"')",true);	
				//TerrJava.engineExecute("setwd('"+_utils.get_terrWorkingDir()+"')", true);				
				//TerrJava.engineExecute("library(car)", true);
				//TerrJava.engineExecute("library(lattice)", true);
				//TerrJava.engineExecute("library(terrJava)", true);	
				//TerrJava.engineExecute("library(rpart)", true);					
				//TerrJava.engineExecute("library(randomForest)", true);				
			}
		
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
			//System.out.println(pExcp.getMessage());
			
		}
		
		
	}
	public TerrDataFrame loadDataFrameEval(String [] pColumns, ArrayList pData)
	{
		TerrDataFrame _terrDataFrame=null;
		
		int numColumns =pColumns.length;
		int numRows = pData.size();
		TerrData [] columnsArray = new TerrData[numColumns];
		
		
		Object [][] _prepMatrix = getTransposeDataMatrix(pData,numColumns, numRows);
		
		for(int _colCntr=0;_colCntr<numColumns;_colCntr++)
		{
									
			Object [] _col = _prepMatrix[_colCntr];
			TerrData _colData = getTERRData(_col);
			columnsArray[_colCntr]=_colData;
			
		}
		
		 _terrDataFrame=new TerrDataFrame(pColumns,columnsArray);
		 return _terrDataFrame;
		
	}
	public void loadDataFrame(String [] pColumns, ArrayList pData) 
	{
		// TODO Auto-generated method stub
			
		int numColumns =pColumns.length;
		int numRows = pData.size();
		TerrData [] columnsArray = new TerrData[numColumns];
		
		
		Object [][] _prepMatrix = getTransposeDataMatrix(pData,numColumns, numRows);
		
		for(int _colCntr=0;_colCntr<numColumns;_colCntr++)
		{
									
			Object [] _col = _prepMatrix[_colCntr];
			TerrData _colData = getTERRData(_col);
			columnsArray[_colCntr]=_colData;
			
		}
				
		_terrDataFrame = new TerrDataFrame(pColumns,columnsArray);
	}
	
	public void generateModel() 
	{
		try
		{
			TerrJava.setVariable(_dfName, _terrDataFrame);			
			//createFactors(_terrDataFrame);
			String _cleanDf = _dfName+".clean";
			TerrJava.evaluateInteractive(_cleanDf+"<-na.omit("+_dfName+")");	
			//TerrJava.engineExecute("clean.df",true);			
			List _list = _utils.get_terrDepVars();
			Iterator _it = _list.iterator();			
			TerrData _model = TerrJava.getVariable(_dfName);
			
			while(_it.hasNext())
			{
				String _var = (String)_it.next();
				String _dfVar = _dfName+"."+_var+".lm";
				//String _resp=TerrJava.evaluateInteractive(_dfVar+"<-lm("+_var+" ~ .,data=clean.df[,"+_utils.get_terrDataCols()+"])"); //lm.a1=lm(a1 ~ .,data=algae[,1:12])
				String _resp=TerrJava.engineExecute(_dfVar+"<-lm("+_dfName+"$"+_var+" ~ .,data="+_cleanDf+"[,"+_utils.get_terrDataCols()+"])",true); //lm.a1=lm(a1 ~ .,data=algae[,1:12])
				System.out.println(_resp);
				//String _resp=TerrJava.evaluateInteractive(_dfVar+"<-lm("+_var+" ~ .,data=df[,"+_utils.get_terrDataCols()+"])"); //lm.a1=lm(a1 ~ .,data=algae[,1:12])
				
				//String _result = TerrJava.evaluateInteractive(_dfFinal+"<-step("+_dfVar+")");
				//TerrJava.evaluateInteractive("lm.summary <- summary("+_dfVar+")");	
				//String _result = TerrJava.engineExecute("lm.summary <- summary("+_dfVar+")",true);
				//TerrJava.engineExecute("lm.summary",true);
				//System.out.println(_result);
				TerrJava.evaluateInteractive("df.coef <- "+_dfVar+"$coefficients");
				TerrData coef = TerrJava.getVariable("df.coef");
				System.out.println("-------var "+_dfVar+" model---------" );
				for (int i=0; i<coef.getLength(); i++)
				{
					System.out.println(coef.names[i]+" : "+((TerrDouble)coef).data[i]);
				
				}
						
			
			}
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}

	}
	
	private Object [][] getTransposeDataMatrix(ArrayList pFrame,int pColumns, int pRows)
	{
		//here we're basically transposing the matrix
		
		Object [][] _dataMatrix = new Object [pColumns][pRows];				
				
		for(int _rowCntr =0;_rowCntr<pRows;_rowCntr++)
		{			
			
			for(int _colCntr=0;_colCntr< pColumns;_colCntr++)
			{
					Object _item = ((ArrayList)(pFrame.get(_rowCntr))).get(_colCntr);					
					_dataMatrix[_colCntr][_rowCntr] = _item;						
			}						
		}
		
		
		
		return _dataMatrix;
	}
	private TerrData getTERRData(Object [] pCol)
	{
		TerrData _retVal=null;
		
		int _len = pCol.length;
		if(pCol[0] instanceof String)
		{
			String [] _colStr = new String[_len];
			for(int x=0;x<_len;x++)
			{
				_colStr[x] = (String) pCol[x];
			
			}
			//TerrString _newStr = new TerrString(_colStr);
			TerrFactor _newFactor =  TerrFactor.create(_colStr);
			//_retVal=_newStr;
			_retVal=_newFactor;
		}
		if(pCol[0] instanceof Double)
		{
			double [] _colDbl = new double[_len];
			for(int x=0;x<_len;x++)
			{
				_colDbl[x] = ((Double) pCol[x]).doubleValue();
			
			}
			TerrDouble _newDbl = new TerrDouble(_colDbl);
			_retVal=_newDbl;
		}
		if(pCol[0] instanceof Integer)
		{
			int [] _colInt = new int[_len];
			for(int x=0;x<_len;x++)
			{
				_colInt[x] = ((Integer) pCol[x]).intValue();
			
			}
			TerrInteger _newInt = new TerrInteger(_colInt);
			_retVal=_newInt;
		}
		if(pCol[0] instanceof Byte)
		{
			byte [] _colByte = new byte[_len];
			for(int x=0;x<_len;x++)
			{
				_colByte[x] = ((Byte) pCol[x]).byteValue();
			
			}
			TerrByte _newByte = new TerrByte(_colByte);
			_retVal=_newByte;
		}
			
				
		return _retVal;
	
	}

	@Override
	public void loadDataFrame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String [] predict(String [] pColumns, ArrayList pData) 
	{
		// TODO Auto-generated method stub
		String _lDfName=null;
		List _depVars = _utils.get_terrDepVars();
		String [] _results = new String [_depVars.size()];
		Iterator _it = _depVars.iterator();
		int _varCntr=0;
	
		Object [] _data = pData.toArray();
		TerrDataFrame _evalDataFrame = loadDataFrameEval(pColumns, pData);		
		
		try
		{
			if(!_spaceName.contains("_"))
				_lDfName=_dfName;
			else
				_lDfName =  "df."+_metaspaceName+"."+_spaceName.substring(0, _spaceName.indexOf("_"));
			String _newRecordName=_lDfName+".new.record";
			TerrJava.setVariable(_newRecordName, _evalDataFrame);
			TerrJava.engineExecute(_newRecordName,true);	
			
			while(_it.hasNext())
			{				
				String _var = (String)_it.next();
				String _model = _lDfName+"."+_var+".lm";
				TerrData _tModel = TerrJava.getVariable(_model);
				
				
				String _terrVarPred = _lDfName+"."+_var+".predict";
				System.out.println("---Executing: "+_terrVarPred+" <- predict("+_model+","+_newRecordName+")");
				//TerrJava.engineExecute(_model,true);
				//String _result = TerrJava.evaluateInteractive(_terrVarPred+" <- predict("+_model+",df.new.record)");		
				String _result = TerrJava.engineExecute(_terrVarPred+"<-predict("+_model+","+_newRecordName+")",true);	
				//String _result = TerrJava.engineExecute(_terrVarPred+"<-predict("+_model+",df.new.record[,4:11])",true);
				System.out.println("Prediction Result:"+_result);
				TerrJava.engineExecute(_terrVarPred,true);	
				
				TerrData _predVar = TerrJava.getVariable(_terrVarPred);				
				
				_results[_varCntr]= Double.toString((((TerrDouble)_predVar).data[0]));
				_varCntr++;
				
				/*
				for (int i=0; i<_predVar.getLength(); i++)
				{
					System.out.println(_predVar.names[i]+" : "+((TerrDouble)_predVar).data[i]);
				
				}
				*/
		
			}
			
						
		
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}

		return _results;
		
	}
	private void createFactors(TerrDataFrame pFrame)
	{
		
		try
		{
			for(int i=0; i< pFrame.data.length;i++)
			{
				TerrData _terrData = pFrame.data[i];
				//System.out.println("Col type="+_terrData.getType());				
				if(_terrData.getType().equals(TerrData.Type.STRING))
				{			
					int _cntr=i+1;
					TerrJava.engineExecute("df[,"+_cntr+"] <- as.factor(df[,"+_cntr+"])", true);
				}
				
				
			}			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	
}
	

