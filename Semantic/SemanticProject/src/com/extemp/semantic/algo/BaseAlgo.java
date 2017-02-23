package com.extemp.semantic.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.extemp.semantic.util.AlgoConstants;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class BaseAlgo 
{
	protected OntModel _ontModel;
	protected String _algoType;
	protected ExecutorService _threadPool;
	private Stack _branchPathStack;
	protected  ArrayList <Path> _pathList;
	
	public BaseAlgo(OntModel pModel,String pAlgoType)
	{
		_ontModel = pModel;
		_algoType = pAlgoType;
		_threadPool = Executors.newFixedThreadPool(AlgoConstants.threadPooliSize);
		_branchPathStack = new Stack();
		

	}
	protected List determineGraphPartitionPathSet(List pOriginalPathList)
	{
		return pOriginalPathList;
	}
	public void updatePathList(List pPathList)
	{
		Iterator _it = pPathList.iterator();
		while(_it.hasNext())
		{
			_pathList.add((Path)_it.next());
		}
		
		
	}	
	public void updatePathList(Path pPath)
	{
		_pathList.add(pPath);
	}
	protected void showAllPaths(List pPathList)
	{
		System.out.println("# paths discovered:"+pPathList.size());
		Iterator _it = pPathList.iterator();
		
		while(_it.hasNext())
		{
			Path _path = (Path) _it.next();
			System.out.println("-------Path length: "+_path.getCurPathLength()+"-----------");
			List stmtList = _path.getStatmentList();
			Iterator _stmtIt = stmtList.iterator();
			while(_stmtIt.hasNext())
			{
				Statement _stmt = (Statement) _stmtIt.next();
				System.out.println(_stmt);
			}
		}
	}
}
