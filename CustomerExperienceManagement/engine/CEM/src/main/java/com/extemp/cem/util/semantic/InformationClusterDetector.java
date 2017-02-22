package com.extemp.cem.util.semantic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.extemp.cem.semantic.vo.EntityVO;
import com.extemp.cem.util.CEMUtil;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class InformationClusterDetector 
{
	private static InformationClusterDetector _instance=null;
	
	private String _classCntrQuery= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX product: <http://engage/ontology/product#>"+
			"PREFIX base: <http://engage/ontology/base#>"+
			"SELECT ?subject ?object"+
				"WHERE { ?subject rdfs:subClassOf* base:BaseClass;"+
					"base:ClassCounter ?clscounter."+
					"FILTER(?clscounter>3)}";
				
	 private String _propertyCntrQuery="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX product: <http://engage/ontology/product#>"+
			"PREFIX base: <http://engage/ontology/base#>"+
			"SELECT ?subject ?object"+
				"WHERE { ?subject rdfs:subPropertyOf* base:BaseProperty;"+
					"base:PropertyCounter ?propcounter."+
					"FILTER(?propcounter>1)}";
	 
	 private String getAllClassCntrQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			 "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
			 "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
			 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			 "PREFIX base: <http://engage/ontology/base#>"+
			 "SELECT *"+
			 	"WHERE { ?subject rdfs:subClassOf* base:BaseClass;"+
			 	"base:ClassCounter ?clscounter;";
	 
	 private String getAllPropertyCntrQuery="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX product: <http://engage/ontology/product#>"+
				"PREFIX base: <http://engage/ontology/base#>"+
				"SELECT *"+
					"WHERE { ?subject rdfs:subPropertyOf* base:BaseProperty;"+
						"base:PropertyCounter ?propcounter;";
						 
	
		
	public static InformationClusterDetector getInstance()
	{
		if(_instance==null)
			_instance = new InformationClusterDetector();
		return _instance;
	}
	
	public long updateClassCounter(OntClass pClass,OntModel pModel)
	{
		
		//OntClass _baseClass=OntologyUtil.getInstance().getBaseClass(pClass);
		
		
		long _cntr;
		DatatypeProperty _classCounter = pModel.getDatatypeProperty(Constants.baseOntNS+"ClassCounter");
		
		if(pClass.getPropertyValue(_classCounter)== null)
			_cntr=0;
		else 
			_cntr = pClass.getPropertyValue(_classCounter).asLiteral().getLong();
		
		_cntr++;
		pClass.setPropertyValue(_classCounter, pModel.createTypedLiteral(_cntr));
		//System.out.println("Class "+pClass.getURI()+" cntr="+_cntr);
		
		return _cntr;
		
	}
	
	public long updatePropertyCounter(OntProperty pProp,OntModel pModel)
	{
		//OntProperty _baseProp=OntologyUtil.getInstance().getBaseProperty(pProp);
		
		long _cntr;
		DatatypeProperty _propertyCounter = pModel.getDatatypeProperty(Constants.baseOntNS+"PropertyCounter");
		
		if(pProp.getPropertyValue(_propertyCounter)== null)
			_cntr=0;
		else 
			_cntr = pProp.getPropertyValue(_propertyCounter).asLiteral().getLong();
		_cntr++;
		pProp.setPropertyValue(_propertyCounter, pModel.createTypedLiteral(_cntr));
	//	System.out.println("Property "+pProp.getURI()+" cntr="+_cntr);
		
		return _cntr;
	}
	/*
	public long updatePropertyCounter(Property pProp,OntModel pModel)
	{
		//OntProperty _baseProp=OntologyUtil.getInstance().getBaseProperty(pProp);
		
		long _cntr;
		DatatypeProperty _propertyCounter = pModel.getDatatypeProperty(Constants.baseOntNS+"PropertyCounter");
		
		if(pProp.getPropertyResourceValue(_propertyCounter)== null)
			_cntr=0;
		else 
			_cntr = pProp.getPropertyResourceValue(_propertyCounter).asLiteral().getLong();
		_cntr++;
		pProp.addLiteral(_propertyCounter, _cntr);
		pProp.setPropertyResourceValue(_propertyCounter, pModel.createTypedLiteral(_cntr));
		System.out.println("Property "+pProp.getURI()+" cntr="+_cntr);
		
		return _cntr;
	}
	*/
	//=================================
	public List getMostPopularEntities(int pTopPercentage,OntModel pModel)
	{
		long _totalClassCntrInstances = 0;
		long _biggestNumber=0;
		ArrayList _entList = new ArrayList();
		ArrayList _finalEntList = new ArrayList();
		
		//1 ) get all entities..
		//Query query = QueryFactory.create(getAllClassCntrQuery, Syntax.syntaxSPARQL_11) ;
		//Query query = QueryFactory.create(getAllClassCntrQuery, Syntax.syntaxARQ) ;		
		   
	    QueryExecution qexec = QueryExecutionFactory.create(getAllClassCntrQuery+"}", pModel);
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                RDFNode subject = soln.get("subject");
                long cntr = soln.getLiteral("clscounter").getLong();                
              //  System.out.println("# Most Populat Entities: Subject="+subject.asResource().getLocalName()+" -  Counter="+cntr);                
                _totalClassCntrInstances=_totalClassCntrInstances+cntr;
                if(cntr>_biggestNumber)
                	_biggestNumber=cntr;
                
                _entList.add(new EntityVO(subject.asResource().getURI(),cntr));
            }
        } catch(Exception excp) {
            excp.printStackTrace();
        }
        // System.out.println("Total instances:"+_totalClassCntrInstances+",biggestNUmber="+_biggestNumber);
       
		//2) persiste resutls to CSV file for perusal by SpotFire       
        	String _mostPopularEntitiesOnt = CEMUtil.getInstance().getCEMProperty("cem.most.popular.entities.location");
			//writeToFile(_entList,Constants.mostPopularEntitiesFile);
			writeToFile(_entList,_mostPopularEntitiesOnt);
		//3) transform the percentage requirement into size of Class Counter i..e 20% means all enities that have Class counter > 5 
			 long _numPercent = getPercentageFilter(pTopPercentage,_biggestNumber);
			 System.out.println(pTopPercentage+"% filter="+_numPercent);
		//4) execute the query again with the new percentatge parameter.
		String _finalQuery = getAllClassCntrQuery+"FILTER(?clscounter>"+_numPercent+")}";
		
		 QueryExecution _finalQexec = QueryExecutionFactory.create(_finalQuery, pModel);
	        try {
	            ResultSet _fResults = _finalQexec.execSelect();
	            while ( _fResults.hasNext() ) {
	                QuerySolution soln = _fResults.nextSolution();
	                RDFNode _fSubject = soln.get("subject");
	                long _fCntr = soln.getLiteral("clscounter").getLong();                
	                System.out.println("# Most Populat Entities: Subject="+_fSubject.asResource().getLocalName()+" -  Counter="+_fCntr);    
	                _finalEntList.add(new EntityVO(_fSubject.asResource().getURI(),_fCntr));
	                
	            }
	        } finally {
	            qexec.close();
	        }
		
	    
		
		return _finalEntList;
	}
	
	public List getMostPopularActions(int pTopPercentage,OntModel pModel)
	{
		long _totalActionCntrInstances = 0;
		long _biggestNumber=0;
		ArrayList _actionList = new ArrayList();
		ArrayList _finalActionList = new ArrayList();
		
		//1 ) get all entities..
		//Query query = QueryFactory.create(getAllClassCntrQuery, Syntax.syntaxSPARQL_11) ;
		//Query query = QueryFactory.create(getAllClassCntrQuery, Syntax.syntaxARQ) ;		
		   
	    QueryExecution qexec = QueryExecutionFactory.create(getAllPropertyCntrQuery+"}", pModel);
        try {
            ResultSet results = qexec.execSelect();
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                RDFNode subject = soln.get("subject");
                long cntr = soln.getLiteral("propcounter").getLong();                
                //System.out.println("# Most Populat Entities: Subject="+subject.asResource().getLocalName()+" -  Counter="+cntr);                
                   if(cntr>_biggestNumber)
                	_biggestNumber=cntr;
                
                _actionList.add(new EntityVO(subject.asResource().getURI(),cntr));
            }
        } catch(Exception excp) {
            excp.printStackTrace();
        }
       
       
		//2) persiste resutls to CSV file for perusal by SpotFire       
        	String _mostPopularActionOnt = CEMUtil.getInstance().getCEMProperty("cem.most.popular.action.location");
			//writeToFile(_actionList,Constants.mostPopularActionFile);
			writeToFile(_actionList,_mostPopularActionOnt);
		//3) transform the percentage requirement into size of Class Counter i..e 20% means all enities that have Class counter > 5 
			 long _numPercent = getPercentageFilter(pTopPercentage,_biggestNumber);
			 System.out.println(pTopPercentage+"% filter="+_numPercent);
		//4) execute the query again with the new percentatge parameter.
		String _finalQuery = getAllPropertyCntrQuery+"FILTER(?propcounter>"+_numPercent+")}";
		
		 QueryExecution _finalQexec = QueryExecutionFactory.create(_finalQuery, pModel);
	        try {
	            ResultSet _fResults = _finalQexec.execSelect();
	            while ( _fResults.hasNext() ) {
	                QuerySolution soln = _fResults.nextSolution();
	                RDFNode _fSubject = soln.get("subject");
	                long _fCntr = soln.getLiteral("propcounter").getLong();                
	                System.out.println("# Most Populat Actions: Action="+_fSubject.asResource().getLocalName()+" -  Counter="+_fCntr);    
	                _finalActionList.add(new EntityVO(_fSubject.asResource().getURI(),_fCntr));
	                
	            }
	        } finally {
	            qexec.close();
	        }
		
	    
		
		return _finalActionList;
	}

	private long getPercentageFilter(int pPercent, long pTotalInstances)
	{
		//System.out.println("percent="+pPercent+",TotalInstance="+pTotalInstances);
		
		double _tmpNum = pPercent / 100.00;
		
		//System.out.println("% = "+_tmpNum);
		double _fTmpNum = 1-_tmpNum;
		
		double fActualNum = pTotalInstances * _fTmpNum;
		//System.out.println("TotalNum= "+fActualNum);
		
		long actualNum = Math.round(fActualNum);
		
		
		return actualNum;		
		
	}
		
	public List getStmtsGivenEntity(Individual pSubject,OntModel pModel)
	{
		Property _nullPredicate = null;
		Resource _nullObj=null,_nullSubj=null;
						
		
		String ont = "http://www.mindswap.org/2004/owl/mindswappers";        
		// create an empty ontology model using Pellet spec
		OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );   
		            
		// read the file
		model.add(pModel);		               
		// get the instances of a class
		//OntClass Person = model.getOntClass( "http://xmlns.com/foaf/0.1/Person" );         
		//Iterator instances = Person.listInstances();
		StmtIterator _iterator = model.listStatements(pSubject, _nullPredicate, _nullObj);
		System.out.println("=======Subj -nul-null================");
		while(_iterator.hasNext())
		{
			System.out.println("Stmt: "+_iterator.next().toString());
		}
		System.out.println("=======null -null-obj =================");
		StmtIterator _iterator2 = model.listStatements(_nullSubj, _nullPredicate, pSubject);
		while(_iterator2.hasNext())
		{
			System.out.println("Stmt: "+_iterator2.next().toString());
		}
		
		
		
		return null;
	}
	public List getEntitiesGivenPredicate(OntProperty pProp, OntModel pModel)
	{
		
		Property _nullPredicate = null;
		Resource _nullObj=null,_nullSubj=null;
						
		
		String ont = "http://www.mindswap.org/2004/owl/mindswappers";        
		// create an empty ontology model using Pellet spec
		OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );   
		            
		// read the file
		model.add(pModel);		               
		// get the instances of a class
		//OntClass Person = model.getOntClass( "http://xmlns.com/foaf/0.1/Person" );         
		//Iterator instances = Person.listInstances();
		StmtIterator _iterator = model.listStatements(_nullSubj, pProp, _nullObj);
		System.out.println("=======Subj -nul-null================");
		while(_iterator.hasNext())
		{
			System.out.println("Stmt: "+_iterator.next().toString());
		}
		
		
		return null;
	}
	
	
	private void writeToFile(List pList, String pFileName)
	{
		//System.out.println("List size = "+pList.size());
		
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter( pFileName));		    
			
			Iterator it = pList.iterator();
			while(it.hasNext())
			{
				EntityVO _vo = (EntityVO)it.next();
				String _line = _vo.getEntityName()+","+_vo.getNumInstances();
				writer.write( _line+"\r\n");
			}
		   

		}
		catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
		
	}

	//==========================================================================
	public List getClusteredEntities(List pEntityList, OntModel pModel)
	{
		Property _nullPredicate = null;
		Resource _nullObj=null,_nullSubj=null;
		
		ArrayList _tmpList = new ArrayList(pEntityList);
		ArrayList _finalStmtList = new ArrayList();
				
		//Object [] _enytityArray = pEntityList.toArray();	
		//int _listSize = _enytityArray.length;
		
		//String ont = "http://www.mindswap.org/2004/owl/mindswappers";        
		//OntModel model = ModelFactory.createOntologyModel( PelletReasonerFactory.THE_SPEC );   
		//model.add(pModel);		               
	
		
		Reasoner _owlReasoner = ReasonerRegistry.getOWLReasoner();		
		InfModel _owlInf = ModelFactory.createInfModel(_owlReasoner, pModel);
		OntModelSpec  _ontModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
		_owlReasoner.bindSchema(pModel);
		 _ontModelSpec.setReasoner(_owlReasoner);
		 OntModel _model = ModelFactory.createOntologyModel(_ontModelSpec, pModel);
		 
		 		
		Iterator _mainIt = pEntityList.iterator();
		while(_mainIt.hasNext())
		{
			EntityVO _entity = (EntityVO) _mainIt.next();
			String _entityURI = _entity.getEntityName();			
			//StmtIterator _iterator = pModel.listStatements(pModel.getIndividual(_entityURI), _nullPredicate, _nullObj);
			StmtIterator _iterator = _model.listStatements(pModel.getIndividual(_entityURI), _nullPredicate, _nullObj);
			List _entityStmtList=_iterator.toList();
			//Statement [] _entityStmtArray = (Statement[]) entityStmtList.toArray();
			Object [] _entityStmtArray =  _entityStmtList.toArray();
			int _arrayLen = _entityStmtArray.length;
			
			/*
			ArrayList _tmpPredList = new ArrayList();			
			while(_iterator.hasNext())
			{
				
				Property _pred = _iterator.next().getPredicate();
				_tmpPredList.add(_pred.getLocalName());
			}
			*/
			//-----------
			
			Iterator _tmpIt = _tmpList.iterator();
			while(_tmpIt.hasNext())
			{
				
				EntityVO _tmpVo = (EntityVO) _tmpIt.next();
				String _tmpEntityURI = _tmpVo.getEntityName();
				if(!_tmpVo.getEntityName().equals(_entity.getEntityName()))
				{
					for(int i=0;i<_arrayLen;i++)
					{
						Statement _stmt = (Statement)_entityStmtArray[i];
						RDFNode _obj=_stmt.getObject();
						if(_obj.isResource())
						{
						
							if(_stmt.getObject().asResource().getURI().equals(_tmpEntityURI ))
							{
								_finalStmtList.add(_stmt);
							}
						}
						
					}
					
					
					/*
					StmtIterator _iterator2 = model.listStatements(_nullSubj, _nullPredicate, model.getIndividual(_tmpEntityURI));
					ArrayList _tmpPredList2 = new ArrayList();
					while(_iterator2.hasNext())
					{						
						Property _pred2 = _iterator2.next().getPredicate();
						_tmpPredList2.add(_pred2.getLocalName());
					}
					
					if(_tmpPredList.containsAll(_tmpPredList2) || _tmpPredList2.containsAll(_tmpPredList))
					{
						System.out.println("There is s connection between:" +_entityURI+" and "+ _tmpEntityURI);
						
						// find out which ones are commones and create a new sub-list of the common statements binding the two entities.
					}
					*/
				}
				
			}
			
			
		}
		
		Iterator _ix = _finalStmtList.iterator();
		while(_ix.hasNext())
		{
			System.out.println("***Cluster Stmt;"+_ix.next().toString());
		}
		
		
		return _finalStmtList;
		
	}
	public List getClusteredPredicates(List pEntityList, OntModel pModel)
	{
		Property _nullPredicate = null;
		Resource _nullObj=null,_nullSubj=null;
		
		ArrayList _tmpList = new ArrayList(pEntityList);
		ArrayList _finalStmtList = new ArrayList();
		
		
		Reasoner _owlReasoner = ReasonerRegistry.getOWLReasoner();		
		InfModel _owlInf = ModelFactory.createInfModel(_owlReasoner, pModel);
		OntModelSpec  _ontModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
		_owlReasoner.bindSchema(pModel);
		 _ontModelSpec.setReasoner(_owlReasoner);
		 OntModel _model = ModelFactory.createOntologyModel(_ontModelSpec, pModel);
		 
		 
		Iterator _mainIt = pEntityList.iterator();
		while(_mainIt.hasNext())
		{
			EntityVO _entity = (EntityVO) _mainIt.next();
			String _actionURI = _entity.getEntityName();			
			System.out.println("action-"+_actionURI);
			StmtIterator _iterator = pModel.listStatements(_nullSubj,_model.getProperty(_actionURI), _nullObj);
		    //OntProperty _action = _model.getOntProperty(_actionURI); 
		    
		  		
		    while(_iterator.hasNext()) {
		        Statement _stmt = (Statement)_iterator.next();
		        System.out.println("*** "+_stmt.toString());
		    } 
			
			/*
			List _entityStmtList=_iterator.toList();
		
			Object [] _entityStmtArray =  _entityStmtList.toArray();
			int _arrayLen = _entityStmtArray.length;
			*/
	 
		}
			
	 
		
		//1) find all subjects that share all the predicates in the list
		
		//2) find all objects tha sahre all predicates in the list
		
		
		return _finalStmtList;
	}
}
