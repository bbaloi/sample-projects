package com.extemp.cem.semantic.generator;

import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.InformationClusterDetector;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.XSD;

public class ProductModelBuilder extends BaseOntModel implements IOntologyBuilder 
{
	
	//private static String _productBaseURI="http://engage/ontology/product";
	//private static String _baseNS=_productBaseURI+"#";
	
	OntModel _wineOntModel;
	OntModel _baseModel;
	
	private DatatypeProperty _productName, _productBrand, _productType,_productSerialNumber, _productMadeIn, _productExpiryDate,_productPrice,_productDiscount,_itemSize,_itemGender;
	private DatatypeProperty _classCounter,_propertyCounter;
	
	
	public ProductModelBuilder()
	{
		super(Constants.productBaseURI,Constants.productNS);
	}

	
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
		_ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		//_ontModel = ModelFactory.createOntologyModel();
		_ontModel.setNsPrefix("product", Constants.productNS);
		_ontModel.setDynamicImports(true);
		 Ontology _productOnt = _ontModel.createOntology(Constants.productBaseURI);
		_productOnt.addComment("Consumable Product Ontology", "EN");
		_productOnt.addLabel("Product Otology", "EN");
		
		// add Wine ontology
		String _wineOnt = CEMUtil.getInstance().getCEMProperty("cem.wine.ontology.location");
		//_wineOntModel = OntologyUtil.getInstance().getOntologyModel(Constants.wineOntFile);		
		_wineOntModel = OntologyUtil.getInstance().getOntologyModel(_wineOnt);		
		String _wineURI = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine";
		//_productOnt.addImport(_ontModel.createResource(_wineURI));		
		//_ontModel.add(_wineOntModel);	
		//_ontModel.addSubModel(_wineOntModel);
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
		
		
		//_classCounter = _ontModel.getDatatypeProperty(Constants.baseOntNS+"ClassCounter");
		//_propertyCounter = _ontModel.getDatatypeProperty(Constants.baseOntNS+"PropertyCounter");
		
		OntClass _product = _ontModel.createClass(_baseNS + "Product");
		_productName = _ontModel.createDatatypeProperty(_baseNS + "ProductName");
		_productName.setDomain(_product);
		_productName.setRange(XSD.xstring);
		_productBrand = _ontModel.createDatatypeProperty(_baseNS + "ProductBrand");
		_productBrand.setDomain(_product);
		_productBrand.setRange(XSD.xstring);
		_productType = _ontModel.createDatatypeProperty(_baseNS + "ProductType");
		_productType.setDomain(_product);
		_productType.setRange(XSD.xstring);
		_productSerialNumber= _ontModel.createDatatypeProperty(_baseNS + "ProductSerialNumber");
		_productSerialNumber.setDomain(_product);
		_productSerialNumber.setRange(XSD.xstring);
		_productMadeIn= _ontModel.createDatatypeProperty(_baseNS + "ProductMadeIn");
		_productMadeIn.setDomain(_product);
		_productMadeIn.setRange(XSD.xstring);
		_productExpiryDate= _ontModel.createDatatypeProperty(_baseNS + "ProductExpiryDate");
		_productExpiryDate.setDomain(_product);
		_productExpiryDate.setRange(XSD.date);
		_productPrice= _ontModel.createDatatypeProperty(_baseNS + "ProductPrice");
		_productPrice.setDomain(_product);
		_productPrice.setRange(XSD.xdouble);		
		_productDiscount= _ontModel.createDatatypeProperty(_baseNS + "ProductDiscount");
		_productDiscount.setDomain(_product);
		_productDiscount.setRange(XSD.xint);
		
		_baseClass.addSubClass(_product);
		
		OntClass _food = _ontModel.createClass(_baseNS + "Food");
		OntClass _drink = _ontModel.createClass(_baseNS + "Drink");
		OntClass _merchandise = _ontModel.createClass(_baseNS + "Merchandise");
		OntClass _vehicle = _ontModel.createClass(_baseNS + "Vehicle");
		OntClass _boat = _ontModel.createClass(_baseNS + "Boat");
		OntClass _clothing = _ontModel.createClass(_baseNS + "Clothing");
		OntClass _appliance = _ontModel.createClass(_baseNS + "Appliance");
		
