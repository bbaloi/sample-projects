/*
 * WysdomMonitorMBean.java
 *
 * Created on March 14, 2003, 4:56 PM
 */

package com.perpetual.management;

import javax.management.monitor.Monitor;
/**
 *
 * @author  brunob
 */
public abstract class PerpetualMonitorMBean extends Monitor
{
    
    /** Creates a new instance of WysdomMonitorMBean */
    public PerpetualMonitorMBean() {
    }
    
    /*public void insertSpecificElementAt(int param)
    {
    }    
    public void removeSpecificElementAt(int param) 
    {
    }*/
    
    public void start() {
    }
    
    public void stop() {
    }
    
}
