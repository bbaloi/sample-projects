package com.tibco.stax.utils;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Attribute;

import com.bea.xml.stream.util.ElementTypeNames;
import java.util.Iterator;

public class ParseUtils 
{

	  public void printEventType(int eventType) {
	    System.out.print("EVENT TYPE("+eventType+"):");
	    System.out.println(ElementTypeNames.getEventTypeString(eventType));
	  }

	  public void printName(XMLStreamReader xmlr){
	    if(xmlr.hasName()){
	      String prefix = xmlr.getPrefix();
	      String uri = xmlr.getNamespaceURI();
	      String localName = xmlr.getLocalName();
	      printName(prefix,uri,localName);
	    } 
	  }
	  public void printName(EndElement pElement)
	  {
		  String elementName=pElement.getName().getLocalPart();
		  System.out.println(elementName);
	  }
	  public void printName(StartElement pElement)
	  {
		  String elementName=pElement.getName().getLocalPart();
		  System.out.println(elementName);
	  }
	  public void printAttributes(StartElement pElement)
	  {
		    Iterator it = pElement.getAttributes();
		    while(it.hasNext())
		    {
		    	Attribute att = (Attribute)it.next();
		    	System.out.println("att="+att.getName().getLocalPart()+";val="+att.getValue());
		    }
		    
	  }
	 	
	  public void printName(String prefix,
	                                String uri,
	                                String localName) {
	    if (uri != null && !("".equals(uri)) ) System.out.print("['"+uri+"']:");
	    if (prefix != null) System.out.print(prefix+":");
	    if (localName != null) System.out.print(localName);
	  }
	  
	  public void printAttributes(XMLStreamReader xmlr){

	    for (int i=0; i < xmlr.getAttributeCount(); i++) {
	      printAttribute(xmlr,i);
	    } 
	  }
	  
	 public void printAttribute(XMLStreamReader xmlr, int index) {
	    String prefix = xmlr.getAttributePrefix(index);
	    String namespace = xmlr.getAttributeNamespace(index);
	    String localName = xmlr.getAttributeLocalName(index);
	    String value = xmlr.getAttributeValue(index);
	    System.out.print(" ");
	    printName(prefix,namespace,localName);
	    System.out.print("='"+value+"'");

	  }
	  
	  public void printNamespaces(XMLStreamReader xmlr){
	    for (int i=0; i < xmlr.getNamespaceCount(); i++) {
	      printNamespace(xmlr,i);
	    }
	  }
	  
	  public void printNamespace(XMLStreamReader xmlr, int index) {
	    String prefix = xmlr.getNamespacePrefix(index);
	    String uri = xmlr.getNamespaceURI(index);
	    System.out.print(" ");
	    if (prefix == null)
	      System.out.print("xmlns='"+uri+"'");
	    else
	      System.out.print("xmlns:"+prefix+"='"+uri+"'");
	  }

}
