package com.tibco.stax.parser;

import java.io.FileReader;
import java.util.Iterator;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.namespace.QName;
import com.bea.xml.stream.util.ElementTypeNames;
import com.tibco.stax.action.jms.*;
import com.tibco.stax.handler.*;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tibco.stax.controller.TibcoStaxController;
import com.tibco.stax.controller.Controller;

public abstract class AbstractParser
{
	protected static boolean initialized=false;
	protected static XMLInputFactory lInputFactory;
	protected static Controller lController;
	protected static XMLStreamReader lStreamReader;
	protected static XMLEventReader lStreamEventReader;
	protected static FileReader lFileReader;
	protected static String jmsPropFile="res/jms.properties";
	//-------------JMS related vars-----------
	protected static String EMSHost="tcp://localhost:7222";
	protected static String EMSUser="admin";
	protected static String EMSPass="";
		
	public AbstractParser()
	{
		
	}
	public static int parse(String pFileName,String pPropFileName) 
	{
		try
		{
		if(initialized==false)
			init(pFileName);
		System.out.println("###Starting processing file: "+pFileName+" ###");
		parseFile();
		}
		catch(Exception excp)
		{
			//excp.printStackTrace();
		}
		int recordCount=lController.getBodyRecords();
		System.out.println("###Finished processing; processed "+recordCount+" records ###");
		
		return recordCount;
			
	}
	protected static void init(String pFileName)
	{
		try
		{
		setProps(pFileName);
		lInputFactory= XMLInputFactory.newInstance();
		 //System.out.println("FACTORY: " + lInputFactory);
		 lFileReader= new FileReader(pFileName);
		 //System.out.println("FileReader:"+lFileReader);
		 //lStreamReader = lInputFactory.createXMLStreamReader(lFileReader);
		 lStreamEventReader = lInputFactory.createXMLEventReader(lFileReader);
		 //System.out.println("+++READER:  " + lStreamReader);
		    
		 lController = new TibcoStaxController(new BaseHandler(),
				 		new JMSAction(EMSHost,EMSUser,EMSPass));
		 lController.registerReader(lStreamEventReader);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		    
		
	}
	protected static void parseFile() throws XMLStreamException
	{
		//System.out.println("READER:"+lStreamEventReader);
		 while(lStreamEventReader.hasNext()){
			      lController.handleEvent((XMLEvent)lStreamEventReader.next());		      
		    }
		 lStreamReader.close();
	}
	private static void setProps(String pPropFile)
	{
		Properties jmsProps = new Properties();
		try
		{
		jmsProps.load(new FileInputStream(pPropFile));
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		if(jmsProps.get("host")!=null)
		{
			System.out.println("EMS-URL:"+jmsProps.get("host"));
			EMSHost=(String)jmsProps.get("host");
		}
		if(jmsProps.get("user")!=null)
		{
			System.out.println("EMS-User:"+jmsProps.get("user"));
			EMSUser=(String)jmsProps.get("user");
		}
		if(jmsProps.get("pass")!=null)
		{
			System.out.println("EMS-Pass:"+jmsProps.get("pass"));
			EMSPass=(String)jmsProps.get("pass");
		}
	}
	
}
