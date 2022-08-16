
package com.perpetual.viewer.control.ejb.crud.role;

import java.rmi.RemoteException;
import java.util.*;

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
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.ejb.Role;
import com.perpetual.viewer.model.ejb.RoleHome;
import com.perpetual.viewer.model.ejb.RolePK;
import com.perpetual.viewer.model.ejb.ActionHome;
import com.perpetual.viewer.model.ejb.Action;
import com.perpetual.viewer.model.ejb.ActionPK;
import com.perpetual.viewer.model.ejb.RoleActionHome;
import com.perpetual.viewer.model.ejb.RoleAction;
import com.perpetual.viewer.model.ejb.RoleActionPK;
import com.perpetual.viewer.model.vo.RoleVO;
import com.perpetual.viewer.model.vo.ActionVO;
import com.perpetual.viewer.model.vo.RoleActionVO;


public class RoleCRUDBean implements SessionBean
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger(RoleCRUDBean.class);

	RoleHome roleHome;
	ActionHome actionHome;
	RoleActionHome roleActionHome;

	public void ejbCreate() 
	{
		InitialContext context = null;

		try
		{
			roleActionHome = (RoleActionHome)EJBLoader.getEJBHome("perpetual/RoleAction", RoleActionHome.class);
			actionHome = (ActionHome)EJBLoader.getEJBHome("perpetual/Action", ActionHome.class);
			roleHome = (RoleHome)EJBLoader.getEJBHome("perpetual/Role", RoleHome.class);
//			context = new InitialContext();
//			Object reference = context.lookup("perpetual/Role");
//			this.roleHome = (RoleHome) PortableRemoteObject.narrow(reference,
//					RoleHome.class);
		}
		catch (Exception e)
		{
			sLog.error("Cannot lookup Role entity: reason = " + e.getMessage());
		}
	}

	public RoleCRUDBean()
	{
	}

	public RoleVO create (RoleVO vo) throws RemoteException, CreateException
	{
		vo.setId(-1);		
		Role role = this.roleHome.create(vo);
		
		// populate VO with generated id 
		return createNewVOFromBean(role);
	}
	
	public void update (RoleVO vo) throws RemoteException, FinderException
	{
		Role role = this.roleHome.findByPrimaryKey(new RolePK(vo.getId()));

		// populate bean with data from VO
		populateBeanFromVO(role, vo);
	}


	public void delete (RoleVO vo) throws RemoteException, RemoveException
	{
		delete(vo.getId());
	}

	public void delete(int id) throws RemoteException, RemoveException
	{
		roleHome.remove(new RolePK(id));
	}
	
	public RoleVO retrieveByPrimaryKey (RoleVO vo) throws RemoteException, FinderException
	{
		Role role = this.roleHome.findByPrimaryKey(new RolePK(vo.getId()));
		
		return createNewVOFromBean(role);
	}
	
	public RoleVO retrieveByPrimaryKey (int id) throws RemoteException, FinderException
	{
		Role role = this.roleHome.findByPrimaryKey(new RolePK(id));
		
		return createNewVOFromBean(role);
	}
	
	public Collection retrieveAll () throws RemoteException, FinderException
	{
		List roleVOs = new ArrayList();
		Collection roles = this.roleHome.findAll();
		
		for (Iterator i = roles.iterator(); i.hasNext();) {
			Role role = (Role) i.next();
			
			roleVOs.add(createNewVOFromBean(role));
		}
		
		return roleVOs;
	}

	public Collection retrieveAllActions() throws RemoteException, FinderException
	{
		Collection col = new ArrayList();
		for (Iterator i = ((ActionHome)EJBLoader.getEJBHome("perpetual/Action", ActionHome.class)).findAll().iterator(); i.hasNext(); )
		{
			col.add(((Action)i.next()).getVO());
		}
		return col;
	}

	// role <--> action relationships
	//
	public Collection retrieveAllActionsForRole(int roleId) throws RemoteException, FinderException
	{
		Collection col = new ArrayList();
		for (Iterator i = roleActionHome.findByRoleId(roleId).iterator(); i.hasNext(); )
		{
			RoleAction roleAction = (RoleAction)i.next();
			Action action = actionHome.findByPrimaryKey(new ActionPK(roleAction.getActionId().intValue()));
			col.add(action.getVO());
		}
		return col;
	}

	public void addActionsToRole(String[] actionIds, int roleId) throws RemoteException, FinderException, CreateException
	{
		Collection actionIdList = Arrays.asList(actionIds);
		Collection current = roleActionHome.findByRoleId(roleId);
		for (Iterator i = current.iterator(); i.hasNext(); )
		{
			RoleAction roleAction = (RoleAction)i.next();
			actionIdList.remove(roleAction.getActionId().toString());		// Integer
		}
		for (Iterator i = actionIdList.iterator(); i.hasNext(); )
		{
			roleActionHome.create(new RoleActionVO(-1, new Integer(roleId), new Integer((String)i.next())));
		}
	}

	public void removeActionsFromRole(String[] actionIds, int roleId) throws RemoteException, FinderException, RemoveException
	{
		Collection actionIdList = Arrays.asList(actionIds);
		Collection current = roleActionHome.findByRoleId(roleId);
		for (Iterator i = current.iterator(); i.hasNext(); )
		{
			RoleAction roleAction = (RoleAction)i.next();
			if (actionIdList.contains(roleAction.getActionId().toString()))		// Integer
			{
				roleAction.remove();
			}
		}
	}

	private void populateBeanFromVO (Role role, RoleVO vo) throws RemoteException {
		role.setRoleName(vo.getRoleName());
	}
	
	private RoleVO createNewVOFromBean (Role role) throws RemoteException {
		return new RoleVO(role.getId(), role.getRoleName());
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
