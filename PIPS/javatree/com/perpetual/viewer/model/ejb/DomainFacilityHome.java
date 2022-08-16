package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.DomainFacilityVO;


public interface DomainFacilityHome extends EJBHome
{
    public DomainFacility create(DomainFacilityVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public DomainFacility findByPrimaryKey( DomainFacilityPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByDomainId( int pDomainId ) throws FinderException, RemoteException;
	
}
