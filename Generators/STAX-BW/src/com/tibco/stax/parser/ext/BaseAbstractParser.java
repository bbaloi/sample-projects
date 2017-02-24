package com.tibco.stax.parser.ext;

import java.io.FileReader;
import java.util.Iterator;

import javanet.staxutils.BaseXMLInputFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.namespace.QName;
import com.bea.xml.stream.util.ElementTypeNames;
import com.bea.xml.stream.*;
import com.tibco.stax.action.jms.*;
import com.tibco.stax.handler.*;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tibco.stax.controller.ext.BaseController;
import com.tibco.stax.utils.PropertiesLoader;

public abstract class BaseAbstractParser
{
	protected static boolean initialized=false;
	protected static XMLInputFactory lInputFactory;
	//protected static BaseXMLInputFactory lInputFactory;
	protected static BaseController lController;
	protected static XMLEventReader lStreamEventReader;
	//protected static XMLEventReaderBase lStreamEventReader;
	protected static FileReader lFileReader;
	protected static PropertiesLoader propLoader;
	protected static String lInputFileName;
			
	protected static PropertiesLoader propertyLoader;
	public BaseAbstractParser()
	{
		
	}
	public static int parse(String pFileName,String pPropFileName) 
	{
		try
		{
			//if(initialized==false)
				init(pFileName,pPropFileName);
			System.out.println("--###Starting processing file: "+pFileName+" ###--");
			parseFile();
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		int recordCount=lController.getBodyRecords();
		System.out.println("--###Finished processing; processed "+recordCount+" records ###--");
		
		return recordCount;
			
	}
	protected static void init(String pFileName,String pPropsFileName)
	{
		try
		{
			lInputFileName=pFileName;
		//System.out.println("###Initializing !");
		 lInputFactory= XMLInputFactory.newInstance();
		 //lInputFactory= BaseXMLInputFactory.newInstance();
		 //System.out.println("File:"+pFileName);
		 lFileReader= new FileReader(pFileName);
		 //System.out.println("FileReader:"+lFileReader);
		 lStreamEventReader = lInputFactory.createXMLEventReader(lFileReader);
		 PropertiesLoader propLoader= PropertiesLoader.getInstance();
		 propLoader.loadProperties(pPropsFileName);
		 lController = new BaseController(pFileName);
		 lController.registerReader(lStreamEventReader);
		 initialized=true;
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		    
		
	}
	protected static void parseFile() throws XMLStreamException
	{
		System.out.println("READER:"+lStreamEventReader);
		 while(lStreamEventReader.hasNext()){
			      lController.handleEvent((XMLEvent)lStreamEventReader.next());		      
		    }
		 lStreamEventReader.close();
		 //propLoader.renameInputFile(lInputFileName);
	}
	
	
}
