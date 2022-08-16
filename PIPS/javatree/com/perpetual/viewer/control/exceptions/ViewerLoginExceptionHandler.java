/*
 * ViewerLoginExceptionHandler.java
 *
 * Created on June 29, 2003, 1:11 PM
 */

package com.perpetual.viewer.control.exceptions;

import com.perpetual.exception.BasePerpetualExceptionHandler;
import com.perpetual.exception.BasePerpetualException;

/**
 *
 * @author  brunob
 */
public class ViewerLoginExceptionHandler extends BasePerpetualExceptionHandler
{
    
    /** Creates a new instance of ViewerLoginExceptionHandler */
    public ViewerLoginExceptionHandler() 
    {
    }
    public int handleException(String pMessage,ViewerLoginException excp) throws ViewerLoginException
    {
        try
        {
            excp.processException( pMessage, excp );
        }
        catch(BasePerpetualException e)
        {
            throw new ViewerLoginException("ViewerLogin processing error !",e);
        }
        return errorCode;
    }
    
}
