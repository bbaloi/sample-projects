package com.extemp.semantic.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extemp.semantic.util.AlgoConstants;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class Path implements Runnable
{
	protected List statementList;
	protected Individual  rootNode=null;
	protected Individual  lastNode=null;	
	protected Individual currentNode;
	protected int numHops;
	//private IRouteOptAlgo rootAlgo;
	protected BaseAlgo rootAlgo;
	protected OntModel ontModel;
	protected int curPathLength=1;
	protected boolean pathExceededLimit;
	protected boolean pathCompleted=false;	

	public Path()
	{
		statementList = new ArrayList();
	}
	
	//public Path(OntModel pModel,Individual pRootNode,Individual pLastNode,int pNumHops,IRouteOptAlgo pRootAlgo)
	public Path(OntModel pModel,Individual pRootNode,Individual pLastNode,int pNumHops,BaseAlgo pRootAlgo)
	{
		statementList = new ArrayList();
		rootNode = pRootNode;
		numHops=pNumHops;
		lastNode = pLastNode;		
		rootAlgo=pRootAlgo;
		ontModel =pModel;
	}
	
	public Path(Path pOldPath)
	{
		rootNode = pOldPath.getRootNode();
		lastNode = pOldPath.getLastNode();
		currentNode = pOldPath.getCurrentNode();
		numHops = pOldPath.getNumHops();
		rootAlgo=pOldPath.getRootAlgo();
		ontModel = pOldPath.getOntModel();
		curPathLength=pOldPath.getCurPathLength();
		statementList = new ArrayList();
		Iterator it =pOldPath.getStatmentList().iterator();
		while(it.hasNext())
		{
			statementList.add(it.next());
		}
			
	}
	public Path(Path pOldPath,List pStmtList,int pNewPathCntr)
	{
		rootNode = pOldPath.getRootNode();
		lastNode = pOldPath.getLastNode();
		currentNode = pOldPath.getCurrentNode();
		numHops = pOldPath.getNumHops();
		rootAlgo=pOldPath.getRootAlgo();
		ontModel = pOldPath.getOntModel();
		curPathLength=pNewPathCntr;
		statementList = new ArrayList();
		Iterator it =pStmtList.iterator();
		while(it.hasNext())
		{
			statementList.add(it.next());
		}
			
	}
	@Override
	public void run() {
		
		if( ! lastNode.getLocalName().equals(currentNode.getLocalName()))
			{
				
				StmtIterator _it = currentNode.listProperties( ontModel.getOntProperty(AlgoConstants.graphTestNS+"Edge"));						
				if(_it.hasNext())
				{
					
					boolean updatedCurrentPath=false;
					List _tmpStmtList = setCurStmtList(statementList);
					curPathLength++;
					int newPathCntr=curPathLength;
					
					while(_it.hasNext())
					{
						Statement _stmt = (Statement) _it.next();				
						//System.out.println(_stmt);					
						
						if(!updatedCurrentPath)
						{				
								if(curPathLength<numHops)
								{
									currentNode=_stmt.getObject().as(Individual.class);
									statementList.add(_stmt);					
									updatedCurrentPath=true;
									run();
								}
							
						}
						//make a copy of the current path
						else
						{
							
							Path newPath = new Path(this,_tmpStmtList,newPathCntr);
							newPath.addStatment(_stmt); 
							newPath.setCurrentNode(_stmt.getObject().as(Individual.class));
							newPath.run();		
							//rootAlgo.executeNewPath(newPath);
						}	
					
					}
				}
				else
				{
					//pathExceededLimit=true;
					//break;
					return;
				}
			}
			else
			{
				System.out.println("----Finished path------");
				if(! pathExceededLimit)
				{
					pathCompleted=true;
					rootAlgo.updatePathList(this);
				}
				
			}		
	}
	
	
	//===============================================
	public List getStatmentList() {
		return statementList;
	}
	
	public Individual getRootNode() {
		return rootNode;
	}
	public void setRootNode(Individual _firstNode) {
		this.rootNode = _firstNode;
	}
	public Individual getLastNode() {
		return lastNode;
	}
	public void setLastNode(Individual lastNode) {
		this.lastNode = lastNode;
	}
	public void addStatment(Statement pStmt)
	{	
		statementList.add(pStmt);
	}
	public Individual getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(Individual currentNode) {
		this.currentNode = currentNode;
	}
	public int getNumHops()
	{
		return numHops;
	}
	public BaseAlgo getRootAlgo()
	{
		return rootAlgo;
	}
	public OntModel getOntModel()
	{
		return ontModel;
	}
	public int getCurPathLength()
	{
		return curPathLength;
	}
	private List setCurStmtList(List pStmtList)
	{
		ArrayList _newList = new ArrayList();
		Iterator it =pStmtList.iterator();
		while(it.hasNext())
		{
			_newList.add(it.next());
		}
		return _newList;
	}
}
