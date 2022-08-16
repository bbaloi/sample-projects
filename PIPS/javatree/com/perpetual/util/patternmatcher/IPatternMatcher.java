/*
 * PatternMatcher.java
 *
 * Created on September 17, 2003, 10:29 AM
 */

package com.perpetual.util.patternmatcher;

import java.util.Collection;
/**
 *
 * @author  brunob
 */
public interface IPatternMatcher 
{
    public IMessagePatternInfo findFirstMatch(String pMessage);  
    public Collection findAllmatches(String pMessage);
}
