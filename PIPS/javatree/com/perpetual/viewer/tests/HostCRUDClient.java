/*
 * Created on 3-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.viewer.tests;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.perpetual.viewer.control.ejb.crud.host.HostCRUD;
import com.perpetual.viewer.control.ejb.crud.host.HostCRUDHome;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.vo.HostVO;


public class HostCRUDClient
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

		Object keyRef = ctx.lookup("perpetual/HostCRUD");
		
		HostCRUDHome crudHome =
			(HostCRUDHome)javax.rmi.
					  PortableRemoteObject.narrow(keyRef, 
							  HostCRUDHome.class);

		HostCRUD crud = crudHome.create();

		System.out.println("testing create()");

		HostVO host1VO = crud.create(new HostVO(-1, "myhost1", "myhost1's description"));
		
		System.out.println("created host with id = " + host1VO.getId()
				+ ", name = " + host1VO.getName());
				
		System.out.println("testing update()");
		
		host1VO.setName("new myhost1");
		
		crud.update(host1VO);
		
		System.out.println("testing retrieveByPK()");
		
		HostVO ret1VO = crud.retrieveByPrimaryKey(host1VO);
		
		System.out.println("retrieved host with id = " + ret1VO.getId()
						+ ", name = " + ret1VO.getName());
						
		System.out.println("testing retrieveAll()");
		
		Collection all = crud.retrieveAll();
		
		System.out.println("retrieved " + all.size() + " hosts");
		
		for (Iterator i = all.iterator(); i.hasNext(); ) {
			HostVO vo = (HostVO) i.next();
			System.out.println("element: id = " + vo.getId() +
					", name = " + vo.getName() );
			System.out.println("deleting id = " + vo.getId());
			crud.delete(vo);					
		}
		
				
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
