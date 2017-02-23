package com.extemp.semantic.algo;

import java.util.List;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntTools;

public interface IRouteOptAlgo 
{
	public OntTools.Path getShortestPath(String pStartNode, String pEndNode);
	public Path getShortestPath_2(String pStartNode, String pEndNode,int pNumHops);
	public List getAllPaths(String pStartNode, String pEndNode,int pNumHops);	
	public void executeNewPath(Path pPath);
	

}
