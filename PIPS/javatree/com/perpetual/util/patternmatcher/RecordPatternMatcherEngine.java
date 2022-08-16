/*
 * SimplePatternMatcherEngine.java
 *
 * Created on September 17, 2003, 10:44 AM
 */

package com.perpetual.util.patternmatcher;

import com.perpetual.util.patternmatcher.syslog.*;
import org.jdom.*;
import com.perpetual.util.ResourceLoader;
import com.perpetual.util.PerpetualC2Logger;

import java.util.Iterator;


/**
 *
 * @author  brunob
 */
public class RecordPatternMatcherEngine extends PatternMatcherEngine implements java.io.Serializable
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( RecordPatternMatcherEngine.class );    
    
    private Element recordPatternList = null;
        
    /** Creates a new instance of SimplePatternMatcherEngine */
    public RecordPatternMatcherEngine(String pPropertyFileName)
    {
        super(pPropertyFileName);
    }
    
   protected void init()     
    {
        //get from DB the list of patterns - could be for a given Domain
        // ....by lPatternCollectionCriteria....in this cae it is $perpetualhome/config/recordformat.xml
        sLog.debug("RecordPatternMatcherEngine.init()");
        IMessagePattern lPattern=null;
        
        try
        {
            recordPatternList = ResourceLoader.loadResourceAsJdomElement((String)lPatternCollectionCriteria);
            //System.out.println("element:"+recordPatternList.toString());
            //Element recordFormats = recordPatternList.getChild("recordformatpattern");     
            Iterator it = recordPatternList.getChildren("pattern").iterator();
             
            while(it.hasNext())
            {
                Element param = (Element)it.next();
                String pattern = param.getAttributeValue("value");
                if (param.getAttributeValue("name").equals("syslog"))
                {
                         lPattern= new SyslogRecordPattern(pattern);  
                         sLog.debug("syslog:"+pattern);
                }
                if (param.getAttributeValue("name").equals("syslog_pid"))
                {
                     lPattern= new SyslogRecordPidPattern(pattern);  
                     sLog.debug("syslog_pid:"+pattern);
                }
            
                
                lPatternList.add(lPattern);
            }
        }
        catch(Exception excp)
        {
            String msg = "Error trying to load record patterns !";
            sLog.error(msg);
            excp.printStackTrace();            
        }
                       
    }
}
