package com.extemp.semantic.control;

import java.util.List;

import com.extemp.semantic.generator.EventOntologyBuilder;
import com.extemp.semantic.generator.IOntologyBuilder;
import com.extemp.semantic.generator.ModelGeneratorFactory;
import com.extemp.semantic.handler.IntentDisoveryEngine;
import com.extemp.semantic.inference.InferenceHarness;
import com.extemp.semantic.util.Constants;
import com.extemp.semantic.util.OntologyUtil;
import com.hp.hpl.jena.ontology.OntModel;

public class EngageMainOld 
{
	//private static boolean _buildMode=true;
	private static String _opMode;
	
	public EngageMainOld(String [] args)
	{
		init(args);
	}
	
	public static void main(String [] args)
	{
		EngageMainOld _main = new EngageMainOld(args);
	
		
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
				   
				
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();;
		}
		
		
		
	}
	
	private void init(String [] args)
	{
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

}
