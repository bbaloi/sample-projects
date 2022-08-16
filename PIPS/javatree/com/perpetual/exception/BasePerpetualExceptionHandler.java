/*
 * BasePerpetualExceptionHandler.java
 *
 * Created on June 25, 2003, 5:37 PM
 */

package com.perpetual.exception;

/**
 *
 * @author  brunob
 */
public abstract class BasePerpetualExceptionHandler 
{
    protected int errorCode;
    /** Creates a new instance of BasePerpetualExceptionHandler */
    public BasePerpetualExceptionHandler() 
    {
    }
    public int handleException(String pMessage,BasePerpetualRemoteException excp)
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
