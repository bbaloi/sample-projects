package com.extemp.semantic.generator;

import com.extemp.semantic.util.Constants;
import com.hp.hpl.jena.ontology.OntModel;

public class EntertainmentModelBuilder extends BaseOntModel implements IOntologyBuilder 
{

	public EntertainmentModelBuilder() {
		super(Constants.entertainmentBaseURI, Constants.entertaimentNS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public OntModel buildBaseSchemaModel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerOntology(OntModel pModel) {
		// TODO Auto-generated method stub
		
	}

}
