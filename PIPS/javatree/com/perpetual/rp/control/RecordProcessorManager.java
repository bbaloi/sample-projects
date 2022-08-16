
/*
 * RecordProcessorManager.java
 *
 * Created on July 17, 2003, 10:23 PM
 */

package com.perpetual.rp.control;

import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.util.threadpool.IThreadManager;
import com.perpetual.util.PerpetualC2Logger;

import com.perpetual.rp.util.jaxb.Facility;
import com.perpetual.rp.util.jaxb.Severity;
import com.perpetual.rp.util.jaxb.Host;
import com.perpetual.rp.util.jaxb.ServiceDomain;
import com.perpetual.rp.util.jaxb.CollectionCriteria;
import com.perpetual.rp.util.jaxb.VendorCriteria;
import com.perpetual.rp.util.jaxb.VendorCriterion;
import com.perpetual.rp.util.jaxb.ServiceDomainList;
import com.perpetual.rp.util.jaxb.IntervalValue;
import com.perpetual.rp.util.jaxb.Selected;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import com.perpetual.rp.control.vendor.VendorCounter;
import com.perpetual.rp.control.vendor.CiscoRouterCounter;
import com.perpetual.rp.control.vendor.CiscoFirewallCounter;
import com.perpetual.exception.BasePerpetualException;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.text.SimpleDateFormat;

/**
 *
 * @author  brunob
 */
