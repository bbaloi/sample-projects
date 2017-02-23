package com.tibco.rv.jca.ra.synch;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.*;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;
import javax.sql.DataSource;

import com.tibco.rv.jca.ra.BaseRVListenerManager;

import java.util.logging.Logger;
import java.util.logging.Level;
import com.tibco.rv.jca.exceptions.RV_JCAException;


public class RVDataSource  implements RVConnectionFactory, Serializable, Referenceable
{

    private String desc;
    private ManagedConnectionFactory mcf;
    private ConnectionManager connectionMgr;
    private Reference reference;
    private String className=null,rbName=null;
    private static Logger sLogger=null;


    public RVDataSource(ManagedConnectionFactory mcf, ConnectionManager cm)
    {
        init();
        sLogger.log(Level.INFO,"In RVDataSource");
        
        this.mcf = mcf;

        if(connectionMgr == null)
            connectionMgr = new RVConnectionManager();
        else
            connectionMgr = cm;
    }
    
    private void init()
    {
    	className = RVDataSource.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    	
    }
  
    public RVConnection getConnection()  throws RV_JCAException
    {
        sLogger.log(Level.INFO,"In RVDataSource.getConnection,1");
        try
        {
            return (RVConnection)connectionMgr.allocateConnection(mcf, null);
        }
        catch(ResourceException excp)
        {
        	String msg="Could not get RVConnection !";
            throw new RV_JCAException(excp,msg);
        }
    }

    public RVConnection getConnection(String username, String password) throws RV_JCAException
    {
       sLogger.log(Level.INFO,"In RVDataSource.getConnection,2");
        try
        {
            javax.resource.spi.ConnectionRequestInfo info = new RVConnectionRequestInfo(username, password);
            return (RVConnection)connectionMgr.allocateConnection(mcf, info);
        }
        catch(ResourceException excp)
        {
        	String msg="Could not get RVConnection !";
            throw new RV_JCAException(excp,msg);
        }
    }
    
    public RVConnection getConnection(String pService,String pNetwork,String pDaemon) throws RV_JCAException
	{
    	sLogger.log(Level.INFO,"In RVDataSource.getConnection,3");
        try
        {
            javax.resource.spi.ConnectionRequestInfo info = new RVConnectionRequestInfo(pService,pNetwork,pDaemon);
            return (RVConnection)connectionMgr.allocateConnection(mcf, info);
        }
        catch(ResourceException excp)
        {
        	String msg="Could not get RVConnection !";
            throw new RV_JCAException(excp,msg);
        }
	}
	public RVConnection getConnection(String pUserName,String pPassword,String pService,String pNetwotk,String pDaemon) throws RV_JCAException
	{
		sLogger.log(Level.INFO,"In RVDataSource.getConnection,4");
        try
        {
            javax.resource.spi.ConnectionRequestInfo info = new RVConnectionRequestInfo(pUserName, pPassword);
            return (RVConnection)connectionMgr.allocateConnection(mcf, info);
        }
        catch(ResourceException excp)
        {
        	String msg="Could not get RVConnection !";
            throw new RV_JCAException(excp,msg);
        }
	}
	
    public int getLoginTimeout()
        throws SQLException
    {
        return DriverManager.getLoginTimeout();
    }

    public void setLoginTimeout(int seconds)
        throws SQLException
    {
        DriverManager.setLoginTimeout(seconds);
    }

    public PrintWriter getLogWriter()
        throws SQLException
    {
        return DriverManager.getLogWriter();
    }

    public void setLogWriter(PrintWriter out)
        throws SQLException
    {
        DriverManager.setLogWriter(out);
    }

    public String getDescription()
    {
        return desc;
    }

    public void setDescription(String desc)
    {
        this.desc = desc;
    }

    public void setReference(Reference reference)
    {
        this.reference = reference;
    }

    public Reference getReference()
    {
        return reference;
    }
}
