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
import com.perpetual.viewer.model.vo.DomainHostVO;
/**
 *
 * @author  brunob
 */
public abstract class DomainHostBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public DomainHostBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract Integer getDomainId();
    public abstract void setDomainId(Integer roleId);
	public abstract Integer getHostId();
	public abstract void setHostId(Integer actionId);   
    
    public DomainHostPK ejbCreate(DomainHostVO vo) throws javax.ejb.CreateException,
    			java.rmi.RemoteException
    {
		DomainHostPK domainHostPK = null;
         
		int id = -1;
        
		if (vo.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}		
		setId(id);

    	setDomainId(vo.getDomainId());
		setHostId(vo.getHostId());
    	
		domainHostPK = new DomainHostPK(vo.getId());
    	
        return domainHostPK;
    }
    
    public void ejbPostCreate(DomainHostVO vo) throws javax.ejb.CreateException
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
