/*
 * PatternMatcherEngine.java
 *
 * Created on September 17, 2003, 10:29 AM
 */

package com.perpetual.util.patternmatcher;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author  brunob
 */
public abstract class PatternMatcherEngine implements IPatternMatcher
//public class PatternMatcherEngine implements IPatternMatcher
{
    protected List lPatternList=new ArrayList();
    protected Object lPatternCollectionCriteria=null;
    
    /** Creates a new instance of PatternMatcherEngine */
    public PatternMatcherEngine(Object pPatternCollectionCriteria) 
    {
        lPatternCollectionCriteria = pPatternCollectionCriteria;
        init();
    }
    
    public IMessagePatternInfo findFirstMatch(String pMessage)
    {
       IMessagePatternInfo info=null;
        
        Iterator it = lPatternList.iterator();
        while(it.hasNext())
        {
            Object obj = (Object) it.next();
            IMessagePattern pattern = (IMessagePattern) obj;
            if(pattern.findMatch(pMessage))
            {
                info = (IMessagePatternInfo) obj;
                break;
            }
        }
        return info;        
    }    
    
    public Collection findAllmatches(String pMessage) 
    {
         IMessagePatternInfo info=null;
         Iterator it = lPatternList.iterator();
         ArrayList matchList = new ArrayList();
        
        while(it.hasNext())
        {
            Object obj = (Object) it.next();
            IMessagePattern pattern = (IMessagePattern) obj;
            if(pattern.findMatch(pMessage))
            {
                 info = (IMessagePatternInfo) obj;
                 matchList.add(info);
            }
        }
        return matchList;                
    }
    
   protected void init()
   {
        //overwirten by child classes
   }
       
}
