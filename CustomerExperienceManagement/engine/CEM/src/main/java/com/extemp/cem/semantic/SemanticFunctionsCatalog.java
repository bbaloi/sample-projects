package com.extemp.cem.semantic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.extemp.cem.semantic.generator.EventOntologyBuilder;
import com.extemp.cem.semantic.generator.IOntologyBuilder;
import com.extemp.cem.semantic.generator.ModelGeneratorFactory;
import com.extemp.cem.semantic.vo.IntentVO;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.InformationClusterDetector;
import com.extemp.cem.util.semantic.OntologyConverterUtil;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Statement;

public class SemanticFunctionsCatalog 
{
	/*private static String _intentListEvent = "/Events/IntentList";
	private static String _intentListConcept="/Concepts/IntentList";
	private static String _intentConcept = "/Concepts/Intent";
	private static String _userConcept = "/Concepts/User";*/
	
	public static List getIntentsFromEvents(String pUserId, List pEventList)
	{
		List _intentList =null;
		
		System.out.println("Getting Items from Event List");		
		try
		{
						
			 _intentList = getIntentFromEvents(pUserId,pEventList);
		  
		 // System.out.println("Got Intent List - "+_intentList);
		  
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}


		return _intentList;
	}
	
	public static List getIntentFromEvents(String pUserId, List pEventList) throws Exception
	{
		// String _intentListXml=null;
		List _intentList=null;
		List _retList=null;
		 
		IOntologyBuilder _eventBuilder = ModelGeneratorFactory.getEventModel();				
		OntModel _eventModel = OntologyConverterUtil.getInstance().getOntEventModel(pEventList,_eventBuilder);		
		
		IOntologyBuilder _intentBuilder = ModelGeneratorFactory.getIntentModel();
		 _intentBuilder.buildBaseSchemaModel();
		 OntModel _intentModel = _intentBuilder.buildBaseInstanceModel();
		_intentBuilder.registerOntology(_intentModel);
		
		// OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.intentBaseURI), Constants.intentOntFile, false,Constants.intentNS);
		
		String _intentOntFile = CEMUtil.getInstance().getCEMProperty("cem.intent.ontology.location");
		
		OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.intentBaseURI), _intentOntFile, false,Constants.intentNS);
					 
		 
		  IntentDisoveryEngine _engine = new IntentDisoveryEngine(_intentModel);
		 _intentList = _engine.getIntents(_eventModel, ((EventOntologyBuilder)_eventBuilder).getPersonModel(),((EventOntologyBuilder)_eventBuilder).getProductModel(), Constants.owlReasoner);
		  _engine.closeBrowsingList();
		  
		 _retList =  createIntentList(pUserId,_intentList);
		  String _eventOntFileFinal = CEMUtil.getInstance().getCEMProperty("cem.event.final.ontology.location");
		 
		OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), _eventOntFileFinal, false,Constants.eventNS);
			 
		  
		  System.out.println("=========top entities===============");
		  List _entityList = InformationClusterDetector.getInstance().getMostPopularEntities(60, _eventModel);
		  System.out.println("==========top actions==============");
		  List _actionList=InformationClusterDetector.getInstance().getMostPopularActions(60, _eventModel);
		  System.out.println("========clusteredy entities================");
		  InformationClusterDetector.getInstance().getClusteredEntities(_entityList,_eventModel);
		  System.out.println("========clustered actions================");
		  InformationClusterDetector.getInstance().getClusteredPredicates(_actionList,_eventModel);		  
		  
		  
		return _retList;
	}

	private static List createIntentList(String pUserId,List pIntentList)
	{
		ArrayList _userList = new ArrayList();
		
		
		Iterator _it = pIntentList.iterator();
				
		while(_it.hasNext())
		{
						
			IntentVO _vo = (IntentVO) _it.next();
			String _userID = _vo.getUserId();
			User _user = new User();
			ArrayList _il = new ArrayList();
			_user.setUserId(_userID);
			_user.setIntentList(_il);
			
			
			List _stmtList = _vo.getStmtList();
			Iterator _stmtIt = _stmtList.iterator();		
			
			while(_stmtIt.hasNext())
			{
				
					Statement _stmt = (Statement) _stmtIt.next();
					String _intent = (_stmt.getObject().asResource()).getLocalName();
					System.out.println("Intent Discovered:"+_intent);
					
					Intent _intentObj = new Intent();
					_intentObj.setIntentId("");
					_intentObj.setIntentName(_intent);
					_intentObj.setUserId(_userID);
					
					_user.getIntentList().add(_intentObj);			
				
			}
				_userList.add(_user);				

		}
		return _userList;
				
	
	}

}
