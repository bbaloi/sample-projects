/*
 * SimplePatternMatcherEngine.java
 *
 * Created on September 17, 2003, 10:44 AM
 */

package com.perpetual.util.patternmatcher;

import com.perpetual.util.patternmatcher.syslog.*;
import com.perpetual.util.ResourceLoader;
import com.perpetual.util.PerpetualC2Logger;

import java.util.Iterator;
import java.util.Collection;

import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.util.EJBLoader;

import java.lang.reflect.Constructor;

/**
 *
 * @author  brunob
 */
public class MessagePatternMatcherEngine extends PatternMatcherEngine implements java.io.Serializable
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( RecordPatternMatcherEngine.class );    
    private MessagePatternCRUD messagePatternCRUD=null;
    
     /** Creates a new instance of SimplePatternMatcherEngine */
    public MessagePatternMatcherEngine(Collection pSelectedMessagePatterns)
    {
        super(pSelectedMessagePatterns);
    }
    
    protected void init()
    {
        //get from DB the list of patterns - could be for a given Domain
        // ....by lPatternCollectionCriteria....in this cae it is $perpetualhome/config/recordformat.xml
        sLog.debug("MessagePatternMatcherEngine.init()");
        messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
	loadPatternEngine();              
    }
    
    private void loadPatternEngine()
    {
        IMessagePattern lPattern=null;
        String patternClassName=null;
        Object patternObj=null;
        MessagePatternData patternData=null;
                
        try
         {
            Iterator it = ((Collection)lPatternCollectionCriteria).iterator();
             
            while(it.hasNext())
            {
                Integer patternId = (Integer) it.next();
                patternData = messagePatternCRUD.retrieveByPrimaryKey(patternId.intValue());
                
                String pattern = patternData.getPattern();                
                String name = patternData.getName();
                sLog.debug("Pattern:"+name+":"+pattern);
                
                patternObj = new BasicMessagePattern(name,pattern);
                
                    //patternClassName="com.perpetual.util.patternmatcher.syslog."+name+"MessagePattern";
                    //sLog.debug("trying to construct pattern class for:"+patternClassName);
                    //String parms [] = {pattern};
                    //Class cparms [] = {Class.forName("java.lang.String")};                       
                    //Constructor constructor = Class.forName(patternClassName).getConstructor(cparms);
                    //patternObj = constructor.newInstance(parms);
                                 
                lPatternList.add(patternObj);
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
