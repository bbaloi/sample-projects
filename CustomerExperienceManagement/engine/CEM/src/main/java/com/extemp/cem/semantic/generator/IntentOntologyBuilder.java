package com.extemp.cem.semantic.generator;

import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.HasValueRestriction;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.vocabulary.XSD;

public class IntentOntologyBuilder extends BaseOntModel implements IOntologyBuilder 
{
	private OntModel _prodModel,_unionModel=null,_baseModel;
	private DatatypeProperty _interestProductName,_interestProductType,_interestProductSize,_interestProductGender,_interestProductCost,_interestProductExpiryDate;
	private OntClass _activity,_intent,_browseActivity,_viewActivity,_publishActivity,_listenActivity,_intentPersonalPurchase,_intentChildPurchase,_intentGiftPurchase,_intentAdultPurchase;
	private OntClass _intentChildBirthdayPurchase,_intentAdultBirthdayPurchase,_intentPubContentTopicPurchase;
	private OntClass _intentVideoContentTopicPurchase,_intentAudioContentTopicPurchase,_compositeIntent,_intentBoxSuitePurchase,_intentGameAttend,_intentCateringPurchase,_intentTicketPurchase;
	private OntClass _intentCorporateEventPurchase;
	private OntProperty _interest,_interestChildProduct,_interestAdultProduct,_interestVideoContent,_interestAudioContent,_interestProductGift,_interestPersonalProduct,_interestPubContent;
	private OntProperty _hasIntent,_interestBoxSuite,_interestCatering,_interestSchedule,_interestTicketing;
	private DatatypeProperty _interestVideoTopic,_interestAudioTopic,_inerestPublishTopic,_interestPublishContent;
	
	/*private static String _bdayRule = "@PREFIX intent:<"+Constants.intentNS+">. [bdayRule: (?activity rdf:type intent:BrowseActivity), "+
			 			"(?intent:InterestProductSize rdf:eq 'S'), "+
			 			"(?intent:InterestProductType rdf:eq 'GiftCertificate') "+
			 		 	" -> makeInstance(?intent:CompositeIntent, intent:hasIntent, intent:IntentBirthdayPurchase)]";
			 		 	*/
	/*
	private static String _bdayRule = "[bdayRule: (?activity rdf:type " +Constants.intentNS+":BrowseActivity), "+
 			"(?"+Constants.intentNS+":InterestProductSize rdf:eq 'S'), "+
 			"(?"+Constants.intentNS+":InterestProductType rdf:eq 'GiftCertificate') "+
 		 	" -> makeInstance(?"+Constants.intentNS+":CompositeIntent, "+Constants.intentNS+":HasIntent,"+Constants.intentNS+":IntentBirthdayPurchase)]";
 		 	*/
	
