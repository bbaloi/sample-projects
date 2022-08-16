package com.perpetual.viewer.control.ejb.crud.host;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.perpetual.viewer.model.vo.HostVO;

public interface HostCRUD extends javax.ejb.EJBObject
{
    public HostVO create(HostVO vo) throws RemoteException, CreateException;
    public void update(HostVO vo) throws RemoteException, FinderException;
    public void delete(HostVO vo) throws RemoteException, RemoveException; 
    public void delete(int id) throws RemoteException, RemoveException; 
    
    // read methods   
	public HostVO retrieveByPrimaryKey(HostVO vo) throws RemoteException, FinderException;
	public HostVO retrieveByPrimaryKey(int id) throws RemoteException, FinderException;
	public Collection retrieveAll() throws RemoteException, FinderException;
}
