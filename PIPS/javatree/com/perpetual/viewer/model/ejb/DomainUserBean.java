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
import com.perpetual.viewer.model.vo.DomainUserVO;
/**
 *
 * @author  brunob
 */
public abstract class DomainUserBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public DomainUserBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract Integer getDomainId();
    public abstract void setDomainId(Integer roleId);
	public abstract Integer getUserId();
	public abstract void setUserId(Integer actionId);   
    
	public DomainUserPK ejbCreate(DomainUserVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException
	{
		DomainUserPK domainUserPK = null;

		if (vo.getId() == -1)
			vo.setId(KeyGeneratorUtility.generateKey());

		setId(vo.getId());
		setDomainId(vo.getDomainId());
		setUserId(vo.getUserId());

//		domainUserPK = new DomainUserPK(vo.getId());
//
//		return domainUserPK;		// MP - It think one is supposed to return null here, because it's CMP

		return null;		// it's fucking up, so I'm return null to make it fit the CMP spec
	}

    public void ejbPostCreate(DomainUserVO vo) throws javax.ejb.CreateException
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
