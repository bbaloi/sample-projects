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
public class XinetdMessagePattern extends MessagePattern
{
    
    /** Creates a new instance of SyslogCrondPattern */
    public XinetdMessagePattern(String pPattern) 
    {
        super(pPattern);
    }
    protected void init()
    {
       lPatternName="XinetdMessagePattern";
       lMessageType="Xinetd";
       lParser = new XinetdMessageParser();
    }
    
}
