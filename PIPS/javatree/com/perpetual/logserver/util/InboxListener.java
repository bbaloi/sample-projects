/*
 * InboxListener.java
 *
 * Created on January 1, 2004, 4:18 PM
 */

package com.perpetual.logserver.util;

import java.util.LinkedList;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class InboxListener 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(InboxListener.class);
    private LinkedList inbox=null;
    
    /** Creates a new instance of InboxListener */
    public InboxListener(LinkedList pInbox) 
    {
        inbox=pInbox;
    }
    public Object getItem() throws Exception
    {
        synchronized(inbox)
        {
                    sLog.debug("Waiting for Cursor !"); 
                    inbox.wait();
        }
        sLog.debug("Message arrived in Inbox !");
        Object item= inbox.removeFirst();                               
        sLog.debug("Got Item");
        
        return item;
        
    }
    public void addItem(Object pItem)
    {
        inbox.add(pItem);
    }
    public void notifyInbox()
    {     
        synchronized(inbox)
        {
            inbox.notifyAll();
        }
    }
    
}
