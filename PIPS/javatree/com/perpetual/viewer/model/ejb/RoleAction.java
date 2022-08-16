package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface RoleAction extends EJBObject
{
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public Integer getRoleId() throws RemoteException;
    public void setRoleId(Integer roleId) throws RemoteException;
	public Integer getActionId() throws RemoteException;
	public void setActionId(Integer roleId) throws RemoteException;
}
