/*
 * Constants.java
 *
 * Created on November 14, 2003, 3:37 PM
 */

package com.perpetual.logserver.util;

/**
 *
 * @author  brunob
 */
public final class LogserverConstants 
{
    
    /** Creates a new instance of Constants */
    public LogserverConstants() {
    }
    public static int STATE_0=0;
    //payload is the LogFilter - just created and requesting the creation of a DiskCursor - place in by LogstoreDatabase.submitQuery()
    public static int STATE_1=1;
    //payload is DiskCursor = Thread has processed query and obtained an appropriate cursor - place on ResultsQueue by Thread
    public static int STATE_2=2;
    //payload is DiskCursor and it is placed on the Query queu by the LogstoreDatabase for further processing
    public static int STATE_3=3;
    //payload is Collection of records - placed ont Results quwe by thread - returned to User
}
