package com.tibco.xpdl.controller;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.wfmc.x2002.xpdl10.ActivitiesDocument;
import org.wfmc.x2002.xpdl10.ActivityDocument;
import org.wfmc.x2002.xpdl10.ConditionDocument;
import org.wfmc.x2002.xpdl10.DataFieldDocument;
import org.wfmc.x2002.xpdl10.DataFieldsDocument;
import org.wfmc.x2002.xpdl10.PackageDocument;
import org.wfmc.x2002.xpdl10.PackageDocument.Package;
import org.wfmc.x2002.xpdl10.TransitionDocument;
import org.wfmc.x2002.xpdl10.TransitionsDocument;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument;
import org.wfmc.x2002.xpdl10.WorkflowProcessesDocument;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument.WorkflowProcess;
import org.wfmc.x2002.xpdl10.PackageHeaderDocument.PackageHeader;
import org.wfmc.x2002.xpdl10.WorkflowProcessesDocument.WorkflowProcesses;
import org.wfmc.x2002.xpdl10.TransitionsDocument;
import org.wfmc.x2002.xpdl10.TransitionDocument;
import org.wfmc.x2002.xpdl10.TransitionsDocument.Transitions;
import com.tibco.xpdl.bw.IBWAssembler;
import com.tibco.xpdl.exceptions.XPDLBWException;
import com.tibco.xpdl.util.Constants;

public class BWBuilder implements IBWBuilder,IBWBuilderClient
{
	
	private static final Logger sLogger = Logger.getLogger( "com.tibco.xpdl.controller.BWBuilder");
	private static BWBuilder instance;
	private PackageDocument lPkgDoc;
	private Package lPkg;
	private PackageHeader lPkgHeader;
	private WorkflowProcesses wpList;
	private WorkflowProcessDocument.WorkflowProcess curWP;
	private IBWAssembler lAssembler;	
	private Properties lConverterProperties;
	
	public static BWBuilder getInstance()
	{
		if(instance==null)
			instance = new BWBuilder();
		return instance;
	}
	
	private BWBuilder()
	{
		
	}
	public void buildBWProcessDefinition(PackageDocument pDoc,Properties pConverterProperties) throws XPDLBWException
	{
		lPkgDoc = pDoc;		
		lConverterProperties = pConverterProperties;
		String assemblerMode = lConverterProperties.getProperty("export.mode");
		try
		{
			if(assemblerMode.equals(Constants.xmlbeans_export_mode))
			{
				lAssembler = AssemblerFactory.getXMLBeanAssembler(this);
				sLogger.debug("Processing XPDL with XmlBeans !");
			}
			if(assemblerMode.equals(Constants.custom_export_mode))
			{
				lAssembler = AssemblerFactory.getCustomAssembler(this);
				sLogger.debug("Processing XPDL with Custom components!");
			}				
			getHeader();
			getWflowProcessList();
	          // read and walk the WorkflowProcesses list
			if (wpList!=null) 
			{
					//sLogger.debug("WorkflowProcess extracted:"+wpList);						
				
					WorkflowProcessDocument.WorkflowProcess[] WorkflowProcess=wpList.getWorkflowProcessArray();
					sLogger.debug("Count of workflow processes: " + WorkflowProcess.length);
					for (int wf=0;wf<WorkflowProcess.length;wf++) 
					{
						String generationPass=(String)lConverterProperties.get("generation.pass");
						
							//curWP = WorkflowProcess[wf];
							handleWorkFlowProcess(WorkflowProcess[wf]);	
							//get the transitions list
							//TransitionsDocument.Transitions trans=WorkflowProcess[wf].getTransitions();
							handleTransitions(WorkflowProcess[wf].getTransitions());				
							exportToFile();						
							
							if(Integer.parseInt(generationPass)>1)
							{
								sLogger.debug("-----Generating call framework-----");
								lAssembler.generateFramework();
							}
					}
				}
			

		} 
		catch (Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	private void getHeader()
	{
		lPkg=lPkgDoc.getPackage();
		lPkgHeader=lPkg.getPackageHeader();			
		//sLogger.debug("Package Header:"+lPkgHeader);
	}
	private void getWflowProcessList()
	{
		wpList=lPkg.getWorkflowProcesses();		
	}
	private void handleWorkFlowProcess(WorkflowProcessDocument.WorkflowProcess pCurWP)
	{
		sLogger.debug("Process Name: " + pCurWP.getName() );
		// get the DataFields
		DataFieldsDocument.DataFields dfs =  pCurWP.getDataFields();
		if (dfs!=null) {
			DataFieldDocument.DataField[] df=dfs.getDataFieldArray();
			sLogger.debug("\tDatafield count: " + df.length );
			for (int dfI=0;dfI<df.length;dfI++)
				sLogger.debug("Datafield: " + df[dfI].getName() );
		}
		
		lAssembler.addBWProcessDefinition(pCurWP);		
		//get the activities list		 
		ActivitiesDocument.Activities acts=pCurWP.getActivities();
		if (acts!=null) 
		{
			//sLogger.debug("Activities Extracted:"+acts);
			ActivityDocument.Activity[] act=acts.getActivityArray();
			sLogger.debug("Activity Count:" + act.length );
			int activityCntr = 0;
			for (int actI=0;actI<act.length;actI++)
			{
				sLogger.debug("\tActivity Name: " + act[actI].getName() );
				if(activityCntr<act.length)
					lAssembler.addBWActivity(act[actI],false,activityCntr,act.length);
				else
					lAssembler.addBWActivity(act[actI],true,activityCntr,act.length);
					
				activityCntr++;
			}
			//lAssembler.adjustBindings();
		}
	}
	private void handleTransitions(Transitions pTransitions)
	{
		if (pTransitions!=null) 
		{
			//sLogger.debug("Transitions Extracted:"+pTransitions);
			TransitionDocument.Transition[] tran=pTransitions.getTransitionArray();
			sLogger.debug("Transition Count:" + tran.length );
			for (int tranI=0; tranI<tran.length; tranI++) {
				sLogger.debug("\tTransition Name: " + tran[tranI].getId()
					+ " From: " + tran[tranI].getFrom()
					+ " To: " + tran[tranI].getTo()
					+ " Name: " + tran[tranI].getName() );

				// determine transition type
				ConditionDocument.Condition cond=tran[tranI].getCondition();
				if (cond!=null)
					sLogger.debug("Condition Type:" + cond.getType() );
				lAssembler.addBWTransition(tran[tranI]);
			}
			lAssembler.adjustAllBindings();
		}
	}
		
	public Properties getConverterProperties()
	{
		return lConverterProperties;
	}
	public void exportToFile()
	{
		lAssembler.export();
		
	}
		
}
