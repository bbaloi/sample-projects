/*
 * SummaryFilter.java
 *
 * Created on August 1, 2003, 4:50 PM
 */

package com.perpetual.viewer.control.query;

import java.util.HashMap;
import java.util.Map;
/**
 *
 *
 * @author  brunob
 */
public class SummaryFilter implements java.io.Serializable
{
    
    private HashMap lMap=null;
    /** Creates a new instance of SummaryFilter */
    public SummaryFilter()
    {
        lMap = new HashMap();
    }
    public SummaryFilter(HashMap pMap) 
    {
        lMap=pMap;
    }
    public void add(String key,Object pObj)
    {
        lMap.put(key,pObj);
        
    }
    public Object get( String key)
    {
        return lMap.get(key);
    }
    public Map getMap()
    {
        return lMap;
    }
    
}
