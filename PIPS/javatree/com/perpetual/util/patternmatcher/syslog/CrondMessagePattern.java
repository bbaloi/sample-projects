/*
 * SyslogCrondPattern.java
 *
 * Created on September 17, 2003, 12:03 PM
 */

package com.perpetual.util.patternmatcher.syslog;

import com.perpetual.util.patternmatcher.MessagePattern;
/**
 *
 * @author  brunob
 */
public class CrondMessagePattern extends MessagePattern
{
    
    /** Creates a new instance of SyslogCrondPattern */
    public CrondMessagePattern(String pPattern) 
    {
        super(pPattern);
    }
    protected void init()
    {
       lPatternName="SyslogCrondPattern";
       lMessageType="SyslogCrondMessage";
       lParser = new CrondMessageParser();
    }
    
}
