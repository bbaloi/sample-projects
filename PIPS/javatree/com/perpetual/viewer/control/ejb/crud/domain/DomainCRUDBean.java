
package com.perpetual.viewer.control.ejb.crud.domain;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.perpetual.util.Constants;
import com.perpetual.util.EJBLoader;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.ejb.Domain;
import com.perpetual.viewer.model.ejb.DomainFacility;
import com.perpetual.viewer.model.ejb.DomainFacilityHome;
import com.perpetual.viewer.model.ejb.DomainHome;
import com.perpetual.viewer.model.ejb.DomainHost;
import com.perpetual.viewer.model.ejb.DomainHostHome;
import com.perpetual.viewer.model.ejb.DomainMessage;
import com.perpetual.viewer.model.ejb.DomainMessageData;
import com.perpetual.viewer.model.ejb.DomainMessageHome;
import com.perpetual.viewer.model.ejb.DomainPK;
import com.perpetual.viewer.model.ejb.DomainSeverity;
import com.perpetual.viewer.model.ejb.DomainSeverityHome;
import com.perpetual.viewer.model.ejb.DomainUser;
import com.perpetual.viewer.model.ejb.DomainUserHome;
import com.perpetual.viewer.model.ejb.Facility;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.FacilityPK;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.HostPK;
import com.perpetual.viewer.model.ejb.MessagePattern;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.MessagePatternHome;
import com.perpetual.viewer.model.ejb.Severity;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.model.ejb.SeverityPK;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.ejb.UserPK;
import com.perpetual.viewer.model.vo.DomainFacilityVO;
import com.perpetual.viewer.model.vo.DomainHostVO;
import com.perpetual.viewer.model.vo.DomainSeverityVO;
import com.perpetual.viewer.model.vo.DomainUserVO;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.SingleIdVO;


public class DomainCRUDBean implements SessionBean {

	private static PerpetualC2Logger sLog = new PerpetualC2Logger(DomainCRUDBean.class);
	
	private DomainHome domainHome;
	private DomainSeverityHome domainSeverityHome;
	private DomainFacilityHome domainFacilityHome;
	private DomainHostHome domainHostHome;
	private DomainUserHome domainUserHome;
	private DomainMessageHome domainMessageHome;
	private UserHome userHome;
	
	public void ejbCreate() 
	{
		InitialContext context = null;

		sLog.info("Caching home interfaces ...");
		try
		{
			domainHome = (DomainHome)EJBLoader.getEJBHome("perpetual/Domain", DomainHome.class);
			domainSeverityHome = (DomainSeverityHome)EJBLoader.getEJBHome("perpetual/DomainSeverity", DomainSeverityHome.class);
			domainFacilityHome = (DomainFacilityHome)EJBLoader.getEJBHome("perpetual/DomainFacility", DomainFacilityHome.class);
			domainHostHome = (DomainHostHome)EJBLoader.getEJBHome("perpetual/DomainHost", DomainHostHome.class);
			domainUserHome = (DomainUserHome)EJBLoader.getEJBHome("perpetual/DomainUser", DomainUserHome.class);
			domainMessageHome = (DomainMessageHome) EJBLoader.getEJBHome("DomainMessage", DomainMessageHome.class);
			
			// should really use the UserCRUD, but ...
			userHome = (UserHome)EJBLoader.getEJBHome("perpetual/User", UserHome.class);
		}
		catch (Exception e)
		{
			sLog.error("Cannot lookup entities: reason = " + e.getMessage());
		}
	}
    
	public DomainVO create (DomainVO vo) throws RemoteException, CreateException
	{
		DomainVO resultVO;
		
		vo.setId(-1);		
		Domain domain = this.domainHome.create(vo);

		// populate VO with generated id 
		return createNewVOFromBean(domain);
	}
	
	public DomainVO create (DomainVO vo, Collection users, Collection hosts,
				Collection facilities, Collection severities, Collection messages)
				throws RemoteException, CreateException
	{
		 
		DomainVO resultVO = create(vo);
		
//		createUsers(resultVO, users);
		createHosts(resultVO, hosts);
		createFacilities(resultVO, facilities);
		createSeverities(resultVO, severities);
		createMessages(resultVO, messages);
		
		return resultVO;
		
	}
	
