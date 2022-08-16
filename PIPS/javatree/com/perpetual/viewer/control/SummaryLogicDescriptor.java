/*
 * SummaryLogicDescriptor.java
 *
 * Created on August 6, 2003, 5:27 PM
 */

package com.perpetual.viewer.control;

/**
 *
 * @author  brunob
 */
public class SummaryLogicDescriptor implements java.io.Serializable
{
    private boolean lFacilitiesAnd;
    private boolean lSeveritiesAnd;
    private boolean lFacilitiesAndSeverities;
    private boolean lFacilitiesAndMessages;
    private boolean lSeveritiesAndMessages;
    
    /** Creates a new instance of SummaryLogicDescriptor */
    public SummaryLogicDescriptor(boolean pFacilitiesAnd,boolean pSeveritiesAnd,boolean pFacilitiesAndSeverities,
                                    boolean pFacilitiesAndMessages,boolean pSeveritiesAndMessages)
    {
        lFacilitiesAnd=pFacilitiesAnd;
        lSeveritiesAnd=pSeveritiesAnd;
        lFacilitiesAndSeverities=pFacilitiesAndSeverities;
        lFacilitiesAndMessages=pFacilitiesAndMessages;
        lSeveritiesAndMessages=pSeveritiesAndMessages;
    }
    public SummaryLogicDescriptor(boolean pFacilitiesAnd,boolean pSeveritiesAnd,boolean pFacilitiesAndSeverities)
    {
        lFacilitiesAnd=pFacilitiesAnd;
        lSeveritiesAnd=pSeveritiesAnd;
         lFacilitiesAndSeverities=pFacilitiesAndSeverities;
    }
    
    public boolean getFacilitiesAnd()
    {
        return lFacilitiesAnd;
    }
    public boolean getSeveritiesAnd()
    {
        return lSeveritiesAnd;
    }
    public boolean getFacilitesOrSeverities()
    {
        return lFacilitiesAndSeverities;
    }
    public boolean getFacilitiesOrMessages()
    {
        return lFacilitiesAndMessages;
    }
    public boolean getSeveritiesOrMessages()
    {
        return lSeveritiesAndMessages;
    }
    
    
    
}
