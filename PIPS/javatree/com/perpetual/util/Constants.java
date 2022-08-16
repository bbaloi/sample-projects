/*
 * Constants.java
 *
 * Created on June 28, 2003, 8:49 PM
 */

package com.perpetual.util;

/**
 *
 * @author  brunob
 */
public final class Constants 
{
    
    /** Creates a new instance of Constants */
    public Constants() 
    {
    }
    
    public static String jndiName_Login="perpetual/Login";
    public static String jndiName_User="perpetual/User";    
    public static String jndiName_Action="perpetual/Action";    
    public static String jndiName_UserProfile="perpetual/UserProfile";
    public static String jndiName_Role="perpetual/Role";
    public static String jndiName_RoleAction="perpetual/RoleAction";
    public static String jndiName_Domain="perpetual/Domain";
    public static String jndiName_DomainHost="perpetual/DomainHost";
    public static String jndiName_DomainFacility="perpetual/DomainFacility";
    public static String jndiName_DomainSeverity="perpetual/DomainSeverity";
    public static String jndiName_Severity="perpetual/Severity";
    public static String jndiName_Host="perpetual/Host";
    public static String jndiName_Facility="perpetual/Facility";
    public static String jndiName_DomainCRUD="perpetual/DomainCRUD";
    public static String jndiName_SummaryCRUD="perpetual/SummaryCRUD";
    public static String jndiName_Summary="perpetual/Summary";
	public static String jndiName_MessagePattern="MessagePattern";
    
    //------------------------------
    public static int FACILITY=1;
    public static int SEVERITY=2;
    public static int HOST=3;
    public static int DOMAIN=4;
	public static int MESSAGEPATTERN=5;
    
    //--------------DB related---------------
    public static String UserName = "username";
    public static String Password = "password";
    public static String DB_URL = "url";
    public static String DBDriver="driver";
    public static String PageSize="recordset.size";
    //-------------------------------------
    public static String HostList = "Host";
    public static String SeverityList = "Severity";
    public static String FacilityList = "Facility";
    public static String StartDate = "startDate";
    public static String EndDate = "endDate";
    public static String DomainList = "Domain";
    public static String VendorCriteria = "vendorCriteria";
    public static String SummaryLogicDescriptor = "logicDescriptor";
    public static String MessagePattern = "MessagePattern";
    //---------------------------------
    public static int NO_SELECTION=999;
    public static String NO_SELECTION_STR="999";
    //----------------Logstore cacche related-----------------------
    public static int CACHE_LRU_COLLECTION_INTERVAL=10000;
    public static int PERCENTAGE_LRU_BATCH=20;
    //------------------Scheduler Vars-----------
    public static String SCHEDULE_NAME="ScheduleObject";
    public static String SCHEDULE_DATA="ScheduleData";
    public static String  QUEUECONFAC ="java:/ConnectionFactory";
    //public static String  QUEUECONFAC ="ConnectionFactory";
    public static String SCHEDULE_QUEUE="queue/ScheduleQueue";
}
