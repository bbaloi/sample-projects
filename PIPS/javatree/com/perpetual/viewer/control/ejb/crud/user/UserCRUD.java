package com.perpetual.viewer.control.ejb.crud.user;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.perpetual.viewer.model.vo.UserVO;

public interface UserCRUD extends javax.ejb.EJBObject
{
    public UserVO create(UserVO vo) throws RemoteException, CreateException;
    public void update(UserVO vo) throws RemoteException, FinderException;
    public void delete(UserVO vo) throws RemoteException, RemoveException; 
    public void delete(int id) throws RemoteException, RemoveException; 
    
    // read methods   
	public UserVO retrieveByPrimaryKey (UserVO vo) throws RemoteException, FinderException;
	public UserVO retrieveByPrimaryKey (int id) throws RemoteException, FinderException;
	public Collection retrieveAll() throws RemoteException, FinderException;
	public Collection retrieveAllByDomainName(String domainName) throws RemoteException, FinderException;
}


