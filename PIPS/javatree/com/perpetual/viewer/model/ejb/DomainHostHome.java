package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.DomainHostVO;


public interface DomainHostHome extends EJBHome
{
    public DomainHost create(DomainHostVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public DomainHost findByPrimaryKey( DomainHostPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByDomainId( int pDomainId ) throws FinderException, RemoteException;
	public Collection findByHostId( int pHostId ) throws FinderException, RemoteException;
}
