/*
 * QueryEngine.java
 *
 * Created on May 22, 2003, 5:25 PM
 */

package com.perpetual.viewer.control.query;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.exception.BasePerpetualException;
import java.util.Collection;
/**
 *
 * @author  brunob
 */
public class BaseQueryEngine 
{
     private static PerpetualC2Logger sLog = new PerpetualC2Logger(BaseQueryEngine.class);
   
    
    /** Creates a new instance of QueryEngine */
    public BaseQueryEngine() {
    }
    
    public synchronized QueryResult parseQuery(String pQuery) 
    {
        sLog.debug("scrubbing the query !");
        //do the whole spiel and stuff the values retieved in a QueryREsult Object
        ///for right now thisi only a file - no parsing requried
        sLog.debug("Hey dude - this is only a file : need to do the real query parser !!!");
        FileQueryResult result = new FileQueryResult();
        result.SyslogFileName=pQuery;
        
        return result;
    }
    private void parseMessage()
    {
    }
    
    public Collection executeQuery(String pQuery) throws BasePerpetualException 
    {
        return null;
    }    
    
}
