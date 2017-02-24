package com.tibco.stax.handler.ext;

import java.lang.StringBuffer;
import com.tibco.stax.action.ext.IAction;

public interface IHandler 
{
	public void setHandlerAction(IAction pAction);
	public void setIncludeValue(String pValue);
	public void setXMLHeader(String pHeader);
	public void incInstance();
	public long getInstaceCntr();
	//public boolean notifyHandler(int pStart);
	public boolean notifyHandler(int pStart,long pCurCntr,long pFileCntr);
	public void appendEvent(String pEvent);
	public boolean includeBody();
	public void createEndElement();
	public void setNodeName(String pNodeName);
	
}
