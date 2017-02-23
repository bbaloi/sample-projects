package com.extemp.semantic.algo;

import java.util.HashMap;

public class Query
{
	private HashMap queryParms;
	private String endPoint=null;
	private String startingPoint=null;
	
	public Query()
	{
		queryParms = new HashMap();
	}
	public Query(String pStartNode)
	{
		queryParms = new HashMap();
		startingPoint = pStartNode;
	}
	public Query(String pStartNode,String pEndNode)
	{
		queryParms = new HashMap();
		startingPoint = pStartNode;
		endPoint = pStartNode;
	}
	
	public HashMap getQueryParms() {
		return queryParms;
	}

	public void setQueryParms(HashMap queryParams) {
		this.queryParms = queryParams;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(String startingPoint) {
		this.startingPoint = startingPoint;
	}

	
	
	
}
