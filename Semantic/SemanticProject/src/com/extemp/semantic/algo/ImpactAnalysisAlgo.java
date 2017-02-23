package com.extemp.semantic.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.extemp.semantic.util.AlgoConstants;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ImpactAnalysisAlgo extends BaseAlgo implements IImpactAnalysys 
{
	
	public ImpactAnalysisAlgo(OntModel pModel)
	{
		 super(pModel,AlgoConstants.impact_analysis);		
		_pathList = new ArrayList();
		
	}
	public List getAllImpactPaths(String pStartNode, int pNumHops) {
		// TODO Auto-generated method stub
		
		Resource _nullSubj = null;
		Resource _nullObj=null;
		
	
			
		Individual _startNode = _ontModel.getIndividual(AlgoConstants.graphTestNS+pStartNode);
		Individual _endNode=null;
		OntProperty _edgePred= _ontModel.getOntProperty(AlgoConstants.graphTestNS+"Edge");
				
							
		StmtIterator _it = _ontModel.listStatements(_startNode,_edgePred,_nullObj);			
		List pathSubset = determineGraphPartitionPathSet(_it.toList());
		
		Iterator _pIt = pathSubset.iterator();
		while(_pIt.hasNext())
		{
			Statement _stmt = (Statement) _pIt.next();
			System.out.println("---"+_stmt+"-----");	
			
			PathImpact _path = new PathImpact(_ontModel,_startNode,pNumHops,this);
			_path.addStatment(_stmt);
			_path.setCurrentNode(_stmt.getObject().as(Individual.class));
			_path.run();
					
		
		}	
		
		return _pathList;
		
	}

	@Override
	public List getAllDownstreamImpactPoints(Query pQuery,int pNumHops) {
		// TODO Auto-generated method stub
		String _startNodeName = pQuery.getStartingPoint();
		Individual _startNode=null;
		double runningCounter=0;
		HashMap _impactNodeMap = new HashMap();
		ArrayList _retList = new ArrayList();
		
		//String pQuery.getQueryParms().get("weight");
				
		List pathList = getAllImpactPaths(_startNodeName,pNumHops);
		showAllPaths(pathList);
		
		Iterator _it = pathList.iterator();
		while(_it.hasNext())
		{
			Path _path = (Path) _it.next();
			ImpactNode _previousNode=null;
			
			Iterator stmtIt = _path.getStatmentList().iterator();			
			
			while(stmtIt.hasNext())
			{
				Statement _stmt = (Statement) stmtIt.next();
				Individual _indObj = _stmt.getObject().as(Individual.class);
				Individual _indSubj = _stmt.getObject().as(Individual.class);
				String _objName = _indObj.getLocalName();
			
				ImpactNode _node = getNode(pQuery,_previousNode,_indSubj,_indObj,_impactNodeMap);
				_previousNode=_node;
				
				
				if(_impactNodeMap.get(_objName)==null)
				{
					_impactNodeMap.put(_objName, _node);
				}
				else
				{
					System.out.println("Updating node: "+_objName);
					ImpactNode _existingNode = (ImpactNode)_impactNodeMap.get(_objName);					
					updateNode(_node,_existingNode);
				}
				
				
			}
		}
		
		//process all paths and list the impact process.
		
		Iterator _nodeIt = _impactNodeMap.keySet().iterator();
		while(_nodeIt.hasNext())
		{
			
			_retList.add(_impactNodeMap.get(_nodeIt.next()));
		}
		
		return _retList;
	}
	
	private ImpactNode getNode(Query pQuery,ImpactNode pPreviousNode,Individual pIndSubj,Individual pIndObj,HashMap pImpactNodeMap)
	{
		ImpactNode _node=null;
			
		HashMap _queryPropMap  = pQuery.getQueryParms();		
		List _propList = pIndObj.listProperties().toList();
		Literal _lit=null;
				
		Iterator _it = _propList.iterator();
		
		//System.out.println("======================");
		while(_it.hasNext())
		{
			
			Statement _stmt = (Statement) _it.next();
			
			//System.out.println(_stmt);
			
			String _propName=_stmt.getPredicate().getLocalName();
			if(_propName.equals(AlgoConstants.property_weight))
			{
				 _lit = _stmt.getObject().asLiteral();	
			}
			if(_propName.equals(AlgoConstants.property_status))
			{
				_lit = _stmt.getObject().asLiteral();	
			}
						
			//Literal _lit = 	_stmt.getObject().asLiteral();		
			
			if(_queryPropMap.containsKey( _propName))
			{
				if(_propName.equals(AlgoConstants.property_weight))
				{
					int prevNodeVal;
					int newVal=0;
					int val = _lit.getInt();		
									
					if(pPreviousNode==null)
						prevNodeVal = pIndSubj.getPropertyValue(_ontModel.getProperty(AlgoConstants.graphNS+AlgoConstants.property_weight)).asLiteral().getInt();					
					else						
						prevNodeVal = (int) pPreviousNode.getValueMap().get(AlgoConstants.property_weight);
							
					if(((String)_queryPropMap.get(_propName)).equals(AlgoConstants.property_action_add))
					{
						newVal = prevNodeVal+val;
					}
					if(((String)_queryPropMap.get(_propName)).equals(AlgoConstants.property_action_substract))
					{
						newVal = val-prevNodeVal;
					}						
							
					if(_node==null)
					{
						_node = new ImpactNode(pIndObj.getLocalName(),_propName,(String)_queryPropMap.get(_propName), new Integer(newVal));
					}
					else 
					{
						_node.setValue(_propName, new Integer(newVal));
					}
				}
				if(_propName.equals(AlgoConstants.property_status))
				{
					String  val = _lit.getString();
					if(_node==null)
						_node = new ImpactNode(pIndObj.getLocalName(),_propName,(String)_queryPropMap.get(_propName), val);
					else 
					{
						_node.setValue(_propName, val);
					}
				}
							
				
			}		
			
		}		
		
		
		return _node;
	}
	
	private void updateNode(ImpactNode pCurNode, ImpactNode pExistingNode)
	{
		
		HashMap _existingMap = pExistingNode.getValueMap();
		HashMap _curMap = pCurNode.getValueMap();
		
		Iterator _it= _curMap.keySet().iterator();
		while(_it.hasNext())
		{
			
			String _key = (String)_it.next();
			if(_existingMap.containsKey(_key))
			{
				Object _val = _existingMap.get(_key);			
				
				if(_val instanceof Integer)										
				{
					int existingValInt=0;
					int curValInt=0;
					
					if(pExistingNode.getAction().equals(AlgoConstants.property_action_add))
					{
						//System.out.println("****** "+_val.getClass());
						
						existingValInt = ((Integer) _val).intValue();
						curValInt = ((Integer)_curMap.get(_key)).intValue();
						
						existingValInt = existingValInt+curValInt;
						
						
					}
					else if(pExistingNode.getAction().equals(AlgoConstants.property_action_substract))
					{
						existingValInt = ((Integer) _val).intValue();
						curValInt = ((Integer)_curMap.get(_key)).intValue();
						
						existingValInt = existingValInt-curValInt;
					}
					
					_existingMap.put(_key, new Integer(existingValInt));
				}
				if(_val instanceof String)
				{
					String existingValStr = (String) _val;
					String curValStr = (String)_curMap.get(_key);
					
					
				}
			}
		}
		
		
		
	}
	public List getAllUpstreamImpactPoints(Query pQuery, int pNumHops)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
