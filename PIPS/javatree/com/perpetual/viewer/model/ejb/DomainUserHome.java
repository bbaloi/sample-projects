package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.DomainUserVO;


public interface DomainUserHome extends EJBHome
{
    public DomainUser create(DomainUserVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public DomainUser findByPrimaryKey( DomainUserPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByDomainId( int pDomainId ) throws FinderException, RemoteException;
	public Collection findByUserId( int pUserId ) throws FinderException, RemoteException;
}
