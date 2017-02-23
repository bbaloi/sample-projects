package com.extemp.semantic.generator;

import com.extemp.semantic.util.Constants;
import com.extemp.semantic.util.InformationClusterDetector;
import com.extemp.semantic.util.OntologyUtil;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.XSD;
import com.hp.hpl.jena.ontology.Individual;

public class PlaceModelBuilder extends BaseOntModel implements IOntologyBuilder
{
	//private static String _placeBaseURI="http://engage/ontology/place";
	//private static String _placeNS=_placeBaseURI+"#";
	private DatatypeProperty _placeName,_placeType;
	private OntProperty _hasPlace,_placeBelongsTo;
	private OntModel _baseModel;
	
	private OntClass _planet,_continent,_country,_state, _region,_city,_address;

	public PlaceModelBuilder() {
		super(Constants.placeBaseURI, Constants.placeNS);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		//_ontModel = ModelFactory.createOntologyModel();
		_ontModel.setNsPrefix("place",  Constants.placeNS);
		_ontModel.setDynamicImports(true);
		 Ontology _placeOnt = _ontModel.createOntology(Constants.placeBaseURI);
		 //Ontology _placeOnt = _ontModel.createOntology(Constants.placeNS);
		_placeOnt.addComment("Place Ontology", "EN");
		_placeOnt.addLabel("PlaceOntology", "EN");		
		IOntologyBuilder _baseBuilder = ModelGeneratorFactory.getEmbeddedBaseModel();
		try
		{
			_baseModel=_baseBuilder.buildBaseSchemaModel();			
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		_ontModel.add(_baseModel);
		
	}

	public void registerOntology(OntModel pModel)
	{
		OntologyUtil.getInstance().addOntology(_ontModel, _baseURI);
	}

	@Override
	public OntModel buildBaseSchemaModel() throws Exception {
		// TODO Auto-generated method stub
		
		OntClass _baseClass = _ontModel.getOntClass(Constants.baseOntNS+"BaseClass");
		OntProperty _baseProperty = _ontModel.getOntProperty(Constants.baseOntNS+"BaseProperty");
		
		OntClass _place = _ontModel.createClass(Constants.placeNS + Constants.placeType);
		_placeName = _ontModel.createDatatypeProperty(Constants.placeNS + "placeName");
		_placeName.setDomain(_place);
		_placeName.setRange(XSD.xstring); 
		_placeType = _ontModel.createDatatypeProperty(Constants.placeNS + "placeType");
		_placeType.setDomain(_place);
		_placeType.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_place);
		
		_planet = _ontModel.createClass(Constants.placeNS + Constants.planetType);
		_continent = _ontModel.createClass(Constants.placeNS + Constants.continentType);
		_country = _ontModel.createClass(Constants.placeNS + Constants.countryType);
		_state = _ontModel.createClass(Constants.placeNS + Constants.stateType);
		_region= _ontModel.createClass(Constants.placeNS + Constants.regionType);
		_city = _ontModel.createClass(Constants.placeNS + Constants.cityType);
		_address = _ontModel.createClass(Constants.placeNS + Constants.addressType);
		
		_place.addSubClass(_planet);
		_place.addSubClass(_continent);
		_place.addSubClass(_country);
		_place.addSubClass(_state);
		_place.addSubClass(_region);
		_place.addSubClass(_city);
		_place.addSubClass(_address);
		
		_hasPlace = _ontModel.createObjectProperty(Constants.placeNS+"hasPlace");
		_placeBelongsTo = _ontModel.createObjectProperty(Constants.placeNS+"placeBelongsTo");
		_place.addProperty(_hasPlace, _place);
		_planet.addProperty(_hasPlace, _continent);
		_continent.addProperty(_hasPlace, _country);
		_country.addProperty(_hasPlace, _state);
		_state.addProperty(_hasPlace, _region);
		_region.addProperty(_hasPlace, _city);
		_city.addProperty(_hasPlace, _address);
		
		_address.addProperty(_placeBelongsTo, _city);
		_city.addProperty(_placeBelongsTo, _region);
		_city.addProperty(_placeBelongsTo, _state);
		_state.addProperty(_placeBelongsTo, _country);
		_country.addProperty(_placeBelongsTo, _continent);
		_continent.addProperty(_placeBelongsTo, _planet);
		
		_baseProperty.addSubProperty(_hasPlace);
		_baseProperty.addSubProperty(_placeBelongsTo);
		
		
		return _ontModel;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		
		Individual _earth = _planet.createIndividual(_baseNS+"Earth");
		_earth.addProperty(_placeName, "Earth");
		_earth.addProperty(_placeType, Constants.planetType);
		 InformationClusterDetector.getInstance().updateClassCounter(_earth.getOntClass(), _ontModel);
		Individual _na = _continent.createIndividual(_baseNS+"North_America");
		_na.addProperty(_placeName, "North America");
		_na.addProperty(_placeType, Constants.continentType);
		 InformationClusterDetector.getInstance().updateClassCounter(_na.getOntClass(), _ontModel);
		Individual _europe = _continent.createIndividual(_baseNS+"Europe");
		_europe.addProperty(_placeName, "Europe");
		_europe.addProperty(_placeType, Constants.continentType);
		 InformationClusterDetector.getInstance().updateClassCounter(_europe.getOntClass(), _ontModel);
		Individual _africa = _continent.createIndividual(_baseNS+"Africa");
		_africa.addProperty(_placeName, "Africa");
		_africa.addProperty(_placeType, Constants.continentType);
		 InformationClusterDetector.getInstance().updateClassCounter(_africa.getOntClass(), _ontModel);
		Individual _asia = _continent.createIndividual(_baseNS+"Asia");
		_asia.addProperty(_placeName, "Asia");
		_asia.addProperty(_placeType, Constants.continentType);		
		 InformationClusterDetector.getInstance().updateClassCounter(_asia.getOntClass(), _ontModel);
		Individual _sa = _continent.createIndividual(_baseNS+"South_America");
		_sa.addProperty(_placeName, "South America");
		_sa.addProperty(_placeType, Constants.continentType);
		 InformationClusterDetector.getInstance().updateClassCounter(_sa.getOntClass(), _ontModel);
		
		_earth.addProperty(_hasPlace, _na);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_earth.addProperty(_hasPlace, _sa);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_earth.addProperty(_hasPlace, _europe);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_earth.addProperty(_hasPlace, _africa);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_earth.addProperty(_hasPlace, _asia);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		
		Individual _ca = _country.createIndividual(_baseNS+"Canada");
		_ca.addProperty(_placeName, "Canada");
		_ca.addProperty(_placeType, Constants.countryType);
		 InformationClusterDetector.getInstance().updateClassCounter(_ca.getOntClass(), _ontModel);
		Individual _us = _country.createIndividual(_baseNS+"United_States");
		_us.addProperty(_placeName, "United States");
		_us.addProperty(_placeType, Constants.countryType);
		 InformationClusterDetector.getInstance().updateClassCounter(_us.getOntClass(), _ontModel);
		Individual _mx = _country.createIndividual(_baseNS+"Mexico");
		_mx.addProperty(_placeName, "Mexico");
		_mx.addProperty(_placeType, Constants.countryType);
		 InformationClusterDetector.getInstance().updateClassCounter(_mx.getOntClass(), _ontModel);
		
		_na.addProperty(_hasPlace, _ca)	;	
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_na.addProperty(_hasPlace, _us)	;	
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_na.addProperty(_hasPlace, _mx)	;	
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		
		
		Individual _ont = _state.createIndividual(_baseNS+"Ontario");
		_ont.addProperty(_placeName, "Ontario");
		_ont.addProperty(_placeType, Constants.stateType);
		 InformationClusterDetector.getInstance().updateClassCounter(_ont.getOntClass(), _ontModel);
		Individual _que= _state.createIndividual(_baseNS+"Quebec");
		_que.addProperty(_placeName, "Quebec");
		 InformationClusterDetector.getInstance().updateClassCounter(_que.getOntClass(), _ontModel);
		_que.addProperty(_placeType, Constants.stateType);
		Individual _al= _state.createIndividual(_baseNS+"Alberta");
		_al.addProperty(_placeName, "Alberta");
		_al.addProperty(_placeType, Constants.stateType);		
		 InformationClusterDetector.getInstance().updateClassCounter(_al.getOntClass(), _ontModel);
		Individual _cal = _state.createIndividual(_baseNS+"California");
		_cal.addProperty(_placeName, "California");
		_cal.addProperty(_placeType, Constants.stateType);
		 InformationClusterDetector.getInstance().updateClassCounter(_ca.getOntClass(), _ontModel);
		
		_ca.addProperty(_hasPlace, _ont);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_ca.addProperty(_hasPlace, _que);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_ca.addProperty(_hasPlace, _al);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		
		Individual _toronto = _city.createIndividual(_baseNS+"Toronto");
		_toronto.addProperty(_placeName, "Toronto");
		_toronto.addProperty(_placeType, Constants.cityType);
		InformationClusterDetector.getInstance().updateClassCounter(_toronto.getOntClass(), _ontModel);
		Individual _montreal = _city.createIndividual(_baseNS+"Montreal");
		_montreal.addProperty(_placeName, "Montreal");
		_montreal.addProperty(_placeType, Constants.cityType);
		InformationClusterDetector.getInstance().updateClassCounter(_montreal.getOntClass(), _ontModel);
		Individual _calgary = _city.createIndividual(_baseNS+"Calgary");
		_calgary.addProperty(_placeName, "Calgary");
		_calgary.addProperty(_placeType, Constants.cityType);
		InformationClusterDetector.getInstance().updateClassCounter(_calgary.getOntClass(), _ontModel);
		Individual _la = _city.createIndividual(_baseNS+"Los_Angeles");
		_la.addProperty(_placeName, "Los Angeles");
		_la.addProperty(_placeType, Constants.cityType);
		InformationClusterDetector.getInstance().updateClassCounter(_la.getOntClass(), _ontModel);
		
		_ont.addProperty(_hasPlace, _toronto);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_que.addProperty(_hasPlace, _montreal);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		_al.addProperty(_hasPlace, _calgary);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_hasPlace, _ontModel);
		
		
		return _ontModel;
	}



}
