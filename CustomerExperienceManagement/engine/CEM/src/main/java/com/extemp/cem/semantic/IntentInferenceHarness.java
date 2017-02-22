package com.extemp.cem.semantic;

import java.io.PrintWriter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.extemp.cem.semantic.generator.IntentOntologyBuilder;
import com.extemp.cem.semantic.generator.ModelGeneratorFactory;
import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.InformationClusterDetector;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.extemp.cem.semantic.vo.IntentVO;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Derivation;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerFactory;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class IntentInferenceHarness 
{
	private Reasoner _rdfsReasoner=null,_owlMiniReasoner=null,_owlReasoner=null,_transitiveReasoner=null,_pelletReasoner=null;
	private InfModel _rdfsInf=null,_owlInf=null,_owlMiniInf=null,_transitiveInf=null,_pelletInf=null,_finalInfModel=null;
	private ProductPurchaseHandler _productHdl;
	private PrintWriter _out;
	private Model _unionModel;
	private OntModel _intentModel;
	private boolean _intitialised=false;
	
	/*private static String bdayRule = "@prefix ex: http://example.com/school#" +
				"[totalPoints: (?s rdf:type ex:Student)"+
				 	"(?s ex:writtenExamPoints ?pw)"+
				 	"(?s ex:labExamPoints ?lp)"+
				 	"ge(?pw,4)" +
				 	"sum(?tp,?pw,?lp)+ -> (?s ex:finalGrade ?tp)]";
				 	*/
	
	public IntentInferenceHarness(OntModel pModel)
	{
				
		_rdfsReasoner = ReasonerRegistry.getRDFSReasoner();
		_owlReasoner = ReasonerRegistry.getOWLReasoner();		
		_owlMiniReasoner = ReasonerRegistry.getOWLMiniReasoner();
		_transitiveReasoner = ReasonerRegistry.getTransitiveReasoner();

		
		_rdfsInf = ModelFactory.createInfModel(_rdfsReasoner, pModel);
		_owlInf = ModelFactory.createInfModel(_owlReasoner, pModel);
		_owlMiniInf = ModelFactory.createInfModel(_owlMiniReasoner, pModel);
		_transitiveInf = ModelFactory.createInfModel(_transitiveReasoner, pModel);
		
		_productHdl = new ProductPurchaseHandler();
		pModel.register(_productHdl);
		_intentModel=pModel;
				
	}
	
	public List runReasoner(List pCandidateStmtList,String pReasonerType,OntModel pModel)
	{
		List _retList=new ArrayList();
		
		if(!_intitialised)
			init(pReasonerType,pModel);
		
		//testOutput(pCandidateStmtList,pModel);		
		
		//1) build new model from statements
		OntModel _candidateModel = createCandidateIntentModel(pCandidateStmtList);		
		//2) get all intents that match the model - union of Events & Intent and the trim out all the null intents
		List _intentList  = getIntentList(_candidateModel,pModel,pCandidateStmtList);	
		
		//3) Obtain the Composite Intent model - either Rules or Search forintersections		
		//List _compositeIntentList = getCompositeIntents(pCandidateStmtList,_intentList);
		List _compositeIntentList = getCompositeIntents(_intentList);
		
		if(_compositeIntentList==null || _compositeIntentList.isEmpty())
			_retList = _intentList;
		else
			_retList = _compositeIntentList;
		
		//
		
		return _retList;
		
	}
	
	public List runReasoner2(Individual pInd,OntModel pEventModel)
	{
		Resource _nullSubj=null;
		Resource _nullObj=null;
		Property _nullPredicate=null;
		
		//Property _predicate = pEventModel.getProperty(Constants.eventNS+"EventActivity");
		Property _predicate = pEventModel.getProperty(Constants.eventNS+"Browse");
	
		Reasoner _reasoner = ReasonerRegistry.getOWLMicroReasoner();
		InfModel _infModel=ModelFactory.createInfModel(_reasoner, pEventModel);
		OntModelSpec _ontModelSpec = OntModelSpec.OWL_DL_MEM_RDFS_INF;
	    _ontModelSpec.setReasoner(_reasoner);
		
		_reasoner.bindSchema(pEventModel);		 
	 
	    // OntModel _model = ModelFactory.createOntologyModel(_ontModelSpec, pEventModel);
	    
		 
		StmtIterator _it=_finalInfModel.listStatements(pInd, _predicate ,_nullObj);
		 //StmtIterator _it=_infModel.listStatements(pInd, _nullPredicate,_nullObj);
		 
		 while(_it.hasNext())
			{
				Statement _stmt = (Statement) _it.next();
				System.out.println("^^^^^^^"+_stmt.toString());
			}
		 
		return null;
	}
	
	public List runReasoner(String pReasonerType,OntModel pModel,Statement pStmt)
	{
		List _retList=new ArrayList();
		RDFNode _nullObj=null;
		Property _nullPred=null;
		Resource _nullSubj=null;
		
		if(!_intitialised)
			init(pReasonerType,pModel);
		
		Resource _subj = pStmt.getSubject();
		Property _predicate=pStmt.getPredicate();
		RDFNode _obj=pStmt.getObject();
		
		//pModel.add(pStmt);
			
				
		//-------------maybe get alist of all intersections and see if there is a match ????-------
		System.out.println("************************");
		//String _predicateURI= _predicate.getURI();
		//Property _rPredicate = pModel.getProperty(_predicateURI);
		
		//_predicate.addProperty(pModel.getProperty(Constants.intentNS+"InterestProductType"), "Clothing");
		System.out.println("Item gender:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductGender")));
		System.out.println("Item type:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductType")));
		System.out.println("Item name:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductName")));
		System.out.println("Item size:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductSize")));
		
	
		//StmtIterator i = _finalInfModel.listStatements();
		//StmtIterator i = _finalInfModel.listStatements(_subj, _predicate ,_nullObj);
		StmtIterator i = _finalInfModel.listStatements(_nullSubj, _predicate ,_nullObj);
		//StmtIterator i = _finalInfModel.listStatements(_subj, _nullPred ,_nullObj);
		
		//StmtIterator i = _finalInfModel.listStatements();
		
		while ( i.hasNext() ) 
		{
		    Statement s = i.nextStatement();
		    _retList.add(s);
		    
		    
		    System.out.println("Statement is " + s);
		    for (Iterator id = _rdfsInf.getDerivation(s); id.hasNext(); ) {
		        Derivation deriv = (Derivation) id.next();
		        deriv.printTrace(_out, true);
		    }
		}
		_out.flush();
		
		
		return _retList;
		
	}
	public void runReasoner(String pReasonerType,OntModel pModel)	
	{	
		
	}
	
	private void init(String pReasonerType,OntModel pModel)
	{
		Reasoner _reasoner=null;
		OntModelSpec _ontModelSpec=null;
		RDFNode _emptyClass=null;
		OntProperty _emptyProperty=null;
		
		if(pReasonerType.equals(Constants.rdfsReasoner))
		{
			_reasoner=_rdfsReasoner;
			_finalInfModel=_rdfsInf;
			 _ontModelSpec = OntModelSpec.RDFS_MEM_RDFS_INF;
		}
		if(pReasonerType.equals(Constants.owlReasoner))
		{
			_reasoner=_owlReasoner;
			_finalInfModel=_owlInf;
			 _ontModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
		}
			
		if(pReasonerType.equals(Constants.owlMinisReasoner))
		{
			_reasoner=_owlMiniReasoner;
			_finalInfModel=_owlMiniInf;
			 _ontModelSpec = OntModelSpec.OWL_MEM_MINI_RULE_INF;
		}
		if(pReasonerType.equals(Constants.transitiveReasoner))
		{
			_reasoner=_transitiveReasoner;
			_finalInfModel=_transitiveInf;
			 _ontModelSpec = OntModelSpec.RDFS_MEM_TRANS_INF;
		}
		if(pReasonerType.equals(Constants.pelletReasoner))
		{
			_reasoner=_pelletReasoner;
		}
		
		_reasoner.bindSchema(pModel);
				 
		    _ontModelSpec.setReasoner(_reasoner);
		    OntModel _model = ModelFactory.createOntologyModel(_ontModelSpec, pModel);
		    
		    System.out.println("###----Running with "+pReasonerType+" reasoner------");
			ValidityReport validity = _finalInfModel.validate();
			if (validity.isValid()) {
			    System.out.println("Validation OK !");
			} else {
			    System.out.println("Conflicts");
			    for (Iterator i = validity.getReports(); i.hasNext(); ) {
			        System.out.println(" - " + i.next());
			    }
			}
			
			
			_out = new PrintWriter(System.out);		 
			
			_intitialised=true;
	}
	
	private OntModel createCandidateIntentModel(List pStmtList)
	{
		OntModel _candidateModel=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		
		Iterator _it = pStmtList.iterator();
		
		while(_it.hasNext())
		{
			
			//Statement _stmt = ((IntentVO)_it.next()).getStmt();
			Statement _stmt = (Statement)_it.next();
			//InformationClusterDetector.getInstance().updatePropertyCounter(_stmt.getPredicate().as(OntProperty), _intentModel);
			
			_candidateModel.add(_stmt);
		}
		
			
		return _candidateModel;
	}
	private List getIntentList(OntModel pCandidateModel, OntModel pIntentModel,List pStmtList)
	{
		ArrayList _intentList=new ArrayList();
		Resource _nullSubj=null;
		RDFNode _nullObj=null;
		
		//1) get union of two models	
		_unionModel= pCandidateModel.union(pIntentModel);
		//Model _intersectModel = pCandidateModel.intersection(pIntentModel);
		
		Iterator _stmtIt = pStmtList.iterator();
		while(_stmtIt.hasNext())
		{
			Statement _stmt = (Statement)_stmtIt.next();		
			//Statement _stmt = ((IntentVO)_stmtIt.next()).getStmt();			
			//String _userId = ((IntentVO)_stmtIt.next()).getUserId();
			
			StmtIterator _uniIt = _unionModel.listStatements(_nullSubj,_stmt.getPredicate(), _nullObj);
			System.out.println("====Union Stmts=====");
			while(_uniIt.hasNext())
			{
				Statement _innerStmt = (Statement)_uniIt.next();
				System.out.println(_innerStmt.toString());
				if(!_innerStmt.getObject().asResource().getURI().equals(Constants.intentNS+"BlankIntent"))
				{
				//	IntentVO _newvo = new IntentVO(_userId,_innerStmt);
					//_intentList.add(_newvo);
					_intentList.add(_innerStmt);
					//InformationClusterDetector.getInstance().updatePropertyCounter(_innerStmt.getPredicate().as(OntProperty.class), _intentModel);
					//InformationClusterDetector.getInstance().updateClassCounter(_innerStmt.getSubject().as(OntClass.class), _intentModel);						
									
				}
				else
				{
					//3) trim null intents
					//System.out.println("Found Blank Intent");
					//_unifiedModel.remove(_innerStmt);	
				}
					
					
				
			}
		}
			/*
		//---------------
		//System.out.println("====Union Stmts=====");
		Iterator _finIt = _intentList.iterator();
		while(_finIt.hasNext())
		{
			System.out.println(_finIt.next().toString());
		}
			
		*/
				
		//_unifiedModel=null;
		
		return _intentList;
		
		
	}
	private List getCompositeIntents(List pIntentList)
	{
		ArrayList _compIntentList = new ArrayList();
		ArrayList _tmpStmtList = new ArrayList();
		ArrayList _rawIntentList = new ArrayList();
		ArrayList _userIdList = new ArrayList();
			
		
		Property _predicate = _intentModel.getProperty(Constants.intentNS,"HasIntent");
		RDFNode _nullObj=null;
		Property _nullPredicate=null;
		
		Iterator _intentIt = pIntentList.iterator();
		while(_intentIt.hasNext())
		{
			_rawIntentList.add(((Statement) _intentIt.next()).getObject().asResource());
		}
		
		ExtendedIterator _it=_intentModel.listIntersectionClasses();
		while(_it.hasNext())
		{
			IntersectionClass _x = (IntersectionClass) _it.next();
			String _xName = _x.getLocalName();
			System.out.println("--X class:"+_xName+","+_x.getRDFType());
			
					
			ExtendedIterator _opIt = _x.listOperands();
			List _operandList = _opIt.toList();
			
				//if(pIntentList.containsAll(_operandList))
				if(_rawIntentList.containsAll(_operandList))
				//if(foundIntersection(_operandList,pIntentList))				
				{
					//System.out.println("FOUND candidate Itersection : "+ _x.getSubClass().toString());		
					System.out.println("FOUND candidate Itersection : "+ _x.toString());
					InformationClusterDetector.getInstance().updateClassCounter(_x, _intentModel);
					//_tmpStmtList.add(_x.getSubClass());			
					_tmpStmtList.add(_x);			
					
				}			
				
				
			}
		
		Iterator _itX = _tmpStmtList.iterator();
		while(_itX.hasNext())
		{
			OntClass _xClss = (OntClass)_itX.next();
			StmtIterator _stmtIt = _finalInfModel.listStatements(_xClss, _predicate, _nullObj);
			//StmtIterator _stmtIt = _finalInfModel.listStatements(_xClss, _nullPredicate, _nullObj);
			
			while(_stmtIt.hasNext())
			{
				Statement _stmt = _stmtIt.next();
				System.out.println("@@@@: "+_stmt.toString());
				//_compIntentList.add(_stmt.getObject());
				
				_compIntentList.add(_stmt);
			}
			
		}
		
		//System.out.println("---comp intent list size:"+_compIntentList.size());
		
		return _compIntentList;
		
	}
	private List getCompositeIntents(List pStmtList,List pIntentList)
	{
		ArrayList _intentList = new ArrayList();
		Resource _nullSubj=null;
		RDFNode _nullObj=null;
		Property _nullPredicate=null;
		Property _predicate = _unionModel.getProperty(Constants.intentNS+"HasIntent");
		Resource _composite  = _unionModel.getResource(Constants.intentNS+"CompositeIntent");
		
		String _rule=((IntentOntologyBuilder) ModelGeneratorFactory.getIntentModel()).getBdayRule();
		
		System.out.println("Rule: "+ _rule);
		
		Reasoner ruleReasoner = new GenericRuleReasoner(Rule.parseRules(_rule));
		
		_unionModel.add(pStmtList);
		ruleReasoner = ruleReasoner.bindSchema(_unionModel);
		
		
		InfModel _compositeIntentModel = ModelFactory.createInfModel(ruleReasoner,_unionModel);
		//StmtIterator _it = _compositeIntentModel.listStatements(_nullSubj,_predicate,_nullObj);
		StmtIterator _it = _compositeIntentModel.listStatements(_composite,_nullPredicate,_nullObj);
		System.out.println("----get Composite Intent---");
		while(_it.hasNext())
		{
			System.out.println("++++"+_it.next().toString());
		}
		
		
		return _intentList;
	}
	public void addPurchaseTransaction()
	{
		
	}
	
	private void testOutput(List pStmtList, OntModel pModel)
	{
		
		if(!_intitialised)
			init(Constants.owlReasoner,pModel);
		
		Iterator _it1 = pStmtList.iterator();
		while(_it1.hasNext())
		{
			pModel.add(((Statement) _it1.next()));
		}
		
		RDFNode _nullObj=null;
		Resource _nullSubj=null;
		Resource _subj = ((Statement)pStmtList.get(2)).getSubject();
		Property _predicate=((Statement)pStmtList.get(2)).getPredicate();
		RDFNode _obj=((Statement)pStmtList.get(2)).getObject();
		//pModel.add(pStmt);
			
					
		//-------------maybe get alist of all intersections and see if there is a match ????-------
		System.out.println("************************");
		//String _predicateURI= _predicate.getURI();
		//Property _rPredicate = pModel.getProperty(_predicateURI);
		
		//_predicate.addProperty(pModel.getProperty(Constants.intentNS+"InterestProductType"), "Clothing");
		System.out.println("Item gender:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductGender")));
		System.out.println("Item type:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductType")));
		System.out.println("Item name:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductName")));
		System.out.println("Item size:"+_predicate.as(OntProperty.class).getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductSize")));
		
	
		
		//StmtIterator i = _finalInfModel.listStatements(_subj, _predicate ,_nullObj);
		StmtIterator i = _finalInfModel.listStatements(_nullSubj, _predicate,_nullObj);
		//StmtIterator i = _finalInfModel.listStatements(_subj, _nullPred ,_nullObj);
		
		//StmtIterator i = _finalInfModel.listStatements();
		
		while ( i.hasNext() ) 
		{
			System.out.println("###"+i.next().toString());
		}
	}
	
	private boolean foundIntersection(List pOperandList,List pIntentList)
	{
		boolean _retValue = false;
		int _operandListCntr =0;
		int _operandListSize=pOperandList.size();
		
		
		Iterator _opIt = pOperandList.iterator();
		Iterator _intentIt = pIntentList.iterator();
		
		System.out.println("*****************************************");
		/*
		
		while(_intentIt.hasNext())
		{
			System.out.println("**intent:"+_intentIt.next());
			
		}
		while(_opIt.hasNext())
		{
			System.out.println("**operand:"+_opIt.next());
			
		}
		*/
		
		while(_opIt.hasNext())
		{
			OntClass _operand = (OntClass) _opIt.next();
			
			while(_intentIt.hasNext())
			{
				Statement _intentStmt = (Statement) _intentIt.next();
				System.out.println("operand:"+_operand+" , intent="+_intentStmt.getObject().asResource().getLocalName());
				if(_operand.getLocalName().equals(_intentStmt.getObject().asResource().getLocalName()))
				{
					_operandListCntr++;
					System.out.println("****Found operand in intent List: "+_operand.getLocalName()+"*****");
				}
			}
			/*
			if(pIntentList.contains(_operand))
			{
				_operandListCntr++;
				System.out.println("Found operand in intent List: "+_operand.getLocalName());
			}
			*/
		}
		
		System.out.println(_operandListCntr+":"+_operandListSize);
		if(_operandListCntr==_operandListSize)
		{ 
			System.out.println("Found the matching X");
			_retValue=true;
		}
		
		return _retValue;
		
	}
}
