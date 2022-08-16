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

import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.UserVO;


public class UserCRUDClient
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

		Object keyRef = ctx.lookup("perpetual/UserCRUD");
		
		UserCRUDHome crudHome =
			(UserCRUDHome)javax.rmi.
					  PortableRemoteObject.narrow(keyRef, 
							  UserCRUDHome.class);

		UserCRUD crud = crudHome.create();

		System.out.println("testing create()");

		UserVO user1VO = crud.create(new UserVO(
			"xxx",			"password",
			"servicedomain",
			"777-7777",
			"xxx@pip.com",
			"Mr XXX",
			0,
			-1,
			true));
		
		System.out.println("created host with id = " + user1VO.getId()
				+ ", name = " + user1VO.getUserId());
				
		System.out.println("testing update()");
		
		user1VO.setRealname("Mr YYY");
		
		crud.update(user1VO);
		
		System.out.println("testing retrieveByPK()");
		
		UserVO ret1VO = crud.retrieveByPrimaryKey(user1VO);
		
		System.out.println("retrieved host with id = " + ret1VO.getId()
						+ ", name = " + ret1VO.getRealname());
						
		System.out.println("testing retrieveAll()");
		
		Collection all = crud.retrieveAll();
		
		System.out.println("retrieved " + all.size() + " users");
		
		for (Iterator i = all.iterator(); i.hasNext(); ) {
			UserVO vo = (UserVO) i.next();
			System.out.println("element: id = " + vo.getId() +
					", name = " + vo.getUserId() );
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
