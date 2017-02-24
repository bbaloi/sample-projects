package com.tibco.stax.handler;

import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.EndElement;

public interface IHandler 
{
	public void appendEvent(XMLEvent pEvent );
	public void setHeader(StartDocument pHeader);
	public void resetOutputBuffer();
	public String getXMLBodyOutput();
	public String getXMLHeaderOutput();
	public void appendDocIntro(StartElement pEvent);
	public void appendDocIntro(EndElement pEvent);
	public String getDocIntro();

}
