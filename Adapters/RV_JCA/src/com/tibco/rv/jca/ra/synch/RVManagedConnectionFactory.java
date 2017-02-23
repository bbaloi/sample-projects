package com.tibco.rv.jca.ra.synch;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;

public class RVManagedConnectionFactory implements ManagedConnectionFactory, Serializable
{
	private String className=null,rbName=null;
	private	Logger sLogger=null;
	
    public RVManagedConnectionFactory() 
    {
    	init();
        sLogger.log(Level.INFO,"In RVssManagedConnectionFactory.constructor");
    }
    private void init()
    {
    	className = RVManagedConnectionFactory.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    
    }

    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
       sLogger.log(Level.INFO,"In RVManagedConnectionFactory.createConnectionFactory,1");
        return new RVDataSource(this, cxManager);
    }

    public Object createConnectionFactory() throws ResourceException {
        sLogger.log(Level.INFO,"In RVManagedConnectionFactory.createManagedFactory,2");
        return new RVDataSource(this, null);
    }

    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo info) {
        sLogger.log(Level.INFO,"In RVManagedConnectionFactory.createManagedConnection");
        // return new RVManagedConnection(this, "test");
        return new RVManagedConnection(this,info);
    }

    public ManagedConnection matchManagedConnections(Set connectionSet, Subject subject, ConnectionRequestInfo info)
        throws ResourceException
    {
        sLogger.log(Level.INFO,"In RVManagedConnectionFactory.matchManagedConnections");
        return null;
    }

    public void setLogWriter(PrintWriter out) throws ResourceException {
        sLogger.log(Level.INFO,"In RVManagedConnectionFactory.setLogWriter");
    }

    public PrintWriter getLogWriter() throws ResourceException {
       sLogger.log(Level.INFO,"In RVManagedConnectionFactory.getLogWriter");
        return DriverManager.getLogWriter();
    }

    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj instanceof RVManagedConnectionFactory)
        {
            int hash1 = ((RVManagedConnectionFactory)obj).hashCode();
            int hash2 = hashCode();
            return hash1 == hash2;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return 1;
    }
}
