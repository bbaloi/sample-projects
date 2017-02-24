package com.tibco.stax.tests;

import com.tibco.stax.parser.*;

public class TibcoStaxParserTest 
{
	 
	 private static String lInputFileName,lPropertiesFileName;
	 public static void main(String[] args) 
	 {
		 validateInput(args);
		 
		 try
		 {
			//System.out.println("Parsing file:"+args[0]);
		    int records=TibcoStaxParser.parse(lInputFileName,lPropertiesFileName);
		   // System.out.println("Records Processed:"+records);
		 }
		 catch(Exception excp)
		 {
			 excp.printStackTrace();
		 }
		  
	 }
	 public static void validateInput(String args[])
	 {
		 //System.out.println("Num parms:"+args.length);
		if(args.length<4 || args.length>4)
		{
			System.out.println("Invlid number of arguments !");
			System.out.println("Proper arguments are: -input_file <IputFileName> -properties_file <PropertiesFileName>");
			System.exit(1);
		}
		else
		{
			lInputFileName=(String) args[1];
			lPropertiesFileName= (String) args[3];
			
		}
			
	 }

}
