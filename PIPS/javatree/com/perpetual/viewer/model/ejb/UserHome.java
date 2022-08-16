/*
 * UserHome.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.viewer.model.ejb;

import javax.ejb.EJBHome;
import com.perpetual.viewer.model.vo.UserVO;
import java.util.Collection;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
/**
 *
 * @author  brunob
 */
public interface UserHome extends EJBHome
{
    public User create(UserVO pUser) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public User findByPrimaryKey( UserPK primaryKey ) throws FinderException, RemoteException;
    public Collection findByUserId(String pId) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    public Collection findByDomain(String pDomainName) throws FinderException,RemoteException;
    public Collection findByRole(int pRole) throws FinderException,RemoteException;
}