	private static String _bdayRule = "[bdayRule: (?x rdf:type "+Constants.intentNS+":InterestProductSize)-> print('FOUND BDAY')]";
			 		 	
			 			
	
	
	public IntentOntologyBuilder() {
		super(Constants.intentBaseURI, Constants.intentNS);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		//_ontModel = ModelFactory.createOntologyModel();
		_ontModel.setNsPrefix("intent",  Constants.intentNS);
		_ontModel.setDynamicImports(true);
		 Ontology _intentOnt = _ontModel.createOntology(Constants.intentBaseURI);
		 //Ontology _placeOnt = _ontModel.createOntology(Constants.placeNS);
		_intentOnt.addComment("IntentOntology", "EN");
		_intentOnt.addLabel("IntentOntology", "EN");	
		
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
	public String getBdayRule()
	{
		return _bdayRule;
	}

	@Override
	public OntModel buildBaseSchemaModel() throws Exception {
		// TODO Auto-generated method stub
		
		
		OntClass _baseClass = _ontModel.getOntClass(Constants.baseOntNS+"BaseClass");
		OntProperty _baseProperty = _ontModel.getOntProperty(Constants.baseOntNS+"BaseProperty");
		
		//-----root structure-----
		_intent= _ontModel.createClass(Constants.intentNS + "Intent");		
		_compositeIntent =_ontModel.createClass(Constants.intentNS + "CompositeIntent");	
		_intent.addSubClass(_compositeIntent);
		
		_baseClass.addSubClass(_intent);
		
		_activity= _ontModel.createClass(Constants.intentNS + "Activity");	
		
		_baseClass.addSubClass(_activity);
		
		_interest= _ontModel.createOntProperty(Constants.intentNS + "Interest");				
		_interestProductName = _ontModel.createDatatypeProperty(_baseNS + "InterestProductName");
		_interestProductName.setDomain(_interest);
		_interestProductName.setRange(XSD.xstring);
		_interestProductType = _ontModel.createDatatypeProperty(_baseNS + "InterestProductType");
		_interestProductType.setDomain(_interest);
		_interestProductType.setRange(XSD.xstring);
		_interestProductSize= _ontModel.createDatatypeProperty(_baseNS + "InterestProductSize");
		_interestProductName.setDomain(_interest);
		_interestProductName.setRange(XSD.xstring);
		_interestProductGender=_ontModel.createDatatypeProperty(_baseNS + "InterestProductGender");
		_interestProductGender.setDomain(_interest);
		_interestProductGender.setRange(XSD.xstring);				
		_interestProductCost=_ontModel.createDatatypeProperty(_baseNS + "InterestProductCost");
		_interestProductCost.setDomain(_interest);
		_interestProductCost.setRange(XSD.xdouble);	
		_interestProductExpiryDate=_ontModel.createDatatypeProperty(_baseNS + "InterestProductExpiry");
		_interestProductExpiryDate.setDomain(_interest);
		_interestProductExpiryDate.setRange(XSD.date);	
		_interestVideoTopic = _ontModel.createDatatypeProperty(_baseNS + "InterestVideoTopic");
		_interestVideoTopic.setDomain(_interest);
		_interestVideoTopic.setRange(XSD.xstring);
		_interestAudioTopic = _ontModel.createDatatypeProperty(_baseNS + "InterestAudioTopic");
		_interestAudioTopic.setDomain(_interest);
		_interestAudioTopic.setRange(XSD.xstring);
		
		_baseProperty.addSubProperty(_interest);
		
		_interestChildProduct= _ontModel.createOntProperty(Constants.intentNS + "InterestChildProduct");	
		_interestAdultProduct= _ontModel.createOntProperty(Constants.intentNS + "InterestAdultProduct");	
		_interestVideoContent= _ontModel.createOntProperty(Constants.intentNS + "InterestVideoContent");	
		_interestAudioContent= _ontModel.createOntProperty(Constants.intentNS + "InterestAudioContent");	
		_interestProductGift= _ontModel.createOntProperty(Constants.intentNS + "InterestProductGift");	
		_interestPersonalProduct= _ontModel.createOntProperty(Constants.intentNS + "InterestPersonalProduct");	
		_interestPubContent= _ontModel.createOntProperty(Constants.intentNS + "InterestPublishedContent");
		
		_interestBoxSuite= _ontModel.createOntProperty(Constants.intentNS + "InterestBoxSuite");	
		_interestCatering= _ontModel.createOntProperty(Constants.intentNS + "InterestCatering");	
		_interestSchedule= _ontModel.createOntProperty(Constants.intentNS + "InterestGameSchedule");	
		_interestTicketing= _ontModel.createOntProperty(Constants.intentNS + "InterestTicketing");	
		
		_interest.addSubProperty(_interestChildProduct);
		_interest.addSubProperty(_interestAdultProduct);
		_interest.addSubProperty(_interestVideoContent);
		_interest.addSubProperty(_interestAudioContent);
		_interest.addSubProperty(_interestProductGift);	
		_interest.addSubProperty(_interestPersonalProduct);		
		_interest.addSubProperty(_interestPubContent);		
		_interest.addSubProperty(_interestBoxSuite);	
		_interest.addSubProperty(_interestCatering);
		_interest.addSubProperty(_interestSchedule);	
		_interest.addSubProperty(_interestTicketing);				
						
		
		//----------------declaring activities---------
		_browseActivity= _ontModel.createClass(Constants.intentNS + "BrowseActivity");	
		_viewActivity= _ontModel.createClass(Constants.intentNS + "ViewActivity");	
		_publishActivity= _ontModel.createClass(Constants.intentNS + "PublishActivity");
		_listenActivity= _ontModel.createClass(Constants.intentNS + "ListenActivity");
		
		_activity.addSubClass(_browseActivity);
		_activity.addSubClass(_viewActivity);
		_activity.addSubClass(_listenActivity);
		_activity.addSubClass(_publishActivity);		
		
		//----------declaring interests------------------
	
		
		//_interest.isSameAs(_product);
		
		//--------declaring intents-----------------
				
		_intentPersonalPurchase= _ontModel.createClass(Constants.intentNS + "IntentPersonalPurchase");	
		_intentGiftPurchase= _ontModel.createClass(Constants.intentNS + "IntentGiftPurchase");
		_intentAdultPurchase= _ontModel.createClass(Constants.intentNS + "IntentAdultPurchase");
		_intentChildPurchase= _ontModel.createClass(Constants.intentNS + "IntentChildPurchase");
		_intentChildBirthdayPurchase= _ontModel.createClass(Constants.intentNS + "IntentChildBirthdayPurchase");
		_intentAdultBirthdayPurchase= _ontModel.createClass(Constants.intentNS + "IntentAdultBirthdayPurchase");
		_intentCorporateEventPurchase= _ontModel.createClass(Constants.intentNS + "IntentCorporateEventPurchase");
		_intentVideoContentTopicPurchase= _ontModel.createClass(Constants.intentNS + "IntentVideoContent");
		_intentAudioContentTopicPurchase= _ontModel.createClass(Constants.intentNS + "IntentAudioContent");
		_intentPubContentTopicPurchase= _ontModel.createClass(Constants.intentNS + "IntentPublishedContent");
		
		_intentBoxSuitePurchase= _ontModel.createClass(Constants.intentNS + "IntentBoxSuitePurchase");
		_intentCateringPurchase= _ontModel.createClass(Constants.intentNS + "IntentCateringPurchase");
		_intentGameAttend= _ontModel.createClass(Constants.intentNS + "IntentGameAttend");
		_intentTicketPurchase= _ontModel.createClass(Constants.intentNS + "IntentTicketPurchase");
		
		
		_hasIntent= _ontModel.createOntProperty(Constants.intentNS + "HasIntent");	
		_baseProperty.addSubProperty(_hasIntent);
		
		_intentGiftPurchase.addSubClass(_intentChildBirthdayPurchase);		
		_intentGiftPurchase.addSubClass(_intentAdultBirthdayPurchase);		
		_intent.addSubClass(_intentPersonalPurchase);
		_intent.addSubClass(_intentGiftPurchase);
		_intent.addSubClass(_intentAdultPurchase);
		_intent.addSubClass(_intentChildPurchase);
		_intent.addSubClass(_intentVideoContentTopicPurchase);
		_intent.addSubClass(_intentAudioContentTopicPurchase);
		_intent.addSubClass(_intentBoxSuitePurchase);
		_intent.addSubClass(_intentCateringPurchase);
		_intent.addSubClass(_intentGameAttend);
		_intent.addSubClass(_intentTicketPurchase);
		
		//--------creating associatons-------
		
		_activity.addProperty(_interest, _intent);
		_browseActivity.addProperty(_interestChildProduct,_intentChildPurchase);
		_browseActivity.addProperty(_interestAdultProduct,_intentAdultPurchase);
		_browseActivity.addProperty(_interestProductGift,_intentGiftPurchase);
		_viewActivity.addProperty(_interestVideoContent,_intentVideoContentTopicPurchase);
		_listenActivity.addProperty(_interestAudioContent,_intentAudioContentTopicPurchase);
		_publishActivity.addProperty(_interestPubContent,_intentPubContentTopicPurchase);
		
		_browseActivity.addProperty(_interestBoxSuite,_intentBoxSuitePurchase);
		_browseActivity.addProperty(_interestCatering,_intentCateringPurchase);
		_browseActivity.addProperty(_interestTicketing,_intentTicketPurchase);
		_browseActivity.addProperty(_interestSchedule,_intentGameAttend);
		
			
		return null;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		
		/*
		Individual _browse = _browseActivity.createIndividual(_baseNS+"Browse");
		Individual _view = _viewActivity.createIndividual(_baseNS+"View");
		Individual _listen = _listenActivity.createIndividual(_baseNS+"Listen");
		Individual _publish = _publishActivity.createIndividual(_baseNS+"Publish");
		*/
		
		
		/*
		Individual _purchaseChildItemIntent = _intent.createIndividual(_baseNS+"PurchaseChildItemIntent");
		Individual _purchaseAdultItemIntent = _intent.createIndividual(_baseNS+"PurchaseAdultItemIntent");
		Individual _purchaseGiftCertIntent = _intent.createIndividual(_baseNS+"PurchaseGiftCertIntent");
		Individual _purchaseVideoTopicIntent= _intent.createIndividual(_baseNS+"PurchaseVideoTopicIntent");
		Individual _purchaseBdayGiftintent= _compositeIntent.createIndividual(_baseNS+"PurchaseBdayGiftIntent");	
		*/
		
		
		HasValueRestriction _childProductInterest = _ontModel.createHasValueRestriction(_baseNS+"ChildSizeRestriction", _interestProductSize, _ontModel.createTypedLiteral("S"));
		HasValueRestriction _adultProductInterest = _ontModel.createHasValueRestriction(_baseNS+"ChildSizeRestriction", _interestProductSize, _ontModel.createTypedLiteral("L"));
		HasValueRestriction _giftInterest = _ontModel.createHasValueRestriction(_baseNS+"GiftRestriction", _interestProductType, _ontModel.createTypedLiteral("GiftCertificate"));
		HasValueRestriction _videoInterest = _ontModel.createHasValueRestriction(_baseNS+"VideoRestriction", _interestProductType, _ontModel.createTypedLiteral("Video"));
		
			
		IntersectionClass _purchaseChildItemX = _ontModel.createIntersectionClass(_baseNS+"PurchaseChldItemIntent", _ontModel.createList( new RDFNode[] {_browseActivity, _childProductInterest}));
		IntersectionClass _purchaseAdultItemX = _ontModel.createIntersectionClass(_baseNS+"PurchaseAdultItemIntent", _ontModel.createList( new RDFNode[] {_browseActivity, _adultProductInterest}));
		IntersectionClass _purchaseGiftCertX = _ontModel.createIntersectionClass(_baseNS+"PurchaseGiftItemIntent", _ontModel.createList( new RDFNode[] {_browseActivity, _giftInterest}));
		IntersectionClass _purchaseVideoTopicX = _ontModel.createIntersectionClass(_baseNS+"PurchaseVideoTopicIntent", _ontModel.createList( new RDFNode[] {_viewActivity, _videoInterest}));
		//IntersectionClass _childBdayGiftX = _ontModel.createIntersectionClass(_baseNS+"PurchaseForBdayIntent", _ontModel.createList( new RDFNode[] {_browse, _childProductInterest,_giftInterest}));
		IntersectionClass _childBdayGiftX = _ontModel.createIntersectionClass(_baseNS+"PurchaseForChildBdayIntent", _ontModel.createList( new RDFNode[] {_intentChildPurchase,_intentGiftPurchase}));
		IntersectionClass _adultBdayGiftX = _ontModel.createIntersectionClass(_baseNS+"PurchaseForAdultBdayIntent", _ontModel.createList( new RDFNode[] {_intentAdultPurchase,_intentGiftPurchase}));
		IntersectionClass _corporateEventX = _ontModel.createIntersectionClass(_baseNS+"PurchaseForCorporateEvent", _ontModel.createList( new RDFNode[] {_intentBoxSuitePurchase,_intentCateringPurchase,_intentTicketPurchase,_intentAdultPurchase}));
		
		_compositeIntent.addSubClass(_childBdayGiftX);	
		_compositeIntent.addSubClass(_adultBdayGiftX);	
		_compositeIntent.addSubClass(_corporateEventX);	
		
		IntersectionClass _baseIntersection = _ontModel.createIntersectionClass(_baseNS+"BaseIntersection", _ontModel.createList( new RDFNode[] {}));	
	
		_baseIntersection.addSubClass(_purchaseChildItemX);
		_baseIntersection.addSubClass(_purchaseAdultItemX);
		_baseIntersection.addSubClass(_purchaseGiftCertX);
		_baseIntersection.addSubClass(_purchaseVideoTopicX);
		_baseIntersection.addSubClass(_childBdayGiftX);
		_baseIntersection.addSubClass(_adultBdayGiftX);
		_baseIntersection.addSubClass(_corporateEventX);
		
				
			
		_purchaseChildItemX.addProperty(_hasIntent, _intentChildPurchase);
		_purchaseAdultItemX.addProperty(_hasIntent, _intentAdultPurchase);
		_purchaseGiftCertX.addProperty(_hasIntent, _intentGiftPurchase);
		_purchaseVideoTopicX.addProperty(_hasIntent, _intentVideoContentTopicPurchase);		
		
		
		_childBdayGiftX.addProperty(_hasIntent, _intentChildBirthdayPurchase);		
		_corporateEventX.addProperty(_hasIntent, _intentCorporateEventPurchase);
		_adultBdayGiftX.addProperty(_hasIntent, _intentAdultBirthdayPurchase);	
		
		
						
			
		
		return _ontModel;
	}

	@Override
	public void registerOntology(OntModel pModel) {
		// TODO Auto-generated method stub
		OntologyUtil.getInstance().addOntology(_ontModel, _baseURI);
		
	}

	

}
