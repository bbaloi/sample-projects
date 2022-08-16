/*
 * SummaryRecordVO.java
 *
 * Created on August 11, 2003, 8:56 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Collection;

/**
 *
 * @author  brunob
 */
public class SummaryRecordVO extends SingleIdVO
{
    private String hostName=null;
    private String domainName=null;
    private String vendorName=null;
    private String deviceType=null;
    private String startDate=null;
    private String endDate=null;
    private Collection facilityList=null;
    private Collection severityList=null;
    private String lMessagePatternName=null;
    
    /** Creates a new instance of SummaryRecordVO */
    public SummaryRecordVO(String pHostName,String pDomainName,String pVendorName,String pDeviceType,
                        String pStartDate,String pEndDate,Collection pFacilities,Collection pSeverities) 
    {
        hostName = pHostName;
        domainName = pDomainName;
        vendorName=pVendorName;
        deviceType=pDeviceType;
        startDate=pStartDate;
        endDate=pEndDate;
        facilityList=pFacilities;
        severityList=pSeverities;
       
    }
    public SummaryRecordVO(String pHostName,String pDomainName,String pMessagePatternName,
                        String pStartDate,String pEndDate,Collection pFacilities,Collection pSeverities) 
    {
        hostName = pHostName;
        domainName = pDomainName;
        lMessagePatternName=pMessagePatternName;
        startDate=pStartDate;
        endDate=pEndDate;
        facilityList=pFacilities;
        severityList=pSeverities;
    }
    public String getHostName()
    {
        return hostName;
    }
    public String getDomainName()
    {
        return domainName;
    }
    public String getVendorName()
    {
        return vendorName;
    }
    public String getDeviceType()
    {
        return deviceType;
    }
    public String getStartDate()
    {
        return startDate;
    }
    public String getEndDate()
    {
        return endDate;
    }
    public Collection getFacilityList()
    {
        return facilityList;
    }
    public Collection getSeverityList()
    {
        return severityList;
    }
    public String getMessagePattern()
    {
        return lMessagePatternName;
    }
}
