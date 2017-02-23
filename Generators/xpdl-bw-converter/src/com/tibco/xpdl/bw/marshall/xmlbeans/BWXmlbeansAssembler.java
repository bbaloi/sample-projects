package com.tibco.xpdl.bw.marshall.xmlbeans;

import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import noNamespace.BWSharedResourceDocument;
import noNamespace.ClientIDDocument;
import noNamespace.NamingCredentialDocument;
import noNamespace.NamingPrincipalDocument;
import noNamespace.PasswordDocument;
import noNamespace.BWSharedResourceDocument.BWSharedResource;
import noNamespace.ClientIDDocument.ClientID;
import noNamespace.NamingCredentialDocument.NamingCredential;
import noNamespace.NamingPrincipalDocument.NamingPrincipal;
import noNamespace.PasswordDocument.Password;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlTokenSource;
import org.apache.xmlbeans.impl.soap.Node;
import org.apache.xmlbeans.impl.xb.xsdschema.ExplicitGroup;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalElement;
import org.apache.xmlbeans.impl.xb.xsdschema.LocalSimpleType;
//import org.apache.xmlbeans.impl.xb.xsdschema.Element;
//import org.apache.xmlbeans.impl.xb.xsdschema.Element;
import org.w3.x1999.xsl.transform.CopyOfDocument.CopyOf;
import org.w3.x1999.xsl.transform.ValueOfDocument.ValueOf;
import org.w3.x2001.xmlSchema.ComplexTypeDocument.ComplexType;
import org.w3.x2001.xmlSchema.ElementDocument.Element;
import org.w3.x2001.xmlSchema.SequenceDocument.Sequence;
import org.wfmc.x2002.xpdl10.ActivityDocument.Activity;
import org.wfmc.x2002.xpdl10.ConditionDocument.Condition;
import org.wfmc.x2002.xpdl10.ExtendedAttributeDocument.ExtendedAttribute;
import org.wfmc.x2002.xpdl10.ExtendedAttributesDocument.ExtendedAttributes;
//import org.wfmc.x2002.xpdl10.SchemaTypeDocument.SchemaType;
import org.wfmc.x2002.xpdl10.TransitionDocument.Transition;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument.WorkflowProcess;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.tibco.xmlns.bw.process.x2003.ProcessDefinitionDocument;
import com.tibco.xmlns.bw.process.x2003.Root;
//import com.tibco.xmlns.bw.process.x2003.RootDocument;
import com.tibco.xmlns.bw.process.x2003.StarterDocument;
//import com.tibco.xmlns.bw.process.x2003.XsdElement;
import com.tibco.xmlns.bw.process.x2003.XslValueOf;
import com.tibco.xmlns.bw.process.x2003.EndTypeDocument.EndType;
import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings;
import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.FrameworkInput;
import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars;
//import com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.Root.Input;
import com.tibco.xmlns.bw.process.x2003.ProcessDefinitionDocument.ProcessDefinition;
import com.tibco.xmlns.bw.process.x2003.ProcessVariablesDocument.ProcessVariables;
import com.tibco.xmlns.bw.process.x2003.ProcessVariablesDocument.ProcessVariables.Schema0;


import com.tibco.xmlns.bw.process.x2003.ReturnBindingsDocument.ReturnBindings;
import com.tibco.xmlns.bw.process.x2003.ReturnBindingsDocument.ReturnBindings.ReturnValue;
import com.tibco.xmlns.bw.process.x2003.Root.Root2;
import com.tibco.xmlns.bw.process.x2003.StartTypeDocument.StartType;
import com.tibco.xmlns.bw.process.x2003.StarterDocument.Starter;
import com.tibco.xmlns.bw.process.x2003.StarterDocument.Starter.Config;
import com.tibco.xmlns.bw.process.x2003.StarterDocument.Starter.Config.ConfigurableHeaders;
import com.tibco.xmlns.bw.process.x2003.StarterDocument.Starter.Config.OutDataxsdString;
import com.tibco.xmlns.bw.process.x2003.StarterDocument.Starter.Config.SessionAttributes;
import com.tibco.xpdl.bw.IBWAssembler;
import com.tibco.xpdl.bw.vo.ExtAttrValsVO;
import com.tibco.xpdl.bw.vo.JMSValuesVO;
import com.tibco.xpdl.bw.vo.TransVO;
import com.tibco.xpdl.controller.BWAbstractAssembler;
import com.tibco.xpdl.controller.IBWBuilderClient;
import com.tibco.xpdl.util.Constants;

public class BWXmlbeansAssembler extends BWAbstractAssembler implements IBWAssembler
{
	private static Logger sLogger = Logger.getLogger( "com.tibco.xpdl.bw.marshall.xmlbeans.BWXmlbeansAssembler");
	
	private ProcessDefinitionDocument lProcDoc;
	private ProcessDefinition proc;
	private boolean starterActivitySet;
	private StarterDocument starterDoc;
	private Starter	 starterActivity;
	private boolean createdJMSConnection;
	private BWSharedResourceDocument lResourceDoc;
	private BWSharedResource lResource;
	private int lCurXCoord=0,lCurYCoord=0;
	private String endActivityId,endActivityName;
	private String jmsRcvrOutputVarName;
	private String lStarterName;
	private String mainProcName;
	private String jmsQueueName;
	private String lastProcCallOutput;
	private boolean jmsStarter;
	private String conditionStr;
	private String lStarterProperties;
		
	public BWXmlbeansAssembler(IBWBuilderClient pClient)
	{
		super(pClient);
		starterActivitySet=false;
		createdJMSConnection=false;
		jmsStarter=false;		
	}
	
