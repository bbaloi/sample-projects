package com.perpetual.viewer.tests;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;

import java.util.Properties;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.PerpetualResourceLoader;
import com.perpetual.viewer.model.ejb.User;
import com.perpetual.viewer.model.ejb.UserPK;
import com.perpetual.viewer.model.ejb.UserHome;
import com.perpetual.viewer.model.vo.UserVO;
import java.util.Collection;
import java.util.Iterator;

/**
 * Some simple tests.
 *
 */
public class UserTest extends TestCase 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( UserTest.class );
     
    
	protected InitialContext lIC=null;
        protected String lContextFactory="org.jnp.interfaces.NamingContextFactory";
        //protected String lContextFactory = "org.jbos.naming.HttpNamingContextFactory";
        protected String lURL="jnp://localhost:1099";
        //protected String lURL="http://localhost:8080/invoker/JNDIFactory";
        protected String lPrefixes = "org.jboss.naming:org.jnp.interfaces";
        protected Properties lProps = null;
        protected String lJndiName = "perpetual/User";
        protected UserHome lHome=null;
	protected User lUser=null;
        protected PerpetualResourceLoader rl=null;
        protected String jndiFile = "/home/perpetual_cvs/projects/viewer/runtime/jndi.properties"; 
               
        public static void main (String[] args) 
        {
                UserTest pt = new UserTest();
                //pt.loadJNDIProperties();
                junit.textui.TestRunner.run (suite());
	}
                
        public UserTest()
        {
            
            rl = PerpetualResourceLoader.getDefault();
            try
            {
             lProps=rl.getProperties(jndiFile); 
            }
            catch(Throwable t)
            {
                sLog.error("Couldn't get properties !");
            }
            
        }
	protected void setUp() 
        {
            //load up values
                //sLog.debug("Setting up User Test !");
                System.out.println("Setting up User Test !");
            	//lProps = new Properties();	
                //lProps.put(Context.INITIAL_CONTEXT_FACTORY,lContextFactory);
                //lProps.put(Context.PROVIDER_URL,lURL);
                //lProps.put(Context.URL_PKG_PREFIXES,lPrefixes);
                try
                {
                    lProps.list(System.out);
                    lIC = new InitialContext(lProps);
                    //sLog.debug("Created Initial Context");
                    System.out.println("Created Initial Context-looking for UserHome");
                    Object homeObj = lIC.lookup(lJndiName);
                    System.out.println("got Object!");
                    lHome = (UserHome) PortableRemoteObject.narrow(homeObj,UserHome.class); 
           
                    //lSessionHome = (PJVTProducerSessionHome) lIC.lookup(lJndiName);
                                      
                    System.out.println("User Home:"+lHome.toString());
                                                            
                }
                catch(Throwable t)
                {
                     t.printStackTrace();
                }
	}
	public static Test suite()
        {
           /* TestSuite suite = new TestSuite();
                suite.addTest(new UserTest("testGetAllUsers"));
            
            return suite;*/
            return new TestSuite(UserTest.class);
	}
        public void testGetAllUsers()
        {
            try
            {
                Collection userList = (Collection) lHome.findAll();
                System.out.println("list size:"+userList.size());
            
                Iterator it = userList.iterator();
                while(it.hasNext())
                {
                    User dUser = (User)it.next();
                    System.out.println("User Name"+dUser.getUserid());
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void testCreateUSer()
        {
            System.out.println("Testing creation of USer !");
            
            try 
            {
                UserVO user =  new UserVO("bula","bula","BulaDomain","666-666",
                                            "bula@bula","bula is my real name",0,6,true);
                 lUser = (User) lHome.create(user);
                 System.out.println("Created User!-showing user values");
                 System.out.println("User Name:"+lUser.getUserid());
                 System.out.println("Password:"+lUser.getUserpass());
                 System.out.println("Real Name:"+lUser.getRealname());
                 System.out.println("Domain:"+lUser.getServicedomain());                 
                
             }
            catch(Throwable ex)
            {
                ex.printStackTrace();
            }
        }
	       
}