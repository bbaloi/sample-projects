/*
 * SyslogMailPattern.java
 *
 * Created on September 17, 2003, 11:16 AM
 */

package com.perpetual.util.patternmatcher.syslog;

import com.perpetual.util.patternmatcher.MessagePattern;

/**
 *
 * @author  brunob
 */
public class MailMessagePattern extends MessagePattern
{
    
    /** Creates a new instance of SyslogMailPattern */
    public MailMessagePattern(String pPattern) 
    {
        super(pPattern);
    }
     protected void init()
    {
       lPatternName="SyslogMailPattern";
       lMessageType="SyslogMailMessage";
       lParser = new MailMessageParser();
    
    }
}
