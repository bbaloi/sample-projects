/*
 * SummaryRecord.java
 *
 * Created on August 6, 2003, 9:59 PM
 */

package com.perpetual.viewer.control.query;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  brunob
 */
public class SummaryRecord implements java.io.Serializable
{
    private HashMap recordMap=null;
    /** Creates a new instance of SummaryRecord */
    public SummaryRecord() 
    {       
        recordMap = new HashMap();
    }
        
    public SummaryRecord(HashMap pMap) 
    {
        recordMap=pMap;
    }
    public void add(String key,Object pObj)
    {
        recordMap.put(key,pObj);
        
    }
    public Object get( String key)
    {
        return recordMap.get(key);
    }
    public Map getMap()
    {
        return recordMap;
    }
    
}
