package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.DomainSeverityVO;


public interface DomainSeverityHome extends EJBHome
{
    public DomainSeverity create(DomainSeverityVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public DomainSeverity findByPrimaryKey( DomainSeverityPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByDomainId( int pDomainId ) throws FinderException, RemoteException;

}
