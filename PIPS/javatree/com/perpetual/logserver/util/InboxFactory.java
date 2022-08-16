/*
 * InboxFactory.java
 *
 * Created on December 31, 2003, 7:10 PM
 */

package com.perpetual.logserver.util;

import java.util.HashMap;
import java.util.LinkedList;
/**
 *
 * @author  brunob
 */
public class InboxFactory 
{
    private static InboxFactory instance=null;
    private HashMap inboxSet=new HashMap();
    
    /** Creates a new instance of InboxFactory */
    private InboxFactory() 
    {
    }
    
    public static InboxFactory getInstance()
    {
        if(instance==null)
            instance=new InboxFactory();
        return instance;
    }
    public LinkedList createInbox(double pUid)
    {
        LinkedList inbox = new LinkedList();
        inboxSet.put(new Double(pUid), inbox);
        return inbox;
    }
    public void removeInbox(double pUid)
    {
        LinkedList inbox= (LinkedList)inboxSet.remove(new Double(pUid));
        inbox=null;
    }
    public LinkedList getInbox(double pUid)
    {
        return (LinkedList) inboxSet.get(new Double(pUid));
    }
}
