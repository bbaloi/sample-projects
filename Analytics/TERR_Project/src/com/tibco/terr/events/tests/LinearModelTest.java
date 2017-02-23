package com.tibco.terr.events.tests;

import com.tibco.terr.TerrData;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrJava;
import com.tibco.terr.events.EngineStarter;

public class LinearModelTest 
{
	public LinearModelTest()
	{
		init();
	}
	public static void main(String [] args)
	{
		
		LinearModelTest lmt = new LinearModelTest();
		lmt.buildModel();
		lmt.predict();
	
		
	}
	private void init()
	{
		System.out.println("+++Starting TERR Engine++++ !");
		try
		{
			TerrJava.startEngine("FFInterface.SignalHandlersEnabled=TRUE");
			TerrJava.engineExecute(".libPaths('D:/R+/additional_modules/')",true);
			TerrJava.engineExecute("setwd('C:/Bruno/TIBCO/TIBCO-Projects/TERR/terr_bw_project/SharedResources/Data')",true);
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	
	}
	public void buildModel()
	{		
		try
		{
			TerrJava.engineExecute("algae = read.csv(file='AlgaeAnalysisData.txt',head=FALSE,sep=',',col.names=c('season','size','speed','mxPH','mn02','C1','NO3','NH4','oP04','PO4','Ch1a','a1','a2','a3','a4','a5','a6','a7'))",true);
			TerrJava.engineExecute("lm.a1=lm(a1 ~ .,data=algae[,1:12])",true);			
			TerrJava.engineExecute("lm.summary <- summary(lm.a1)",true);
			TerrJava.evaluateInteractive("df.coef <- lm.a1$coefficients");
			TerrData coef = TerrJava.getVariable("df.coef");
		
			for (int i=0; i<coef.getLength(); i++)
			{
				System.out.println(coef.names[i]+" : "+((TerrDouble)coef).data[i]);
			
			}
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	public void predict()
	{
		
		try
		{
			TerrJava.engineExecute("algae.newdata = read.csv(file='AlgaeEvalData.txt',head=FALSE,sep=',',col.names=c('season','size','speed','mxPH','mn02','C1','NO3','NH4','oP04','PO4','Ch1a'))",true);	
			String _result = TerrJava.engineExecute("lm.predictions.a1 = predict(lm.a1,algae.newdata)",true);
			System.out.println(_result);
			TerrData _predVar = TerrJava.getVariable("lm.predictions.a1");
			
			for (int i=0; i<_predVar.getLength(); i++)
			{
				System.out.println(_predVar.names[i]+" : "+((TerrDouble)_predVar).data[i]);			
			
			}
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}

}