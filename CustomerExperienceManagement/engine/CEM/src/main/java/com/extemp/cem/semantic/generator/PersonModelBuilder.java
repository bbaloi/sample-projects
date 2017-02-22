package com.extemp.cem.semantic.generator;


import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.InformationClusterDetector;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.EnumeratedClass;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.XSD;



public class PersonModelBuilder extends BaseOntModel implements IOntologyBuilder 
{
	private static String _personBaseURI="http://engage/ontology/person360";
	private static String _personNS=_personBaseURI+"#";
	
	private DatatypeProperty _age,_firstName,_lastName,_address,_gender,_dob, _profession,_workTitle,_speaksLanguage,_education,_name, _loyaltyTier,_id,_browsingProcessed,_degree;
	private DatatypeProperty _cardNumber,_cardType,_issuingBank,_loyaltyUserId,_loyaltyPoints,_sportType,_gameSentiment,_numGamesTotal,_numGamesSeason,_playerName,_teamName;
	private DatatypeProperty _teamName2,_sportType2,_sportType3,_musicianName,_musicStyle,_musicianTypeName,_bandName,_bandStyle,_socialMediaFriendName,_socialMediaFriendApp;
	private DatatypeProperty _fbId,_twitterId,_linkedInId;
	private ObjectProperty _relationHas,_works,_relationLikes,_livesIn,_relationFather,_relationMother,_relationBrother,_relationOwns,_relationLives,_relationParent,_relationSon,_relationDaughter;
	private OntClass _person,_workPlace,_browsingPerson,_paymentPreference,_payment,_loyaltyProgram,_sport,_hockey,_basketball,_music,_sportsTeam,_sportsPlayer,_musician,_band,_musicType,_socialMediaFriend;
	private OntModel _prodModel,_placeModel,_baseModel;
	private Individual _phil;
	
	
	public PersonModelBuilder()
	{
		super(Constants.personBaseURI,Constants.personNS);
	}
		
	protected void init()
	{
		_ontModel = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);
		_ontModel.setNsPrefix("person", Constants.personNS);
		_ontModel.setDynamicImports(true);
		 Ontology _personOnt = _ontModel.createOntology(Constants.personBaseURI);
		_personOnt.addComment("Person Ontology", "EN");
		_personOnt.addLabel("PersonOtology", "EN");
		// String _foafURI = "http://xmlns.com/foaf/0.1";
		//_personOnt.addImport(_ontModel.createResource(_foafURI));
		
