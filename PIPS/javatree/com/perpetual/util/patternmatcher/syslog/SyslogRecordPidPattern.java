/*
 * SyslogPattern.java
 *
 * Created on September 17, 2003, 11:15 AM
 */

package com.perpetual.util.patternmatcher.syslog;

import com.perpetual.util.patternmatcher.MessagePattern;

/**
 *
 * @author  brunob
 */
public class SyslogRecordPidPattern extends MessagePattern
{
    
    /** Creates a new instance of SyslogPattern */
    public SyslogRecordPidPattern(String pPattern) 
    {    
        super(pPattern);
    }
    protected void init()
    {
       lPatternName="SyslogRecordWithPID";
       lParser = new SyslogRecordPidParser();
    
    }
    
}
