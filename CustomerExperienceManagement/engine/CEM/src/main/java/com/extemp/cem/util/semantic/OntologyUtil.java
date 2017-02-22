package com.extemp.cem.util.semantic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.extemp.cem.semantic.CustomerProfileUtil;
import com.extemp.cem.util.CEMUtil;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.FileManager;

public class OntologyUtil 
{
	
	private static OntologyUtil _instance=null;
	private HashMap<String, OntModel> _ontologyMap; 
	private int _counter=0;
	
	public OntologyUtil()
	{
		_ontologyMap = new HashMap();
	}
	
	public int getCounter()
	{
		return _counter++;
	}
	public static  OntologyUtil getInstance()
	{
		if (_instance==null)
			_instance = new OntologyUtil();
		
		return _instance;
	}
	
	public void printOntology(OntModel pModel, String pPath, boolean pSysOut, String pNameSpace )
	{
		
		try{
			
			
			if(pSysOut)
			{
				System.out.println("writing out the Ontolgy model - "+pNameSpace+" to Sys.out");
				System.out.print(pModel.toString());
			}
			else
			{
				System.out.println("writing out the Ontolgy model - "+pNameSpace+"- to File");
				File file=new File(pPath);
				//file.createNewFile();
				FileOutputStream f1=new FileOutputStream(file,false);				
				RDFWriter d = pModel.getWriter("RDF/XML-ABBREV");
				d.setProperty( "xmlbase", pNameSpace );
				d.write(pModel,f1,pNameSpace);
				f1.close();
				
			}
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	public synchronized OntModel getOntologyModel(String pFilePath)
	{
		OntModel _ontModel;
		
		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec( OntModelSpec.OWL_MEM);
		s.setDocumentManager( mgr );
		_ontModel = ModelFactory.createOntologyModel( s, null );
		// use the FileManager to open the ontology from the filesystem
		
		//OntModel _model = CustomerProfileUtil.getInstance().loadOntModel(CEMUtil.getInstance().getCustomerProfileOntology());				
		try
		{
			FileInputStream _fs = new FileInputStream(new File(pFilePath));
			_ontModel.read( _fs, "" );
			_fs.close();
		
			
			
			/*InputStream in = FileManager.get().open(pFilePath);
		
			if (in == null) 
			{
			  throw new IllegalArgumentException( "File: " + pFilePath + " not found"); 
			}
			
			// read the ontology file
			_ontModel.read( in, "" );
			*/
			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
	
		return _ontModel;
	}
	public void queryClass()
	{
		
	}
	public void queryProductInstance(String pProductType, String pProductName, OntModel pModel)
	{
		/*
		String queryString = 
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
                "PREFIX product: <http://engage/ontology/product> " +                
        		"SELECT ?name  ?brand ?type WHERE { " +
        		"    ?person foaf:mbox <mailto:alice@example.org> . " +
        		"    ?person foaf:name ?name . " +
        		"}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, pModel);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.outputAsJSON(System.out, results);
            while ( results.hasNext() ) {
                QuerySolution soln = results.nextSolution();
                Literal name = soln.getLiteral("name");
                System.out.println(name);
            }
        } finally {
            qexec.close();
        }
	*/
	}
	public void addOntology(OntModel pModel, String pURI)
	{
		_ontologyMap.put( pURI, pModel);
	}
	public OntModel getOntology(String pURI)
	{
		return (OntModel)_ontologyMap.get(pURI);
	}
	public boolean isIndividualOfType(Individual pIndividual,OntClass pClass)
	{
		boolean _retVal=false;
		OntClass cls=null;;
		ArrayList _superClassList = new ArrayList();
		
		
		 for (Iterator xy = pIndividual.listOntClasses(false); xy.hasNext(); )
		 {
		    		cls = (OntClass) xy.next();		    		
		    		
		    			OntClass _scls = cls;
		    			System.out.println( "Object has rdf:type " + cls.getLocalName() );  
		    			
		    			while(_scls.hasSuperClass())
		    			{						    				 
		    				//if(cls.hasSuperClass(pClass,true))		
		    				if(_scls.getURI().equals(pClass.getURI()))		 
		    				{				    			
				    			//System.out.println("*****FOund It*****");
		    					_retVal=true;
		    					//break;
				    		}
				    		    				
		    				_scls = _scls.getSuperClass();
		    				_superClassList.add(_scls); 				
		    			
		    			}
		    		
		    }
		 
		 if(_retVal)
		 {
			 Iterator _it = _superClassList.iterator();
			 while(_it.hasNext())
				 System.out.println("Super class of "+ cls.getLocalName()+" is "+((OntClass)_it.next()).getLocalName());
		 }
		
		return _retVal;
		
	}
	public void printProductproperties(Individual pProd, OntModel pProdModel)
	{
		 
		 System.out.println(" # Product LocalName="+pProd.getLocalName());
		 System.out.println("ProductName:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductName")));
		 System.out.println("BrandName:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductBrand")));
		 System.out.println("SerialNumber:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductSerialNumber")));		
		 System.out.println("MadeIn:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductMadeIn")));		
		 System.out.println("ExpiryDate:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductExpiryDate")));	
		 System.out.println("Price:"+pProd.getPropertyValue(pProdModel.createDatatypeProperty(Constants.productNS+"ProductPrice")));		
		 System.out.println("class="+pProd.getClass().getName());
		 System.out.println("type="+pProd.getRDFType().getLocalName());
		 System.out.println("uri="+pProd.getURI());
	}


}