		//importProdcutModel
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
		IOntologyBuilder _prodBuilder = ModelGeneratorFactory.getEmbeddedProductModel();
		try
		{
			_prodBuilder.buildBaseSchemaModel();
			_prodModel = _prodBuilder.buildBaseInstanceModel();
			_prodBuilder.registerOntology(_prodModel);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		_ontModel.addSubModel(_prodModel);		
		//_ontModel.add(_prodModel);
		//import Place Model
		IOntologyBuilder _placeBuilder = ModelGeneratorFactory.getEmbeddedPlaceModel();
		try
		{
			_placeBuilder.buildBaseSchemaModel();
			_placeModel = _placeBuilder.buildBaseInstanceModel();
			_placeBuilder.registerOntology(_placeModel);
			//OntologyUtil.getInstance().printOntology(_placeModel, Constants.personOntFile, true);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
		_ontModel.addSubModel(_placeModel);
		
	
	
		
	}
	public OntModel getProductModel()
	{
		return _prodModel;
	}
	public void registerOntology(OntModel pModel)
	{
		OntologyUtil.getInstance().addOntology(_ontModel, _baseURI);
	}

	public OntModel buildBaseSchemaModel() throws Exception {
		
			
		OntClass _baseClass = _ontModel.getOntClass(Constants.baseOntNS+"BaseClass");
		OntProperty _baseProperty = _ontModel.getOntProperty(Constants.baseOntNS+"BaseProperty");
		
		_person = _ontModel.createClass(_personNS + "Person");
		_id = _ontModel.createDatatypeProperty(_personNS + "ID");
		_id.setDomain(_person);
		_id.setRange(XSD.xstring); 
		_age = _ontModel.createDatatypeProperty(_personNS + "Age");
		_age.setDomain(_person);
		_age.setRange(XSD.integer); 
		_name = _ontModel.createDatatypeProperty(_personNS + "Name");
		_name.setDomain(_person);
		_name.setRange(XSD.xstring); 
		_firstName = _ontModel.createDatatypeProperty(_personNS + "FirstName");
		_firstName.setDomain(_person);
		_firstName.setRange(XSD.xstring); 
		_lastName = _ontModel.createDatatypeProperty(_personNS + "LastName");
		_lastName.setDomain(_person);
		_lastName.setRange(XSD.xstring); 
		_dob = _ontModel.createDatatypeProperty(_personNS + "DateOfBirth");
		_dob.setDomain(_person);
		_dob.setRange(XSD.xstring); 
		_gender = _ontModel.createDatatypeProperty(_personNS + "Gender");
		_gender.setDomain(_person);
		_gender.setRange(XSD.xstring); 
		_profession = _ontModel.createDatatypeProperty(_personNS + "Profession");
		_profession.setDomain(_person);
		_profession.setRange(XSD.xstring); 		
		_address = _ontModel.createDatatypeProperty(_personNS + "Address");
		_address.setDomain(_person);
		_address.setRange(XSD.xstring); 
		_fbId = _ontModel.createDatatypeProperty(_personNS + "FaceBookId");
		_fbId.setDomain(_person);
		_fbId.setRange(XSD.xstring); 
		_twitterId = _ontModel.createDatatypeProperty(_personNS + "TwitterId");
		_twitterId.setDomain(_person);
		_twitterId.setRange(XSD.xstring); 
		_linkedInId = _ontModel.createDatatypeProperty(_personNS + "LinkedInId");
		_linkedInId.setDomain(_person);
		_linkedInId.setRange(XSD.xstring); 
		
		_works = _ontModel.createObjectProperty(_personNS + "WorksAt");
		
		//-----------------payment related-----------------
		 _relationHas = _ontModel.createObjectProperty(_personNS + "Has");
		_paymentPreference = _ontModel.createClass(_personNS + "PaymentPreference");
		_cardNumber = _ontModel.createDatatypeProperty(_personNS + "CardNumber");
		_cardNumber.setDomain(_paymentPreference);
		_cardNumber.setRange(XSD.xstring); 
		_cardType = _ontModel.createDatatypeProperty(_personNS + "CardType");
		_cardType.setDomain(_paymentPreference);
		_cardNumber.setRange(XSD.xstring); 
		_issuingBank = _ontModel.createDatatypeProperty(_personNS + "IssuingBank");
		_issuingBank.setDomain(_paymentPreference);
		_issuingBank.setRange(XSD.xstring); 
		_baseClass.addSubClass(_paymentPreference);
						
		//---------------------sports-------------------------------------
		_sport = _ontModel.createClass(_personNS + "Sport");	
		_sportsPlayer = _ontModel.createClass(_personNS + "SportsPlayer");			
		_playerName = _ontModel.createDatatypeProperty(_personNS + "PlayerName");
		_playerName .setDomain(_sportsPlayer);
		_playerName.setRange(XSD.xstring);	
		_teamName = _ontModel.createDatatypeProperty(_personNS + "TeamName");
		_teamName .setDomain(_sportsPlayer);
		_teamName.setRange(XSD.xstring);	
		_sportType = _ontModel.createDatatypeProperty(_personNS + "SportType");
		_sportType .setDomain(_sportsPlayer);
		_sportType.setRange(XSD.xstring);	
		
		_sportsTeam = _ontModel.createClass(_personNS + "SportsTeam");		
		_teamName2 = _ontModel.getDatatypeProperty(_personNS + "TeamName");
		_teamName2 .setDomain(_sportsTeam);
		_teamName2.setRange(XSD.xstring);	
		_sportType2 = _ontModel.getDatatypeProperty(_personNS + "SportType");
		_sportType2 .setDomain(_sportsTeam);
		_sportType2.setRange(XSD.xstring);	
		
		_gameSentiment = _ontModel.createDatatypeProperty(_personNS + "GameSentiment");
		_gameSentiment .setDomain(_sport);
		_gameSentiment.setRange(XSD.xstring); 
		_numGamesTotal = _ontModel.createDatatypeProperty(_personNS + "NumGamesAttendedTotal");
		_numGamesTotal .setDomain(_sport);
		_numGamesTotal.setRange(XSD.integer); 
		_numGamesSeason = _ontModel.createDatatypeProperty(_personNS + "NumGamesAttendedSeason");
		_numGamesSeason .setDomain(_sport);
		_numGamesSeason.setRange(XSD.integer); 
		_sportType3 = _ontModel.getDatatypeProperty(_personNS + "SportType");
		_sportType3 .setDomain(_sport);
		_sportType3.setRange(XSD.xstring);	
				
		_hockey = _ontModel.createClass(_personNS + "Hockey");
		_basketball = _ontModel.createClass(_personNS + "Basketball");
		_sport.addSubClass(_hockey);
		_sport.addSubClass(_basketball);
		
		_baseClass.addSubClass(_sport);
		_baseClass.addSubClass(_sportsTeam);
		_baseClass.addSubClass(_sportsPlayer);
		
		//----------------music related--------------
		
		_musicType = _ontModel.createClass(_personNS + "MusicType");
				
		_musician = _ontModel.createClass(_personNS + "Musician");
		_musicianName = _ontModel.createDatatypeProperty(_personNS + "MusicianName");
		_musicianName .setDomain(_musician);
		_musicianName.setRange(XSD.xstring); 
		_musicStyle = _ontModel.createDatatypeProperty(_personNS + "MusicStyle");
		_musicStyle .setDomain(_musician);
		_musicStyle.setRange(XSD.xstring); 
		
		_band = _ontModel.createClass(_personNS + "Band");
		_bandName = _ontModel.createDatatypeProperty(_personNS + "BandName");
		_bandName.setDomain(_band);
		_bandName.setRange(XSD.xstring); 
		_bandStyle = _ontModel.createDatatypeProperty(_personNS + "BandStyle");
		_bandStyle .setDomain(_band);
		_bandStyle.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_musicType);
		_baseClass.addSubClass(_musician);
		_baseClass.addSubClass(_band);
				
		//-------loyalty related--------------------
		
		_loyaltyProgram = _ontModel.createClass(_personNS + "LoyaltyProgram");
		_loyaltyUserId = _ontModel.createDatatypeProperty(_personNS + "LoyaltyUserId");
		_loyaltyUserId .setDomain(_loyaltyProgram);
		_loyaltyUserId.setRange(XSD.xstring); 
		_loyaltyTier = _ontModel.createDatatypeProperty(_personNS + "LoyaltyTier");
		_loyaltyTier .setDomain(_loyaltyProgram);
		_loyaltyTier.setRange(XSD.xstring);
		_loyaltyPoints = _ontModel.createDatatypeProperty(_personNS + "TotalLoyaltyPoints");
		_loyaltyPoints .setDomain(_loyaltyProgram);
		_loyaltyPoints.setRange(XSD.integer);		
		
		_baseClass.addSubClass(_loyaltyProgram);
		
		//------social media----------------------------
		_socialMediaFriend = _ontModel.createClass(_personNS + "SocialMediaFriend");
		_socialMediaFriendName = _ontModel.createDatatypeProperty(_personNS + "SocialMediaFriendName");
		_socialMediaFriendName.setDomain(_socialMediaFriend);
		_socialMediaFriendName.setRange(XSD.xstring); 
		_socialMediaFriendApp = _ontModel.createDatatypeProperty(_personNS + "SocialMediaFriendApp");
		_socialMediaFriendApp.setDomain(_socialMediaFriend);
		_socialMediaFriendApp.setRange(XSD.xstring);
		
		_baseClass.addSubClass(_socialMediaFriend);
		
		_baseProperty.addSubProperty(_works);
		
		_works.setDomain(_person);
			
		_workTitle = _ontModel.createDatatypeProperty(_personNS + "workTitle");
		_workTitle.setDomain(_person);
		_workTitle.setRange(XSD.xstring); 	
		_speaksLanguage = _ontModel.createDatatypeProperty(_personNS + "speaksLanguage");
		_speaksLanguage.setDomain(_person);
		_speaksLanguage.setRange(XSD.xstring); 		
		_education = _ontModel.createDatatypeProperty(_personNS + "hasEducation");
		_education.setDomain(_person);
		_education.setRange(XSD.xstring); 
		
		_baseClass.addSubClass(_person);
				
		
		_browsingPerson = _ontModel.createClass(_personNS + "BrowsingPerson");
		_browsingProcessed = _ontModel.createDatatypeProperty(_personNS + "BrowsingProcessed");
		_workTitle.setDomain(_browsingPerson);
		_workTitle.setRange(XSD.xstring); 	
		_person.addSubClass(_browsingPerson);
		
		//------Family Relationships----------------
		
		OntProperty _relationProperty = _ontModel.createObjectProperty(_personNS + "FamilyRelationship");
		_degree = _ontModel.createDatatypeProperty(_personNS + "Degree");
		_degree.setDomain(_relationProperty);
		_id.setRange(XSD.xstring); 
		
		//_relationProperty.convertToTransitiveProperty();
		_relationParent = _ontModel.createObjectProperty(_personNS + "hasParent");
		_relationFather = _ontModel.createObjectProperty(_personNS + "hasFather");
		_relationMother = _ontModel.createObjectProperty(_personNS + "hasMother");
		OntProperty _relationChild = _ontModel.createObjectProperty(_personNS + "hasChild");
		_relationChild.addLiteral(_degree, "1");
		_relationSon = _ontModel.createObjectProperty(_personNS + "hasSon");
		_relationDaughter = _ontModel.createObjectProperty(_personNS + "hasDaughter");
		_relationChild.addSubProperty(_relationSon);
		_relationChild.addSubProperty(_relationDaughter);		
		OntProperty _relationSibling = _ontModel.createSymmetricProperty(_personNS + "hasSibling");		
		_relationBrother = _ontModel.createObjectProperty(_personNS + "hasBrother");
		OntProperty _relationSister = _ontModel.createObjectProperty(_personNS + "hasSister");
		_relationSister.addLiteral(_degree, "2");
		OntProperty _relationRelative = _ontModel.createObjectProperty(_personNS + "hasRelative");
		OntProperty _relationCousin = _ontModel.createObjectProperty(_personNS + "hasCousin");
		OntProperty _relationUncle = _ontModel.createObjectProperty(_personNS + "hasUncle");
		OntProperty _relationAunt = _ontModel.createObjectProperty(_personNS + "hasAunt");
		OntProperty _relationAquaintance = _ontModel.createObjectProperty(_personNS + "hasAquaintance");
		OntProperty _relationFriend = _ontModel.createObjectProperty(_personNS + "hasFriend");
		_relationProperty.addSubProperty(_relationParent );
		_relationParent.addSubProperty(_relationFather );
		_relationParent.addSubProperty(_relationMother );		
		_relationProperty.addSubProperty(_relationChild );
		_relationProperty.addSubProperty(_relationSibling );
		_relationSibling.addSubProperty(_relationBrother );
		_relationSibling.addSubProperty(_relationSister );
		_relationProperty.addSubProperty(_relationRelative );
		_relationRelative.addSubProperty(_relationCousin);
		_relationRelative.addSubProperty(_relationAunt);
		_relationRelative.addSubProperty(_relationUncle);
		_relationProperty.addSubProperty(_relationAquaintance);
		_relationAquaintance.addSubProperty(_relationFriend);
		
		_person.addProperty(_relationProperty, _person);
		
		_baseProperty.addSubProperty(_relationProperty);
				
		//---------------------------Work Relationships---------------------------------------------------------
		_workPlace = _ontModel.createClass(_personNS + "WorkPlace");
		DatatypeProperty _companyName = _ontModel.createDatatypeProperty(_personNS + "CompanyName");
		_companyName .setDomain(_workPlace);
		_companyName .setRange(XSD.xstring); 
		DatatypeProperty _companyAddress = _ontModel.createDatatypeProperty(_personNS + "CompanyAddress");
		_companyAddress.setDomain(_workPlace);
		_companyAddress.setRange(XSD.xstring); 
		
		OntProperty _relationWork= _ontModel.createObjectProperty(_personNS + "WorkRelationship");
		OntProperty _relationColeague = _ontModel.createObjectProperty(_personNS + "Coleague");
		OntProperty _relationManager = _ontModel.createObjectProperty(_personNS + "Manager");
		OntProperty _relationPeer = _ontModel.createObjectProperty(_personNS + "Peer");
		OntProperty _relationReportsTo = _ontModel.createObjectProperty(_personNS + "ReportsTo");
		_relationWork.addSubProperty(_relationColeague);
		_relationWork.addSubProperty(_relationPeer);
		_relationColeague.addSameAs(_relationPeer);
		_relationWork.addSubProperty(_relationManager);
		_relationWork.addSubProperty(_relationReportsTo);
		
		_person.addProperty(_works, _workPlace);	
		
		_baseClass.addSubClass(_workPlace);
		_baseProperty.addSubProperty(_relationWork);
		
		//--------------Preferences------------------
		_relationLikes= _ontModel.createObjectProperty(_personNS + "Likes"); //likes product, like entertainment, likes person
		
		OntClass _product = _prodModel.getOntClass(Constants.productNS+"Product");
		
		_person.addProperty(_relationLikes,_person);		
		_person.addProperty(_relationLikes, _product);
		_person.addProperty(_relationLikes, _sport);
		_person.addProperty(_relationLikes, _sportsPlayer);
		_person.addProperty(_relationLikes, _sportsTeam);
		_person.addProperty(_relationLikes, _musician);
		_person.addProperty(_relationLikes, _band);
		_person.addProperty(_relationLikes, _musicType);
		_person.addProperty(_relationLikes, _socialMediaFriend);
		_person.addProperty(_relationHas, _paymentPreference);
		_person.addProperty(_relationHas, _loyaltyProgram);
		
		
		_baseProperty.addSubProperty(_relationLikes);
		_baseProperty.addSubProperty(_relationHas);
		
				//--------------Assets------------------
		_relationOwns= _ontModel.createObjectProperty(_personNS + "Owns"); //car, house, boat, bike
		_person.addProperty(_relationOwns, _product);
		
		_baseProperty.addSubProperty(_relationOwns);
		
		//-----------Places------------------
		_relationLives= _ontModel.createObjectProperty(_personNS + "LivesIn");		//city,province,country,continennt
		
		OntClass _planet = _placeModel.getOntClass(Constants.placeNS+Constants.planetType);
		if(_planet==null)
				System.out.println(" Can't get Planet");
		OntClass _continent = _placeModel.getOntClass(Constants.placeNS+Constants.continentType);
		OntClass _country = _placeModel.getOntClass(Constants.placeNS+Constants.countryType);
		OntClass _state = _placeModel.getOntClass(Constants.placeNS+Constants.stateType);
		OntClass _city = _placeModel.getOntClass(Constants.placeNS+Constants.cityType);
		OntClass _address = _placeModel.getOntClass(Constants.placeNS+Constants.addressType);
		OntClass _region = _placeModel.getOntClass(Constants.placeNS+Constants.regionType);
		
		
		_person.addProperty(_relationLives, _planet);
		_person.addProperty(_relationLives, _continent);
		_person.addProperty(_relationLives, _country);
		_person.addProperty(_relationLives, _state);
		_person.addProperty(_relationLives, _city);
		_person.addProperty(_relationLives, _address);
		
		_baseProperty.addSubProperty(_relationLives);
				
		//--------------Medical------------------
		OntProperty _relationAlergy= _ontModel.createObjectProperty(_personNS + "hasAlergy");
		OntProperty _relationIllness= _ontModel.createObjectProperty(_personNS + "hasIllness");
		OntProperty _hasdoctor= _ontModel.createObjectProperty(_personNS + "hasDoctor");
		

		_baseProperty.addSubProperty(_relationAlergy);
		_baseProperty.addSubProperty(_relationIllness);
		_baseProperty.addSubProperty(_hasdoctor);
		
		//===========================================
						
				
		return _ontModel ;
	}
	
