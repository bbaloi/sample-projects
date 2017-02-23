package com.extemp.semantic.inference;

import java.io.PrintWriter;
import java.util.Iterator;

import com.extemp.semantic.handler.ProductPurchaseHandler;
import com.extemp.semantic.util.Constants;
import com.extemp.semantic.util.OntologyUtil;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
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
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class InferenceHarness 
{
	private Reasoner _rdfsReasoner,_owlMiniReasoner,_owlReasoner,_transitiveReasoner,_pelletReasoner;
	private InfModel _rdfsInf,_owlInf,_owlMiniInf,_transitiveInf,_pelletInf;
	private ProductPurchaseHandler _productHdl;
	private OntModel _ontModel;
	
	private String _sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX person: <http://engage/ontology/person360#>"+
			"PREFIX product: <http://engage/ontology/product#>"+
			"SELECT ?object"+
			"WHERE {?Wayne_Gretsky  person:Likes ?object ."+
				"?object rdf:type person:Person }";
	
	public InferenceHarness(OntModel pModel)
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
		_ontModel=pModel;
				
	}
	
	public void runReasoner(String pReasonerType,OntModel pModel)	
	{
		
		Reasoner _reasoner=null;
		InfModel _infModel=null;
		OntModelSpec _ontModelSpec=null;
		RDFNode _emptyClass=null;
		OntProperty _emptyProperty=null;
		
		if(pReasonerType.equals(Constants.rdfsReasoner))
		{
			_reasoner=_rdfsReasoner;
			_infModel=_rdfsInf;
			 _ontModelSpec = OntModelSpec.RDFS_MEM_RDFS_INF;
		}
		if(pReasonerType.equals(Constants.owlReasoner))
		{
			_reasoner=_owlReasoner;
			_infModel=_owlInf;
			 _ontModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
		}
			
		if(pReasonerType.equals(Constants.owlMinisReasoner))
		{
			_reasoner=_owlMiniReasoner;
			_infModel=_owlMiniInf;
			 _ontModelSpec = OntModelSpec.OWL_MEM_MINI_RULE_INF;
		}
		if(pReasonerType.equals(Constants.transitiveReasoner))
		{
			_reasoner=_transitiveReasoner;
			_infModel=_transitiveInf;
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
			ValidityReport validity = _infModel.validate();
			if (validity.isValid()) {
			    System.out.println("Validation OK !");
			} else {
			    System.out.println("Conflicts");
			    for (Iterator i = validity.getReports(); i.hasNext(); ) {
			        System.out.println(" - " + i.next());
			    }
			}
			
			
			PrintWriter out = new PrintWriter(System.out);		 
		  
		     System.out.println("List all fathers:");
		    ObjectProperty _hasChild = _model.getObjectProperty(Constants.personNS + "hasChild"); // this is the uri for MarriedPerson class
		    ExtendedIterator _fatherIt = _model.listResourcesWithProperty(_hasChild);
		    while(_fatherIt.hasNext()) {
		        Resource mp = (Resource)_fatherIt.next();
		        System.out.println(mp.getURI());
		    } // this code returns 2 individuals with the help of reasoner
		
						
		   System.out.println("List food subclasses:");
		   OntClass _food = pModel.getOntClass(Constants.productNS+"Food");
		   if(_food==null)
				System.out.println(" NO food found !");
		
		 ExtendedIterator _subclassIt=_food.listSubClasses();
		 while(_subclassIt.hasNext())
		 {
			 OntClass _cls = (OntClass) _subclassIt.next();
			 System.out.println(_cls.getURI());
		 }
		
	
		Individual _wayneg=pModel.getIndividual(Constants.personNS+"Wayne_Gretsky");
		Individual _mariol=pModel.getIndividual(Constants.personNS+"Mario_Lemieux");
		Individual _matts=pModel.getIndividual(Constants.personNS+"Matts_Sundin");
		
		if(_wayneg==null && _mariol==null)
			System.out.println("No individuals found");
		OntProperty _likes=pModel.getObjectProperty(Constants.personNS+"Likes");
		if(_likes==null)
			System.out.println("No - likes found");
		
		System.out.println("----Gretsky Likes:------");
		
		//StmtIterator i = _infModel.listStatements(_wayneg,_likes , _food);
		StmtIterator i = _infModel.listStatements(_wayneg,_likes ,_emptyClass);
		
		while ( i.hasNext() ) 
		{
		    Statement s = i.nextStatement();
		    System.out.println("Statement is " + s);
		    for (Iterator id = _rdfsInf.getDerivation(s); id.hasNext(); ) {
		        Derivation deriv = (Derivation) id.next();
		        deriv.printTrace(out, true);
		    }
		}
		out.flush();
		
		System.out.println("----Lemieux Likes food:------");
		//StmtIterator i2 = _infModel.listStatements(_mariol,_likes, _food.asClass());
		StmtIterator i2 = _infModel.listStatements(_mariol,_likes, _emptyClass);
		
		while ( i2.hasNext() ) 
		{
		    Statement s = i2.nextStatement();
		   // System.out.println(s.getObject().toString());
		    RDFNode _object = s.getObject();
		    OntResource _res = _object.as(OntResource.class);
		    
			 System.out.println("------Object is " + _res.getURI()+"------");
			// System.out.println( "Object has rdf:type " + ((OntClass)_res).getLocalName() );  
		    Individual _item=pModel.getIndividual(_res.getURI());		  
		   
		    if(OntologyUtil.getInstance().isIndividualOfType(_item, _food))
		    {
		    	
		    	 System.out.println("---"+_item.getLocalName()+" is a Food !");
		    	 //create a list of candidate foods
		    	 
		    }
		     
		   
		   
		}
		out.flush();
		
		//==========================
		StmtIterator i3= _infModel.listStatements(_wayneg,_emptyProperty, _emptyClass);
		System.out.println("======Wayne Gretsky properties:===============");
		String objectname;
		while ( i3.hasNext() ) 
		{
			   Statement s = i3.nextStatement();
			   String predicate = s.getPredicate().getLocalName();
			   RDFNode object = s.getObject();
			   if (object.isLiteral())
			   {
				   Literal _value = object.as(Literal.class);
				   objectname=_value.getValue().toString();
			   }
			   else
			   {
				   objectname = (s.getObject()).as(OntResource.class).getLocalName();
			   }
			  
			 //  System.out.println("Statement is " + s);
			   System.out.println("		" + predicate + " "+ objectname);
		}
		
		//======================do SPARQL====================		
		System.out.println("======SPARQL query:"+_sparqlQuery+"===============");
		
		System.out.println("======SPRQL RESULTS:===============");
		executeSparqlQuery();
		
	}
	
	public void addPurchaseTransaction()
	{
		
	}
	
	private void executeSparqlQuery()
	{
		  Query query = QueryFactory.create(_sparqlQuery);
	        QueryExecution qexec = QueryExecutionFactory.create(query, _ontModel);
	      //  ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());
	        
	        try {
	            ResultSet results = qexec.execSelect();
	            //ResultSetFormatter.outputAsXML(System.out, results)
	           // ResultSetFormatter.out(System.out, results);
	            
	            while ( results.hasNext() ) {
	                QuerySolution soln = results.nextSolution();
	                System.out.println("****"+soln.toString());
	            /*    Iterator _it = soln.varNames();
	                while(_it.hasNext())
	                {
	                	System.out.println("***"+_it.next().toString());
	                }
	               	*/
	            }
	            
	            
	        } finally {
	            qexec.close();
	        }

	}
	
	
}
