package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.RoleActionVO;
/**
 *
 * @author  brunob
 */
public interface RoleActionHome extends EJBHome
{
    public RoleAction create(RoleActionVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public RoleAction findByPrimaryKey( RoleActionPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByRoleId(int pRoleId) throws FinderException, RemoteException;
}
