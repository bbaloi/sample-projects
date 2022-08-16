/*
 * IResourceAccess.java
 *
 * Created on July 16, 2003, 2:55 PM
 */

package com.perpetual.recordprocessor.control;

import com.perpetual.rp.util.navigator.INavigator;

/**
 *
 * @author  brunob
 */
public interface IResourceAccess 
{
    public  INavigator getNavigator();
    public  RPConfigManager getConfigManager();
    public  RecordProcessorManager getRecordProcessor();
    
    
}