	public DomainVO update (DomainVO vo)
					throws RemoteException, FinderException
	
	{
		Domain domain = this.domainHome.findByPrimaryKey(new DomainPK(vo.getId()));
		
		populateBeanFromVO(domain, vo);
		
		return vo;
		
	}
	
	public DomainVO update (DomainVO vo, Collection users, Collection hosts,
				Collection facilities, Collection severities, Collection messages)
				throws RemoteException, CreateException, FinderException, RemoveException
	{
		update(vo);

//		deleteUsers(vo); // user-domain link is via user.serviceDomain - will change at some point
//		createUsers(vo, users);
		deleteHosts(vo);
		createHosts(vo, hosts);
		deleteFacilities(vo);
		createFacilities(vo, facilities);
		deleteSeverities(vo);
		createSeverities(vo, severities);
		deleteMessages(vo);
		createMessages(vo, messages);
		
		return vo;		
	}


	public void delete (DomainVO vo) throws RemoteException, RemoveException
	{
		try {
			// update
		
			Domain domain = this.domainHome.findByPrimaryKey(
					new DomainPK(vo.getId()));
			
			// update various association tables
			//deleteUsers(vo);
			deleteUsersFromDomain(domain);
			deleteHosts(vo);
			deleteFacilities(vo);			
			deleteSeverities(vo);
			deleteMessages(vo);
			
			domain.remove();
			
		} catch (FinderException e) {
			// do nothing - not in DB
		}
	}
	
	public DomainVO retrieveByPrimaryKey (DomainVO vo)
			throws RemoteException, FinderException
	{
		Domain domain = this.domainHome.findByPrimaryKey(new DomainPK(vo.getId()));
		
		return createNewVOFromBean(domain);
	}
	
	public DomainVO retrieveByDomainName (String domainName)
			throws RemoteException, FinderException
	{
		Domain domain = this.domainHome.findByDomainName(domainName);
		
		return createNewVOFromBean(domain);
	}
	
	public Collection retrieveAll ()
			throws RemoteException, FinderException
	{
		List domainVOs = new ArrayList();
		Collection hosts = this.domainHome.findAll();
		
		for (Iterator i = hosts.iterator(); i.hasNext();) {
			Domain domain = (Domain) i.next();
			
			domainVOs.add(createNewVOFromBean(domain));
		}
		
		return domainVOs;
	}
	
	// returns a Collection of SeverityVOs
	public Collection retrieveAllSeveritiesFor (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection domainSeverities = this.domainSeverityHome
				.findByDomainId(vo.getId());
				
		List severities = new ArrayList();
		
		for (Iterator i = domainSeverities.iterator(); i.hasNext(); ) {
			DomainSeverity ds = (DomainSeverity) i.next();
			
			try {
				SeverityHome severityHome = (SeverityHome)PortableRemoteObject
										.narrow(ServiceLocator.getServiceLocatorInstance().
											getContext().lookup(Constants.jndiName_Severity),
											SeverityHome.class);
				Severity severity = severityHome.findByPrimaryKey(
						new SeverityPK(ds.getSeverityId().intValue()));
				
				SeverityVO svo = new SeverityVO(severity.getId(), severity.getName());
				severities.add(svo);		
										
			} catch (NamingException e) {
				throw new RemoteException(e.getMessage());
			}
		}
				
		return severities;		
	}
	
	// returns a Collection of FacilityVOs
	public Collection retrieveAllFacilitiesFor (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection domainFacilities = this.domainFacilityHome
				.findByDomainId(vo.getId());
				
		List facilities = new ArrayList();
		
		for (Iterator i = domainFacilities.iterator(); i.hasNext(); ) {
			DomainFacility df = (DomainFacility) i.next();
			
			try {
				FacilityHome facilityHome = (FacilityHome)PortableRemoteObject
										.narrow(ServiceLocator.getServiceLocatorInstance().
											getContext().lookup(Constants.jndiName_Facility),
											FacilityHome.class);
				Facility facility = facilityHome.findByPrimaryKey(
						new FacilityPK(df.getFacilityId().intValue()));
				
				facilities.add(new FacilityVO(facility.getId(), facility.getName()));		
											
			} catch (NamingException e) {
				throw new RemoteException(e.getMessage());
			}
		}
				
		return facilities;		
	}

