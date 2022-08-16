package com.perpetual.viewer.control.ejb.summary;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */
import java.util.*;
import java.rmi.*;
import javax.rmi.*;
import java.io.*;
import java.text.*;
import javax.ejb.*;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.output.*;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.ejb.SummaryScheduleHome;
import com.perpetual.viewer.model.ejb.SummarySchedule;
import com.perpetual.viewer.model.ejb.SummaryScheduleUtil;
import com.perpetual.viewer.model.ejb.SummaryScheduleData;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.logreport.scheduler.ScheduleTask;
import com.perpetual.logserver.LogSystem;
import com.perpetual.util.ServiceLocator;
import com.perpetual.util.PerpetualC2Logger;

import com.perpetual.util.Constants;
import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.query.SummaryFilter;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;
import com.perpetual.viewer.control.SummaryLogicDescriptor;
import com.perpetual.viewer.control.query.SummaryRecord;
import com.perpetual.viewer.model.vo.SummaryRecordVO;
import java.sql.Timestamp;
import com.perpetual.rp.util.RPConstants;
import java.util.LinkedHashMap;

import com.perpetual.viewer.control.query.SummaryCursor;
import com.perpetual.viewer.control.query.SummaryQueryEngine;

import com.perpetual.viewer.presentation.ViewGeneralSummaryAction;

import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;

import com.perpetual.reports.SummaryReportsEngine;
import com.perpetual.reports.Header;
import com.perpetual.reports.RecordSet;
import com.perpetual.reports.Record;
import com.perpetual.reports.Field;
import com.perpetual.reports.ReportsEngine;
import com.perpetual.viewer.util.ViewerGlobals;

import com.perpetual.util.SchedulerMutex;
import com.perpetual.logreport.scheduler.Scheduler;
import com.perpetual.util.SchedulerMutex;

