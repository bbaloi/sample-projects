package com.extemp.semantic.algo;

import java.util.Iterator;

import java.util.List;

import com.extemp.semantic.control.EngageMain;
import com.extemp.semantic.util.AlgoConstants;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class AlgoMainController 
{
	private static String _opMode;
	private static String _sampleFileName;
	private static String _propertiesFileName;
	private static OntModel _ontModel;
	private static int _maxHops;
	private static String _startPoint,_endPoint;
	
	public AlgoMainController(String [] args)
	{
		init(args);
	}
	public AlgoMainController()
	{
		
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		AlgoMainController _main = new AlgoMainController(args);
			
			try
			{
								
				if(_opMode.equals("route_opt"))
					runRouteOpt();				
				if(_opMode.equals("impact_analysis"))
					runImpact();				
				
				
			}
			catch(Exception excp)
			{
				excp.printStackTrace();
			}				
			
		
	}
	private void init(String [] args)
	{
		//System.out.println("NUm parms:"+args.length);
		if(args.length<2)
		{
			System.out.println("Command is: java -jar extemp.engage.algo.jar -mode [build/infer] -OntFile xxx.rdf -props emantic_events.properties");
			System.exit(0);
		}
		else
		{
			String mode = args[1];
			_opMode=mode;	
				
		}
		if(args.length>2)
		{
			_sampleFileName=args[3];
		}
		if(args.length>4)
		{
			_maxHops=Integer.parseInt(args[5]);
		}
		if(args.length>6)
		{
			_startPoint=args[7];
		}
		if(args.length>8)
		{
			_endPoint=args[9];
		}
		if(args.length>10)
		{
			_propertiesFileName=args[11];
		}
		loadOntModel();
		
	}
	private void loadOntModel()
	{
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		_ontModel .read(_sampleFileName, null);
		
	}
	private static void runRouteOpt()
	{
		RouteOptimisationAlgo _algo= AlgoFactory.getRouteOptimisationAlgo(_ontModel);
		
		//Path _result = _algo.getShortestPath_2("A", "Z",_maxHops);
		System.out.println("start:"+_startPoint+", end:"+_endPoint);
		Path _result = _algo.getShortestPath_2(_startPoint, _endPoint,_maxHops);
		
		Iterator it = _result.getStatmentList().iterator();
		
		System.out.println("=====ShortestPath By Edges============");
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
		
		
	}
	private static void runImpact()
	{
		ImpactAnalysisAlgo _algo= AlgoFactory.getImapctAnalysisAlgo(_ontModel);
		
		//build Query
		Query _query = new Query(_startPoint);
		
		_query.getQueryParms().put(AlgoConstants.property_weight, AlgoConstants.property_action_add);
		//_query.getQueryParms().put(AlgoConstants.property_status, AlgoConstants.property_action_add);
		
		List _result = _algo.getAllDownstreamImpactPoints(_query, _maxHops);
		
		Iterator it = _result.iterator();
		
		System.out.println("=====List of points impacted by "+_startPoint+" ============");
		while(it.hasNext())
		{
			ImpactNode _node = (ImpactNode)it.next();
			
			System.out.println("------Node: "+_node.getNodeName()+", action: "+_node.getAction()+"-------");
			Iterator _propIt = _node.getValueMap().keySet().iterator();
			while(_propIt.hasNext())
			{
				String _property = (String)_propIt.next();
				Object _val = _node.getValueMap().get(_property);
				
				System.out.println("+Property: "+_property+", value:"+_val.toString());
				
			}
			
			
		}
		
		
	}
}