	//	returns a Collection of HostVOs
	public Collection retrieveAllHostsFor (DomainVO vo)
			 throws FinderException, RemoteException
	{
		return retrieveAllHostsFor(vo.getId());
	}

	//	returns a Collection of HostVOs
	public Collection retrieveAllHostsFor (int id)
			 throws FinderException, RemoteException
	{
		Collection domainHosts = this.domainHostHome.findByDomainId(id);

		List hosts = new ArrayList();

		for (Iterator i = domainHosts.iterator(); i.hasNext(); ) {
			DomainHost dh = (DomainHost) i.next();
			
			try {
				HostHome hostHome = (HostHome)PortableRemoteObject
										.narrow(ServiceLocator.getServiceLocatorInstance().
											getContext().lookup(Constants.jndiName_Host),
											HostHome.class);
				Host host = hostHome.findByPrimaryKey(
						new HostPK(dh.getHostId().intValue()));
				
				hosts.add(new HostVO(host.getId(), host.getName(), host.getDescription()));		
											
			} catch(NamingException e){
				throw new RemoteException(e.getMessage());
			}
		}
				
		return hosts;		
	}
	
	// returns a Collection of SeverityVOs
	public Collection retrieveAllMessagePatternsFor (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection domainMessages = this.domainMessageHome
				.findByDomainId(vo.getId());
				
		List messages = new ArrayList();
		
		for (Iterator i = domainMessages.iterator(); i.hasNext(); ) {
			DomainMessage dm = (DomainMessage) i.next();
			
			try {
				MessagePatternHome messageHome = (MessagePatternHome)PortableRemoteObject
										.narrow(ServiceLocator.getServiceLocatorInstance().
											getContext().lookup(Constants.jndiName_MessagePattern),
											MessagePatternHome.class);
				MessagePattern message = messageHome.findByPrimaryKey(dm.getMessageid());
				
				MessagePatternData messageVO =
					new MessagePatternData(message.getId(),
						message.getName(), message.getPattern(),
						message.getDescription());
				messages.add(messageVO);		
										
			} catch (NamingException e) {
				throw new RemoteException(e.getMessage());
			}
		}
				
		return messages;		
	}
	
	// get a Collection of severity VOs that are NOT in the Domain
	public Collection retrieveAllSeveritiesNotIn (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection severitiesInDomain = retrieveAllSeveritiesFor(vo);
		
		SeverityHome severityHome;
		try {
			severityHome =
				(SeverityHome) PortableRemoteObject.narrow(
					ServiceLocator.getServiceLocatorInstance().getContext().lookup(
						Constants.jndiName_Severity),
					SeverityHome.class);
		} catch (NamingException e) {
			throw new RemoteException(e.getMessage());
		}
				
		Collection allSeverities = severityHome.findAll();
		List severitiesNotInDomain = new ArrayList();
		
		for (Iterator i = allSeverities.iterator(); i.hasNext(); ) {
			Severity severity = (Severity) i.next();
			
			if (!isIdInCollectionOfSingleIDVOs(severity.getId(), severitiesInDomain)) {
				severitiesNotInDomain.add(new SeverityVO(severity.getId(),
					severity.getName()));
			}
		}
		
		return severitiesNotInDomain;		
	}
	
