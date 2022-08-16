package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface DomainFacility extends EJBObject
{
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public Integer getDomainId() throws RemoteException;
    public void setDomainId(Integer domainId) throws RemoteException;
	public Integer getFacilityId() throws RemoteException;
	public void setFacilityId(Integer facilityId) throws RemoteException;
}
