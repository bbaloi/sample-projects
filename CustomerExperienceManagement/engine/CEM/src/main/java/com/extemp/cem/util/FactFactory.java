package com.extemp.cem.util;

import java.io.PrintWriter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlElement;

import org.kie.api.KieBaseConfiguration;
import org.kie.api.definition.type.FactType;

import com.extemp.cem.profiles.Address;
import com.extemp.cem.profiles.AddressList;
import com.extemp.cem.profiles.Band;
import com.extemp.cem.profiles.CustomerProfileCBO;
import com.extemp.cem.profiles.Drink;
import com.extemp.cem.profiles.DrinkProfile;
import com.extemp.cem.profiles.FavouriteBandList;
import com.extemp.cem.profiles.FavouriteDrinkList;
import com.extemp.cem.profiles.FavouriteFoodList;
import com.extemp.cem.profiles.FavouriteMerchandiseList;
import com.extemp.cem.profiles.FavouritePlayerList;
import com.extemp.cem.profiles.FavouriteSportList;
import com.extemp.cem.profiles.FavouriteTeamList;
import com.extemp.cem.profiles.Food;
import com.extemp.cem.profiles.FoodProfile;
import com.extemp.cem.profiles.LanguageSpokenList;
import com.extemp.cem.profiles.LoyaltyProgram;
import com.extemp.cem.profiles.Merchandise;
import com.extemp.cem.profiles.MusicProfile;
import com.extemp.cem.profiles.PaymentPrefference;
import com.extemp.cem.profiles.PaymentPrefferenceList;
import com.extemp.cem.profiles.PreferencesList;
import com.extemp.cem.profiles.ShoppingProfile;
import com.extemp.cem.profiles.SocialMediaFriend;
import com.extemp.cem.profiles.SocialMediaFriendList;
import com.extemp.cem.profiles.SocialMediaProfile;
import com.extemp.cem.profiles.Sport;
import com.extemp.cem.profiles.SportPlayer;
import com.extemp.cem.profiles.SportTeam;
import com.extemp.cem.profiles.SportsProfile;
import com.extemp.cem.semantic.CustomerProfileUtil;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Derivation;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class FactFactory 
{
	
	private static Reasoner _reasoner=null;
	private static InfModel _infModel=null;
	private static OntModelSpec _ontModelSpec=null;
	private static OntModel _ontModel=null;
	/*
	private static String _sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
										 "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
										 "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
										 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
										 "PREFIX person: <http://engage/ontology/person360#>"+
										 "SELECT ?subject "+
										 "WHERE { ?subject rdf:type person:Person;"+
										 "person:ID ?userId."+
										 "FILTER(str(?userId)='777').}";
	*/
	private static String _sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			 "PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
			 "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
			 "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			 "PREFIX person: <http://engage/ontology/person360#>"+
			 "SELECT ?subject "+
			 "WHERE { ?subject rdf:type person:Person;"+
			 "person:ID ?userId.";
			 
	


	public static CustomerProfileCBO createCustomerProfile(String pUserId)
	{
		CustomerProfileCBO _custProfile=null; 	
		String _firstName=null,_lastName=null,_age=null,_middleName=null,_gender=null,_dob=null,_profession=null;
		List<Address> _addressList=null;
		PreferencesList _preferenceList=null;
		List<PaymentPrefference> _paymentPreferenceList=null;
		List<LoyaltyProgram> _loyaltyProgramList=null;
		List _customerOntStmts;
		OntModelSpec  _reasonerType;
		
		try
		{
			//1) invoke JENA to get customer profile and map it to the local object
			OntModel _model = CustomerProfileUtil.getInstance().loadOntModel(CEMUtil.getInstance().getCustomerProfileOntology());				
			_customerOntStmts = runReasoner(CEMUtil.getInstance().getReasonerType(),_model,pUserId);			
			
			//2 create the CustomerProfile CBO fact from Statment list
			_custProfile = buildCustomerProfile(_customerOntStmts,pUserId);
			
			
			/*_custProfile = new CustomerProfileCBO();
			_custProfile.setUserId(pUserId);
			_custProfile.setFirstName(_firstName);
			_custProfile.setLastName(_lastName);
			_custProfile.setMiddleName(_middleName);
			_custProfile.setAge(_age);
			_custProfile.setDob(_dob);
			_custProfile.setGender(_gender);
			_custProfile.setProfession(_profession);
			_custProfile.setAddressList(_addressList);
			_custProfile.setLanguagesSpokenList(_languagesList);
			_custProfile.setPreferencesList(_preferenceList);
			_custProfile.setPaymentPrefference(_paymentPreference);
			_custProfile.setLoyaltyProgram(_loyaltyProgram);
			*/
				
			
			KBUtil.getInstance().getKSession().insert(_custProfile);		
			//System.out.println("In createCustomerProfile.createProfile() - userId="+pUserId);
		
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		return _custProfile;
	}
	private static List runReasoner(String  pReasonerType,OntModel pModel,String pUserId)	
	{
		
		RDFNode _nullObject=null;
		OntProperty _nullPredicate=null;		
		List _retVal=null;
		StmtIterator _retStmtIt=null;
			
				
		if(pReasonerType.equals(CEMConstants.owlRdfsReasoner))
		{
			System.out.println("###***----Running with "+pReasonerType+" reasoner------");
			
			if(_reasoner==null)
			{
				_reasoner=ReasonerRegistry.getRDFSReasoner();
				_reasoner.bindSchema(pModel);	
			}
			if(_infModel==null)
			_infModel=ModelFactory.createInfModel(_reasoner, pModel);
			if( _ontModelSpec==null)
			{
				_ontModelSpec = OntModelSpec.RDFS_MEM_RDFS_INF;
				_ontModelSpec.setReasoner(_reasoner);
			}
		}
		if(pReasonerType.equals(CEMConstants.owlReasoner))
		{
			System.out.println("###+++----Running with "+pReasonerType+" reasoner------");
			if(_reasoner==null)
			{
				_reasoner=ReasonerRegistry.getOWLReasoner();	
				_reasoner.bindSchema(pModel);
			}
			if(_infModel==null)
			_infModel=ModelFactory.createInfModel(_reasoner, pModel);
			if( _ontModelSpec==null)
			{
				_ontModelSpec = OntModelSpec.OWL_MEM_RULE_INF;
				_ontModelSpec.setReasoner(_reasoner);
			}
		}
			
			 
		if(_ontModel==null)
		{
			
			
			if(pModel==null)
				System.out.println("OntModel raw is null !");
			if(_ontModelSpec==null)
				System.out.println("OntModel Spec is null ");
			_ontModel = ModelFactory.createOntologyModel(_ontModelSpec, pModel);		    
		    
		    
		    ValidityReport validity = _infModel.validate();
			if (validity.isValid()) {
			    System.out.println("Validation OK !");
			} else {
			    System.out.println("Conflicts");
			    for (Iterator i = validity.getReports(); i.hasNext(); ) {
			        System.out.println(" - " + i.next());
			    }
			}
			
		}
				
		//try SPARQL to get a handle to the Individual by ID 
		//======================do SPARQL====================		
		Individual _ind = executeSparqlQuery(pUserId);
		if(_ind !=null)
		{

			// list statements by Individual
				
			
			StmtIterator _stmtIt = _infModel.listStatements(_ind,_nullPredicate ,_nullObject);
			_retStmtIt = _infModel.listStatements(_ind,_nullPredicate ,_nullObject);
			System.out.println("----UserId:"+pUserId+"statements------");
						
			while( _stmtIt.hasNext() ) 
			{
			    Statement s = _stmtIt.nextStatement();
			    System.out.println("Statement is " + s);	   
			}
			
		}
		
		
		return _retStmtIt.toList();
	}
	
	private static Individual executeSparqlQuery(String pUserId)
	{
		boolean _found=false;
		Individual _retInd=null;
		
		//ParameterizedSparqlString pss = new ParameterizedSparqlString(_sparqlQuery);
		//pss.setLiteral(3, pUserId);
		//Query query = pss.asQuery();
		
		String _fullQuery = _sparqlQuery+"FILTER(str(?userId)='"+pUserId+"').}";
				
		System.out.println("======SPARQL query:"+_fullQuery.toString()+"===============");	
		
		
			
			Query query = QueryFactory.create(_fullQuery);
	        QueryExecution qexec = QueryExecutionFactory.create(query, _ontModel);
	      //  ResultSetRewindable results = ResultSetFactory.makeRewindable(qexec.execSelect());
	      
	        try {
		            ResultSet _results = qexec.execSelect();
		            int _size = _results.getRowNumber();
		           
		            while(_results.hasNext())
		            {
	            	
		            	if(!_found)
		            	{
		            	    QuerySolution soln = _results.nextSolution();	
		 	                String _res = soln.toString(); 
		 	                String _userID = (_res.substring(_res.indexOf("<")+1,_res.lastIndexOf('>'))).trim();            
		 	                Individual _ind = _ontModel.getIndividual(_userID);		 	              
		 	                String _id = _ind.getPropertyValue(_ontModel.getProperty(CEMConstants.personNS+"ID")).toString();
		 	                if(_id.equals(pUserId))
		 	                {
		 	                	_found=true;
		 	                	_retInd=_ind;
		 	                	 System.out.println("Found individual ID= "+_id+", name="+_ind.getLocalName());  
		 	                	break;
		 	                }
		 	                
		 	               
		            	}
	 	            
		            }
		           
	            }				
	            catch(Exception pExcp)
	            {	
	            	pExcp.printStackTrace();
	            }
	            
	            
	        finally {
	        	qexec.close();
	        }
	        
	        if(!_found)
	        	System.out.println("Did not find an individual with ID="+pUserId);
	        
	        return _retInd;

	}
	
	private static CustomerProfileCBO buildCustomerProfile(List pCustomerOntStmts,String pUserId)
	{
		
		Resource _subject;
		Property _predicate;
		RDFNode _object;
		CustomerProfileCBO _customerProfile=null;
		HashMap<String,Literal> _literalMap = new HashMap();
		HashMap<String,Resource> _objectMap = new HashMap();
		
		
		try
		{
		
			/*
			Class<?> c = Class.forName(CEMUtil.getInstance().getCEMProperty(CEMConstants.customerProfileClass));
			Object _custProfile = c.newInstance();
			Class[] paramString = new Class[1];	
			paramString[0] = String.class;		
			Method m = c.getDeclaredMethod("setUserId", paramString);
			m.invoke(_custProfile,pUserId);
			*/
			
			_customerProfile = new CustomerProfileCBO();
			_customerProfile.setUserId(pUserId);
			
			
			Iterator _it =  pCustomerOntStmts.iterator();
			while(_it.hasNext())
			{
				Statement _stmt = (Statement) _it.next();
				_predicate = _stmt.getPredicate();
				String _predicateName = _predicate.getLocalName();	
				String _predicateNameSpace=_predicate.getURI();	
				
				_object = _stmt.getObject();
				
				if(_object.isLiteral())
				{
					String _varName = ((Resource) _predicate).getLocalName();
					Literal _varValue = _object.asLiteral();					
					_literalMap.put(_varName, _varValue);			
					
				}
				if(_object.isResource())
				{
					
					String _entityName = _object.asResource().getURI();
					//System.out.println("Entity Name:"+_entityName);
					Individual _ind = CustomerProfileUtil.getInstance().getOntModel().getIndividual(_entityName);
					String _className=null,_namespace=null;
					if(_ind !=null)
					{
						
						try
						{
							_className=_ind.getOntClass().getLocalName().toString();
							_namespace = _ind.getNameSpace();
						}
						catch(Exception excp)
						{
							
						}
						//System.out.println("Entity Name:"+_entityName+", Class Name: "+_className);
						String mKey = _predicateName+"_"+_className+"."+_ind.getLocalName();
						//System.out.println("Key:"+ mKey);
					

						//System.out.println("Predicate Namespace:"+_predicateNameSpace+",MyPredicate: "+CEMConstants.baseOntBaseURI+"BaseProperty");
						if(! _predicateNameSpace.equals(CEMConstants.baseOntNS+"BaseProperty"))
							_objectMap.put(mKey, _ind);
					}
					
					
					
					
				}
			}
			updateLiterals(_literalMap,_customerProfile);
			updateObjects(_objectMap,_customerProfile);
			
			CEMUtil.getInstance().printOutCustomerProfile(_customerProfile);
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		
		
		return _customerProfile;
	}
	
	private static void updateLiterals(HashMap pLiteralMap,CustomerProfileCBO pCustomerProfile)
	{
				
		
		
		pCustomerProfile.setFirstName(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontFirstName"))).getString());
		pCustomerProfile.setLastName(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontLastName"))).getString());
		pCustomerProfile.setDateOfBirth(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontDoB"))).getString());
		pCustomerProfile.setGender(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontGender"))).getString());
		pCustomerProfile.setAge(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontAge"))).getInt());
		pCustomerProfile.setProfession(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontProfession"))).getString());	
		pCustomerProfile.setFaceBookId(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontFaceBookId"))).getString());
		if((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontTwitterId"))!=null)
			pCustomerProfile.setTwitterId(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontTwitterId"))).getString());
		if((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontLInkedInId"))!=null)
			pCustomerProfile.setLinkedInId(((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontLInkedInId"))).getString());
		
		if((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontAddress"))!=null)
		{
			String _address = ((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontAddress"))).getString();
			Address _addr = new Address();		
			_addr.setStreetNum(_address);
			AddressList _addrList = new AddressList();
			List _alist = _addrList.getAddress();
			_alist.add(_addr);
			pCustomerProfile.setAddressList(_addrList);		
		}
		
		String _languageSpoken = ((Literal)pLiteralMap.get(CEMUtil.getInstance().getCEMProperty("ontLanguage"))).getString();	
		LanguageSpokenList _languages = new LanguageSpokenList();
		List _ls = _languages.getLanguageSpoken();
		_ls.add(_languageSpoken);
		pCustomerProfile.setLanguageSpokenList(_languages);	
		
	}
	
	private static void updateObjects(HashMap pObjMap, CustomerProfileCBO pCustProfile)
	{
		OntClass mclass=null, _sportClass=null,_foodClass=null,_drinkClass=null,_clothingClass=null;
		String _paymentKey = CEMConstants.has+"_"+CEMUtil.getInstance().getCEMProperty("ontPayment");
		String _loyaltyKey = CEMConstants.has+"_"+CEMUtil.getInstance().getCEMProperty("ontLoyalty");
		String _sportClassName=CEMConstants.personNS+CEMUtil.getInstance().getCEMProperty("ontSport");
		String _foodClassName=CEMConstants.productNS+CEMUtil.getInstance().getCEMProperty("ontFood");
		String _drinkClassName=CEMConstants.productNS+CEMUtil.getInstance().getCEMProperty("ontDrink");
		String _clothingClassName=CEMConstants.productNS+CEMUtil.getInstance().getCEMProperty("ontClothing");
		ArrayList _paymentList=null,_loyaltyList=null;
		
		
		PreferencesList _prefList = new PreferencesList();
		FoodProfile _fp = new FoodProfile();
		FavouriteFoodList _foodList = new FavouriteFoodList();
		DrinkProfile _dp = new DrinkProfile();
		FavouriteDrinkList  _drinkList = new FavouriteDrinkList();
		SportsProfile _sp = new SportsProfile();
		FavouriteSportList _fsl = new FavouriteSportList();	
		FavouritePlayerList _fvl= new FavouritePlayerList();	 
		FavouriteTeamList _ftl = new FavouriteTeamList();
		MusicProfile _mp = new MusicProfile();
		FavouriteBandList _fbl = new FavouriteBandList();
		SocialMediaProfile _smp = new SocialMediaProfile();
		SocialMediaFriendList _smfl = new SocialMediaFriendList();
		ShoppingProfile _shopP = new ShoppingProfile();
		FavouriteMerchandiseList _fml = new FavouriteMerchandiseList();
		
				
		//System.out.println("@@@@ ClassNames: Food="+_foodClassName+",Drink="+_drinkClassName+",Sport="+_sportClassName);
		
		Set _keySet = pObjMap.keySet();
		Iterator _keyIt = _keySet.iterator();
		
		while(_keyIt.hasNext())
		{
			String _key = (String) _keyIt.next();
			
			if(_key.contains(_paymentKey))
			{
				_paymentList = new ArrayList();
				_paymentList.add(pObjMap.get(_key));
				
				
			}
			if(_key.contains(_loyaltyKey))
			{
				_loyaltyList= new ArrayList();
				_loyaltyList.add(pObjMap.get(_key));
			}
			
		}
		
		//Individual _paymentInd = (Individual) pObjMap.get(_paymentKey);
		//Individual _loyaltyInd = (Individual) pObjMap.get(_loyaltyKey);
		//System.out.println("LoyaltyPrefs:"+_loyaltyList.size()+", PaymentPrefs:"+_paymentList.size());
		
		//-----payment------------------
		if(_paymentList !=null)
		{
			Iterator _paymentIt = _paymentList.iterator();
			while(_paymentIt.hasNext())
			{
				Individual _paymentInd = (Individual) _paymentIt.next();
				PaymentPrefference _payPref = new PaymentPrefference();
				_payPref.setCardNumber(_paymentInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"CardNumber")).toString());
				_payPref.setCardType(_paymentInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"CardType")).toString());
				_payPref.setIssuingBank(_paymentInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"IssuingBank")).toString());
		  		PaymentPrefferenceList _ppl = new PaymentPrefferenceList();
		  		_ppl.getPaymentPrefference().add(_payPref);
		  		pCustProfile.setPaymentPrefferenceList(_ppl);
			}							
		}
	
	  	//-----loyalty-----------------
		if(_loyaltyList !=null)
		{
			Iterator _loyaltyIt = _loyaltyList.iterator();
			while(_loyaltyIt.hasNext())
			{
				Individual _loyaltyInd = (Individual) _loyaltyIt.next();
				LoyaltyProgram _loyalty = new LoyaltyProgram();
				_loyalty.setLoyaltyProramName("BaseProgram");
				_loyalty.setLoyaltyUserId(_loyaltyInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"LoyaltyUserId")).toString());
				_loyalty.setLoyaltyTier(_loyaltyInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"LoyaltyTier")).toString());
				_loyalty.setTotalAcumulatedPoints(_loyaltyInd.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"TotalLoyaltyPoints")).asLiteral().getLong());
				pCustProfile.setLoyaltyProgram(_loyalty);
			}
		}
  		
  		
  		Set _mapList = pObjMap.entrySet();
  		Iterator _setIt = _mapList.iterator();
  		while(_setIt.hasNext())
  		{
  			
  			Entry _ee = (Entry) _setIt.next();
  			String _key=(String)_ee.getKey();  	
  			String _pKey = _key.substring(0, _key.indexOf("_"));
  			
  			//System.out.println("Key ="+ _pKey);
  			
  			Individual _entry = (Individual)_ee.getValue();  	
  			//System.out.println("Individual="+_entry);
  			String _namespace = _entry.getNameSpace();
  			  			
  			
  			//if(( _namespace.equals(CEMConstants.personNS) ||  _namespace.equals(CEMConstants.productNS)) && (_pKey.equals(CEMConstants.has) || _pKey.equals(CEMConstants.likes)))
  			if(( _namespace.equals(CEMConstants.personNS) ||  _namespace.equals(CEMConstants.productNS)))
  		  			
  			{
	  			try
	  			{
	  				mclass = _entry.getOntClass();
	  				//System.out.println("@@@@ Class = "+mclass.getLocalName());
	  				_sportClass=CustomerProfileUtil.getInstance().getOntModel().getOntClass(_sportClassName);
	  				_foodClass=CustomerProfileUtil.getInstance().getOntModel().getOntClass(_foodClassName);
	  				_drinkClass=CustomerProfileUtil.getInstance().getOntModel().getOntClass(_drinkClassName); 
	  				_clothingClass=CustomerProfileUtil.getInstance().getOntModel().getOntClass(_clothingClassName);
	  				
	  			//	System.out.println("classes: sports:"+_sportClass+", Food="+_foodClass+",Drink="+_drinkClass);
	  				
	  			}
	  			catch(Exception excp)
	  			{
	  				
	  			}
	  		
	  			
	  			if(CEMUtil.getInstance().isIndividualOfType(_entry, _foodClass))
	  			{
	  				//System.out.println("--Found Food");
	  					  				
	  				Food _food = new Food();
	  				_food.setDishName(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.productNS+"ProductName")).toString());  	  				
	  				 List _fl = _foodList.getFood();
	  				_fl.add(_food);  				
	  				//System.out.println("FOOOD = "+_food.getDishName());
	  				
	  			}
	  			
	  			if(CEMUtil.getInstance().isIndividualOfType(_entry, _drinkClass))
	  			{
	  				//System.out.println("--Found Drink");
	  				Drink _drink = new Drink();
	  				String _drinkName = _entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.productNS+"ProductName")).toString();
	  				//System.out.println("DRINK = "+_drinkName);
	  				_drink.setDrinkName(_drinkName); 
	  				String _drinkType=null;
	  				try
	  				{
	  					//_drinkType = _entry.getOntClass().getSubClass().getLocalName();
	  					_drinkType = _entry.getOntClass().getLocalName();
	  					
	  				}
	  				catch(Exception excp)
	  				{
	  					excp.printStackTrace();
	  				}
	  				
	  				_drink.setDrinkType(_drinkType);
	  				 List _dl = _drinkList.getDrink();
	  				_dl.add(_drink);  				
	  				
	  			}
	  			
	  			if(CEMUtil.getInstance().isIndividualOfType(_entry, _sportClass))
	  			{
	  				//System.out.println("--Found Sport");
	  				
	  				  
	  				List _sportList = _fsl.getSport();
	  				
	  				Sport _sport = new Sport();	  				
	  				_sport.setGeneralGameSentiment(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"GameSentiment")).toString());
	  				_sport.setNumGamesAttendedSeason(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"NumGamesAttendedSeason")).asLiteral().getLong());
	  				_sport.setNumGamesAttendedTotal(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"NumGamesAttendedTotal")).asLiteral().getLong());
	  				_sport.setSportType(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"SportType")).toString());
	  				
	  				_sportList.add(_sport);
	  				
	  				
	  			}
	  			if(mclass.getLocalName().equals(CEMUtil.getInstance().getCEMProperty("ontSportsPlayer")))
	  			{
	  					  				
	  				 				
	  				List _spl = _fvl.getSportPlayer();
	  				
	  				SportPlayer _player = new SportPlayer();
	  				
	  				//System.out.println("Entry Value: "+_entry);
	  				RDFNode _playerName = _entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"PlayerName"));
	  				RDFNode _playerTeam = _entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"TeamName"));
	  				RDFNode _playerSportType = _entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"SportType"));
	  				
	  				if(_playerName!=null)
	  					_player.setPlayerName(_playerName.toString());
	  				if(_playerSportType!=null)
	  					_player.setSportType(_playerSportType.toString()); 	
	  				if(_playerTeam!=null)
	  					_player.setTeamName(_playerTeam.toString()); 	
	  				if(_playerName!=null && _playerSportType!=null && _playerTeam!=null )	  	
	  					_spl.add(_player);
	  				
	  				
	  				
	  			}
	  			
	  			//if(CEMUtil.getInstance().isIndividualOfType(_entry, CustomerProfileUtil.getInstance().getOntModel().getOntClass(CEMConstants.personNS+"SportsTeam")))
	  			if(mclass.getLocalName().equals(CEMUtil.getInstance().getCEMProperty("ontSportsTeam")))
	  			{
	  				List _sportsTeam = _ftl.getSportTeam();
	  				
	  				SportTeam _team = new SportTeam();
	  				
	  				_team.setTeamName( _entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"TeamName")).toString());
	  				_team.setSportType(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"SportType")).toString());
	  				
	  				_sportsTeam.add(_team);
	  				
	  			}
	  			
	  			//if(CEMUtil.getInstance().isIndividualOfType(_entry, CustomerProfileUtil.getInstance().getOntModel().getOntClass(CEMConstants.personNS+"Band")))
	  			if(mclass.getLocalName().equals(CEMUtil.getInstance().getCEMProperty("ontBand")))
	  			{
	  				
	  				List _bandList = _fbl.getBand();
	  				
	  				Band _band = new Band();
	  				_band.setBandName(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"BandName")).toString());
	  				_band.setMusicType(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"BandStyle")).toString());
	  				
	  				_bandList.add(_band);
	  				
	  				
	  			}
	  			//if(CEMUtil.getInstance().isIndividualOfType(_entry, CustomerProfileUtil.getInstance().getOntModel().getOntClass(CEMConstants.personNS+"SportsPlayer")))
	  			
	  			//if(CEMUtil.getInstance().isIndividualOfType(_entry, CustomerProfileUtil.getInstance().getOntModel().getOntClass(CEMConstants.personNS+"SocialMediaFriend")))
	  			if(mclass.getLocalName().equals(CEMUtil.getInstance().getCEMProperty("ontSocialMedia")))
	  			{
	  						
	  				List _friendList=_smfl.getSocialMediaFriend();
	  				
	  				SocialMediaFriend _friend = new SocialMediaFriend();
	  				_friend.setFriendName(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"SocialMediaFriendName")).toString());
	  				_friend.setSocialAppName(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.personNS+"SocialMediaFriendApp")).toString());
	  				
	  				_friendList.add(_friend);
	  				
	  			}
	  			
	  			if(CEMUtil.getInstance().isIndividualOfType(_entry, _clothingClass))
	  			{
	  				List _mercL = _fml.getMerchandise();
	  				
	  				Merchandise _merchandise = new Merchandise();
	  				_merchandise.setMerchandiseName(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.productNS+"ProductName")).toString());
	  				_merchandise.setMerchandiseSize(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.productNS+"ItemSize")).toString());
	  				_merchandise.setMerchandiseType(_entry.getPropertyValue(CustomerProfileUtil.getInstance().getOntModel().getProperty(CEMConstants.productNS+"ProductType")).toString());	  		
	  			
	  				String _prodCategory = _entry.getOntClass().getSuperClass().getLocalName();
	  				
	  				_merchandise.setMerchandiseCategory(_prodCategory);  						
	  				
	  				_mercL.add(_merchandise);
	  				
	  			}
	  	  			
  			}
  			
  		}
  		//---settle food----
  		_fp.setFavouriteFoodList(_foodList);
		_prefList.setFoodProfile(_fp);
		_dp.setFavouriteDrinkList(_drinkList);
		_prefList.setDrinkProfile(_dp);
		_prefList.setSportsProfile(_sp);
		_sp.setFavouriteSportList(_fsl);
		_sp.setFavouritePlayerList(_fvl);
		_sp.setFavouriteTeamList(_ftl);
		_mp.setFavouriteBandList(_fbl);
		_prefList.setMusicProfile(_mp);
		_smp.setSocialMediaFriendList(_smfl);
		_prefList.setSocialMediaProfile(_smp);
		_shopP.setFavouriteMerchandiseList(_fml);
		_prefList.setShoppingProfile(_shopP);
		
		
  		pCustProfile.setPreferencesList(_prefList);		
  		
		
		
	}

}
