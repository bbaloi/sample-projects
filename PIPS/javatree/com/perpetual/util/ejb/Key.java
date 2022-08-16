package com.perpetual.util.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Key extends EJBObject
{
    public int getNumber() throws RemoteException;
    public void setNumber(int id) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String roleName) throws RemoteException;
}