public final class RecordProcessorManager implements ICollectionTime
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( RecordProcessorManager.class );    
    
    INavigator lNavigator=null;
    RPConfigManager lConfigMgr=null;
    IThreadManager lThreadPoolMgr=null;
    TimeZone lTimeZone = null;
    
    
    /** Creates a new instance of RecordProcessorManager */
    public RecordProcessorManager(IThreadManager pThreadPoolMgr,RPConfigManager pConfigMgr,
                        INavigator pNavigator)
    {
        lNavigator=pNavigator;
        lConfigMgr = pConfigMgr;
        lThreadPoolMgr = pThreadPoolMgr;
        init();
    }
    private void init()
    {
        sLog.debug("RecordProcessorManager - initialising Threads With Domains !");
        //for every DOmain in the COnfig Manager :
        lTimeZone = getLocalTimeZone();
        //1) strip out a Domain entity - construct a DomainMap
        if(lThreadPoolMgr!=null)
        {
            List domainList = lConfigMgr.getDomainList();
            Iterator it = domainList.iterator();
            while(it.hasNext())
            {
                ServiceDomain domainDesc = (ServiceDomain)it.next();
                DomainCriteriaMap criteriaMap = getCriteriaMap(domainDesc);
                  Map domainCounterMap = getDomainCounter(criteriaMap); 
                //2) Censtruct a new SyslogRecorProcessor load passing it its map
                RecordProcessor rp = new SyslogRecordProcessor(criteriaMap,lNavigator,domainCounterMap,this);
                //3) addNewThread to Thread pool Manager.....
                try
                {
                     lThreadPoolMgr.addNewThread(criteriaMap.getDomainName(),rp);
                }
                catch(BasePerpetualException excp)
                {
                    sLog.error("Could not Add A new Record Processor Thread !");
                 excp.printStackTrace();
                }
            }
        }
        sLog.debug("Finished Initialising the Record Porcessor Manger !");   
    }
    public List collectSummaryRecordVOs(Date pStartDate,Date pEndDate)
    {
        sLog.debug("ReccordProcessorMaanger.collectSUmmaryRecordVOs ()!");
        ArrayList recordList=new ArrayList();
        
        List domainList = lConfigMgr.getDomainList();
        Iterator it = domainList.iterator();
        while(it.hasNext())
         {
             ServiceDomain domainDesc = (ServiceDomain)it.next();
             DomainCriteriaMap criteriaMap = getCriteriaMap(domainDesc);
             Map domainCounterMap = getDomainCounter(criteriaMap); 
                //2) Censtruct a new SyslogRecorProcessor load passing it its map
             RecordProcessor rp = new SyslogRecordProcessor(criteriaMap,lNavigator,
                                                    domainCounterMap,pStartDate,pEndDate);
                List hostSummaryVoList = rp.getSummaryRecords();
                recordList.add(hostSummaryVoList);                
                
          }
       
        return recordList;
    }
    
    private Map getDomainCounter(DomainCriteriaMap pCriteria)
    {
        sLog.debug("Constructing the Domain Counter !");
        HashMap domainCounterMap = new HashMap();
        HostCounter hostCounter=null;
        VendorCounter vendorCounter=null;
        
        List hostList = pCriteria.getHostList();
        Iterator it = hostList.iterator();
        while(it.hasNext())
        {
            VendorDevice device = (VendorDevice) it.next();
            String hostName = device.getHostName();
            String vendorName = device.getVendorName();
            String deviceType = device.getDeviceType();
            List  criterionList = device.getVendorCriteria();
            if(vendorName==null && deviceType==null && criterionList==null)
            {
                sLog.debug("This Domain Counter has no vendor criteria");
                hostCounter = new HostCounter(null,hostName,vendorName,deviceType);
            }
            else
            {
                sLog.debug("This Domain Counter has to count criteria for:"+vendorName+" "+deviceType);
                vendorCounter=getVendorCounter(vendorName,deviceType,criterionList);
                hostCounter = new HostCounter(vendorCounter,hostName,vendorName,deviceType);
            }
              domainCounterMap.put(hostName,hostCounter);
        }
        
        return domainCounterMap;
    }
    private VendorCounter getVendorCounter(String pVendorName,String pDeviceType,List pCriterionList)
    {
        sLog.debug("Getting Vendor Counter !");
        VendorCounter counter=null;
        //determine the type of counter 
        if(pVendorName.equals("Cisco") && pDeviceType.equals("Firewall"))
        {
            counter = new CiscoFirewallCounter(pCriterionList);            
        }
        if(pVendorName.equals("Cisco") && pDeviceType.equals("Router"))
        {
            counter = new CiscoRouterCounter(pCriterionList);            
        }
        //++++++++many many more+++++++=
        return counter;
        
    }
    private DomainCriteriaMap getCriteriaMap(ServiceDomain pDomain)
    {
        sLog.debug("Building Criteria Map !");
        DomainCriteriaMap criteriaMap = null;
        ArrayList facilityMap =new ArrayList();
        ArrayList severityMap =new ArrayList();
        ArrayList hostMap     =new ArrayList();
        String selected=null;
         VendorDevice deviceMap=null;
        
        IntervalValue intervalValue = (pDomain.getCollectionIntervalValue()).getIntervalValue();
        
        int interval = (new Integer(intervalValue.getContent())).intValue();
        sLog.debug("Collection Interval value:"+interval);
        String timeUnit = (pDomain.getCollectionIntervalValue()).getTimeunit();
        String domainName = pDomain.getName();
        CollectionCriteria collectionCriteria = pDomain.getCollectionCriteria();
        List facilityList  = collectionCriteria.getFacilityList().getFacility();
        List severityList  = collectionCriteria.getSeverityList().getSeverity();
        List hostList      = collectionCriteria.getHostList().getHost();
        
        //prepare Facility Map
        Iterator fit = facilityList.iterator();
        while(fit.hasNext())
        {
            Facility facility = (Facility) fit.next();  
            selected = facility.getSelected().getContent(); 
            //if(facility.getSelected())
            if(selected.equals("true"))
            {
                Integer fac = new Integer(facility.getName());
                facilityMap.add(fac);   
            }
        }
        //prepare Severity Map
        Iterator sit = severityList.iterator();
         while(sit.hasNext())
        {
            Severity severity = (Severity) sit.next();
            selected = severity.getSelected().getContent(); 
            //if(severity.getSelected())
            if(selected.equals("true"))
            {               
                Integer sev = new Integer(severity.getName());
                severityMap.add(sev);  
            }
        }
        //getting Host List
        Iterator hit = hostList.iterator();
         while(hit.hasNext())
        {
            Host hostCriteria = (Host) hit.next();      
            String hostName = hostCriteria.getHostName().getContent();
            VendorCriteria vendorCriteria = (VendorCriteria) hostCriteria.getVendorCriteria();
            List criterionList = vendorCriteria.getVendorCriterion();
            if(criterionList.size()!=0)
            {
                sLog.debug("We have vendor criteria !");
                String vendorName = vendorCriteria.getName();
                String deviceType = vendorCriteria.getType();                
                Iterator cit = criterionList.iterator();
                ArrayList criterionMap = new ArrayList();
                while(cit.hasNext())
                {
                    VendorCriterion criterion = (VendorCriterion) cit.next();
                    selected = criterion.getSelected().getContent(); 
                    if(criterion.equals("true"))
                        criterionMap.add(criterion.getName());
                }
                deviceMap=new VendorDevice(hostName,vendorName,deviceType,criterionMap);
            }
            else
            {
                sLog.debug("We have NO Vendor Criteria !"); 
                deviceMap=new VendorDevice(hostName,null,null,null);
            }
            hostMap.add(deviceMap);
        }
        
        criteriaMap = new DomainCriteriaMap(domainName,interval,timeUnit,lTimeZone,hostMap,facilityMap,severityMap);
        return criteriaMap;
    }
    
    TimeZone getLocalTimeZone()
    {
        TimeZone zone = null;
        lTimeZone = TimeZone.getDefault(); 
        sLog.debug("Setting TimeZone to:"+lTimeZone.getDisplayName());
       	/*double offset = zone.getRawOffset() / (1000.0 * 60 * 60);
	String timeZoneName;
	if (offset == 0)
	{
		timeZoneName = zone.getID() + " " + zone.getDisplayName();
	}
	else
	{
		String offsetText = Double.toString(offset);
		if (offset >= 0) offsetText = "+" + offsetText;
		timeZoneName = "(GMT " + offsetText + ") "  + zone.getID() + " " + zone.getDisplayName();
		}GMT " + offsetText + ") "  + zone.getID() + " " + zone.getDisplayName();
	}		
    }*/
        
        return zone;
    }
    public void resetLastColectionDate(String pDomainName,Date pCollectionDate)
    {
        sLog.debug("Reseting LastCollectionTime  !");
        lConfigMgr.resetLastCollectionTime(pDomainName,dateToString(pCollectionDate));        
    }
    
    public Date getLastCollectionDate(String pDomain) 
    {
        sLog.debug("Geting CollectionDate from config file !");
        List domainList = lConfigMgr.getDomainList();
        Iterator it = domainList.iterator();
        String _date=null;
        while(it.hasNext())
        {
            ServiceDomain domain = (ServiceDomain) it.next();
            if(pDomain.equals(domain.getName()))
            {
                 _date=domain.getLastCollectionDate().getContent();
                 break;
            }
        }
        sLog.debug("recovered date from file:"+_date);
        return dateFromString(_date);
    }
    private String dateToString(Date pDate)
    {
        String lDate=null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);        
        String year=new Integer(cal.get(Calendar.YEAR)).toString();
        int _mon = new Integer(cal.get(Calendar.MONTH)).intValue();
        _mon++;
        String month =  new Integer(_mon).toString();        
        String day = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
        String hour =  new Integer(cal.get(Calendar.HOUR)).toString();
        String minute =  new Integer(cal.get(Calendar.MINUTE)).toString();
        String second =  new Integer(cal.get(Calendar.SECOND)).toString();
        
        
        lDate=year+":"+month+":"+day+":"+hour+":"+minute+":"+second;
        
        return lDate;
        
    }
    private Date dateFromString(String pDate)
    {
        Date date = null;
        ArrayList dateArray = new ArrayList();
            
        if(pDate!="")
        {
            String _date = pDate.trim();
            SimpleDateFormat m_dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");            
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
            //System.out.println("Date size:"+dateArray.size());
            /*
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
        }
          
            return date;
    }
}
