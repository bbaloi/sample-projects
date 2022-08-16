package com.perpetual.logreport.control;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.rmi.*;
import java.io.*;
import java.text.*;
import javax.ejb.*;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.output.*;

import com.perpetual.util.EJBLoader;
//import com.perpetual.logreport.model.LogScheduleLocalHome;
//import com.perpetual.logreport.model.LogScheduleLocal;
import com.perpetual.logreport.model.LogScheduleHome;
import com.perpetual.logreport.model.LogSchedule;
import com.perpetual.logreport.model.LogScheduleUtil;
import com.perpetual.logreport.model.LogScheduleData;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.logreport.scheduler.ScheduleTask;
import com.perpetual.viewer.control.ejb.logstore.LogSession;
import com.perpetual.viewer.control.ejb.logstore.LogSessionHome;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
//import com.perpetual.log.LogSystem;
import com.perpetual.logserver.LogSystem;
import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.reports.LogReportsEngine;
import com.perpetual.reports.Header;
import com.perpetual.reports.RecordSet;
import com.perpetual.reports.Record;
import com.perpetual.reports.Field;
import com.perpetual.reports.ReportsEngine;
import com.perpetual.util.SchedulerMutex;
import com.perpetual.logreport.scheduler.Scheduler;
import com.perpetual.util.UIDGenerator;

