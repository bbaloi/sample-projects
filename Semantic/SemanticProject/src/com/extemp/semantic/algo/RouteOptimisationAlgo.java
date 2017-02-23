package com.extemp.semantic.algo;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import com.extemp.semantic.util.AlgoConstants;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntTools;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.Filter;


public class RouteOptimisationAlgo extends BaseAlgo implements IRouteOptAlgo
{
		
	
	public RouteOptimisationAlgo(OntModel pModel)
	{
		 super(pModel,AlgoConstants.route_opt);		
		_pathList = new ArrayList();
		
	}
	public List getAllPathsForClases(String pStartNode, String pEndNode,int pDistance)
	{
		OntClass _classType = _ontModel.getOntClass(AlgoConstants.graphTestNS+"Node");
		OntProperty _endClass = _ontModel.getOntProperty(AlgoConstants.graphTestNS+"Edge");
		return null;
	}

	
	public OntTools.Path getShortestPath(String pStartNode, String pEndNode) {
		// TODO Auto-generated method stub
		
		
		
		Individual _startNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pStartNode);
		Individual _endNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pEndNode);
		
		OntTools.Path _path = OntTools.findShortestPath(_ontModel, _startNode.asResource(), _endNode.asResource(), Filter.any);
		
		System.out.println("=====Shortest Path from "+_startNode.getLocalName()+" to "+_endNode.getLocalName()+" =====");
		
		Iterator _it=_path.iterator();
		while(_it.hasNext())
		{
			System.out.println(_it.next());
		}
		
		return _path;
	}
	@Override
	public Path getShortestPath_2(String pStartNode, String pEndNode, int pNumHops) 
	{
		Individual _startNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pStartNode);
		Individual _endNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pEndNode);
		 
		//1) get all relevant paths
		List pathList =  getAllPaths(pStartNode,pEndNode,pNumHops);
		showAllPaths(pathList);
		
		//2) get path with smallest # of hops other properties....
		Path _retPath = getShortestPath_2(pathList);
		
		
		
		return _retPath;
	}
	@Override
	public List getAllPaths(String pStartNode, String pEndNode,	int pNumHops) 
	{
		Resource _nullSubj = null;
		Property _nulPredicate=null;
		Resource _nullObj=null;
	
			
		Individual _startNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pStartNode);
		Individual _endNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pEndNode);
		OntProperty _edgePred= _ontModel.getOntProperty(AlgoConstants.graphTestNS+"Edge");
				
		/*
		List _graphs = _ontModel.getSubGraphs();		
		Iterator _graphIt = _graphs.iterator();
		while(_graphIt.hasNext())
		{
			System.out.println(_graphIt.next().toString());
		}
		*/
						
		StmtIterator _it = _ontModel.listStatements(_startNode,_edgePred,_nullObj);			
		List pathSubset = determineGraphPartitionPathSet(_it.toList());
		
		Iterator _pIt = pathSubset.iterator();
		while(_pIt.hasNext())
		{
			Statement _stmt = (Statement) _pIt.next();
			System.out.println("---"+_stmt+"-----");	
			
			Path _path = new Path(_ontModel,_startNode,_endNode,pNumHops,this);
			_path.addStatment(_stmt);
			_path.setCurrentNode(_stmt.getObject().as(Individual.class));
			_path.run();
					
		
		}
		
		
		return _pathList;
	}	
	@Override
	public void executeNewPath(Path pPath) {
		// TODO Auto-generated method stub
		_threadPool.execute(pPath);
		
	}
	private Path getShortestPath_2(List pPathList)
	{
		Path _retPath = null;
		
		ArrayList _retList = new ArrayList();
		
		int shortestRoutePath=0;
		
		Iterator it = pPathList.iterator();
		while(it.hasNext())
		{
			 
			Path _path = (Path) it.next();
			if(shortestRoutePath==0)
			{
				shortestRoutePath=_path.getCurPathLength();
				_retPath=_path;
			}
			else
			{
				if(_path.getCurPathLength()< shortestRoutePath)
				{
					shortestRoutePath=_path.getCurPathLength();
					_retPath=_path;
				}
				
			}
			
		}
		
		
		return _retPath;
	}
	
}
