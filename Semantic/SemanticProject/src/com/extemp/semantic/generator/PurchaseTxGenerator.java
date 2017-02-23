package com.extemp.semantic.generator;

import com.extemp.semantic.util.Constants;
import com.hp.hpl.jena.ontology.OntModel;

public class PurchaseTxGenerator extends BaseOntModel implements IOntologyBuilder
{

	public PurchaseTxGenerator() {
		super(Constants.purchaseBaseURI,Constants.purchaseNS);
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
	public void registerOntology(OntModel pModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

}