		_product.addSubClass(_merchandise);
		_product.addSubClass(_food);
		_product.addSubClass(_drink);
		_product.addSubClass(_vehicle);
		_product.addSubClass(_boat);
		_product.addSubClass(_appliance);
		_product.addSubClass(_clothing);
		
		//-----Food Model-------------------------
		OntClass _meat= _ontModel.createClass(_baseNS + "Meat");
		OntClass _dairy = _ontModel.createClass(_baseNS + "Dairy");
		OntClass _bread = _ontModel.createClass(_baseNS + "Bread");
		OntClass _vegetables = _ontModel.createClass(_baseNS + "Vegetables");
		OntClass _fruits = _ontModel.createClass(_baseNS + "Fruits");
		OntClass _confectionary= _ontModel.createClass(_baseNS + "Confectionary");
		OntClass _pasta = _ontModel.createClass(_baseNS + "Pasta");
		OntClass _fish = _ontModel.createClass(_baseNS + "Fish");
		
		OntClass _cheese= _ontModel.createClass(_baseNS + "Cheese");
		OntClass _milk= _ontModel.createClass(_baseNS + "Milk");
		
		_food.addSubClass(_meat);
		_food.addSubClass(_dairy);
		_dairy.addSubClass(_cheese);
		_dairy.addSubClass(_milk);
		_food.addSubClass(_bread);
		_food.addSubClass(_vegetables);
		_food.addSubClass(_fruits);
		_food.addSubClass(_confectionary);
		_food.addSubClass(_pasta);
		_food.addSubClass(_fish);
		
			
				
		//-----Drink Model-----------------------
		OntClass _alcoholic = _ontModel.createClass(_baseNS + "Alcoholic");
		OntClass _nonAlcoholic = _ontModel.createClass(_baseNS + "NonAlcoholic");
		_drink.addSubClass(_alcoholic);
		_drink.addSubClass(_nonAlcoholic);
		
		OntClass _beer = _ontModel.createClass(_baseNS + "Beer");
		
		//OntClass _wine = _wineOntModel.getOntClass("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#Wine");
		//if(_wine == null)
		//{
			System.out.println("Didn't find wine !");
			OntClass _wine = _ontModel.createClass(_baseNS + "Wine");
		//}
		/*else
		{
			System.out.println("Found Wine ontology !");
			
			StmtIterator stmtIt = _wine.listProperties();
			ExtendedIterator _declPropIt = _wine.listDeclaredProperties();
			ExtendedIterator _instIt = _wine.listInstances();
			
			System.out.println("Wine declared properties:");
			while(_declPropIt.hasNext())
			{
				OntProperty _stmt = (OntProperty)_declPropIt.next();
				//System.out.println(_stmt.toString());
			}
			
		}
		*/
		_product.addSubClass(_wine);
		
		
		OntClass _spirit = _ontModel.createClass(_baseNS + "Spirit");
		_alcoholic.addSubClass(_beer);
		_alcoholic.addSubClass(_wine);
		_alcoholic.addSubClass(_spirit);		
		
		OntClass _stout = _ontModel.createClass(_baseNS + "Stout");
		OntClass _pilsener = _ontModel.createClass(_baseNS + "Pilsener");
		OntClass _blonde = _ontModel.createClass(_baseNS + "Blonde");
		_beer.addSubClass(_stout);
		_beer.addSubClass(_pilsener);
		_beer.addSubClass(_blonde);
		
