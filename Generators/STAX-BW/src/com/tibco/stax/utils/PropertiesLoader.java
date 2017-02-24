package com.tibco.stax.utils;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Properties;

public class PropertiesLoader 
{
	private Properties lRootProps =new Properties(), lNodeHandlerProps=new Properties(),lNodeActionProps=new Properties(),lActionProps=new Properties();
	private String fileCntr;
	private long cntr=0;
	private static PropertiesLoader instance=null;
	//private static PrintWriter pWriter=null;
	private static RandomAccessFile lFile;
		
	private PropertiesLoader()
	{				
	}
	public static PropertiesLoader getInstance()
	{
		if(instance==null)
			instance= new PropertiesLoader();
		return instance;
	}
	public void loadProperties(String pRootFileName)
	{
		try
		{
			lRootProps.load(new FileInputStream(pRootFileName));
			lNodeHandlerProps.load(new FileInputStream((String)lRootProps.get("node.handler.map")));
			lNodeActionProps.load(new FileInputStream((String)lRootProps.get("node.action.map")));
			lActionProps.load(new FileInputStream((String)lRootProps.get("action.map")));			
			_getFileCntr();
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		//
	}	
	public Hashtable getNodeHandlerMap()
	{
		return lNodeHandlerProps;
	}
	public Hashtable getNodeActionMap()
	{
		return lNodeActionProps;
	}
	public Hashtable getActionPropsMap()
	{
		return lActionProps;
	}
	private void _getFileCntr()
	{
		BufferedReader reader;
		fileCntr=(String)lRootProps.get("cntr.file");	
		try
		{
			reader = new BufferedReader(new FileReader(fileCntr));
			String line = reader.readLine();
			cntr = Long.parseLong(line);
			System.out.println("Cur fileCntr="+cntr);
			reader.close();
		}
		catch(Exception pExcp)
		{
			//pExcp.printStackTrace();
			System.out.println("First time reading File !");			
			
		}		
		
	}
	public long getFileCntr()
	{
		return cntr;
	}
	public void resetFileCntr()
	{
		try
		{
			lFile.close();
			lFile=null;
		}
		catch(IOException excp)
		{
			excp.printStackTrace();
		}
	}
	public void updateFileCntr(long pFileCntr)
	{
		try
		{
			if(lFile==null)
				lFile = new RandomAccessFile(fileCntr,"rwd");
			lFile.seek(0);
			System.out.println("record #:"+pFileCntr);			
			lFile.writeBytes(Long.toString(pFileCntr));			
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	public void removeFileCntr()
	{
		try
		{
			lFile.close();
			File _f = new File(fileCntr);
			_f.delete();
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	public void renameInputFile(String pFileName)
	{
		File _f= new File(pFileName);
		String procFile = pFileName+".processed";
		_f.renameTo(new File(procFile));		
	}
}
