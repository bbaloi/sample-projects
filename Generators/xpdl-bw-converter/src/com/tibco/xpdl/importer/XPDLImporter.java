package com.tibco.xpdl.importer;

import java.io.File;

import org.apache.xmlbeans.XmlException;
import org.wfmc.x2002.xpdl10.*;

public class XPDLImporter 
{
	private File xpdlFile =null;

	private static XPDLImporter instance=null;
	
	private XPDLImporter()
	{		
	}
	
	public static XPDLImporter getInstance()
	{
		if(instance==null)
			instance = new XPDLImporter();
		return instance;
	}
	public PackageDocument getXPDLContent(String pXpdlFile)
	{
		PackageDocument doc=null;
		try
		{
		xpdlFile = new File (pXpdlFile);
		doc =  PackageDocument.Factory.parse(xpdlFile);
		}
		catch(java.io.IOException pExcp)
		{
			pExcp.printStackTrace();
		}
		catch(XmlException pExcp)
		{
			pExcp.printStackTrace();
		}
		return doc;
	}
}