		OntClass _soda = _ontModel.createClass(_baseNS + "Soda");
		OntClass _juice = _ontModel.createClass(_baseNS + "Juice");
		_nonAlcoholic.addSubClass(_soda);
		_nonAlcoholic.addSubClass(_juice);
			
				
		//-----vehicle model------
		OntClass _car = _ontModel.createClass(_baseNS + "Car");
		OntClass _motorcycle = _ontModel.createClass(_baseNS + "Motorcyle");
		_vehicle.addSubClass(_car);
		_product.addSubClass(_motorcycle);
		
		OntClass _sedan= _ontModel.createClass(_baseNS + "Sedan");
		OntClass _sportsCar= _ontModel.createClass(_baseNS + "SportsCar");
		OntClass _truck= _ontModel.createClass(_baseNS + "Truck");
		_car.addSubClass(_sedan);
		_car.addSubClass(_truck);
		_car.addSubClass(_sportsCar);
	
						
		//---------------Clothing Model--------------------
		
		_itemSize = _ontModel.createDatatypeProperty(_baseNS + "ItemSize");
		_itemSize.setDomain(_clothing);
		_itemSize.setRange(XSD.xstring);
		_itemGender = _ontModel.createDatatypeProperty(_baseNS + "ItemGender");
		_itemGender.setDomain(_clothing);
		_itemGender.setRange(XSD.xstring);
		
		OntClass _casual = _ontModel.createClass(_baseNS + "CasualWear");
		OntClass _formal = _ontModel.createClass(_baseNS + "FormalWear");
		OntClass _beach = _ontModel.createClass(_baseNS + "BeachWear");
		OntClass _outdoors = _ontModel.createClass(_baseNS + "OutdoorsWear");
		OntClass _foot = _ontModel.createClass(_baseNS + "FootWear");
		OntClass _sports = _ontModel.createClass(_baseNS + "SportsWear");
		
		_clothing.addSubClass(_casual);
		_clothing.addSubClass(_formal);
		_clothing.addSubClass(_beach);
		_clothing.addSubClass(_outdoors);
		_clothing.addSubClass(_foot);
		_clothing.addSubClass(_sports);
		
		OntClass _denim = _ontModel.createClass(_baseNS + "Denim");
		OntClass _khaki = _ontModel.createClass(_baseNS + "Khakis");
		_casual.addSubClass(_denim);
		_casual.addSubClass(_khaki);
		
		OntClass _gloves = _ontModel.createClass(_baseNS + "HockeyGloves");
		OntClass _skates = _ontModel.createClass(_baseNS + "HockeySkates");
		OntClass _jerseys = _ontModel.createClass(_baseNS + "HockeyJersey");
		_sports.addSubClass(_gloves);
		_sports.addSubClass(_skates);
		_sports.addSubClass(_jerseys);
		
		
			
		//-----Appliance Model----------------------------
		OntClass _washer = _ontModel.createClass(_baseNS + "Washer");
		OntClass _drier = _ontModel.createClass(_baseNS + "Drier");
		OntClass _fridge = _ontModel.createClass(_baseNS + "Fridge");
		OntClass _stove = _ontModel.createClass(_baseNS + "Stove");
		
		_appliance.addSubClass(_washer);
		_appliance.addSubClass(_drier);
		_appliance.addSubClass(_stove);
		_appliance.addSubClass(_fridge);
		
	    //-----gift certificate-----
		OntClass _gf = _ontModel.createClass(_baseNS + "GiftCertificate");
		_gf.addSuperClass(_product);
		//----------equipment model-----
		OntClass _equipment = _ontModel.createClass(_baseNS + "Equipment");
		_product.addSubClass(_equipment);
		
		OntClass _sportsEquipment = _ontModel.createClass(_baseNS + "SportsEquipment");
		OntClass _constructionEquipment = _ontModel.createClass(_baseNS + "ConstructionEquipment");
		_equipment.addSubClass(_sportsEquipment);
		_equipment.addSubClass(_constructionEquipment);		

