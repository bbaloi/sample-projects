package com.tibco.xpdl.bw.marshall.custom;

import org.wfmc.x2002.xpdl10.ActivityDocument;
import org.wfmc.x2002.xpdl10.ConditionDocument;
import org.wfmc.x2002.xpdl10.TransitionDocument;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument;
import org.wfmc.x2002.xpdl10.ActivityDocument.Activity;
import org.wfmc.x2002.xpdl10.ConditionDocument.Condition;
import org.wfmc.x2002.xpdl10.TransitionDocument.Transition;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument.WorkflowProcess;

import com.tibco.xpdl.bw.IBWAssembler;
import com.tibco.xpdl.bw.vo.JMSValuesVO;
import com.tibco.xpdl.controller.BWAbstractAssembler;
import com.tibco.xpdl.controller.IBWBuilderClient;

public class BWCustomAssembler extends BWAbstractAssembler implements IBWAssembler
{
	public BWCustomAssembler(IBWBuilderClient pClient)
	{
		super(pClient);
		
	}
	public void addJMSResource(JMSValuesVO pValues)
	{
		
	}
	public void addBWProcessDefinition(WorkflowProcess pDoc)
	{
		
	}
	public void addBWActivity(Activity pDoc,boolean pLast,int pActivityIndex,int pNumActivities)
	{
		
	}
	public void addBWTransition(Transition pDoc)
	{
		
	}
	public void addBWCondition(Condition pDoc)
	{
		
	}
	public void export()
	{
		
	}
	protected void createJMSReceiver()
	{
		
	}
	protected void createProcDefintion()
	{
		
	}
	protected  void createProcCall()
	{
		
	}
	protected void writeToFile()
	{
		
	}
	protected void addTransitions()
	{
		
	}
	protected void createEndPoint()
	{
		
	}

}
