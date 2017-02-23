package com.tibco.terr.events.tests;

import com.tibco.terr.TerrData;
import com.tibco.terr.TerrDataFrame;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrJava;
import com.tibco.terr.TerrString;
import com.tibco.terr.events.EngineStarter;

public class LinearModelTest_2 
{
	public LinearModelTest_2()
	{
		init();
	}
	public static void main(String [] args)
	{
		
		LinearModelTest_2 lmt = new LinearModelTest_2();
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
			
			TerrDataFrame algae = new TerrDataFrame(new String[] { "season","size","speed","mxPH","mn02","C1","NO3","NH4","oP04","PO4","Ch1a","a1","a2","a3","a4","a5","a6","a7"},
				new TerrData[] {
				new TerrString(new String[] { "Winter","Spring","Autumn","Summer","Spring","Winter","Spring"}),
				new TerrString(new String[] { "Small","Small","Small","Small","Small","Small","Small"}),
				new TerrString(new String[] { "Medium","Medium","Medium","Medium","Medium","High","High"}),
				new TerrDouble(new double[] { 8.3,8.1,8.03, 8.1,8.05,8.06,8.25 }),
				new TerrDouble(new double[] { 9.8,8.0,11.0,4.01,13.00,10.01,10.02}),					
				new TerrDouble(new double[] { 60.8,57.75,40.0,77.00,65.00,75.00,73.02}),					
				new TerrDouble(new double[] { 6.23800,1.28800,5.33000,2.30200,10.41600,9.24800,1.53500 }),					
				new TerrDouble(new double[] { 578.00000,370.00000,346.66699,98.18200,233.70000,430.00000,110.00000}),					
				new TerrDouble(new double[] { 105.00000,428.75000,125.66700,61.18200,58.22200,18.25000,61.25000 }),					
				new TerrDouble(new double[] { 170.00000,558.75000,187.05701,138.70000,97.58000,56.66700,111.75000 }),					
				new TerrDouble(new double[] { 50.00000,1.30000,15.60000,1.40000,10.50000,28.40000,3.20000 }),					
				new TerrDouble(new double[] { 0.00000,1.4000,3.30000,3.10000,9.20000,15.10000,2.40000}),					
				new TerrDouble(new double[] { 0.00000,7.60000,53.60000,41.00000,2.90000,14.60000,1.20000 }),					
				new TerrDouble(new double[] { 0.00000,4.80000,1.90000,18.90000,7.50000,1.40000,3.20000}),					
				new TerrDouble(new double[] { 0.00000,1.90000,0.00000,0.00000,0.00000,0.00000,3.90000}),					
				new TerrDouble(new double[] { 34.20000,6.70000,0.00000,1.40000,7.50000,22.50000,5.80000}),					
				new TerrDouble(new double[] {34.20000,6.70000,0.00000,1.40000,0.0000,0.0000,12.60000}),					
				new TerrDouble(new double[] { 0.00000,2.10000,9.70000,1.40000,1.00000,2.90000,0.00000  })					
				});
			
			
			
			TerrJava.setVariable("algae", algae);	
			TerrJava.engineExecute("algae[,1] <- as.factor(algae[,1])", true);
            TerrJava.engineExecute("algae[,2] <- as.factor(algae[,2])", true);
            TerrJava.engineExecute("algae[,3] <- as.factor(algae[,3])", true);
			TerrJava.engineExecute("algae", true);
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
		
			TerrDataFrame algae_newdata = new TerrDataFrame(new String[] { "season","size","speed","mxPH","mn02","C1","NO3","NH4","oP04","PO4","Ch1a"},
					new TerrData[] {
					new TerrString(new String[] { "Summer"}),
					new TerrString(new String[] { "Small"}),
					new TerrString(new String[] { "Medium"}),
					new TerrDouble(new double[] { 7.95000}),
					new TerrDouble(new double[] { 5.70000}),					
					new TerrDouble(new double[] { 57.33300}),					
					new TerrDouble(new double[] { 2.46000}),					
					new TerrDouble(new double[] { 273.33301}),					
					new TerrDouble(new double[] { 295.66699}),					
					new TerrDouble(new double[] { 380.00000}),					
					new TerrDouble(new double[] { 6.0000})						
					});
				
						
			TerrJava.setVariable("algae.newdata", algae_newdata);	
			TerrJava.engineExecute("algae.newdata[,1] <- as.factor(algae.newdata[,1])", true);
			TerrJava.engineExecute("algae.newdata[,2] <- as.factor(algae.newdata[,2])", true);
			TerrJava.engineExecute("algae.newdata[,3] <- as.factor(algae.newdata[,3])", true);          
			
			TerrJava.engineExecute("algae.newdata",true);
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