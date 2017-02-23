package com.extemp.semantic.generator;

import com.hp.hpl.jena.ontology.OntModel;

public class ModelGeneratorFactory 
{
	private static IOntologyBuilder _productModel=null,_personModel=null,_placeModel=null,_eventModel=null,_intentModel=null;
	
	public static IOntologyBuilder getProductModel()
	{
		if(_productModel==null)
			_productModel = new ProductModelBuilder();
			
		return _productModel;
	}
	public static IOntologyBuilder getPersonModel()
	{
		if(_personModel==null)
			_personModel=new PersonModelBuilder();
		return _personModel;
	}
	public static IOntologyBuilder getPlaceModel()
	{
		if(_placeModel==null)
			_placeModel=new PlaceModelBuilder();
		return _placeModel;
	}
	public static IOntologyBuilder getEventModel()
	{
		if(_eventModel==null)
			_eventModel=new EventOntologyBuilder();
		
		return _eventModel;
	}
	public static IOntologyBuilder getIntentModel()
	{
		if(_intentModel==null)
			_intentModel= new IntentOntologyBuilder();
		return _intentModel;
	}
	//================inner functions===================
	public static IOntologyBuilder getEmbeddedProductModel()
	{
		return new ProductModelBuilder();			
		
	}
	public static IOntologyBuilder getEmbeddedPersonModel()
	{
		
		return new PersonModelBuilder();
		
	}
	public static IOntologyBuilder getEmbeddedPlaceModel()
	{
		
			return new PlaceModelBuilder();
		
	}
	public static IOntologyBuilder getEmbeddedEventModel()
	{
	
			return new EventOntologyBuilder();
		
	}
	public static IOntologyBuilder getEmbeddedIntentModel()
	{
			return new IntentOntologyBuilder();
		
	}
	public static IOntologyBuilder getEmbeddedBaseModel()
	{
			return new BaseOntologyBuilder();
		
	}

}
