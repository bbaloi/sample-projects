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

import com.perpetual.viewer.model.vo.DomainVO;
/**
 *
 * @author  brunob
 */
public interface DomainHome extends EJBHome
{
    public Domain create(DomainVO vo) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public Domain findByPrimaryKey( DomainPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Domain findByDomainName(String pDomainName)  throws FinderException, RemoteException;
}
