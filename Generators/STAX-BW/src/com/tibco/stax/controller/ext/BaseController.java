package com.tibco.stax.controller.ext;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.Characters;

import com.bea.xml.stream.util.ElementTypeNames;
import com.tibco.stax.handler.BaseHandler;
import com.tibco.stax.handler.ext.IHandler;
import com.tibco.stax.action.ext.IAction;
import com.tibco.stax.utils.XMLContextBuilder;
import com.tibco.stax.utils.Constants;

import java.util.Stack;
import com.tibco.stax.utils.PropertiesLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.util.StringTokenizer;
import java.lang.reflect.InvocationTargetException;



public class BaseController 
{
	protected XMLEventReader lReader;
	private HashMap lNodeHandlerMap;
	//private HashMap lNodeActionMap;
	//private HashMap lActionMap;
	protected int bodyCntr=0;	
	//element & handler stack
	protected XMLContextBuilder lElementContextBuilder;
	protected Stack lHandlerStack;
	protected IAction defaultAction;	
	protected Collection handlerList;
	protected Set handlerSet;
	protected IHandler current_handler=null;
	protected boolean requestedElementOnStart=false;
	protected boolean requestedElementOnEnd=false;
	protected String XMLHeader=null;
	protected long curCounter=0,fileCounter=0;
	protected PropertiesLoader propLoader;
	protected String lInputFileName;
	
	
	public BaseController(String pFileName)
	{
		lInputFileName=pFileName;
		init();
	}
	private void init()
	{
		propLoader=PropertiesLoader.getInstance();
		initNodeHandlerMap(propLoader.getNodeHandlerMap(),propLoader.getNodeActionMap(),propLoader.getActionPropsMap(),propLoader.getFileCntr());				
	}
	
