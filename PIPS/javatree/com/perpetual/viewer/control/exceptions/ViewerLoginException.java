/*
 * ViewerLoginException.java
 *
 * Created on June 28, 2003, 3:43 PM
 */

package com.perpetual.viewer.control.exceptions;

import com.perpetual.exception.BasePerpetualException;


/**
 *
 * @author  brunob
 */
public class ViewerLoginException extends BasePerpetualException
{
     static
    {
        lHandler = new ViewerLoginExceptionHandler();
    }
    /** Creates a new instance of ViewerLoginException */
            
    public ViewerLoginException(String pMessage,Throwable t)
    {
        super(pMessage,t);
    }
    public static void handle(String pMessage,Throwable t)
        {
            lHandler.handleException(pMessage,new ViewerLoginException(pMessage,t));
        }
}