/**

	A session bean that the scheduler invokes upon an outstanding schedule task.
	This is a session bean, so we'll perform the meat of doing log reports right here, as opposed to talking to CRUD's.

 * @ejb.bean name="LogReportTask" jndi-name="perpetual/LogReportTask" type="Stateless" view-type="remote"
   @ejb.transaction="NotSupported"
   @ejb.ejb-ref ejb-name="MessagePatternCRUD" view-type="remote" ref-name="perpetual/MessagePatternCRUD"
   @ejb.ejb-ref ejb-name="LogSchedule" view-type="remote" ref-name="LogSchedule"
   @ejb.interface extends="javax.ejb.EJBObject, com.perpetual.logreport.scheduler.ScheduleTask"
   @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class LogReportTaskBean implements SessionBean, ScheduleTask 
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger(LogReportTask.class);
    private static MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
	
    private static ReportsEngine lReportsEngine=null;
    //private LogScheduleLocalHome m_logScheduleHome;
     private LogScheduleHome m_logScheduleHome;
     private LogSession logSession=null;
     private static SchedulerMutex mutex = SchedulerMutex.getInstance();
       
    private String sPage=null;
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{
			//m_logScheduleHome = LogScheduleUtil.getLocalHome();
                        m_logScheduleHome = LogScheduleUtil.getHome();
		}
		catch (Throwable ex)
		{
			sLog.error("Couldn't initialize LogReportTask", ex);
			throw new CreateException("Couldn't initialize LogReportTask");
		}
                //lReportsEngine = (ReportsEngine) LogReportsEngine.getInstance();
	}
        /*
	public void execute(ScheduleData scheduleData, int referenceId) throws Exception	//, RemoteException is an Exception
	{
            
                //String sevList=new String("/"),facList=new String("/"),hostList=new String("/"),messageList=new String("/");
		//String xmlHeader = "<?xml version='1.0' encoding='iso-8859-1'?>";
                lReportsEngine = (ReportsEngine) LogReportsEngine.getInstance();
                Iterator it=null;
                sLog.info("Executing LogReportTask");
		LogScheduleData logScheduleData = m_logScheduleHome.findByPrimaryKey(new Integer(referenceId)).getData();
		LogSystem logSystem = LogSystem.getDefault();
		File outputDir = new File(logSystem.getParam("LogReportOutputDir"));
                sLog.debug("LogReportOutputDir:"+logSystem.getParam("LogReportOutputDir"));
		logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
		Collection lines=null;
                 Mutex mutex=null;
                
                try
		{
			Map map = new HashMap();

			List severityList = new ArrayList();
			for (int mask = logScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					severityList.add(Integer.toString(i));
                        if(severityList.size()>0)
                        {
                            map.put("Severity", severityList.toArray());
                            //it = severityList.iterator();
                             //while(it.hasNext())
                            //{                                
                              //  sevList+= (String) it.next()+"/";                                
                            //}
                        }

			List facilityList = new ArrayList();
			for (int mask = logScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					facilityList.add(Integer.toString(i));
                         if(facilityList.size()>0)
                         {
                            map.put("Facility", facilityList.toArray());
                            //it = facilityList.iterator();
                            //while(it.hasNext())
                            //{                                
                              //  facList+= (String) it.next() +"/";
                            //}
                         }
                        
                        List messagePatternList = new ArrayList(),str_messagePatternList = new ArrayList();
			for (int mask = logScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
                                {
                                        Integer mint = new Integer(i); 
					messagePatternList.add(mint);
                                        MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(mint.intValue());
                                        //str_messagePatternList.add(Integer.toString(i));
                                        str_messagePatternList.add(data.getName());
                                }
                        if(messagePatternList.size()>0)
                        {
                            sLog.debug("Adding messagepatterns to logstore query !");
                            map.put(Constants.MessagePattern, messagePatternList);
                            //it = messagePatternList.iterator();
                             //while(it.hasNext())
                            //{                       
                              //  Integer msg = (Integer) it.next();
                                ///MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(msg.intValue());
                                //str_messagePatternList+= data.getName() +"/";
                            //}
                             
                        }
                        
                        List m_hostList = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(logScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
				m_hostList.add(tokenizer.nextToken());
                        if(m_hostList.size()>0)
                        {
                            map.put("Host", m_hostList.toArray());
                            
                        }                        
                        
                        Date startDate = new Date(scheduleData.getStartDate()), endDate = new Date(scheduleData.getEndDate());
                        Date whenDate = new Date(scheduleData.getWhenDate());
			map.put("timeZone", TimeZone.getTimeZone(scheduleData.getTimeZone()));
			map.put("startTime", startDate);
			map.put("maxTime", endDate);
                       
                        Header header= new  Header(logScheduleData.getName(),startDate,endDate,whenDate,
                                            m_hostList,severityList,facilityList,str_messagePatternList);
   
                        
			Collection fieldNames = logSession.getFieldNames();                          
			int lineNumber = 0, linesPerPage = Integer.parseInt(logSystem.getParam("LogReportLinesPerPage"));
			
                       mutex = Mutex.getInstance();
                       sLog.debug("Waiting for lock");
                       mutex.getLock();
                       {
                           sLog.debug("Got lock -  executing query and doing reports !");
                           for (int page = 0; ; page++)
                            {

                                sPage = new Integer(page).toString();
                                  if(logSession==null)
                                  {
                                      sLog.debug("Lost LogSessionBean Handler, Should be getting another one !");
                                      //logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
		                  }
                                    lines = logSession.getLogRecords(map, linesPerPage, page);
                                    
                                    sLog.debug("Page"+page+"-# records found:"+ lines.size());                                

                                    if (lines.size() == 0)
                                    {
                                       sLog.debug("Finished Report- last Page:"+page);
                                       break;
                                    }

                                    //----------------writing page---------------------
                                    RecordSet recordSet = prepareRecordSet(lines,fieldNames);
                                    lReportsEngine.writePage(recordSet,header,page,linesPerPage);         
                                    //----------------------------------------------------                               

                            }                            
                            lines.clear();
                            lines=null;
                       }
                       mutex.releaseLock();
                       sLog.debug("releasedLock");
                    
		}
                catch(Throwable excp)
                {
                    sLog.error("Error in LogReportTask Bean trying to get records from LogSessionBean!");
                    mutex.releaseLock();
                    sLog.debug("releasedLock");
                    excp.printStackTrace();
                }
		finally
		{
                       //sLog.debug("Removing the logrecords for the page !");
                       //lines.clear();
                       //lines=null;
			//logSession.remove();
		}
	}*/
        //=================Multithreaded Version==============
        public void execute(ScheduleData scheduleData, int referenceId) throws Exception	//, RemoteException is an Exception
	{
            
                //String sevList=new String("/"),facList=new String("/"),hostList=new String("/"),messageList=new String("/");
		//String xmlHeader = "<?xml version='1.0' encoding='iso-8859-1'?>";
                lReportsEngine = (ReportsEngine) LogReportsEngine.getInstance();
                Iterator it=null;
                sLog.info("Executing LogReportTask");
		LogScheduleData logScheduleData = m_logScheduleHome.findByPrimaryKey(new Integer(referenceId)).getData();
		LogSystem logSystem = LogSystem.getDefault();
		File outputDir = new File(logSystem.getParam("LogReportOutputDir"));
                sLog.debug("LogReportOutputDir:"+logSystem.getParam("LogReportOutputDir"));
		logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
		Collection lines=null;
                double uid=0;
                
                try
		{
			Map map = new HashMap();

			List severityList = new ArrayList();
			for (int mask = logScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					severityList.add(Integer.toString(i));
                        if(severityList.size()>0)
                        {
                            map.put("Severity", severityList.toArray());
                           /* it = severityList.iterator();
                             while(it.hasNext())
                            {                                
                                sevList+= (String) it.next()+"/";                                
                            }*/
                        }

			List facilityList = new ArrayList();
			for (int mask = logScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					facilityList.add(Integer.toString(i));
                         if(facilityList.size()>0)
                         {
                            map.put("Facility", facilityList.toArray());
                            /*it = facilityList.iterator();
                            while(it.hasNext())
                            {                                
                                facList+= (String) it.next() +"/";
                            }*/
                         }
                        
                        List messagePatternList = new ArrayList(),str_messagePatternList = new ArrayList();
			for (int mask = logScheduleData.getMessagepatterns(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
                                {
                                        Integer mint = new Integer(i); 
					messagePatternList.add(mint);
                                        MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(mint.intValue());
                                        //str_messagePatternList.add(Integer.toString(i));
                                        str_messagePatternList.add(data.getName());
                                }
                        if(messagePatternList.size()>0)
                        {
                            sLog.debug("Adding messagepatterns to logstore query !");
                            map.put(Constants.MessagePattern, messagePatternList);
                            /*it = messagePatternList.iterator();
                             while(it.hasNext())
                            {                       
                                Integer msg = (Integer) it.next();
                                MessagePatternData data=messagePatternCRUD.retrieveByPrimaryKey(msg.intValue());
                                str_messagePatternList+= data.getName() +"/";
                            }*/
                             
                        }
                        
                        List m_hostList = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(logScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
				m_hostList.add(tokenizer.nextToken());
                        if(m_hostList.size()>0)
                        {
                            map.put("Host", m_hostList.toArray());
                            
                        }                        
                        
                        Date startDate = new Date(scheduleData.getStartDate()), endDate = new Date(scheduleData.getEndDate());
                        Date whenDate = new Date(scheduleData.getWhenDate());
			map.put("timeZone", TimeZone.getTimeZone(scheduleData.getTimeZone()));
			map.put("startTime", startDate);
			map.put("maxTime", endDate);
                       
                        Header header= new  Header(logScheduleData.getName(),startDate,endDate,whenDate,
                                            m_hostList,severityList,facilityList,str_messagePatternList);
   
                        
			Collection fieldNames = logSession.getFieldNames();                          
			int lineNumber = 0, linesPerPage = Integer.parseInt(logSystem.getParam("LogReportLinesPerPage"));
			
                       //mutex = Mutex.getInstance();
                       //sLog.debug("Waiting for lock");
                       //mutex.getLock();
                       //{
                           
                                //sLog.debug("Got lock -  executing query and doing reports !");
                                //if(!logSystem.isMultiThreaded())
                                if(logSystem.isMultiThreaded())
                                {                                    
                                    uid = UIDGenerator.getInstance().getUID();
                                    sLog.debug("UID:"+uid);
                                }
                                for (int page = 0; ; page++)
                                {

                                    sPage = new Integer(page).toString();
                                    if(logSession==null)
                                    {
                                      sLog.debug("Lost LogSessionBean Handler, Should be getting another one !");
                                      //logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
                                    }
                                    if(!logSystem.isMultiThreaded())
                                    {
                                        sLog.debug("Runing report scheduler in single-threaded mode !");
                                        lines = logSession.getLogRecords(map, linesPerPage, page);
                                    }
                                    else
                                    {
                                        sLog.debug("Runing report scheduler in multi-threaded mode !");                                       
                                        lines = logSession.getQueryRecordSet(map, linesPerPage, page,uid);  
                                        //lines = logSession.getQueryRecordSet(map, linesPerPage, page);  
                                    }
                                    
                                    sLog.debug("Page"+page+"-# records found:"+ lines.size());                                

                                    if (lines.size() == 0)
                                    {
                                       sLog.debug("Finished Report- last Page:"+page);
                                       break;
                                    }

                                    //----------------writing page---------------------
                                    RecordSet recordSet = prepareRecordSet(lines,fieldNames);
                                    lReportsEngine.writePage(recordSet,header,page,linesPerPage);         
                                    //----------------------------------------------------                               
                                
                            }                            
                                                        
                            lines.clear();
                            lines=null;
                       //}
                       //mutex.releaseLock();
                       //sLog.debug("releasedLock");
                             if(logSystem.isMultiThreaded())
                             {
                                 //sLog.debug("LogReportTaskBean got Lock");
                                  //mutex.getLock();
                                   Scheduler.removeJob();
                                   //mutex.releaseLock();
                                  //sLog.debug("releasedLock");
                             }
                    
		}
                catch(Throwable excp)
                {
                    sLog.error("Error in LogReportTask Bean trying to get records from LogSessionBean!");
                    //mutex.releaseLock();
                    if(logSystem.isMultiThreaded())
                        Scheduler.removeJob();
                    //sLog.debug("releasedLock");
                    excp.printStackTrace();
                }
		finally
		{
                       //sLog.debug("Removing the logrecords for the page !");
                       //lines.clear();
                       //lines=null;
			//logSession.remove();
		}
	}
        
        //=====================================================
        private RecordSet prepareRecordSet(Collection lines,Collection fieldNames)
        {
            sLog.debug("In prepareRecordSet()-GOT:"+lines.size()+" records to process!");
            RecordSet recordSet = new RecordSet();
            Record record=null;
            Field field=null;
            
            Iterator i = lines.iterator();
            while(i.hasNext())
	    {
                record = new Record();
                Collection line = (Collection)i.next();
                                
                for (Iterator column = line.iterator(), fieldName = fieldNames.iterator(); fieldName.hasNext(); )
                {
                        String name = fieldName.next().toString(), value = String.valueOf(column.next());
                        record.addField(new Field(name,value));
                }
               
               recordSet.addRecord(record);
            }
            return recordSet;
        }
}


