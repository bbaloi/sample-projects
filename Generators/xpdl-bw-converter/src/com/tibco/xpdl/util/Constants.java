package com.tibco.xpdl.util;

public interface Constants 
{
	public String xmlbeans_export_mode="xmlbeans";
	public String custom_export_mode="custom";
	public String jms_queue_receiver="JMS_QUEUE_RECEIVER";
	public String activity_type="_ActivityType";
		
	public String jmsReceiverType="com.tibco.plugin.jms.JMSQueueEventSource";
	public String jmsReceiverResourceType = "ae.activities.JMSQueueEventSource";
	//public String jms_xml_type="XML Text";
	public String jms_xml_type="Text";
	public String params="_Parameters";
	public String set_properties="_SetProperties";
	public String output="_Output";
	public String input="_Input";
	public int xOffset=200;
	public int yOffset=100;
	public String activity_process_call="PROCESS_CALL";
	public String process_call_type="com.tibco.pe.core.CallProcessActivity";
	public String process_call_resource_type="ae.process.subprocess";
	public String xpd = "XPD";
	public String activity_end="END";
	public String activity_start="START";
	//-------------input-output-types-------
	public String jms_request="JMS_REQUEST";
	public String jms_reponse="JMS_RESPONSE";
	
	//------------------------------------------
	public String dynamic_var="dynamicVar";
	public String static_var="staticVar";
	public int proc_header_length=1000;
	///--------famework generation----
	public String framework_jms_queue_output="JMSMessageIn";
	public String framework_proc_output="ProcOut";
	//-----------------------------
	public String condition_activity="CONDITION";
	public String null_type="com.tibco.plugin.timer.NullActivity";
	public String null_resource_type="ae.activities.null";
	//----------conditions related---------
	public String condition_criteria="TransitionSimulationData";

}
	
