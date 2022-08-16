/*
 * SyslodDaoBean.java
 *
 * Created on May 16, 2003, 3:55 PM
 */

package com.perpetual.viewer.model.ejb;
import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;

import javax.ejb.EntityBean;
import com.perpetual.util.PerpetualC2Logger;
/**
 *
 * @author  brunob
 */
public class SyslogDaoBean implements EntityBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SyslogDaoBean.class );
  
    /** Creates a new instance of SyslodDaoBean */
    public SyslogDaoBean() 
    {
    }
    public void setEntityContext(EntityContext entityContext)  throws EJBException,RemoteException
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
    public void unsetEntityContext() throws EJBException,RemoteException
    {
    }
    public void ejbLoad() throws EJBException,RemoteException
    {
    }
    public void ejbStore() throws EJBException,RemoteException
    {
    }
    public void ejbCreate() 
    {
        sLog.debug("Creating SyslogViewerDelegateBean !");
    }
    
}
