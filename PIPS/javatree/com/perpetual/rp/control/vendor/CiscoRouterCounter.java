/*
 * CiscoRouter.java
 *
 * Created on July 23, 2003, 11:13 PM
 */

package com.perpetual.rp.control.vendor;

import java.util.List;

/**
 *
 * @author  brunob
 */
public class CiscoRouterCounter extends VendorCounter
{
    private List lCriterionList=null;
    
    /** Creates a new instance of CiscoRouter */
    public CiscoRouterCounter(List pCriterionList) 
    {
        lCriterionList = pCriterionList;
    }
    
}
