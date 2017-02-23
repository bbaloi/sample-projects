package com.extemp.semantic.be;

import org.w3c.dom.Document;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.extemp.semantic.control.EngageMain;
import com.extemp.semantic.control.IIntentDetector;
import com.extemp.semantic.util.OntologyConverterUtil;
import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.event.EventPayload;
import com.tibco.cep.runtime.model.event.SimpleEvent;
import com.tibco.cep.runtime.session.RuleServiceProvider;
import com.tibco.cep.runtime.session.RuleServiceProviderManager;
import com.tibco.cep.runtime.session.RuleSession;
import com.tibco.cep.runtime.session.RuleSessionManager;
import com.tibco.cep.runtime.model.TypeManager;
import com.tibco.xml.data.primitive.ExpandedName;

public class BEInterface 
{
	
	private static EngageMain _mainController=null;
	private static String _intentListEvent = "/Events/IntentList";
	private static String _intentListConcept="/Concepts/IntentList";
	private static String _intentConcept = "/Concepts/Intent";
	private static String _userConcept = "/Concepts/User";

	
	public  static Object getIntentsFromEvents(SimpleEvent pEventList)
	{
		String _intentList= null;
		SimpleEvent _eventIntentList=null;
		Concept _intentListConcept=null;

		if(_mainController==null)
			_mainController = new EngageMain();
		try
		{
			final RuleSession ruleSession = RuleSessionManager.getCurrentRuleSession();
			
			String _payload = pEventList.getPayloadAsString();
			//System.out.println("We have payload:"+ _payload);

		  _intentList = _mainController.getIntentFromEvents2(_payload);
		  
		  System.out.println("Got Intent List - "+_intentList);
		  
		// convert XML sString to IntentEventList
		  _intentListConcept = createIntentListConcept(_intentList);
		  
		  
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}


		return _intentListConcept;

	}
		public static SimpleEvent getUser(String pUserID)
		{
			String _serializedUser=null;
			SimpleEvent _userProfile=null;


			//get user from Ontology engine Person Model

			//return _serializedUser;
			return _userProfile;
		}
		
		
		private static Concept createIntentListConcept(String pIntentList)
		{
			 Concept _retConcept=null,_intent=null,_user=null,_intentList=null;
			 String _userID=null;
			
			
				RuleServiceProviderManager rMgr = RuleServiceProviderManager.getInstance();
				RuleServiceProvider rProvider = rMgr.getDefaultProvider();
				TypeManager _typeMgr = rProvider.getTypeManager();			
				
				try 
				{
					_retConcept = (Concept) _typeMgr.createEntity(_intentListConcept);	
					Document _doc = OntologyConverterUtil.getInstance().convertStringToDocument(pIntentList); 
					
					//System.out.println(_doc.getNodeName()+","+_doc.getLocalName()+","+_doc.getFirstChild().getNodeName());
					
					NodeList _nodeList = _doc.getFirstChild().getChildNodes();
					for (int i = 0; i < _nodeList.getLength(); i++) 
					{	
					      Node _node = _nodeList.item(i);
					     // System.out.println("Node:"+_node.getNodeName()+",type:"+_node.ge+",value="+_node.getTextContent());
					      
					      if(_node.getNodeType()==Node.ELEMENT_NODE)
					      {
					    	 				    	  
						      if(_node.getNodeName().equals("User"))
						      {
						    	 // System.out.println("Found user:");
						    	  _user = (Concept) _typeMgr.createEntity(_userConcept);
						    	  
						    	  NodeList _userNodeList = _node.getChildNodes();					    	  
						    	 
								for(int x=0;x<_userNodeList.getLength();x++)
						    	  {									
									Node _userNode = _userNodeList.item(x);
									//System.out.println("Node name:"+_userNode.getNodeName());
									
									 if(_userNode.getNodeName().equals("UserID"))
									 {
										 	 _userID=_node.getFirstChild().getTextContent();	 								
										 	 _user.getPropertyAtom("UserID").setValue(_userID);
										 	 System.out.println("--concept userid:"+_user.getPropertyValue("UserID"));
									 }			 
									 
									 
									 if(_userNode.getNodeName().equals("Intent"))
									 {
										 System.out.println("Found Intent:");
										 
										 String _intentName = _userNode.getFirstChild().getTextContent();
										 String _intentID = _userNode.getLastChild().getTextContent();
										 
										 _intent = (Concept)_typeMgr.createEntity(_intentConcept);
										 
										 _intent.getPropertyAtom("IntentName").setValue(_intentName);
										 _intent.getPropertyAtom("IntentID").setValue(_intentID);
										 _user.getPropertyArray("intent_list").add(_intent);
										 
										 System.out.println("---IntentName: "+_intent.getPropertyValue("IntentName"));									 
										
									 }
						    	  }
						    	 
						    	_retConcept.getPropertyArray("user_list").add(_user);
						      }
						      
					      }
					      
					      
					}
					
		          					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return _retConcept;
			}
}
