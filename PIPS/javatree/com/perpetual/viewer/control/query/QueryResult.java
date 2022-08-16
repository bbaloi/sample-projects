/*
 * QueryResult.java
 *
 * Created on May 22, 2003, 5:26 PM
 */

package com.perpetual.viewer.control.query;
import java.util.Date;
/**
 *
 * @author  brunob
 */
public abstract class QueryResult 
{    
    public Date startDate=null;
    public Date endDate=null;
    public String process=null;
    public String host=null;
    /** Creates a new instance of QueryResult */
    public QueryResult() {
    }
    
}
