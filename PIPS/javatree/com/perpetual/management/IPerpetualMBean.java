/*
 * WysdomMBean.java
 *
 * Created on March 13, 2003, 2:13 PM
 */

package com.perpetual.management;

import javax.management.DynamicMBean;
import javax.management.MBeanRegistration;

/**
 *
 * @author  brunob
 */
public interface IPerpetualMBean extends DynamicMBean, MBeanRegistration
//public interface IWysdomMBean 
{
    public void shutdown();
    public void start();
    public boolean isAlive();
    
        
}
