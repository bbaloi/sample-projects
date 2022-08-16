package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Domain extends EJBObject
{
    
   // public void deleteUser(UserPK pPrimayKey) throws java.rmi.RemoteException;
    //public void updateUser(UserVO pUser) throws java.rmi.RemoteException;
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String domainName) throws RemoteException;
	public boolean getSummaryProcessingEnabled() throws RemoteException;
	public void setSummaryProcessingEnabled(boolean enabled) throws RemoteException;
	public Long getLastCollectionTime() throws RemoteException;
	public void setLastCollectionTime(Long lastCollectionTime) throws RemoteException;
	public Long getCollectionInterval() throws RemoteException;
	public void setCollectionInterval(Long collectionInterval) throws RemoteException;
}