	public OntModel buildBaseInstanceModel() throws Exception {
		// TODO Auto-generated method stub
		Individual _matts = _person.createIndividual(_baseNS+"Matts_Sundin");		
		InformationClusterDetector.getInstance().updateClassCounter(_matts.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _wayne = _person.createIndividual(_baseNS+"Wayne_Gretsky");	
		InformationClusterDetector.getInstance().updateClassCounter(_wayne.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _mario = _person.createIndividual(_baseNS+"Mario_Lemieux");	
		InformationClusterDetector.getInstance().updateClassCounter(_mario.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _bill = _person.createIndividual(_baseNS+"Bill_Gretsky");
		InformationClusterDetector.getInstance().updateClassCounter(_bill.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _carl = _person.createIndividual(_baseNS+"Carl_Gretsky");
		InformationClusterDetector.getInstance().updateClassCounter(_carl.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _jean = _person.createIndividual(_baseNS+"Jean_Lemieux");
		InformationClusterDetector.getInstance().updateClassCounter(_jean.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _marie = _person.createIndividual(_baseNS+"Marie_Lemieux");
		InformationClusterDetector.getInstance().updateClassCounter(_marie.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		Individual _artemisia = _person.createIndividual(_baseNS+"Artemisia_Sundin");
		InformationClusterDetector.getInstance().updateClassCounter(_artemisia.getOntClass(), _ontModel);
		InformationClusterDetector.getInstance().updateClassCounter(_person, _ontModel);
		
		_phil = _sportsPlayer.createIndividual(Constants.personNS+"Phil_Kessel");
		_phil.addProperty(_playerName, "Phil Kessel");
		_phil.addProperty(_teamName, "Maple Leafs");
		_phil.addProperty(_sportType, "Hockey");
		
		buildJean(_jean,_mario);
		buildMarie(_marie,_mario);
		buildCarl(_carl,_wayne);
		buildArtemisa(_artemisia,_matts);
		buildBill(_bill,_wayne);

		buildWayne(_wayne,_matts,_carl,_bill);
		buildMatts(_matts,_wayne,_artemisia);
		buildMario(_mario,_jean,_marie);
			
	
		 		
				return _ontModel;
		
	}
	private void buildJean(Individual _jean,Individual _mario)
	{
		_jean.addProperty(_name, "Jean_Lemieux");
		_jean.addLiteral(_age, 20);
		_jean.addProperty(_gender, "M");
		_jean.addProperty(_profession, "Student");
		_jean.addProperty(_id, "111");
		_jean.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"University_McGill"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_jean.addProperty(_speaksLanguage, "English");
		_jean.addProperty(_speaksLanguage, "French");
		
		 Individual _mtl=_placeModel.getIndividual(Constants.placeNS+"Montreal");
		 //_ontModel.createStatement(_jean,_relationLives,_mtl);
		 _jean.addProperty(_relationLives, _mtl);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		 _jean.addProperty(_relationFather, _mario);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationFather, _ontModel);
		 
		
	}
	private void buildMarie(Individual _marie,Individual _mario)
	{
		_marie.addProperty(_name, "Marie_Lemieux");
		_marie.addProperty(_id, "222");
		_marie.addLiteral(_age, 15);
		_marie.addLiteral(_gender, "F");
		_marie.addProperty(_profession, "Student");
		_marie.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"Davis_College"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_marie.addProperty(_speaksLanguage, "English");
		_marie.addProperty(_speaksLanguage, "French");
		 Individual _mtl=_placeModel.getIndividual(Constants.placeNS+"Montreal");
		//_ontModel.createStatement(_marie,_relationLives,_mtl);
		 _marie.addProperty(_relationLives, _mtl);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		_marie.addProperty(_relationFather, _mario);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationFather, _ontModel);
	}
	private void buildCarl(Individual _carl,Individual _wayne)
	{
		_carl.addProperty(_name, "Carl_Gretsky");
		_carl.addLiteral(_age, 18);
		_carl.addProperty(_id, "333");
		_carl.addProperty(_gender, "M");
		_carl.addProperty(_profession, "Student");
		_carl.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"University_Of_Calgary"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_carl.addProperty(_speaksLanguage, "English");		
		_carl.addProperty(_relationFather, _wayne);
		
		 Individual _calgary=_placeModel.getIndividual(Constants.placeNS+"Calgary");
		// _ontModel.createStatement(_carl,_relationLives,_calgary);
		 _carl.addProperty(_relationLives, _calgary);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
	}
	private void buildArtemisa(Individual _artemisia,Individual _matts)
	{
		_artemisia.addProperty(_name, "Artemisia_Sundin");
		_artemisia.addLiteral(_age, 19);
		_artemisia.addProperty(_id, "444");
		_artemisia.addProperty(_gender, "F");
		_artemisia.addProperty(_profession, "Student");
		_artemisia.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"University_Of_Toroto"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_artemisia.addProperty(_speaksLanguage, "English");
		_artemisia.addProperty(_speaksLanguage, "Finnish");
		 Individual _to=_placeModel.getIndividual(Constants.placeNS+"Toronto");
		//_ontModel.createStatement(_artemisia,_relationLives,_to);
		 _artemisia.addProperty(_relationLives, _to);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		 _artemisia.addProperty(_relationFather, _matts);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationFather, _ontModel);
	}
	private void buildBill(Individual _bill,Individual _wayne)
	{
		_bill.addProperty(_name, "Bill_Gretsky");
		_bill.addLiteral(_age, 45);
		_bill.addProperty(_id, "555");
		_bill.addProperty(_gender, "M");
		_bill.addProperty(_profession, "Electrical Engineer");
		_bill.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"AT&T"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_bill.addProperty(_speaksLanguage, "English");
		
		Individual _la=_placeModel.getIndividual(Constants.placeNS+"Los_Angeles");
		_ontModel.createStatement(_bill,_relationLives,_la);
		 _bill.addProperty(_relationLives, _la);
			InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		_bill.addProperty(_relationBrother, _wayne);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationBrother, _ontModel);
	}
	private Individual buildWayne(Individual _wayne,Individual _matts, Individual _carl,Individual _bill)
	{

		_wayne.addProperty(_name, "Wayne_Gretsky");
		_wayne.addProperty(_firstName, "Wayne");
		_wayne.addProperty(_lastName, "Gretsky");
		_wayne.addLiteral(_age, 53);
		_wayne.addProperty(_gender, "M");
		_wayne.addProperty(_id, "777");
		_wayne.addProperty(_dob, "1961-01-26");		
		_wayne.addProperty(_profession, "Hockey Player");
		_wayne.addProperty(_address, "123 Stevens Ave., Calgary,AL, CA");
		_wayne.addProperty(_fbId, "Wayne_Gretsky");
		_wayne.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"Calgary_Flames"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_wayne.addProperty(_speaksLanguage, "English");
		
		Individual _payPref = _paymentPreference.createIndividual(Constants.personNS+"PaymentPreference_1");
		_payPref.addProperty(_cardNumber, "123456789");
		_payPref.addProperty(_cardType, "VISA");
		_payPref.addProperty(_issuingBank, "CIBC");		
		_wayne.addProperty(_relationHas, _payPref);
		
		Individual _loyalty = _loyaltyProgram.createIndividual(Constants.personNS+"LoyaltyProgram_1");
		_loyalty.addProperty(_loyaltyUserId, "777");
		_loyalty.addProperty(_loyaltyTier, "GOLD");
		_loyalty.addLiteral(_loyaltyPoints,100000);		
		_wayne.addProperty(_relationHas, _loyalty);		
		
		Individual _sport = _hockey.createIndividual(Constants.personNS+"PreferedSport_Hockey");
		_sport.addProperty(_sportType, "Hockey");
		_sport.addProperty(_gameSentiment, "Positive");
		_sport.addLiteral(_numGamesSeason, 10);
		_sport.addLiteral(_numGamesTotal, 1000);
		Individual _sport2 = _basketball.createIndividual(Constants.personNS+"PreferedSport_Basketball");
		_sport2.addProperty(_sportType, "Basketball");
		_sport2.addProperty(_gameSentiment, "Positive");
		_sport2.addLiteral(_numGamesSeason, 10);
		_sport2.addLiteral(_numGamesTotal, 1000);
		
		Individual _demar = _sportsPlayer.createIndividual(Constants.personNS+"DeMar_DeRosen");
		_demar.addProperty(_playerName, "DeMar DeRosen");
		_demar.addProperty(_teamName, "Raptors");
		_demar.addProperty(_sportType, "Basketball");
		
		Individual _team = _sportsTeam.createIndividual(Constants.personNS+"Raptors");
		_team.addProperty(_teamName, "Toronto Raptors");
		_team.addProperty(_sportType, "Basketball");
		
		
		Individual _mband = _band.createIndividual(Constants.personNS+"RollingStones");
		_mband.addProperty(_bandName, "Rolling Stones");
		_mband.addProperty(_bandStyle, "Rock & Roll");
		
		
		Individual _msocialFriend = _socialMediaFriend.createIndividual(Constants.personNS+"SocFriend_1");
		_msocialFriend.addProperty(_socialMediaFriendName, "Alice Of Wonderland");
		_msocialFriend.addProperty(_socialMediaFriendApp, "twitter");
		Individual _msocialFriend2 = _socialMediaFriend.createIndividual(Constants.personNS+"SocFriend_2");
		_msocialFriend2.addProperty(_socialMediaFriendName, "The Cheshire Cat");
		_msocialFriend2.addProperty(_socialMediaFriendApp, "Facebook");
		Individual _msocialFriend3 = _socialMediaFriend.createIndividual(Constants.personNS+"SocFriend_3");
		_msocialFriend3.addProperty(_socialMediaFriendName, "Matts_Sundin");
		_msocialFriend3.addProperty(_socialMediaFriendApp, "Facebook");
		Individual _msocialFriend4 = _socialMediaFriend.createIndividual(Constants.personNS+"SocFriend_4");
		_msocialFriend4.addProperty(_socialMediaFriendName, "Mario_Lemieux");
		_msocialFriend4.addProperty(_socialMediaFriendApp, "Facebook");
						
		_wayne.addProperty(_relationLikes, _sport);
		_wayne.addProperty(_relationLikes, _sport2);
		_wayne.addProperty(_relationLikes, _phil);
		_wayne.addProperty(_relationLikes, _demar);
		_wayne.addProperty(_relationLikes, _team);
		_wayne.addProperty(_relationLikes, _mband);
		_wayne.addProperty(_relationLikes, _msocialFriend);
		_wayne.addProperty(_relationLikes, _msocialFriend2);
		_wayne.addProperty(_relationLikes, _msocialFriend3);
		_wayne.addProperty(_relationLikes, _msocialFriend4);
		
		
		_wayne.addProperty(_relationLikes, _matts);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		_wayne.addProperty(_relationSon, _carl);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationSon, _ontModel);
		_wayne.addProperty(_relationBrother, _bill);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationBrother, _ontModel);
			
		//_wayne.addObjectProperty(_relationLikes, product);	
				
		Individual _gloves=_prodModel.getIndividual(Constants.productNS+"BauerHockeyGloves");
		Individual _skates=_prodModel.getIndividual(Constants.productNS+"BauerHockeySkates");
		 Individual _guiness=_prodModel.getIndividual(Constants.productNS+"Guiness");
		 Individual _corona=_prodModel.getIndividual(Constants.productNS+"Corona");
		 //OntologyUtil.getInstance().printProductproperties(_guiness, _prodModel);
		 Individual _hotdog=_prodModel.getIndividual(Constants.productNS+"HotDog");
		 Individual _chorizo=_prodModel.getIndividual(Constants.productNS+"Chorizo_Saussage");
		 Individual _car=_prodModel.getIndividual(Constants.productNS+"DodgeRAM");
		 Individual _calgary=_placeModel.getIndividual(Constants.placeNS+"Calgary");
		 
		 _wayne.addProperty(_relationLikes, _gloves);
		 _wayne.addProperty(_relationLikes, _skates);
		 _wayne.addProperty(_relationLikes, _guiness);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _wayne.addProperty(_relationLikes, _corona);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _wayne.addProperty(_relationLikes, _hotdog);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _wayne.addProperty(_relationLikes, _chorizo);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _wayne.addProperty(_relationOwns, _car);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		
		
		_wayne.addProperty(_relationLives, _calgary);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		
		
		return _wayne;
		 
	}
	private void buildMatts(Individual _matts,Individual _wayne,Individual _artemisia)
	{
				
		_matts.addProperty(_name, "Matts_Sundin");
		_matts.addProperty(_firstName, "Matts");
		_matts.addProperty(_lastName, "Sundin");
		_matts.addLiteral(_age, 50);
		_matts.addProperty(_gender, "M");
		_matts.addProperty(_id, "888");
		_matts.addProperty(_dob, "1971-02-13");		
		_matts.addProperty(_profession, "Hockey Player");
		_matts.addProperty(_address, "3981 Steels Ave., Toronto,ON, CA");
		_matts.addProperty(_fbId, "Matts_Sundin");
		_matts.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"Toronto_Maple_Leafs"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_matts.addProperty(_speaksLanguage, "English");
		_matts.addProperty(_speaksLanguage, "Swedish");
		
		_matts.addProperty(_relationLikes, _wayne);
		_matts.addProperty(_relationLikes, _phil);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		_matts.addProperty(_relationDaughter, _artemisia );
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationDaughter, _ontModel);
		
		Individual _msocialFriend_03 = _socialMediaFriend.createIndividual(Constants.personNS+"SocFriend_03");
		_msocialFriend_03.addProperty(_socialMediaFriendName, "Wayne_Gretsky");
		_msocialFriend_03.addProperty(_socialMediaFriendApp, "Facebook");
		
				
		_matts.addProperty(_relationLikes, _msocialFriend_03);
		
		 Individual _vodka=_prodModel.getIndividual(Constants.productNS+"Vodka_Finlandia");
		 //OntologyUtil.getInstance().printProductproperties(_guiness, _prodModel);
		 Individual _hotdog=_prodModel.getIndividual(Constants.productNS+"HotDog");
		 Individual _chorizo=_prodModel.getIndividual(Constants.productNS+"Chorizo_Saussage");
		 Individual _car=_prodModel.getIndividual(Constants.productNS+"TestaRosa");
		 Individual _toronto=_placeModel.getIndividual(Constants.placeNS+"Toronto");
		 
		 Individual _sport1 = _ontModel.getIndividual(Constants.personNS+"PreferedSport_Hockey");
		 _sport1.addProperty(_gameSentiment, "Neutral");
		 _sport1.addLiteral(_numGamesSeason, 8);
		 _sport1.addLiteral(_numGamesTotal, 500);
		 
		 _matts.addProperty(_relationLikes, _sport1);
		 
		 _matts.addProperty(_relationLikes, _vodka);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _matts.addProperty(_relationLikes, _hotdog);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _matts.addProperty(_relationLikes, _chorizo);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _matts.addProperty(_relationOwns, _car);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationOwns, _ontModel);
		_ontModel.createStatement(_matts, _relationLikes, _vodka); 
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		_ontModel.createStatement(_matts, _relationLikes, _hotdog);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		_ontModel.createStatement(_matts, _relationLikes, _chorizo);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		_ontModel.createStatement(_matts, _relationOwns, _car);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		
		//_ontModel.createStatement(_wayne,_relationLives,_toronto);
		_matts.addProperty(_relationLives, _toronto);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationLives, _ontModel);
		
	}
	private void buildMario(Individual _mario,Individual _jean,Individual _marie)
	{
		
		_mario.addProperty(_name, "Mario Lemieux");
		_mario.addProperty(_firstName, "Mario");
		_mario.addProperty(_lastName, "Lemieux");
		_mario.addLiteral(_age, 51);
		_mario.addProperty(_gender, "M");
		_mario.addProperty(_id, "999");
		_mario.addProperty(_dob, "1965-10-5");		
		_mario.addProperty(_profession, "Hockey Player");
		_mario.addProperty(_fbId, "Mario_Lemieux");
		_mario.addProperty(_works,_workPlace.createIndividual(Constants.placeNS+"Montreal_Canadiens"));
		InformationClusterDetector.getInstance().updatePropertyCounter(_works, _ontModel);
		_mario.addProperty(_speaksLanguage, "English");
		_mario.addProperty(_speaksLanguage, "French");
		
		
		_mario.addProperty(_relationSon, _jean);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationSon, _ontModel);
		_mario.addProperty(_relationDaughter, _marie);
		InformationClusterDetector.getInstance().updatePropertyCounter(_relationDaughter, _ontModel);
		//_ontModel.createStatement(_guy,_relationFather,_jean );
		//_ontModel.createStatement(_guy,_relationFather,_marie );
		
		//_wayne.addObjectProperty(_relationLikes, product);	
				
		 Individual _beer=_prodModel.getIndividual(Constants.productNS+"Cheval_Blanc");
		
		 //OntologyUtil.getInstance().printProductproperties(_guiness, _prodModel);
		Individual _cheese=_prodModel.getIndividual(Constants.productNS+"Camembert");
		Individual _fish=_prodModel.getIndividual(Constants.productNS+"Sockeye_Salmon");
		 Individual _car=_prodModel.getIndividual(Constants.productNS+"Jaguar_XS");
		 Individual _mtl=_placeModel.getIndividual(Constants.placeNS+"Montreal");
		 
		 
		 _mario.addProperty(_relationLikes, _beer);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _mario.addProperty(_relationLikes, _cheese);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _mario.addProperty(_relationLikes, _fish);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		 _mario.addProperty(_relationOwns, _car);
		 InformationClusterDetector.getInstance().updatePropertyCounter(_relationLikes, _ontModel);
		
		//_ontModel.createStatement(_guy, _relationLikes, _beer); 
		//_ontModel.createStatement(_guy, _relationLikes, _cheese);
		//_ontModel.createStatement(_guy, _relationLikes, _fish);
		//_ontModel.createStatement(_guy, _relationOwns, _car);

		//_ontModel.createStatement(_guy,_relationLives,_mtl);
		 _mario.addProperty(_relationLives, _mtl);
	
	}

	private void createEducation(OntModel pCustOntModel, OntClass pEducationClass)
	{
		//-----Education-------
				OntClass _baseEducation = pCustOntModel.createClass(_personNS + "BaseEducation");
				DatatypeProperty _educationType = pCustOntModel.createDatatypeProperty(_personNS + "EducationType");
				_educationType.setDomain(_baseEducation);
				_educationType.setRange(XSD.xstring); 
				DatatypeProperty _educationInst = pCustOntModel.createDatatypeProperty(_personNS + "EducationInstitution");
				_educationInst.setDomain(_baseEducation);
				_educationInst.setRange(XSD.xstring); 
				DatatypeProperty _educationFromYear = pCustOntModel.createDatatypeProperty(_personNS + "FromYear");
				_educationFromYear.setDomain(_baseEducation);
				_educationFromYear.setRange(XSD.integer); 
				DatatypeProperty _educationToYear = pCustOntModel.createDatatypeProperty(_personNS + "ToYear");
				_educationToYear.setDomain(_baseEducation);
				_educationToYear.setRange(XSD.integer); 
				
				OntClass _primaryEducation = pCustOntModel.createClass(_personNS + "PrimaryEducation");
				OntClass _secondaryEducation = pCustOntModel.createClass(_personNS + "SecondaryEducation");
				OntClass _underGradEducation =pCustOntModel.createClass(_personNS + "UnderGraduateEducation");
				OntClass _postGradEducation =pCustOntModel.createClass(_personNS + "PostGraduateEducation");
				
				_primaryEducation.addSuperClass(_baseEducation);
				_secondaryEducation.addSuperClass(_baseEducation);
				_underGradEducation.addSuperClass(_baseEducation);
				_postGradEducation.addSuperClass(_baseEducation);
				
				RDFList _list = pCustOntModel.createList();
				_list.add(_primaryEducation);
				_list.add(_secondaryEducation);
				_list.add(_underGradEducation);
				_list.add(_postGradEducation);
				EnumeratedClass _educationlist = pCustOntModel.createEnumeratedClass(_personNS + "Education", _list);
				
				
	}
	
	
}
