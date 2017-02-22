package com.extemp.cem.semantic.generator;

import com.hp.hpl.jena.ontology.OntModel;

public abstract class BaseOntModel
{
	
	protected String _baseURI;
	protected String _baseNS;	
	
	protected OntModel _ontModel;
	
	public BaseOntModel(String pURI, String pNS)
	{
		_baseURI = pURI;
		_baseNS=pNS;
		init();
	}
	protected abstract void init();
}
