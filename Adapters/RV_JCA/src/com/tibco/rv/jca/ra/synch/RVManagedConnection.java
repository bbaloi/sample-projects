
package com.tibco.rv.jca.ra.synch;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.util.logging.Level;

import com.tibco.rv.jca.ra.BaseRVListenerManager;



public class RVManagedConnection   implements ManagedConnection
{
	private RVConnectionEventListener myListener;
	private String user;
	private ManagedConnectionFactory mcf;
	private PrintWriter logWriter;
	private boolean destroyed;
	private Set connectionSet;
	private String className=null,rbName=null;
	private static Logger sLogger=null;

	

    RVManagedConnection(ManagedConnectionFactory mcf, String user)
    {      
        this.mcf = mcf;
        this.user = user;
        connectionSet = new HashSet();
        init();
        sLogger.log(Level.INFO,"In RVManagedConnection");
        myListener = new RVConnectionEventListener(this);
    }
    RVManagedConnection(ManagedConnectionFactory mcf)
    {      
        this.mcf = mcf;
        connectionSet = new HashSet();
        init();
        sLogger.log(Level.INFO,"In RVManagedConnection");
        myListener = new RVConnectionEventListener(this);
    }
    RVManagedConnection(ManagedConnectionFactory mcf,ConnectionRequestInfo info)
    {      
        this.mcf = mcf;
        connectionSet = new HashSet();
        init();
        sLogger.log(Level.INFO,"In RVManagedConnection");
        myListener = new RVConnectionEventListener(this);
    }
    private void init()
    {
    	className = BaseRVListenerManager.class.getName();
		try 
			{
		        sLogger = Logger.getLogger(className, rbName);
		    } catch (Exception e)
		    {
		        // Problem initializing logging.  Continue anyway.
		    }    	
    }

    private void throwResourceException(SQLException ex)
        throws ResourceException
    {
        ResourceException re = new ResourceException("SQLException: " + ex.getMessage());
        re.setLinkedException(ex);
        throw re;
    }

    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo)
        throws ResourceException
    {
        sLogger.log(Level.INFO,"In RVManagedConnection.getConnection");
        RVConnection myCon = new RVConnection(this,(RVConnectionRequestInfo)connectionRequestInfo);
        addRVConnection(myCon);
        return myCon;
    }
    
    public void destroy()
    {
		sLogger.log(Level.INFO,"In MyManagedConnection.destroy");		
		destroyed = true;
    }

    public void cleanup()
     {
		sLogger.log(Level.INFO,"In RVManagedConnection.cleanup");
		Iterator it = connectionSet.iterator();
		while(it.hasNext())
		{
			RVConnection conn = (RVConnection) it.next();
			conn.destroy();
		}
    }

    public void associateConnection(Object connection)
     {
		sLogger.log(Level.INFO,"In RVManagedConnection.associateConnection");
		
    }

    public void addConnectionEventListener(ConnectionEventListener listener)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.addConnectionEventListener");
        myListener.addConnectorListener(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.removeConnectionEventListener");
        myListener.removeConnectorListener(listener);
    }

    public XAResource getXAResource()
        throws ResourceException
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.getXAResource");
        return null;
    }

    public LocalTransaction getLocalTransaction()
    {
			sLogger.log(Level.INFO,"In RVManagedConnection.getLocalTransaction");
            return null;
    }

    public ManagedConnectionMetaData getMetaData()
        throws ResourceException
    {
        sLogger.log(Level.INFO,"In RVManagedConnection.getMetaData");
        return new RVConnectionMetaData(this);
    }


    public void setLogWriter(PrintWriter out)
        throws ResourceException
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.setLogWriter");
        logWriter = out;
    }

    public PrintWriter getLogWriter()
        throws ResourceException
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.getLogWriter");
        return logWriter;
    }
    
    boolean isDestroyed()
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.isDestroyed");
        return destroyed;
    }
    
    void sendEvent(int eventType, Exception ex)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.sendEvent,1");
        myListener.sendEvent(eventType, ex, null);
    }

    void sendEvent(int eventType, Exception ex, Object connectionHandle)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.sendEvent,2 ");
        myListener.sendEvent(eventType, ex, connectionHandle);
    }

    void removeRVConnection(RVConnection myCon)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.removeRVConnection");
        connectionSet.remove(myCon);
    }

    void addRVConnection(RVConnection myCon)
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.addRVConnection");
        connectionSet.add(myCon);
    }

    ManagedConnectionFactory getManagedConnectionFactory()
    {
		sLogger.log(Level.INFO,"In RVManagedConnection.getManagedConnectionFactory");
        return mcf;
    }

}
