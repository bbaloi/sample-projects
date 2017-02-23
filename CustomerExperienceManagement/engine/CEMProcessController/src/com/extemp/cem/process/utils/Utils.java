package com.extemp.cem.process.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.extemp.cem.process.ControlProcess;
import com.extemp.cem.process.JMSController;

public class Utils 
{
	private static Utils instance=null;
	private JMSController _jms_controller=null;
	private boolean _log_to_console=true;
	private int _wait_for_process_end_milis=10000;
	
	public JMSController get_jms_controller() {
		return _jms_controller;
	}
	public void set_jms_controller(JMSController _jms_controller) {
		this._jms_controller = _jms_controller;
	}
	
	private HashMap _propsFileConfig=null;
	
	
	public static Utils getInstance()
	{
		if(instance==null)
			instance = new Utils();
		return instance;
	}
	public Utils()
	{
		
	}
	public HashMap loadPropertiesFile(String pPropsFile)
	{
		String _processName=null,_processExec=null,_processExecStop=null,_status=null,_startPhrase=null,_endPhrase=null,_waitForEnd;
		
		_propsFileConfig=new HashMap();
		
		try
		{
			Document _propsDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(pPropsFile));
			Node _rootNode = _propsDoc.getFirstChild();
			System.out.println("Root node:"+_rootNode.getNodeName());
			NodeList _processList = _rootNode.getChildNodes();
			
			int listLen = _processList.getLength();
			for(int i=0;i<listLen;i++)
			{
				Node _node = _processList.item(i);
				String _procName = _node.getNodeName();
				if(_procName.equals("Process"))
				{
					ControlProcess _newProcess = new ControlProcess();					
					
					NodeList _procKids = _node.getChildNodes();
					int _len = _procKids.getLength();
					for (int x=0;x<_len;x++)
					{
						Node _ProcNode = _procKids.item(x);
						String _nodeName = _ProcNode.getNodeName();
						if(_nodeName.equals(Constants.ProcessName))
						{
							_processName = _ProcNode.getTextContent();
							_newProcess.set_processName(_processName);
						}
						if(_nodeName.equals(Constants.Executable))
						{
							_processExec = _ProcNode.getTextContent();
							_newProcess.set_processExecutable(_processExec);
						}
						if(_nodeName.equals(Constants.ExecutableStop))
						{
							_processExecStop = _ProcNode.getTextContent();
							_newProcess.set_processExecutableStop(_processExecStop);
						}
						if(_nodeName.equals(Constants.Status))
						{
							_status = _ProcNode.getTextContent();
							_newProcess.set_status(_status);
						}
						if(_nodeName.equals(Constants.StartPhrase))
						{
							_startPhrase = _ProcNode.getTextContent();
							_newProcess.set_start_phrase(_startPhrase);
						}
						if(_nodeName.equals(Constants.EndPhrase))
						{
							_endPhrase = _ProcNode.getTextContent();
							_newProcess.set_end_phrase(_endPhrase);
						}
						if(_nodeName.equals(Constants.WaitForEnd))
						{
							_waitForEnd = _ProcNode.getTextContent();
							_newProcess.set_wait_for_end(Long.parseLong(_waitForEnd ));
						}
						
						
						if(_nodeName.equals(Constants.AttributeList))
						{
							NodeList _attrList = _ProcNode.getChildNodes();
							int _attrListLen = _attrList .getLength();
							//System.out.println("# atributes:"+_attrListLen);
							HashMap _attrMap = new HashMap();
							
							for (int z=0;z<_attrListLen;z++)
							{
								Node _attr = _attrList.item(z);
								if(_attr.getNodeName().equals("process_attribute"))
								{					
									NodeList _attrs = _attr.getChildNodes();
									//System.out.println("attr kids:"+_attrs.getLength());
									String _attrName = _attrs.item(1).getTextContent();
									String _attrVal =  _attrs.item(3).getTextContent();
									System.out.println(_attrName+"-"+_attrVal);
									_attrMap.put(_attrName, _attrVal);
								}
							}
							
							_newProcess.set_attributeList(_attrMap);
						}						
					}
					
					_propsFileConfig.put(_processName, _newProcess);
				}
				if(_procName.equals("jms"))
				{
					_jms_controller = new JMSController();
					
					NodeList _jmsKids = _node.getChildNodes();
					int _jlen = _jmsKids.getLength();
					
					for(int j=0;j<_jlen;j++)
					{
						Node _jmsNode = _jmsKids.item(j);
						String _nodeName = _jmsNode.getNodeName();
						if(_nodeName.equals(Constants.jms_connect))
						{
							_jms_controller.set_jms_url(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_user))
						{
							_jms_controller.set_user(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_pass))
						{
							_jms_controller.set_pass(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_in_dest_name))
						{
							_jms_controller.set_in_destination_name(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_in_dest_type))
						{
							_jms_controller.set_in_destination_type(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_out_dest_name))
						{
							_jms_controller.set_out_destination_name(_jmsNode.getTextContent());
						}
						if(_nodeName.equals(Constants.jms_out_dest_type))
						{
							_jms_controller.set_out_destination_type(_jmsNode.getTextContent());
						}
					}
					
				}
				if(_procName.equals("log_to_console"))
				{
					String _val = _node.getTextContent();
				
					if(_val.equals("true"))
						_log_to_console=true;
					else
						_log_to_console=false;
					
				}
				if(_procName.equals("wait_for_process_end_milis"))
				{
					String _val = _node.getTextContent();
					_wait_for_process_end_milis=Integer.parseInt(_val);
				}
				
			}
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
			
		return _propsFileConfig;		
	}
	public void displayConfig(HashMap pConfig)
	{
		
		System.out.println("----JMS properties-----------");
		System.out.println("+++URL:"+_jms_controller.get_jms_url()+", user/pass:"+_jms_controller.get_user()+"/"+_jms_controller.get_pass());
		System.out.println("+++in_dest:"+_jms_controller.get_in_destination_name()+"-"+_jms_controller.get_in_destination_type());
		System.out.println("+++ou_dest:"+_jms_controller.get_out_destination_name()+"-"+_jms_controller.get_out_destination_type());
		System.out.println("### log to console:"+_log_to_console);
		System.out.println("### wait for proc end:"+_wait_for_process_end_milis);
		
		
		Set _keySet = pConfig.keySet();
		Iterator _it = _keySet.iterator();
		while(_it.hasNext())
		{
			String _processName = (String)_it.next();
			ControlProcess _ctrlProc = (ControlProcess) pConfig.get(_processName);
			System.out.println("---ProcName:"+_ctrlProc.get_processName()+",Exec:"+_ctrlProc.get_processExecutable()+",status:"+_ctrlProc.get_status()+",start_phrase:"+_ctrlProc.get_start_phrase()+",end_phrase:"+_ctrlProc.get_end_phrase());
			HashMap _attrs = _ctrlProc.get_attributeList();
			Set _attrSet = _attrs.keySet();
			Iterator _attrIt =_attrSet.iterator();
			while(_attrIt.hasNext())
			{
				String _attr = (String)_attrIt.next();
				String _attrVal = (String)_attrs.get(_attr);
				System.out.println("   +++attribute:name="+_attr+", value="+_attrVal);
			}
			
		}
		
		
		
	}
	public HashMap get_propsFileConfig() {
		return _propsFileConfig;
	}
	public boolean is_log_to_console() {
		return _log_to_console;
	}
	public void set_log_to_console(boolean _log_to_console) {
		this._log_to_console = _log_to_console;
	}
	public int get_wait_for_process_end_milis() {
		return _wait_for_process_end_milis;
	}
	public void set_wait_for_process_end_milis(int _wait_for_process_end_milis) {
		this._wait_for_process_end_milis = _wait_for_process_end_milis;
	}
}
