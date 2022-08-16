/*
 * UserRemote.java
 *
 * Created on June 28, 2003, 4:07 PM
 */

package com.perpetual.viewer.model.ejb;

import javax.ejb.EJBObject;
import com.perpetual.viewer.model.vo.UserVO;
import java.rmi.RemoteException;
/**
 *
 * @author  brunob
 */
public interface User extends EJBObject
{
    
   // public void deleteUser(UserPK pPrimayKey) throws java.rmi.RemoteException;
    //public void updateUser(UserVO pUser) throws java.rmi.RemoteException;
    public int getId() throws RemoteException;
    public void setId(int pId) throws RemoteException;
    public boolean getEnabled() throws RemoteException;
    public void setEnabled(boolean pEnabled) throws RemoteException;
    public String getUserid() throws RemoteException;
    public void setUserid(String pUserId) throws RemoteException;
    public String getUserpass() throws RemoteException;
    public void setUserpass(String pPassword) throws RemoteException;
    public String getServicedomain() throws RemoteException;
    public void setServicedomain(String pServiceDomain) throws RemoteException;
    public String getPhone() throws RemoteException;
    public void setPhone(String pPhone) throws RemoteException;
    public String getEmail() throws RemoteException;
    public void setEmail(String pEmail) throws RemoteException; 
    public String getRealname() throws RemoteException;
    public void setRealname(String pRealName) throws RemoteException;
    public int getRoleid() throws RemoteException;
    public void setRoleid(int pRole) throws RemoteException;
	public UserVO getVO() throws RemoteException;
}


