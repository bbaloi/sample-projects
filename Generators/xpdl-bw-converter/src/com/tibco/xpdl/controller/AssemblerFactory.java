package com.tibco.xpdl.controller;

import com.tibco.xpdl.bw.IBWAssembler;
import com.tibco.xpdl.bw.marshall.custom.BWCustomAssembler;
import com.tibco.xpdl.bw.marshall.xmlbeans.BWXmlbeansAssembler;

public class AssemblerFactory 
{
	public static IBWAssembler getXMLBeanAssembler(IBWBuilderClient pBuilder)
	{
		return new BWXmlbeansAssembler(pBuilder);
		
	}
	public static IBWAssembler getCustomAssembler(IBWBuilderClient pBuilder)
	{
		return new BWCustomAssembler(pBuilder);
	}
}
