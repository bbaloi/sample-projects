package com.tibco.xpdl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import com.tibco.xmlns.bw.process.x2003.XslValueOf;
import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings;
//import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars.Choose.Otherwise;
//import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars.Choose.When;
//import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars.Choose;
import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars;
import com.tibco.xmlns.bw.process.x2003.ReturnBindingsDocument.ReturnBindings;
import com.tibco.xmlns.bw.process.x2003.ReturnBindingsDocument.ReturnBindings.ReturnValue;
import com.tibco.xpdl.bw.vo.InputBindingVO;
import com.tibco.xpdl.bw.vo.TransVO;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
//import org.apache.xmlbeans.impl.soap.Node;
import org.w3.x1999.xsl.transform.ChooseDocument.Choose;
import org.w3.x1999.xsl.transform.OtherwiseDocument.Otherwise;
import org.w3.x1999.xsl.transform.ValueOfDocument.ValueOf;
import org.w3.x1999.xsl.transform.WhenDocument.When;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wfmc.x2002.xpdl10.ExtendedAttributeDocument.ExtendedAttribute;

import com.tibco.xpdl.bw.IFramework;
import com.tibco.xpdl.bw.vo.ExtAttrValsVO;

public abstract class BWAbstractAssembler 
{
	protected IBWBuilderClient lBuilderClient;
	public HashMap lTransitionMap=null;
	public HashMap lActivityInputOutputMap=null;
	public boolean generateFramework=false;
	public HashMap lConditionMap;
	public HashMap lProcMap;
	public HashMap lActivityMap=null;
	public HashMap lUnresolvedBindingsMap=null;
	public HashMap lBindingsMap=null;
	public HashMap lActivityIdOutputMap=null;
	public HashMap lActivityIdInputMap=null;
	public ArrayList lTransitionList;
	private static int lOutputVarCntr=0;
	protected boolean newProc=false;

	
	private static Logger sLogger = Logger.getLogger( "com.tibco.xpdl.controller.BWAbstractAssembler");
	
