/*
 * Header.java
 *
 * Created on October 2, 2003, 4:29 PM
 */

package com.perpetual.reports;

import java.util.Date;
import java.util.List;

/**
 *
 * @author  brunob
 */
public final class Header 
{
    private String lName;
    private Date lStartDate=null;
    private Date lEndDate=null;
    private Date lWhenDate=null;
    private List lHostList=null;
    private List  lSeverityList=null;
    private List  lFacilityList;
    private List  lMessagePatternList=null;
    
    /** Creates a new instance of Header */
    public Header(String pName,Date pStartDate,Date pEndDate,Date pWhenDate,List pHosts,List pSeverities,
                    List pFacilities, List pMessagePatterns)
    {
        lName = pName;
        lStartDate = pStartDate;
        lEndDate = pEndDate;
        lWhenDate=pWhenDate;
        lHostList = pHosts;
        lSeverityList=pSeverities;
        lFacilityList = pFacilities;
        lMessagePatternList = pMessagePatterns;
        
    }
    public String getName()
    {
        return lName;
    }
    public Date getStartDate()
    {
        return lStartDate;
    }
    public Date getEndDate()
    {
        return lEndDate;
    }
    public Date getWhenDate()
    {
        return lWhenDate;
    }
    public String [] getHosts()
    {
        return (String[]) lHostList.toArray(new String[lHostList.size()]);       
    }
    public String [] getFacilities()
    {
     return (String[]) lFacilityList.toArray(new String[lFacilityList.size()]);   
    }
    public String [] getSeverities()
    {
        return (String[]) lSeverityList.toArray(new String[lSeverityList.size()]);
    }
    public String [] getMessagePatterns()
    {
        return (String[]) lMessagePatternList.toArray(new String[lMessagePatternList.size()]);
    }
    
}

