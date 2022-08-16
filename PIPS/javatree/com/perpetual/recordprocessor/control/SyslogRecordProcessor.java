/*
 * RecordProcessor.java
 *
 * Created on July 16, 2003, 4:01 PM
 */

package com.perpetual.recordprocessor.control;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.rmi.PortableRemoteObject;

import com.perpetual.application.collector.syslog.SyslogConstants;
import com.perpetual.exception.BasePerpetualException;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogFilter;
import com.perpetual.logserver.LogRecord;
import com.perpetual.logserver.LogRecordFormat;
import com.perpetual.recordprocessor.control.HostCounter.MessagePatternCounter;
import com.perpetual.recordprocessor.control.domain.DomainData;
import com.perpetual.recordprocessor.control.domain.IDomainChangeObserver;
import com.perpetual.rp.control.query.SummaryQueryTemplate;
import com.perpetual.rp.control.vendor.VendorCounter;
import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.rp.model.vo.VendorSummaryVO;
import com.perpetual.rp.util.RPConstants;
import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.util.Constants;
import com.perpetual.util.GMTFormatter;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;

/**
 *
 * @author  brunob
 */
public class SyslogRecordProcessor extends RecordProcessor
	implements IDomainChangeObserver
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger( SyslogRecordProcessor.class );    
        
    private DomainData domainData = null;
	private Map domainCounterMap = null;
	RPConfigManager configManager = null;
	
    private static INavigator lNavigator = null; 
    private long lastCollectionTime = 0;
    private GMTFormatter formatter;

	private long currentTime = 0;
	
	private Thread currentThread = null;

    private Cursor lCursor=null;
    private boolean run=true;
    private boolean firstTime=true;
    private HostCounter hostCounter=null;
   
    private String lDomainName=null;
    
    private static SummaryCRUDHome summaryHome=null;
    private static SummaryCRUD summaryCrud=null;
    
    private RecordProcessorManager manager = null;
           
    /** Creates a new instance of RecordProcessor */
    public SyslogRecordProcessor(DomainData domainData,
    							RecordProcessorManager manager,
                                INavigator pNavigator)
    {
        this.domainData = domainData;
        lNavigator = pNavigator;
        this.domainCounterMap = getDomainCounter(domainData);
        this.manager = manager;
        this.formatter = new GMTFormatter();
      
        init();
        initialiseSummaryCRUD();
        
    }
    
	private Map getDomainCounter(DomainData domainData)
	{
		sLog.debug("Constructing the Domain Counter !");
		HashMap domainCounterMap = new HashMap();
		HostCounter hostCounter=null;
		VendorCounter vendorCounter=null;
        
		Collection hosts = domainData.getHosts();
        
		for (Iterator i = hosts.iterator(); i.hasNext(); ) {
			HostVO hostVO = (HostVO) i.next();
			hostCounter = new HostCounter(hostVO, domainData.getMessagePatterns());
			domainCounterMap.put(hostVO.getName(), hostCounter);        	
		}
  
		return domainCounterMap;
	}
   
	private void init()
    {
        sLog.debug("RecordProcessor init for Domain:"
        		+ this.domainData.getDomainVO().getName());

		Long lastCollectionTimeLong = this.domainData.getDomainVO()
			.getLastCollectionTime();        		
        
        //get current time stamp & set lLastCollectionTimeStamp
        if (lastCollectionTimeLong == null)
        {
              //this.lastCollectionTime = 0;
            this.lastCollectionTime=System.currentTimeMillis() - RPConstants.LagTime - 1;
            
              sLog.debug("There is not specified start time for collection," 
              		+ " setting at epoch: " +
              		this.formatter.convertToGMT(this.lastCollectionTime));
        }
        else
        {
        	this.lastCollectionTime = lastCollectionTimeLong.longValue();
            sLog.debug("The start collection date (in GMT) is:"
            	+ this.formatter.convertToGMT(this.lastCollectionTime));
        }

		sLog.debug("The collection interval is " + this.domainData.getDomainVO()
				.getCollectionInterval().longValue());
	
		sLog.debug("The lag time is " + RPConstants.LagTime + " ms.");
     }
    
	private void initialiseSummaryCRUD()
	{
		if (summaryCrud==null) {

			try {
				Object homeObj = ServiceLocator.findHome(Constants.jndiName_SummaryCRUD);
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
		this.currentThread = Thread.currentThread();
		
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
			    Thread.sleep(this.domainData.getDomainVO()
						.getCollectionInterval().longValue());
			}
			catch(java.lang.InterruptedException excp)
			{
			    sLog.error("Domain Thread:"
			    		+ this.domainData.getDomainVO().getName() + " interrupted !");
			}
			
			if (!run) {
				break;
			}
            
			sLog.info("Waking up for processing of " 
					+ this.domainData.getDomainVO().getName()
					+ "...reading the logstore !");
					
			// collection interval should be [start, current - lag - 1]
			// the -1 is to ensure we don't get overlapping intervals		
            this.currentTime = System.currentTimeMillis() - RPConstants.LagTime - 1;
            
            // 1) run the query & get Cursor aka SummaryCollectionSet + process
            List hostSummaryList = getRecords(constructFilter());
            
            sLog.debug("----got Collection VO List !----");   
            if(hostSummaryList != null)
            {
                Iterator it = hostSummaryList.iterator();
                while(it.hasNext())
                 {
                    HostSummaryVO vo = (HostSummaryVO) it.next();
                    //showSummaryVO(vo);
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
                sLog.debug("Did not find any Records matching the required criteria !...");
            }
            
            //5) set lLastCollectionTime with current timestamp
            // and update Domain table
            
			this.lastCollectionTime = this.currentTime;
			
			resetLastCollectionTime();            
        }
        
        sLog.info("Processing thread for domain '"
        			+ this.domainData.getDomainVO().getName()
					+ " is shutting down");
        
    }
 
	private LogFilter constructFilter()
	{
		sLog.debug("Constructing Log Query filter !");
		LogFilter  filter =null;
		HashMap pQueryMap = new HashMap();
       
		//1)set start time
		pQueryMap.put("startTime", new Date(this.lastCollectionTime)); 
		sLog.debug("startTime:"
           		+ this.formatter.convertToGMT(this.lastCollectionTime));
           		
		//2) set end time
		pQueryMap.put("maxTime", new Date(this.currentTime));
		sLog.debug("maxTime:"
           		+ this.formatter.convertToGMT(this.currentTime));

//		pQueryMap.put("startTime",
//				new Date(this.formatter.convertToMsFromEpoch("20030903-051000-0")));
//
//		pQueryMap.put("maxTime",
//				new Date(this.formatter.convertToMsFromEpoch("20030903-053500-0")));

          
        sLog.debug("Collection interval = " +
        		((double) this.currentTime - this.lastCollectionTime)/1000. + " seconds.");
        				
		//3) set facility list
		Collection facilities = this.domainData.getFacilities();
		String [] facArray = new String [facilities.size()];
		int index = 0;
		for (Iterator i = facilities.iterator(); i.hasNext(); index++) {
			facArray[index] = String.valueOf(((FacilityVO) i.next()).getId());
		}
		pQueryMap.put("Facility", facArray);           
        
        //4) set severityList
		Collection severities = this.domainData.getSeverities();
		String [] sevArray = new String [severities.size()];
		index = 0;
		for (Iterator i = severities.iterator(); i.hasNext(); index++) {
			sevArray[index] = String.valueOf(((SeverityVO) i.next()).getId());
		}
		pQueryMap.put("Severity", sevArray);

		//5) set host list
		
		Collection hosts = this.domainData.getHosts();
		String [] hostArray = new String [hosts.size()];
		index = 0;
		for (Iterator i = hosts.iterator(); i.hasNext(); index++) {
			hostArray[index] = ((HostVO) i.next()).getName();  // use Name - this is the IP or a symbolic name
		}
		pQueryMap.put("Host", hostArray);
          
        //6) set directory
		//pQueryMap.put("filetypes", new Object[] { "archive" });
                
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
        
		// reset counts
		this.domainCounterMap = getDomainCounter(this.domainData);
        
        int cntr=0;
        try
        {
           
                 
            while ((nextLogRecord = pCursor.nextLogRecord()) != null)
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
               
 				sLog.debug("+++Data in Record:");
                sLog.debug("    Time:Host:Severity:Facility:Message = "+timeField.toString()+
                                        ":"+hostField.toString()+":"+severityField.toString()+":"
                                        +facilityField.toString()+":"+messageField.toString());               
                
                Integer severity = new Integer(SyslogConstants
                			.getSeverityIdByLongName(severityField.toString()));
                Integer facility = new Integer(SyslogConstants
                			.getFacilityIdByLongName(facilityField.toString()));
                String  message  = messageField.toString();
                String  host     = hostField.toString();
                String  time =  timeField.toString();
                sLog.debug("Record"+cntr+":"+time+":"+host+":"+severity.intValue()+":"
                                        +facility.intValue()+":"+message);
                
                validateRecord(severity,facility,host,message);                              
            }
            
			//System.out.println("-------- found " + cntr + " matching records");  

			sLog.info("found " + cntr + " matching records for " 
					+ hostField  + " for " + this.domainData.getDomainVO().getName());
            
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
    
    private List constructHostVOList()
    {
        sLog.debug("Building VO from Counter !");
        HostSummaryVO vo=null;
        ArrayList hostSummaryList = new ArrayList();
        Collection hostCounters = this.domainCounterMap.values();
        Iterator it = hostCounters.iterator();
        int cntr=0;
        while(it.hasNext())
        {
            cntr++;
            HostCounter hostCounter = (HostCounter) it.next();
     
     		if (hostCounter.messagePatternCounter.keySet().size() == 0) {
     			hostSummaryList.add(constructSummaryVO(hostCounter, null));       
     		} else {
	            // for each message pattern construct its own vo
				for (Iterator i = hostCounter.messagePatternCounter.keySet().iterator();
							 i.hasNext();) {
					
					Integer patternId = (Integer) i.next();
							 	
					vo = constructSummaryVO(hostCounter, patternId);
					hostSummaryList.add(vo);	
				}
     		}    
        }
        
        sLog.debug("processed records for:"+cntr+" hosts!");
        return hostSummaryList;
    }

	private HostSummaryVO constructSummaryVO(HostCounter hostCounter,
			Integer patternId) {

		HostSummaryVO vo;
		
		vo = new HostSummaryVO(hostCounter.getHostVO().getName(),
		    			this.domainData.getDomainVO().getName(),
		    			patternId,
		                new Date(this.lastCollectionTime),
		                new Date(this.currentTime),
		                constructVendorVO(null));
		
		MessagePatternCounter counter = (MessagePatternCounter) hostCounter
				.messagePatternCounter.get(patternId);
		    
		vo.fac0=counter.facilityCounter.Kernel;
		vo.fac1=counter.facilityCounter.User;
		vo.fac2=counter.facilityCounter.Mail;
		vo.fac3=counter.facilityCounter.Daemon;
		vo.fac4=counter.facilityCounter.Security;
		vo.fac5=counter.facilityCounter.Syslog;
		vo.fac6=counter.facilityCounter.Lpr;
		vo.fac7=counter.facilityCounter.News;
		vo.fac8=counter.facilityCounter.UUCP;
		vo.fac9=counter.facilityCounter.Crond;
		vo.fac10=counter.facilityCounter.Authority;
		vo.fac11=counter.facilityCounter.FTP;
		vo.fac12=counter.facilityCounter.NTP;
		vo.fac13=counter.facilityCounter.Audit;
		vo.fac14=counter.facilityCounter.Alert;
		vo.fac15=counter.facilityCounter._Crond;
		vo.fac16=counter.facilityCounter.Local0;
		vo.fac17=counter.facilityCounter.Local1;
		vo.fac18=counter.facilityCounter.Local2;
		vo.fac19=counter.facilityCounter.Local3;
		vo.fac20=counter.facilityCounter.Local4;
		vo.fac21=counter.facilityCounter.Local5;
		vo.fac22=counter.facilityCounter.Local6;
		vo.fac23=counter.facilityCounter.Local7;
		
		vo.sev0 = counter.severityCounter.Emergency;
		vo.sev1 = counter.severityCounter.Alert;
		vo.sev2 = counter.severityCounter.Critical;
		vo.sev3 = counter.severityCounter.Error;
		vo.sev4 = counter.severityCounter.Warning;
		vo.sev5 = counter.severityCounter.Notice;
		vo.sev6 = counter.severityCounter.Info;
		vo.sev7 = counter.severityCounter.Debug;

		return vo;
	}
    
    private VendorSummaryVO constructVendorVO(VendorCounter pVendorCounter)
    {
        VendorSummaryVO vendorVO=null;
        
        return vendorVO;
    }
    
	private void validateRecord(Integer pSeverity, Integer pFacility,
			String pHost, String pMessage)
    {
		sLog.debug("Performing filtered count on syslog record.");
		sLog.debug("Severity = " + pSeverity + ", Facility = "
				+ pFacility + ", Host = " + pHost);
                boolean defaultPattern=true;
                MessagePatternCounter patternCounter =null;
                Integer defaultValue = new Integer(-1);
        
		if (this.domainCounterMap.containsKey(pHost))
		{
			sLog.debug("This is message for host:" + pHost);
			HostCounter counter = (HostCounter) this.domainCounterMap.get(pHost);

			sLog.debug("Matching Facility");
			Iterator fit = (this.domainData.getFacilities()).iterator();
            
                        // loop through the message pattern counters
                        // and for each match, do a count
                            if(counter.messagePatternCounter.containsKey(defaultValue))
                            {
                                sLog.debug("We have a default pattern !");
                                patternCounter = (MessagePatternCounter)counter.messagePatternCounter.get(defaultValue);
                            }
                            else
                            {
                                sLog.debug("We have some real patterns to match against-looking for the right pattern!");
                                for (Iterator i = counter.messagePatternCounter.keySet().iterator();i.hasNext();) 
                                {            	
                                    Integer patternId = (Integer) i.next();
                                    MessagePatternCounter tmpPatternCounter = 
                                            (MessagePatternCounter) counter.messagePatternCounter
                                                            .get(patternId);
                                        if(tmpPatternCounter!=null)
                                        {
                                            Matcher matcher = tmpPatternCounter.pattern.matcher(pMessage);	
                                            if (matcher.matches()) 
                                            {
                                                  sLog.debug("message text `" + pMessage + "' matches " + 
                                                                    tmpPatternCounter.messagePatternData.getPattern());
                                                patternCounter=tmpPatternCounter;
                                                defaultPattern=false;
                                                break;
                                            }
                                          }
                                     }        
                                     if(defaultPattern)
                                     {
                                        sLog.debug("No valid pattern found,using a default pattern !");
                                        counter.createDefaulMessagePatternCounter();
                                        patternCounter = (MessagePatternCounter)counter.messagePatternCounter.get(defaultValue);
                                     }
                            }
                              // find the facility and count it
                            while(fit.hasNext())
                            {
                                    int fac = ((FacilityVO) fit.next()).getId(); 
                                    if (pFacility.intValue() == fac)
                                    {
                                            countFacility(fac, patternCounter);
                                    } 
                            }           
                            // find the severity and count it
                            sLog.debug("Matching Severity");
                            Iterator sit = this.domainData.getSeverities().iterator();

                            while(sit.hasNext())
                            {
                               int sev = ((SeverityVO) sit.next()).getId();
                               if(pSeverity.intValue() == sev)
                               {
                                       sLog.debug("Found Severity:"+sev);
                                       countSeverity(sev, patternCounter);
                               } 
                            }
        }
    }                 

    private void countSeverity(int pSeverity, MessagePatternCounter pCounter)
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
    private void countFacility(int pFacility, MessagePatternCounter pCounter)
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
    
    private void resetLastCollectionTime ()
    {
		DomainCRUD domainCRUD = null;
		
    	try {
    	
	    	domainCRUD = this.manager.getDomainCRUDHome().create();
	    	
	    	DomainVO dvo = this.domainData.getDomainVO();

			// refresh the Domain data
			DomainVO newDvo = domainCRUD.retrieveByPrimaryKey(dvo);

			sLog.debug("Setting the last collection time for domain '"
					+ dvo.getName() + "' to "
					+ this.formatter.convertToGMT(this.currentTime) );
								
	    	newDvo.setLastCollectionTime(new Long(this.currentTime));
	    	
	    	domainCRUD.update(newDvo);
	    	
	    	this.domainData.setDomainVO(newDvo);
		} catch (ObjectNotFoundException e) {
			// domain has been removed and we haven't received notification yet
			// shutdown thread
			this.run = false;
	    } catch (Exception e) {
	    	sLog.error("Error updating domain table with last collection date: " + e);
    	} finally {
    		if (domainCRUD != null) {
  			try {
					domainCRUD.remove();
				} catch (RemoteException e1) {
				} catch (RemoveException e1) {
				}
    		}
    	}
    }
    
	public void notify (DomainData newDomainData, int eventType)
	{
		// make sure we have notification for the right domain
		if (newDomainData.getDomainVO().getId() ==
				this.domainData.getDomainVO().getId()) {
			sLog.info("Got a domain CHANGE notification, domain id = "
					+ this.domainData.getDomainVO().getId());
				
			this.domainData = newDomainData;
			this.domainCounterMap = getDomainCounter(this.domainData);		
		}
	}
	
	public void shutdown()
	{
		sLog.info("Shutdown request for processing thread, domain id = "
				+ this.domainData.getDomainVO().getId());
		run = false;		
		this.currentThread.interrupt();
	}
	
	
	public void showQueryMap (Map queryMap)
	{
		for (Iterator i = queryMap.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			
			System.out.println("value for key '" + key + "' = " + key);
			Object value = queryMap.get(key);
			
			if (value instanceof String[]) {
				String[] sarray = (String []) value;
				for (int j = 0; j < sarray.length; j++) {
					System.out.println("value[" + j + "] = " + sarray[j]); 
				}
			} else if (value instanceof Date) {
				Date date = (Date) value;
				System.out.println("date = "
						+ this.formatter.convertToGMT(date.getTime()));
			} else {
				System.out.println("Unknown type ..." + value.toString());
			}
		}
	}
}