	public BWAbstractAssembler(IBWBuilderClient pClient)
	{
		lBuilderClient = pClient;
		init();
		
	}
	public void generateFramework()
	{
		newProc=false;
		generateFramework=true;
		createProcDefintion();
		createJMSReceiver();
		createProcCall();
		createEndPoint();
		addTransitions();
		writeToFile();
	}
	protected void init()
	{
		newProc=true;
		lTransitionMap = new HashMap();
		lActivityInputOutputMap=new HashMap();
		lConditionMap=new HashMap();
		lProcMap=new HashMap();
		lActivityMap =new HashMap();
		lUnresolvedBindingsMap=new HashMap();
		lBindingsMap=new HashMap();	
		lTransitionList=new ArrayList();
		lActivityIdOutputMap=new HashMap();
		lActivityIdInputMap=new HashMap();
		generateFramework=false;			
	}
	protected void reset()
	{
		lTransitionMap.clear();
		lActivityInputOutputMap.clear();
		lConditionMap.clear();
		lProcMap.clear();
		lActivityMap.clear();
		lUnresolvedBindingsMap.clear();
		lBindingsMap.clear();
		lTransitionList.clear();
		lActivityIdOutputMap.clear();
		lActivityIdInputMap.clear();
		newProc=true;
	}
	protected ExtAttrValsVO getExtensions(ExtendedAttribute pAttr)
	{
		ExtAttrValsVO extAttr=null;
		
		/*
		String namespace="declare namespace ext='http://www.tibco.com/xpd/XpdlExtensions1.0.0';";
		//String query="$this//ext:extensions:XOffset";
		String query="$this//ext:extensions";
		XmlObject [] resultSet=pAttr.execQuery(namespace+query);
		sLogger.debug("------getting extensions------");
		for(int i=0;i<resultSet.length;i++)
		{
			XmlObject obj = resultSet[i];
			sLogger.debug("item "+i+":"+obj.toString());
		}*/			
				
		Node p_node=pAttr.getDomNode();
		sLogger.debug("$$getting ExtAttrValues for:"+p_node);	
		NodeList nodeList=p_node.getChildNodes();
		int listLen=nodeList.getLength();
		sLogger.debug("$$num children:"+listLen);
		
		if(listLen>1)
			extAttr=_getExtAttrs(1,nodeList);
		else
			extAttr=_getExtAttrs(0,nodeList);
			
		
		return extAttr;
		
	}
	private ExtAttrValsVO _getExtAttrs(int pStartNum,NodeList pNodeList)
	{
		ExtAttrValsVO extAttr=null;

	    int listLen=pNodeList.getLength();
		for(int i=pStartNum;i<listLen;i=i+2)
		{			
			Node _node=pNodeList.item(i);
				
			String nodeName=_node.getLocalName();	
			//sLogger.debug("NodeValue:"+nodeName);
			if(nodeName.equals("Activity"))
			{
				NodeList activityNodeList=_node.getChildNodes();
				extAttr=new ExtAttrValsVO();
			
				for(int x=pStartNum;x<activityNodeList.getLength();x=x+2)
				{
					Node activityNode=activityNodeList.item(x);					
					String actNodeName=activityNode.getLocalName();
					//sLogger.debug("Node-full:"+activityNode.getNodeName());
					sLogger.debug("Node:"+activityNode.getLocalName());
					//sLogger.debug("Node-children:"+activityNode.getChildNodes().getLength());
					//sLogger.debug("Node-nextChild:"+activityNode.getFirstChild().getNodeName());
					//sLogger.debug("Node-root:"+activityNode.getParentNode().getNodeName());
					if(actNodeName.equals("ActivityType"))
					{
						Node aValueNode=activityNode.getFirstChild();
						sLogger.debug("ExtAttr-ActivityType:"+aValueNode.getNodeValue());
						extAttr.setActivityType(aValueNode.getNodeValue());
					}
					if(actNodeName.equals("EventFlowType"))
					{
						Node eValueNode=activityNode.getFirstChild();	
						sLogger.debug("ExtAttr-EventFlowType:"+eValueNode.getNodeValue());
						extAttr.setEventFlowType(eValueNode.getNodeValue());
							
					}
					if(actNodeName.equals("Location"))
					{
						NodeList coordsList=activityNode.getChildNodes();
						sLogger.debug("Location len:"+coordsList.getLength());
						for(int y=1;y<coordsList.getLength();y=y+2)						
						{
							sLogger.debug("cntr:"+y);
							Node coordNode=coordsList.item(y);
							String coordNodeName=coordNode.getLocalName();
							if(coordNodeName.equals("XOffset"))
							{
								
								Node valueNode=coordNode.getFirstChild();
								sLogger.debug("ExtAttr-X:"+valueNode.getNodeValue());
								extAttr.setXOffset(valueNode.getNodeValue());					
								
							}
							if(coordNodeName.equals("YOffset"))
							{
								Node valueNode=coordNode.getFirstChild();							
								sLogger.debug("ExtAttr-Y:"+valueNode.getNodeValue());
								extAttr.setYOffset(valueNode.getNodeValue());		
								
							}
							
						}
					}					
					
				}
			}			
		}		
		return extAttr;
		
	}
	protected String getOutputStructure(String pOutputVarValues)
	{
		String retVal=new String();
		String tmpVal;
		ArrayList list=null;
		
		if(pOutputVarValues.contains(new StringBuffer("~")))
		{
			tmpVal=pOutputVarValues.substring(0,pOutputVarValues.indexOf("~"));
		}
		else tmpVal=pOutputVarValues;
		
		StringTokenizer tokenizer= new StringTokenizer(tmpVal,".");
		sLogger.debug("Formatting dest addr:"+tmpVal);
		if(tokenizer.countTokens()>1)
		{
			list = new ArrayList();
			while(tokenizer.hasMoreTokens())
			{
				list.add(tokenizer.nextToken());
			}			
			int i=0;
			while(i<list.size())
			{
				retVal+=(String)list.get(i);
				if(i<list.size()-1)
					retVal+="/";
				i++;
			}
			
			
			
		}
		else retVal=pOutputVarValues;
		
		return retVal;
	}
	protected void listInOutEntriesUtil()
	{
		Set keySet=(Set)lActivityInputOutputMap.keySet();		
		Iterator it=keySet.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			String value= (String) lActivityInputOutputMap.get(key);
			sLogger.debug("@@@key:"+key+",value:"+value);
		}
		
	}
	protected void listInOutIdEntriesUtil()
	{
		Set keySet=(Set)lActivityIdOutputMap.keySet();		
		Iterator it=keySet.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			String value= (String) lActivityIdOutputMap.get(key);
			sLogger.debug("@@@key:"+key+",value:"+value);
		}
		
	}
	
	public void adjustBindings()
	{
		//listInOutEntriesUtil();
		Set keySet=(Set)lUnresolvedBindingsMap.keySet();
		Iterator it=keySet.iterator();
		while(it.hasNext())
		{
			String key=(String) it.next();
			XmlAnySimpleType valBind = (XmlAnySimpleType)lUnresolvedBindingsMap.get(key);
			String val=(String)lActivityInputOutputMap.get(key);
			if(val==null)
				sLogger.debug("not found value yet for:"+key);
			else
			{
				valBind.setStringValue(val);
			}
		}
		//-----add choose elements------------
		//setChooseElements();	
				
	}
	public void adjustAllBindings()
	{
		//listInOutIdEntriesUtil();
		adjustConfluenceBindings();
	}
	protected void adjustConfluenceBindings()
	{
		ArrayList fromList=new ArrayList();		
		HashMap transHdld=new HashMap();
		
		int listLen=lTransitionList.size();	
		sLogger.debug("setChoseElements; transitions list size:"+listLen);
		
		for(int x=0;x<listLen;x++)
		{
			fromList.clear();
			TransVO trans=(TransVO)lTransitionList.get(x);
			String to=trans.getToId();
			String from=trans.getFromId();
			fromList.add(from);
			
			if(!transHdld.containsKey(to))
			{
				sLogger.debug("Handling:"+to);
				for(int i=0;i<listLen;i++)
				{
					TransVO _vo=(TransVO)lTransitionList.get(i);
					String _to = _vo.getToId();
					if(_to.equals(to) && !from.equals(_vo.getFromId()))
					{
						fromList.add(_vo.getFromId());
					}					
				}
				transHdld.put(to, to);
				constructBindings(to,fromList,transHdld);
			}
			else
				sLogger.debug("Already handled:"+to);
			//I now have  alist of Froms for a given To
						
			
		}
		
	}
	private void constructBindings(String pTo,ArrayList pFromList,HashMap pTransHdld)
	{
		InputBindings inputBindings=null;
		ReturnBindings retBindings=null;
		
		sLogger.debug("Constructing bindings for:activity "+pTo+"with # inputs:"+pFromList.size());
		try
		{
			inputBindings= (InputBindings) lBindingsMap.get(pTo);
		}
		catch(java.lang.ClassCastException pExcp)
		{
			sLogger.debug("This is the endpoint !");
			retBindings= (ReturnBindings) lBindingsMap.get(pTo);			
		}
		
		if(retBindings==null && inputBindings!=null)
		{
			if(pFromList.size()>1)
			{
			
					InputVars inputVars=inputBindings.getInputVars();
					if(inputVars!=null)			
					{
						if(inputVars.isSetDynamicVar())
						{
							sLogger.debug("dynamicVars already exists, removing!");
							XslValueOf dynVar=inputVars.getDynamicVar();	
							//dynVar.setNil();
							XmlCursor cursor= dynVar.newCursor();
							cursor.removeXml();
						}
					}
					else
						inputVars=inputBindings.addNewInputVars();			
				
						Choose choose=inputVars.addNewChoose();
			
					int listLen=pFromList.size();
					for(int i=0;i<listLen;i++)
					{
						String fromId=(String)pFromList.get(i);
						String outVal=(String)lActivityIdOutputMap.get(fromId);
						if(i<listLen-1)
						{
							//and when					
							When when=choose.addNewWhen();
							XmlAnySimpleType test=when.addNewTest();
							test.setStringValue("exists("+outVal+")");					
							XslValueOf _whenValueOf=when.addNewDynamicVar();
							ValueOf _whenValue=_whenValueOf.addNewValueOf();					
							XmlAnySimpleType _whenValType=_whenValue.addNewSelect();
							_whenValType.setStringValue(outVal);
						}
						else
						{
							//add otherwise
							Otherwise otherwise=choose.addNewOtherwise();
							XslValueOf _otherValueOf=otherwise.addNewDynamicVar();
							ValueOf _otherValue=_otherValueOf.addNewValueOf();
							XmlAnySimpleType _otherValType=_otherValue.addNewSelect();
							_otherValType.setStringValue(outVal);					
						}						
					}//end for listLen
				}//end if listlen>1
				else
				{
				
					//single value
					//InputVars inputVars=bindings.getInputVars();		
					InputVars inputVars=inputBindings.getInputVars();
					XslValueOf dynVar;
						String fromId=(String)pFromList.get(0);
						//String inputVarValue=(String)lActivityIdOutputMap.get(fromId);
						String inputVarValue=getInputVarPath(pTo,fromId);
						sLogger.debug("Adding input to activity "+pTo+" from activity "+fromId);
						if(inputVarValue !=null)
						{
							if(inputVars.isSetDynamicVar())
							{
								sLogger.debug("dynamicVars already exists,re-using");
								dynVar = inputVars.getDynamicVar();
							}
							else						
							dynVar=inputVars.addNewDynamicVar();				
							ValueOf _dynVar = dynVar.addNewValueOf();
							XmlAnySimpleType dynamicValue=_dynVar.addNewSelect();				
							
								//String inputVarValue = (String)lActivityInputOutputMap.get(pInputVarValue);
								if(inputVarValue!=null)
									dynamicValue.setStringValue(inputVarValue);		
								else
								{
									sLogger.debug("didn't find output for activity:"+fromId);
								}
						}		
					}			
			}//if retBindings==null
			else
			{
//					return Bindings			
				ReturnValue retVal=retBindings.addNewReturnValue();		
				XslValueOf endElemData=retVal.addNewOutData();		
				ValueOf val=endElemData.addNewValueOf();
				XmlAnySimpleType select=val.addNewSelect();
				String fromId=(String)pFromList.get(0);
				//sLogger.debug("Ret Binding input Proc:"+fromId);
				//String endInputVarValue = (String)lActivityIdOutputMap.get(fromId);
				String endInputVarValue=getInputVarPath(pTo,fromId);				
				sLogger.debug("Ret Binding input Proc:"+fromId+":"+endInputVarValue);
				if(endInputVarValue!=null)
					select.setStringValue(endInputVarValue);	
				else
				{
						sLogger.debug("COuldn't find value for Endpoint !");
					
				}
			
			}		
			
		}
		
		protected void addOutputVar(String pOutputVar,String pOutputVarValue)
		{
			String tmpVar = (String)lActivityInputOutputMap.get(pOutputVar);
			if(tmpVar==null)			
				lActivityInputOutputMap.put(pOutputVar, pOutputVarValue);
			else
			{
				lOutputVarCntr++;
				lActivityInputOutputMap.put(pOutputVar+"~"+lOutputVarCntr, pOutputVarValue);
			}			
		}
		
		private String getInputVarPath(String pTo,String pFrom)
		{
			String retVal=null,inputVarPath=null;
			boolean foundMatch=false;
			InputBindingVO bindingVO=new InputBindingVO();
			
			String fromPath=(String) lActivityIdOutputMap.get(pFrom);
			String inVar=(String)lActivityIdInputMap.get(pTo);
			sLogger.debug("from path:"+fromPath+",inVar:"+inVar);
			
			if(inVar!=null)
			{
				List candidateInputVarPathList = getCandidateInputVars(inVar);
				Iterator it=candidateInputVarPathList.iterator();
	//			if fromPath==null then use the input var path (for conditions)
				if(fromPath==null)
				{
					retVal=(String)it.next();
					sLogger.debug("Null value for input path, chosing inputva:"+retVal);
				}
				else
				{
					while(it.hasNext())
					{
						inputVarPath=(String) it.next();
						if(inputVarPath.equals(fromPath))
						{
							foundMatch=true;
							break;					
						}
					}				
					//if fromPath=varPath chose fromPath
					if(foundMatch)
					{
						retVal=fromPath;
						
					}
					else
					{
					//if fromPath !=varPath then chose varPath	
					 retVal=(String)candidateInputVarPathList.get(0);
					}
				}
				
			}			
			
			return retVal;			
		}
		private List getCandidateInputVars(String pVar)
		{
			//sLogger.debug("InputVar:"+pVar);
			ArrayList list=new ArrayList();
			
			Set keySet=lActivityInputOutputMap.keySet();
			Iterator it=keySet.iterator();
			while(it.hasNext())
			{
				String key=(String)it.next();
				String path=(String)lActivityInputOutputMap.get(key);
				if(pVar.equals(pruneVar(key)))
					list.add(path);
			}
			sLogger.debug("candidateVarList:"+list.size());
			
			return list;
		}
		private String pruneVar(String pVar)
		{
			String retVal=null;
			if(pVar.contains("~"))
			{
				retVal=pVar.substring(0,pVar.indexOf("~"));
			}
			else retVal=pVar;
			//sLogger.debug("prune "+pVar+" to "+retVal);
			return retVal;
		}
		
	
	protected abstract void createJMSReceiver();
	protected abstract void createProcDefintion();
	protected abstract void createProcCall();
	protected abstract void writeToFile();
	protected abstract void addTransitions();
	protected abstract void createEndPoint();
	
}



