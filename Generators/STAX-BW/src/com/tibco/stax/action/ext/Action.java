package com.tibco.stax.action.ext;

import java.util.HashMap;
import java.util.StringTokenizer;

public abstract class Action  implements IAction
{
	protected StringTokenizer tokenizer;
	protected String delimiter="|";
	protected String destination;
	protected String type;
	
	public Action()
	{
	}
	public void execute()
	{		
	}	
	public void setProps(String pProps)
	{			
	}
	public void execute(HashMap  pMessage)
	{
		
	}
	public void setDestination(String pDest)
	{
		destination=pDest;
	}
	public void setActionType(String pType)
	{
		type=pType;
	}
	
}
