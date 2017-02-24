package com.tibco.stax.handler.ext;

import java.lang.StringBuffer;

import com.tibco.stax.action.ext.IAction;
import com.tibco.stax.utils.Constants;
import com.tibco.stax.utils.PropertiesLoader;

public class BaseHandler implements IHandler
{
	protected StringBuffer lStrBuffer=null;
	protected String lXMLHeader;
	protected String lNodeName;
	protected long lInvocationCntr=0;
	protected IAction defaultAction;
	protected boolean includeBody;
	protected PropertiesLoader propLoader=null;
	
	
	public void setHandlerAction(IAction pAction)
	{
		defaultAction=pAction;
		//System.out.println("Hdl:"+this+"Action:"+defaultAction);
	}
	public void setXMLHeader(String pHeader)
	{
		lXMLHeader=pHeader;
	}
	public void incInstance()
	{
		lInvocationCntr++;
	}
	public long getInstaceCntr()
	{
		return lInvocationCntr;
	}
	public void appendEvent(String pEvent)
	{
		lStrBuffer.append(pEvent);
	}
	
	public boolean notifyHandler(int pStart,long pCurCntr,long pFileCntr)
	{
		//System.out.println("Notification:"+pStart);
		if(pStart==Constants.START_ELEMENT)		
		{
			//System.out.println("---Start Element:"+lNodeName+", hdl:"+this);
			
			if(lStrBuffer==null)
			{
				lStrBuffer = new StringBuffer();
				lStrBuffer.append(lXMLHeader);
			}					
			
		}
		else if(pStart== Constants.END_ELEMENT)
		{
				//System.out.println(this+" Sending:"+lStrBuffer);				
				//System.out.println("+++End Element:"+lNodeName+", hdl:"+this);				
				if(includeBody)
				{					
					if(lInvocationCntr>=pFileCntr)
					{
						if(propLoader==null)
							propLoader=PropertiesLoader.getInstance();
						
						propLoader.updateFileCntr(lInvocationCntr);
						defaultAction.execute(lStrBuffer.toString());		
						System.out.println("FileCntr="+pFileCntr+",InvocatoinCntr="+lInvocationCntr);
					}
						
						
					//defaultAction.execute(lStrBuffer.toString());
					lStrBuffer= new StringBuffer();	
					lStrBuffer.append(lXMLHeader);
				}								
				
		}
		return includeBody;
	}
	public void setIncludeValue(String pValue)
	{
		if(pValue.equals(Constants.INCLUDE_BODY))
			includeBody=true;
		else if(pValue.equals(Constants.EXCLUDE_BODY))
			includeBody=false;
		//System.out.println("include value:"+lNodeName+":"+includeBody);
	}
	public boolean includeBody()
	{
		return includeBody;
	}
	public void createEndElement()
	{
		String endElement = "</"+lNodeName+">";
		lStrBuffer.append(endElement);
		//System.out.println("+++Excluded-End Element:"+lNodeName+", hdl:"+this);
		defaultAction.execute(lStrBuffer.toString());	
		//lStrBuffer = null;		
		
	}
	public void setNodeName(String pNodeName)
	{
		lNodeName=pNodeName;
	}
	
}
