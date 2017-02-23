package com.extemp.semantic.algo;

import java.util.List;

public interface IImpactAnalysys 
{
	public List getAllImpactPaths(String pStartNode,int pNumHops);
	public List getAllDownstreamImpactPoints(Query pQuery,int pNumHops);
	public List getAllUpstreamImpactPoints(Query pQuery,int pNumHops);
}