	public void registerReader(XMLEventReader pReader)
	{
		lReader=pReader;
	}
	public void handleEvent(XMLEvent pEvent)
	{
		boolean includeBody;
		//System.out.println("Handling Event:"+pEvent);
		switch (pEvent.getEventType()) 
		{
			case XMLStreamConstants.START_ELEMENT:	
				selectCurElement((StartElement)pEvent);
				//System.out.println("---StartElement:"+pEvent+", hdl:"+current_handler);
				if(current_handler!=null && requestedElementOnStart)
				{
					 includeBody=current_handler.notifyHandler(Constants.START_ELEMENT,curCounter,fileCounter);
					 appendEvent(pEvent);	
					 if(!includeBody)
					 {
						 current_handler.createEndElement();
						 current_handler=null;
					}					 				
				}
				else appendEvent(pEvent);
				
			 break;
			case XMLStreamConstants.END_ELEMENT:
				appendEvent(pEvent);		
				if(endRequestedNode((EndElement)pEvent) && requestedElementOnEnd)	
				{
					//System.out.println("+++End Element:"+pEvent+", hdl:"+current_handler);
				       	current_handler.incInstance();					
				    	current_handler.notifyHandler(Constants.END_ELEMENT,curCounter,fileCounter);
		  	
				}
			break;
			case XMLStreamConstants.SPACE:
				break;
			case XMLStreamConstants.CHARACTERS:
			  //System.out.println(((Characters)pEvent).getData());
			  appendEvent(pEvent);
			  break;
			case XMLStreamConstants.ATTRIBUTE:				
				 appendEvent(pEvent);				
				break;
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
			break;
			case XMLStreamConstants.CDATA:
			    appendEvent(pEvent);
			  break;			
			case XMLStreamConstants.COMMENT:
			break;
			case XMLStreamConstants.ENTITY_REFERENCE:
			break;
			case XMLStreamConstants.START_DOCUMENT:
			{
				//set the event on all Handlers				
				Iterator it = handlerList.iterator();
				while(it.hasNext())
				{
					IHandler hdl = (IHandler)it.next();
					hdl.setXMLHeader(pEvent.toString());					
				}
			}
			  break;
			case XMLStreamConstants.END_DOCUMENT:
				//System.out.println("Got to the END of the Document");
				cleanup();
				break;
			
			}			
		}
	public int getBodyRecords()
	{
		int cntr=0;
		Iterator it = handlerList.iterator();
		while(it.hasNext())
		{
			IHandler hdl = (IHandler)it.next();
			cntr+=hdl.getInstaceCntr();
		}
		return cntr;
		
	}
	private void initNodeHandlerMap(Hashtable pNodeHdlMap,Hashtable pNodeActionMap,Hashtable pActionPropMap,long pFileCntr)
	{
		StringTokenizer tokenizer;
		int cntr=0;
		String nodeName,nodeClassName,nodeClassNameStr,includeStr;
		fileCounter=pFileCntr;		
		
		lNodeHandlerMap = new HashMap();
		Enumeration hdlList = pNodeHdlMap.keys();
		while(hdlList.hasMoreElements())
		{
			nodeName=(String)hdlList.nextElement();	
			cntr++;
			
			try
			{
				//System.out.println(cntr+")Processing node:"+nodeName);
				//instantiate handler class
				nodeClassNameStr = (String)pNodeHdlMap.get(nodeName);
				tokenizer = new StringTokenizer(nodeClassNameStr,"|");
				nodeClassName = tokenizer.nextToken();
				includeStr = tokenizer.nextToken();				
				Object handlerObj = (Class.forName(nodeClassName)).newInstance();
				IHandler handler = (IHandler) handlerObj;
				//associate Action with handler		
				handler.setHandlerAction(getAction(nodeName,pNodeActionMap,pActionPropMap));
				handler.setNodeName(nodeName);
				handler.setIncludeValue(includeStr);				
				lNodeHandlerMap.put(nodeName,handler);	
				
			}
			catch(ClassNotFoundException excp)
			{
				excp.printStackTrace();
			}
			catch(IllegalAccessException excp)
			{
				excp.printStackTrace();
			}
			catch(InstantiationException excp)
			{
				excp.printStackTrace();
			}			
			
		}
		handlerList = lNodeHandlerMap.values();
		handlerSet = lNodeHandlerMap.keySet();
		//System.out.println("KeySet:"+handlerSet);
	}
	private IAction getAction(String pNodeName,Hashtable pNodeActionMap,Hashtable pActionPropMap)
	{
		IAction action=null;
		HashMap actionProps=null;
		Object actionObj=null;
		String destination=null,actionClassName= null,type=null;
		StringTokenizer tokenizer=null;
		String actionClassNameStr = (String)pNodeActionMap.get(pNodeName);
		System.out.println("Nodename="+pNodeName+",action="+actionClassNameStr);
		if(actionClassNameStr!=null)
		{
			tokenizer=new StringTokenizer(actionClassNameStr,"|");
			actionClassName= (String)tokenizer.nextToken();
			type=(String)tokenizer.nextToken();
			destination=(String)tokenizer.nextToken();
			
		}
		//System.out.println("Node name:"+pNodeName+",Action class:"+actionClassName+", dest:"+destination+",type:"+type);
	
		try
		{
			Class actionClass=Class.forName(actionClassName);
			actionObj= actionClass.newInstance();
			action = (IAction)actionObj;
			action.setDestination(destination);
			action.setActionType(type);
			
		}		
		catch(InstantiationException excp)
		{
			excp.printStackTrace();
		}
		catch(IllegalAccessException excp)
		{
			excp.printStackTrace();
		}
		catch(ClassNotFoundException excp)
		{
			excp.printStackTrace();
		}
				
		action.setProps((String)pActionPropMap.get(actionClassName));
				
		return action;
	}
	private void selectCurElement(StartElement pEvent)
	{
			String nodeName = pEvent.getName().getLocalPart();
			IHandler hdl = (IHandler)lNodeHandlerMap.get(nodeName);
			//System.out.println("Node:"+nodeName+",hdl:"+hdl);
			if(hdl!=null)
			{
				//System.out.println("Node:"+nodeName+",hdl:"+hdl);
				requestedElementOnStart=true;
				current_handler = hdl;									
			}
			else 
			{
				requestedElementOnStart=false;
			}
			curCounter++;
		
	}
	private boolean endRequestedNode(EndElement pEvent)
	{
		
		String nodeName = pEvent.getName().getLocalPart();
		IHandler hdl = (IHandler)lNodeHandlerMap.get(nodeName);
		//System.out.println("node:"+nodeName+", hdl:"+hdl+",cur_hdl:"+current_handler);
		if(hdl!=null)
		{
			requestedElementOnEnd=true;
			current_handler = hdl;
			return true;
		}
		else 
		{
			requestedElementOnEnd=false;
			return false;
		}
	}
	private void appendEvent(XMLEvent pEvent)
	{
		if(current_handler!=null)
			current_handler.appendEvent(pEvent.toString());
			//System.out.println("current_buffer:"+current_buffer);
		
	}
	private void cleanup()
	{
		propLoader.removeFileCntr();
		propLoader.resetFileCntr();
		//propLoader.renameInputFile(lInputFileName);
	}
	
}
