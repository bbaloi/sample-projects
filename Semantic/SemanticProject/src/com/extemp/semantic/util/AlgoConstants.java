package com.extemp.semantic.util;

public class AlgoConstants 
{
	public static String route_opt="ROUTE_OPTIMISATION";
	public static String proximity_detection = "PROCIMITY_DETECTION";
	public static String impact_analysis = "IMPACT_ANALYSIS";
	
	public static String graphBaseURI="http://engage/ontology/graphanalyser";
	public static String graphNS=graphBaseURI+"#";	
	
	
	public static String graphTestNS = "http://www.co-ode.org/ontologies/ont.owl#";
	
	public static int threadPooliSize=10;
	
	public static String property_weight="Weight";
	public static String property_distance="Distance"; 
	public static String property_weather="Weather";
	public static String property_status="Status";	
	public static String property_action_substract="substract";
	public static String property_action_add="add";
		
	public static String values_status [] = new String [] {"OK","UNRESPONISSIVE", "ERROR"};
	public static String values_weather [] = new String [] {"SUNNY","RAIN", "SNOW","FOG"};
	
}
