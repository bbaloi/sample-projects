/*
 * WysdomMBean.java
 *
 * Created on March 14, 2003, 2:10 PM
 */

package com.perpetual.management;

import javax.management.MBeanException;
import javax.management.InvalidAttributeValueException;
import javax.management.AttributeNotFoundException;
import javax.management.ReflectionException;
import javax.management.MBeanInfo;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanInfo;
import javax.management.NotificationBroadcasterSupport;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.ObjectName;
import javax.management.MBeanServer;

/**
 *
 * @author  brunob
 */

//public abstract class WysdomMBean extends StandardMBean implements IWysdomMBean
public abstract class PerpetualMBean extends NotificationBroadcasterSupport implements IPerpetualMBean, NotificationListener
{
    boolean lHealth=false;
    protected MBeanInfo lBeanInfo=null;
    protected MBeanServer lServer=null;
    /** Creates a new instance of WysdomMBean */
       
    public boolean isAlive()  
    {
        return lHealth;
    }
    
    public void shutdown() {
    }
    
    public void start() {
    }
    //-------------DynamicMBean methods--------
    public Object getAttribute(String pAttribute) throws AttributeNotFoundException
    {
        return null;
    }
    public void setAttribute(Attribute pAttribute) throws AttributeNotFoundException,
                                                         InvalidAttributeValueException,
                                                         MBeanException,ReflectionException
    {
     }
    public AttributeList getAttributes(String [] pAttributeList)
    {
        return null;
    }
    public Object invoke(String pActionName,Object [] pParams, String [] pSignature)
            throws MBeanException,ReflectionException 
    {
        return null;
    }
    public MBeanInfo getMBeanInfo()
    {
        return lBeanInfo;
    }
    public AttributeList setAttributes(AttributeList pAttributeList)
    {
        return null;
    }
    public ObjectName preRegister(MBeanServer pServer,ObjectName pName) throws java.lang.Exception
    {
        return pName;
    }
    public void postDeregister()
    {
    }
    public void preDeregister()
    {
    }
    public void postRegister(java.lang.Boolean pRegistrationDone)
    {
    }
}
