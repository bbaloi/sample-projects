/*
 * PropertyHandlerException.java
 *
 * Created on June 29, 2003, 9:54 AM
 */

package com.perpetual.util;

import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */
public class PropertyHandlerException extends BasePerpetualException
{
    static
    {
        lHandler = new PropertyHandlerExceptionHandler();
    }
    
    /** Creates a new instance of PropertyHandlerException */
    public PropertyHandlerException()
    {
        super();
    }
    public PropertyHandlerException(String pMessage)
    {
        super(pMessage);
    }
}