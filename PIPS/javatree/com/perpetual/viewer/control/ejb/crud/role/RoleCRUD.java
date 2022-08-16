package com.perpetual.viewer.control.ejb.crud.role;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.perpetual.viewer.model.vo.RoleVO;

public interface RoleCRUD extends javax.ejb.EJBObject
{
    public RoleVO create(RoleVO vo) throws RemoteException, CreateException;
    public void update(RoleVO vo) throws RemoteException, FinderException;
    public void delete(RoleVO vo) throws RemoteException, RemoveException; 
    public void delete(int id) throws RemoteException, RemoveException; 
    
    // read methods   
	public RoleVO retrieveByPrimaryKey (RoleVO vo) throws RemoteException, FinderException;
	public RoleVO retrieveByPrimaryKey (int id) throws RemoteException, FinderException;
	public Collection retrieveAll() throws RemoteException, FinderException;

	public Collection retrieveAllActions() throws RemoteException, FinderException;

	// RoleCRUD is courteous enough to host a few half related functions
	//
	public Collection retrieveAllActionsForRole(int roleId) throws RemoteException, FinderException;
	public void addActionsToRole(String[] actionIds, int roleId) throws RemoteException, FinderException, CreateException;
	public void removeActionsFromRole(String[] actionIds, int roleId) throws RemoteException, FinderException, RemoveException;
}