	// get a Collection of severity VOs that are NOT in the Domain
	public Collection retrieveAllFacilitiesNotIn (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection facilitiesInDomain = retrieveAllFacilitiesFor(vo);
		
		FacilityHome facilityHome;
		try {
			facilityHome =
				(FacilityHome) PortableRemoteObject.narrow(
					ServiceLocator.getServiceLocatorInstance().getContext().lookup(
						Constants.jndiName_Facility),
					FacilityHome.class);
		} catch (NamingException e) {
			throw new RemoteException(e.getMessage());
		}
				
		Collection allFacilities = facilityHome.findAll();
		List facilitiesNotInDomain = new ArrayList();
		
		for (Iterator i = allFacilities.iterator(); i.hasNext(); ) {
			Facility facility = (Facility) i.next();
			
			if (!isIdInCollectionOfSingleIDVOs(facility.getId(), facilitiesInDomain)) {
				facilitiesNotInDomain.add(new FacilityVO(facility.getId(),
					facility.getName()));
			}
		}
		
		return facilitiesNotInDomain;		
	}
	
//	get a Collection of host VOs that are NOT in the Domain
	 public Collection retrieveAllHostsNotIn (DomainVO vo)
			 throws FinderException, RemoteException
	 {
		 Collection hostsInDomain = retrieveAllHostsFor(vo);
		
		 HostHome hostHome;
		 try {
			 hostHome =
				 (HostHome) PortableRemoteObject.narrow(
					 ServiceLocator.getServiceLocatorInstance().getContext().lookup(
						 Constants.jndiName_Host),
					 HostHome.class);
		 } catch (NamingException e) {
			 throw new RemoteException(e.getMessage());
		 }
				
		 Collection allHosts = hostHome.findAll();
		 List hostsNotInDomain = new ArrayList();
		
		 for (Iterator i = allHosts.iterator(); i.hasNext(); ) {
			 Host host = (Host) i.next();
			
			 if (!isIdInCollectionOfSingleIDVOs(host.getId(), hostsInDomain)) {
				 hostsNotInDomain.add(new HostVO(host.getId(),
					 host.getName(), host.getDescription()));
			 }
		 }
		
		 return hostsNotInDomain;		
	 }

	// get a Collection of message pattern VOs that are NOT in the Domain
	public Collection retrieveAllMessagePatternsNotIn (DomainVO vo)
			throws FinderException, RemoteException
	{
		Collection messagesInDomain = retrieveAllMessagePatternsFor(vo);
		
		MessagePatternHome messagePatternHome;
		try {
			messagePatternHome =
				(MessagePatternHome) PortableRemoteObject.narrow(
					ServiceLocator.getServiceLocatorInstance().getContext().lookup(
						Constants.jndiName_MessagePattern), MessagePatternHome.class);
		} catch (NamingException e) {
			throw new RemoteException(e.getMessage());
		}
				
		Collection allMessages = messagePatternHome.findAll();
		List messagesNotInDomain = new ArrayList();
		
		for (Iterator i = allMessages.iterator(); i.hasNext(); ) {
			MessagePattern message = (MessagePattern) i.next();
			
			// look for message pattern id in those that are defined for the domain

			boolean found = false;
		
			for (Iterator j = messagesInDomain.iterator(); !found && j.hasNext(); ) {
				MessagePatternData data = (MessagePatternData) j.next();
				found = (data.getId().intValue() == message.getId().intValue());
			}
		
			if (!found) {
				messagesNotInDomain.add(new MessagePatternData(message.getId(),
					message.getName(), message.getPattern(),
					message.getDescription()));
			}
		}
		
		return messagesNotInDomain;		
	}	 
	
	public Collection retrieveAllUsersFor(int id) throws RemoteException, FinderException
	{
		Collection domainUsers = domainUserHome.findByDomainId(id);
		Collection userVOs = new ArrayList();
		for (Iterator i = domainUsers.iterator(); i.hasNext(); )
		{
			DomainUser domainUser = (DomainUser)i.next();
			User user = userHome.findByPrimaryKey(new UserPK(domainUser.getUserId().intValue()));
			userVOs.add(user.getVO());
		}
		return userVOs;
	}
	public Collection retrieveAllUsersNotIn(int id) throws RemoteException, FinderException
	{
		Collection users = userHome.findAll();
		Collection userVOs = new ArrayList();
		for (Iterator i = users.iterator(); i.hasNext(); )
		{
			User user = (User)i.next();
			userVOs.add(user.getVO());
		}
		userVOs.removeAll(retrieveAllUsersFor(id));
		return userVOs;
	}

