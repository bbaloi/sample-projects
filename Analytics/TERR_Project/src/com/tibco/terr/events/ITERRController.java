package com.tibco.terr.events;

import java.util.ArrayList;

import com.tibco.terr.TerrDataFrame;

public interface ITERRController 
{
	public void generateModel();
	public void loadDataFrame();
	public void loadDataFrame(String [] pColumns, ArrayList pData);
	public TerrDataFrame loadDataFrameEval(String [] pColumns, ArrayList pData);
	public String [] predict(String [] pColumns,ArrayList pData);
	
}
