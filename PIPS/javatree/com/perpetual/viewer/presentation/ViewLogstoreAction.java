package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.text.*;
import javax.servlet.http.*;
import javax.rmi.*;
import javax.servlet.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.FacilityVO;
/*import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogSystem;*/
import com.perpetual.logserver.LogRecordFormat;
import com.perpetual.logserver.LogDatabase;
import com.perpetual.logserver.LogFilter;
import com.perpetual.logserver.LogRecord;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogSystem;
import com.perpetual.util.Constants;

import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.util.EJBLoader;
import com.perpetual.util.Constants;
import com.perpetual.util.UIDGenerator;
import java.util.LinkedList;
import com.perpetual.logserver.util.InboxFactory;
import com.perpetual.logserver.util.InboxListener;

public class ViewLogstoreAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewLogstoreAction.class );
        private static MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
	
        //public static int page=0;
	// careful !  LogScheduleAction relies on this one too !
	//
	protected void init(ViewLogstoreActionForm viewLogStoreForm, HttpServletRequest request)
	{
		Locale locale = getLocale(request);
		m_timeZone = TimeZone.getTimeZone(viewLogStoreForm.getTimeZone());
		m_fullDateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
		m_fullDateFormat.setTimeZone(m_timeZone);
		m_compactDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		m_compactDateFormat.setTimeZone(m_timeZone);

		{
			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, viewLogStoreForm.getStartYear());
			cal.set(Calendar.MONTH, viewLogStoreForm.getStartMonth());
			cal.set(Calendar.DAY_OF_MONTH, viewLogStoreForm.getStartDay());
			cal.set(Calendar.HOUR_OF_DAY, viewLogStoreForm.getStartHour());
			cal.set(Calendar.MINUTE, viewLogStoreForm.getStartMinute());
			m_startDate = cal.getTime();
		}
		{
			Calendar cal = Calendar.getInstance(m_timeZone);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.YEAR, viewLogStoreForm.getEndYear());
			cal.set(Calendar.MONTH, viewLogStoreForm.getEndMonth());
			cal.set(Calendar.DAY_OF_MONTH, viewLogStoreForm.getEndDay());
			cal.set(Calendar.HOUR_OF_DAY, viewLogStoreForm.getEndHour());
			cal.set(Calendar.MINUTE, viewLogStoreForm.getEndMinute());
			m_endDate = cal.getTime();
		}
		sLog.debug("startDate=" + m_startDate + " endDate=" + m_endDate);
		{
			Map map = new LinkedHashMap();                        
			for (Iterator i = userProfile.getSeverityList().iterator(); i.hasNext(); )
			{
				SeverityVO vo = (SeverityVO)i.next();
				map.put(new Integer(vo.getId()), vo.getName());
			}
			m_severityCollection = new ArrayList();
			String[] severities = viewLogStoreForm.getSeverities();                        
			if (severities != null)
			{
                            
                             if(Constants.NO_SELECTION_STR.equals(severities[0]))
                             {
                                 int sctr=0;
                                 if(severities.length>1)
                                 {
                                     sLog.debug("list contains no_selection +!");
                                      filterSeverities = new String [severities.length-1];                                      
                                      for (int i = 0; i < severities.length; i++)
                                      {
                                        String sid = severities[i];
                                        sLog.debug("selected sev:"+sid);
                                        if(sid.equals(Constants.NO_SELECTION_STR))
                                            sLog.debug("No selection made !");
                                        else
                                        {
                                            filterSeverities[sctr]=severities[i];sctr++;
                                            m_severityCollection.add(map.get(new Integer(sid)));                                            
                                        }
                                    }
                                     
                                 }
                                 else
                                 {
                                     sLog.debug(" no Severity selection was made!");
                                 }
                             }
                             else
                             {
                                  filterSeverities = new String [severities.length];
                                  for (int i = 0; i < severities.length; i++)
                                  {
                                    String sid = severities[i];
                                    sLog.debug("selected sev:"+sid);
                                      filterSeverities[i]=severities[i];
                                      m_severityCollection.add(map.get(new Integer(sid)));                                            
                                    }
                             }   
                        }
                    }
		{
			Map map = new LinkedHashMap();
			for (Iterator i = userProfile.getFacilityList().iterator(); i.hasNext(); )
			{
				FacilityVO vo = (FacilityVO)i.next();
				map.put(new Integer(vo.getId()), vo.getName());
			}
			m_facilityCollection = new ArrayList();
			String[] facilities = viewLogStoreForm.getFacilities();
			if (facilities != null)
			{
                            
                            if(Constants.NO_SELECTION_STR.equals(facilities[0]))
                            {
                                int fctr=0;
                                if(facilities.length>1)
                                {
                                    sLog.debug("list contains no_selection +!");
                                     filterFacilities = new String [facilities.length-1];                                     
                                    for (int i = 0; i < facilities.length; i++)
                                    {
                                        String fid = facilities[i];
                                        sLog.debug("selected fac:"+fid);
                                        if(fid.equals(Constants.NO_SELECTION_STR))
                                            sLog.debug("No selection made !");
                                        else
                                        {
                                            filterFacilities[fctr]=facilities[i];fctr++;
                                            m_facilityCollection.add(map.get(new Integer(fid)));
                                        }
                                    }                  
                                }
                                else
                                {
                                    sLog.debug(" no Facility selection was made!");
                                }
                            }
                            else
                            {
                                filterFacilities = new String [facilities.length];
                                for (int i = 0; i < facilities.length; i++)
				{
                                    String fid = facilities[i];
                                    sLog.debug("selected fac:"+fid);
                                        filterFacilities[i]=facilities[i];
                                    	m_facilityCollection.add(map.get(new Integer(fid)));                                    
				}
                            }       
			}
		}
		{
			String[] hostList = viewLogStoreForm.getHostList();
                        m_hostCollection = new ArrayList();
			//m_hostCollection = hostList != null ? Arrays.asList(hostList) : new ArrayList();
                        
                        if (hostList != null)
			{                            
                            if(Constants.NO_SELECTION_STR.equals(hostList[0]))  
                            {
                                int hctr=0;
                                if(hostList.length>1)
                                {
                                    sLog.debug("list contains no_selection +!");
                                    filterHosts= new String [hostList.length-1];  
                                    for (int i = 0; i < hostList.length; i++)
                                    {                                    
                                        String hid = hostList[i];
                                        sLog.debug("selected host:"+hid);
                                        if(hid.equals(Constants.NO_SELECTION_STR))
                                            sLog.debug("No selection made !");
                                        else
                                        {
                                            filterHosts[hctr]=hostList[i];hctr++;
                                            m_hostCollection.add(hid);
                                        }
                                    }                        
                                }
                                else
                                {
                                    sLog.debug(" no Host selection was made!");
                                }
                            }
                            else
                            {
                                filterHosts= new String [hostList.length];
                                for (int i = 0; i < hostList.length; i++)
				{                                    
                                    String hid = hostList[i];
                                    sLog.debug("selected host:"+hid);
                                        filterHosts[i]=hostList[i];
                                        m_hostCollection.add(hid);
                                }              
                            }                               
                              
			}                        
		}
	}

	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		ViewLogstoreActionForm viewLogStoreForm = (ViewLogstoreActionForm)form;
		sLog.debug(viewLogStoreForm.toString());
		try
		{
			HttpSession session = request.getSession();
			MessageResources res = getResources(request);
			Map filterMap = new HashMap();

			init(viewLogStoreForm, request);

			request.setAttribute("startTime", m_fullDateFormat.format(m_startDate));
			filterMap.put("startTime", m_startDate);

			request.setAttribute("endTime", m_fullDateFormat.format(m_endDate));
			filterMap.put("maxTime", m_endDate);

			request.setAttribute("severityCollection", m_severityCollection);
                        filterMap.put("Severity", filterSeverities);
			//filterMap.put("Severity", viewLogStoreForm.getSeverities());
                        

			request.setAttribute("facilityCollection", m_facilityCollection);
			filterMap.put("Facility", filterFacilities);
                        //filterMap.put("Facility", viewLogStoreForm.getFacilities());

			request.setAttribute("hostCollection", m_hostCollection);
                        filterMap.put("Host", filterHosts);		// must be String[]
                        //filterMap.put("Host", viewLogStoreForm.getHostList());		// must be String[]

			{
				LogSystem logSystem = LogSystem.getDefault();
				LogDatabase logDatabase = logSystem.getLogDatabase();
				LogRecordFormat logRecordFormat = logDatabase.getLogRecordFormat();
				Locale locale = getLocale(request);

				Collection col = new ArrayList();
				for (Iterator i = logRecordFormat.iterateFieldNames(); i.hasNext(); )
				{
					String fieldName = res.getMessage(locale, "LogView.Field." + i.next());
					col.add(fieldName);
					if (!i.hasNext())
						request.setAttribute("lastFieldName", fieldName);
				}
                                request.setAttribute("fieldNameCollection", col);
                                
                                 {//-----------------------new addtion - message aptterns----------
                                    sLog.debug("--retrieving message pattern list--");
                                    String [] msgPatternList = viewLogStoreForm.getMessagePatternList();
                                    ArrayList patternNameList = new ArrayList();
                                    if (msgPatternList != null)
                                    {                                        
                                         sLog.debug("Patterns Selected to filter on:");
                                         if(msgPatternList.length>1 ||
                                            (msgPatternList.length==1 && 
                                                (!Constants.NO_SELECTION_STR.equals(msgPatternList[0]))))                                             
                                         {
                                            ArrayList patternList = new ArrayList();
                                            for(int i=0;i<msgPatternList.length;i++)
                                            {
                                               sLog.debug("pattern:"+msgPatternList[i]);
                                               if(Constants.NO_SELECTION_STR.equals(msgPatternList[i]))
                                                        sLog.debug("No selection field !");
                                               else
                                               {
                                                    Integer patternId = new Integer((String) msgPatternList[i]);
                                                    patternNameList.add(getMessagePatternName(patternId));
                                                    sLog.debug("pattern:"+patternId.toString());
                                                    patternList.add(patternId);
                                               } 
                                        
                                            }
                                            filterMap.put(Constants.MessagePattern,patternList);
                                         }
                                         
                                    }
                                    request.setAttribute("messagePatternCollection", patternNameList);
                                }//-------------------------------------------------

				try
				{
					int page = viewLogStoreForm.getPage();
					int maxLines = Integer.parseInt(LogSystem.getDefault().getParam("ViewLogLinesPerPage")), minLine = page * maxLines, maxLine = minLine + maxLines;		// maxLine is really the max line + 1

					CursorSession cursorSession = (CursorSession)session.getAttribute("CursorSession");
					if (cursorSession == null || minLine == 0)
					{
						cursorSession = new CursorSession(logDatabase, new LogFilter(filterMap), m_compactDateFormat);
                                                cursorSession.setPage(page);
						request.getSession().setAttribute("CursorSession", cursorSession);
					}
					request.setAttribute("rowCollection", cursorSession.getRows(minLine, maxLine));
					Map map = new LinkedHashMap();
					map.put(pageLabel(res.getMessage(locale, "LogView.First"), 0, page), "0");
					if (page > 0)
						map.put(res.getMessage(locale, "LogView.Prev"), Integer.toString(page - 1));
					map.put(res.getMessage(locale, "LogView.Next"), Integer.toString(page + 1));
					int left = page - 5, right = page + 5;
					if (left < 1) left = 1;
					for (int i = 1; i < left && i < 5; i++)
						map.put(pageLabel(Integer.toString(i), i, page), Integer.toString(i));
					for (int i = 5; i < left && i < 100; i += 5)
						map.put(pageLabel(Integer.toString(i), i, page), Integer.toString(i));
					for (int i = left; i < right; i++)
						map.put(pageLabel(Integer.toString(i), i, page), Integer.toString(i));
					for (int i = right / 5 * 5; i < 100; i += 5)
						map.put(pageLabel(Integer.toString(i), i, page), Integer.toString(i));
					request.setAttribute("pageMap", map);
				}
				finally
				{
				}
			}

			return mapping.findForward("success");
		}
		catch(Exception ex)
		{
			sLog.error("problem", ex);
			return mapping.findForward("failure");
		}
	}
        private String getMessagePatternName(Integer pPatternId) throws javax.ejb.FinderException,java.rmi.RemoteException
        {
            MessagePatternData data = messagePatternCRUD.retrieveByPrimaryKey(pPatternId.intValue());
            return data.getName();
        }

        private String pageLabel(String label, int page, int currentPage)
	{
		StringBuffer buffer = new StringBuffer();
		if (page == currentPage)
			buffer.append("<strong><font color=red>");		// change to style sheets later
		buffer.append(label);
		if (page == currentPage)
			buffer.append("</font></strong>");
		return buffer.toString();
	}
        
	public static class CursorSession implements HttpSessionBindingListener		// can't be non static - classloader problems
	{
               private double uid=0; 
               private static int pageSize=0;
               private int page=0;
               private InboxListener inboxListener=null;
                           
		public CursorSession(LogDatabase logDatabase, LogFilter logFilter, DateFormat dateFormat)
		{
			m_logDatabase = logDatabase;
			m_logFilter = logFilter;
			m_dateFormat = dateFormat;
                        uid = UIDGenerator.getInstance().getUID();
                        pageSize = m_logDatabase.getLinesPerPage();
                        inboxListener = new InboxListener(InboxFactory.getInstance().createInbox(uid));
                    
		}
                public void setPage(int pPage)
                {
                    page=pPage;
                }
		public void valueBound(HttpSessionBindingEvent event)
		{
		}
		public void valueUnbound(HttpSessionBindingEvent event)
		{
			try
			{
				if (m_cursor != null)
				{
					System.out.println("Cursor is closing");		// can't sLog ?  something to do with some classloader, this inner class must be static !
					m_cursor.close();
				}
			}
			catch (Throwable ex)
			{
				ex.printStackTrace();
			}
		}

		/**
		 @return collection of rows, null if no rows match at all, empty collection if no further matches if not on page 0
		 */
                public Collection getRows(int minLine, int maxLine) throws Exception
		{
                    Collection col=null;
                    Collection col1=null;
                                    
                    sLog.debug("getting rows !");
			if (m_cursor == null || m_line > minLine)
			{
				m_line = 0;
				if (m_cursor != null)
					m_cursor.close();
                                sLog.debug("getting rows from db!");
                                if(m_logDatabase.isMultithreaded())
                                {
                                    sLog.debug("Getting Cursor in multi-threaded mode !");
                                    //m_cursor = m_logDatabase.submitQueryRequest(uid,m_logFilter);
                                    m_logDatabase.submitQueryRequest(uid,m_logFilter,inboxListener);
                                    sLog.debug("Waiting for Cursor !");
                                    m_cursor = (Cursor)inboxListener.getItem();
                                    /*synchronized(resultsInbox)
                                    {
                                        resultsInbox.wait();    
                                        m_cursor = (Cursor)resultsInbox.removeFirst();
                                    }*/                     
                                    sLog.debug("Got Cursor");
                                    
                                }
                                else
                                {
                                    sLog.debug("Getting Cursor in single-threaded mode !");
                                    m_cursor = m_logDatabase.retrieveLogRecords(m_logFilter);
                                }
			}
                        if(m_logDatabase.isMultithreaded())
                         {
                             sLog.debug("Running query in multi-threaded mode !");
                              //col = m_logDatabase.getQueryResults(uid,m_cursor,page,pageSize); 
                              m_logDatabase.getQueryResults(uid,m_cursor,page,pageSize,inboxListener); 
                              sLog.debug("Waiting for ResultSet #"+page);
                              col=(Collection) inboxListener.getItem();
                              //resultsInbox.wait();                
                              //col = (Collection)resultsInbox.removeFirst();                               
                                sLog.debug("Got Result Set !");
                                if(col.size()==0)
                                {
                                    sLog.debug("Last Record Set, removing inbox");
                                    inboxListener=null;
                                    InboxFactory.getInstance().removeInbox(uid);
                                }
                         }
                        
                            if(col==null)
                            {
                                col = new ArrayList();
                                sLog.debug("Building displayable values !");
                                for (LogRecord logRecord; m_line < maxLine && (logRecord = m_cursor.nextLogRecord()) != null; m_line++)
                                {
                                    if (m_line >= minLine)
                                    {
					Collection col2 = new ArrayList();
					for (Iterator i = logRecord.iterateFields(); i.hasNext(); )
					{
						LogRecordFormat.Field field = (LogRecordFormat.Field)i.next();
						if (field instanceof LogRecordFormat.TimeField)
							col2.add(m_dateFormat.format(((LogRecordFormat.TimeField)field).getTime()));
						else if (field instanceof LogRecordFormat.DateField)
							col2.add(m_dateFormat.format(((LogRecordFormat.DateField)field).getDate()));
						else
							col2.add(field);
                                               
					}
					col.add(col2);
                                        
                                    }
                                }
                            }
                        
                       if (minLine == 0 && col.isEmpty()) col = null;
			
			m_cursor.close();		// TEMP HACK TO PREVENT LONG SESSION TIME OUTS FROM ACCUMULATING TOO MANY CURSORS, AND THEREFORE TOO MANY FILES
			m_cursor = null;
                        
                        
			return col;
		}
                /*
		public Collection getRows(int minLine, int maxLine) throws Exception
		{
                    sLog.debug("getting rows !");
			if (m_cursor == null || m_line > minLine)
			{
				m_line = 0;
				if (m_cursor != null)
					m_cursor.close();
                                sLog.debug("getting rows from db!");
				m_cursor = m_logDatabase.retrieveLogRecords(m_logFilter);
			}
			Collection col = new ArrayList();
			for (LogRecord logRecord; m_line < maxLine && (logRecord = m_cursor.nextLogRecord()) != null; m_line++)
			{
				if (m_line >= minLine)
				{
					Collection col2 = new ArrayList();
					for (Iterator i = logRecord.iterateFields(); i.hasNext(); )
					{
						LogRecordFormat.Field field = (LogRecordFormat.Field)i.next();
						if (field instanceof LogRecordFormat.TimeField)
							col2.add(m_dateFormat.format(((LogRecordFormat.TimeField)field).getTime()));
						else if (field instanceof LogRecordFormat.DateField)
							col2.add(m_dateFormat.format(((LogRecordFormat.DateField)field).getDate()));
						else
							col2.add(field);
					}
					col.add(col2);
				}
			}
			if (minLine == 0 && col.isEmpty()) col = null;

			m_cursor.close();		// TEMP HACK TO PREVENT LONG SESSION TIME OUTS FROM ACCUMULATING TOO MANY CURSORS, AND THEREFORE TOO MANY FILES
			m_cursor = null;

			return col;
		}*/

		private LogDatabase m_logDatabase;
		private LogFilter m_logFilter;
		private Cursor m_cursor;
		private int m_line;
		private DateFormat m_dateFormat;
	}

	private DateFormat m_fullDateFormat, m_compactDateFormat;
	private int m_line;
	protected TimeZone m_timeZone;
	protected Date m_startDate, m_endDate;
	protected Collection m_severityCollection, m_facilityCollection, m_hostCollection;
        protected String [] filterSeverities,filterFacilities,filterHosts,filterMessagePatterns;
}



