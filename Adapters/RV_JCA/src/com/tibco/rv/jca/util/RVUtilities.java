/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.util;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.tibco.rv.jca.util.Constants;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RVUtilities 
{

	//	parse the comma delimited string & create an array
	
	public static List getPropertyList(String pPropertyList,Logger pLogger)
	{
		ArrayList propList=null;
		StringTokenizer tokenizer = new StringTokenizer(pPropertyList,Constants.DELIMITER);
		int tokens = tokenizer.countTokens();
		pLogger.log(Level.INFO,"Value to parse:"+pPropertyList);
		pLogger.log(Level.INFO,"Num tokens:"+tokens);
		
		if(pPropertyList!=null)			
		{
			propList = new ArrayList();
			
					while(tokenizer.hasMoreTokens())
					{
						String token = tokenizer.nextToken();
						propList.add(token);
					}
				
		}
		return propList;
		
	}

}
