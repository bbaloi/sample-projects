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
public class CiscoMessagePattern extends MessagePattern
{
    
    /** Creates a new instance of SyslogCrondPattern */
    public CiscoMessagePattern(String pPattern) 
    {
        super(pPattern);
    }
    protected void init()
    {
       lPatternName="CiscoMessagePattern";
       lMessageType="Cisco";
       lParser = new CiscoMessageParser();
    }
    
}
