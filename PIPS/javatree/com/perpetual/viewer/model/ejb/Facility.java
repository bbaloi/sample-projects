package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Facility extends EJBObject
{
    
   // public void deleteUser(UserPK pPrimayKey) throws java.rmi.RemoteException;
    //public void updateUser(UserVO pUser) throws java.rmi.RemoteException;
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
}
