/*
 * DomainCriteriaMap.java
 *
 * Created on July 20, 2003, 10:06 PM
 */

package com.perpetual.rp.control;

import com.perpetual.util.PerpetualC2Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 *
 * @author  brunob
 */
public class DomainCriteriaMap
{
     private PerpetualC2Logger sLog = new PerpetualC2Logger( DomainCriteriaMap.class );    
   
    int lCollectionInterval=0;
    private String  lTimeUnit=null;
    private String lDomainName=null;
    private List lFacilityList=null;
    private List lSeverityList=null;
    private List lHostList=null;
    private TimeZone lTimeZone=null;
   
        
    /** Creates a new instance of DomainCriteriaMap */
    public DomainCriteriaMap(String pDomainName,int pCollectionInterval,String pTimeUnit,TimeZone pTimeZone,List pHostMap,List pFacilityMap, List pSeverityMap) 
    {
        lCollectionInterval = pCollectionInterval;
        lFacilityList = pFacilityMap;
        lSeverityList = pSeverityMap;
        lDomainName=pDomainName;
        lTimeUnit = pTimeUnit;
        lHostList = pHostMap;
        lTimeZone = pTimeZone; 
        sLog.debug("DomainCriteriaMap for domain:"+lDomainName);
    }
    public String getDomainName()
    {
        return lDomainName;
    }
    
    public int getCollectionInterval()
    {
        return lCollectionInterval;
    }
    public String getTimeUnit()
    {
        return lTimeUnit;
    }
    public List getMeasurableFacilities()
    {
        return lFacilityList;
    }
    public List getMeasurableSeverities()
    {
        return lSeverityList;
    }
    public List getHostList()
    {
        return lHostList;
    }
    public TimeZone getTimeZone()
    {
        return lTimeZone;
    }
}
