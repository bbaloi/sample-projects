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
import com.perpetual.viewer.model.vo.RoleVO;
/**
 *
 * @author  brunob
 */
public abstract class RoleBean implements EntityBean
{
    
    /** Creates a new instance of UserBean */
    public RoleBean() 
    {
    }
    
    public abstract int getId();
    public abstract void setId(int id);
    public abstract String getRoleName();
    public abstract void setRoleName(String roleName);
   
    
    public RolePK ejbCreate(RoleVO vo) throws javax.ejb.CreateException,
    			java.rmi.RemoteException
    {
		RolePK rolePK = null;
         
		int id = -1;
        
		if (vo.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);
    	setRoleName(vo.getRoleName());
    	
		rolePK = new RolePK(vo.getId());
    	
        return rolePK;
    }
    
    public void ejbPostCreate(RoleVO vo) throws javax.ejb.CreateException
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