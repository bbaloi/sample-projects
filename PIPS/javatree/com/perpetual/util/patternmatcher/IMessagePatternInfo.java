/*
 * IPatternInfo.java
 *
 * Created on September 17, 2003, 11:33 AM
 */

package com.perpetual.util.patternmatcher;

import java.util.regex.Matcher;

/**
 *
 * @author  brunob
 */
public interface IMessagePatternInfo
{
    public String getPatternName();
    public String getMessageType();
    public MessageParser getMessageParser();   
    public String getPattern();
    public String group(int pGroup);
    public Matcher getMatcher();
}
