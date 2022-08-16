package com.perpetual.viewer.control.ejb.crud.domain;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.perpetual.viewer.model.vo.DomainVO;

public interface DomainCRUD extends javax.ejb.EJBObject
{
	public DomainVO create(DomainVO vo) throws RemoteException, CreateException;
    public DomainVO create(DomainVO vo, Collection users, Collection hosts,
    	Collection facilities, Collection severities, Collection messages) throws RemoteException, CreateException;

	public DomainVO update(DomainVO vo) throws RemoteException, FinderException;    		
	public DomainVO update(DomainVO vo, Collection users, Collection hosts,
			Collection facilities, Collection severities, Collection messages) throws RemoteException, CreateException, FinderException, RemoveException;

	// cascade delete	
    public void delete(DomainVO vo) throws RemoteException, RemoveException;
    
	// read methods   
	public DomainVO retrieveByPrimaryKey (DomainVO vo) throws RemoteException, FinderException;
	public DomainVO retrieveByDomainName (String domainName) throws RemoteException, FinderException;
	public Collection retrieveAllSeveritiesFor (DomainVO vo) throws RemoteException, FinderException;
	public Collection retrieveAllFacilitiesFor (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllHostsFor (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllHostsFor (int id) throws FinderException, RemoteException;
	public Collection retrieveAllMessagePatternsFor (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllSeveritiesNotIn (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllFacilitiesNotIn (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllHostsNotIn (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAllMessagePatternsNotIn (DomainVO vo) throws FinderException, RemoteException;
	public Collection retrieveAll() throws RemoteException, FinderException; 
	public Collection retrieveAllUsersFor(int id) throws RemoteException, FinderException; 
	public Collection retrieveAllUsersNotIn(int id) throws RemoteException, FinderException; 
	public void addUsersToDomain(String[] userIds, int domainId) throws RemoteException, FinderException, CreateException;
	public void removeUsersFromDomain(String[] userIds, int domainId) throws RemoteException, FinderException, RemoveException;
	public Collection retrieveAllEnabledUsersFor(int id, boolean enabled) throws RemoteException, FinderException; 
}


