package com.tibco.stax.handler;

import java.lang.StringBuffer;

import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;

public class BaseHandler implements IHandler
{
	private static StringBuffer XML_OUTPUT = null;
	private String docHeader=null;
	private StringBuffer docIntro=null;

	public void setHeader(StartDocument pHeader)
	{
		docHeader = pHeader.toString();
	}		
	public void appendDocIntro(StartElement pEvent)
	{
		if(docIntro==null)
			docIntro = new StringBuffer();
		docIntro.append(pEvent.toString());
	}
	public void appendDocIntro(EndElement pEvent)
	{
		docIntro.append(pEvent.toString());
	}
	public String getDocIntro()
	{
		return docIntro.toString();
	}
	
	public void appendEvent(XMLEvent pEvent )
	{
		appendXMLOutput(pEvent);
	}

	public void appendXMLOutput(XMLEvent pXmlEvent) 
	{
    
		if (XML_OUTPUT == null) 
		{
			XML_OUTPUT = new java.lang.StringBuffer();
			//Add header to document			
		}
		/*if(docIntro==null)
		{
			docIntro = new StringBuffer();
			docIntro = docIntro.append(pXmlEvent.toString());
		}*/
		//add remainder of body.    
		XML_OUTPUT = XML_OUTPUT.append(pXmlEvent.toString());
    }
	public void resetOutputBuffer()
	{
		XML_OUTPUT = new StringBuffer();
		XML_OUTPUT.append(docHeader);
		
	}
	public String getXMLBodyOutput()
	{
		return XML_OUTPUT.toString();
	}
	public String getXMLHeaderOutput()
	{
		//XML_OUTPUT.append("	</DPDocument>");
		return XML_OUTPUT.toString();
	}
  }
	
	