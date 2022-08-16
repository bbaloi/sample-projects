package com.perpetual.recordprocessor.control.tests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.perpetual.rp.control.RPConfigManager;
import com.perpetual.rp.control.query.SummaryQueryEngine;
import com.perpetual.rp.control.query.SummaryQueryTemplate;
import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.PerpetualResourceLoader;
import com.perpetual.util.resource.ResourceLoader;
import com.perpetual.viewer.control.ejb.Login;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;

/**.
 * Some simple tests.
 *
 */
public class SummaryCollectionTestCase extends TestCase 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryCollectionTestCase.class );
     
    
	protected InitialContext lIC=null;
        protected String lContextFactory="org.jnp.interfaces.NamingContextFactory";
        //protected String lContextFactory = "org.jbos.naming.HttpNamingContextFactory";
        protected String lURL="jnp://localhost:1099";
        //protected String lURL="http://localhost:8080/invoker/JNDIFactory";
        protected String lPrefixes = "org.jboss.naming:org.jnp.interfaces";
        protected Properties jndiProps = null;
        protected Properties appProps=null;
        protected String lJndiName = "perpetual/SummaryCollection";
        protected SummaryCRUDHome lHome=null;
	protected Login login=null;
        protected PerpetualResourceLoader rl=null;
        protected String jndiFile = "jndi.properties"; 
        protected String propertyFile = "test.properties";
        
        protected RPConfigManager lConfigManager=null;
        protected INavigator lNavigator=null;
        protected List domainList=null;
        
        
        public static void main (String[] args) 
        {
                SummaryCollectionTestCase pt = new SummaryCollectionTestCase();
                //pt.loadJNDIProperties();
                junit.textui.TestRunner.run (suite());
	}
                
        public SummaryCollectionTestCase()
        {            
            rl = PerpetualResourceLoader.getDefault();
            try
            {
            	jndiProps = ResourceLoader.loadProperties("properties/config/" + 
							jndiFile);
				appProps = 	ResourceLoader.loadProperties("properties/config/" + 
						propertyFile);		
             //jndiProps=rl.getProperties(jndiFile); 
             //appProps=rl.getProperties(propertyFile);
            }
            catch(Throwable t)
            {
                sLog.error("Couldn't get properties !");
            }
            
        }
	protected void setUp() 
        {
                System.out.println("Setting up SummaryCollection Test !");
            	SummaryQueryTemplate queryTemplate=null;
                Date pStartDate = null;
                Date pEndDate=null;
                
                try
                {
                    System.out.println("----------------------");
                    jndiProps.list(System.out);
                    System.out.println("----------------------");
                    appProps.list(System.out);
                    
                    String startDate = appProps.getProperty("rp.start.date");
                    String endDate = appProps.getProperty("rp.end.date");
                    pStartDate = getDate(startDate);
                    System.out.println("StartDate:"+pStartDate);
                    pEndDate = getDate(endDate);
                    System.out.println("EndDate:"+pEndDate);
                    
                    queryTemplate = new SummaryQueryTemplate(pStartDate,pEndDate,null,null,null,null);
                    SummaryQueryEngine queryEngine = SummaryQueryEngine.getInstance();
                                     
                    domainList = queryEngine.getSummaryRecords(queryTemplate,true);
                    
                    Iterator it = domainList.iterator();
                    int cntr=0;
                    while(it.hasNext())
                    {
                        cntr++;
                        List voList = (List) it.next();
                        if(voList !=null)
                        {
                          Iterator vit = voList.iterator();
                          while(vit.hasNext())
                          {
                            HostSummaryVO vo = (HostSummaryVO) vit.next();
                        
                            System.out.println("-----host"+cntr+":"+vo.getHostName()+"--------");
                            System.out.println("Domain:"+vo.getDomainName());
 
                            System.out.println("StartTime:"+vo.getStartTimeStamp());
                            System.out.println("EndTime:"+vo.getEndTimeStamp());
                            System.out.println("----------Facilities----------");
                            System.out.println("Fac0:"+vo.fac0);
                            System.out.println("Fac1:"+vo.fac1);
                            System.out.println("Fac2:"+vo.fac2);
                            System.out.println("Fac3:"+vo.fac3);
                            System.out.println("Fac4:"+vo.fac4);
                            System.out.println("Fac5:"+vo.fac5);
                            System.out.println("Fac6:"+vo.fac6);
                            System.out.println("Fac7:"+vo.fac7);
                            System.out.println("Fac8:"+vo.fac8);
                            System.out.println("Fac9:"+vo.fac9);
                            System.out.println("Fac10:"+vo.fac10);
                            System.out.println("Fac11:"+vo.fac11);
                            System.out.println("Fac12:"+vo.fac12);
                            System.out.println("Fac13:"+vo.fac13);
                            System.out.println("Fac14:"+vo.fac14);
                            System.out.println("Fac15:"+vo.fac15);
                            System.out.println("Fac16:"+vo.fac16);
                            System.out.println("Fac17:"+vo.fac18);
                            System.out.println("Fac19:"+vo.fac19);
                            System.out.println("Fac20:"+vo.fac20);
                            System.out.println("Fac21:"+vo.fac21);
                            System.out.println("Fac22:"+vo.fac22);
                            System.out.println("Fac23:"+vo.fac23);
                            System.out.println("---------Severities-----------");
                            System.out.println("Sev0:"+vo.sev0);
                            System.out.println("Sev1:"+vo.sev1);
                            System.out.println("Sev2:"+vo.sev2);
                            System.out.println("Sev3:"+vo.sev3);
                            System.out.println("Sev4:"+vo.sev4);
                            System.out.println("Sev5:"+vo.sev5);
                            System.out.println("Sev6:"+vo.sev6);
                            System.out.println("Sev7:"+vo.sev7);
                        }                        
                      }
                    }
                                                                                
                }
                catch(Throwable t)
                {
                     t.printStackTrace();
                }
	}
	public static Test suite()
        {
           
            return new TestSuite(SummaryCollectionTestCase.class);
	}
        public void testAddSummaryData()
        {
            System.out.println("---testCollectSummaryData----");
            try
            {
                lIC = new InitialContext(jndiProps);
                    System.out.println("Created Initial Context-looking for LoginHome");
                    Object homeObj = lIC.lookup(Constants.jndiName_SummaryCRUD);
                    System.out.println("got Object!");
                    lHome = (SummaryCRUDHome) PortableRemoteObject.narrow(homeObj,SummaryCRUDHome.class); 
                    System.out.println("SummaryCRUDHome:"+lHome.toString());
                    
                    SummaryCRUD summary = lHome.create();
                    
                    Iterator it = domainList.iterator();
                    while(it.hasNext())
                    {
                        List voList = (List) it.next();
                        if(voList !=null)
                        {
                          Iterator vit = voList.iterator();
                          while(vit.hasNext())
                          {
                            HostSummaryVO vo = (HostSummaryVO) vit.next();
                            summary.create(vo);
                          }
                        }
                    }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
       	       
        private Date getDate(String pDate)
        {
            Date date = null;
            ArrayList dateArray = new ArrayList();
            SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");            
            String _date = pDate.trim();      
            
            StringTokenizer tokenizer = new StringTokenizer(_date,":");
            
            while(tokenizer.hasMoreElements())
            {
                dateArray.add(tokenizer.nextElement());
            }
            
            String formattedDate = (String) dateArray.get(0)+(String) dateArray.get(1)+
                                    (String) dateArray.get(2)+" "+(String) dateArray.get(3)+
                                    (String) dateArray.get(4)+(String) dateArray.get(5);
            try
            {
                date = m_dateFormat.parse(formattedDate);
            }
            catch( java.text.ParseException excp)
            {
                excp.printStackTrace();
            }
            /*
            //System.out.println("Date size:"+dateArray.size());

            int year = (new Integer((String) dateArray.get(0))).intValue();
            int month = (new Integer((String) dateArray.get(1))).intValue();
            int day = (new Integer((String) dateArray.get(2))).intValue();
            int hour = (new Integer((String) dateArray.get(3))).intValue();
            int min = (new Integer((String) dateArray.get(4))).intValue();
            int sec = (new Integer((String) dateArray.get(5))).intValue();
              
            TimeZone timeZone = TimeZone.getDefault();
            Calendar cal = Calendar.getInstance(timeZone);
            cal.set(Calendar.SECOND, sec);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.HOUR_OF_DAY,hour);
            cal.set(Calendar.MINUTE, min);
            date = cal.getTime();
          */
           
            
            return date;
        }
}