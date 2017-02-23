package com.tibco.xpdl.pkg;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

import org.apache.log4j.Logger;

public class ProcessAssembler 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.xpdl.pkg.ProcessAssembler");
	
	private ArrayList procList =null;
	private File generatedDir=null;
	private BWProcessPackager lPackager;
	
	public ProcessAssembler(BWProcessPackager pPackager)
	{
		lPackager = pPackager;
		init();
		
	}
	
	private void init()
	{
		generatedDir = new File(lPackager.getProperties().getProperty("bw.proj.dir"));
		
	}
	
	
	public List getProcessList()
	{
		procList = new ArrayList();
		File [] fileList=generatedDir.listFiles();
		int listLen=fileList.length;
		for(int i=0;i<listLen;i++)
		{
			File file = fileList[i];
			if(file.isFile())
			{
				
				String fileName=file.getName();				
				
				if(!fileName.equals(".folder"))
				{
					sLogger.debug("Proc:"+fileName);
					procList.add(fileName);
				}
			}
		}
		
		return procList;
	}
}
