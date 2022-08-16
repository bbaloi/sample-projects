/*
 * DAOFactory.java
 *
 * Created on May 19, 2003, 8:55 PM
 */

package com.perpetual.viewer.model;

import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.util.PerpetualC2Logger;

/**
 *
 * @author  brunob
 */
public class DAOFactory 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( DAOFactory.class );
       
    /** Creates a new instance of DAOFactory */
    public DAOFactory() 
    {
    }
    public static SyslogDAO getDAO(String pDataSourceType)
    {
        try
        {
            return (SyslogDAO)(Class.forName(pDataSourceType)).newInstance();
        }
        catch(Throwable t)
        {
            sLog.error("Couldn't construct class:"+pDataSourceType);
        }
        return null;
    }
}
