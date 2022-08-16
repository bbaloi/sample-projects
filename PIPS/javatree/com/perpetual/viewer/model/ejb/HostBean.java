/*
 * UserBean.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import com.perpetual.util.ejb.KeyGeneratorUtility;
import com.perpetual.viewer.model.vo.HostVO;

public abstract class HostBean implements EntityBean
{
    public abstract int getId();
    public abstract void setId(int id);
    public abstract String getName();
    public abstract void setName(String name);
    public abstract String getDescription();
    public abstract void setDescription(String description);
    
    public HostPK ejbCreate(HostVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException
    {
		HostPK hostPK = null;
        int id = -1;
        
        if (vo.getId() == -1) vo.setId(KeyGeneratorUtility.generateKey());
    	setId(vo.getId());
    	setName(vo.getName());
		setDescription(vo.getDescription());

//		hostPK = new HostPK(vo.getId());
//    	
//        return hostPK;
		return null;		// CMP spec
    }
    
    public void ejbPostCreate(HostVO vo) throws javax.ejb.CreateException
    {
        // Do nothing for now.
        //return;
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
    public void setEntityContext(EntityContext entityContext)  throws EJBException,RemoteException
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
   
}
