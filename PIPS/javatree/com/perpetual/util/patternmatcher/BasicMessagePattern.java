/*
 * SyslogCrondPattern.java
 *
 * Created on September 17, 2003, 12:03 PM
 */

package com.perpetual.util.patternmatcher;

import com.perpetual.util.PerpetualC2Logger;

//import com.perpetual.util.patternmatcher.MessagePattern;
/**
 *
 * @author  brunob
 */
public class BasicMessagePattern extends MessagePattern
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( BasicMessagePattern.class );    
   
    /** Creates a new instance of SyslogCrondPattern */
    public BasicMessagePattern(String pName,String pPattern) 
    {
        super(pName,pPattern);
    }
    protected void init()
    {
        //load the proper parser based on the appropriate Name of the Command
        
        String parserClassName="com.perpetual.util.patternmatcher.syslog."+lPatternName+"MessageParser";
        sLog.debug("Construct parser class for:"+parserClassName);
        //String parms [] = {pattern};
        //Class cparms [] = {Class.forName("java.lang.String")};                       
        //Constructor constructor = Class.forName(patternClassName).getConstructor(cparms);
       try
       {
        Object parserObj = Class.forName(parserClassName).newInstance();
        lParser = (MessageParser) parserObj;
       }
       catch(Exception excp)
       {
           sLog.debug("No parser class-"+parserClassName+"-was found; setting parser to null !");
       }
    }
    
}
