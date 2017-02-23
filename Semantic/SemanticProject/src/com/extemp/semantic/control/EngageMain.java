package com.extemp.semantic.control;

import java.util.Iterator;

import java.util.List;

import com.extemp.semantic.generator.EventOntologyBuilder;
import com.extemp.semantic.generator.IOntologyBuilder;
import com.extemp.semantic.generator.ModelGeneratorFactory;
import com.extemp.semantic.handler.IntentDisoveryEngine;
import com.extemp.semantic.inference.InferenceHarness;
import com.extemp.semantic.util.Constants;
import com.extemp.semantic.util.InformationClusterDetector;
import com.extemp.semantic.util.OntologyConverterUtil;
import com.extemp.semantic.util.OntologyUtil;
import com.hp.hpl.jena.ontology.OntModel;

//public class EngageMain  implements IIntentDetector
public class EngageMain  
{
	private static String _opMode;
	private static String _eventSampleFileName;
	private static String _propertiesFileName;

	
	public EngageMain(String [] args)
	{
		init(args);
	}
	public EngageMain()
	{
		
	}
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		EngageMain _main = new EngageMain(args);
			/*
			IOntologyBuilder _prodBuilder = ModelGeneratorFactory.getProductModel();
			_prodBuilder.buildBaseSchemaModel();
			OntModel _model = _prodBuilder.buildBaseInstanceModel();
			OntologyUtil.getInstance().printOntology(_model, Constants.prodOntFile, false,Constants.productNS);
			*/
			try
			{
				/*
				IOntologyBuilder _prodBuilder = ModelGeneratorFactory.getProductModel();
				_prodBuilder.buildBaseSchemaModel();
				OntModel _model = _prodBuilder.buildBaseInstanceModel();
				OntologyUtil.getInstance().printOntology(_model, Constants.prodOntFile, false,Constants.productNS);
				*/
				
				if(_opMode.equals("build"))
					buildOtologyModels();				
				if(_opMode.equals("infer"))
					runInferenceEngine();
				if(_opMode.equals("intent"))
					testIntentCase();	   
				if(_opMode.equals("intent_sample"))
					testIntentCaseSample();	   
				if(_opMode.equals("intent_sample_full"))
					testIntentCaseSampleFull();	   				
				
			}
			catch(Exception excp)
			{
				excp.printStackTrace();;
			}				
			
		
	}

	
	private void init(String [] args)
	{
		//System.out.println("NUm parms:"+args.length);
		if(args.length<2)
		{
			System.out.println("Command is: java -jar extemp.engage.jar -mode [build/infer] -props emantic_events.properties");
			System.exit(0);
		}
		else
		{
			String mode = args[1];
			_opMode=mode;	
				
		}
		if(args.length>2)
		{
			_eventSampleFileName=args[3];
		}
		if(args.length>4)
		{
			_propertiesFileName=args[5];
		}
		
	}
	
	public static OntModel buildOtologyModels() throws Exception
	{
		  IOntologyBuilder _personBuilder = ModelGeneratorFactory.getPersonModel();
			_personBuilder.buildBaseSchemaModel();
			OntModel _model = _personBuilder.buildBaseInstanceModel();
			_personBuilder.registerOntology(_model);
			
			OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.personBaseURI), Constants.personOntFile, false,Constants.personNS);
			OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.productBaseURI), Constants.prodOntFile, false,Constants.productNS);
			OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.placeBaseURI), Constants.placeOntFile, false,Constants.placeNS);
			
			return _model;
	}
	public static void runInferenceEngine() throws Exception
	{
		//OntModel _personOnt = OntologyUtil.getInstance().getOntologyModel(Constants.personOntFile);
		//OntModel _prodOnt = OntologyUtil.getInstance().getOntologyModel(Constants.prodOntFile);
		//OntModel _placeOnt = OntologyUtil.getInstance().getOntologyModel(Constants.placeOntFile);	
		
		//_personOnt.add(_prodOnt);
		//_personOnt.add(_placeOnt);
		
		OntModel _personOnt=buildOtologyModels();
		
		InferenceHarness _harness = new InferenceHarness(_personOnt);
		_harness.runReasoner(Constants.rdfsReasoner, _personOnt);
		//_harness.runReasoner(Constants.owlReasoner, _personOnt);
		//_harness.runReasoner(Constants.owlMinisReasoner, _personOnt);
		//_harness.runReasoner(Constants.transitiveReasoner, _personOnt);
		
		
		
	}
	public static void testIntersection() throws Exception
	{
		OntModel _personOnt=buildOtologyModels();
	}
	public static void testListener() throws Exception
	{
		
	}
	public static void testIntentCase() throws Exception
	{
		
		  IOntologyBuilder _intentBuilder = ModelGeneratorFactory.getIntentModel();
		  _intentBuilder.buildBaseSchemaModel();
		  OntModel _intentModel = _intentBuilder.buildBaseInstanceModel();
		  _intentBuilder.registerOntology(_intentModel);
		  
		  IOntologyBuilder _eventBuilder = ModelGeneratorFactory.getEventModel();		
		  _eventBuilder.buildBaseSchemaModel();
		  OntModel _eventModel = _eventBuilder.buildBaseInstanceModel();
		  _eventBuilder.registerOntology(_eventModel);
		
		  		  
		  OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.intentBaseURI), Constants.intentOntFile, false,Constants.intentNS);
		  OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), Constants.eventOntFile, false,Constants.eventNS);
		  
		  //
		  
		  IntentDisoveryEngine _engine = new IntentDisoveryEngine(_intentModel);
		  List _candidateStmts = _engine.getCandidateIntentStatements(_eventModel,((EventOntologyBuilder)_eventBuilder).getPersonModel(),((EventOntologyBuilder)_eventBuilder).getProductModel());
		  //List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.rdfsReasoner);
		  List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.owlReasoner);
		  
		  
		  //list all the actual intents....
		
	}
	
	private static void testIntentCaseSample() throws Exception
	{
		System.out.println("sample filename:"+_eventSampleFileName);
		String _eventDoc = OntologyConverterUtil.getInstance().getXMLDocFromFile(_eventSampleFileName);
		//String _intentList = getIntentFromEvents(_eventDoc); 
		
		String _intentList = getIntentFromEvents2(_eventDoc); 
		
		System.out.println("Intent List 1:"+_intentList);
		//String _intentList2 = getIntentFromEvents2(_eventDoc); 
		//System.out.println("Intent List 2:"+_intentList);		
	}
	public static void testIntentCaseSampleFull()
	{
		String _rerList;
		
		System.out.println("sample filename:"+_eventSampleFileName);
		
		String _eventDoc = OntologyConverterUtil.getInstance().getXMLDocFromFile(_eventSampleFileName);
		//String _intentList = getIntentFromEvents(_eventDoc); 
		
		//get a List of Evetn docs and iterate through it.
		
		List _eventDocList = OntologyConverterUtil.getInstance().getEventDocList(_eventDoc);
		
		Iterator _it = _eventDocList.iterator();
		try
		{
			while(_it.hasNext())
			{
				String _eventDocElement = (String) _it.next();
				String _intentList = getIntentFromEvents2(_eventDocElement); 			
				System.out.println("Intent List 1:"+_intentList);
			}
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	
	public static String getIntentFromEvents(String pEventList) throws Exception
	{
		IOntologyBuilder _eventBuilder = ModelGeneratorFactory.getEventModel();	
		
		OntModel _eventModel = OntologyConverterUtil.getInstance().convertXMLToEventModel(pEventList,_eventBuilder);		
		
		IOntologyBuilder _intentBuilder = ModelGeneratorFactory.getIntentModel();
		 _intentBuilder.buildBaseSchemaModel();
		 OntModel _intentModel = _intentBuilder.buildBaseInstanceModel();
		_intentBuilder.registerOntology(_intentModel);
		
		 OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.intentBaseURI), Constants.intentOntFile, false,Constants.intentNS);
		  OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), Constants.eventOntFile, false,Constants.eventNS);
		
		
		  IntentDisoveryEngine _engine = new IntentDisoveryEngine(_intentModel);
		  List _candidateStmts = _engine.getCandidateIntentStatements(_eventModel,((EventOntologyBuilder)_eventBuilder).getPersonModel(),((EventOntologyBuilder)_eventBuilder).getProductModel());
		  //List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.rdfsReasoner);
		  List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.owlReasoner);
		  
		  String _intentListXml = OntologyConverterUtil.getInstance().convertIntentStmtsToXML(_intentList);
		  
		 		
		return _intentListXml;
	}
	public static String getIntentFromEvents2(String pEventList) throws Exception
	{
		 String _intentListXml=null;
		 
		IOntologyBuilder _eventBuilder = ModelGeneratorFactory.getEventModel();				
		OntModel _eventModel = OntologyConverterUtil.getInstance().convertXMLToEventModel(pEventList,_eventBuilder);		
		
		IOntologyBuilder _intentBuilder = ModelGeneratorFactory.getIntentModel();
		 _intentBuilder.buildBaseSchemaModel();
		 OntModel _intentModel = _intentBuilder.buildBaseInstanceModel();
		_intentBuilder.registerOntology(_intentModel);
		
		 OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.intentBaseURI), Constants.intentOntFile, false,Constants.intentNS);
		 //OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), Constants.eventOntFile, false,Constants.eventNS);
		
		  		
		  IntentDisoveryEngine _engine = new IntentDisoveryEngine(_intentModel);
		  List _intentList = _engine.getIntents(_eventModel, ((EventOntologyBuilder)_eventBuilder).getPersonModel(),((EventOntologyBuilder)_eventBuilder).getProductModel(), Constants.owlReasoner);
		  _engine.closeBrowsingList();
		  
		 // List _candidateStmts = _engine.getCandidateIntentStatements(_eventModel,((EventOntologyBuilder)_eventBuilder).getPersonModel(),((EventOntologyBuilder)_eventBuilder).getProductModel());
		  //List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.rdfsReasoner);
		 // List _intentList=_engine.getIntentList(_candidateStmts ,_intentModel,Constants.owlReasoner);
		  
		  _intentListXml = OntologyConverterUtil.getInstance().convertIntentStmtsToXML2(_intentList);
		  OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), Constants.eventOntFileFinal, false,Constants.eventNS);
		 
		  System.out.println("=========top entities===============");
		  List _entityList = InformationClusterDetector.getInstance().getMostPopularEntities(60, _eventModel);
		  System.out.println("==========top actions==============");
		  List _actionList=InformationClusterDetector.getInstance().getMostPopularActions(60, _eventModel);
		  System.out.println("========clusteredy entities================");
		  InformationClusterDetector.getInstance().getClusteredEntities(_entityList,_eventModel);
		  System.out.println("========clustered actions================");
		  InformationClusterDetector.getInstance().getClusteredPredicates(_actionList,_eventModel);		  
		  
		  
		return _intentListXml;
	}

	

}
