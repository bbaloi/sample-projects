/*
 * Created on 3-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.viewer.tests;

import java.util.Collection;
import java.util.Properties;

import javax.ejb.ObjectNotFoundException;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.perpetual.viewer.model.ejb.Action;
import com.perpetual.viewer.model.ejb.ActionHome;
import com.perpetual.viewer.model.ejb.ActionPK;
import com.perpetual.viewer.model.ejb.Domain;
import com.perpetual.viewer.model.ejb.DomainFacility;
import com.perpetual.viewer.model.ejb.DomainFacilityHome;
import com.perpetual.viewer.model.ejb.DomainFacilityPK;
import com.perpetual.viewer.model.ejb.DomainHome;
import com.perpetual.viewer.model.ejb.DomainHost;
import com.perpetual.viewer.model.ejb.DomainHostHome;
import com.perpetual.viewer.model.ejb.DomainHostPK;
import com.perpetual.viewer.model.ejb.DomainPK;
import com.perpetual.viewer.model.ejb.DomainSeverity;
import com.perpetual.viewer.model.ejb.DomainSeverityHome;
import com.perpetual.viewer.model.ejb.DomainSeverityPK;
import com.perpetual.viewer.model.ejb.DomainUser;
import com.perpetual.viewer.model.ejb.DomainUserHome;
import com.perpetual.viewer.model.ejb.DomainUserPK;
import com.perpetual.viewer.model.ejb.Facility;
import com.perpetual.viewer.model.ejb.FacilityHome;
import com.perpetual.viewer.model.ejb.FacilityPK;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.HostPK;
import com.perpetual.viewer.model.ejb.Role;
import com.perpetual.viewer.model.ejb.RoleAction;
import com.perpetual.viewer.model.ejb.RoleActionHome;
import com.perpetual.viewer.model.ejb.RoleActionPK;
import com.perpetual.viewer.model.ejb.RoleHome;
import com.perpetual.viewer.model.ejb.RolePK;
import com.perpetual.viewer.model.ejb.Severity;
import com.perpetual.viewer.model.ejb.SeverityHome;
import com.perpetual.viewer.model.ejb.SeverityPK;
import com.perpetual.viewer.model.vo.DomainFacilityVO;
import com.perpetual.viewer.model.vo.DomainUserVO;


public class EntityClient
{
  public static void main(String[] args)
  {
    try
    {
      Properties prop = new Properties();
      prop.put(Context.INITIAL_CONTEXT_FACTORY,
               "org.jnp.interfaces.NamingContextFactory");
      prop.put(Context.PROVIDER_URL, "localhost:1099");

      Context ctx = new InitialContext(prop);

//      Object objref = ctx.lookup("perpetual/User");
//
//	  
//      UserHome home =
//          (UserHome)javax.rmi.
//                    PortableRemoteObject.narrow(objref, 
//                            UserHome.class);
//
//		Collection collection = home.findAll();
//		
//		User user = home.findByPrimaryKey(new UserPK(1));
//		
//		System.out.println("found " + user.getRealname());
		
		Object roleRef = ctx.lookup("perpetual/Role");

	  
		RoleHome roleHome =
			(RoleHome)javax.rmi.
					  PortableRemoteObject.narrow(roleRef, 
							  RoleHome.class);

		Collection roleCollection = roleHome.findAll();
		
		System.out.println("found " + roleCollection.size() + " roles.");
		
		RolePK rolePK = new RolePK(1);
		
		  Role role = roleHome.findByPrimaryKey(rolePK);
		
		  System.out.println("found " + role.getRoleName());
		
		
		Object facilityRef = ctx.lookup("perpetual/Facility");

	  
		FacilityHome facilityHome =
			(FacilityHome)javax.rmi.
					  PortableRemoteObject.narrow(facilityRef, 
							  FacilityHome.class);

		Collection facCollection = facilityHome.findAll();
		
		System.out.println("found " + facCollection.size() + " facilities.");
		
		FacilityPK facilityPK = new FacilityPK(0);
		
		  Facility facility = facilityHome.findByPrimaryKey(facilityPK);
		
		  System.out.println("found " + facility.getName());
	
		Object severityRef = ctx.lookup("perpetual/Severity");
   
		SeverityHome severityHome =
			(SeverityHome)javax.rmi.
					  PortableRemoteObject.narrow(severityRef, 
							  SeverityHome.class);
		
		Collection sevCollection = severityHome.findAll();
			
		System.out.println("found " + sevCollection.size() + " severities.");
			
		SeverityPK severityPK = new SeverityPK(0);
			
		Severity severity = severityHome.findByPrimaryKey(severityPK);
			
		System.out.println("found " + severity.getName());

		Object actionRef = ctx.lookup("perpetual/Action");
   
		ActionHome actionHome =
			(ActionHome)javax.rmi.
					  PortableRemoteObject.narrow(actionRef, 
							  ActionHome.class);
		
		Collection actionCollection = actionHome.findAll();
			
		System.out.println("found " + actionCollection.size() + " actions.");
			
		ActionPK actionPK = new ActionPK(1);
			
		Action action = actionHome.findByPrimaryKey(actionPK);
			
		System.out.println("found " + action.getName());
		
		
		Object domainRef = ctx.lookup("perpetual/Domain");
   
		DomainHome domainHome =
			(DomainHome)javax.rmi.
					  PortableRemoteObject.narrow(domainRef, 
							  DomainHome.class);
	
		Collection domainCollection = domainHome.findAll();
		
		System.out.println("found " + domainCollection.size() + " domains.");
		
		DomainPK domainPK = new DomainPK(1);
		
		Domain domain = domainHome.findByPrimaryKey(domainPK);
		
		System.out.println("found " + domain.getName());
		
		Object hostRef = ctx.lookup("perpetual/Host");
   
		HostHome hostHome =
			(HostHome)javax.rmi.
					  PortableRemoteObject.narrow(hostRef, 
							  HostHome.class);
	
		Collection hostCollection = hostHome.findAll();
		
		System.out.println("found " + hostCollection.size() + " hosts.");
		
		HostPK hostPK = new HostPK(1);
		
		try {
			Host host = hostHome.findByPrimaryKey(hostPK);
		
			System.out.println("found " + host.getName());
		} catch  (ObjectNotFoundException re){
			System.out.println("cannot find host, id = " + hostPK.getId());
		}
		
		Object roleActionRef = ctx.lookup("perpetual/RoleAction");
   
		RoleActionHome roleActionHome =
			(RoleActionHome)javax.rmi.
					  PortableRemoteObject.narrow(roleActionRef, 
							  RoleActionHome.class);
	
		Collection roleActionCollection = roleActionHome.findAll();
		
		System.out.println("found " + roleActionCollection.size() + " role-actions.");
		
		RoleActionPK roleActionPK = new RoleActionPK(1);
		
		RoleAction roleAction = roleActionHome.findByPrimaryKey(roleActionPK);
		
		System.out.println("found roleId = " + roleAction.getRoleId()
				+ " actionId = " + roleAction.getActionId());
				
				
		Object domainFacilityRef = ctx.lookup("perpetual/DomainFacility");
   
		DomainFacilityHome domainFacilityHome =
			(DomainFacilityHome)javax.rmi.
					  PortableRemoteObject.narrow(domainFacilityRef, 
							  DomainFacilityHome.class);
	
		Collection domainFacilityCollection = domainFacilityHome.findAll();
		
		System.out.println("found " + domainFacilityCollection.size() + " domain-facilities.");
		
		DomainFacilityPK domainFacilityPK = new DomainFacilityPK(1);
		
//		DomainFacility domainFacility = domainFacilityHome.findByPrimaryKey(domainFacilityPK);
//		
//		System.out.println("found domainId = " + domainFacility.getDomainId()
//				+ " facilityId = " + domainFacility.getFacilityId());
//			
//		DomainFacilityVO domainFacilityVO = new DomainFacilityVO(2, new Integer(2),
//					new Integer(3));
//		
		//domainFacilityHome.create(domainFacilityVO);
					
		domainFacilityPK = new DomainFacilityPK(2);


		Object domainHostRef = ctx.lookup("perpetual/DomainHost");
   
		DomainHostHome domainHostHome =
			(DomainHostHome)javax.rmi.
					  PortableRemoteObject.narrow(domainHostRef, 
							  DomainHostHome.class);
	
		Collection domainHostCollection = domainHostHome.findAll();
		
		System.out.println("found " + domainHostCollection.size() + " domain-facilities.");
		
		DomainHostPK domainHostPK = new DomainHostPK(1);
		
//		DomainHost domainHost = domainHostHome.findByPrimaryKey(domainHostPK);
//		
//		System.out.println("found domainId = " + domainHost.getDomainId()
//				+ " facilityId = " + domainHost.getHostId());
//			
//		DomainHostVO domainHostVO = new DomainHostVO(2, new Integer(2),
//					new Integer(3));
//		
//		domainHostHome.create(domainHostVO);
//					
//		domainHostPK = new DomainHostPK(2);


		Object domainSeverityRef = ctx.lookup("perpetual/DomainSeverity");
   
		DomainSeverityHome domainSeverityHome =
			(DomainSeverityHome)javax.rmi.
					  PortableRemoteObject.narrow(domainSeverityRef, 
							  DomainSeverityHome.class);
	
		Collection domainSeverityCollection = domainSeverityHome.findAll();
		
		System.out.println("found " + domainSeverityCollection.size() + " domain-facilities.");
		
//		DomainSeverityPK domainSeverityPK = new DomainSeverityPK(1);
//		
//		DomainSeverity domainSeverity = domainSeverityHome.findByPrimaryKey(domainSeverityPK);
//		
//		System.out.println("found domainId = " + domainSeverity.getDomainId()
//				+ " severityId = " + domainSeverity.getSeverityId());
				
		Object domainUserRef = ctx.lookup("perpetual/DomainUser");
   
		DomainUserHome domainUserHome =
			(DomainUserHome)javax.rmi.
					  PortableRemoteObject.narrow(domainUserRef, 
							  DomainUserHome.class);
	
		Collection domainUserCollection = domainUserHome.findAll();
		
		System.out.println("found " + domainUserCollection.size() + " domain-users.");
		
		domainUserHome.create(new DomainUserVO(-1, new Integer(2), new Integer(3)));
		domainUserHome.create(new DomainUserVO(-1, new Integer(2), new Integer(5)));
		
		DomainUserPK domainUserPK = new DomainUserPK(1);
		
		DomainUser domainUser = domainUserHome.findByPrimaryKey(domainUserPK);
		
		System.out.println("found domainId = " + domainUser.getDomainId()
				+ " severityId = " + domainUser.getUserId());										
	
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
