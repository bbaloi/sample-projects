package com.tibco.xpdl.controller;

import java.util.Properties;

import com.tibco.xpdl.exceptions.XPDLBWException;
import org.wfmc.x2002.xpdl10.*;

public interface IBWBuilder 
{
	public void buildBWProcessDefinition(PackageDocument pDoc,Properties pConverterProperties) throws XPDLBWException;
}
