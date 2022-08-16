/*
 * IQueryBuilder.java
 *
 * Created on August 5, 2003, 5:53 PM
 */

package com.perpetual.viewer.control.query;

import java.util.Map;
import com.perpetual.exception.BasePerpetualException;

/**
 *
 * @author  brunob
 */
public interface IQueryBuilder extends java.io.Serializable
{
    public String constructQuery(Map pMap,Map pExecutionParamters) throws BasePerpetualException;
       
}
