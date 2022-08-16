
package com.perpetual.viewer.control.ejb.crud.user;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.ejb.UserPK;
import com.perpetual.viewer.model.vo.UserVO;


public class UserCRUDBean implements SessionBean {

	private static PerpetualC2Logger sLog = new PerpetualC2Logger(UserCRUDBean.class);
	
	UserHome userHome = null;
	
	public void ejbCreate() 
	{
		InitialContext context = null;

		try {
			context = new InitialContext();
			Object reference = context.lookup("perpetual/User");
			this.userHome = (UserHome) PortableRemoteObject.narrow(reference,
					UserHome.class);
		} catch (NamingException e) {
			sLog.error("Cannot lookup User entity: reason = " + e.getMessage());
		}
	}
    
	public UserCRUDBean() {
	}
	
	public UserVO create (UserVO vo) throws RemoteException, CreateException
	{
		vo.setId(-1);		
		User user = this.userHome.create(vo);
		
		// populate VO with generated id 
		return createNewVOFromBean(user);
	}
	
	public void update (UserVO vo) throws RemoteException, FinderException
	{
		User user = this.userHome.findByPrimaryKey(new UserPK(vo.getId()));

		// populate bean with data from VO
		populateBeanFromVO(user, vo);
	}


	public void delete (UserVO vo) throws RemoteException, RemoveException
	{
		try {
		
			User host = this.userHome.findByPrimaryKey(new UserPK(vo.getId()));
			
			host.remove();
			
		} catch (FinderException e) {
			// do nothing - not in DB
		}
	}

	public void delete (int id) throws RemoteException, RemoveException
	{
		userHome.remove(new UserPK(id));
	}
	
	public UserVO retrieveByPrimaryKey (UserVO vo) throws RemoteException, FinderException
	{
		User user = this.userHome.findByPrimaryKey(new UserPK(vo.getId()));
		
		return createNewVOFromBean(user);
	}

	public UserVO retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		User user = this.userHome.findByPrimaryKey(new UserPK(id));
		return createNewVOFromBean(user);
	}

	public Collection retrieveAll () throws RemoteException, FinderException
	{
		List vos = new ArrayList();
		for (Iterator i = userHome.findAll().iterator(); i.hasNext(); )
			vos.add(createNewVOFromBean((User)i.next()));
		return vos;
	}

	public Collection retrieveAllByDomainName(String domainName) throws RemoteException, FinderException
	{
		List vos = new ArrayList();
		for (Iterator i = userHome.findByDomain(domainName).iterator(); i.hasNext(); )
			vos.add(createNewVOFromBean((User)i.next()));
		return vos;
	}

	private void populateBeanFromVO (User user, UserVO vo) throws RemoteException {
		user.setUserid(vo.getUserId());
		if (vo.getPassword() != null)		// null means leave intact
			user.setUserpass(vo.getPassword());
		user.setServicedomain(vo.getServiceDomain());
		user.setPhone(vo.getPhone());
		user.setEmail(vo.getEmail());
		user.setRealname(vo.getRealname());
		user.setRoleid(vo.getRoleId());
		user.setEnabled(vo.isEnabled());
		user.setRealname(vo.getRealname());
	}
	
	private UserVO createNewVOFromBean (User user) throws RemoteException {
		return new UserVO(user.getUserid(), user.getUserpass(),
			user.getServicedomain(), user.getPhone(), user.getEmail(),
			user.getRealname(),  user.getRoleid(), user.getId(), user.getEnabled());
	}

	public void setSessionContext(SessionContext sessionContext)
		throws EJBException,RemoteException
	{
	}
   
	public void ejbRemove() throws RemoteException
	{
	}
   
	public void ejbActivate() throws RemoteException
	{
	}

	public void ejbPassivate()  throws RemoteException
	{
	}
}
