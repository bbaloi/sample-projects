package com.extemp.cem.semantic;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.ontology.OntModel;


public class CustomerProfileUtil 
{	
	
	private OntModel _customerOntModel; 
	
	private static CustomerProfileUtil instance=null;
	
	public CustomerProfileUtil()
	{
		
	}
	
	public static CustomerProfileUtil getInstance()
	{
		if(instance==null)
			instance = new CustomerProfileUtil();
		
		return instance;
	}
	
	
	
	
	public OntModel loadOntModel(String pCustomerOntologyFile)
	{
		_customerOntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		_customerOntModel.read(pCustomerOntologyFile, null);
		
		return _customerOntModel;
		
	}
	public OntModel getOntModel()
	{
		return _customerOntModel;
	}

}
