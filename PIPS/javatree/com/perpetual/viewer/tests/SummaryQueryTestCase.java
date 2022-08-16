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
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;

import com.perpetual.util.ServiceLocator;
import com.perpetual.util.Constants;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.query.SummaryFilter;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;
import com.perpetual.viewer.model.ejb.Summary;
import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.viewer.control.SummaryLogicDescriptor;
import com.perpetual.viewer.control.query.SummaryRecord;

import java.text.SimpleDateFormat;


/**
 * Some simple tests.
 *
 */
public class SummaryQueryTestCase extends TestCase 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryQueryTestCase.class );
   
        protected SummaryCRUDHome summaryHome=null;
	protected SummaryCRUD queryEngine=null;
               
        public static void main (String[] args) 
        {
                SummaryQueryTestCase pt = new SummaryQueryTestCase();
               junit.textui.TestRunner.run (suite());
	}
                
        public SummaryQueryTestCase()
        {
        }
	protected void setUp() 
        {
              System.out.println("Setting up SummaryQuery Test !");
            try
            {
                 Object homeObj = ServiceLocator.getServiceLocatorInstance().findHome(Constants.jndiName_SummaryCRUD);
               sLog.debug("got Object!");
               summaryHome = (SummaryCRUDHome) PortableRemoteObject.narrow(homeObj,SummaryCRUDHome.class); 
               System.out.println("SUmmary Home:"+summaryHome.toString());
               sLog.debug("SummaryCRUDHome:"+summaryHome.toString());                    
               queryEngine = (SummaryCRUD)summaryHome.create();
                System.out.println("Summary QUery Engine:"+queryEngine.toString());
            }           
            catch(Exception t)
            {
                sLog.error("Couldn't get properties !");
                t.printStackTrace();
            }
              
	}
	public static Test suite()
        {
           /* TestSuite suite = new TestSuite();
                suite.addTest(new UserTest("testGetAllUsers"));
            
            return suite;*/
            return new TestSuite(SummaryQueryTestCase.class);
	}
        public void testGetSummary()
        {
            SummaryFilter  filter = prepareFilter();
            try
            {
               
                //Collection summaryList = (Collection) queryEngine.getSummaryRecords(filter);
                HashMap paramMap = new HashMap();
                String query = queryEngine.constructQuery(filter.getMap(),paramMap); 
                System.out.println("Constructed Query !, executing...");
                Collection summaryList = null; // queryEngine.executeQuery(query,paramMap);                
                System.out.println("# of records found:"+summaryList.size());
            
                Iterator it = summaryList.iterator();
                int i=0;
                while(it.hasNext())
                {   
                    i++;
                    SummaryRecord summaryRecord = (SummaryRecord)it.next();
                    Map summaryMap=summaryRecord.getMap();
                    //Display contents of all the records retrieved....
                   System.out.println("+++++SummaryRecord"+i+"++++++");
                    System.out.println("id:"+((Integer)summaryMap.get(ViewerGlobals.Id)).toString());
                    System.out.println("hostname:"+(String)summaryMap.get(ViewerGlobals.HostName));
                    System.out.println("domainname:"+(String)summaryMap.get(ViewerGlobals.DomainName));
                    System.out.println("vendor:"+(String)summaryMap.get(ViewerGlobals.VendorName));
                    System.out.println("DeviceType:"+(String)summaryMap.get(ViewerGlobals.DeviceType));
                    System.out.println("StartDate:"+(Timestamp)summaryMap.get(ViewerGlobals.StartDate));
                    System.out.println("EndDate:"+(Timestamp)summaryMap.get(ViewerGlobals.EndDate));
                     
                    System.out.println("fac0:"+((Integer)summaryMap.get(ViewerGlobals.fac0)).toString());
                    System.out.println("fac4:"+((Integer)summaryMap.get(ViewerGlobals.fac0)).toString());
                    System.out.println("fac9:"+((Integer)summaryMap.get(ViewerGlobals.fac0)).toString());
                    System.out.println("fac12:"+((Integer)summaryMap.get(ViewerGlobals.fac0)).toString());
                    
                     System.out.println("sev0:"+((Integer)summaryMap.get(ViewerGlobals.sev0)).toString());
                     System.out.println("sev1:"+((Integer)summaryMap.get(ViewerGlobals.sev1)).toString());
                     //sLog.debug("sev4:"+((Integer)summaryMap.get(ViewerGlobals.sev4)).toString());
                     System.out.println("sev6:"+((Integer)summaryMap.get(ViewerGlobals.sev6)).toString());
                   
                    
                }
                 
                
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        private SummaryFilter prepareFilter()
        {
            SummaryFilter filter=null;
            HashMap map = new HashMap();
            
            SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
            
            try
            {
                map.put(Constants.StartDate,(m_dateFormat.parse("20030626 100000")));
                map.put(Constants.EndDate,(m_dateFormat.parse("20030827 230000")));
       
            }
            catch(java.text.ParseException excp)
            {
                excp.printStackTrace();
            }
            
            String [] hosts = new String []{"10.1.1.250","127.0.0.1"};
            map.put(Constants.HostList,hosts);
            //map.put(ViewerGlobals.DomainName,"rootdomain");
            //map.put(ViewerGlobals.VendorName,null);
            //map.put(ViewerGlobals.DeviceName,null);
             String [] facilities = new String []{ViewerGlobals.fac0,ViewerGlobals.fac1,ViewerGlobals.fac4,
                                        ViewerGlobals.fac7,ViewerGlobals.fac9,ViewerGlobals.fac10,
                                        ViewerGlobals.fac11,ViewerGlobals.fac12};
             map.put(Constants.FacilityList,facilities);
            
            String [] severities = new String []{ViewerGlobals.sev0,ViewerGlobals.sev1,ViewerGlobals.sev3,
                                        ViewerGlobals.sev5,ViewerGlobals.sev6};
             map.put(Constants.SeverityList,severities);
            
            SummaryLogicDescriptor descriptor = new SummaryLogicDescriptor(false,false,false);
            map.put(Constants.SummaryLogicDescriptor,descriptor);
             
            filter = new SummaryFilter(map);            
            
            return filter;
        }
}