/**

	A session bean that the scheduler invokes upon an outstanding schedule task.
	This is a session bean, so we'll perform the meat of doing log reports right here, as opposed to talking to CRUD's.

 * @ejb.bean name="SummaryReportTask" jndi-name="perpetual/SummaryReportTask" type="Stateless" view-type="remote"
 * @ejb.transaction="NotSupported"
   @ejb.ejb-ref ejb-name="MessagePatternCRUD" view-type="remote" ref-name="perpetual/MessagePatternCRUD"
   @ejb.interface extends="javax.ejb.EJBObject, com.perpetual.logreport.scheduler.ScheduleTask"
 * @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class SummaryReportTaskBean implements SessionBean, ScheduleTask
{
        private boolean bSeveritiesAnd;
        private boolean bFacilitiesAnd;
        private boolean bFacilitiesOrSeverities;
        private boolean bFacilitiesOrMessages;
        private boolean bSeveritiesOrMessages;
        private SummaryCursor summaryList,viewSummaryList=null;
        private SummaryCRUDHome summaryHome=null;
        private SummaryCRUD queryEngine=null;
        private LogSystem logSystem=null;
        private static Collection severityCollectionNames=new ArrayList();
        private static Collection facilityCollectionNames=new ArrayList();
               
        private File outputDir=null;
        private SummaryScheduleData summaryScheduleData=null;
        private ScheduleData tmpScheduleData=null;
        private Date startDate=null,endDate=null,whenDate=null;        
        private static ReportsEngine lReportsEngine=null;
        private String xmlHeader = "<?xml version='1.0' encoding='iso-8859-1'?>";   
        private Header lHeader=null;
        private static SchedulerMutex mutex = SchedulerMutex.getInstance();
        
        private static MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);

        
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{
			m_summaryScheduleHome = SummaryScheduleUtil.getHome();
		}
		catch (Throwable ex)
		{
			sLog.error("Couldn't initialize SummaryReportTask", ex);
			throw new CreateException("Couldn't initialize SummaryReportTask");
		}
               
	}

	public void execute(ScheduleData scheduleData, int referenceId) throws Exception	//, RemoteException is an Exception
	{
              sLog.info("Executing SummaryReportTask");
               lReportsEngine = (ReportsEngine) SummaryReportsEngine.getInstance();
                tmpScheduleData=scheduleData;
		summaryScheduleData = m_summaryScheduleHome.findByPrimaryKey(new Integer(referenceId)).getData();
		logSystem = LogSystem.getDefault();
		outputDir = new File(logSystem.getParam("LogReportOutputDir"));
                
                sLog.debug("About to invoke the Summary Query Engine !");
                
                try
                {
                    HashMap map = new HashMap();
                    List sevList = new ArrayList();   
                    List strSevList = new ArrayList();
                    for (int mask = summaryScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
                                {
                                        Integer sint = new Integer(i);
					sevList.add(sint);
                                        strSevList.add(sint.toString());
                                        sLog.debug("sev:"+sint);
                                }
                        String [] sevArray = new String [sevList.size()] ;
                        for(int s=0;s<sevList.size();s++)
                        {
                            sevArray[s] = ((Integer) sevList.get(s)).toString();
                        }
                        map.put(Constants.SeverityList, mapSeverities(sevArray));
                                                
			List facList = new ArrayList();
                        List strFacList = new ArrayList();
			for (int mask = summaryScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
                                {
                                        Integer fint = new Integer(i);
					facList.add(fint);
                                        strFacList.add(fint.toString());
                                        sLog.debug("fac:"+fint);
                                }
			String [] facArray = new String [facList.size()];
                        for(int f=0;f<facList.size();f++)
                        {
                            facArray[f] = ((Integer) facList.get(f)).toString();
                        }                        
                        map.put(Constants.FacilityList,mapFacilities(facArray));
                        //----------message paterns--------------
                        List mpList = new ArrayList();
                        List str_mpList = new ArrayList();
			for (int mask = summaryScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
                                {
                                        Integer mint = new Integer(i);
					mpList.add(mint);
                                        MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(mint.intValue());
                                        //str_mpList.add(Integer.toString(i));
                                        str_mpList.add(data.getName());
                                        sLog.debug("message pattern:"+mint);
                                }
                        if(mpList.size()>0)
                            map.put(Constants.MessagePattern,mpList);
                                                   
                        //---------------------end message patterns-------------------
                        
			List hostList = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(summaryScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
                        {
                                String _host = (String)tokenizer.nextToken();
				hostList.add(_host);
                                sLog.debug("host:"+_host);
                        }
                        String [] hostArray = new String [hostList.size()];
                        for(int i=0;i<hostList.size();i++)
                        {
                            hostArray[i]= (String) hostList.get(i);
                        }
			map.put(Constants.HostList, hostArray);
                        
                        startDate = new Date(scheduleData.getStartDate());
                        endDate = new Date(scheduleData.getEndDate());
                        whenDate = new Date(scheduleData.getWhenDate());
			map.put("timeZone", TimeZone.getTimeZone(scheduleData.getTimeZone()));
			//sLog.debug("timeZone:",TimeZone.getTimeZone(scheduleData.getTimeZone()));
                        map.put(Constants.StartDate, startDate);
                        sLog.debug("startTime:"+startDate);
			map.put(Constants.EndDate, endDate);
                        sLog.debug("endDate:"+endDate);
                        
                         //---------------------------------------------------
                          lHeader= new  Header(summaryScheduleData.getName(),startDate,endDate,whenDate,
                                            hostList,strSevList,strFacList,str_mpList);   
                        //---------------------------------------------------			
                        
                          //get the logic descriptors
                        int severitiesAnd = summaryScheduleData.getAndSevs();
                        if(severitiesAnd ==1)
                            bSeveritiesAnd = true;
                        else
                            bSeveritiesAnd=false;
                        int facilitiesAnd = summaryScheduleData.getAndFacs();
                        if(facilitiesAnd==1)
                            bFacilitiesAnd = true;
                        else
                            bFacilitiesAnd=false;                        
                        int facilitiesOrSeverities = summaryScheduleData.getFacsOrSevs();
                        if(facilitiesOrSeverities==1)
                            bFacilitiesOrSeverities= true;
                        else
                            bFacilitiesOrSeverities=false;  
                         int facilitiesOrMessages = summaryScheduleData.getFacsOrMessages();
                        if(facilitiesOrMessages==1)
                            bFacilitiesOrMessages= true;
                        else
                            bFacilitiesOrMessages=false;  
                          int severitiesOrMessages = summaryScheduleData.getSevsOrMessages();
                        if(severitiesOrMessages==1)
                            bSeveritiesOrMessages= true;
                        else
                            bSeveritiesOrMessages=false;  
                        
                        SummaryLogicDescriptor descriptor = new SummaryLogicDescriptor(bFacilitiesAnd,bSeveritiesAnd,bFacilitiesOrSeverities,bFacilitiesOrMessages,bSeveritiesOrMessages);
                        map.put(Constants.SummaryLogicDescriptor,descriptor);
                       
                        SummaryFilter summaryFilter = new SummaryFilter(map);
                        
                        try
                        {
                            Object homeObj = ServiceLocator.getServiceLocatorInstance().findHome(Constants.jndiName_SummaryCRUD);
                            sLog.debug("got Object!");
                            summaryHome = (SummaryCRUDHome) PortableRemoteObject.narrow(homeObj,SummaryCRUDHome.class); 
                            sLog.debug("Summary Home:"+summaryHome.toString());
                            sLog.debug("SummaryCRUDHome:"+summaryHome.toString());   
                            queryEngine = (SummaryCRUD)summaryHome.create();
                            sLog.debug("Summary QUery Engine:"+queryEngine.toString());
                        }           
                        catch(Exception t)
                        {
                            sLog.error("Couldn't get properties !");
                            t.printStackTrace();
                        }
                       
                            sLog.debug("making query - ");
                            HashMap paramMap = new HashMap();
                            String query = queryEngine.constructQuery(summaryFilter.getMap(),paramMap); 
                            sLog.debug("Constructed Query !, executing...");
                            //Collection summaryList = queryEngine.executeQuery(query,paramMap); 
                            summaryList = queryEngine.executeQuery(query,paramMap);
                            sLog.debug("# of records found:"+summaryList.size());
            
                            viewSummaryList = prepareSummaryList(summaryList,sevList,facList);
                                            
                            writeReport(viewSummaryList);
                            
                             if(logSystem.isMultiThreaded())
                             {
                                 //sLog.debug("LogReportTaskBean got Lock");
                                  //mutex.getLock();
                                   Scheduler.removeJob();
                                   //mutex.releaseLock();
                                 // sLog.debug("releasedLock");
                             }
                            
                                              
                }
                catch(Exception excp)
                {
                    String errMsg = "Messed up real bad in doing the SummaryQuery !!!";
                    if(logSystem.isMultiThreaded())
                        Scheduler.removeJob();
                    sLog.error(errMsg);
                    excp.printStackTrace();
                }
                
                
	}
        private void writeReport(SummaryCursor pSummary)
        {
            String sevList=new String("/"),facList=new String("/"),hostList=new String("/"),messageList=new String("/");
	    Iterator it=null;
            sLog.debug("About to write SummaryReport !!!");
            //SummaryReportWriter.writeReport(viewSummaryList);
             int numPages =pSummary.getNumPages();
             sLog.debug("Num pages:"+numPages);
             String sPage=null;
             
             try
             {                 
                 
                 Map map = new HashMap();
                 
                 List list = new ArrayList();
			for (int mask = summaryScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
                        if(list.size()>0)
                        {
                            map.put("Severity", list.toArray());
                            it = list.iterator();
                            while(it.hasNext())
                            {                                
                                sevList+= (String) it.next()+"/";                                
                            }
                        }

			list = new ArrayList();
			for (int mask = summaryScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
                         if(list.size()>0)
                         {
                            map.put("Facility", list.toArray());
                            it = list.iterator();
                            while(it.hasNext())
                            {                                
                                facList+= (String) it.next() +"/";
                            }
                         }
                        
                        list = new ArrayList();
			for (int mask = summaryScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(new Integer(i));
                        if(list.size()>0)
                        {
                            map.put(Constants.MessagePattern, list);
                            it = list.iterator();
                            Collection msgPatterns = messagePatternCRUD.retrieveAll();
                            Iterator msgPatternIt = msgPatterns.iterator();
                             while(it.hasNext())
                            {                       
                                Integer msg = (Integer) it.next();
                                messageList+= getMsgPatternName(msgPatterns,msg) +"/";                                
                                // MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(msg.intValue());
                                //messageList+= data.getName() +"/";
                            }
                        }
                        
                        list = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(summaryScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
				list.add(tokenizer.nextToken());
                        if(list.size()>0)
                        {
                            map.put("Host", list.toArray());
                            it = list.iterator();
                            while(it.hasNext())
                            {                                
                                hostList += (String) it.next() +"/";
                            }
                        }                                         
                 
                 
                for (int page = 0; page < numPages; page++)
                {
                    sPage = new Integer(page).toString();
                    Collection rowsInPage = pSummary.getPage(page);
                    sLog.debug("Page:"+page+" has "+rowsInPage.size()+" rows");
                    sLog.debug("Writing page...");
                    
                    //----------------writing page---------------------
                     RecordSet recordSet = prepareRecordSet(rowsInPage);
                     lReportsEngine.writePage(recordSet,lHeader,page,pSummary.getPageSize());         
                    //----------------------------------------------------                               
                               
                    
                            if(logSystem.isMultiThreaded())
                             {
                                 //sLog.debug("LogReportTaskBean got Lock");
                                  //mutex.getLock();
                                   Scheduler.removeJob();
                                   //mutex.releaseLock();
                                  //sLog.debug("releasedLock");
                             }
                    
                }
                      
             }
             catch(Exception excp)
             {
                 String errMsg="Error trying to write query result to report file !";
                  //mutex.releaseLock();
                  //sLog.debug("releasedLock");
                 sLog.error(errMsg,excp);
                 excp.printStackTrace();
             }
                         
        }
        private String getMsgPatternName(Collection msgPatterns,Integer msg)
        {
            String patternName=null;
            Iterator it = msgPatterns.iterator();
            while(it.hasNext())
            {
                MessagePatternData data = (MessagePatternData) it.next();
                if(msg.equals(data.getId()))
                {
                    patternName= data.getName();
                    break;
                }
                    
            }
            return patternName;
            
        }
        private RecordSet prepareRecordSet(Collection lines) throws Exception
        {
            sLog.debug("In prepareRecordSet()-GOT:"+lines.size()+" records to process!");
            RecordSet recordSet = new RecordSet();
            Record record=null;
            Field field=null;
                        
            Iterator i = lines.iterator();
            while(i.hasNext())
	    {                               
                record = new Record();
                SummaryRecordVO summaryRecord = (SummaryRecordVO)i.next();
                 String hostName=summaryRecord.getHostName();
                 field = new Field(ViewerGlobals.HostName,hostName);
                 record.addField(field);
                
                 String domainName= summaryRecord.getDomainName();
                 field = new Field(ViewerGlobals.DomainName,domainName);
                 record.addField(field);
                
                String startDate=summaryRecord.getStartDate();
                field = new Field(ViewerGlobals.StartDate,startDate);
                record.addField(field);
                               
                String endDate=summaryRecord.getEndDate();
                field = new Field(ViewerGlobals.EndDate,endDate);
                record.addField(field);
                               
                String patternName = summaryRecord.getMessagePattern();
                field = new Field(Constants.MessagePattern,patternName);
                 record.addField(field);
                       
                 Collection facilityList = summaryRecord.getFacilityList();
                 Iterator fit = facilityList.iterator();
                 Iterator facNamesIt=facilityCollectionNames.iterator(); 
                 
                 while(fit.hasNext())
                 {
                     
                     field = new Field((String) facNamesIt.next(),(String)fit.next());
                     record.addField(field);
                      
                 }
                //============================Severities=====================
                 Collection severityList = summaryRecord.getSeverityList();
                 Iterator sit = severityList.iterator();
                 Iterator sevNamesIt=severityCollectionNames.iterator(); 
                 
                 while(sit.hasNext())
                 {
                     
                     field = new Field((String) sevNamesIt.next(),(String)sit.next());
                     record.addField(field);
                      
                 }
                                                
               recordSet.addRecord(record);
            }
            return recordSet;
        }
        private static String getMessagePatternName(Integer pPatternId) throws javax.ejb.FinderException,java.rmi.RemoteException
        {
            MessagePatternData data = messagePatternCRUD.retrieveByPrimaryKey(pPatternId.intValue());
            return data.getName();
        }
        
        private  String [] mapSeverities(String [] pSelectedSeverities) 
        { 
         if(pSelectedSeverities!=null)
         {
                int listlen = pSelectedSeverities.length;
                String [] mappedSevs = new String [listlen];
            
            for(int i=0;i<listlen;i++)
            {
                String sev = pSelectedSeverities[i];
                
                if(sev.equals("0"))
                      mappedSevs[i]=ViewerGlobals.sev0;
                if(sev.equals("1"))
                           mappedSevs[i]=ViewerGlobals.sev1;                          
                if(sev.equals("2"))
                           mappedSevs[i]=ViewerGlobals.sev2;
                if(sev.equals("3"))
                           mappedSevs[i]=ViewerGlobals.sev3;
                if(sev.equals("4"))
                           mappedSevs[i]=ViewerGlobals.sev4;
                if(sev.equals("5"))
                           mappedSevs[i]=ViewerGlobals.sev5;
                if(sev.equals("6"))
                           mappedSevs[i]=ViewerGlobals.sev6;  
                 if(sev.equals("7"))
                           mappedSevs[i]=ViewerGlobals.sev7;  
            }
                return mappedSevs;
         }
            return null;
            
        }
     private  String [] mapFacilities(String [] pSelectedFacilities)
        {
          if(pSelectedFacilities!=null)
          {
            int listlen = pSelectedFacilities.length;
            String [] mappedFacs = new String [listlen];
           
            for(int i=0;i<listlen;i++)
            {
                String fac = pSelectedFacilities[i];
                
                if(fac.equals("0"))
                      mappedFacs[i]=ViewerGlobals.fac0;
                if(fac.equals("1"))
                      mappedFacs[i]=ViewerGlobals.fac1;
                if(fac.equals("2"))
                      mappedFacs[i]=ViewerGlobals.fac2;
                if(fac.equals("3"))
                      mappedFacs[i]=ViewerGlobals.fac3;
                if(fac.equals("4"))
                      mappedFacs[i]=ViewerGlobals.fac4;
                if(fac.equals("5"))
                      mappedFacs[i]=ViewerGlobals.fac5;
                if(fac.equals("6"))
                      mappedFacs[i]=ViewerGlobals.fac6;
                if(fac.equals("7"))
                      mappedFacs[i]=ViewerGlobals.fac7;
                if(fac.equals("8"))
                      mappedFacs[i]=ViewerGlobals.fac8;
                if(fac.equals("9"))
                      mappedFacs[i]=ViewerGlobals.fac9;
                if(fac.equals("10"))
                      mappedFacs[i]=ViewerGlobals.fac10;
                if(fac.equals("11"))
                      mappedFacs[i]=ViewerGlobals.fac11;
                if(fac.equals("12"))
                      mappedFacs[i]=ViewerGlobals.fac12;
                if(fac.equals("13"))
                      mappedFacs[i]=ViewerGlobals.fac13;
                if(fac.equals("14"))
                      mappedFacs[i]=ViewerGlobals.fac14;
                if(fac.equals("15"))
                      mappedFacs[i]=ViewerGlobals.fac15;
                if(fac.equals("16"))
                      mappedFacs[i]=ViewerGlobals.fac16;
                if(fac.equals("17"))
                      mappedFacs[i]=ViewerGlobals.fac17;
                if(fac.equals("18"))
                      mappedFacs[i]=ViewerGlobals.fac18;
                if(fac.equals("19"))
                      mappedFacs[i]=ViewerGlobals.fac19;
                if(fac.equals("20"))
                      mappedFacs[i]=ViewerGlobals.fac20;
                if(fac.equals("21"))
                      mappedFacs[i]=ViewerGlobals.fac21;
                if(fac.equals("22"))
                      mappedFacs[i]=ViewerGlobals.fac22;
                if(fac.equals("23"))
                      mappedFacs[i]=ViewerGlobals.fac23;
            }
            return mappedFacs;
          }
           return null;
        }
      public static SummaryCursor prepareSummaryList(SummaryCursor pSummaryList,Collection pSeverityList,Collection pFacilityList)
        {
            SummaryCursor viewCursor=null;
            severityCollectionNames.clear();
            facilityCollectionNames.clear();
            ArrayList viewSummaryList = new ArrayList();
            String patternName=null;
            
            Iterator it = pSummaryList.iterator();
            while(it.hasNext())
            {
                 
                 Collection facList = new ArrayList();
                 Collection sevList = new ArrayList();
            
                SummaryRecord record = (SummaryRecord)it.next();
                Map summaryMap=record.getMap();
                String hostName = (String) summaryMap.get(ViewerGlobals.HostName);
                sLog.debug("HostName:"+hostName);
                String domainName= (String)summaryMap.get(ViewerGlobals.DomainName);
                sLog.debug("DomainName:"+domainName);
                String vendorName= (String)summaryMap.get(ViewerGlobals.VendorName);
                sLog.debug("VendorName:"+vendorName);
                String deviceType=(String)summaryMap.get(ViewerGlobals.DeviceType);
                sLog.debug("deviceType:"+deviceType);
                String startDate=((Timestamp)summaryMap.get(ViewerGlobals.StartDate)).toString();
                sLog.debug("StartDate:"+startDate);
                String endDate=((Timestamp)summaryMap.get(ViewerGlobals.EndDate)).toString();
                sLog.debug("EndDate:"+endDate);   
                Integer patternId= (Integer)summaryMap.get(ViewerGlobals.MessagePatternId);
                try
                {
                    patternName= getMessagePatternName(patternId);
                    sLog.debug("Paternname:"+patternName); 
                }
                catch(Exception excp)
                {
                    String msg="Could not get message pattern name !";
                    sLog.error(msg);
                    //excp.printStackTrace();
                    patternName="";
                }
                  
                                    
                Iterator fit = pFacilityList.iterator();
                while(fit.hasNext())
                {
                    String facVal = ((Integer)fit.next()).toString();
                    
                    if(facVal.equals(RPConstants.Kernel))
                    {
                        facilityCollectionNames.add("Kernel");
                        Integer fac0=(Integer)summaryMap.get(ViewerGlobals.fac0);
                        if(fac0!=null)
                        {     
                        sLog.debug("Got Kernel/Fac0:"+fac0.toString());
                        facList.add(fac0.toString());   
                        }
                    }
                    if(facVal.equals(RPConstants.User))
                    {
                        facilityCollectionNames.add("User");
                         Integer fac1= (Integer)summaryMap.get(ViewerGlobals.fac1);
                        if(fac1!=null)
                        {
                            sLog.debug("Got User/Fac1:"+fac1.toString());
                            facList.add(fac1.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Mail))
                    {
                        facilityCollectionNames.add("Mail");
                         Integer fac2= (Integer)summaryMap.get(ViewerGlobals.fac2);
                        if(fac2!=null)
                        {
                            sLog.debug("Got Mail/fac2:"+fac2.toString());
                            facList.add(fac2.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Daemon))
                    {
                        facilityCollectionNames.add("Daemon");
                        Integer fac3= (Integer)summaryMap.get(ViewerGlobals.fac3);
                        if(fac3!=null)
                        {
                            sLog.debug("Got Daemon/Fac3:"+fac3.toString());
                            facList.add(fac3.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Security))
                    {                         
                        facilityCollectionNames.add("Security");
                        Integer fac4= (Integer)summaryMap.get(ViewerGlobals.fac4);
                        if(fac4!=null)
                        {
                            sLog.debug("Got Security/Fac4:"+fac4.toString());
                            facList.add(fac4.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.Syslog))
                    { 
                        facilityCollectionNames.add("Syslog");
                        Integer fac5= (Integer)summaryMap.get(ViewerGlobals.fac5);
                        if(fac5!=null)
                        {
                            sLog.debug("Got Syslog/Fac5:"+fac5.toString());
                            facList.add(fac5.toString());
                        }
                     }
                     if(facVal.equals(RPConstants.Lpr))
                    { 
                        facilityCollectionNames.add("Lpr");
                        Integer fac6= (Integer)summaryMap.get(ViewerGlobals.fac6);
                        if(fac6!=null)
                        {
                            sLog.debug("Got Lpr/Fac6:"+fac6.toString());
                            facList.add(fac6.toString());
                        }
                     }
                     if(facVal.equals(RPConstants.News))
                    {
                        facilityCollectionNames.add("News");
                         Integer fac7= (Integer)summaryMap.get(ViewerGlobals.fac7);
                        if(fac7!=null)
                        {
                            sLog.debug("Got News/Fac7:"+fac7.toString());
                            facList.add(fac7.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.UUCP))
                    {
                        facilityCollectionNames.add("UUCP");
                         Integer fac8= (Integer)summaryMap.get(ViewerGlobals.fac8);
                         if(fac8!=null)
                        {
                            sLog.debug("Got UUCP/Fac8:"+fac8.toString());
                            facList.add(fac8.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Crond))
                    {
                        facilityCollectionNames.add("Crond");
                        Integer fac9= (Integer)summaryMap.get(ViewerGlobals.fac9);
                        if(fac9!=null)
                        {
                            sLog.debug("Got Crond/Fac9:"+fac9.toString());
                            facList.add(fac9.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Authority))
                    {
                        facilityCollectionNames.add("Authority");
                         Integer fac10= (Integer)summaryMap.get(ViewerGlobals.fac10);
                         if(fac10!=null)
                         {
                            sLog.debug("Got Authority/Fac10:"+fac10.toString());
                            facList.add(fac10.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.FTP))
                    {
                        facilityCollectionNames.add("FTP");
                        Integer fac11= (Integer)summaryMap.get(ViewerGlobals.fac11);
                        if(fac11!=null)
                        {
                            sLog.debug("Got FTP/Fac11:"+fac11.toString());
                            facList.add(fac11.toString());
                        }
                    }
                     if(facVal.equals(RPConstants.NTP))
                    {
                        facilityCollectionNames.add("NTP");
                         Integer fac12= (Integer)summaryMap.get(ViewerGlobals.fac12);
                        if(fac12!=null)
                        {
                            sLog.debug("Got NTP/Fac12:"+fac12.toString());
                            facList.add(fac12.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.Audit))
                    {
                        facilityCollectionNames.add("Audit");
                        Integer fac13= (Integer)summaryMap.get(ViewerGlobals.fac13);
                        if(fac13!=null)
                        {
                            sLog.debug("Got Audit/Fac13:"+fac13.toString());
                            facList.add(fac13.toString());
                        }
                    }
                     if(facVal.equals(RPConstants.Alert))
                    {
                        facilityCollectionNames.add("Alert");
                        Integer fac14= (Integer)summaryMap.get(ViewerGlobals.fac14);
                        if(fac14!=null)
                        {
                            sLog.debug("Got Alert/Fac14:"+fac14.toString());
                            facList.add(fac14.toString());
                        }
                     }
                    if(facVal.equals(RPConstants._Crond))
                    {
                        facilityCollectionNames.add("Crond2");
                        Integer fac15= (Integer)summaryMap.get(ViewerGlobals.fac15);
                        if(fac15!=null)
                        {
                            sLog.debug("Got Crond2/Fac15:"+fac15.toString());
                            facList.add(fac15.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local0))
                    {
                        facilityCollectionNames.add("Local0");
                        Integer fac16= (Integer)summaryMap.get(ViewerGlobals.fac16);
                        if(fac16!=null)
                        {
                            sLog.debug("Got Local0/Fac16:"+fac16.toString());
                            facList.add(fac16.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local1))
                    {
                        facilityCollectionNames.add("Local1");
                        Integer fac17= (Integer)summaryMap.get(ViewerGlobals.fac17);
                        if(fac17!=null)
                        {
                            sLog.debug("Got Local1/Fac17:"+fac17.toString());
                            facList.add(fac17.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local2))
                    {
                        facilityCollectionNames.add("Local2");
                        Integer fac18= (Integer)summaryMap.get(ViewerGlobals.fac18);
                        if(fac18!=null)
                        {
                            sLog.debug("Got Local2/Fac18:"+fac18.toString());
                            facList.add(fac18.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local3))
                    {
                        facilityCollectionNames.add("Local3");
                        Integer fac19= (Integer)summaryMap.get(ViewerGlobals.fac19);
                        if(fac19!=null)
                        {
                            sLog.debug("Got Local3/Fac19:"+fac19.toString());
                            facList.add(fac19.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local4))
                    {
                        facilityCollectionNames.add("Local4");
                        Integer fac20= (Integer)summaryMap.get(ViewerGlobals.fac20);
                        if(fac20!=null)
                        {
                            sLog.debug("Got Fac20:"+fac20.toString());
                            facList.add(fac20.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local5))
                    {
                        facilityCollectionNames.add("Local5");
                        Integer fac21= (Integer)summaryMap.get(ViewerGlobals.fac21);
                        if(fac21!=null)
                        {
                            sLog.debug("GotLocal5/ Fac21:"+fac21.toString());
                            facList.add(fac21.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local6))
                    {
                        facilityCollectionNames.add("Local6");
                        Integer fac22= (Integer)summaryMap.get(ViewerGlobals.fac22);
                        if(fac22!=null)
                        {
                            sLog.debug("Got Local6/Fac22:"+fac22.toString());
                            facList.add(fac22.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local7))
                    {
                        facilityCollectionNames.add("Local7");
                        Integer fac23= (Integer)summaryMap.get(ViewerGlobals.fac23);
                        if(fac23!=null)
                        {
                             sLog.debug("Got Local7/Fac23:"+fac23.toString());
                            facList.add(fac23.toString());
                        }
                    }         
                }
                
                Iterator sit = pSeverityList.iterator();
                while(sit.hasNext())
                {
                     String sevVal = ((Integer)sit.next()).toString();
                    
                    if(sevVal.equals(RPConstants.Emergency))
                    {
                        severityCollectionNames.add("Emergency");
                        Integer sev0= (Integer)summaryMap.get(ViewerGlobals.sev0);
                        if(sev0!=null)
                        {
                            sLog.debug("Got Emergency/Sev0:"+sev0.toString());
                            sevList.add(sev0.toString());
                        }
                    }
                     if(sevVal.equals(RPConstants.Severity_Alert))
                    {
                        severityCollectionNames.add("Alert");
                        Integer sev1= (Integer)summaryMap.get(ViewerGlobals.sev1);
                        if(sev1!=null)
                        {
                            sLog.debug("Got Alert/Sev1:"+sev1.toString());
                            sevList.add(sev1.toString());
                         }
                     }
                      if(sevVal.equals(RPConstants.Critical))
                    {
                        severityCollectionNames.add("Critical");
                        Integer sev2= (Integer)summaryMap.get(ViewerGlobals.sev2);
                        if(sev2!=null)
                        {
                         sLog.debug("Got Critical/Sev2:"+sev2.toString());
                            sevList.add(sev2.toString());
                        }
                      }
                     if(sevVal.equals(RPConstants.Error))
                    {
                        severityCollectionNames.add("Error");
                        Integer sev3= (Integer)summaryMap.get(ViewerGlobals.sev3);
                        if(sev3!=null)
                        {
                            sLog.debug("Got Error/Sev3:"+sev3.toString());
                            sevList.add(sev3.toString());
                         }
                     }
                      if(sevVal.equals(RPConstants.Warning))
                    {
                        severityCollectionNames.add("Warning");
                        Integer sev4= (Integer)summaryMap.get(ViewerGlobals.sev4);
                        if(sev4!=null)
                        {
                            sLog.debug("Got Warning/Sev4:"+sev4.toString());
                            sevList.add(sev4.toString());
                        }
                      }
                      if(sevVal.equals(RPConstants.Notice))
                    {
                        severityCollectionNames.add("Notice");
                        Integer sev5= (Integer)summaryMap.get(ViewerGlobals.sev5);
                        if(sev5!=null)
                        {
                            sLog.debug("Got Notice/Sev5:"+sev5.toString());
                            sevList.add(sev5.toString());
                        }
                      }
                     if(sevVal.equals(RPConstants.Info))
                    {
                        severityCollectionNames.add("Info");
                        Integer sev6= (Integer)summaryMap.get(ViewerGlobals.sev6);
                        if(sev6!=null)
                        {
                            sLog.debug("Got Info/Sev6:"+sev6.toString());
                            sevList.add(sev6.toString());
                        }
                     }
                     if(sevVal.equals(RPConstants.Debug))
                    {
                        severityCollectionNames.add("Debug");
                        Integer sev7= (Integer)summaryMap.get(ViewerGlobals.sev7);
                        if(sev7!=null)
                        {
                            sLog.debug("Got Debug/Sev7:"+sev7.toString());
                            sevList.add(sev7.toString());
                        }
                     }                
                }
                
                sLog.debug("creating - SummaryRecordVO");
             
                //SummaryRecordVO vo = new SummaryRecordVO(hostName,domainName,vendorName,deviceType,
                  //                              startDate,endDate,facList,sevList);
                SummaryRecordVO vo = new SummaryRecordVO(hostName,domainName,patternName,
                                                startDate,endDate,facList,sevList);
    
    
                     viewSummaryList.add(vo);
            }
            
            viewCursor = new SummaryCursor(SummaryQueryEngine.getPageSize(),viewSummaryList); 
            
            return viewCursor;
            
        }
	private SummaryScheduleHome m_summaryScheduleHome;

	private static Logger sLog = Logger.getLogger(SummaryReportTaskBean.class);
}


