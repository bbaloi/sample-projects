package com.tibco.stax.handler.ext;

import com.tibco.stax.utils.Constants;

public class DocHeaderHandler extends BaseHandler
{
	/*
	public boolean notifyHandler(int pStart)
	{
		//System.out.println("Notification:"+pStart);
		if(pStart==Constants.START_ELEMENT)		
		{
			if(lStrBuffer==null)
			{
				lStrBuffer = new StringBuffer();
				lStrBuffer.append(lXMLHeader);
			}		
			
		}
		else if(pStart== Constants.END_ELEMENT)
		{
				System.out.println("+++End Element:"+lNodeName+", hdl:"+this);
				//System.out.println(this+" Sending:"+lStrBuffer);				
				
				defaultAction.execute(lStrBuffer.toString());
				if(includeBody)
				{
					//defaultAction.execute(lStrBuffer.toString());
					lStrBuffer= new StringBuffer();	
					lStrBuffer.append(lXMLHeader);
				}			
				
		}
		return includeBody;
	}	
	*/
}