	public void addUsersToDomain(String[] userIds, int domainId) throws RemoteException, FinderException, CreateException
	{
		Collection idList = Arrays.asList(userIds);
		Collection domainUsers = domainUserHome.findByDomainId(domainId);
		for (Iterator i = domainUsers.iterator(); i.hasNext(); )
		{
			DomainUser domainUser = (DomainUser)i.next();
			idList.remove(domainUser.getUserId().toString());		// Integer
		}
		for (Iterator i = idList.iterator(); i.hasNext(); )
		{
			domainUserHome.create(new DomainUserVO(-1, new Integer(domainId), new Integer((String)i.next())));
		}
	}

	public void removeUsersFromDomain(String[] userIds, int domainId) throws RemoteException, FinderException, RemoveException
	{
		Collection idList = Arrays.asList(userIds);
		Collection domainUsers = domainUserHome.findByDomainId(domainId);
		for (Iterator i = domainUsers.iterator(); i.hasNext(); )
		{
			DomainUser domainUser = (DomainUser)i.next();
			if (idList.contains(domainUser.getUserId().toString()))		// Integer
				domainUser.remove();
		}
	}

	public Collection retrieveAllEnabledUsersFor(int id, boolean enabled) throws RemoteException, FinderException
	{
		Collection domainUsers = domainUserHome.findByDomainId(id);
		Collection userVOs = new ArrayList();
		for (Iterator i = domainUsers.iterator(); i.hasNext(); )
		{
			DomainUser domainUser = (DomainUser)i.next();
			User user = userHome.findByPrimaryKey(new UserPK(domainUser.getUserId().intValue()));
			if (user.getEnabled() == enabled)
				userVOs.add(user.getVO());
		}
		return userVOs;
	}

	// if VO inherited from common class with id get/set, could have a single class
	// for this, but...
	private boolean isIdInCollectionOfSingleIDVOs (int id, Collection vos)
	{
		boolean found = false;
		
		for (Iterator i = vos.iterator(); !found && i.hasNext(); ) {
			SingleIdVO sivo = (SingleIdVO) i.next();
			found = (sivo.getId() == id);
		}
		
		return found;
	}
	
	private void populateBeanFromVO (Domain domain, DomainVO vo) throws RemoteException {
		domain.setName(vo.getName());
		domain.setCollectionInterval(vo.getCollectionInterval());
		domain.setSummaryProcessingEnabled(vo.getSummaryProcessingEnabled());
		domain.setLastCollectionTime(vo.getLastCollectionTime());
	}
	
	private DomainVO createNewVOFromBean (Domain domain) throws RemoteException {
		return new DomainVO(domain.getId(), domain.getName(), 
				domain.getSummaryProcessingEnabled(),
				domain.getLastCollectionTime(),
				domain.getCollectionInterval());
	}

	private void deleteUsers (DomainVO vo) throws RemoteException,
			RemoveException, FinderException
	{
		Collection domainUsers = this.domainUserHome.findByDomainId(vo.getId());
		
		for (Iterator i = domainUsers.iterator(); i.hasNext(); ) {
			DomainUser domainUser = (DomainUser) i.next();
			domainUser.remove();
		}
	}
	
	private void deleteUsersFromDomain (Domain domain) throws RemoteException,
		RemoveException, FinderException
	{	
//System.out.println("delete all users from domain = " + domain.getName());
// changed by michael@perpetual-ip.com 09/29/2003 from System.out.println to sLog.info

sLog.info("delete all users from domain = " + domain.getName());

		Collection users = this.userHome.findByDomain(domain.getName());
//System.out.println("found " + users.size() +  " users from domain = " + domain.getName());
// changed by michael@perpetual-ip.com 09/29/2003 from System.out.println to sLog.info

sLog.info("found " + users.size() + " users from domain = " + domain.getName());
		for (Iterator i = users.iterator(); i.hasNext(); ) {			
			User user = (User) i.next();

//System.out.println("updating user " + user.getUserid());
// changed by michael@perpetual-ip.com 09/29/2003 from System.out.println to sLog.info

sLog.info("update user " + user.getUserid());

			user.setEnabled(false);
			user.setServicedomain(null);
		}		
	}

/*
 	private void createUsers (DomainVO vo, Collection users)
 			throws RemoteException, CreateException
 	{
 		if (users != null) {
	 		for (Iterator i = users.iterator(); i.hasNext(); ) {
	 			UserVO userVO = (UserVO) i.next();
	 			DomainUserVO domainUserVO = new DomainUserVO(-1,
	 						new Integer(vo.getId()), new Integer(userVO.getId()));
	 			this.domainUserHome.create(domainUserVO);		
	 		}
 		}
 	}
*/