	public void addJMSResource(JMSValuesVO pValues)
	{
		sLogger.debug("Creating JMS Connection !");
		lResourceDoc = BWSharedResourceDocument.Factory.newInstance();
		lResource = lResourceDoc.addNewBWSharedResource();
		
		lResource.setName(pValues.getJmsConnectionName());
		lResource.setResourceType(pValues.getJmsResourceType());
		lResource.addNewConfig();		
		lResource.getConfig().addNewNamingEnvironment();
		lResource.getConfig().getNamingEnvironment().setUseJNDI(getBoolVal(pValues.getUserJNDI()));
		lResource.getConfig().getNamingEnvironment().setProviderURL(pValues.getProviderURL());
		lResource.getConfig().getNamingEnvironment().setNamingURL(pValues.getNamingURL());
		lResource.getConfig().getNamingEnvironment().setNamingInitialContextFactory(pValues.getInitialContextFactory());
		lResource.getConfig().getNamingEnvironment().setTopicFactoryName(pValues.getTopicFactory());
		lResource.getConfig().getNamingEnvironment().setQueueFactoryName(pValues.getQueueFactory());
				
		NamingPrincipalDocument principalDoc= NamingPrincipalDocument.Factory.newInstance();
		principalDoc.addNewNamingPrincipal();
		NamingPrincipal principal = principalDoc.getNamingPrincipal();
		try
		{
			XmlString _principal = XmlString.Factory.newInstance();
			_principal.setStringValue(pValues.getNamingPrincipal());
			principal.set(_principal);
			lResource.getConfig().getNamingEnvironment().setNamingPrincipal(principal);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		NamingCredentialDocument credentialDoc= NamingCredentialDocument.Factory.newInstance();
		credentialDoc.addNewNamingCredential();
		NamingCredential credential = credentialDoc.getNamingCredential();
		try
		{
			XmlString _credential = XmlString.Factory.newInstance();
			_credential.setStringValue(pValues.getNamingPassword());
			credential.set(_credential);
			lResource.getConfig().getNamingEnvironment().setNamingCredential(credential);
		
			lResource.getConfig().addNewConnectionAttributes();
			lResource.getConfig().getConnectionAttributes().setUsername(pValues.getUser());
			PasswordDocument passwdDoc = PasswordDocument.Factory.newInstance();
			Password passwd = passwdDoc.addNewPassword();			
			XmlString _paswd = XmlString.Factory.newInstance();
			_paswd.setStringValue(pValues.getPasswd());			
			passwd.set(_paswd);
			lResource.getConfig().getConnectionAttributes().setPassword(passwd);
			ClientIDDocument clientDoc = ClientIDDocument.Factory.newInstance();
			ClientID clientId = clientDoc.addNewClientID();
			
			XmlString _clientid = XmlString.Factory.newInstance();
			_clientid.setStringValue(pValues.getClientId());			
			clientId.set(_clientid);
			lResource.getConfig().getConnectionAttributes().setClientID(clientId);
			lResource.getConfig().setUseXACF(pValues.isUseXACF());
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
		createdJMSConnection=true;
	}
	public void addBWProcessDefinition(WorkflowProcess pProc)
	{
		if(newProc)
			init();	
		else
			reset();
		lCurXCoord=0;lCurYCoord=0;
		lProcDoc = ProcessDefinitionDocument.Factory.newInstance();
		proc=lProcDoc.addNewProcessDefinition();			
		//proc.setName(pProc.getName());
		if(pProc!=null)
		{
			mainProcName=pProc.getName();		
			proc.setName(lBuilderClient.getConverterProperties().getProperty("bw.proc.internal.path")+pProc.getName()+".process");
		}
		else
			proc.setName(lBuilderClient.getConverterProperties().getProperty("bw.proc.internal.path")+mainProcName+"-Main.process");
		
		proc.setStartX(new BigInteger(Integer.toString(lCurXCoord)));
		proc.setStartY(new BigInteger(Integer.toString(lCurYCoord)));
		
	}
	public void addBWActivity(Activity pActivity,boolean pLast,int pActivityIndex,int pNumActivities)
	{
		String activityType=null,activityTypeValue=null,parameters=null,parametersValue=null,setProperties=null,setPropertiesValue=null,output=null,outputValue=null;
		String input=null,inputValue=null;
		String activityId = pActivity.getId();
		String activityName=pActivity.getName();		
		ExtendedAttributes extAttrs = pActivity.getExtendedAttributes();
		ExtendedAttribute [] extAttrArray = extAttrs.getExtendedAttributeArray();
		ExtAttrValsVO extAttrsVals=null;
		
		if(extAttrArray!=null)
		{
			sLogger.debug("-Processing Activity:Name="+activityName+",id="+pActivity.getId());
			sLogger.debug("NumExtAttrs:"+extAttrArray.length);
			for(int i=0; i<extAttrArray.length;i++)
			{ 
				ExtendedAttribute extAttr = extAttrArray[i];
				
				String name = extAttr.getName();
				String value = extAttr.getValue();						
				if(name.equals(Constants.activity_type))
				{
					activityType=name;
					activityTypeValue=value;	
					sLogger.debug("++++++ActivtyType:"+name+":value="+value);
					
					if(value.equals(Constants.jms_queue_receiver))
					{
						if((lBuilderClient.getConverterProperties().get("jms.generate.connection")).equals("true"))
						{
							if(!createdJMSConnection)
							{
								addJMSResource(new JMSValuesVO(lBuilderClient.getConverterProperties().getProperty("jms.connection.name"),
										lBuilderClient.getConverterProperties().getProperty("jms.resource.type"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.useJNDI"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.providerURL"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.namingURL"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.contextFactory"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.topicFactory"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.queueFactory"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.namingPrincipal"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.namingCredential"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.username"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.password"),
										lBuilderClient.getConverterProperties().getProperty("jms.connection.clientId"),
										getBoolVal((String)lBuilderClient.getConverterProperties().getProperty("jms.connection.password"))));
							}					
					}
						starterActivitySet=true;
						jmsStarter=true;
					}
					if(value.equals(Constants.activity_start))
					{
						starterActivitySet=true;
						
					}
					
				}
				if(name.equals(Constants.params) )
				{
					parameters = name;
					parametersValue=value;				
					//sLogger.debug("Props read:name-"+parameters+",value-"+parametersValue);
					
				}
				if(name.equals(Constants.set_properties) )
				{
					setProperties=name;
					setPropertiesValue=value;
				}
				if(name.equals(Constants.output) )
				{
					output=name;
					outputValue=value;					
				}	
				if(name.equals(Constants.input) )
				{
					input=name;
					inputValue=value;
				}	
				if(name.equals(Constants.xpd))
				{
					extAttrsVals=getExtensions(extAttr);					
				}
			}
			
		}
		if(starterActivitySet)
		{				
			if(jmsStarter)
				setProcessStarterJMS(activityName,pActivity,getQueueName(parametersValue),inputValue,activityId,output,outputValue,extAttrsVals,setPropertiesValue);
			else	
				setProcessStarterPlain(activityName,pActivity,inputValue,activityId,output,outputValue,getQueueName(parametersValue),extAttrsVals,setPropertiesValue);
			
			starterActivitySet=false;			
		}
		else
		{
			//create a regular activity
			
			//if(pActivityIndex>0 && pActivityIndex<pNumActivities)
			if(pActivityIndex>0)
					
			{
				//if(!pLast)
				//{
					if(parametersValue==null)
						createActivity(activityName,activityTypeValue,parametersValue,inputValue,activityId,pActivityIndex,setPropertiesValue,
								input,inputValue,output,outputValue,extAttrsVals);
					else
						createActivity(activityName,activityTypeValue,getParamValue(parametersValue),inputValue,activityId,pActivityIndex,setPropertiesValue,
								input,inputValue,output,outputValue,extAttrsVals);
				//}
				//else
					//createProcessEndPoint(activityId);
			}
		}
	}
	public void addBWTransition(Transition pTransition)
	{
		com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity fromActivity=null,toActivity=null;
		
		String fromId =  pTransition.getFrom();
		String toId=pTransition.getTo();		
		TransVO transVo=new TransVO(fromId,toId);
		lTransitionList.add(transVo);
		
		String fromName = (String)lTransitionMap.get(fromId);
		String toName = (String)lTransitionMap.get(toId);
		com.tibco.xmlns.bw.process.x2003.TransitionDocument.Transition bwTransition =  proc.addNewTransition();
		
		bwTransition.setFrom(fromName);
		if(toId.equals(endActivityId))
		{
			bwTransition.setTo("End");
			sLogger.debug("from:"+fromName+"-to:"+toName);
		}
		else
			bwTransition.setTo(toName);
		bwTransition.setLineType("Default");
		bwTransition.setLineColor(new BigInteger("-16777216"));
		String conditionStr=(String)lConditionMap.get(fromId);
		sLogger.debug("addtransition-Condition:"+conditionStr);
		if(conditionStr==null)
		{
			bwTransition.setConditionType("always");
			
		}
		else
		{
			ExtendedAttributes attrs=pTransition.getExtendedAttributes();			
			if(attrs!=null)
			{
				
				ExtendedAttribute [] attrArray=attrs.getExtendedAttributeArray();
				for(int i=0;i<attrArray.length;i++)
				{
					ExtendedAttribute attr=attrArray[i];	
					String attrName=attr.getName();
					String attrValue=attr.getValue();
					//sLogger.debug("Condition attr:name="+attrName+",value="+attrValue);
					if(attr.getName().equals(Constants.condition_criteria))
					{
					    sLogger.debug("Cond Str:"+conditionStr);
						//String conditionExpr = 	getCondition(conditionStr);
						String condVar=	conditionStr.substring(1,conditionStr.indexOf("="));
						String procName=getProcName(condVar);
						String condExpr= conditionStr.substring(conditionStr.indexOf("'")+1,conditionStr.lastIndexOf("'"));
						sLogger.debug("CondVar:"+condVar+",CondExpr:"+condExpr);						
						
						if(isConditionDestPoc(toName,condExpr)) 
						{
							bwTransition.setConditionType("xpath");							
							bwTransition.setXpath(getConditionExpr(procName,condVar,condExpr));
						}
					}
				}
			}	
			else
			{
				//otherwise
				bwTransition.setConditionType("otherwise");			
								
			}
			
		}
		
	}
	public void addBWCondition(Condition pCondition)
	{
		
	}
	public void export()
	{
		String bwProcDoc=null;
		//export resource object - i.e. JMS file
		if(lResource!=null)
		{
			String resFileName=lBuilderClient.getConverterProperties().getProperty("bw.res.dir");
			resFileName+=lResource.getName()+".sharedjmscon";	
			sLogger.debug("JMSConnection file:"+resFileName);
			
			File resFile = new File(resFileName);
			try
			{
				FileWriter resWriter = new FileWriter(resFile);
				resWriter.write(lResourceDoc.toString());	
				resWriter.close();
			}
			catch(java.io.IOException pExcp)
			{
				pExcp.printStackTrace();
			}
			
		}
		String projFileName = lBuilderClient.getConverterProperties().getProperty("bw.proj.dir")+mainProcName+".process";
		//export BW document		
		File projFile = new File(projFileName);
		try
		{			
			FileWriter projWriter = new FileWriter(projFile);	
			sLogger.debug("========This is my new Project:=======");
			String newDoc=addJmsNsPlugin();
			sLogger.debug(newDoc);			
			projWriter.write(newDoc);
			projWriter.close();
		}
		catch(java.io.IOException pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	private void setProcessStarterPlain(String pStarterName,Activity pActivity,String pDataIn,String pActivityId,
			String pOutputVar,String pOutputVarValue,String pQueueName,ExtAttrValsVO pAttrVals,String pProperties)
	{
		lStarterProperties=pProperties;
		XmlAnySimpleType activityName = XmlAnySimpleType.Factory.newInstance();
		activityName.set(pStarterName);
		proc.setStartName(pStarterName);					
		StartType startType=proc.addNewStartType();
		Element _elem=startType.addNewElement();
		_elem.setName("root");
		ComplexType _complexType=_elem.addNewComplexType();
		Sequence _seq=_complexType.addNewSequence();
		Element _newElem=_seq.addNewElement();
		_newElem.setName("dataInput");
		_newElem.setType("xs:string");		
		
		//sLogger.debug("ExtAttrVals-x:"+pAttrVals.getXOffset());
		
		//starterActivity.setX(new BigInteger(pAttrVals.getXOffset()));
		//starterActivity.setY(new BigInteger(pAttrVals.getYOffset()));	
		
		
		lStarterName=pStarterName;			
		String outputVar="$"+buffName(lStarterName," ","-")+"/root/dataInput";
		sLogger.debug("StarterMapValues:key="+pOutputVarValue+",val="+outputVar);	
		addOutputVar(pOutputVarValue, outputVar);
		//lActivityInputOutputMap.put(pOutputVarValue, outputVar);
		lActivityIdOutputMap.put(pActivityId,outputVar);
		jmsQueueName=pQueueName;
		
		lTransitionMap.put(pActivityId,pStarterName);
		
	}

	private void setProcessStarterJMS(String pStarterName,Activity pActivity,String pQueueName,String pDataIn,String pActivityId,
									String pOutputVar,String pOutputVarValue,ExtAttrValsVO pAttrVals,String pProperties)
	{
		XmlAnySimpleType activityName = XmlAnySimpleType.Factory.newInstance();
		if(!generateFramework)
			lStarterProperties=pProperties;
		if(pStarterName!=null)			
		{
			proc.setStartName(pStarterName);	
			activityName.setStringValue(pStarterName);
		}
		
		proc.addNewStarter();
		starterActivity = proc.getStarter();	
		starterActivity.setName(activityName);		
		starterActivity.setType(Constants.jmsReceiverType);
		starterActivity.setResourceType(Constants.jmsReceiverResourceType);
		//String pass=(String)lBuilderClient.getConverterProperties().get("generation.pass");
				
		if(!generateFramework)
		{
			starterActivity.setX(new BigInteger(pAttrVals.getXOffset()));
			starterActivity.setY(new BigInteger(pAttrVals.getYOffset()));	
			
			//starterActivity.setX(new BigInteger(Integer.toString(lCurXCoord)));
			//starterActivity.setY(new BigInteger(Integer.toString(lCurYCoord)));		
		}
		else
		{
			starterActivity.setX(new BigInteger("0"));
			starterActivity.setY(new BigInteger("0"));		
		}
		starterActivity.addNewConfig();
		Config starterConfig = starterActivity.getConfig();
		starterConfig.setPermittedMessageType(Constants.jms_xml_type);
		
		starterConfig.addNewSessionAttributes();
		SessionAttributes sessionAttributes = starterConfig.getSessionAttributes();
		sessionAttributes.setTransacted(false);
		sessionAttributes.setAcknowledgeMode(new BigInteger("1"));
		sessionAttributes.setMaxSessions(new BigInteger("1"));
		if(pQueueName!=null)
			sessionAttributes.setDestination(pQueueName);		
		else
			sessionAttributes.setDestination(jmsQueueName);
		
		starterConfig.addNewConfigurableHeaders();
		ConfigurableHeaders configHeader = starterConfig.getConfigurableHeaders();
		configHeader.setJMSDeliveryMode("PERSISTENT");
		configHeader.setJMSExpiration(new BigInteger("0"));
		configHeader.setJMSPriority(new BigInteger("4"));		
		starterConfig.setConnectionReference(lBuilderClient.getConverterProperties().getProperty("jms.connection.reference") );
		starterConfig.addNewOutDataxsdString();
		OutDataxsdString outData=starterConfig.addNewOutDataxsdString();
		Element rootElement = outData.addNewElement();
		rootElement.setName("root");
		ComplexType _complexType=rootElement.addNewComplexType();
		Sequence _seq=_complexType.addNewSequence();
		Element outputElement=_seq.addNewElement();
		if(pOutputVarValue!=null)
			outputElement.setName(pOutputVarValue);
		else
			outputElement.setName(Constants.framework_jms_queue_output);
		outputElement.setType("xsd:string");		
		jmsRcvrOutputVarName=pOutputVar;
		lStarterName=pStarterName;
		if(pActivity!=null)
			addStarterOutputVars(starterActivity,pOutputVar,pOutputVarValue,pActivityId);		
		else
			addStarterOutputVars(starterActivity,pOutputVar,Constants.framework_jms_queue_output,pActivityId);		
		
		if(pActivityId!=null)
			lTransitionMap.put(pActivityId,pStarterName);
		else
			lTransitionMap.put("1",pStarterName);
		
	}
	private void createActivity(String pActivityName,String pActivityValue,String pProcessName,String pInputData,String pActivityId,int pActivityIndex,
								String pProcessVars,String pInputVar,String pInputVarValue,
								String pOutputVar,String pOutputVarValue,ExtAttrValsVO pAttrVals)
	{
		com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity activity=null;
		lProcMap.put(pActivityId, pActivityName);
		try
		{
			if(pActivityName!=null)
			{
				activity = (com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity)proc.addNewActivity();
				lActivityMap.put(pActivityId,activity);
				XmlAnySimpleType _activityName=XmlAnySimpleType.Factory.newInstance();
				_activityName.setStringValue(pActivityName);
				activity.setName(_activityName);	
				String pass=(String)lBuilderClient.getConverterProperties().get("generation.pass");
				
				if(generateFramework)
				{
					activity.setX(new BigInteger("150"));
					activity.setY(new BigInteger(Integer.toString(lCurYCoord)));				
				}
				else 
				{
					sLogger.debug("value-x:"+pAttrVals.getXOffset());
					activity.setX(new BigInteger(pAttrVals.getXOffset()));
					activity.setY(new BigInteger(pAttrVals.getYOffset()));					
					//activity.setX(new BigInteger(nextXCoord()));
					//activity.setY(new BigInteger(Integer.toString(lCurYCoord)));
					
				}
								
			}
								
				if(pActivityValue!=null)
				{
					sLogger.debug("ActivityType:"+pActivityValue);
					if(pActivityValue.equals(Constants.activity_process_call))
					{
						activity.setType(Constants.process_call_type);
						activity.setResourceType(Constants.process_call_resource_type);
						com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity.Config config = activity.addNewConfig();
						config.setProcessName(explodeBWProjectPath(pProcessName));
						//addInputVars(activity,pInputVar,pInputVarValue,pProcessVars);
					}
					if(pActivityValue.equals(Constants.activity_end))
					{
						//addOutputVars("End",activity,pOutputVar,Constants.framework_proc_output);
						createProcessEndPoint(pActivityId,pInputVarValue,pAttrVals);
						//createProcessEndPoint(pActivityId,Constants.framework_proc_output);
						//addInputVars(activity,pInputVar,pInputVarValue,pProcessVars);
						
					}
					if(pActivityValue.equals(Constants.condition_activity))
					{
						activity.setType(Constants.null_type);
						activity.setResourceType(Constants.null_resource_type);
						com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity.Config config = activity.addNewConfig();
						//config.setProcessName(explodeBWProjectPath(pProcessName));
						//conditionStr=pProcessVars;
						conditionStr=pProcessName;
						sLogger.debug("****Condition:Id"+pActivityId+",value="+conditionStr);
						lConditionMap.put(pActivityId, conditionStr);
						//lCurYCoord+=Constants.yOffset;
				
					}
			
					lTransitionMap.put(pActivityId,pActivityName);
					//addProcessVariables(pProcessVars);
					
					addInputVars(activity,pInputVar,pInputVarValue,pProcessVars,pActivityId);
					if(pOutputVarValue!=null)
						addOutputVars(pActivityValue,pActivityName,activity,pOutputVar,pOutputVarValue,pActivityId);
					else
					{
						
						if(generateFramework)
							addOutputVars(pActivityValue,pActivityName,activity,pOutputVar,Constants.activity_end,pActivityId);
						else
						{							
							addOutputVars(pActivityValue,pActivityName,activity,pOutputVar,"''",pActivityId);
							
						}
					}
				}
			
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		//listInOutEntriesUtil();
		
	}
	private String getQueueName(String pStr)
	{
		//sLogger.debug("QueueNameParam:"+pStr);
		String tmpRetVal = pStr.substring(pStr.indexOf("="),pStr.length());
		String retVal = tmpRetVal.substring(1,tmpRetVal.length());
		
		return retVal;
	}
	private String getParamValue(String pStr)
	{
		
		String tmpRetVal = pStr.substring(pStr.indexOf("="),pStr.length());
		String retVal = tmpRetVal.substring(1,tmpRetVal.length());
		sLogger.debug("ParamValue:"+retVal);
		return retVal;
	}
	private boolean getBoolVal(String pVal)
	{
		if(pVal.equals("true"))
			return true;
		else
			return false;
	}
	private String nextXCoord()
	{
		lCurXCoord = lCurXCoord+Constants.xOffset;
		return Integer.toString(lCurXCoord);		
	}
	private void createProcessEndPoint(String pActivityId,String pInputVarValue,ExtAttrValsVO pAttrVals)
	{
		sLogger.debug("adding Endpoint,inputvalue:"+pInputVarValue);
		endActivityId=pActivityId;		
		proc.setEndName("End");
		//String pass=(String)lBuilderClient.getConverterProperties().get("generation.pass");
		
		if(generateFramework)
		{
			proc.setEndX(new BigInteger("300"));
			proc.setEndY(new BigInteger(Integer.toString(lCurYCoord)));
		
		}
		else
		{
			proc.setEndX(new BigInteger(pAttrVals.getXOffset()));
			proc.setEndY(new BigInteger(pAttrVals.getYOffset()));
			//proc.setEndX(new BigInteger(nextXCoord()));
			//proc.setEndY(new BigInteger(Integer.toString(lCurYCoord)));
	
		}
		EndType end=proc.addNewEndType();
		//if(pInputVarValue!=null)
		//{
			Element elem = end.addNewElement();
			elem.setName("returnValue");
			ComplexType _complexType=elem.addNewComplexType();
			Sequence _seq=_complexType.addNewSequence();
			Element endElem=_seq.addNewElement();
			endElem.setName("outData");
			endElem.setType("xs:string");		
			
			ReturnBindings retBindings=proc.addNewReturnBindings();
			lActivityIdInputMap.put(pActivityId, pInputVarValue);
			lBindingsMap.put(pActivityId, retBindings);			
			
			if(generateFramework)
			{
			
				ReturnValue retVal=retBindings.addNewReturnValue();		
				XslValueOf endElemData=retVal.addNewOutData();		
				ValueOf val=endElemData.addNewValueOf();
				XmlAnySimpleType select=val.addNewSelect();
				String inputVarValue = (String)lActivityInputOutputMap.get(pInputVarValue);
				
				//String inputVarValue = (String)lActivityInputOutputMap.get(Constants.activity_end);
				//sLogger.debug("endpoint input binding value:"+inputVarValue);
				//String outputVar="$"+buffName(pActivityName," ","-")+"/outputVars/outValues";
				if(inputVarValue!=null)
					select.setStringValue(inputVarValue);	
				else
				{
						//flag empty value - probably due to improer ordering
						lUnresolvedBindingsMap.put(pInputVarValue,select );
				}
			}
		
					
		//}
	}
	private void addProcessVariables(String pProcVars)
	{
		StringTokenizer tokenizer = new StringTokenizer(pProcVars,"=");
		String varName = tokenizer.nextToken();
		String varValue = tokenizer.nextToken();
		sLogger.debug("ProcVars:"+varName+"->"+varValue);
		ProcessVariables procVars = proc.addNewProcessVariables();
		Schema0 schema=procVars.addNewSchema0();
		Element elem=schema.addNewElement();
		ComplexType _complexType=elem.addNewComplexType();
		Sequence _seq=_complexType.addNewSequence();
		//XsdElement _seq=_complexType.addNewSequence();
		Element _elem=_seq.addNewElement();
		_elem.setName(varName);
		_elem.setType("ComplexType");
	}
	
	private void addInputVars(com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity pActivity,
				String pInputVar,String pInputVarValue,String pProcessVars,String pActivityId)
	{
		
		//String inputVar;
		String inputVarValue = (String)lActivityInputOutputMap.get(pInputVarValue);
		
		//sLogger.debug("%%%input vars:"+pInputVarValue+",inputValue:"+inputVarValue);
		//adjustBindings();
		
			if(pActivity!=null)
			{
			InputBindings  inputBindings= pActivity.addNewInputBindings();			
			lBindingsMap.put(pActivityId, inputBindings);
			lActivityIdInputMap.put(pActivityId, pInputVarValue);
			if(!generateFramework)
			{
				
				com.tibco.xmlns.bw.process.x2003.InputBindingsDocument.InputBindings.InputVars rootVal = inputBindings.addNewInputVars();
				/*
				if(pInputVarValue !=null)
				{
					
					//ValueOf dynamicVar=inputValues.addNewValueOf();
					XslValueOf dynVar=rootVal.addNewDynamicVar();				
					ValueOf _dynVar = dynVar.addNewValueOf();
					XmlAnySimpleType dynamicValue=_dynVar.addNewSelect();				
					
						//String inputVarValue = (String)lActivityInputOutputMap.get(pInputVarValue);
						if(inputVarValue!=null)
							dynamicValue.setStringValue(inputVarValue);		
						else
						{
								//flag empty value - probably due to improer ordering
								lUnresolvedBindingsMap.put(pInputVarValue,dynamicValue );
						}
					
				}
				*/
				if(pProcessVars!=null)
				{
					XslValueOf staticVar=rootVal.addNewStaticVar();
					ValueOf _staticVar = staticVar.addNewValueOf();
					XmlAnySimpleType staticValue=_staticVar.addNewSelect();	
					staticValue.setStringValue("'"+pProcessVars+"'");
											
				}
				else
				{
					XslValueOf staticVar=rootVal.addNewStaticVar();
					ValueOf _staticVar = staticVar.addNewValueOf();
					XmlAnySimpleType staticValue=_staticVar.addNewSelect();	
					staticValue.setStringValue("''");
				}
			}
			else
			{
				InputBindings.Root inputRoot=inputBindings.addNewRoot();
				XslValueOf dataVal=inputRoot.addNewDataInput();
				ValueOf val=dataVal.addNewValueOf();
				XmlAnySimpleType _val=val.addNewSelect();
				_val.setStringValue(inputVarValue);						
			}
		}
		
	}
	
	private void addStarterOutputVars(Starter pStarterActivity,String pOutputVar,String pOutputVarValue,String pActivityId)
	{
		String outputVar="$"+buffName(lStarterName," ","-")+"/ns:ActivityOutput/Body";
		addOutputVar(pOutputVarValue, outputVar);
		//lActivityInputOutputMap.put(pOutputVarValue, outputVar);		
		lActivityIdOutputMap.put(pActivityId,outputVar);
	}
	
	private void addOutputVars(String pActivityType,String pActivityName,com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity pActivity,
				String pOutputVar,String pOutputVarValue,String pActivityId)
	{
		String _val,outputVar;
		
		if(pActivity!=null)
		{
			if(!generateFramework)
			{
				sLogger.debug("=====ActivityType:"+pActivityType+",output:"+pOutputVarValue);
			
				//if(pOutputVarValue!=null)
				if(!pOutputVarValue.equals("''"))
				{
					if(pOutputVarValue.equals(Constants.jms_request))
						outputVar="$"+buffName(pActivityName," ","-")+"/root/"+pOutputVarValue;
					else
						outputVar="$"+buffName(pActivityName," ","-")+"/OutputData/outputValue";
						//outputVar="$"+buffName(pActivityName," ","-")+"/"+getOutputStructure(pOutputVarValue);
					
					_val="'"+outputVar+"'";
					addOutputVar(pOutputVarValue, outputVar);
					//lActivityInputOutputMap.put(pOutputVarValue, outputVar);					
					//lActivityInputOutputMap.put(outputVar, outputVar);						
					sLogger.debug("=====OutputVarKey:"+pOutputVarValue+",output:"+lActivityInputOutputMap.get(pOutputVarValue));
					lActivityIdOutputMap.put(pActivityId,outputVar);
					
				}			
				else
				{
					
					if(!pActivityType.equals(Constants.condition_activity))
					{
						//outputVar="$"+buffName(pActivityName," ","-")+"/outputVars/outValues";
						outputVar="$"+buffName(pActivityName," ","-")+"/OutputData/outputValue";						
						_val="'"+outputVar+"'";					
						addOutputVar(Constants.activity_end, outputVar);
						//lActivityInputOutputMap.put(Constants.activity_end, outputVar);
						lActivityIdOutputMap.put(pActivityId,outputVar);
						//lActivityInputOutputMap.put(pOutputVarValue, outputVar);
						//sLogger.debug("outputvalue=null:"+pOutputVarValue+"|"+outputVar);
					}
					
				}
			}
			else //this this end value
			{
				outputVar="$"+pActivityName+"/returnValue/outData";
				
				_val="'"+outputVar+"'";
				addOutputVar(pOutputVarValue, outputVar);				
				lActivityInputOutputMap.put(pOutputVarValue, outputVar);				
				//lActivityInputOutputMap.put(outputVar, outputVar);
				lActivityIdOutputMap.put(pActivityId,outputVar);
				
			}
		}
		
	}
	private String buffName(String pStr,String pDelim,String pReplaceDelim)
	{
		String retVal="";
		ArrayList list = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(pStr,pDelim);
		
		while(tokenizer.hasMoreTokens())
		{
			String token = tokenizer.nextToken();
			list.add(token);
		}
		int len=list.size();
		
		Iterator it = list.iterator();		
		while(it.hasNext())
		{
			retVal+=it.next()+pReplaceDelim;
		}
		
		return retVal.substring(0,retVal.lastIndexOf(pReplaceDelim));
	}
	private String explodeBWProjectPath(String pProcName)
	{
		String retVal=null;		
		String procPrepend=lBuilderClient.getConverterProperties().getProperty("bw.proc.path.prepend");
		
		sLogger.debug("-------ProcCallName:"+pProcName+"generateFramework:"+generateFramework);
		String buffedStr;
		if(!generateFramework)
		{
			buffedStr = buffName(pProcName,".","/");
			retVal=lBuilderClient.getConverterProperties().getProperty("bw.proj.dir.gen")+buffedStr+".process";
		}
		else
			retVal=lBuilderClient.getConverterProperties().getProperty("bw.proj.dir.framework_src")+"/"+pProcName+".process";
			retVal=procPrepend+retVal;
		
		return retVal;
	}
	private String addJmsNsPlugin()
	{
		String retVal=null;
		StringBuffer newHeader = new StringBuffer();
		
		String ns = lBuilderClient.getConverterProperties().getProperty("jms.plugin.namespace");
		String xmlHeader = lBuilderClient.getConverterProperties().getProperty("xml.header");
		String doc = lProcDoc.toString();
		StringBuffer docBuff=new StringBuffer(doc);
		int chCntr=0;
		char ch = docBuff.charAt(chCntr);
		
		while(ch!='>')
		{		
			newHeader.append(ch);				
			chCntr++;
			ch = docBuff.charAt(chCntr);
		}
		String newHeaderStr=newHeader.toString();
		newHeaderStr+=" "+ns+">";
		sLogger.debug("New Header:"+newHeaderStr);		
		chCntr=chCntr+2; //move at new '<'
		String docBody = doc.substring(chCntr,doc.length());	
		
		String newDoc = newHeaderStr+docBody;
		retVal = xmlHeader+newDoc;
		
		
		return retVal;
	}
	protected void createJMSReceiver()
	{
		if((lBuilderClient.getConverterProperties().get("jms.generate.connection")).equals("true"))
		{
			addJMSResource(new JMSValuesVO(lBuilderClient.getConverterProperties().getProperty("jms.connection.name"),
					lBuilderClient.getConverterProperties().getProperty("jms.resource.type"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.useJNDI"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.providerURL"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.namingURL"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.contextFactory"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.topicFactory"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.queueFactory"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.namingPrincipal"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.namingCredential"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.username"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.password"),
					lBuilderClient.getConverterProperties().getProperty("jms.connection.clientId"),
					getBoolVal((String)lBuilderClient.getConverterProperties().getProperty("jms.connection.password"))));

		}
		setProcessStarterJMS("JMS Receiver",null,null,null,null,null,null,null,null);		
	}
	protected void createProcDefintion()
	{
		addBWProcessDefinition(null);
	}
	protected  void createProcCall()
	{
			/*createActivity(mainProcName,Constants.activity_process_call,
				mainProcName,null,"2",0,null,
				null,Constants.framework_jms_queue_output,Constants.framework_proc_output,null,null);
			*/
		com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity activity=null;
		
		String frameworkProcName=lBuilderClient.getConverterProperties().getProperty("bw.framework.proc");
		
		activity = (com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity)proc.addNewActivity();
		//lActivityMap.put(pActivityId,activity);
		XmlAnySimpleType _activityName=XmlAnySimpleType.Factory.newInstance();
		_activityName.setStringValue("Framework");
		activity.setName(_activityName);	
		
		activity.setX(new BigInteger("150"));
		activity.setY(new BigInteger(Integer.toString(lCurYCoord)));				
		
		activity.setType(Constants.process_call_type);
		activity.setResourceType(Constants.process_call_resource_type);
		com.tibco.xmlns.bw.process.x2003.ActivityDocument.Activity.Config config = activity.addNewConfig();
		//config.setProcessName(explodeBWProjectPath(frameworkProcName));
		config.setProcessName(frameworkProcName);
		
		InputBindings inputBindings=activity.addNewInputBindings();		
		FrameworkInput frameworkInput=inputBindings.addNewFrameworkInput();
		CopyOf jmsInput=frameworkInput.addNewCopyOf();
		XmlAnySimpleType jmsInputType=jmsInput.addNewSelect();
		String _jmsInput="$JMS-Receiver/ns:ActivityOutput";
		jmsInputType.setStringValue(_jmsInput);
		
		String procDir = lBuilderClient.getConverterProperties().getProperty("bw.proj.dir.framework_src");
		String _procName="'"+procDir+mainProcName+".process'";
		XslValueOf procNameVal=frameworkInput.addNewProcessName();
		ValueOf _val=procNameVal.addNewValueOf();
		XmlAnySimpleType _valType=_val.addNewSelect();
		_valType.setStringValue(_procName);
		
		XslValueOf props=frameworkInput.addNewProperties();
		ValueOf _props=props.addNewValueOf();
		XmlAnySimpleType _propsValue=_props.addNewSelect();
		_propsValue.setStringValue("'"+lStarterProperties+"'");
		
				
		lTransitionMap.put("2","Framework");
			
	}
	protected void createEndPoint()
	{
		createProcessEndPoint("3",Constants.framework_proc_output,null);
	}
	protected void addTransitions()
	{
		//from start to proc		
		String fromName = (String)lTransitionMap.get("1");
		String toName = (String)lTransitionMap.get("2");
		com.tibco.xmlns.bw.process.x2003.TransitionDocument.Transition bwTransition1 =  proc.addNewTransition();
		bwTransition1.setFrom(fromName);
		bwTransition1.setTo(toName);
		bwTransition1.setLineType("Default");
		bwTransition1.setLineColor(new BigInteger("-16777216"));
		bwTransition1.setConditionType("always");
		
		//add from proc call to end
		String fromProcName = toName;
		com.tibco.xmlns.bw.process.x2003.TransitionDocument.Transition bwTransition2 =  proc.addNewTransition();
		bwTransition2.setFrom(fromProcName);
		bwTransition2.setTo("End");
		bwTransition2.setLineType("Default");
		bwTransition2.setLineColor(new BigInteger("-16777216"));
		bwTransition2.setConditionType("always");
	}
	protected void writeToFile()
	{
		String frameworkFileName = lBuilderClient.getConverterProperties().getProperty("bw.proj.dir")+mainProcName+"-Main.process";
		//export BW document		
		File frameworkFile = new File(frameworkFileName);
		try
		{			
			FileWriter projWriter = new FileWriter(frameworkFile);	
			sLogger.debug("========This is my new Framework:=======");
			String newDoc=addJmsNsPlugin();
			sLogger.debug(newDoc);			
			projWriter.write(newDoc);
			projWriter.close();
		}
		catch(java.io.IOException pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	private boolean isConditionDestPoc(String pProcName, String pConditionStr)
	{
		boolean retVal=false;
		sLogger.debug("ProcName:"+pProcName+",Condition:"+pConditionStr);	
		//String cond=pConditionStr.substring(pConditionStr.indexOf("'")+1,pConditionStr.lastIndexOf("'"));
		//if(pProcName.contains(new StringBuffer(cond)))
		
		
		//if(pProcName.contains(new StringBuffer(pConditionStr)))
			retVal=true;		
		
		
		return retVal;
	}
	private String getCondition(String  pConditionStr)
	{
		String retVal=null;
		retVal=pConditionStr.substring(pConditionStr.indexOf("="+2),pConditionStr.lastIndexOf('"')-1);
		
		sLogger.debug("retval1:"+retVal);		
		return retVal;
	}
	private String getConditionExpr(String pProcName,String pCondVar,String pExpr)
	{
		String retVal=null;
		sLogger.debug("Proc name:"+pProcName+",cond="+pCondVar+",expr:"+pExpr);
		StringTokenizer tokenizer = new StringTokenizer(pCondVar,".");
		String outRoot = tokenizer.nextToken();
		String outVar = tokenizer.nextToken();
				
		String procName= pProcName.substring(0, pProcName.indexOf("/"));
		//retVal=procName+"/"+outRoot+"/"+outVar+"="+"'"+pExpr+"'";	
		retVal=procName+"/OutputData/outputValue="+"'"+pExpr+"'";	
		sLogger.debug("cond var:"+retVal);
		return retVal;
	}
	private String getProcName(String pCondVar)
	{
		String retVal=null;
		
		StringTokenizer tokenizer = new StringTokenizer(pCondVar,".");
		String outputValue = tokenizer.nextToken();
		retVal = (String)lActivityInputOutputMap.get(outputValue);
		sLogger.debug("Original Val:"+pCondVar+",Proc root:"+retVal);
		return retVal;
	}
	
}
