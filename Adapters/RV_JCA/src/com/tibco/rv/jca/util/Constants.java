/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tibco.rv.jca.util;

/**
 * @author bbaloi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Constants 
{
	public static final String RV_DAEMON="RV_DAEMON";
	public static final String RV_MODE="RV_MODE";
	public static final String RV_SERVICE="RV_SERVICE";
	public static final String RV_NETWORK="RV_NETWORK";
	public static final String RV_SUBJECT="RV_SUBJECT";
	public static final String RV_DQ_WEIGHT="RV_DQ_WEIGHT"; 	
	public static final String RV_DQ_TASKS="RV_DQ_TASKS"; 
	public static final String RV_DQ_SCHEDULER_WEIGHT="RV_DQ_SCHEDULER_WEIGHT"; 
	public static final String RV_DQ_SCHEDULER_HEARTBEAT="RV_DQ_SCHEDULER_HEARTBEAT"; 
	public static final String RV_DQ_SCHEDULER_ACTIVATION="RV_DQ_SCHEDULER_ACTIVATION"; 
	public static final String RV_FT_GROUP="RV_FT_GROUP"; 
	public static final String RV_FT_WEIGHT="RV_FT_WEIGHT"; 
	public static final String RV_FT_ACTIVE="RV_FT_ACTIVE"; 
	public static final String RV_FT_ACTIVE_GOAL="RV_FT_ACTIVE_GOAL"; 
	public static final String RV_FT_HEARTBEAT="RV_FT_HEARTBEAT"; 
	public static final String RV_FT_PREP_INTERVAL="RV_FT_PREP_INTERVAL";
	public static final String RV_FT_ACTIVATION_INTERVAL="RV_FT_ACTIVATION_INTERVAL"; 
	
	public static final String NORMAL_MODE="norm";
	public static final String FT_MODE="ft";
	public static final String DQ_MODE="dq";
	
	public static final long WORK_STARTUP_TIMEOUT=5000;
	
	public static double DEFAULT_DQ_SCHEDULER_ACTIVATION=3.5;
	public static double DEFAULT_DQ_SCHEDULER_HEARTBEAT=1.0;
	public static int DEFAULT_DQ_WEIGHT=1;
	public static int DEFAULT_DQ_SCHEDULER_WEIGHT=1;
	public static int DEFAULT_DQ_TASKS=1;
	public static boolean DEFAULT_FT_ACTIVE=false;
	public static String FT_ACTIVE_TRUE="true";
	public static int DEFAULT_FT_WEIGHT=1;
	public static int DEFAULT_FT_ACTIVE_GOAL=1;
	public static double DEFAULT_FT_HEARTBEAT=1;
	public static double DEFAULT_FT_PREP_INTERVAL=0;
    public static double DEFAULT_FT_ACTIVATION_INTERVAL=2;
	
	public static String DELIMITER=",";
	public static int FT_ACTIVE_VALUE=999;
	
	public static String DEFAULT_RV_SERVICE="7500";
	public static String DEFAULT_RV_NETWORK="";
	public static String DEFAULT_RV_DAEMON="tcp:7500";
	
	public static int FT_TIME_DISPATCH=999;
	
	public static String EIS_PRODUCT_NAME="TIBCO Rendezvous JCA Adapter";
	public static String EIS_PRODUCT_VERSION="1.0";
	public static int EIS_MAX_CONNECTIONS=10;
	
	
		
	
	
	

}
