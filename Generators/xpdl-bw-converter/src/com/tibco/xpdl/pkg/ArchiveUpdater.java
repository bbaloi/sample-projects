package com.tibco.xpdl.pkg;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ArchiveUpdater 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.xpdl.pkg.ArchiveUpdater");
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder docBuilder;
	private File archiveFile,archiveFileTmp;
	private BWProcessPackager lPackager;
	private String archiveFileName,archiveFileNameTmp;
	
	public ArchiveUpdater(BWProcessPackager pPackager)
	{
		lPackager=pPackager;
		init();
	}
	private void init()
	{
		archiveFileName=(String)lPackager.getProperties().get("bw.archive.file");
		archiveFileNameTmp=archiveFileName+".tmp";
		archiveFile = new File(archiveFileName);
		archiveFileTmp = new File(archiveFileNameTmp);
	}
	
	public void updateArchive(List pProcList)
	{
		try
		{
			factory=DocumentBuilderFactory.newInstance(); 
			docBuilder=factory.newDocumentBuilder();
			
			Document archiveDoc=docBuilder.parse(archiveFile);
			NodeList nodeList =archiveDoc.getElementsByTagName("processArchive");
			Node node = nodeList.item(0);
			if(node!=null)
			{
				String curValue=node.getNodeValue();	
				if(curValue==null)
					curValue= new String();
				sLogger.debug("Node name:"+node.getNodeName()+",value:"+curValue);
				
				int listLen=pProcList.size();
				for(int i=0;i<listLen;i++)
				{
					curValue+=(String)pProcList.get(i);
					if(i<listLen-1)
						curValue+=",";
				}
				
				node.setNodeValue(curValue);
				sLogger.debug("Updated Val:"+node.getNodeValue());
				
				FileWriter fileWriter = new FileWriter(archiveFileTmp);
				String docOut=archiveDoc.toString();
				sLogger.debug("DocValue:"+docOut);
				fileWriter.write(docOut);
				fileWriter.close();
				// overwrite new file
				//overWriteFile();
			}
			else sLogger.error("Got no node !");
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
			
		}
	}
	private void overWriteFile()
	{
		archiveFile.delete();
		archiveFileTmp.renameTo(archiveFile);
		
	}
	
	

}
