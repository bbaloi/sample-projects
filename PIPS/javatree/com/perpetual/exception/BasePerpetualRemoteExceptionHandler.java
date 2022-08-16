/*
 * BasePerpetualRemoteExceptionHandler.java
 *
 * Created on June 25, 2003, 5:42 PM
 */

package com.perpetual.exception;

/**
 *
 * @author  brunob
 */
public abstract class BasePerpetualRemoteExceptionHandler 
{ 
    protected int errorCode;
    
    /** Creates a new instance of BasePerpetualRemoteExceptionHandler */
    public BasePerpetualRemoteExceptionHandler() 
    {
    }
    public int handleException(String pMessage,BasePerpetualRemoteException excp )
    {
        return errorCode;
    }
    public int handleException(String pMessage)
    {
        return errorCode;
    }
      public int handleException(String pMessage,Throwable t)
    {
        return errorCode;
    }
    
}
