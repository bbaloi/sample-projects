package com.extemp.cem.util;

public class CEMConstants 
{
	//-------------------cem property names-----------
	public static String ontFileProp = "cem.customer.ontology.location";
	public static String reasonerTypeProp ="cem.owl.reasoner"; 
	public static String customerProfileClass ="CustomerProfileClass";
	//------------------OWL related----------
	public static String owlRdfsReasoner="RDFS_MEM_RDFS_INF";
	public static String owlReasoner="OWL_MEM_RULE_INF";
	public static String personBaseURI="http://engage/ontology/person360";
	public static String personNS=personBaseURI+"#";
	public static String productBaseURI="http://engage/ontology/product";
	public static String productNS=productBaseURI+"#";
	public static String baseOntBaseURI="http://engage/ontology/base";
	public static String baseOntNS=baseOntBaseURI+"#";	
	//---------------------ont-predicates-----
	public static String has = "Has";
	public static String likes="Likes";
	//--------------persistence options-----------
	public static String write_DB="db";
	public static String write_File="file";
	public static String write_Cache="cache";
	

}
