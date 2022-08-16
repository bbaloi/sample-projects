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
import com.perpetual.viewer.model.vo.DomainSeverityVO;
/**
 *
 * @author  brunob
 */
public abstract class DomainSeverityBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public DomainSeverityBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract Integer getDomainId();
    public abstract void setDomainId(Integer roleId);
	public abstract Integer getSeverityId();
	public abstract void setSeverityId(Integer actionId);   
    
    public DomainSeverityPK ejbCreate(DomainSeverityVO vo) throws javax.ejb.CreateException,
    			java.rmi.RemoteException
    {
		DomainSeverityPK domainSeverityPK = null;
         
		int id = -1;
        
		if (vo.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);

    	setDomainId(vo.getDomainId());
		setSeverityId(vo.getSeverityId());
    	
		domainSeverityPK = new DomainSeverityPK(vo.getId());
    	
        return domainSeverityPK;
    }
    
    public void ejbPostCreate(DomainSeverityVO vo) throws javax.ejb.CreateException
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
