package com.extemp.semantic.algo;

import java.util.HashMap;

public class ImpactNode 
{
	private String nodeName;
	private String action;
	private HashMap propertyValueMap;
	
	public ImpactNode()
	{
		
	}
	public ImpactNode(String pNodeName,String pProperty, String pAction,Object pValue)
	{
		propertyValueMap = new HashMap();
		nodeName = pNodeName;
		action = pAction;
		propertyValueMap.put(pProperty, pValue);		
	}
	
	
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Object getValue(String pProperty) {
		return propertyValueMap.get(pProperty);
	}
	public void setValue(String pProperty,Object value) {
		propertyValueMap.put(pProperty, value);
	}
	public HashMap getValueMap()
	{
		return propertyValueMap;
	}
}
