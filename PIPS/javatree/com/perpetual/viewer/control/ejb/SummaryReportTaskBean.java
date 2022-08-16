package com.perpetual.viewer.control.ejb;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms*
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.rmi.*;
import javax.rmi.*;
import java.io.*;
import java.text.*;
import javax.ejb.*;

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
import com.perpetual.log.LogSystem;
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

/**

	A session bean that the scheduler invokes upon an outstanding schedule task.
	This is a session bean, so we'll perform the meat of doing log reports right here, as opposed to talking to CRUD's.

 * @ejb.bean name="SummaryReportTask" jndi-name="perpetual/SummaryReportTask" type="Stateless" view-type="remote"
 * @ejb.interface extends="javax.ejb.EJBObject, com.perpetual.logreport.scheduler.ScheduleTask"
 */
public abstract class SummaryReportTaskBean implements SessionBean, ScheduleTask
{
     private boolean bSeveritiesAnd;
        private boolean bFacilitiesAnd;
        private boolean bFacilitiesAndSeverities;
         private SummaryCursor viewSummaryList=null;
         private SummaryCRUDHome summaryHome=null;
        private SummaryCRUD queryEngine=null;
               
        private ArrayList facilityCollectionVal=new ArrayList(); 
        private ArrayList severityCollectionVal=new ArrayList();
        
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
		SummaryScheduleData summaryScheduleData = m_summaryScheduleHome.findByPrimaryKey(new Integer(referenceId)).getData();
		LogSystem logSystem = LogSystem.getDefault();
		File outputDir = new File(logSystem.getParam("LogReportOutputDir"));
                
                sLog.debug("About to invoke the Summary Query Engine !");
                
                try
                {
                    HashMap map = new HashMap();
                    List list = new ArrayList();
                    
                    for (int mask = summaryScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
			map.put("Severity", list.toArray());

			list = new ArrayList();
			for (int mask = summaryScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
			map.put("Facility", list.toArray());

			list = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(summaryScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
				list.add(tokenizer.nextToken());
			map.put("Host", list.toArray());

			Date startDate = new Date(scheduleData.getStartDate()), endDate = new Date(scheduleData.getEndDate());
			map.put("timeZone", TimeZone.getTimeZone(scheduleData.getTimeZone()));
			map.put("startTime", startDate);
			map.put("maxTime", endDate);
                        
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
                        int facilitiesAndSeverities = summaryScheduleData.getFacsOrSevs();
                        if(facilitiesAndSeverities==1)
                            bFacilitiesAndSeverities= true;
                        else
                            bFacilitiesAndSeverities=false;                        
                        
                        SummaryLogicDescriptor descriptor = new SummaryLogicDescriptor(bFacilitiesAnd,bSeveritiesAnd,bFacilitiesAndSeverities);
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
                            SummaryCursor summaryList = queryEngine.executeQuery(query,paramMap);
                            sLog.debug("# of records found:"+summaryList.size());
            
                            //Collection viewSummaryList = prepareSummaryList(summaryList,severityCollectionVal,facilityCollectionVal);
                           viewSummaryList = ViewGeneralSummaryAction.prepareSummaryList(summaryList,severityCollectionVal,facilityCollectionVal);
                        
                        /*   
                        //build page map
                        Map map = new LinkedHashMap();
                        int numPages = viewSummaryList.getNumPages();
                        sLog.debug("Num pages:"+numPages);
                        for (int i = 0; i < numPages; i++)
                            map.put(pageLabel(Integer.toString(i), i, page), new Integer(i).toString());
			request.setAttribute("pageMap",map);    
                        
                        Collection rowsInPage = viewSummaryList.getPage(page);
                        sLog.debug("Page:"+page+" has "+rowsInPage.size()+" rows");
                        request.setAttribute("rowList",rowsInPage);
                        */
                        
                        
                }
                catch(Exception excp)
                {
                    String errMsg = "Messed up real bad in doing the SummaryQuery !!!";
                    sLog.error(errMsg);
                    excp.printStackTrace();
                }
                
                /*
		LogSession logSession = (LogSession)EJBLoader.createEJBObject(LogSessionHome.JNDI_NAME, LogSessionHome.class);
		try
		{
			Map map = new HashMap();

			List list = new ArrayList();
			for (int mask = logScheduleData.getSeverities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
			map.put("Severity", list.toArray());

			list = new ArrayList();
			for (int mask = logScheduleData.getFacilities(), i = 0; mask != 0; i++, mask >>= 1)
				if ((mask & 1) != 0)
					list.add(Integer.toString(i));
			map.put("Facility", list.toArray());

			list = new ArrayList();
			for (StringTokenizer tokenizer = new StringTokenizer(logScheduleData.getHosts()); tokenizer.hasMoreTokens(); )
				list.add(tokenizer.nextToken());
			map.put("Host", list.toArray());

			Date startDate = new Date(scheduleData.getStartDate()), endDate = new Date(scheduleData.getEndDate());
			map.put("timeZone", TimeZone.getTimeZone(scheduleData.getTimeZone()));
			map.put("startTime", startDate);
			map.put("maxTime", endDate);

			Collection fieldNames = logSession.getFieldNames();

			int lineNumber = 0, linesPerPage = Integer.parseInt(logSystem.getParam("LogReportLinesPerPage"));
			for (int page = 0; ; page++)
			{
				Collection lines = logSession.getLogRecords(map, linesPerPage, page);
				if (lines.size() == 0)
					break;

				Element pageElem = new Element("page");
				pageElem.setAttribute("page", Integer.toString(page));
				pageElem.setAttribute("firstRow", Integer.toString(lineNumber));
				for (Iterator i = lines.iterator(); i.hasNext(); lineNumber++)
				{
					Collection line = (Collection)i.next();

					Element lineElem = new Element("row");
					for (Iterator column = line.iterator(), fieldName = fieldNames.iterator(); fieldName.hasNext(); )
					{
						String name = fieldName.next().toString(), value = String.valueOf(column.next());
						if (fieldName.hasNext())
							lineElem.setAttribute(name, value);
						else
						{
							// last one is the message and needs to be in a cdata
							//
							lineElem.addContent(new CDATA(value));
						}
					}
					pageElem.addContent(lineElem);
				}
				pageElem.setAttribute("lastRow", Integer.toString(lineNumber - 1));

				File dir = new File(outputDir, MessageFormat.format(logSystem.getParam("LogReportDirFormat"),
						new Object[] { logScheduleData.getName(), new Date(scheduleData.getWhenDate()), startDate, endDate }));
				dir.mkdirs();
				File file = new File(dir, MessageFormat.format(logSystem.getParam("LogReportFileFormat"),
						new Object[] { logScheduleData.getName(), new Date(scheduleData.getWhenDate()), startDate, endDate }));
				OutputStream os = new FileOutputStream(file);
				try
				{
					XMLOutputter xmlOutputter = new XMLOutputter("\011", true);
					xmlOutputter.setLineSeparator("\n");
					//		xmlOutputter.setTextNormalize(false);
					xmlOutputter.setTextTrim(true);
					xmlOutputter.output(pageElem, os);
				}
				finally
				{
					os.close();
				}
			}
		}
		finally
		{
			logSession.remove();
		}
                */
         }
        
	private SummaryScheduleHome m_summaryScheduleHome;

	private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryReportTaskBean.class );
}


