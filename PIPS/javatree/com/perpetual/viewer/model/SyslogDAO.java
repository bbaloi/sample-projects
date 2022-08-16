/*
 * SyslogDAO.java
 *
 * Created on May 16, 2003, 3:08 PM
 */

package com.perpetual.viewer.model;

import java.util.Collection;
import com.perpetual.exception.BasePerpetualException;


/**
 *
 * @author  brunob
 */
public interface SyslogDAO
{
  
    public Collection getData(String pQuery) throws BasePerpetualException;    
    
}
