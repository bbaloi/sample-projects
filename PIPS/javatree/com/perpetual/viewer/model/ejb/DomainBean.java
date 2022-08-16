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
import com.perpetual.viewer.model.vo.DomainVO;

public abstract class DomainBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public DomainBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract String getName();
    public abstract void setName(String name);
	public abstract boolean getSummaryProcessingEnabled() throws RemoteException;
	public abstract void setSummaryProcessingEnabled(boolean enabled) throws RemoteException;
	public abstract Long getLastCollectionTime() throws RemoteException;
	public abstract void setLastCollectionTime(Long lastCollectionTime) throws RemoteException;
	public abstract Long getCollectionInterval() throws RemoteException;
	public abstract void setCollectionInterval(Long collectionInterval) throws RemoteException;
   
    
    public DomainPK ejbCreate(DomainVO vo) throws javax.ejb.CreateException,
    			java.rmi.RemoteException
    {
		DomainPK domainPK = null;
         
		int id = -1;
        
		if (vo.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);
    	setName(vo.getName());
    	setLastCollectionTime(vo.getLastCollectionTime());
    	setSummaryProcessingEnabled(vo.getSummaryProcessingEnabled());
    	setCollectionInterval(vo.getCollectionInterval());
    	
		domainPK = new DomainPK(vo.getId());
    	
        return domainPK;
    }
    
    public void ejbPostCreate(DomainVO vo) throws javax.ejb.CreateException
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
