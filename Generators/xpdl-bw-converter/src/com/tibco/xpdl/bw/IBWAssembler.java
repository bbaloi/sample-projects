package com.tibco.xpdl.bw;

import com.tibco.xpdl.bw.vo.JMSValuesVO;

import org.wfmc.x2002.xpdl10.ActivityDocument;
import org.wfmc.x2002.xpdl10.ConditionDocument;
import org.wfmc.x2002.xpdl10.TransitionDocument;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument;
import org.wfmc.x2002.xpdl10.ActivityDocument.Activity;
import org.wfmc.x2002.xpdl10.ConditionDocument.Condition;
import org.wfmc.x2002.xpdl10.TransitionDocument.Transition;
import org.wfmc.x2002.xpdl10.WorkflowProcessDocument.WorkflowProcess;
import com.tibco.xmlns.bw.process.x2003.ProcessDefinitionDocument.ProcessDefinition;

public interface IBWAssembler extends IFramework
{
	public void addJMSResource(JMSValuesVO pValues);
	public void addBWProcessDefinition(WorkflowProcess pDoc);
	public void addBWActivity(Activity pDoc,boolean pLast,int pActivityIndex,int pNumActivities);
	public void addBWTransition(Transition pDoc);
	public void addBWCondition(Condition pDoc);
	public void export();
	public void adjustBindings();
	public void adjustAllBindings();
	

}
