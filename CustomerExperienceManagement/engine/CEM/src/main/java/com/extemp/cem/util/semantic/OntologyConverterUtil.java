package com.extemp.cem.util.semantic;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.extemp.cem.events.BrowseActivityEvent;
import com.extemp.cem.semantic.generator.EventOntologyBuilder;
import com.extemp.cem.semantic.generator.IOntologyBuilder;
import com.extemp.cem.semantic.generator.ModelGeneratorFactory;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.XSD;
//import com.tibco.be.functions.trax.util.XMLSerializer;
import com.extemp.cem.semantic.generator.EventOntologyBuilder;
import com.extemp.cem.semantic.vo.EventVO;
import com.extemp.cem.semantic.vo.IntentVO;
import com.extemp.cem.util.CEMUtil;

import org.xml.sax.InputSource;


public class OntologyConverterUtil 
{
	
	private static OntologyConverterUtil _instance=null;
	private static DocumentBuilder _xmlBuilder;
	private static DocumentBuilderFactory _xmlFactory;
	private OntModel _personModel,_productModel,_eventModel;
	private DatatypeProperty _personID;
	private int _cntr=0;
	private static String _xmlHeader="<?xml version = '1.0' encoding = 'UTF-8'?>";
	private static String _activityListNS="<ActivityEventList xmlns:xsi = 'http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation = '../XSD/ActivityEventSchema.xsd'>";
	private boolean _wroteBaseSchema=false;	
	
	public static  OntologyConverterUtil getInstance()
	{
		if (_instance==null)
		{
			_instance = new OntologyConverterUtil();
			_xmlFactory =  DocumentBuilderFactory.newInstance();
			try
			{
			 _xmlBuilder = _xmlFactory.newDocumentBuilder();
			}
			catch(Exception pExcp)
			{
				pExcp.printStackTrace();
			}
		}
		
		return _instance;
	}
	private void init()
	{
		 
	}
	
