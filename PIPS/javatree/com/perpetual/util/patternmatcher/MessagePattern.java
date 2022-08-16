/*
 * MessagePattern.java
 *
 * Created on September 17, 2003, 10:27 AM
 */

package com.perpetual.util.patternmatcher;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public abstract class MessagePattern implements IMessagePattern,IMessagePatternInfo
{
     private static PerpetualC2Logger sLog = new PerpetualC2Logger(MessagePattern.class );    
   
    protected MessageParser lParser=null;
    protected Pattern lPattern=null;
    protected Matcher lMatcher = null;
    protected String lPatternName=null;
    protected String lMessageType=null;
    
    /** Creates a new instance of MessagePattern */
    public MessagePattern(String pPattern) 
    {
        sLog.debug("pattern:"+pPattern);
        //System.out.println("pattern:"+pPattern);
        lPattern=Pattern.compile(pPattern);
        lMatcher = lPattern.matcher("");       
        init();
    }
    public MessagePattern(String pName,String pPattern) 
    {
        sLog.debug("pattern:"+pPattern);
        lPatternName=pName;
        //System.out.println("pattern:"+pPattern);
        lPattern=Pattern.compile(pPattern);
        lMatcher = lPattern.matcher("");       
        init();
    }
    
    public boolean findMatch(String pMessage)
    {
        lMatcher.reset(pMessage);
        //if(lMatcher.find())
        if(lMatcher.matches())
            return true;
        else 
            return false;
    }
    public String getPatternName()
    {
        return lPatternName;
    }
    public String getMessageType()
    {
        return lMessageType;
    }
    public MessageParser getMessageParser()
    {
         return lParser;
    }
    public String getPattern() 
    {
        return lPattern.pattern();
    }
    public String group(int pGroup) 
    {
        return lMatcher.group(pGroup);
    }    
     public Matcher getMatcher() 
    {
        return lMatcher;
    }    
    protected void init()
    {
    }
    
   
   
    
    
}
