package com.extemp.cem.semantic.generator;

import com.extemp.cem.util.semantic.Constants;

import com.extemp.cem.util.semantic.OntologyUtil;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.XSD;

public class EventOntologyBuilder extends BaseOntModel implements IOntologyBuilder
{
	private OntModel _personModel,_productModel,_baseModel;
	private DatatypeProperty _browseURL,_browsedItemName,_browsedItemCategory,_browsedItemID,_videoURL,_audioURL,_videoTopic,_audioTopic,_publishedContentURL,_publishedContentTopic,_publishedContentData,_publishedContentCategory,_processedPerson;
	private OntClass _videoContent,_audioContent,_browsedContent,_publishedContent,_eventID,_event;
	private OntProperty _browseProperty,_viewVideoProperty,_publishContentProperty;
	private DatatypeProperty eventID;
	
	//private ObjectProperty 

	public EventOntologyBuilder() {
		super(Constants.eventBaseURI, Constants.eventNS);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		//_ontModel = ModelFactory.createOntologyModel();
		_ontModel.setNsPrefix("event",  Constants.eventNS);
		_ontModel.setDynamicImports(true);
		 Ontology _eventOnt = _ontModel.createOntology(Constants.eventBaseURI);
		 //Ontology _placeOnt = _ontModel.createOntology(Constants.placeNS);
		_eventOnt.addComment("EventOntology", "EN");
		_eventOnt.addLabel("EventOntology", "EN");	
			
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
	   //---ad person model..
	   IOntologyBuilder _personBuilder = ModelGeneratorFactory.getEmbeddedPersonModel();
		try
		{
			_personBuilder.buildBaseSchemaModel();
			_personModel = _personBuilder.buildBaseInstanceModel();
			_personBuilder.registerOntology(_personModel);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		// _ontModel.add(_personModel);
		 _ontModel.addSubModel(_personModel);
		_productModel = ((PersonModelBuilder)_personBuilder).getProductModel();

			
	}
	public OntModel getPersonModel()
	{
		return _personModel;
	}
	public OntModel getProductModel()
	{
		return _productModel;
	}
	@Override
	public OntModel buildBaseSchemaModel() throws Exception {
		// TODO Auto-generated method stub
		
		OntClass _baseClass = _ontModel.getOntClass(Constants.baseOntNS+"BaseClass");
		OntProperty _baseProperty = _ontModel.getOntProperty(Constants.baseOntNS+"BaseProperty");
		
		OntClass _person = _personModel.getOntClass(Constants.personNS+"Person");
		OntClass _product = _personModel.getOntClass(Constants.productNS+"Product");	
		
		_event = _ontModel.createClass(Constants.eventNS + "Event");
		eventID = _ontModel.createDatatypeProperty(Constants.eventNS + "EventID");
		eventID.setDomain(_event);	
		eventID.setRange(XSD.xstring); 		
		eventID = _ontModel.createDatatypeProperty(Constants.eventNS + "TimeStamp");
		eventID.setDomain(_event);	
		eventID.setRange(XSD.dateTime); 		
		
		OntProperty _hasPerson = _ontModel.createOntProperty(Constants.eventNS+"HasPerson");		
		_event.addProperty(_hasPerson,  _person);
		
		_baseClass.addSubClass(_event);
		
		_baseProperty.addSubProperty(_hasPerson);
		
		_videoContent= _ontModel.createClass(Constants.eventNS + "VideoContent");
		_videoURL = _ontModel.createDatatypeProperty(Constants.eventNS + "VideoURL");
		_videoURL.setDomain(_videoContent);
		_videoURL.setRange(XSD.xstring); 
		_videoTopic = _ontModel.createDatatypeProperty(Constants.eventNS + "VideoTopic");
		_videoURL.setDomain(_videoContent);
		_videoURL.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_videoContent);
		
		_audioContent= _ontModel.createClass(Constants.eventNS + "AudioContent");
		_audioURL = _ontModel.createDatatypeProperty(Constants.eventNS + "AudioURL");
		_audioURL.setDomain(_videoContent);
		_audioURL.setRange(XSD.xstring); 
		_audioTopic = _ontModel.createDatatypeProperty(Constants.eventNS + "AudioTopic");
		_audioTopic.setDomain(_videoContent);
		_audioTopic.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_audioContent);
		
		
		 _browsedContent = _ontModel.createClass(Constants.eventNS + "BrowsedContent");
		_browseURL = _ontModel.createDatatypeProperty(Constants.eventNS + "BrowsedURL");
		_browseURL.setDomain(_browsedContent);
		_browseURL.setRange(XSD.xstring); 
		_browsedItemName = _ontModel.createDatatypeProperty(Constants.eventNS + "BrowsedItemName");
		_browsedItemName.setDomain(_browsedContent);
		_browsedItemName.setRange(XSD.xstring); 
		_browsedItemCategory = _ontModel.createDatatypeProperty(Constants.eventNS + "BrowsedItemCategory");
		_browsedItemCategory.setDomain(_browsedContent);
		_browsedItemCategory.setRange(XSD.xstring); 
		_browsedItemID = _ontModel.createDatatypeProperty(Constants.eventNS + "BrowsedItemID");
		_browsedItemID.setDomain(_browsedContent);
		_browsedItemID.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_browsedContent);
		
		OntProperty _hasProduct = _ontModel.createOntProperty(Constants.eventNS+"hasProduct");	
		_browsedContent.addProperty(_hasProduct, _product);			
					
		_baseProperty.addSubProperty(_hasProduct);
		
		_publishedContent= _ontModel.createClass(Constants.eventNS + "PublishedContent");
		_publishedContentURL = _ontModel.createDatatypeProperty(Constants.eventNS + "PublishedContentURL");
		_publishedContentURL.setDomain(_publishedContent);
		_publishedContentURL.setRange(XSD.xstring); 
		_publishedContentTopic = _ontModel.createDatatypeProperty(Constants.eventNS + "PublishedContentTopic");
		_publishedContentTopic.setDomain(_publishedContent);
		_publishedContentTopic.setRange(XSD.xstring); 
		_publishedContentData = _ontModel.createDatatypeProperty(Constants.eventNS + "PublishedContentData");
		_publishedContentData.setDomain(_publishedContent);
		_publishedContentData.setRange(XSD.xstring); 
		_publishedContentCategory = _ontModel.createDatatypeProperty(Constants.eventNS + "PublishedContentCategory");
		_publishedContentCategory.setDomain(_publishedContentCategory);
		_publishedContentCategory.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_publishedContent);
				
		_product.addSubClass(_browsedContent);
		_product.addSubClass(_publishedContent);
		_browsedContent.addSubClass(_videoContent);
		_browsedContent.addSubClass(_audioContent);		
		
		OntProperty _activityProperty = _ontModel.createOntProperty(Constants.eventNS + "EventActivity");
		OntProperty _consumeContentProperty = _ontModel.createOntProperty(Constants.eventNS + "ConsumeContent");
		_publishContentProperty = _ontModel.createOntProperty(Constants.eventNS + "PublishContent");
		_browseProperty = _ontModel.createOntProperty(Constants.eventNS + "Browse");		
		_viewVideoProperty = _ontModel.createOntProperty(Constants.eventNS + "ViewVideo");
		OntProperty _listenAudioProperty = _ontModel.createOntProperty(Constants.eventNS + "ListenAudio");
		OntProperty _tweetContent = _ontModel.createOntProperty(Constants.eventNS + "Tweet");
				
		_activityProperty.addSubProperty(_consumeContentProperty);
		_activityProperty.addSubProperty(_publishContentProperty);
				
		_consumeContentProperty.addSubProperty(_browseProperty);
		_consumeContentProperty.addSubProperty(_viewVideoProperty);
		_consumeContentProperty.addSubProperty(_listenAudioProperty);
		_publishContentProperty.addSubProperty(_tweetContent);		
		
		_person.addProperty(_browseProperty, _browsedContent);
		_person.addProperty(_viewVideoProperty, _videoContent);
		_person.addProperty(_listenAudioProperty, _audioContent);
		_person.addProperty(_publishContentProperty, _tweetContent);
		
		_baseProperty.addSubProperty(_activityProperty);
		
		return _ontModel;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		
		DatatypeProperty _productName=_personModel.getDatatypeProperty(Constants.productNS + "ProductName");
		DatatypeProperty _productBrand=_personModel.getDatatypeProperty(Constants.productNS + "ProductBrand");
		DatatypeProperty _productSerialNumber=_personModel.getDatatypeProperty(Constants.productNS + "ProductSerialNumber");
		DatatypeProperty _productMadeIn=_personModel.getDatatypeProperty(Constants.productNS + "ProductMadeIn");
		DatatypeProperty _itemSize=_personModel.getDatatypeProperty(Constants.productNS + "ItemSize");
		DatatypeProperty _itemGender=_personModel.getDatatypeProperty(Constants.productNS + "ItemGender");	
		DatatypeProperty _productPrice=_personModel.getDatatypeProperty(Constants.productNS + "ProductPrice");	
		DatatypeProperty _productExpiryDate=_personModel.getDatatypeProperty(Constants.productNS + "ProductExpiryDate");
		DatatypeProperty _browsingProcessed = _personModel.getDatatypeProperty(Constants.personNS + "BrowsingProcessed");
		
		DatatypeProperty _personID=_personModel.getDatatypeProperty(Constants.personNS + "ID");	
				
		OntClass _personCls = _personModel.getOntClass(Constants.personNS + "BrowsingPerson");		
		Individual _person = _personCls.createIndividual(Constants.personNS+"John_Smith");
		_person.addProperty(_personID, "JS_1234");		
		
		//OntClass _jersey = _ontModel.getOntClass(Constants.productNS + "Clothing");
		OntClass _jersey = _ontModel.getOntClass(Constants.eventNS + "BrowsedContent");
		Individual _ml_jersey = _jersey.createIndividual(Constants.eventNS+"HockeyJersey");		
		_ml_jersey.addProperty(_productName, "HockeyJersey");
		_ml_jersey.addProperty(_productBrand, "HockeyJersey");
		_ml_jersey.addProperty(_productSerialNumber, "ml123");
		_ml_jersey.addProperty(_productMadeIn, "Canada");
		_ml_jersey.addProperty(_itemSize, "S");
		_ml_jersey.addProperty(_itemGender, "M");
		_ml_jersey.addLiteral(_productPrice, 99.99);
		_ml_jersey.addProperty(_browseURL, "http;//acmesports/productcatalog/item=jersey456");
		_ml_jersey.addProperty(_browsedItemName, "HockeyJersey");
		_ml_jersey.addProperty(_browsedItemCategory, "Equipment");
		_ml_jersey.addProperty(_browsedItemID, "jersey456");		
		
		//2) build skates kids
		//OntClass _skates = _ontModel.getOntClass(Constants.productNS + "Clothing");
		OntClass _skates = _ontModel.getOntClass(Constants.eventNS + "BrowsedContent");
		Individual _skates_ml = _skates.createIndividual(Constants.eventNS+"HockeySkates");		
		_skates_ml.addProperty(_productName, "HockeySkates");
		_skates_ml.addProperty(_productBrand, "Bauer");
		_skates_ml.addProperty(_productSerialNumber, "skates123");
		_skates_ml.addProperty(_productMadeIn, "Canada");
		_skates_ml.addProperty(_itemSize, "S");
		_skates_ml.addProperty(_itemGender, "M");
		_skates_ml.addLiteral(_productPrice, 235.99);
		_skates_ml.addProperty(_browseURL, "http://acmesports/productcatalog/item=skates457");
		_skates_ml.addProperty(_browsedItemName, "Skates");
		_skates_ml.addProperty(_browsedItemCategory, "Equipment");
		_skates_ml.addProperty(_browsedItemID, "skates457");	
				
		//3) build gift card
		
		//OntClass _giftcard = _ontModel.getOntClass(Constants.productNS + "GiftCertificate");
		OntClass _giftcard = _ontModel.getOntClass(Constants.eventNS + "BrowsedContent");
		Individual _gc = _giftcard.createIndividual(Constants.eventNS+"SportsGiftCertificate_123");	
		_gc.addProperty(_productName, "GiftCertificate");
		_gc.addLiteral(_productPrice, 500.00);
		_gc.addLiteral(_productExpiryDate,_ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
		_gc.addProperty(_browseURL, "http;//acmesports/productcatalog/item=gift459");
		_gc.addProperty(_browsedItemName, "GiftCertificate_HockeyEquipmentGiftCertificate");
		_gc.addProperty(_browsedItemCategory, "GiftCertificate");
		_gc.addProperty(_browsedItemID, "gift459");	
		
		
		//4) build video watch
		Individual _video = _videoContent.createIndividual(Constants.eventNS + "PlayerInterview_PhillKessel_100");
		_video.addProperty(_browseURL, "http;//acmesports/playervideo/item=player459");
		_video.addProperty(_browsedItemName, "PhillKessel_interview_101014 ");
		_video.addProperty(_browsedItemCategory, "VideoContent");
		_video.addProperty(_browsedItemID, "player459");	
		
		//5) build tweet publish
		OntClass _tweetCls = _ontModel.getOntClass(Constants.eventNS + "PublishedContent");	
		Individual _tweet = _tweetCls.createIndividual(Constants.eventNS + "Tweet_34567");
		_tweet.addLiteral(_publishedContentURL, "http://twitter/acme_sports_hockey_games_questions");
		_tweet.addLiteral(_publishedContentTopic, "tweet:Hockey Game Dates");
		_tweet.addProperty(_publishedContentCategory, "PublishedContent");
		_tweet.addLiteral(_publishedContentData, "When does Phil Kessel Play next ?");
		
		//System.out.println("tweet="+_tweet.getOntClass(true));
		
		///******build tripples : person->browse-> product
		
		
	    _person.addProperty(_browseProperty,_ml_jersey);	   
		_person.addProperty(_browseProperty, _skates_ml);		
		_person.addProperty(_browseProperty, _gc);			 
		_person.addProperty(_publishContentProperty, _tweet);
		_person.addProperty(_viewVideoProperty, _video);		
		_person.addProperty(_browsingProcessed, "false");
		
				
				
		//System.out.println("SKates URL="+_skates_ml.getPropertyValue(_ontModel.getProperty(Constants.eventNS + "BrowsedURL")).toString());
		//System.out.println("Prodname="+_skates_ml.getPropertyValue(_ontModel.getProperty(Constants.productNS + "ProductName")).toString());
	
		return _ontModel;
	}

	@Override
	public void registerOntology(OntModel pModel) {
		// TODO Auto-generated method stub
		OntologyUtil.getInstance().addOntology(_ontModel, _baseURI);
		
	}
	

	

}
