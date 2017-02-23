package com.tibco.rv.jca.ra.synch;

import java.util.Vector;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ManagedConnection;

public class RVConnectionEventListener   implements javax.sql.ConnectionEventListener
{
    private Vector listeners;
    private ManagedConnection mcon;

    

    public RVConnectionEventListener(ManagedConnection mcon)
    {
        this.mcon = mcon;
        listeners = new Vector();
    }

    public void sendEvent(int eventType, Exception ex, Object connectionHandle)
    {
        //System.out.println("In MyConnectionEventListener.sendEvent");
    }

    public void addConnectorListener(ConnectionEventListener l)
    {
       // System.out.println("In MyConnectionEventListener.addConnectorListener");
    	listeners.add(l);
    }

    public void removeConnectorListener(ConnectionEventListener l)
    {
      // System.out.println("In MyConnectionEventListener.removeConnectorListener");
    	listeners.remove(l);
    }

    public void connectionClosed(javax.sql.ConnectionEvent connectionevent)
    {
        //System.out.println("In MyConnectionEventListener.connectorClosed");
    }

    public void connectionErrorOccurred(javax.sql.ConnectionEvent event)
    {
        //System.out.println("In MyConnectionEventListener.connectorErrorOccurred");
    }

}
