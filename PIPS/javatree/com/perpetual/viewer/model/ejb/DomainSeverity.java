package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface DomainSeverity extends EJBObject
{
    public int getId() throws RemoteException;
    public void setId(int id) throws RemoteException;
    public Integer getDomainId() throws RemoteException;
    public void setDomainId(Integer domainId) throws RemoteException;
	public Integer getSeverityId() throws RemoteException;
	public void setSeverityId(Integer severityId) throws RemoteException;
}
