package com.extemp.cem.util;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.kie.api.runtime.rule.EntryPoint;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

import com.extemp.cem.events.SportsVenueMobileEvent;
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
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

public class CEMUtil 
{
	private Properties _props;
	private static CEMUtil instance=null;
	private ActorSystem akkaSystem;
	private ActorRef _ruleSession;
	
	
	public CEMUtil()
	{
		_props = new Properties();
	}
	
	public static CEMUtil getInstance()
	{
		if(instance==null)
			instance = new CEMUtil();
		
		return instance;
	}
	public String getCEMProperty(String pKey)
	{
		return _props.getProperty(pKey);
	}
	public String getReasonerType()
	{
		return _props.getProperty(CEMConstants.reasonerTypeProp);
	}
	public void setAkkaSystem(ActorSystem pSystem)
	{
		akkaSystem=pSystem;
		
		ActorSelection _actor = akkaSystem.actorSelection("KieRuleSession");
		_ruleSession = _actor.anchor();
	}
	public ActorRef getRuleSession()
	{
		return _ruleSession;
	}
	public ActorSystem getAkkaSystem()
	{
		return akkaSystem;
	}
	
	
	public void loadProperties(String pPropertiesFile)
	{
		
		InputStream _input = null;
		
		if(pPropertiesFile !=null)
		{
			System.out.println("Propsfilename:"+pPropertiesFile );
			try {
	 
				_input = new FileInputStream(pPropertiesFile); 
				_props.load(_input); 
				_input.close();
				//System.out.println(prop.getProperty("database"));		
	 
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
			
		}
 
	}
	public String getCustomerProfileOntology()
	{
		return _props.getProperty(CEMConstants.ontFileProp);
	}
	public void simulateBaiscEvents()
	{
		
		
		EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "MobileEventChannel");
		
		//SportsVenueMobileEvent event0 = new SportsVenueMobileEvent("123","777","MOBILE_EVENT","my_phone","GAME_ON_SOCIAL_FEED","Getting twitter feeds !","333");
		SportsVenueMobileEvent event1 = new SportsVenueMobileEvent("123","777","GAME_ON_PARKING","my_phone","IN_ARENA","Hello, I'm in the parking lot !","333");
		event1.setApplicationSource("Hockey Mobile App");		
		SportsVenueMobileEvent event2 = new SportsVenueMobileEvent("123","777","GAME_ON_TICKET_CHECK","my_phone","IN_ARENA","Bought ticket 3456 !","333");
		event2.setApplicationSource("Hockey Mobile App");	
		/*
		SportsVenueMobileEvent event3 = new SportsVenueMobileEvent("123","777","GAME_ON_IN_SEAT","my_phone","IN_ARENA","Got to my seat !","333");		
		SportsVenueMobileEvent event4 = new SportsVenueMobileEvent("123","777","GAME_ON_START","my_phone","IN_ARENA","Game starting !","333");		
		SportsVenueMobileEvent event5 = new SportsVenueMobileEvent("123","777","GAME_ON_STOPPAGE","my_phone","IN_ARENA","We're in stoppage time!","333");		
		SportsVenueMobileEvent event6 = new SportsVenueMobileEvent("123","777","GAME_ON_FOOD","my_phone","IN_ARENA","BOught some food !","333");		
		SportsVenueMobileEvent event7 = new SportsVenueMobileEvent("123","777","GAME_ON_IN_GAME","my_phone","IN_ARENA","Hat trick time !","333");		
		SportsVenueMobileEvent event8 = new SportsVenueMobileEvent("123","777","GAME_ON_FINISH","my_phone","IN_ARENA","Game is finished !","333");
		SportsVenueMobileEvent event9 = new SportsVenueMobileEvent("123","777","GAME_OFF_PARKING","my_phone","IN_ARENA","Game done, going home, in parking lot !","333");
		SportsVenueMobileEvent event10 = new SportsVenueMobileEvent("123","777","GAME_ON_OUT_OF_ARENA","my_phone","OUT_OF_ARENA_GAME_ON","Watching the game in a pub !","333");
		SportsVenueMobileEvent event11 = new SportsVenueMobileEvent("123","777","GAME_OFF_OUT_OF_ARENA","my_phone","OUT_OF_ARENA","Just walking about, waiting for the game to start !","333");
		*/
					
		//entryPoint.insert(event0);
		entryPoint.insert(event1);		
		entryPoint.insert(event2);
		
		/*entryPoint.insert(event3);
		entryPoint.insert(event4);
		entryPoint.insert(event5);
		entryPoint.insert(event6);
		entryPoint.insert(event7);
		entryPoint.insert(event8);
		entryPoint.insert(event9);
		entryPoint.insert(event10);
		entryPoint.insert(event10);
		*/
		
		  // Fire all Rules
        KBUtil.getInstance().getKSession().fireAllRules();


        // Close the session
        KBUtil.getInstance().getKSession().destroy();
        System.out.println("*** DONE *** ");
	}
	
	
	public boolean isIndividualOfType(Individual pIndividual,OntClass pClass)
	{
		boolean _retVal=false;
		OntClass cls=null;;
		ArrayList _superClassList = new ArrayList();
		
		
		//System.out.println("Ind = "+pIndividual+", class="+pClass);
		
		if(pIndividual!=null && pClass !=null)
		{
			 for (Iterator xy = pIndividual.listOntClasses(false); xy.hasNext(); )
			 {
				 		try
				 		{
				 			cls = (OntClass) xy.next();		  
				 		}
				 		catch(Exception excp)
				 		{			 			
				 		}
			    		
			    			OntClass _scls = cls;
			    			//System.out.println( "Object has rdf:type " + cls.getLocalName() );  
			    			if(_scls!=null)
			    			{
			    				while(_scls.hasSuperClass())
				    			{						    				 
				    				//if(cls.hasSuperClass(pClass,true))		
				    				if(_scls.getURI().equals(pClass.getURI()))		 
				    				{				    			
						    			//System.out.println("*****FOund It*****");
				    					_retVal=true;
				    					//break;
						    		}
						    		    				
				    				_scls = _scls.getSuperClass();
				    				_superClassList.add(_scls); 				
				    			
				    			}
			    				
			    			}
			    			
			    		
			    }
		}
		
		 /*
		 if(_retVal)
		 {
			 Iterator _it = _superClassList.iterator();
			 while(_it.hasNext())			
				System.out.println("Super class of "+ cls.getLocalName()+" is "+((OntClass)_it.next()).getLocalName());
			
				
		 }
		*/
		return _retVal;
		
	}
	
	
	public void printOutCustomerProfile(CustomerProfileCBO pCustProfile)
	{
		System.out.println("======Customer:"+pCustProfile.getUserId()+"=========");
		System.out.println("Name : "+pCustProfile.getFirstName()+" "+pCustProfile.getLastName());
		System.out.println("Profession: "+ pCustProfile.getProfession());
		System.out.println("Gender: "+ pCustProfile.getGender());
		System.out.println( "Age: "+pCustProfile.getAge());
		System.out.println("DoB: "+ pCustProfile.getDateOfBirth());
		System.out.println("FacebookId: "+ pCustProfile.getFaceBookId());
		System.out.println("TwitterId: "+ pCustProfile.getTwitterId());
		System.out.println("LinkedINId: "+ pCustProfile.getLinkedInId());
		
		AddressList _addrList = pCustProfile.getAddressList();
		if(_addrList !=null)
		{
			List<Address> _alist = _addrList.getAddress();
		
			Iterator _it =_alist.iterator();
			while(_it.hasNext())
			{
				Address _addr = (Address) _it.next();
				System.out.println("Address: "+_addr.getStreetNum());
			}
		}
		
		LanguageSpokenList _languages = pCustProfile.getLanguageSpokenList();
		if(_languages !=null)
		{
			List _ls = _languages.getLanguageSpoken();
			Iterator _itl = _ls.iterator();
			while(_itl.hasNext())
			{
				String _language = (String) _itl.next();
				System.out.println("Spoken Language:"+ _language);
			}
			
		}
		
		PaymentPrefferenceList _ppl = pCustProfile.getPaymentPrefferenceList();
		if(_ppl!=null)
		{
			List _payList = _ppl.getPaymentPrefference();
			Iterator _payIt = _payList.iterator();
			while(_payIt.hasNext())
			{
				PaymentPrefference _ppf = (PaymentPrefference) _payIt.next();
				System.out.println("Payment Prefference: Card Num="+_ppf.getCardNumber()+",Card Type="+_ppf.getCardType()+", Issuing Bank="+_ppf.getIssuingBank());
					
			}
		}
		
		LoyaltyProgram _loyalty = pCustProfile.getLoyaltyProgram();
		if(_loyalty!=null)
		System.out.println("LoyaltyProgram:"+_loyalty.getLoyaltyProramName()+", ID="+_loyalty.getLoyaltyUserId()+", Tier="+_loyalty.getLoyaltyTier()+",points="+_loyalty.getTotalAcumulatedPoints());
		
		
		System.out.println("-----------Preferences------------");
		PreferencesList _prefList = pCustProfile.getPreferencesList();
		
		System.out.println("---Food");
		FoodProfile _fp = _prefList.getFoodProfile();
		FavouriteFoodList _fvl=_fp.getFavouriteFoodList();
		List _fl = _fvl.getFood();
		Iterator _fit=_fl.iterator();
		while(_fit.hasNext())
		{
			Food _food = (Food) _fit.next();
			System.out.println("FoodItem:"+_food.getDishName());
			
		}
			
		System.out.println("---Drink");
		DrinkProfile _dp = _prefList.getDrinkProfile();
		FavouriteDrinkList _fdl=_dp.getFavouriteDrinkList();
		List _dl = _fdl.getDrink();
		Iterator _dit=_dl.iterator();
		while(_dit.hasNext())
		{
			Drink _drink = (Drink) _dit.next();
			System.out.println("FoodItem:"+_drink.getDrinkName()+", of type "+_drink.getDrinkType());
			
		}
		
		System.out.println("---Sport");
		SportsProfile _sp = _prefList.getSportsProfile();
		FavouriteSportList _fsl= _sp.getFavouriteSportList();
		List _sl = _fsl.getSport();
		
		Iterator _sit = _sl.iterator();
		while(_sit.hasNext())
		{
			Sport _sport = (Sport) _sit.next();
			System.out.println("SportType="+_sport.getSportType()+",Sentiment="+_sport.getGeneralGameSentiment()+",SeasonGamesAttended="+_sport.getNumGamesAttendedSeason()+",TotalGamesattended="+_sport.getNumGamesAttendedTotal());
			
		}
		System.out.println("---SportPlayer");
		FavouritePlayerList _fpl = _sp.getFavouritePlayerList();
		List _fpList=_fpl.getSportPlayer();
		Iterator _fplIt = _fpList.iterator();
		while(_fplIt.hasNext())
		{
			SportPlayer _player = (SportPlayer)_fplIt.next();
			System.out.println("Favourite Sport Player: Sport="+_player.getSportType()+", Team="+_player.getTeamName()+", Player Name="+_player.getPlayerName());
		}
				
		System.out.println("---SportTeam");
		FavouriteTeamList _teamList = _sp.getFavouriteTeamList();
		List _tl=_teamList.getSportTeam();
		Iterator _tlIt = _tl.iterator();
		while(_tlIt.hasNext())
		{
			SportTeam _team = (SportTeam) _tlIt.next();
			System.out.println("Sport Team: Name="+_team.getTeamName()+", SportType="+_team.getSportType());
			
			
		}
		//System.out.println("---Musician");
		System.out.println("---Band");
		MusicProfile _mp=_prefList.getMusicProfile();
		FavouriteBandList _bandList=_mp.getFavouriteBandList();
		List _bl=_bandList.getBand();
		Iterator _blIt = _bl.iterator();
		while(_blIt.hasNext())
		{
			Band _band = (Band) _blIt.next();
			System.out.println("FavouriteBand: BandName="+_band.getBandName()+", BandStyle="+_band.getMusicType());
		}
		System.out.println("---Social Media");
		
		SocialMediaProfile _smp = _prefList.getSocialMediaProfile();
		SocialMediaFriendList _smfl = _smp.getSocialMediaFriendList();
		List _friendList = _smfl.getSocialMediaFriend();
		Iterator _smfIt = _friendList.iterator();
		while(_smfIt.hasNext())
		{
			SocialMediaFriend _friend = (SocialMediaFriend) _smfIt.next();
			System.out.println("Social Buddy: SocMediaFriendName="+_friend.getFriendName()+", SocMediaApp="+_friend.getSocialAppName());
			
		}
		
		
		System.out.println("---Merchandise");
		
		ShoppingProfile _shProfile=_prefList.getShoppingProfile();
		FavouriteMerchandiseList _mercList=_shProfile.getFavouriteMerchandiseList();
		List _ml = _mercList.getMerchandise();
		Iterator _mIt = _ml.iterator();
		while(_mIt.hasNext())
		{
			Merchandise _merc = (Merchandise) _mIt.next();
			System.out.println("Merchandise: Name="+_merc.getMerchandiseName()+", Type="+_merc.getMerchandiseType()+", Category="+_merc.getMerchandiseCategory()+", Size="+_merc.getMerchandiseSize());
		}
		
		
	}
	
	
}
