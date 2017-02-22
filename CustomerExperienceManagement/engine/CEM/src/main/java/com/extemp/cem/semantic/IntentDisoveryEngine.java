package com.extemp.cem.semantic;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.extemp.cem.util.semantic.Constants;
import com.extemp.cem.util.semantic.InformationClusterDetector;
import com.extemp.cem.util.semantic.OntologyConverterUtil;
import com.extemp.cem.util.semantic.OntologyUtil;
import com.extemp.cem.semantic.vo.IntentVO;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.ResourceUtils;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class IntentDisoveryEngine
{
	
	private OntModel _intentModel,_personModel,_eventModel,_productModel;
	private OntClass _browseActivityClass,_viewActivityClass,_publishActivityClass,_listenActivityClass;
	private OntProperty _hasIntent;
	private DatatypeProperty _prodName, _prodType,_prodSize,_prodGender,_prodCost,_prodExpiry;
	private String _objName,_objType,_predicateName,_predicateType,_itemSize,_itemGender,_expiryDate,_audioTopic,_videoTopic,_auddioURL,videoURL,_productName;	
	private String _publishedContentData,_publishedContentURL,_publishedContentTopic,_browsedURL,_browsedItemCategory,_browsedItemName,_browsedItemID;
	private double _prodPrice;
	private List _candidateIntentStatementList;
	private Bag _statementBag;
	private IntentInferenceHarness _intentInferenceHarness;
	private ArrayList _userList=new ArrayList();	
	
	public IntentDisoveryEngine(OntModel pModel)
	{
		_intentModel=pModel;
		
				
		_browseActivityClass = _intentModel.getOntClass(Constants.intentNS+"BrowseActivity");
		_viewActivityClass = _intentModel.getOntClass(Constants.intentNS+"ViewActivity");
		_publishActivityClass = _intentModel.getOntClass(Constants.intentNS+"PublishActivity");
		_listenActivityClass = _intentModel.getOntClass(Constants.intentNS+"ListenActivity");
		_hasIntent = _intentModel.getOntProperty(Constants.intentNS+"HasIntent");		
		/*
		_prodName = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductName");
		_prodType = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductType");
		_prodSize = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductSize");
		_prodGender = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductGender");
		_prodCost = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductCost");		
		_prodExpiry = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductExpiryDate");
		*/
		
		_intentInferenceHarness=new IntentInferenceHarness(pModel);
		
		
	}
	
	public synchronized List getIntents(OntModel pEventModel,OntModel pPersonModel,OntModel pProductModel,String pReasonerType)
	{
		//ArrayList _lUserList=new ArrayList();
		ArrayList _finalList = new ArrayList();
		
		//1) get all users===============================================================
		_productModel=pProductModel;
		_personModel=pPersonModel;
		_eventModel=pEventModel;
		
		ExtendedIterator _xit = pPersonModel.listIndividuals(pPersonModel.getOntClass(Constants.personNS+"BrowsingPerson"));	
		
		while(_xit.hasNext())
		{
			Individual _ind = (Individual) _xit.next();
			//System.out.println("&&&&&&&&&&&&&&&&& "+_ind.toString());
			String _browsing = _ind.getPropertyValue(_personModel.getProperty(Constants.personNS + "BrowsingProcessed")).toString();
			System.out.println("Browsing Processed:"+_browsing);
			if(_browsing.equals("false"))
				//_lUserList.add(_ind);	
				_userList.add(_ind);
		}
		
		//2) for every user get composite intents===========================================
		//Iterator _mit = _lUserList.iterator();
		Iterator _mit = _userList.iterator();
		//System.out.println("+++num users:"+_lUserList.size());
		
		while(_mit.hasNext())
		{
					
			Individual _browsingIndividual = (Individual)_mit.next();
			//System.out.println(_subj.toString());
			String _userId = _browsingIndividual.getPropertyValue(_personModel.getProperty(Constants.personNS+"ID")).toString();			
			List _candidateStmts = getCandidateStmstByUser(_browsingIndividual,_eventModel,_personModel,_productModel);
			
			List _intentList = getIntentList(_candidateStmts,_intentModel,pReasonerType);
			_finalList.add(new IntentVO(_userId,_intentList));	
			
			//3) move person to history======================================================
			
			
			//backupIndividual(_browsingIndividual,pReasonerType);	
			
		}
						
		//closeBrowsingList();
		
		return _finalList;
	}
	
	private void backupIndividual(Individual pInd,String pReasonerType)
	{
		
		//List _intentList_x = _intentInferenceHarness.runReasoner2(pInd,_eventModel);			
		//List _intentList_x = _intentInferenceHarness.runReasoner2(pInd,_personModel);	
		int _cntr=0;
		boolean _gotUid=false;
		Individual _person=null;
		 OntClass _personCls = _personModel.getOntClass(Constants.personNS + "BrowsingPerson");	
		 DatatypeProperty _personID=_personModel.getDatatypeProperty(Constants.personNS + "ID");	
		 OntProperty _hasPerson = _eventModel.getOntProperty(Constants.eventNS+"HasPerson");
		 OntClass _event = _eventModel.getOntClass(Constants.eventNS+"Event");
		
		StmtIterator _it = pInd.listProperties();
		
		while(_it.hasNext())
		{
			Statement _stmt = (Statement) _it.next();
			Resource _subj = _stmt.getSubject();
			Property _predicate = _stmt.getPredicate();
			RDFNode _object = _stmt.getObject();
			if(!_gotUid)
			{
				String _userId = (_subj.as(Individual.class)).getPropertyValue(_personModel.getProperty(Constants.personNS,"ID")).toString();
				//System.out.println("UID="+_userId);
				_cntr= OntologyUtil.getInstance().getCounter();
				String _newUserId = "user-"+_userId+"-"+_cntr;
				 _person = _personCls.createIndividual(Constants.personNS+_newUserId);
				 _person.addProperty(_personID, _userId);
			}
						 
			 _person.addProperty(_predicate,_object);	 
		
			//System.out.println("^^^^^^^"+_stmt.toString());		 
		}
		
		
		Individual _evInd = _event.createIndividual(Constants.eventNS+"Event"+"-"+_cntr);
		System.out.println("++++Subj="+_evInd+",pred="+_hasPerson+",obj="+_person);
		_evInd.addProperty(_hasPerson, _person);
				
		
	}
	private List getCandidateStmstByUser(Individual pIndividual,OntModel pEventModel,OntModel pPersonModel,OntModel pProductModel)
	{
		_candidateIntentStatementList = new ArrayList();
		
		StmtIterator _stmtIt=pIndividual.listProperties();
		
		while(_stmtIt.hasNext())
		{
			_productName=null;_itemSize=null;_itemGender=null;_prodPrice=0; _expiryDate=null;_publishedContentURL=null;_publishedContentTopic=null;_publishedContentData=null;
			_browsedItemName=null;_browsedItemCategory=null;_browsedURL=null;_browsedItemID=null;
			
			
			Statement _stmt= _stmtIt.next();
			System.out.println(_stmt.toString());
			
			Individual _subj=_stmt.getSubject().as(Individual.class);
			//_userList.add(_subj);
			Property _predicate = _stmt.getPredicate();		
			RDFNode _obj = _stmt.getObject();
			
			System.out.println("===========");
			
			 if(_obj.isResource())
			    {
					//subject Data
					String _subjName=_subj.getLocalName();
					Literal _subjID = _subj.getPropertyValue(pEventModel.getProperty(Constants.personNS+"ID")).asLiteral();
					String _subjID_1=_subjID.toString();
					
					System.out.println("---Subject:"+_subjName+"; PersonID="+_subjID_1);
					
					//predicate data
					 _predicateName=_predicate.getLocalName();
					System.out.println("---Predicate:"+ _predicateName);
				
					//object data
			   
					Resource _objR = _obj.as(Resource.class);
									
					Individual _obj_1=pEventModel.getIndividual(_objR.getURI());					
			
					//System.out.println("BrowseURL="+_objR.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL")).asLiteral());
					//System.out.println("Prodname="+_objR.getPropertyValue(pPersonModel.getDatatypeProperty(Constants.productNS + "ProductName")));
					
					if(_predicateName.equals("Browse") || _predicateName.equals("ViewVideo") || _predicateName.equals("ListenAudio") || _predicateName.equals("PublishContent"))
					{
									
					if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductName"))!=null)
						_productName= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductName")).toString();
					if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemSize"))!=null)
						_itemSize= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemSize")).asLiteral().toString();
					if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemGender"))!=null)
						_itemGender= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemGender")).asLiteral().toString();
					if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductPrice"))!=null)
						_prodPrice= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductPrice")).asLiteral().getDouble();					
					if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductExpiryDate"))!=null)
						_expiryDate= _obj_1.getPropertyValue(pProductModel.createDatatypeProperty(Constants.productNS + "ProductExpiryDate")).asLiteral().toString();					 
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentURL"))!=null)
						 _publishedContentURL= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentURL")).toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentTopic"))!=null)
						 _publishedContentTopic= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentTopic")).toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentData"))!=null)
						 _publishedContentData= _obj_1.getPropertyValue(pEventModel.createDatatypeProperty(Constants.eventNS + "PublishedContentData")).toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemName"))!=null)
							_browsedItemName= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemName")).asLiteral().toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemCategory"))!=null)
							_browsedItemCategory= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemCategory")).asLiteral().toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL"))!=null)
							_browsedURL= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL")).asLiteral().toString();
					 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemID"))!=null)
							_browsedItemID= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemID")).asLiteral().toString();
					
					
					/* System.out.println("prodName="+_productName+",itemSize="+_itemSize+",itemGender="+_itemGender+",prodPrice="+_prodPrice+",expDate="+_expiryDate+",pubContURL="+
											_publishedContentURL+",pubContTopic="+_publishedContentTopic+",pubContData="+_publishedContentData+",BrowsedItemID="+_browsedItemID+
											",BrowsedURL="+_browsedURL+",BrowsedItemName="+_browsedItemName+",BrowsedItemCategory="+_browsedItemCategory);
											*/
					 
					//================convert to Intent model======	
					 updateIntentCandidateModel(_intentModel,_candidateIntentStatementList,_subjID_1);	
					}
			    }
		}
		
		
		
		return _candidateIntentStatementList;
		
	}
	public synchronized List getCandidateIntentStatements(OntModel pEventModel,OntModel pPersonModel,OntModel pProductModel)
	{
		
		//Bag _eventBag = pEventModel.getBag(Constants.eventNS+"EventBag");
		//		_eventBag.		
	
		_productModel=pProductModel;
		_personModel=pPersonModel;
		_eventModel=pEventModel;
		
		_candidateIntentStatementList = new ArrayList();
		ExtendedIterator _xit = pPersonModel.listIndividuals(pPersonModel.getOntClass(Constants.personNS+"BrowsingPerson"));	
		
		//breakOut by User ID
		
		while(_xit.hasNext())
		{
			Individual _ind = (Individual) _xit.next();
			String _browsing = _ind.getPropertyValue(_personModel.getProperty(Constants.personNS + "BrowsingProcessed")).toString();
			System.out.println("Browsing Processed:"+_browsing);
			if(_browsing.equals("false"))
				_userList.add(_ind);			
		}
		
		Iterator _it = _userList.iterator();
		while(_it.hasNext())
		{
					
			Individual _browsingIndividual = (Individual)_it.next();
			//System.out.println(_subj.toString());
			
			StmtIterator _stmtIt=_browsingIndividual.listProperties();
			//StmtIterator _stmtIt = _browsingIndividual.listProperties(pEventModel.getProperty(Constants.eventNS + "EventActivity"));
			while(_stmtIt.hasNext())
			{
				
				_productName=null;_itemSize=null;_itemGender=null;_prodPrice=0; _expiryDate=null;_publishedContentURL=null;_publishedContentTopic=null;_publishedContentData=null;
				_browsedItemName=null;_browsedItemCategory=null;_browsedURL=null;_browsedItemID=null;
				
				
				Statement _stmt= _stmtIt.next();
				System.out.println(_stmt.toString());
				
				Individual _subj=_stmt.getSubject().as(Individual.class);
				//_userList.add(_subj);
				Property _predicate = _stmt.getPredicate();		
				RDFNode _obj = _stmt.getObject();
								
				System.out.println("===========");
				
				 if(_obj.isResource())
				    {
						//subject Data
						String _subjName=_subj.getLocalName();
						Literal _subjID = _subj.getPropertyValue(pEventModel.getProperty(Constants.personNS+"ID")).asLiteral();
						String _subjID_1=_subjID.toString();
						
						System.out.println("---Subject:"+_subjName+"; PersonID="+_subjID_1);
						
						//predicate data
						 _predicateName=_predicate.getLocalName();
						System.out.println("---Predicate:"+ _predicateName);
					
						//object data
				   
						Resource _objR = _obj.as(Resource.class);
										
						Individual _obj_1=pEventModel.getIndividual(_objR.getURI());					
				
						//System.out.println("BrowseURL="+_objR.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL")).asLiteral());
						//System.out.println("Prodname="+_objR.getPropertyValue(pPersonModel.getDatatypeProperty(Constants.productNS + "ProductName")));
						
						if(_predicateName.equals("Browse") || _predicateName.equals("ViewVideo") || _predicateName.equals("ListenAudio") || _predicateName.equals("PublishContent"))
						{
										
						if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductName"))!=null)
							_productName= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductName")).toString();
						if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemSize"))!=null)
							_itemSize= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemSize")).asLiteral().toString();
						if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemGender"))!=null)
							_itemGender= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ItemGender")).asLiteral().toString();
						if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductPrice"))!=null)
							_prodPrice= _obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductPrice")).asLiteral().getDouble();					
						if(_obj_1.getPropertyValue(pProductModel.getProperty(Constants.productNS + "ProductExpiryDate"))!=null)
							_expiryDate= _obj_1.getPropertyValue(pProductModel.createDatatypeProperty(Constants.productNS + "ProductExpiryDate")).asLiteral().toString();					 
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentURL"))!=null)
							 _publishedContentURL= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentURL")).toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentTopic"))!=null)
							 _publishedContentTopic= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentTopic")).toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "PublishedContentData"))!=null)
							 _publishedContentData= _obj_1.getPropertyValue(pEventModel.createDatatypeProperty(Constants.eventNS + "PublishedContentData")).toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemName"))!=null)
								_browsedItemName= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemName")).asLiteral().toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemCategory"))!=null)
								_browsedItemCategory= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemCategory")).asLiteral().toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL"))!=null)
								_browsedURL= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedURL")).asLiteral().toString();
						 if(_obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemID"))!=null)
								_browsedItemID= _obj_1.getPropertyValue(pEventModel.getProperty(Constants.eventNS + "BrowsedItemID")).asLiteral().toString();
						
						
						/* System.out.println("prodName="+_productName+",itemSize="+_itemSize+",itemGender="+_itemGender+",prodPrice="+_prodPrice+",expDate="+_expiryDate+",pubContURL="+
												_publishedContentURL+",pubContTopic="+_publishedContentTopic+",pubContData="+_publishedContentData+",BrowsedItemID="+_browsedItemID+
												",BrowsedURL="+_browsedURL+",BrowsedItemName="+_browsedItemName+",BrowsedItemCategory="+_browsedItemCategory);
												*/
						 
						//================convert to Intent model======	
						 updateIntentCandidateModel(_intentModel,_candidateIntentStatementList,_subjID_1);	
						}
				    }
				
												
				
			}
			
								
			
		}
		
		
		return _candidateIntentStatementList;
	}
	
	private void updateIntentCandidateModel(OntModel pModel,List pList,String pUserId)
	{
		DatatypeProperty _prodName, _prodType,_prodSize,_prodGender,_prodCost,_prodExpiry;
		String _activityName;
		//Individual _activity=null;
		OntClass _activity=null;
		OntProperty _interestProperty=null;
		RDFNode _nullIntent=pModel.createResource(Constants.intentNS+"BlankIntent");
								
		
		if(_statementBag==null)
			_statementBag=pModel.createBag(Constants.intentNS+"EventStatementBag");

		
		if(_predicateName.equals("Browse"))		
			 _activity= pModel.getOntClass(Constants.intentNS+"BrowseActivity");
		else if(_predicateName.equals("ViewVideo"))
			_activity= pModel.getOntClass(Constants.intentNS+"ViewActivity");
		else if(_predicateName.equals("ListenAudio"))
			_activity= pModel.getOntClass(Constants.intentNS+"ListenActivity");
		else if(_predicateName.equals("PublishContent"))
			_activity= pModel.getOntClass(Constants.intentNS+"PublishActivity");
		else
			_activity=null;
			
		if( _itemSize!=null)
		{
			if(_itemSize.equals("S") || _itemSize.equals("XS") ||_itemSize.equals("M")) 				
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestChildProduct");
			if(_itemSize.equals("L") || _itemSize.equals("XL"))				
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestAdultProduct");				
		}
		if(_browsedItemCategory!=null)
		{
			if(_browsedItemCategory.equals("GiftCertificate"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestProductGift");
			if(_browsedItemCategory.equals("VideoContent"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestVideoContent");
			if(_browsedItemCategory.equals("AudioContent"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestAudioContent");
			if(_browsedItemCategory.equals("PublishedContent"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestPublishedContent");
			
			if(_browsedItemCategory.equals("Seating"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestBoxSuite");
			if(_browsedItemCategory.equals("Ticketing"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestTicketing");
			if(_browsedItemCategory.equals("Catering"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestCatering");
			if(_browsedItemCategory.equals("GameSchedule"))
				_interestProperty = _intentModel.getOntProperty(Constants.intentNS + "InterestGameSchedule");
			
		}			
		
		//_interestProperty=_intentModel.getOntProperty(Constants.intentNS+"Interest");
		//_interestProperty=_intentModel.createOntProperty(Constants.intentNS+"Interest");
		if( _itemSize!=null)
		{
			_prodSize = _intentModel.createDatatypeProperty(Constants.intentNS+"InterestProductSize");
			_interestProperty.addProperty(_prodSize, _itemSize);
		}
		if(_browsedItemCategory!=null)
		{
			_prodType = _intentModel.createDatatypeProperty(Constants.intentNS+"InterestProductType");
			_interestProperty.addProperty(_prodType, _browsedItemCategory);
		}
		
		if(_productName!=null)
		{
			_prodName = _intentModel.getDatatypeProperty(Constants.intentNS+"InterestProductName");
			_interestProperty.addProperty(_prodName, _productName);
		}
		
		
		if(_itemGender!=null)
		{
			_prodGender = _intentModel.createDatatypeProperty(Constants.intentNS+"InterestProductGender");
			_interestProperty.addProperty(_prodGender, _itemGender);
		}
		if(_prodPrice!=0)
		{
			 _prodCost = _intentModel.createDatatypeProperty(Constants.intentNS+"InterestProductCost");		
			 _interestProperty.addLiteral(_prodCost, _prodPrice);
		}
		if(_expiryDate!=null)
		{
			_prodExpiry = _intentModel.createDatatypeProperty(Constants.intentNS+"InterestProductExpiryDate");
			 _interestProperty.addLiteral(_prodExpiry, _expiryDate);
		}
				
		//System.out.println("Item type:"+_interestProperty.getPropertyValue(pModel.getProperty(Constants.intentNS+"InterestProductType")));
		
		if(_activity!=null && _interestProperty!=null)
		{
			Statement _stmt= pModel.createStatement(_activity, _interestProperty,_nullIntent);	
			 InformationClusterDetector.getInstance().updateClassCounter( _activity, pModel);
			 InformationClusterDetector.getInstance().updatePropertyCounter(  _interestProperty, pModel);
		
			pList.add(_stmt);			
		}
		
		
	}
	
	public synchronized List getIntentList(List pCandidateStatements,OntModel pModel,String pReasonerType)
	{
			
		List _intentList_x = _intentInferenceHarness.runReasoner(pCandidateStatements,pReasonerType, pModel);	
		
		
		return _intentList_x;
	}
	public void closeBrowsingList()
	{
		 DatatypeProperty browsingProcessed = _personModel.getDatatypeProperty(Constants.personNS + "BrowsingProcessed");
		 DatatypeProperty _personID=_personModel.getDatatypeProperty(Constants.personNS + "ID");	
		 
		 
		 System.out.println("UserLIstsize:"+_userList.size());
		Iterator _it = _userList.iterator();
		while(_it.hasNext())
		{
			Individual _person = (Individual) _it.next();		
			String _user_id = _person.getPropertyValue(_personID).toString();
			System.out.println("Closing out user:"+_user_id);
			_person.setPropertyValue(browsingProcessed, _eventModel.createLiteral("true"));
			
			//ResourceUtils.renameResource(_person, Constants.personNS+_user_id+"-"+OntologyUtil.getInstance().getCounter());
			 DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd-HH:mm:ss");
			 Date date = new Date();
			 
			ResourceUtils.renameResource(_person, Constants.personNS+_user_id+"-"+dateFormat.format(date));
		}
	}

}
