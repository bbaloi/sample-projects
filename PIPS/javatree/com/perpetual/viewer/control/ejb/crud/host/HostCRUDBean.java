
package com.perpetual.viewer.control.ejb.crud.host;

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

import com.perpetual.util.EJBLoader;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.ejb.DomainHost;
import com.perpetual.viewer.model.ejb.DomainHostHome;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.HostPK;
import com.perpetual.viewer.model.vo.HostVO;


public class HostCRUDBean implements SessionBean {

	private static PerpetualC2Logger sLog = new PerpetualC2Logger(HostCRUDBean.class);

	HostHome hostHome = null;
	DomainHostHome domainHostHome = null;

	public void ejbCreate() 
	{
		InitialContext context = null;

		try {
			context = new InitialContext();
			Object reference = context.lookup("perpetual/Host");
			this.hostHome = (HostHome) PortableRemoteObject.narrow(reference,
					HostHome.class);
					
			this.domainHostHome = (DomainHostHome)
					EJBLoader.getEJBHome("perpetual/DomainHost",
						DomainHostHome.class);		
		} catch (NamingException e) {
			sLog.error("Cannot lookup Host entity: reason = " + e.getMessage());
//		} catch (RemoteException e) {
//			sLog.error("Cannot lookup Host entity: reason = " + e.getMessage());
		}
		
	}

	public HostCRUDBean() {
	}

	public HostVO create (HostVO vo) throws RemoteException, CreateException
	{
		vo.setId(-1);		
		Host host = this.hostHome.create(vo);

		// populate VO with generated id 
		return createNewVOFromBean(host);
	}

	public void update (HostVO vo) throws RemoteException, FinderException
	{
		Host host = this.hostHome.findByPrimaryKey(new HostPK(vo.getId()));

		// populate bean with data from VO
		populateBeanFromVO(host, vo);
	}


	public void delete (HostVO vo) throws RemoteException, RemoveException
	{
		removeHostAndAssociatedLinks(vo.getId());
	}

	public void delete(int id) throws RemoteException, RemoveException
	{
		removeHostAndAssociatedLinks(id);
	}
	
	public void removeHostAndAssociatedLinks (int hostId)
		throws RemoteException, RemoveException
	{
		hostHome.remove(new HostPK(hostId));
		
		// remove domain host as well - normally this would be handled by a cascade 
		// delete, but not all DB engines support this
		
		try {
			Collection domainHosts = this.domainHostHome.findByHostId(hostId);
			
			for (Iterator i = domainHosts.iterator(); i.hasNext(); ) {
				DomainHost domainHost = (DomainHost) i.next();
				domainHost.remove();
			}
		} catch (FinderException e) {
			// couldn`t find any - do nothing
		}
	}
	

	public HostVO retrieveByPrimaryKey (HostVO vo) throws RemoteException, FinderException
	{
		return retrieveByPrimaryKey(vo.getId());
	}

	public HostVO retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		return createNewVOFromBean(hostHome.findByPrimaryKey(new HostPK(id)));
	}

	public Collection retrieveAll () throws RemoteException, FinderException
	{
		List hostVOs = new ArrayList();
		Collection hosts = this.hostHome.findAll();

		for (Iterator i = hosts.iterator(); i.hasNext();) {
			Host host = (Host) i.next();

			hostVOs.add(createNewVOFromBean(host));
		}

		return hostVOs;
	}

	private void populateBeanFromVO (Host host, HostVO vo) throws RemoteException
	{
		host.setName(vo.getName());
		host.setDescription(vo.getDescription());
	}

	private HostVO createNewVOFromBean (Host host) throws RemoteException
	{
		return new HostVO(host.getId(), host.getName(), host.getDescription());
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
