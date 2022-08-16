/*
 * QueryFormatter.java
 *
 * Created on May 16, 2003, 3:58 PM
 */

package com.perpetual.viewer.control.query;

import java.util.HashMap;
import com.perpetual.util.PerpetualC2Logger;
import java.util.Map;
import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */

/*need to determine whether these params are only for standard logging
 *i.e. -startDate/endDatents 
 *     -process
 *     -hostname
 *     
 *and for the Message
 *      will need to build specific Qery types 
 *      any parmaerets passed in other than the standard SYslog will have to 
 *      be treated specifically....CISCO type params, Nokia params etc.
 *
 *    Example
 *      get all messages wheream
 *          date between x and Y &
 *          processs=Z
 *          hostname=B
 *          & Message.param1='ABC' | Message.param2="xyz"
 *
 *
 *

*/

public abstract class AbstractQueryBuilder implements IQueryBuilder
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( AbstractQueryBuilder.class );
    protected AbstractMessageQueryBuilder lMessageBuilder=null;
       
    /** Creates a new instance of QueryFormatter */
    public AbstractQueryBuilder() 
    {
     }
   public String constructQuery(String pFileName, HashMap pQueryParameters) 
   {
       
       if(pQueryParameters==null)
       {
        sLog.debug("There are no parameters  - returning just the log file name");
        return pFileName;
       }
       return null;
    }
    public Object [] getQueryParameters(Map pMap) 
    {
        return null;
    }    
   public String constructQuery(Map pMap,Map pExecutionParamters) throws BasePerpetualException
   {
       return null;
   }
    
}
