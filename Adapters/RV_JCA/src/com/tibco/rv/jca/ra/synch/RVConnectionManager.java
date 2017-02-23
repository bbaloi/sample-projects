
package com.tibco.rv.jca.ra.synch;

import java.io.Serializable;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.resource.spi.*;

public class RVConnectionManager  implements ConnectionManager, Serializable
{
	private String className=null,rbName=null;
	private static Logger sLogger=null;

    public RVConnectionManager()
    {
    	init();
        sLogger.log(Level.INFO,"In RVConnectionManager");
    }
    private void init()
    {
    	className = RVConnectionManager.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    	
    }

    public Object allocateConnection(ManagedConnectionFactory mcf, ConnectionRequestInfo info)
        throws ResourceException
    {
       sLogger.log(Level.INFO,"In RVConnectionManager.allocateConnection");
        ManagedConnection mc = mcf.createManagedConnection(null, info);
        return mc.getConnection(null, info);
    }
}
