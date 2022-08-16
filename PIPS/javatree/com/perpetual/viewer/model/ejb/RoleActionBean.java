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
import com.perpetual.viewer.model.vo.RoleActionVO;
/**
 *
 * @author  brunob
 */
public abstract class RoleActionBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public RoleActionBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract Integer getRoleId();
    public abstract void setRoleId(Integer roleId);
	public abstract Integer getActionId();
	public abstract void setActionId(Integer actionId);   
    
    public RoleActionPK ejbCreate(RoleActionVO vo) throws javax.ejb.CreateException,
    			java.rmi.RemoteException
    {
		RoleActionPK roleActionPK = null;
         
		int id = -1;
        
		if (vo.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);
		
    	setRoleId(vo.getRoleId());
		setActionId(vo.getActionId());
    	
		roleActionPK = new RoleActionPK(vo.getId());
    	
        return roleActionPK;
    }
    
    public void ejbPostCreate(RoleActionVO vo) throws javax.ejb.CreateException
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
