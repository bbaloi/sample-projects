/*
 * Created on 3-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.viewer.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.model.ejb.Facility;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.Severity;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.UserVO;


public class DomainCRUDClient
{
  public static void main(String[] args)
  {
    try
    {
		Properties prop = new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY,
		               "org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.PROVIDER_URL, "localhost:1099");
		
		Context context = new InitialContext(prop);
		Object reference;
	
		reference = context.lookup("perpetual/Severity");		
		SeverityHome severityHome = (SeverityHome) PortableRemoteObject
			.narrow(reference, SeverityHome.class);
		reference = context.lookup("perpetual/Facility");
		FacilityHome facilityHome = (FacilityHome)
				PortableRemoteObject.narrow(reference, FacilityHome.class);
		reference = context.lookup("perpetual/User");
		UserHome userHome = (UserHome) PortableRemoteObject
			.narrow(reference, UserHome.class);
		reference = context.lookup("perpetual/Host");
		HostHome hostHome = (HostHome) PortableRemoteObject
						.narrow(reference, HostHome.class);	

		Object keyRef = context.lookup("perpetual/DomainCRUD");
		

		
		DomainCRUDHome crudHome =
			(DomainCRUDHome)javax.rmi.
					  PortableRemoteObject.narrow(keyRef, 
							  DomainCRUDHome.class);

		DomainCRUD crud = crudHome.create();

		System.out.println("testing create()");
		System.out.println("populating 'domain' tables");
		

		System.out.println("populating severity list");
		List severityVOs = new ArrayList();
		Collection severities = severityHome.findAll();
			
		for (Iterator i = severities.iterator(); i.hasNext(); ) {
			Severity severity = (Severity) i.next();
			SeverityVO svo = new SeverityVO(severity.getId(), severity.getName());
			severityVOs.add(svo);			
		}

		System.out.println("populating facility list");
		List facilityVOs = new ArrayList();
		Collection facilities = facilityHome.findAll();
			
		for (Iterator i = facilities.iterator(); i.hasNext(); ) {
			Facility facility = (Facility) i.next();
			FacilityVO fvo = new FacilityVO(facility.getId(), facility.getName());
			facilityVOs.add(fvo);			
		}
		

		System.out.println("populating host list");
		List hostVOs = new ArrayList();
		Collection hosts = hostHome.findAll();
			
		for (Iterator i = hosts.iterator(); i.hasNext(); ) {
			Host host = (Host) i.next();
			HostVO hvo = new HostVO(host.getId(), host.getName(), host.getDescription());
			hostVOs.add(hvo);			
		}
		
		System.out.println("populating user list");
		List userVOs = new ArrayList();
//		Collection users = userHome.findAll();
//			
//		for (Iterator i = users.iterator(); i.hasNext(); ) {
//			User user = (User) i.next();
//			UserVO uvo = new UserVO(user.getUserid(), user.getUserpass(), user.getServicedomain(),
//					  user.getPhone(), user.getEmail(), user.getRealname(), user.getRoleid(),
//					  user.getId(), user.getEnabled());
//		
//			userVOs.add(uvo);			
//		}



		System.out.println("populating domain and associated tables");
		
		
		Random random = new Random(System.currentTimeMillis());
			
		DomainVO domainVO = new DomainVO(-1,
			"masterofmydomain" + random.nextInt(1000));

		User user = userHome.create(new UserVO("auser", "", domainVO.getName(),
			"email", "phone", "a user", 2, -1, true)); 
		
		userHome.create(new UserVO("auser2", "", domainVO.getName(),
					"email", "phone", "a user", 2, -1, true));
		
		userHome.create(new UserVO("auser3", "", domainVO.getName(),
					"email", "phone", "a user", 2, -1, true));	
				
		
//		UserVO(String pUserId,String pPassword,String pServiceDomain,String pPhone,
//		String pEmail,String pRealname,int pRole,int pId,boolean pEnabled)))

		
//		for (Iterator i = severityVOs.iterator(); i.hasNext();) {
//			if (!(i.next() instanceof SeverityVO)) {
//				System.out.println("not a severity vo");
//			}
//		}
		
		DomainVO dvo = crud.create(domainVO, userVOs, hostVOs,
			facilityVOs, severityVOs);
	
//		System.out.println("doing an update");	
//		dvo.setName("mydomain" +  random.nextInt(1000));
//		
//		// delete some items from the various lists
//		
//		severityVOs.remove(0);
//		facilityVOs.remove(0);
//		hostVOs.remove(0);
//		//userVOs.remove(0);
//		
//		crud.update(dvo, userVOs, hostVOs,
//			facilityVOs, severityVOs);
		
		System.out.println("deleting domain");	
		crud.delete(dvo);	
//
//		
//		
//
//		HostVO host1VO = crud.create(new HostVO(-1, "myhost1"));
//		
//		System.out.println("created host with id = " + host1VO.getId()
//				+ ", name = " + host1VO.getName());
//				
//		System.out.println("testing update()");
//		
//		host1VO.setName("new myhost1");
//		
//		crud.update(host1VO);
//		
//		System.out.println("testing retrieveByPK()");
//		
//		HostVO ret1VO = crud.retrieveByPrimaryKey(host1VO);
//		
//		System.out.println("retrieved host with id = " + ret1VO.getId()
//						+ ", name = " + ret1VO.getName());
//						
//		System.out.println("testing retrieveAll()");
//		
//		Collection all = crud.retrieveAll();
//		
//		System.out.println("retrieved " + all.size() + " hosts");
//		
//		for (Iterator i = all.iterator(); i.hasNext(); ) {
//			HostVO vo = (HostVO) i.next();
//			System.out.println("element: id = " + vo.getId() +
//					", name = " + vo.getName() );
//			System.out.println("deleting id = " + vo.getId());
//			crud.delete(vo);					
//		}
//		
				
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
