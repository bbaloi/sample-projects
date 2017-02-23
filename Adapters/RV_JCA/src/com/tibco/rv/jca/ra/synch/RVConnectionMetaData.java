package com.tibco.rv.jca.ra.synch;

//import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import com.tibco.rv.jca.util.Constants;

public class RVConnectionMetaData implements ManagedConnectionMetaData
{

	private RVManagedConnection mc;
	private String userName;
	private String className=null,rbName=null;
	private static Logger sLogger=null;

    public RVConnectionMetaData(RVManagedConnection mc)
    {
        this.mc = mc;
        init();
        sLogger.log(Level.INFO,"In RVConnectionMetaData.constructor");
    }
    private void init()
    {
    	className = RVConnectionMetaData.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    
    }
    public String getEISProductName()
        throws ResourceException
    {
     sLogger.log(Level.INFO,"In RVConnectionMetaData.getEISProductName");
        return Constants.EIS_PRODUCT_NAME;
    }

    public String getEISProductVersion()
        throws ResourceException
    {
       sLogger.log(Level.INFO,"In RVConnectionMetaData.getEISProductVersion");
        return Constants.EIS_PRODUCT_VERSION;

    }
    public String getUserName()
    {
    	return userName;
    }

    public int getMaxConnections()
        throws ResourceException
    {
            sLogger.log(Level.INFO,"In RVConnectionMetaData.getMaxConnections");
            return Constants.EIS_MAX_CONNECTIONS;
    }
   

}
