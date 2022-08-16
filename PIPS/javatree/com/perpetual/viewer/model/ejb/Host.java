package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Host extends EJBObject
{
    
   // public void deleteUser(UserPK pPrimayKey) throws java.rmi.RemoteException;
    //public void updateUser(UserVO pUser) throws java.rmi.RemoteException;
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String hostName) throws RemoteException;
    public String getDescription() throws RemoteException;
    public void setDescription(String description) throws RemoteException;
}
