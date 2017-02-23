package com.extemp.semantic.generator;

import com.extemp.semantic.util.Constants;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.XSD;

public class BaseOntologyBuilder extends BaseOntModel implements IOntologyBuilder 
{
	private DatatypeProperty _classAccessCounter,_propertyAccessCounter;

	public BaseOntologyBuilder()
	{
		super(Constants.baseOntBaseURI, Constants.baseOntNS);
		// TODO Auto-generated constructor stub
	}

	@Override
	public OntModel buildBaseSchemaModel() throws Exception {
		// TODO Auto-generated method stub
		
		OntClass _baseClass = _ontModel.createClass(Constants.baseOntNS+"BaseClass");		
		_classAccessCounter = _ontModel.createDatatypeProperty(Constants.baseOntNS + "ClassCounter");
		_classAccessCounter.setDomain(_baseClass);
		_classAccessCounter.setRange(XSD.xlong); 
		
		OntProperty _baseProperty = _ontModel.createOntProperty(Constants.baseOntNS + "BaseProperty");
		_propertyAccessCounter = _ontModel.createDatatypeProperty(Constants.baseOntNS + "PropertyCounter");
		_propertyAccessCounter.setDomain(_baseClass);
		_propertyAccessCounter.setRange(XSD.xlong); 
			
		return _ontModel;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		return _ontModel;
	}

	@Override
	public void registerOntology(OntModel pModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		//_ontModel = ModelFactory.createOntologyModel();
		_ontModel.setNsPrefix("base",  Constants.baseOntNS);
		_ontModel.setDynamicImports(true);
		 Ontology _eventOnt = _ontModel.createOntology(Constants.baseOntBaseURI);
		 //Ontology _placeOnt = _ontModel.createOntology(Constants.placeNS);
		_eventOnt.addComment("BaseOntology", "EN");
		_eventOnt.addLabel("BaseOntology", "EN");	
	}

}
