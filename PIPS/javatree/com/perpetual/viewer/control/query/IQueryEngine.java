/*
 * IQueryEngine.java
 *
 * Created on May 22, 2003, 5:24 PM
 */

package com.perpetual.viewer.control.query;

import java.util.Collection;
import com.perpetual.exception.BasePerpetualException;

import java.util.Map;
/**
 *
 * @author  brunob
 */
public interface IQueryEngine extends java.io.Serializable
{
  //public Collection executeQuery(String pQuery,Map pParameters) throws BasePerpetualException;    
  public SummaryCursor executeQuery(String pQuery,Map pParameters) throws BasePerpetualException;    
    
}
