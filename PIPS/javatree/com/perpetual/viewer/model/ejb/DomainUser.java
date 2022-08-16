package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface DomainUser extends EJBObject
{
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public Integer getDomainId() throws RemoteException;
    public void setDomainId(Integer domainId) throws RemoteException;
	public Integer getUserId() throws RemoteException;
	public void setUserId(Integer hostId) throws RemoteException;
}
