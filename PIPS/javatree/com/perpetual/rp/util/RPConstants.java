/*
 * PerformanceConstants.java
 *
 * Created on January 2, 2003, 6:32 PM
 */

package com.perpetual.rp.util;

import java.util.Properties;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.PerpetualPropertiesHandler;
import javax.naming.InitialContext;
/**
 *
 * @author  brunob
 */
public final class RPConstants 
{
   private static PerpetualC2Logger sLog = new PerpetualC2Logger( RPConstants.class );    
     
    private static Properties	sProperties	= null;
    private static Properties   jndiProperties =null;
    private static InitialContext intialContext=null;
    private static RPConstants lRPConstants=null;
    public static String CollectionTemplate=null;
    public static int ThreadPoolInitSize=0;
    public static int ThreadPoolMaxSize=0;
    public static long RefreshInterval;
    public static long LagTime = 0;
    private static final String PROPERTIES_FILENAME = "rp.properties";
    private static final String JNDI_PROPERTIES_FILENAME = "jndi.properties";
    
    public static  String SECOND    = "second";
    public static  String MINUTE    = "minute";
    public static  String HOUR      = "hour";
    public static  String DAY       = "day";
    public static  String WEEK      = "week";
    public static  String MONTH     = "month";
    public static  String YEAR      = "year";
    //-----------Facilities-----------------    
    public static String Kernel = "0";
    public static String User="1";
    public static String Mail="2";
    public static String Daemon="3";
    public static String Security="4";
    public static String Syslog="5";
    public static String Lpr="6";
    public static String News="7";
    public static String UUCP="8";
    public static String Crond="9";
    public static String Authority="10";
    public static String FTP="11";
    public static String NTP="12";
    public static String Audit="13";
    public static String Alert="14";
    public static String _Crond="15";
    public static String Local0="16";
    public static String Local1="17";
    public static String Local2="18";
    public static String Local3="19";
    public static String Local4="20";
    public static String Local5="21";
    public static String Local6="22";
    public static String Local7="23";
    //-----------Severities-----------------
     public static String  Emergency="0";
     public static String  Severity_Alert="1";
     public static String  Critical="2";
     public static String  Error="3";
     public static String  Warning="4";
     public static String  Notice="5";
     public static String  Info="6";
     public static String  Debug="7";      
    
    
    static
    {
        
        try
		{
                    if(sProperties==null)
                    {
                        String perpetualHome=System.getProperty("perpetualhome");
                        sLog.debug("PerpetualHome:"+perpetualHome);
                        String propertiesPath=perpetualHome+"/config/"+PROPERTIES_FILENAME;
                        sProperties = PerpetualPropertiesHandler.getPropertyFile(propertiesPath );
                    //jndiProperties = PerpetualPropertiesHandler.getPropertyFile( JNDI_PROPERTIES_FILENAME );
                        init(sProperties);
                    //initJndi(jndiProperties);
                    }
		}
		catch( Exception e )
		{
			sLog.error( "Failed to load config file in static block...", e );
		}
    }
    
    public static void init(Properties pProperties)
    {
        CollectionTemplate = pProperties.getProperty("rp.collection.template");
        ThreadPoolInitSize = new Integer( pProperties.getProperty("rp.threadpool.init.size")).intValue();
        ThreadPoolMaxSize = new Integer( pProperties.getProperty("rp.threadpool.max.size")).intValue();
        RefreshInterval = new Long( pProperties.getProperty("rp.db.refresh.interval")).longValue();
        LagTime = new Long(pProperties.getProperty("rp.lag.time")).longValue();
    }
    public static void initJndi(Properties pProperties)
    {
        //initialContext = new InitialContext();
    }
    
     public static RPConstants getInstance()
    {        
        if(lRPConstants==null)
            lRPConstants = new RPConstants();
        return lRPConstants;
    }
     
     
}
