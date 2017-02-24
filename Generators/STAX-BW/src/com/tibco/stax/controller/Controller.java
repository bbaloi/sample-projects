package com.tibco.stax.controller;

import java.util.HashMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Characters;

import com.bea.xml.stream.util.ElementTypeNames;
import com.tibco.stax.handler.BaseHandler;
import com.tibco.stax.handler.IHandler;
import com.tibco.stax.utils.ParseUtils;
import com.tibco.stax.action.Action;

public abstract class Controller 
{
	protected XMLEventReader lReader;
	private HashMap lNodeHandlerMap;
	private HashMap lNodeActionMap;
	private String lNodeHandlerPropertiesFile="";
	private String lNodeActionPropertiedFile="";
	private ParseUtils lParseUtils;
	private IHandler handler;
	protected String HEADER_END_ELEMENT="TradingPartnerDirectory";
	protected String ITEM_END_ELEMENT="Consumer";
	protected String ITEM_END_ELEMENT_RONA="Record";
	protected String SKIP_ELEMENT="MonthlyAccountActivity";
	protected Action defaultAction=null;
	protected String HeaderQueue="de.header.queue";
	//protected String BodyQueue = "de.body.queue";
	protected String BodyQueue = "rona.sc4.queue";
	protected String DocIntroQueue = "de.docintro.queue";
	protected int bodyCntr=0;	
	
	public Controller(IHandler pHdl,Action pAction)
	{
		//read maps from propertie files
		lNodeHandlerMap = new HashMap();
		lNodeActionMap = new HashMap();
		lParseUtils = new ParseUtils();
		if (pHdl==null)
			handler = new BaseHandler();
		else
		{
			handler = pHdl;
			defaultAction=pAction;
		}
		
	}
	public Controller()
	{
		//read maps from propertie files
		lNodeHandlerMap = new HashMap();
		lNodeActionMap = new HashMap();
		lParseUtils = new ParseUtils();
		handler = new BaseHandler();
		
		
	}
	public void registerReader(XMLEventReader pReader)
	{
		lReader=pReader;
	}
	public void handleEvent(XMLEvent pEvent)
	{
		 //System.out.print("EVENT:["+pEvent.getLocation().getLineNumber()+"]["+
          //       pEvent.getLocation().getColumnNumber()+"] ");
		//System.out.print(ElementTypeNames.getEventTypeString(pEvent.getEventType()));
		//System.out.print(" [");
		switch (pEvent.getEventType()) 
		{
			case XMLStreamConstants.START_ELEMENT:
				//System.out.print("<");
				 //lParseUtils.printName((StartElement)pEvent);
				// lParseUtils.printAttributes((StartElement)pEvent);
				//System.out.print(">");
				String startElement = ((StartElement)pEvent).getName().getLocalPart();
			  if(!startElement.equals(SKIP_ELEMENT))
			  {
				  if(startElement.equals("DPDocument"))
				  {
					  //System.out.println("DPDocumentStart");
					  handler.appendDocIntro((StartElement)pEvent);
				  }
				  else 
					  handler.appendEvent(pEvent);
			  }
			  
			  break;
			case XMLStreamConstants.END_ELEMENT:
			  //System.out.print("</");
			  //lParseUtils.printName((EndElement)pEvent);
			  //System.out.print(">");
			  String endName=(((EndElement)pEvent).getName()).getLocalPart();
			  if(!endName.equals(SKIP_ELEMENT))
				  handler.appendEvent(pEvent);			  
			  if(endName.equals(HEADER_END_ELEMENT))
			  {
				  //send out XML Output
				  String headerOutput = handler.getXMLHeaderOutput();
				  //System.out.println("###Sending out header!###");
				  defaultAction.sendMessage(headerOutput,HeaderQueue);
				  handler.resetOutputBuffer();				  
			  }
			  else if(endName.equals(ITEM_END_ELEMENT))
			  {
				  //send out XML Output
				  bodyCntr++;
				  String bodyOutput = handler.getXMLBodyOutput();
				  //System.out.println("###Sending out body item:"+bodyCntr+"###");
				  defaultAction.sendMessage(bodyOutput,BodyQueue);
				  handler.resetOutputBuffer();					  
			  }
			  else if(endName.equals(ITEM_END_ELEMENT_RONA))
			  {
				  //send out XML Output
				  bodyCntr++;
				  String bodyOutput = handler.getXMLBodyOutput();
				  //System.out.println("###Sending out body item:"+bodyCntr+"###");
				  defaultAction.sendMessage(bodyOutput,BodyQueue);
				  handler.resetOutputBuffer();					  
			  }
			  else if(endName.equals("DPDocument"))
			  {
				  //send out XML Output
				  handler.appendDocIntro((EndElement)pEvent);
				  String bodyOutput = handler.getDocIntro();
				  //System.out.println("###Sending out DocIntro ###");
				  defaultAction.sendMessage(bodyOutput,DocIntroQueue);
				  handler.resetOutputBuffer();	
				  
			  }					  
					  
			  break;
			case XMLStreamConstants.SPACE:
				//System.out.println("<Got Space>");
				break;
			case XMLStreamConstants.CHARACTERS:
			  //System.out.println(((Characters)pEvent).getData());
			  handler.appendEvent(pEvent);
			  break;
			case XMLStreamConstants.ATTRIBUTE:
				 handler.appendEvent(pEvent);				
				break;
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
			break;
			case XMLStreamConstants.CDATA:
			  //System.out.print("<![CDATA[");
			  //System.out.println(((Characters)pEvent).getData());
			  //System.out.print("]]>");		
			  handler.appendEvent(pEvent);
			  break;
			
			case XMLStreamConstants.COMMENT:
			break;
			case XMLStreamConstants.ENTITY_REFERENCE:
			break;
			case XMLStreamConstants.START_DOCUMENT:
			  //System.out.print("<?xml");
			  //System.out.print(" version='"+((StartDocument)pEvent).getVersion()+"'");
			  //System.out.print(" encoding='"+((StartDocument)pEvent).getCharacterEncodingScheme()+"'");
			  //if (((StartDocument)pEvent).isStandalone())
			    //System.out.print(" standalone='yes'");
			  //else
			    //System.out.print(" standalone='no'");
			  //System.out.print("?>");
			  handler.setHeader((StartDocument)pEvent);
			  handler.appendEvent(pEvent);
			  break;
			case XMLStreamConstants.END_DOCUMENT:
				//System.out.println("Got to the END of the Document");
				break;
			
			}
			//System.out.println("]");
		}
	public int getBodyRecords()
	{
		return bodyCntr;
		
	}

}
