/*
 * SchedulePayload.java
 *
 * Created on December 22, 2003, 9:04 PM
 */

package com.perpetual.logreport.scheduler;

import javax.ejb.EJBObject;
import com.perpetual.logreport.model.ScheduleData;

/**
 *
 * @author  brunob
 */
public class SchedulePayload implements java.io.Serializable
{
    private EJBObject sessionObject=null;
    private ScheduleData scheduleData=null;
    /** Creates a new instance of SchedulePayload */
    public SchedulePayload(EJBObject pSessionObject,ScheduleData pScheduleData) 
    {
        sessionObject=pSessionObject;
        scheduleData=pScheduleData;
    }
    public EJBObject getSessionObject()
    {
        return sessionObject;
    }
    public ScheduleData getScheduleData()
    {
        return scheduleData;
    }
}
