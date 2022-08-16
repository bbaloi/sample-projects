/*
 * SyslogViewerDelegateBean.java
 *
 * Created on May 16, 2003, 3:43 PM
 */

package com.perpetual.viewer.control.ejb;

import javax.ejb.SessionBean;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import com.perpetual.util.PerpetualC2Logger;
/**
 *
 * @author  brunob
 */
public class SystemBean implements SessionBean
{
      private static PerpetualC2Logger sLog = new PerpetualC2Logger( SyslogViewerBean.class );
  
    
    /** Creates a new instance of SyslogViewerDelegateBean */
      public SystemBean() {
    }
    public void setSessionContext(SessionContext sessionContext)  throws EJBException,RemoteException
    {
    }
    public void ejbRemove() throws RemoteException
    {
    }
    public void ejbActivate() throws RemoteException
    {
    }
    public void ejbPassivate()  throws RemoteException
    {
    }
    public void ejbCreate() 
    {
        sLog.debug("Creating SyslogViewerDelegateBean !");
    }
}
