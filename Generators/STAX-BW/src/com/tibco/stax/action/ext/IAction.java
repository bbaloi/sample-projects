package com.tibco.stax.action.ext;

import java.util.HashMap;

public interface IAction 
{
	public void execute();	
	public void setProps(String pProps);
	public void execute(String pParms);
	public void setDestination(String pDest);
	public void setActionType(String pType);

}
