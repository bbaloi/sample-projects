/*
 * QueryTemplate.java
 *
 * Created on July 22, 2003, 9:05 PM
 */

package com.perpetual.rp.control.query;

import java.util.HashMap;
import java.util.Date;
import java.util.List;

/**
 *
 * @author  brunob
 */
public class SummaryQueryTemplate
{
    private Date lStartDate=null;
    private Date lEndDate=null;
    private List lFacilityList=null;
    private List lSeverityList=null;
    private List lHostList=null;
    private VendorQueryTemplate lVendorTemplate=null;
       
    /** Creates a new instance of Template */
    public SummaryQueryTemplate(Date pStartDate,Date pEndDate,
                    List pFacilityList,List pSeverityList,List pHostList,VendorQueryTemplate pVendorTemplate) 
    {
        lStartDate = pStartDate;
        lEndDate=pEndDate;
        lFacilityList=pFacilityList;
        lSeverityList=pSeverityList;
        lHostList=pHostList;
        lVendorTemplate = pVendorTemplate;
        
    }
    public Date getStartDate()
    {
        return lStartDate;
    }
     public Date getEndDate()
    {
        return lEndDate;
    }
    public List getHostList()
    {
        return lHostList;
    }
    public List getFacilityList()
    {
        return lFacilityList;
    }
    public List getSeverityList()
    {
        return lSeverityList;
    }
    public VendorQueryTemplate getVendorTemplate()
    {
        return lVendorTemplate;
    }
}
