package com.tibco.xpdl.pkg;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class BWProcessPackager 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.xpdl.pkg.BWProcessPackager");
	private Properties lProps;
	private String lPropertiesFileName;
	
	public static void main(String[] args) 
	{
		BWProcessPackager packager= new BWProcessPackager();
		packager.validateInput(args);
		packager.initProps(args);
		ArchiveUpdater updater = new ArchiveUpdater(packager);
		ProcessAssembler assembler= new ProcessAssembler(packager);
		List procList = assembler.getProcessList();
		updater.updateArchive(procList);
		
		
	}
	public void initProps(String [] args)
	{
		lProps = new Properties();
		try
		{
			lProps.load(new FileInputStream(lPropertiesFileName));
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
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
	public Properties getProperties()
	{
		return lProps;
	}
	

}
