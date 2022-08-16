/*
 * WysdomManagementNotification.java
 *
 * Created on March 18, 2003, 2:13 PM
 */

package com.perpetual.management.notifications;

import javax.management.Notification;
/**
 *
 * @author  brunob
 */
public abstract class PerpetualManagementNotification extends Notification
{
     
    /** Creates a new instance of WysdomManagementNotification */
    public PerpetualManagementNotification(String type, Object source, long sequenceNumber, long timeStamp, String message) 
    {
        super(type,source,sequenceNumber,timeStamp,message);
    }
    
}
