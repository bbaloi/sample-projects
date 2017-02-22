package com.extemp.cem.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.extemp.cem.actions.AcceptEventAction;
import com.extemp.cem.util.CEMUtil;

public class EventGeneratorTestHarness 
{
	
	private HashMap eventMap;
	private DocumentBuilder _xmlBuilder;
	private DocumentBuilderFactory _xmlFactory;
	private Properties _testFileProps;
	private AcceptEventAction _acceptEvent;
	
	public EventGeneratorTestHarness()
	{
		
		eventMap=new HashMap();
		_acceptEvent = new AcceptEventAction();
		_testFileProps = new Properties();
		try
		{
			_xmlFactory =  DocumentBuilderFactory.newInstance();
		 _xmlBuilder = _xmlFactory.newDocumentBuilder();
		 
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	
	public void loadEvents()
	{
		String _testFile = CEMUtil.getInstance().getCEMProperty("cem.test.file.location");
		
		//System.out.println("Test Props Filename:"+_testFile);
		
		InputStream _input = null;
		
		if(_testFileProps !=null)
		{
			//System.out.println("Propsfilename:"+_testFileProps );
			try {
	 
				_input = new FileInputStream(_testFile); 
				_testFileProps.load(_input); 
				_input.close();
				//System.out.println(prop.getProperty("database"));		
	 
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
			
		}
		
		String inParkingEventDoc = getXMLDocFromFile((String)_testFileProps.get("event.in.parking")); 		
		_acceptEvent.acceptMobileEvent(inParkingEventDoc);
		
		String ticketCheckEventDoc = getXMLDocFromFile((String)_testFileProps.get("event.ticket.check")); 
		
		String inSeatEventDoc = getXMLDocFromFile((String)_testFileProps.get("event.in.seat")); 
		
		
	}
	
	public String getEvent(String pEventName)
	{
		return (String) eventMap.get(pEventName);
	}

	public String getXMLDocFromFile(String pFileName)
	{
		String _retDoc=null;
		if(pFileName!=null)
		{
			 
			 try
			 {
					File _file = new File(pFileName);
						Document _document  = _xmlBuilder.parse(_file);
						_retDoc=getStringFromDocument(_document);
					
					

			 }
			 catch(Exception pExcp)
			 {
				pExcp.printStackTrace();
			 }
		}
		
	
		return _retDoc;
		
	}
	public String getStringFromDocument(Document doc)
	{
		String _dom=null;
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       _dom= writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();	       
	    }
	    return _dom;
	} 
}
