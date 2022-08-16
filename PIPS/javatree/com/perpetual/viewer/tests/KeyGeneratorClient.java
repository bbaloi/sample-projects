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

import com.perpetual.util.ejb.Key;
import com.perpetual.util.ejb.KeyHome;
import com.perpetual.util.ejb.KeyPK;
import com.perpetual.viewer.model.ejb.Host;
import com.perpetual.viewer.model.ejb.HostHome;
import com.perpetual.viewer.model.ejb.HostPK;
import com.perpetual.viewer.model.vo.HostVO;


public class KeyGeneratorClient
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

		Object keyRef = ctx.lookup("perpetual/Key");
		
		KeyHome keyHome =
			(KeyHome)javax.rmi.
					  PortableRemoteObject.narrow(keyRef, 
							  KeyHome.class);

		//keyHome.create("anonymous", 11);


//		KeyPK keyPK = new KeyPK("anonymous");
//		
//		Key key = keyHome.findByPrimaryKey(keyPK);
//		
//		System.out.println("number " + key.getNumber());

		
		Object hostRef = ctx.lookup("perpetual/Host");
   
		HostHome hostHome =
			(HostHome)javax.rmi.
					  PortableRemoteObject.narrow(hostRef, 
							  HostHome.class);


		for (int i = 0; i < 10; i++) {
			HostVO hostVO = new HostVO(-1, " my host " + i, "my host " + i + "'s description");
		
			Host host = hostHome.create(hostVO);
			
			System.out.println("created host, id = " + host.getId());
		}
			
		Collection hostCollection = hostHome.findAll();
		
		System.out.println("found " + hostCollection.size() + " hosts.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
