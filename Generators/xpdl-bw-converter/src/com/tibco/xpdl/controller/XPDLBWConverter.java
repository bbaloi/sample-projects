package com.tibco.xpdl.controller;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
//import org.wfmc.x2002.xpdl10.*;
import com.tibco.xpdl.exceptions.XPDLBWException;
import com.tibco.xpdl.importer.XPDLImporter;

public class XPDLBWConverter 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.xpdl.controller.XPDLBWConverter");
	private static String xpdlFile=null;
	private static XPDLBWConverter converterController = null;
	private static Properties lConverterProperties;
	private static String lPropertiesFileName;
	
	public static void main(String[] args) 
	{
		
		sLogger.info("Starting XPDL-BW conversion... !");
		converterController = new XPDLBWConverter();
		converterController.validateInput(args);
		converterController.initProps(args);
		XPDLImporter xpdlImporter = XPDLImporter.getInstance();
		BWBuilder bwBuilder = BWBuilder.getInstance();	
		try
		{
			//1. generate BW files
			bwBuilder.buildBWProcessDefinition(xpdlImporter.getXPDLContent(xpdlFile),lConverterProperties);
			sLogger.info("Succeffully generated BW Process Definition !");
		}
		catch(XPDLBWException pExcp)
		{
			sLogger.error("Failed to generate BW Process Definition");
			pExcp.printStackTrace();			
		}
		//2. package EAR
		//3. deploy
		
	}
	public void validateInput(String args[])
	 {
		 //System.out.println("Num parms:"+args.length);
		if(args.length<2 )
		{
			System.out.println("Invalid number of arguments !");
			System.out.println("Proper arguments are: -properties_file <PropertiesFileName>");
			System.exit(1);
		}
		else
		{
			lPropertiesFileName= (String) args[1];
			
		}			
	 }
	public void initProps(String [] args)
	{
		lConverterProperties = new Properties();
		try
		{
			lConverterProperties.load(new FileInputStream(lPropertiesFileName));
			xpdlFile = lConverterProperties.getProperty("xpdl.file");
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
	}
	public Properties getProperties()
	{
		return lConverterProperties;
	}
	
}
