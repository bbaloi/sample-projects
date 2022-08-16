/*
 * UserHome.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBHome;
import javax.ejb.FinderException;

import com.perpetual.viewer.model.vo.HostVO;
/**
 *
 * @author  brunob
 */
public interface HostHome extends EJBHome
{
    public Host create(HostVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public Host findByPrimaryKey( HostPK primaryKey ) throws FinderException, RemoteException;
	public Collection findAll() throws FinderException, RemoteException;
}
