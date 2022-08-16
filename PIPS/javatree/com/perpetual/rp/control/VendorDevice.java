/*
 * VendorDevice.java
 *
 * Created on July 20, 2003, 11:39 PM
 */

package com.perpetual.rp.control;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  brunob
 */
public class VendorDevice 
{
    private String lHostName=null;
    private String lVendorName=null;
    private String lDeviceType=null;
    private List lCriteriaList=null;
    
    /** Creates a new instance of VendorDevice */
    public VendorDevice(String pHostName,String pVendorName,String pDeviceType,List pCriteriaList) 
    {
        lHostName=pHostName;
        lVendorName = pVendorName;
        lDeviceType=pDeviceType;
        lCriteriaList=pCriteriaList;
        
    }
    public String getHostName()
    {
        return lHostName;
    }
    public String getVendorName()
    {
        return lHostName;
    }
    public String getDeviceType()
    {
        return lDeviceType;
    }
    public List getVendorCriteria()
    {
        return lCriteriaList;
    }
    
}