	private void deleteHosts (DomainVO vo) throws RemoteException,
			RemoveException, FinderException
	{
		Collection domainHosts = this.domainHostHome.findByDomainId(vo.getId());
		
		for (Iterator i = domainHosts.iterator(); i.hasNext(); ) {
			DomainHost domainHost = (DomainHost) i.next();
			domainHost.remove();
		}
	}

	private void createHosts (DomainVO vo, Collection hosts)
			throws RemoteException, CreateException
	{
		if (hosts != null) {
			for (Iterator i = hosts.iterator(); i.hasNext(); ) {
				HostVO hostVO = (HostVO) i.next();
				DomainHostVO domainHostVO = new DomainHostVO(-1,
							new Integer(vo.getId()), new Integer(hostVO.getId()));
				this.domainHostHome.create(domainHostVO);		
			}
		}
	}
	

	private void createSeverities (DomainVO vo, Collection severities)
			throws RemoteException, CreateException
	{
		if (severities != null) {
			for (Iterator i = severities.iterator(); i.hasNext(); ) {
				SeverityVO severityVO = (SeverityVO) i.next();
				DomainSeverityVO domainSeverityVO = new DomainSeverityVO(-1,
							new Integer(vo.getId()), new Integer(severityVO.getId()));
				this.domainSeverityHome.create(domainSeverityVO);		
			}
		}
	}

	private void deleteFacilities (DomainVO vo) throws RemoteException,
			RemoveException, FinderException
	{
		Collection domainFacilities = this.domainFacilityHome.findByDomainId(vo.getId());
		
		for (Iterator i = domainFacilities.iterator(); i.hasNext(); ) {
			DomainFacility domainFacility = (DomainFacility) i.next();
			domainFacility.remove();
		}
	}

	private void createFacilities (DomainVO vo, Collection facilities)
			throws RemoteException, CreateException
	{
		if (facilities != null) {
			for (Iterator i = facilities.iterator(); i.hasNext(); ) {
				FacilityVO facilityVO = (FacilityVO) i.next();
				DomainFacilityVO domainFacilityVO = new DomainFacilityVO(-1,
							new Integer(vo.getId()), new Integer(facilityVO.getId()));
				this.domainFacilityHome.create(domainFacilityVO);		
			}
		}
	}
	
	private void deleteSeverities (DomainVO vo) throws RemoteException,
			RemoveException, FinderException
	{
		Collection domainSeverities = this.domainSeverityHome.findByDomainId(vo.getId());
		
		for (Iterator i = domainSeverities.iterator(); i.hasNext(); ) {
			DomainSeverity domainSeverity = (DomainSeverity) i.next();
			domainSeverity.remove();
		}
	}

	private void createMessages (DomainVO vo, Collection messages)
			throws RemoteException, CreateException
	{
		if (messages != null) {
			for (Iterator i = messages.iterator(); i.hasNext(); ) {
				MessagePatternData messageVO = (MessagePatternData) i.next();
				DomainMessageData domainMessageVO = new DomainMessageData(
							null,
							new Integer(vo.getId()),
							messageVO.getId());
				this.domainMessageHome.create(domainMessageVO);		
			}
		}
	}

	private void deleteMessages (DomainVO vo) throws RemoteException,
			RemoveException, FinderException
	{
		Collection domainMessages =
			this.domainMessageHome.findByDomainId(vo.getId());
		
		for (Iterator i = domainMessages.iterator(); i.hasNext(); ) {
			DomainMessage domainMessage = (DomainMessage) i.next();
			domainMessage.remove();
		}
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