	public List getEventDocList(String pEventDocXML)
	{
		ArrayList _retDocList=new ArrayList();
		
		
		Document _document= convertStringToDocument(pEventDocXML);
		
		System.out.println("BaseURI:"+_document.getBaseURI());
		System.out.println("Prefix:"+_document.getPrefix());
		System.out.println("XmlEncoding:"+_document.getXmlEncoding());
		System.out.println("XlVersion:"+_document.getXmlVersion());
		
		
		NodeList nodeList = _document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) 
		{	
		      Node _node = nodeList.item(i);
		     
		      //System.out.println("Node:"+_node.getNodeName()+",node type:"+_node.getNodeType()+",value="+_node.getNodeValue());
		      if(_node.getNodeName().equals("ActivityEventList"))
		      {
		    	  System.out.println("Found ActivityEventList ");
		    	  
		    	    try {
					     Document _newDoc = _xmlBuilder.newDocument();  
					     
					     Node newNode = _newDoc.importNode(_node, true);
					     // Make the new node an actual item in the target document
					     //_newDoc.getDocumentElement().appendChild(newNode);    
					     _newDoc.appendChild(newNode);    
					     
					     String _eventListStr =  getStringFromDocument(_newDoc);				    	  
				    	//  System.out.println("ActivityeventList: " +_eventListStr );
				    	  _retDocList.add(_eventListStr);
					    
					      // Create from whole cloth
					     
					    } catch (Exception pce) {
					      // Parser with specified options can't be built
					    	pce.printStackTrace();
					    }
				
		    	  
		    	 
		    	    	  
		    	  
		      }		
		      
		}
		 		
		
		return _retDocList;
		
	}
	
	public String getXMLDocFromFile(String pFileName)
	{
		String _retDoc=null;
		if(pFileName!=null)
		{
			 
			 try
			 {
					
					// Document _document  = _xmlBuilder.parse(ClassLoader.getSystemResourceAsStream(pFileName));
					 Document _document  = _xmlBuilder.parse(new File(pFileName));
				 //_retDoc=_document.getTextContent();
					// _retDoc = _document..toString();	
					_retDoc=getStringFromDocument(_document);

			 }
			 catch(Exception pExcp)
			 {
				pExcp.printStackTrace();
			 }
		}
		
	
		return _retDoc;
		
	}
	
	public OntModel getOntEventModel(List pEventList,IOntologyBuilder pEventBuilder)
	{

		IOntologyBuilder _eventBuilder=null;
		ArrayList<String> _personMap = new ArrayList<String>();
		
		try
		{
			if(pEventBuilder==null)
				 _eventBuilder = ModelGeneratorFactory.getEventModel();	
			else
				_eventBuilder=pEventBuilder;
			
			_eventModel=_eventBuilder.buildBaseSchemaModel();				
			_personModel = ((EventOntologyBuilder) _eventBuilder).getPersonModel();
			_productModel = ((EventOntologyBuilder) _eventBuilder).getProductModel();
			//add xmls documents to the OntModel.
			 _eventBuilder.registerOntology(_eventModel);
			 if(! _wroteBaseSchema)
			 {
				 String _eventOntFile = CEMUtil.getInstance().getCEMProperty("cem.event.ontologoy.location");
				
				 OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), _eventOntFile, false,Constants.eventNS);
					
				 _wroteBaseSchema=true;
			 }
			
			Iterator _it = pEventList.iterator();
			while(_it.hasNext())
			{
				BrowseActivityEvent _event = (BrowseActivityEvent) _it.next();				
			    processActivityEvent2(_eventModel,_event, _personMap);
			   
				
				
			}
			
		
			 
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		

		return _eventModel;
		
		
	}
	public OntModel convertXMLToEventModel(String pEventXML,IOntologyBuilder pEventBuilder)
	{
		IOntologyBuilder _eventBuilder=null;
		ArrayList<String> _personMap = new ArrayList<String>();
		
		try
		{
			Document _document= convertStringToDocument(pEventXML);
			
			if(pEventBuilder==null)
				 _eventBuilder = ModelGeneratorFactory.getEventModel();	
			else
				_eventBuilder=pEventBuilder;
			
			_eventModel=_eventBuilder.buildBaseSchemaModel();				
			_personModel = ((EventOntologyBuilder) _eventBuilder).getPersonModel();
			_productModel = ((EventOntologyBuilder) _eventBuilder).getProductModel();
			//add xmls documents to the OntModel.
			 _eventBuilder.registerOntology(_eventModel);
			 if(! _wroteBaseSchema)
			 {
				 String _eventOntFile = CEMUtil.getInstance().getCEMProperty("cem.event.ontologoy.location");
				 //OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), Constants.eventOntFile, false,Constants.eventNS);
				 OntologyUtil.getInstance().printOntology(OntologyUtil.getInstance().getOntology(Constants.eventBaseURI), _eventOntFile, false,Constants.eventNS);
					
				 _wroteBaseSchema=true;
			 }
			
			NodeList nodeList = _document.getDocumentElement().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) 
			{
		
			      Node _node = nodeList.item(i);
			     
			      //System.out.println("Node:"+_node.getNodeName()+",node type:"+_node.getNodeType()+",value="+_node.getNodeValue());
			      if(_node.getNodeName().equals("ActivityEvent"))
			      {
			    	  processActivityEvent(_eventModel,_node, _personMap);
			      }		
			      
			}
			 
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		

		return _eventModel;
	}

	public String getStringFromDocument(Document doc)
	{
		String _dom=null;
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       _dom= writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();	       
	    }
	    return _dom;
	} 
	//====================================
	public String convertIntentStmtsToXML2(List pIntentStmtList)
	{
		 String _intentStr=null;
		 Element _rootElement=null;
		 Document _doc=null;
		 
		 try
			{
			 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			 DocumentBuilder builder = dbf.newDocumentBuilder();
			 _doc = builder.newDocument();
			 // create the root element node
			 _rootElement = _doc.createElement("IntentList2");
			 _doc.appendChild(_rootElement);
			 
			}
			catch(Exception pExcp)
			{
				pExcp.printStackTrace();
			}
			
			Iterator _it = pIntentStmtList.iterator();
			System.out.println("IntentList size:"+pIntentStmtList.size());
			while(_it.hasNext())
			{
				
				//Statement _stmt = (Statement) _it.next();
				IntentVO _vo = (IntentVO) _it.next();
				String _userID = _vo.getUserId();
				Element _userElement = _doc.createElement("User");
				Element _id = _doc.createElement("UserID");
				_id.appendChild(_doc.createTextNode(_userID)) ;
				_userElement.appendChild(_id);
				
				List _intentList = _vo.getStmtList();
				Iterator _stmtIt = _intentList.iterator();
				while(_stmtIt.hasNext())
				{
					Statement _stmt = (Statement) _stmtIt.next();
					String _intent = (_stmt.getObject().asResource()).getLocalName();
					System.out.println("Intent Discovered:"+_intent);
					Element _itemElement = _doc.createElement("Intent");
					Element _intentName = _doc.createElement("IntentName");
					_intentName.appendChild(_doc.createTextNode(_intent)) ;
					Element _intentId = _doc.createElement("IntentId");
					_intentId.appendChild(_doc.createTextNode("")) ;
					_itemElement.appendChild(_intentName);
					_itemElement.appendChild(_intentId);
					_userElement.appendChild(_itemElement);	
				}
				_rootElement.appendChild(_userElement);									

			}
							
			_intentStr = convertDocumentToString(_doc);
		 
		 
		 return _intentStr;
	}
	
	public String convertIntentStmtsToXML(List pIntentStmtList)
	{
		String _intentStr=null;
		 Element _rootElement=null;
		 Document _doc=null;
		
		try
		{
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = dbf.newDocumentBuilder();
		 _doc = builder.newDocument();
		 // create the root element node
		 _rootElement = _doc.createElement("IntentList");
		 _doc.appendChild(_rootElement);
		 
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		Iterator _it = pIntentStmtList.iterator();
		System.out.println("IntentList size:"+pIntentStmtList.size());
		while(_it.hasNext())
		{
			
			Statement _stmt = (Statement) _it.next();
			
			
			String _intent = (_stmt.getObject().asResource()).getLocalName();
			System.out.println("Intent Discovered:"+_intent);
			
			Element _itemElement = _doc.createElement("Intent");
			Element _intentName = _doc.createElement("IntentName");
			_intentName.appendChild(_doc.createTextNode(_intent)) ;
			Element _intentId = _doc.createElement("IntentId");
			_intentId.appendChild(_doc.createTextNode("")) ;
			
			_itemElement.appendChild(_intentName);
			_itemElement.appendChild(_intentId);
			_rootElement.appendChild(_itemElement);			

		}
						
		_intentStr = convertDocumentToString(_doc);
				
	return _intentStr;
		
	}
	
	 private  String convertDocumentToString(Document doc)
	 {
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer;
	        try {
	            transformer = tf.newTransformer();
	            // below code to remove XML declaration
	            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	            StringWriter writer = new StringWriter();
	            transformer.transform(new DOMSource(doc), new StreamResult(writer));
	            String output = writer.getBuffer().toString();
	            return output;
	        } catch (TransformerException e) {
	            e.printStackTrace();
	        }
	         
	        return null;
	    }
	  public static Document convertStringToDocument(String xmlStr) 
	  {
	       
	        try 
	        {  
	            _xmlBuilder = _xmlFactory.newDocumentBuilder();  
	            Document doc = _xmlBuilder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
	            return doc;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        return null;
	    }
	 private void processActivityEvent(OntModel pEventModel,Node pNode,List pPersonMap)
	 {
		 
		 String _activityName=null,_eventID=null,_userID=null,_activitEvent=null,_activityType=null,_browsedURL=null,_browsedItemName=null,_browsedItemCategory=null,_browsedItemID=null,_browsedItemSize=null,_browsedItemGender=null;
		 String _publishedContentURL=null,_publishedContentTopic=null ,_publishedContentCategory=null,_publishedContentData=null;
		 String _expiryDate=null;
		
		 double _browsedItemPrice=0;
		 OntClass _itemCls=null;
		 Individual _person=null;
		 RDFNode _nullObject=null;
		 String _item_id ;
		 
			
		 DatatypeProperty _personID=_personModel.getDatatypeProperty(Constants.personNS + "ID");				
		 OntClass _personCls = _personModel.getOntClass(Constants.personNS + "BrowsingPerson");	
		 DatatypeProperty browsingProcessed = _personModel.getDatatypeProperty(Constants.personNS + "BrowsingProcessed");
		 
		 DatatypeProperty browsedURL = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedURL");
		 DatatypeProperty browsedItemName = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemName");
		 DatatypeProperty browsedItemCategory = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemCategory");
		 DatatypeProperty browsedItemID = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemID");
		 DatatypeProperty publishedContentURL = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentURL");
		 DatatypeProperty publishedContentTopic = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentTopic");
		 DatatypeProperty publishedContentData = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentData");
		 DatatypeProperty publishedContentCategory = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentCategory");
		 DatatypeProperty timeStamp = _eventModel.getDatatypeProperty(Constants.eventNS + "TimeStamp");
					 
		 OntProperty browseProperty = _eventModel.getOntProperty(Constants.eventNS + "Browse");		
		 OntProperty viewVideoProperty = _eventModel.getOntProperty(Constants.eventNS + "ViewVideo");
		 OntProperty listenAudioProperty = _eventModel.getOntProperty(Constants.eventNS + "ListenAudio");
		 OntProperty tweetContent = _eventModel.getOntProperty(Constants.eventNS + "Tweet");
		 
		
		 DatatypeProperty _productName=_personModel.getDatatypeProperty(Constants.productNS + "ProductName");		
		DatatypeProperty _productBrand=_personModel.getDatatypeProperty(Constants.productNS + "ProductBrand");
		DatatypeProperty _productSerialNumber=_personModel.getDatatypeProperty(Constants.productNS + "ProductSerialNumber");
		DatatypeProperty _productMadeIn=_personModel.getDatatypeProperty(Constants.productNS + "ProductMadeIn");
		DatatypeProperty _itemSize=_personModel.getDatatypeProperty(Constants.productNS + "ItemSize");
		DatatypeProperty _itemGender=_personModel.getDatatypeProperty(Constants.productNS + "ItemGender");	
		DatatypeProperty _productPrice=_personModel.getDatatypeProperty(Constants.productNS + "ProductPrice");	
		DatatypeProperty _productExpiryDate=_personModel.getDatatypeProperty(Constants.productNS + "ProductExpiryDate");
	
		 
		// System.out.println("------"+pNode.getNodeName()+"----------");
		 System.out.println("---------------");
		 NodeList _kidList=pNode.getChildNodes();
		
		 for(int i=0;i<_kidList.getLength();i++)
		 {
			 Node _node = _kidList.item(i);		
			 
		//	System.out.println(_node.getNodeName()+":"+_node.getTextContent()+":"+ _node.getNodeType());	
			 
			 	if(_node.getNodeType()==Node.ELEMENT_NODE)
			 	{
			 
					 if(_node.getNodeName().equals(Constants.eventID))
						 _eventID=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.userID))
						 _userID=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.activityType))
						 _activityType=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.browsedURL))
						 _browsedURL=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.browsedItemID))
						 _browsedItemID=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.browsedItemName))
						 _browsedItemName=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.browsedItemCategory))
						 _browsedItemCategory=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.browsedItemPrice))
						 _browsedItemPrice=Double.parseDouble(_node.getTextContent());		
					 if(_node.getNodeName().equals(Constants.browsedItemSize))
						 _browsedItemSize=_node.getTextContent();		
					 if(_node.getNodeName().equals(Constants.browsedItemGender))
						 _browsedItemGender=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.publishedContentURL))
						 _publishedContentURL=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.publishedContentTopic))
						 _publishedContentTopic=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.publishedContentCategory))
						 _publishedContentCategory=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.publishedContentData))
						 _publishedContentData=_node.getTextContent();
					 if(_node.getNodeName().equals(Constants.expiryDate))
						 _expiryDate=_node.getTextContent();
			 
			 	}
		 	}
			
		
			System.out.println("eventID="+_eventID+",_userID="+_userID+",activityTYpe="+_activityType+",browsedURL="+_browsedURL+",browsedItemID="+_browsedItemID+",browsedItemName="+_browsedItemName+
					",browsedItemCateogry="+_browsedItemCategory+",itemPrice="+_browsedItemPrice+",itemGender="+_browsedItemGender+",publishedCOntentURL="+_publishedContentURL+
					",publishedCOntentTopic="+_publishedContentTopic+",_publishedContentCategory"+_publishedContentCategory+",publishedcontentData="+_publishedContentData);
					
			
			// String person_event_id = "event-"+_eventID+"-"+_userID;
			 
			 if(!pPersonMap.contains(_userID))
			 //if(!pPersonMap.contains(person_event_id))
			 {
				 	System.out.println("New User:"+_userID);
					 _person = _personCls.createIndividual(Constants.personNS+_userID);
					 //_person = _personCls.createIndividual(Constants.personNS+person_event_id);
					 _person.addProperty(_personID, _userID);	
					 pPersonMap.add(_userID);
					 //pPersonMap.add(person_event_id);
			 }
			 else
			 {
				// add to exiting user
				 System.out.println("Existing User:"+_userID);
				// System.out.println("Existing User:"+person_event_id);
				 _person = _personModel.getIndividual(Constants.personNS+_userID);
				 //_person = _personModel.getIndividual(Constants.personNS+person_event_id);
				  
				  //System.out.println("person:"+_person);
				  
				 
			 }
			 InformationClusterDetector.getInstance().updateClassCounter(_person.getOntClass(), pEventModel);
						
			   			
				if(_activityType.equals(Constants.browse))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "BrowsedContent");
				if(_activityType.equals(Constants.view))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "VideoContent");			
				if(_activityType.equals(Constants.listen))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "AudioContent");
				if(_activityType.equals(Constants.publish))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "PublishedContent");				
							
				if(_browsedItemName!=null)					
					_item_id = _browsedItemName+"-"+_eventID;
				else
				{
					if(_activityType.equals(Constants.publish))
						_item_id = "PUB-EVENT"+"-"+_eventID;
					else
						_item_id =_eventID;
						
				}
					
				Individual _item = _itemCls.createIndividual(Constants.eventNS+_item_id);		
				if(_browsedItemName!=null)
					_item.addProperty(_productName, _browsedItemName);		
				if(_browsedItemSize!=null)
					_item.addProperty(_itemSize, _browsedItemSize);
				if(_browsedItemGender!=null)
					_item.addProperty(_itemGender, _browsedItemGender);
				if(_browsedItemPrice!=0)
					_item.addLiteral(_productPrice, _browsedItemPrice);				
				if(_browsedURL!=null)
					_item.addProperty(browsedURL, _browsedURL);
				if(_browsedItemName!=null)
				_item.addProperty(browsedItemName, _browsedItemName);
				if(_browsedItemCategory!=null)
				_item.addProperty(browsedItemCategory, _browsedItemCategory);
				if(_browsedItemID!=null)
				  _item.addProperty(browsedItemID, _browsedItemID);		
				else
				{
					if(_activityType.equals(Constants.publish))	
						_item.addProperty(browsedItemID, "PUB-EVENT");	
				}
					 
					 
				if(_expiryDate!=null)
					_item.addProperty(_productExpiryDate, _eventModel.createTypedLiteral(_expiryDate, XSDDatatype.XSDdate));					
				if(_publishedContentURL!=null)
					_item.addProperty(publishedContentURL, _publishedContentURL);	
				if(_publishedContentTopic!=null)
					_item.addProperty(publishedContentTopic, _publishedContentTopic);	
				if(_publishedContentCategory!=null)
					_item.addProperty(publishedContentCategory, _publishedContentCategory);	
				if(_publishedContentData!=null)
					_item.addProperty(publishedContentData, _publishedContentData);	
								
					_item.addProperty(timeStamp, _eventModel.createTypedLiteral(new Date(), XSDDatatype.XSDdateTime));	
					
					 InformationClusterDetector.getInstance().updateClassCounter(_item.getOntClass(), pEventModel);
						
				
					 if(_activityType.equals(Constants.browse))
						{
							_person.addProperty(browseProperty,_item);			
							 InformationClusterDetector.getInstance().updatePropertyCounter(browseProperty, pEventModel);
						}
					    if(_activityType.equals(Constants.view))
					    {
					       	_person.addProperty(viewVideoProperty,_item);
					        InformationClusterDetector.getInstance().updatePropertyCounter(viewVideoProperty, pEventModel);
					    }
						if(_activityType.equals(Constants.listen))
						{
							_person.addProperty(listenAudioProperty,_item);
							 InformationClusterDetector.getInstance().updatePropertyCounter(listenAudioProperty, pEventModel);
						}
						if(_activityType.equals(Constants.publish))			
						{
							_person.addProperty(tweetContent,_item);	
							InformationClusterDetector.getInstance().updatePropertyCounter(tweetContent, pEventModel);
						}
				
				_person.addProperty(browsingProcessed, "false");
					
						
	 }
	 
	 private void processActivityEvent2(OntModel pEventModel,BrowseActivityEvent pEvent,List pPersonMap)
	 {
		 
		 String _activityName=null,_eventID=null,_userID=null,_activitEvent=null,_activityType=null,_browsedURL=null,_browsedItemName=null,_browsedItemCategory=null,_browsedItemID=null,_browsedItemSize=null,_browsedItemGender=null;
		 String _publishedContentURL=null,_publishedContentTopic=null ,_publishedContentCategory=null,_publishedContentData=null;
		 String _expiryDate=null;
		
		 double _browsedItemPrice=0;
		 OntClass _itemCls=null;
		 Individual _person=null;
		 RDFNode _nullObject=null;
		 String _item_id ;
		 
			
		 DatatypeProperty _personID=_personModel.getDatatypeProperty(Constants.personNS + "ID");				
		 OntClass _personCls = _personModel.getOntClass(Constants.personNS + "BrowsingPerson");	
		 DatatypeProperty browsingProcessed = _personModel.getDatatypeProperty(Constants.personNS + "BrowsingProcessed");
		 
		 DatatypeProperty browsedURL = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedURL");
		 DatatypeProperty browsedItemName = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemName");
		 DatatypeProperty browsedItemCategory = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemCategory");
		 DatatypeProperty browsedItemID = _eventModel.getDatatypeProperty(Constants.eventNS + "BrowsedItemID");
		 DatatypeProperty publishedContentURL = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentURL");
		 DatatypeProperty publishedContentTopic = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentTopic");
		 DatatypeProperty publishedContentData = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentData");
		 DatatypeProperty publishedContentCategory = _eventModel.getDatatypeProperty(Constants.eventNS + "PublishedContentCategory");
		 DatatypeProperty timeStamp = _eventModel.getDatatypeProperty(Constants.eventNS + "TimeStamp");
					 
		 OntProperty browseProperty = _eventModel.getOntProperty(Constants.eventNS + "Browse");		
		 OntProperty viewVideoProperty = _eventModel.getOntProperty(Constants.eventNS + "ViewVideo");
		 OntProperty listenAudioProperty = _eventModel.getOntProperty(Constants.eventNS + "ListenAudio");
		 OntProperty tweetContent = _eventModel.getOntProperty(Constants.eventNS + "Tweet");
		 
		
		 DatatypeProperty _productName=_personModel.getDatatypeProperty(Constants.productNS + "ProductName");		
		DatatypeProperty _productBrand=_personModel.getDatatypeProperty(Constants.productNS + "ProductBrand");
		DatatypeProperty _productSerialNumber=_personModel.getDatatypeProperty(Constants.productNS + "ProductSerialNumber");
		DatatypeProperty _productMadeIn=_personModel.getDatatypeProperty(Constants.productNS + "ProductMadeIn");
		DatatypeProperty _itemSize=_personModel.getDatatypeProperty(Constants.productNS + "ItemSize");
		DatatypeProperty _itemGender=_personModel.getDatatypeProperty(Constants.productNS + "ItemGender");	
		DatatypeProperty _productPrice=_personModel.getDatatypeProperty(Constants.productNS + "ProductPrice");	
		DatatypeProperty _productExpiryDate=_personModel.getDatatypeProperty(Constants.productNS + "ProductExpiryDate");
	
		 
		 System.out.println("---------------");
	 		_eventID=pEvent.getEventID();
			_userID=pEvent.getUserID();
			_activityType=pEvent.getActivityType();
			_browsedURL=pEvent.getBrowsedURL();
			_browsedItemID=pEvent.getBrowsedItemID();
			_browsedItemName=pEvent.getBrowsedItemName();
			_browsedItemCategory=pEvent.getBrowsedItemCategory();
			_browsedItemPrice=pEvent.getBrowsedItemPrice();		
			_browsedItemSize=pEvent.getBrowsedItemSize();		
			_browsedItemGender=pEvent.getBrowsedItemGender();
			 _publishedContentURL=pEvent.getPublishedContentURL();
			 _publishedContentTopic=pEvent.getPublishedContentTopic();
			 _publishedContentCategory=pEvent.getPublishedContentCategory();
			 _publishedContentData=pEvent.getPublishedContentData();
			 _expiryDate=pEvent.getExpiryDate();
					
		
			System.out.println("eventID="+_eventID+",_userID="+_userID+",activityTYpe="+_activityType+",browsedURL="+_browsedURL+",browsedItemID="+_browsedItemID+",browsedItemName="+_browsedItemName+
					",browsedItemCateogry="+_browsedItemCategory+",itemPrice="+_browsedItemPrice+",itemGender="+_browsedItemGender+",publishedCOntentURL="+_publishedContentURL+
					",publishedCOntentTopic="+_publishedContentTopic+",_publishedContentCategory"+_publishedContentCategory+",publishedcontentData="+_publishedContentData);
					
			
			// String person_event_id = "event-"+_eventID+"-"+_userID;
			 
			 if(!pPersonMap.contains(_userID))
			 //if(!pPersonMap.contains(person_event_id))
			 {
				 	System.out.println("New User:"+_userID);
					 _person = _personCls.createIndividual(Constants.personNS+_userID);
					 //_person = _personCls.createIndividual(Constants.personNS+person_event_id);
					 _person.addProperty(_personID, _userID);	
					 pPersonMap.add(_userID);
					 //pPersonMap.add(person_event_id);
			 }
			 else
			 {
				// add to exiting user
				 System.out.println("Existing User:"+_userID);
				// System.out.println("Existing User:"+person_event_id);
				 _person = _personModel.getIndividual(Constants.personNS+_userID);
				 //_person = _personModel.getIndividual(Constants.personNS+person_event_id);
				  
				  //System.out.println("person:"+_person);
				  
				 
			 }
			 InformationClusterDetector.getInstance().updateClassCounter(_person.getOntClass(), pEventModel);
						
			   			
				if(_activityType.equals(Constants.browse))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "BrowsedContent");
				if(_activityType.equals(Constants.view))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "VideoContent");			
				if(_activityType.equals(Constants.listen))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "AudioContent");
				if(_activityType.equals(Constants.publish))
					_itemCls = _eventModel.getOntClass(Constants.eventNS + "PublishedContent");				
							
				if(_browsedItemName!=null)					
					_item_id = _browsedItemName+"-"+_eventID;
				else
				{
					if(_activityType.equals(Constants.publish))
						_item_id = "PUB-EVENT"+"-"+_eventID;
					else
						_item_id =_eventID;
						
				}
					
				Individual _item = _itemCls.createIndividual(Constants.eventNS+_item_id);		
				if(_browsedItemName!=null)
					_item.addProperty(_productName, _browsedItemName);		
				if(_browsedItemSize!=null)
					_item.addProperty(_itemSize, _browsedItemSize);
				if(_browsedItemGender!=null)
					_item.addProperty(_itemGender, _browsedItemGender);
				if(_browsedItemPrice!=0)
					_item.addLiteral(_productPrice, _browsedItemPrice);				
				if(_browsedURL!=null)
					_item.addProperty(browsedURL, _browsedURL);
				if(_browsedItemName!=null)
				_item.addProperty(browsedItemName, _browsedItemName);
				if(_browsedItemCategory!=null)
				_item.addProperty(browsedItemCategory, _browsedItemCategory);
				if(_browsedItemID!=null)
				  _item.addProperty(browsedItemID, _browsedItemID);		
				else
				{
					if(_activityType.equals(Constants.publish))	
						_item.addProperty(browsedItemID, "PUB-EVENT");	
				}
					 
					 
				if(_expiryDate!=null)
					_item.addProperty(_productExpiryDate, _eventModel.createTypedLiteral(_expiryDate, XSDDatatype.XSDdate));					
				if(_publishedContentURL!=null)
					_item.addProperty(publishedContentURL, _publishedContentURL);	
				if(_publishedContentTopic!=null)
					_item.addProperty(publishedContentTopic, _publishedContentTopic);	
				if(_publishedContentCategory!=null)
					_item.addProperty(publishedContentCategory, _publishedContentCategory);	
				if(_publishedContentData!=null)
					_item.addProperty(publishedContentData, _publishedContentData);	
								
					_item.addProperty(timeStamp, _eventModel.createTypedLiteral(new Date(), XSDDatatype.XSDdateTime));	
					
					 InformationClusterDetector.getInstance().updateClassCounter(_item.getOntClass(), pEventModel);
						
				
					 if(_activityType.equals(Constants.browse))
						{
							_person.addProperty(browseProperty,_item);			
							 InformationClusterDetector.getInstance().updatePropertyCounter(browseProperty, pEventModel);
						}
					    if(_activityType.equals(Constants.view))
					    {
					       	_person.addProperty(viewVideoProperty,_item);
					        InformationClusterDetector.getInstance().updatePropertyCounter(viewVideoProperty, pEventModel);
					    }
						if(_activityType.equals(Constants.listen))
						{
							_person.addProperty(listenAudioProperty,_item);
							 InformationClusterDetector.getInstance().updatePropertyCounter(listenAudioProperty, pEventModel);
						}
						if(_activityType.equals(Constants.publish))			
						{
							_person.addProperty(tweetContent,_item);	
							InformationClusterDetector.getInstance().updatePropertyCounter(tweetContent, pEventModel);
						}
				
				_person.addProperty(browsingProcessed, "false");
					
						
	 }


}


