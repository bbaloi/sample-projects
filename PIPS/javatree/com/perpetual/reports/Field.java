/*
 * Field.java
 *
 * Created on October 2, 2003, 3:59 PM
 */

package com.perpetual.reports;

/**
 *
 * @author  brunob
 */
public final class Field
{
    private String lFieldName=null;
    private String lValue=null;
    
    /** Creates a new instance of Field */
    public Field(String pFieldName,String pValue)
    {
        lFieldName=pFieldName;
        lValue=pValue;
    }
    public String getFieldName()
    {
        return lFieldName;
    }
    public String getValue()
    {
        return lValue;
    }
    
}