		return _ontModel;
	}

	@Override
	public OntModel buildBaseInstanceModel() throws Exception 
	{
		// TODO Auto-generated method stub
		generateFoodModel();
		generateDrinkModel();
		generateVehicleModel();
		generateClothingModel();
		generateApplianceModel();		
		
		return _ontModel;
	}

	private void generateFoodModel()
	{
long _dairyClsCntr,_meatCntr;
		
		//OntClass _dairyBaseClass=null,_cheeseBaseClass=null,_fishBaseClass=null,_sodaBaseClass=null,_meatBaseClass=null;
		OntClass _dairy=_ontModel.getOntClass(_baseNS+"Dairy");	
		OntClass _chese=_ontModel.getOntClass(_baseNS+"Cheese");	
		OntClass _meat=_ontModel.getOntClass(_baseNS+"Meat");		
		OntClass _fish=_ontModel.getOntClass(_baseNS+"Fish");
		OntClass _soda=_ontModel.getOntClass(_baseNS+"Soda");
		/*	
		_dairyBaseClass=OntologyUtil.getInstance().getBaseClass(_dairy);
		_cheeseBaseClass=OntologyUtil.getInstance().getBaseClass(_chese);		
		_meatBaseClass=OntologyUtil.getInstance().getBaseClass(_meat);
		_fishBaseClass=OntologyUtil.getInstance().getBaseClass(_fish);
		_sodaBaseClass=OntologyUtil.getInstance().getBaseClass(_soda);
		
		System.out.println("__Class Counter="+_classCounter.getURI());
		System.out.println("dairy base class:"+_dairyBaseClass.getLocalName());
		
		if(_dairy.getPropertyValue(_classCounter)==null)
			_dairyClsCntr=0;
		else 
			_dairyClsCntr = _dairyBaseClass.getPropertyValue(_classCounter).asLiteral().getLong();	
				
				
		long  _meat.getPropertyValue(_classCounter).asLiteral().getLong();
		long _fishCntr = _fish.getPropertyValue(_classCounter).asLiteral().getLong();
		long _sodaCntr = _soda.getPropertyValue(_classCounter).asLiteral().getLong();
		
		*/
		
		/*
		DatatypeProperty _productName= _ontModel.getOntProperty(_baseNS+"ProductName").asDatatypeProperty();
		DatatypeProperty _productBrand= _ontModel.getOntProperty(_baseNS+"ProductBrand").asDatatypeProperty();
		DatatypeProperty _productSerialNumber= _ontModel.getOntProperty(_baseNS+"ProductSerialNumber").asDatatypeProperty();
		DatatypeProperty _productMadeIn= _ontModel.getOntProperty(_baseNS+"ProductMadeIn").asDatatypeProperty();
		DatatypeProperty _productExpiryDate= _ontModel.getOntProperty(_baseNS+"ProductExpiryDate").asDatatypeProperty();
		*/
		
		Individual _milk= _dairy.createIndividual(_baseNS+"Milk");
		_milk.addProperty(_productName, "Milk");
		_milk.addProperty(_productBrand, "GayLea");
		_milk.addProperty(_productSerialNumber, "12345");
		_milk.addProperty(_productMadeIn, "US");				
		_milk.addLiteral(_productExpiryDate,_ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
		_milk.addLiteral(_productPrice, 2.99);				
		 InformationClusterDetector.getInstance().updateClassCounter(_dairy, _ontModel);
		
		Individual _cam = _chese.createIndividual(_baseNS+"Camembert");
		_cam.addProperty(_productName, "Camembert");
		_cam.addProperty(_productBrand, "President");
		_cam.addProperty(_productSerialNumber, "12345tg");
		_cam.addProperty(_productMadeIn, "France");
		_cam.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-07", XSDDatatype.XSDdate));
		_cam.addLiteral(_productPrice, 15.99);		
		 InformationClusterDetector.getInstance().updateClassCounter(_dairy, _ontModel);
		
	//	System.out.println("### "+_dairy.getPropertyValue(_baseModel.getProperty(Constants.baseOntNS+"ClassCounter")).asLiteral().getLong());
		
		Individual _chorizo= _meat.createIndividual(_baseNS+"Chorizo_Saussage");
		_chorizo.addProperty(_productName, "ChorizoSaussage");
		_chorizo.addProperty(_productBrand, "MapleLeaf Farms");
		_chorizo.addProperty(_productSerialNumber, "33-22-rt");
		_chorizo.addProperty(_productMadeIn, "Canada");
		_chorizo.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
		_chorizo.addLiteral(_productPrice, 10.99);
		 InformationClusterDetector.getInstance().updateClassCounter(_meat, _ontModel);
		
		Individual _its= _meat.createIndividual(_baseNS+"Italian_Saussage");
		_its.addProperty(_productName, "ChorizoSaussage");
		_its.addProperty(_productBrand, "MapleLeaf Farms");
		_its.addProperty(_productSerialNumber, "33-22-rt");
		_its.addProperty(_productMadeIn, "Canada");
		_its.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
		_its.addLiteral(_productPrice, 10.99);
		_its.addLiteral(_productDiscount,10);
		 InformationClusterDetector.getInstance().updateClassCounter(_meat, _ontModel);

		Individual _hd= _meat.createIndividual(_baseNS+"HotDog");
		_hd.addProperty(_productName, "HotDog");
		_hd.addProperty(_productBrand, "MapleLeaf Farms");
		_hd.addProperty(_productSerialNumber, "33-22-ht");
		_hd.addProperty(_productMadeIn, "Canada");
		_hd.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
		_hd.addLiteral(_productPrice, 4.99);
		_hd.addLiteral(_productDiscount,25);
		 InformationClusterDetector.getInstance().updateClassCounter(_meat, _ontModel);
		
				
		Individual _ssalmon= _fish.createIndividual(_baseNS+"Sockeye_Salmon");
		_ssalmon.addProperty(_productName, "Sockeye Salmon");
		_ssalmon.addProperty(_productBrand, "MapleLeaf Farms");
		_ssalmon.addProperty(_productSerialNumber, "33-22-xv");
		_ssalmon.addProperty(_productMadeIn, "Canada");
		_ssalmon.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));	
		 InformationClusterDetector.getInstance().updateClassCounter(_fish, _ontModel);
	   }
		
		private void generateDrinkModel()
		{
			OntClass _stout=_ontModel.getOntClass(_baseNS+"Stout");	
			OntClass _pilsener=_ontModel.getOntClass(_baseNS+"Pilsener");
			OntClass _blonde=_ontModel.getOntClass(_baseNS+"Blonde");
			OntClass _soda=_ontModel.getOntClass(_baseNS+"Soda");	
			OntClass _spirit=_ontModel.getOntClass(_baseNS+"Spirit");	
			OntClass _beer=_ontModel.getOntClass(_baseNS+"Beer");	
			/*
			
			long _beerCntr= _beer.getPropertyValue(_classCounter).asLiteral().getLong();
			long _spiritCntr= _spirit.getPropertyValue(_classCounter).asLiteral().getLong();
			long _sodaCntr= _spirit.getPropertyValue(_classCounter).asLiteral().getLong();
			*/
			
			///...?????need to import the wine model.......
			
			Individual _corona= _pilsener.createIndividual(_baseNS+"Corona");
			_corona.addProperty(_productName, "Corona");
			_corona.addProperty(_productBrand, "Corona");
			_corona.addProperty(_productSerialNumber, "mx447");
			_corona.addProperty(_productMadeIn, "MEX");
			_corona.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_beer, _ontModel);
			
			Individual _guiness= _stout.createIndividual(_baseNS+"Guiness");
			_guiness.addProperty(_productName, "Guiness");
			_guiness.addProperty(_productBrand, "Guiness");
			_guiness.addProperty(_productSerialNumber, "1234y4");
			_guiness.addProperty(_productMadeIn, "EU");
			_guiness.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_beer, _ontModel);
			
			Individual _uruquell= _pilsener.createIndividual(_baseNS+"Uruquell");
			_uruquell.addProperty(_productName, "Uruquell");
			_uruquell.addProperty(_productBrand, "PivoVar");
			_uruquell.addProperty(_productSerialNumber, "1234y4");
			_uruquell.addProperty(_productMadeIn, "CZ");
			_uruquell.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_beer, _ontModel);
			
			Individual _labatts= _pilsener.createIndividual(_baseNS+"Labatts");
			_labatts.addProperty(_productName, "Labatts");
			_labatts.addProperty(_productBrand, "Labatts Brewery");
			_labatts.addProperty(_productSerialNumber, "1234y4xx");
			_labatts.addProperty(_productMadeIn, "Canada");
			_labatts.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_beer, _ontModel);
			
			_labatts.addLiteral(_productDiscount,15);
			Individual _cheval= _blonde.createIndividual(_baseNS+"Cheval_Blanc");
			_cheval.addProperty(_productName, "ChevalBlanc");
			_cheval.addProperty(_productBrand, "Brewpub");
			_cheval.addProperty(_productSerialNumber, "1234y4xcb");
			_cheval.addProperty(_productMadeIn, "Canada");
			_cheval.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			_cheval.addLiteral(_productDiscount,15);
			 InformationClusterDetector.getInstance().updateClassCounter(_beer, _ontModel);
			
			Individual _vodka = _spirit.createIndividual(_baseNS+"Vodka_Finlandia");
			_vodka.addProperty(_productName, "Vodka Finlandia");
			_vodka.addProperty(_productBrand, "ABS");
			_vodka.addProperty(_productSerialNumber, "1234y-vf");
			_vodka.addProperty(_productMadeIn, "FIN");
			_uruquell.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_spirit, _ontModel);		
			
			Individual _pepsi= _soda.createIndividual(_baseNS+"Pepsi");
			_pepsi.addProperty(_productName, "Pepsi");
			_pepsi.addProperty(_productBrand, "Pepsi");
			_pepsi.addProperty(_productSerialNumber, "1234zc4");
			_pepsi.addProperty(_productMadeIn, "US");
			_pepsi.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_soda, _ontModel);
			
			Individual _coke= _soda.createIndividual(_baseNS+"Coke");
			_coke.addProperty(_productName, "Coke");
			_coke.addProperty(_productBrand, "CocaCola");
			_coke.addProperty(_productSerialNumber, "1234zrt");
			_coke.addProperty(_productMadeIn, "US");
			_pepsi.addProperty(_productExpiryDate, _ontModel.createTypedLiteral("2014-12-06", XSDDatatype.XSDdate));
			 InformationClusterDetector.getInstance().updateClassCounter(_soda, _ontModel);
	}
	private void generateVehicleModel()
	{
		
		OntClass _sportsCar=_ontModel.getOntClass(_baseNS+"SportsCar");	
		OntClass _truck=_ontModel.getOntClass(_baseNS+"Truck");	
		
		/*
		long _sportsCntr = _sportsCar.getPropertyValue(_classCounter).asLiteral().getLong();
		long _truckCntr = _sportsCar.getPropertyValue(_classCounter).asLiteral().getLong();
		*/
				
		Individual _ferarriTestaRosa = _sportsCar.createIndividual(_baseNS+"TestaRosa");
		_ferarriTestaRosa.addProperty(_productName, "TestaRosa");
		_ferarriTestaRosa.addProperty(_productBrand, "Ferrarri");
		_ferarriTestaRosa.addProperty(_productSerialNumber, "1234zc4vb444");
		_ferarriTestaRosa.addProperty(_productMadeIn, "IT");
		 InformationClusterDetector.getInstance().updateClassCounter(_sportsCar, _ontModel);
		
		Individual _jaguar= _sportsCar.createIndividual(_baseNS+"Jaguar_XS");
		_jaguar.addProperty(_productName, "Jaguar XS");
		_jaguar.addProperty(_productBrand, "Tata");
		_jaguar.addProperty(_productSerialNumber, "1234zc4vb333");
		_jaguar.addProperty(_productMadeIn, "UK");
		 InformationClusterDetector.getInstance().updateClassCounter(_sportsCar, _ontModel);
		
		Individual _ram = _truck.createIndividual(_baseNS+"DodgeRAM");
		_ram.addProperty(_productName, "Jaguar XS");
		_ram.addProperty(_productBrand, "Tata");
		_ram.addProperty(_productSerialNumber, "1234zc4vb333");
		_ram.addProperty(_productMadeIn, "US");
		 InformationClusterDetector.getInstance().updateClassCounter(_truck, _ontModel);
	}
	private void generateClothingModel()
	{
		OntClass _denim=_ontModel.getOntClass(_baseNS+"Denim");	
		OntClass _clothing=_ontModel.getOntClass(_baseNS+"Clothing");	
		OntClass _gloves=_ontModel.getOntClass(_baseNS+"HockeyGloves");	
		OntClass _skates=_ontModel.getOntClass(_baseNS+"HockeySkates");
		
		/*
		long _denimCntr = _denim.getPropertyValue(_classCounter).asLiteral().getLong();
		*/
		
		Individual _501 = _denim.createIndividual(_baseNS+"Levis501");
		_501.addProperty(_productName, "Levis 501");
		_501.addProperty(_productBrand, "Levi Strauss");
		_501.addProperty(_productSerialNumber, "lv501");
		_501.addProperty(_productMadeIn, "US");
		 InformationClusterDetector.getInstance().updateClassCounter(_clothing, _ontModel);
		 
		 Individual _hg = _gloves.createIndividual(_baseNS+"BauerHockeyGloves");
		 	_hg.addProperty(_productName, "Bauer Hockey Gloves");
			_hg.addProperty(_productType, "HockeyGloves");
			_hg.addProperty(_itemSize, "L");
			_hg.addProperty(_productBrand, "Bauer");
			_hg.addProperty(_productSerialNumber, "hg444");
			_hg.addProperty(_productMadeIn, "US");
			 InformationClusterDetector.getInstance().updateClassCounter(_clothing, _ontModel);
			 
		Individual _hs = _skates.createIndividual(_baseNS+"BauerHockeySkates");
			 	_hs.addProperty(_productName, "Bauer Hockey Skates");
				_hs.addProperty(_productType, "HockeySkates");
				_hs.addProperty(_itemSize, "L");
				_hs.addProperty(_productBrand, "Bauer");
				_hs.addProperty(_productSerialNumber, "hk444");
				_hs.addProperty(_productMadeIn, "US");
				 InformationClusterDetector.getInstance().updateClassCounter(_clothing, _ontModel);
		
	}
	private void generateApplianceModel()
	{
		OntClass _washer=_ontModel.getOntClass(_baseNS+"Washer");
		OntClass _drier=_ontModel.getOntClass(_baseNS+"Drier");
		OntClass _appliance=_ontModel.getOntClass(_baseNS+"Appliance");
		/*
		long _applianceCntr = _washer.getPropertyValue(_classCounter).asLiteral().getLong();
		*/
		
		Individual _geWasher = _washer.createIndividual(_baseNS+"GE-34-W");
		_geWasher.addProperty(_productName, "GE 34 W");
		_geWasher.addProperty(_productBrand, "GeneralElectric");
		_geWasher.addProperty(_productSerialNumber, "123xxx");
		_appliance.addProperty(_productMadeIn, "US");
		 InformationClusterDetector.getInstance().updateClassCounter(_appliance, _ontModel);
		
		Individual _geDrier = _drier.createIndividual(_baseNS+"GE-34-D");
		_geDrier.addProperty(_productName, "GE 34 D");
		_geDrier.addProperty(_productBrand, "GeneralElectric");
		_geDrier.addProperty(_productSerialNumber, "12yyy");
		_geDrier.addProperty(_productMadeIn, "US");
		 InformationClusterDetector.getInstance().updateClassCounter(_appliance, _ontModel);
	}

}
