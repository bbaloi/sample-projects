/*
 * PropertyExceptionHandler.java
 *
 * Created on June 29, 2003, 9:56 AM
 */

package com.perpetual.util;

import com.perpetual.exception.BasePerpetualExceptionHandler;
import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */
public class PropertyHandlerExceptionHandler extends BasePerpetualExceptionHandler
{
    
    /** Creates a new instance of PropertyExceptionHandler */
    public PropertyHandlerExceptionHandler() 
    {
    }
    public int handleException(String pMessage,BasePerpetualException excp) throws BasePerpetualException
    {
        excp.processException( pMessage, excp );
        return errorCode;
    }
}
