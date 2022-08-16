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

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ejb.KeyGeneratorUtility;
import com.perpetual.viewer.model.vo.UserVO;
/**
 *
 * @author  brunob
 */
public abstract class UserBean implements EntityBean
{
     private static PerpetualC2Logger sLog = new PerpetualC2Logger( UserBean.class );
  
    
    /** Creates a new instance of UserBean */
     public UserBean() 
    {
    }
    public abstract int getId();
    public abstract void setId(int pId);
    public abstract boolean getEnabled();
    public abstract void setEnabled(boolean pEnabled);
    public abstract String getUserid();
    public abstract void setUserid(String pUserId);
    public abstract String getUserpass();
    public abstract void setUserpass(String pPassword);
    public abstract String getServicedomain();
    public abstract void setServicedomain(String pServiceDomain);
    public abstract String getPhone();
    public abstract void setPhone(String pPhone); 
    public abstract String getEmail();
    public abstract void setEmail(String pEmail); 
    public abstract String getRealname();
    public abstract void setRealname(String pRealName);
    public abstract int getRoleid();
    public abstract void setRoleid(int pRole);
    
	public UserVO getVO() throws RemoteException
	{
		return new UserVO(getUserid(), getUserpass(), getServicedomain(), getPhone(), getEmail(), getRealname(), getRoleid(), getId(), getEnabled());
	}

    public UserPK ejbCreate(UserVO pUser) throws javax.ejb.CreateException, java.rmi.RemoteException
    {
        sLog.debug("Creating UserBean !");
         UserPK userPrimary = null;
                  
		int id = -1;
        
		if (pUser.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);
         setEnabled(pUser.isEnabled());
         setUserid(pUser.getUserId());
         setUserpass(pUser.getPassword());
         setServicedomain(pUser.getServiceDomain());
         setRoleid(pUser.getRoleId());
         setRealname(pUser.getRealname());
         setEmail(pUser.getEmail());  
         setPhone(pUser.getPhone());          
         sLog.debug("Set the bean up !");
         
         userPrimary = new UserPK(pUser.getId());
         
         sLog.debug("Created PrimaryKey !");
        return userPrimary;
    }
     public void ejbPostCreate(UserVO pUser) throws javax.ejb.CreateException
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
