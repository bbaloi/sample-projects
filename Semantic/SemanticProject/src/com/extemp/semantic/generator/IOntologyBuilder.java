package com.extemp.semantic.generator;

import com.hp.hpl.jena.ontology.OntModel;

public interface IOntologyBuilder 
{
	public OntModel buildBaseSchemaModel() throws Exception;
	public OntModel buildBaseInstanceModel() throws Exception;
	public void registerOntology(OntModel pModel);

}
