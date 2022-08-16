/*
 * RecordProcessor.java
 *
 * Created on July 16, 2003, 4:01 PM
 */

package com.perpetual.rp.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rmi.PortableRemoteObject;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.log.LogRecordFormat;
import com.perpetual.rp.control.query.SummaryQueryTemplate;
import com.perpetual.rp.control.vendor.VendorCounter;
import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.rp.model.vo.VendorSummaryVO;
import com.perpetual.rp.util.RPConstants;
import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;

/**
 *
 * @author  brunob
 */
public class SyslogRecordProcessor extends RecordProcessor
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger( SyslogRecordProcessor.class );    
        
    private DomainCriteriaMap lCriteriaMap=null;
    private static INavigator lNavigator = null; 
    private Date lLastCollectionTime =null;
    private Date lCurrentTime=null;
    private long lCollectionInterval=0;
    private Cursor lCursor=null;
    private boolean run=true;
    private boolean firstTime=true;
    private HostCounter hostCounter=null;
    private Map lDomainCounterMap=null;
    private String lDomainName=null;
    private ICollectionTime RPMgr=null;
    private static SummaryCRUDHome summaryHome=null;
    private static SummaryCRUD summaryCrud=null;
           
    /** Creates a new instance of RecordProcessor */
    public SyslogRecordProcessor(DomainCriteriaMap pCriteriaMap,INavigator pNavigator,
                                Map pDomainCounterMap,ICollectionTime pCollectionTime)
    {
        lCriteriaMap = pCriteriaMap;
        lNavigator = pNavigator;
        lDomainCounterMap = pDomainCounterMap;
        RPMgr=pCollectionTime;
        init();
        initialiseSummaryCRUD();
        
    }
     public SyslogRecordProcessor(DomainCriteriaMap pCriteriaMap,INavigator pNavigator,
                            Map pDomainCounterMap,Date pStartDate,Date pEndDate)
    {
       
        sLog.debug("RecordProcessor construction -from query- for Domain:"+pCriteriaMap.getDomainName());
       
        lCriteriaMap = pCriteriaMap;
        lNavigator = pNavigator;
        lDomainCounterMap = pDomainCounterMap;
        lLastCollectionTime = pStartDate;
        lCurrentTime = pEndDate;
        lDomainName=lCriteriaMap.getDomainName();
        initialiseSummaryCRUD();
        try
        {
            lCollectionInterval = getCollectionInterval(lCriteriaMap.getCollectionInterval(),lCriteriaMap.getTimeUnit());
        }
        catch(BasePerpetualException excp)
        {
            excp.printStackTrace();
        }
        }
   
    private void init()
    {
        sLog.debug("RecordProcessor init for Domain:"+lCriteriaMap.getDomainName());
        //get current time stamp & set lLastCollectionTimeStamp
        if(RPMgr.getLastCollectionDate(lCriteriaMap.getDomainName())==null)
        {
              lLastCollectionTime = prepareDate();
              sLog.debug("There is not specified start time for collection, setting now:"+lLastCollectionTime.toString());
            
              
        }
        else
        {
            lLastCollectionTime = RPMgr.getLastCollectionDate(lCriteriaMap.getDomainName());
            sLog.debug("The start collection date indicated in the config file is:"+lLastCollectionTime);
        }
        lDomainName=lCriteriaMap.getDomainName();
        //set CollectionTime stamp in the correct units
        try
        {
            lCollectionInterval = getCollectionInterval(lCriteriaMap.getCollectionInterval(),lCriteriaMap.getTimeUnit());
        }
        catch(BasePerpetualException excp)
        {
            excp.printStackTrace();
        }
     }
    
    private void initialiseSummaryCRUD()
    {
        
        if(summaryCrud==null)
        {
         try
            {
               Object homeObj = ServiceLocator.getServiceLocatorInstance().findHome(Constants.jndiName_SummaryCRUD);
               sLog.debug("got Object!");
               summaryHome = (SummaryCRUDHome) PortableRemoteObject.narrow(homeObj,SummaryCRUDHome.class); 
               sLog.debug("SummaryCRUDHome:"+summaryHome.toString());                    
               summaryCrud = summaryHome.create();
         }
         catch(Exception excp)
         {
             sLog.error("Couldn't find SUmmaryCRUD !");
             excp.printStackTrace();
         }
        }
    }
    public void run() 
    {
        //go to sleep !
        while(run)
        {        
            /*if(firstTime)
            {
                lLastCollectionTime = prepareDate();
                firstTime=false;
                sLog.debug("Started Thread @: "+lLastCollectionTime);      
                lDomainName= lCriteriaMap.getDomainName();
                
            }*/
                        
          //0) Go to sleep for the alloted ammount of time    
            try
            {
                sLog.debug("Going to sleep....!");
                synchronized(this)
                {                    
                   wait(lCollectionInterval);
                }
            }
            catch(java.lang.InterruptedException excp)
            {
                sLog.error("Domain Thread:"+lDomainName+" interrupted !");
            }
            
            sLog.debug("Ok, now I wake up !-"+lCriteriaMap.getDomainName()+"...gonna go read the logstore !");
           lCurrentTime = prepareDate();
             //1) run the query & get Cursor aka SummaryCollectionSet + process
            List hostSummaryList = getRecords(constructFilter());
            sLog.debug("----got Collection VO List !----");   
            if(hostSummaryList !=null)
            {
                Iterator it = hostSummaryList.iterator();
                while(it.hasNext())
                 {
                    HostSummaryVO vo = (HostSummaryVO) it.next();
                    showSummaryVO(vo);
                    //3) retrieve reference to SummaryRecord EJ B 
                    //4) insert record into DB
                    sLog.debug("Inserting VO into DB!");
                    try
                    {
                        summaryCrud.create(vo);
                    }
                    catch(javax.ejb.CreateException excp)
                    {
                        sLog.error("Could not insert Summary Log record into DB ! - ejb creation failure !");
                        excp.printStackTrace();
                    }
                    catch(java.rmi.RemoteException excp)
                    {
                        sLog.error("Could not insert Summary Log record into DB ! ");
                        excp.printStackTrace();
                   
                    }
                }
            }
            else
            {
                sLog.error("Did not find any Records matching the required criteria !...");           }
            
            //5) set lLastCollectionTime with current timestamp
              lLastCollectionTime = prepareDate();
              RPMgr.resetLastColectionDate(lDomainName,lLastCollectionTime);            
        }
        
    }
    //-------------------------------------------------
    public List getSummaryRecords()
    {
        sLog.debug("Getting summary Records for:"+lCriteriaMap.getDomainName()+"...gonna go read the logstore !");
           
             //1) run the query & get Cursor aka SummaryCollectionSet + process
            List hostSummaryList = getRecords(constructStaticFilter());
            sLog.debug("----got Collection VO List !----");            
                       
            return hostSummaryList;
    }
    
    //-------------------------------------------------
    private long getCollectionInterval(int pInterval,String pTimeUnit) throws BasePerpetualException
    {
        long value=0;
        if(pTimeUnit.equals(RPConstants.SECOND))
        {
            value = pInterval*1000;
            sLog.debug("Collection Interval is:"+pInterval+" seconds!");
        }
        else if(pTimeUnit.equals(RPConstants.MINUTE))
        {
            value = pInterval*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" minutes!");
        }
        else if(pTimeUnit.equals(RPConstants.HOUR))
        {
            value = pInterval*60*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" hours!");
        }
        else if(pTimeUnit.equals(RPConstants.DAY))
        {
            value = pInterval*24*60*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" days!");
        }
        else if(pTimeUnit.equals(RPConstants.WEEK))
        {
            value = pInterval*7*24*60*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" weeks!");
        }
        else if(pTimeUnit.equals(RPConstants.MONTH))
        {
            value = pInterval*30*7*24*60*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" months");
        }
        else if(pTimeUnit.equals(RPConstants.YEAR))
        {
            value = pInterval*12*30*7*24*60*60*1000;
            sLog.debug("Collection Interval is:"+pInterval+" years!");
        }
       else
        {
            sLog.error("Invalid time unit must bne one of :use-year,month,week,day,hour,minute,second");
            throw new BasePerpetualException("Invalid Time unit specified !");
            //value = pInterval*1000; 
        }
        return value;
    }
    private LogFilter constructStaticFilter()
    {
        sLog.debug("Constructing Static Log Query filter !");
        LogFilter  filter =null;
        HashMap pQueryMap = new HashMap();
               
        //1)set start time
           pQueryMap.put("startTime",lLastCollectionTime); 
           sLog.debug("startTime:"+lLastCollectionTime);
        //2) set end time
           pQueryMap.put("maxTime",lCurrentTime);
           sLog.debug("maxTime:"+lCurrentTime);
        //3) set facility list
           List facList  =lCriteriaMap.getMeasurableFacilities();
           int facListSize=facList.size();
           String [] facArray = new String [facListSize];
           for(int i=0;i<facListSize;i++)
           {
               Integer fac = (Integer) facList.get(i);
               facArray[i] = fac.toString();
               sLog.debug("fac:"+i+" "+fac.toString());
           }          
           pQueryMap.put("Facility", facArray);           
        //4) set severityList
            List sevList  =lCriteriaMap.getMeasurableSeverities();
           int sevListSize=sevList.size();
           String [] sevArray = new String [sevListSize];
           for(int i=0;i<sevListSize;i++)
           {
               Integer sev = (Integer) sevList.get(i);
               sevArray[i] = sev.toString();
               sLog.debug("sev:"+i+" "+sev.toString());
           }          
           pQueryMap.put("Severity",sevArray);
           //5) set host list
           List hostList  =lCriteriaMap.getHostList();
           int hostListSize=hostList.size();
           String [] hostArray = new String [hostListSize];
           for(int i=0;i<hostListSize;i++)
           {
               VendorDevice vd = (VendorDevice) hostList.get(i);              
               hostArray[i] = vd.getHostName();
               sLog.debug("host:"+i+" "+vd.getHostName());
           }          
           pQueryMap.put("Host",hostArray);
          
        //6) set directory
           pQueryMap.put("filetypes", new Object[] { "archive" });
                
        filter = new LogFilter(pQueryMap);
        return filter;
    }
    private LogFilter constructFilter()
    {
        sLog.debug("Constructing Log Query filter !");
        LogFilter  filter =null;
        HashMap pQueryMap = new HashMap();
       
        //1)set start time
           pQueryMap.put("startTime",lLastCollectionTime); 
           sLog.debug("startTime:"+lLastCollectionTime);
        //2) set end time
           pQueryMap.put("maxTime",lCurrentTime);
           sLog.debug("maxTime:"+lCurrentTime);
        //3) set facility list
           List facList  =lCriteriaMap.getMeasurableFacilities();
           int facListSize=facList.size();
           String [] facArray = new String [facListSize];
           for(int i=0;i<facListSize;i++)
           {
               Integer fac = (Integer) facList.get(i);
               facArray[i] = fac.toString();
               sLog.debug("fac"+i+": "+fac.toString());
           }          
           pQueryMap.put("Facility", facArray);           
        //4) set severityList
            List sevList  =lCriteriaMap.getMeasurableSeverities();
           int sevListSize=sevList.size();
           String [] sevArray = new String [sevListSize];
           for(int i=0;i<sevListSize;i++)
           {
               Integer sev = (Integer) sevList.get(i);
               sevArray[i] = sev.toString();
               sLog.debug("sev"+i+": "+sev.toString());
           }          
           pQueryMap.put("Severity",sevArray);
           //5) set host list
           List hostList  =lCriteriaMap.getHostList();
           int hostListSize=hostList.size();
           String [] hostArray = new String [hostListSize];
           for(int i=0;i<hostListSize;i++)
           {
               VendorDevice vd = (VendorDevice) hostList.get(i);              
               hostArray[i] = vd.getHostName();
               sLog.debug("host"+i+": "+vd.getHostName());
           }          
           pQueryMap.put("Host",hostArray);
          
        //6) set directory
           pQueryMap.put("filetypes", new Object[] { "archive" });
                
        filter = new LogFilter(pQueryMap);
        return filter;
    }
    //-------the biggest & baddesst ass methof in the class----------   
    public List processRecords(Cursor pCursor) 
    {
        List hostSummaryList=null;
        sLog.debug("About to start processing  a batch or records !");
        Date timeField=null;
        LogRecordFormat.Field hostField=null;
        LogRecordFormat.Field procField=null;
        LogRecordFormat.Field pidField=null;
        LogRecordFormat.Field severityField=null;
        LogRecordFormat.Field facilityField=null;
        LogRecordFormat.Field messageField=null;  
        LogRecord nextLogRecord=null;
        LogRecord record=null;
        boolean empty=true;
              
        int cntr=0;
        try
        {
           
                 
            for(;(nextLogRecord=pCursor.nextLogRecord())!=null;)
            {
                empty=false;
                cntr++;
                record = pCursor.currentLogRecord();                               
                                
                timeField = record.getTime();
                hostField = record.getField("Host");
                //procField = record.getField("Proc");
                //pidField = record.getField("PID");            
                severityField = record.getField("Severity");
                facilityField = record.getField("Facility");
                messageField =  record.getField("Message");
               
                /*sLog.debug("+++Data in Record:");
                sLog.debug("    Time:Host:Severity:Facility:Message = "+timeField.toString()+
                                        ":"+hostField.toString()+":"+severityField.toString()+":"
                                        +facilityField.toString()+":"+messageField.toString());
                */               
                
                Integer severity = new Integer(getSeverity(severityField.toString()));
                Integer facility = new Integer(getFacility(facilityField.toString()));
                String  message  = messageField.toString();
                String  host     = hostField.toString();
                String  time =  timeField.toString();
                sLog.debug("Record"+cntr+":"+time+":"+host+":"+severity.intValue()+":"
                                        +facility.intValue()+":"+message);
                
                validateRecord(severity,facility,host,message);                              
                
                record = pCursor.nextLogRecord();
            }
            
        }
        catch(Exception excp)
        {
                sLog.error("Error retrieving field! ");
                excp.printStackTrace();
        }
            
         if(empty)
         {
             sLog.error("Empty Record Set !");
             return null;
         }
        hostSummaryList = constructHostVOList();
             
        return hostSummaryList;
    }
    private int getFacility(String pFacility)
    {
        int fac=0;
        
        if(pFacility.equals("Kernel"))
            fac= 0;
         if(pFacility.equals("User"))
            fac=1;
         if(pFacility.equals("Mail"))
            fac=2;
         if(pFacility.equals("Daemon"))
            fac=3;
         if(pFacility.equals("Security"))
            fac=4;
         if(pFacility.equals("Syslog"))
            fac=5;
         if(pFacility.equals("Lpr"))
            fac=6;
         if(pFacility.equals("News"))
            fac=7;
         if(pFacility.equals("UUCP"))
            fac=8;
         if(pFacility.equals("Crond"))
            fac=9;
         if(pFacility.equals("Authority"))
            fac=10;
         if(pFacility.equals("FTP"))
            fac=11;
         if(pFacility.equals("NTP"))
            fac=12;
         if(pFacility.equals("Audit"))
            fac=13;
         if(pFacility.equals("Alert"))
            fac=14;
         if(pFacility.equals("Local0"))
            fac=16;
         if(pFacility.equals("Local1"))
            fac=17;
         if(pFacility.equals("Local2"))
            fac=18;
        if(pFacility.equals("Local3"))
            fac=19;
        if(pFacility.equals("Local4"))
            fac=20;
        if(pFacility.equals("Local5"))
            fac=21;
        if(pFacility.equals("Local6"))
            fac=22;
        if(pFacility.equals("Local7"))
            fac=23;
      return fac;
    }
    private int getSeverity(String pSeverity)
    {
        int sev=0;
        if(pSeverity.equals("Emergency"))
            sev=0;
         if(pSeverity.equals("Alert"))
            sev=1;
         if(pSeverity.equals("Critical"))
            sev=2;
         if(pSeverity.equals("Error"))
            sev=3;
         if(pSeverity.equals("Warning"))
            sev=4;
         if(pSeverity.equals("Notice"))
            sev=5;
         if(pSeverity.equals("Info"))
            sev=6;
         if(pSeverity.equals("Debug"))
            sev=7;
        
        return sev;
    }
    private List constructHostVOList()
    {
        sLog.debug("Building VO from Counter !");
        HostSummaryVO vo=null;
        ArrayList hostSummaryList = new ArrayList();
        Collection hostCounters = lDomainCounterMap.values();
        Iterator it = hostCounters.iterator();
        int cntr=0;
        while(it.hasNext())
        {
            cntr++;
            HostCounter hostCounter = (HostCounter) it.next();
            vo = new HostSummaryVO(hostCounter.hostName,lDomainName, null,
            			/* hostCounter.vendorName, */
                        /* hostCounter.deviceType, */ lLastCollectionTime,lCurrentTime,
                        constructVendorVO(hostCounter.vendorCounter));
            
            vo.fac0=hostCounter.facilityCounter.Kernel;
            vo.fac1=hostCounter.facilityCounter.User;
            vo.fac2=hostCounter.facilityCounter.Mail;
            vo.fac3=hostCounter.facilityCounter.Daemon;
            vo.fac4=hostCounter.facilityCounter.Security;
            vo.fac5=hostCounter.facilityCounter.Syslog;
            vo.fac6=hostCounter.facilityCounter.Lpr;
            vo.fac7=hostCounter.facilityCounter.News;
            vo.fac8=hostCounter.facilityCounter.UUCP;
            vo.fac9=hostCounter.facilityCounter.Crond;
            vo.fac10=hostCounter.facilityCounter.Authority;
            vo.fac11=hostCounter.facilityCounter.FTP;
            vo.fac12=hostCounter.facilityCounter.NTP;
            vo.fac13=hostCounter.facilityCounter.Audit;
            vo.fac14=hostCounter.facilityCounter.Alert;
            vo.fac15=hostCounter.facilityCounter._Crond;
            vo.fac16=hostCounter.facilityCounter.Local0;
            vo.fac17=hostCounter.facilityCounter.Local1;
            vo.fac18=hostCounter.facilityCounter.Local2;
            vo.fac19=hostCounter.facilityCounter.Local3;
            vo.fac20=hostCounter.facilityCounter.Local4;
            vo.fac21=hostCounter.facilityCounter.Local5;
            vo.fac22=hostCounter.facilityCounter.Local6;
            vo.fac23=hostCounter.facilityCounter.Local7;
            
            vo.sev0 = hostCounter.severityCounter.Emergency;
            vo.sev1 = hostCounter.severityCounter.Alert;
            vo.sev2 = hostCounter.severityCounter.Critical;
            vo.sev3 = hostCounter.severityCounter.Error;
            vo.sev4 = hostCounter.severityCounter.Warning;
            vo.sev5 = hostCounter.severityCounter.Notice;
            vo.sev6 = hostCounter.severityCounter.Info;
            vo.sev7 = hostCounter.severityCounter.Debug;
            
            hostSummaryList.add(vo);
        }
        
        sLog.debug("processed records for:"+cntr+" hosts!");
        return hostSummaryList;
    }
    private VendorSummaryVO constructVendorVO(VendorCounter pVendorCounter)
    {
        VendorSummaryVO vendorVO=null;
        
        return vendorVO;
    }
    private void validateRecord(Integer pSeverity,Integer pFacility,String pHost,String pMessage)
    {
        sLog.debug("Validating whether this record is to be considered or not !");
        
        if(lDomainCounterMap.containsKey(pHost))
        {
            sLog.debug("This is message for host:"+pHost);
            HostCounter counter = (HostCounter) lDomainCounterMap.get(pHost);
            sLog.debug("Matching Facility");
            Iterator fit = (lCriteriaMap.getMeasurableFacilities()).iterator();
            while(fit.hasNext())
            {
               Integer fac = (Integer) fit.next(); 
               if(pFacility.equals(fac))
               {
                   countFacility(fac.intValue(),counter);
               } 
            }           
            sLog.debug("Matching Severity");
            Iterator sit = (lCriteriaMap.getMeasurableSeverities()).iterator();
            while(sit.hasNext())
            {
               Integer sev = (Integer) sit.next(); 
               if(pSeverity.equals(sev))
               {
                   sLog.debug("Found Severity:"+sev);
                   countSeverity(sev.intValue(),counter);
               } 
            }
            sLog.debug("Matching Vendor Criteria");            
            
            countVendorCriteria(pMessage,counter);          
           
        }
    }                 
    private void countVendorCriteria(String pMessage,HostCounter pCounter)
    {
        sLog.debug("incrementing Vendor Criteria");
        sLog.debug("========Need to setup Message Parser Framework====CISCO/Apache etc==");
    }
    private void countSeverity(int pSeverity,HostCounter pCounter)
    {
        sLog.debug("incrementing severity");
        if(pSeverity==0)
        {
            //emergency
            pCounter.severityCounter.Emergency++;
        }
         if(pSeverity==1)
        {
            //emergency
            pCounter.severityCounter.Alert++;
        }
         if(pSeverity==2)
        {
            //critical
            pCounter.severityCounter.Critical++;
        }
         if(pSeverity==3)
        {
            //error
            pCounter.severityCounter.Error++;
        }
         if(pSeverity==4)
        {
            //warning
            pCounter.severityCounter.Warning++;
        }
         if(pSeverity==5)
        {
            //notice
            pCounter.severityCounter.Notice++;
        }
         if(pSeverity==6)
        {
            ///Info
            //sLog.debug("Incrementing Severity -6:Info -!");
            pCounter.severityCounter.Info++;
        }
        if(pSeverity==7)
        {
            //Info
            pCounter.severityCounter.Debug++;
        }
    }
    private void countFacility(int pFacility,HostCounter pCounter)
    {
        sLog.debug("incrementing facility");
        if(pFacility==0)
        {
            //kenel
            pCounter.facilityCounter.Kernel++;
        }
        if(pFacility==1)
        {
            //User
            pCounter.facilityCounter.User++;
        }
        if(pFacility==2)
        {
            //Mail
            pCounter.facilityCounter.Mail++;
        }
        if(pFacility==3)
        {
            //Daemon
            pCounter.facilityCounter.Daemon++;
        }
        if(pFacility==4)
        {
            //Security
            pCounter.facilityCounter.Security++;
        }
        if(pFacility==5)
        {
            //Syslog
            pCounter.facilityCounter.Syslog++;
        }
        if(pFacility==6)
        {
            //Lpr
            pCounter.facilityCounter.Lpr++;
        }
        if(pFacility==7)
        {
            //News
            pCounter.facilityCounter.News++;
        }
        if(pFacility==8)
        {
            //UUCP
            pCounter.facilityCounter.UUCP++;
        }
        if(pFacility==9)
        {
            //Crond
            pCounter.facilityCounter.Crond++;
        }
        if(pFacility==10)
        {
            //Authority
            pCounter.facilityCounter.Authority++;
        }
        if(pFacility==11)
        {
            //ftp
            pCounter.facilityCounter.FTP++;
        }
        if(pFacility==12)
        {
            //ntp
            pCounter.facilityCounter.NTP++;
        }
        if(pFacility==13)
        {
            //audit
            pCounter.facilityCounter.Audit++;
        }
        if(pFacility==14)
        {
            //Alert
            pCounter.facilityCounter.Alert++;
        }
        if(pFacility==15)
        {
            //_Crond
            pCounter.facilityCounter._Crond++;
        }if(pFacility==16)
        {
            //Local0
            pCounter.facilityCounter.Local0++;
        }
        if(pFacility==17)
        {
            //Local1
            pCounter.facilityCounter.Local1++;
        }
        if(pFacility==18)
        {
            //Local2
            pCounter.facilityCounter.Local2++;
        }
        if(pFacility==19)
        {
            //Local3
            pCounter.facilityCounter.Local3++;
        }
        if(pFacility==20)
        {
            //Local4
            pCounter.facilityCounter.Local4++;
        }
        if(pFacility==21)
        {
            //Local5
            pCounter.facilityCounter.Local5++;
        }
        if(pFacility==22)
        {
            //Local6
            pCounter.facilityCounter.Local6++;
        }
        if(pFacility==23)
        {
            //Local7
            pCounter.facilityCounter.Local7++;
        }
       
        
    }
    public synchronized void stop() 
    {
        run=false;
        this.notify();
    }
    
    private List getRecords(LogFilter pFilter)
    {
          sLog.debug("Doing query against Logstore !");
            try
            {                
                lCursor = lNavigator.retrieveLogRecords(pFilter);
            }
            catch(Exception excp)
            {
                sLog.error("Error in querying the logstore !");
                excp.printStackTrace();
            }
   
           //2) begin processing the result set & construct the SummaryCollectionVO
            List hostSummaryList = processRecords(lCursor);            
           
            return hostSummaryList;
    }
    public List getSummaryRecords(SummaryQueryTemplate pTemplate) throws BasePerpetualException
    {
        List hostSummaryList=null;
        /*
        if(pTemplate.queryLogstore())
        {
            return getRecords(new LogFilter(pTemplate.getQueryParameterMap()));
        }
         */       
        return hostSummaryList;
    }
    
    private Date prepareDate()
    {
        Date date = null;        
        Calendar cal = Calendar.getInstance();
        //Calendar cal = Calendar.getInstance(lCriteriaMap.getTimeZone());
	sLog.debug("Date obtained:"+cal.toString());
	date = cal.getTime();
        return date;
    }
    private void showSummaryVO(HostSummaryVO vo)
    {
                                
          sLog.debug("-----host:"+vo.getHostName()+"--------");
          sLog.debug("Domain:"+vo.getDomainName());
          sLog.debug("StartTime:"+vo.getStartTimeStamp());
          sLog.debug("EndTime:"+vo.getEndTimeStamp());
          sLog.debug("----------Facilities----------");
          sLog.debug("Fac0:"+vo.fac0);
          sLog.debug("Fac1:"+vo.fac1);
          sLog.debug("Fac2:"+vo.fac2);
          sLog.debug("Fac3:"+vo.fac3);
          sLog.debug("Fac4:"+vo.fac4);
          sLog.debug("Fac5:"+vo.fac5);
          sLog.debug("Fac6:"+vo.fac6);
          sLog.debug("Fac7:"+vo.fac7);
          sLog.debug("Fac8:"+vo.fac8);
          sLog.debug("Fac9:"+vo.fac9);
          sLog.debug("Fac10:"+vo.fac10);
          sLog.debug("Fac11:"+vo.fac11);
          sLog.debug("Fac12:"+vo.fac12);
          sLog.debug("Fac13:"+vo.fac13);
          sLog.debug("Fac14:"+vo.fac14);
          sLog.debug("Fac15:"+vo.fac15);
          sLog.debug("Fac16:"+vo.fac16);
          sLog.debug("Fac17:"+vo.fac18);
          sLog.debug("Fac19:"+vo.fac19);
          sLog.debug("Fac20:"+vo.fac20);
          sLog.debug("Fac21:"+vo.fac21);
          sLog.debug("Fac22:"+vo.fac22);
          sLog.debug("Fac23:"+vo.fac23);
          sLog.debug("---------Severities-----------");
          sLog.debug("Sev0:"+vo.sev0);
          sLog.debug("Sev1:"+vo.sev1);
          sLog.debug("Sev2:"+vo.sev2);
          sLog.debug("Sev3:"+vo.sev3);
          sLog.debug("Sev4:"+vo.sev4);
          sLog.debug("Sev5:"+vo.sev5);
          sLog.debug("Sev6:"+vo.sev6);
          sLog.debug("Sev7:"+vo.sev7);
    }
    
}
