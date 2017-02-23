package com.tibco.xpdl.bw.components;

import java.util.ArrayList;
import java.util.List;

public abstract class BWMarshaller 
{
	protected boolean xmlBeans=true;
	protected abstract String write(String pCurrentModel);
		
	protected List childrenList;
	
	public BWMarshaller(boolean pXmlBeans)
	{
		xmlBeans=pXmlBeans;
		childrenList = new ArrayList();
	}
	public void addChild(BWMarshaller pComponent)
	{
		childrenList.add(pComponent);
	}
	
}