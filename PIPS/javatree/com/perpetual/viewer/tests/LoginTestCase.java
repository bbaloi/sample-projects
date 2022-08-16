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

import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.SessionRoleVO;
import com.perpetual.viewer.model.vo.SessionDomainVO;
import com.perpetual.viewer.model.vo.RoleVO;
import com.perpetual.viewer.model.vo.ActionVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;

import com.perpetual.viewer.control.ejb.Login;
import com.perpetual.viewer.control.ejb.LoginHome;
import com.perpetual.util.Constants;


/**
 * Some simple tests.
 *
 */
public class LoginTestCase extends TestCase 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( LoginTestCase.class );
     
    
	protected InitialContext lIC=null;
        protected String lContextFactory="org.jnp.interfaces.NamingContextFactory";
        //protected String lContextFactory = "org.jbos.naming.HttpNamingContextFactory";
        protected String lURL="jnp://localhost:1099";
        //protected String lURL="http://localhost:8080/invoker/JNDIFactory";
        protected String lPrefixes = "org.jboss.naming:org.jnp.interfaces";
        protected Properties lProps = null;
        protected String lJndiName = "perpetual/Login";
        protected LoginHome lHome=null;
	protected Login login=null;
        protected PerpetualResourceLoader rl=null;
        protected String jndiFile = "/home/perpetual_cvs/projects/viewer/runtime/jndi.properties"; 
               
        public static void main (String[] args) 
        {
                UserTest pt = new UserTest();
                //pt.loadJNDIProperties();
                junit.textui.TestRunner.run (suite());
	}
                
        public LoginTestCase()
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
                System.out.println("Setting up Login Test !");
            	
                try
                {
                    lProps.list(System.out);
                    lIC = new InitialContext(lProps);
                    //sLog.debug("Created Initial Context");
                    System.out.println("Created Initial Context-looking for LoginHome");
                    Object homeObj = lIC.lookup(Constants.jndiName_Login);
                    System.out.println("got Object!");
                    lHome = (LoginHome) PortableRemoteObject.narrow(homeObj,LoginHome.class); 
           
                    //lSessionHome = (PJVTProducerSessionHome) lIC.lookup(lJndiName);
                                      
                    System.out.println("Login Home:"+lHome.toString());
                                                            
                }
                catch(Throwable t)
                {
                     t.printStackTrace();
                }
	}
	public static Test suite()
        {
           
            return new TestSuite(LoginTestCase.class);
	}
        public void testLogin()
        {
            try
            {
                Login login = lHome.create();
                UserProfileVO profileVo = login.login("root","root");
                System.out.println("-----Got user profile---");
                System.out.println("User Id:"+profileVo.getUserId());
                System.out.println("UserName:"+profileVo.getUserName());
                System.out.println("Password:"+profileVo.getPassword());
                System.out.println("Phone:"+profileVo.getPhone());
                System.out.println("Email:"+profileVo.getEmail());
                System.out.println("Real Name:"+profileVo.getRealName());
                System.out.println("RoleId:"+profileVo.getRoleId());
                System.out.println("RoleName:"+profileVo.getRoleName());
                System.out.println("Domain ID:"+profileVo.getDomainId());
                System.out.println("Domain Name:"+profileVo.getDomainName());
                
                Collection actionList = profileVo.getActionList();
                Collection hostList = profileVo.getHostList();
                Collection facilityList = profileVo.getFacilityList();
                Collection severityList = profileVo.getSeverityList();
                System.out.println("++++ActionList+++++");
                Iterator it = actionList.iterator();
                while(it.hasNext())
                {
                    ActionVO action = (ActionVO)it.next();
                    System.out.println("Action Id:"+action.getId()+" ActionName:"+action.getName());
                }
                 System.out.println("++++HostList+++++");
                it = hostList.iterator();
                while(it.hasNext())
                {
                    HostVO host = (HostVO)it.next();
                    System.out.println("Host Id:"+host.getId()+" HostName:"+host.getName());
                }
                 System.out.println("++++FacilityList+++++");
                it = facilityList.iterator();
                while(it.hasNext())
                {
                    FacilityVO facility = (FacilityVO)it.next();
                    System.out.println("FacilityId:"+facility.getId()+" FacilityName:"+facility.getName());
                }
                 System.out.println("++++SeverityList+++++");
                it = severityList.iterator();
                while(it.hasNext())
                {
                    SeverityVO severity = (SeverityVO)it.next();
                    System.out.println("SeverityId:"+severity.getId()+" SeverityName:"+severity.getName());
                }
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
       	       
}