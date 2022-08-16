/*
 * DaoImpl.java
 *
 * Created on May 16, 2003, 3:51 PM
 */

package com.perpetual.viewer.model;
import java.util.Collection;
import com.perpetual.exception.BasePerpetualException;
import com.perpetual.viewer.control.query.BaseQueryEngine;


/**i
 *
 * @author  brunob
 */
public abstract class DaoImpl implements SyslogDAO
{
    protected BaseQueryEngine lQueryEngine=null; 
   
    
    /** Creates a new instance of DaoImpl */
    public DaoImpl() 
    {
    }
    
    public abstract Collection getData(String pQuery) throws BasePerpetualException;
            
}
