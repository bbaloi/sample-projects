/*
 * AppenderTester.java
 *
 * Created on April 3, 2003, 12:35 PM
 */

package com.perpetual.management;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class AppenderTester 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( AppenderTester.class );
    
    /** Creates a new instance of AppenderTester */
    public AppenderTester() {
    }
    public static void main(String [] args)
    {
        
        sLog.debug("DEBUG msg");
        sLog.info("INFO msg");
        sLog.warn("WARNING msg");
        sLog.error("ERROR msg");
        sLog.fatal("FATAL msg");
        
    }
}
