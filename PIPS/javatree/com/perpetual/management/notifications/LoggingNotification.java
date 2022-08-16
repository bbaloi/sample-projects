/*
 * LogingNotification.java
 *
 * Created on March 18, 2003, 2:11 PM
 */

package com.perpetual.management.notifications;

import javax.management.Notification;



/**
 *
 * @author  brunob
 */
public class LoggingNotification extends PerpetualManagementNotification
{
    
    private String lMessage=null;
    private Integer lLevel;
    /** Creates a new instance of LogingNotification */
    public LoggingNotification(String type,Object source,long sequenceNumber,
                            long timeStamp, String message,Integer pLevel, String pLogMessage) 
    { 
        super(type,source,sequenceNumber,timeStamp,message);
        lLevel=pLevel;
        lMessage=pLogMessage;
        
    }
    public Integer getLevel()
    {
        return lLevel;
    }
    public String getMessage()
    {
        return lMessage;
    }
}
