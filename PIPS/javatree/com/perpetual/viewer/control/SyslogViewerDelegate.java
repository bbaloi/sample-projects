/*
 * SyslogViewerDelegate.java
 *
 * Created on May 16, 2003, 3:09 PM
 */

package com.perpetual.viewer.control;

import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author  brunob
 */
public interface SyslogViewerDelegate 
{
    
    /** Creates a new instance of SyslogViewerDelegate */
    Collection readSyslog(String pFileName,HashMap pQueryParameters) throws Exception;
   
}
