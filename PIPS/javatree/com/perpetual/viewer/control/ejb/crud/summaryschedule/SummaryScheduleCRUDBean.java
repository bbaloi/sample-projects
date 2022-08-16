package com.perpetual.viewer.control.ejb.crud.summaryschedule;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.ejb.SummaryScheduleHome;
import com.perpetual.viewer.model.ejb.SummarySchedule;
import com.perpetual.viewer.model.ejb.SummaryScheduleUtil;
import com.perpetual.viewer.model.ejb.SummaryScheduleData;
//import com.perpetual.logreport.model.ScheduleLocalHome;
//import com.perpetual.logreport.model.ScheduleLocal;
import com.perpetual.logreport.model.ScheduleHome;
import com.perpetual.logreport.model.Schedule;
import com.perpetual.logreport.model.ScheduleUtil;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.logreport.model.TestLocalHome;
import com.perpetual.logreport.model.TestLocal;
import com.perpetual.logreport.model.TestData;


/**
 * @ejb:bean name="SummaryScheduleCRUD" jndi-name="perpetual/SummaryScheduleCRUD" type="Stateless" view-type="remote"
   @ejb.transaction="NotSupported"
   @ejb.ejb-ref ejb-name="SummarySchedule" view-type="remote" ref-name="SummarySchedule"
   @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class SummaryScheduleCRUDBean implements SessionBean
{
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{
			m_summaryScheduleHome = SummaryScheduleUtil.getHome();
			//m_scheduleHome = ScheduleUtil.getLocalHome();
                        m_scheduleHome = ScheduleUtil.getHome();
//			m_logScheduleHome = (LogScheduleLocalHome)EJBLoader.getEJBLocalHome("LogScheduleLocal");	//LogScheduleUtil.getLocalHome();
		}
		catch (Throwable ex)
		{
			sLog.error("Couldn't initialize LogScheduleCRUDBean", ex);
			throw new CreateException("Couldn't initialize LogScheduleCRUDBean");
		}
	}

	/** @ejb.interface-method */
	public Object test(Object something) throws RemoteException, CreateException
	{
		sLog.info("test being performed, arg=" + something);
		TestLocalHome testLocalHome = (TestLocalHome)EJBLoader.getEJBLocalHome("TestLocal");	//LogScheduleUtil.getLocalHome();
System.out.println("!!!!!!! " + testLocalHome);
		sLog.info(testLocalHome.toString());
		testLocalHome.create(new TestData(new Integer(123123), "test"));

//		m_logScheduleHome.create(new LogScheduleData(new Integer(-1), "test", 0, "hosts", 1, 2));
		return null;
	}

	/** @ejb.interface-method */
	public SummaryScheduleData retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		return m_summaryScheduleHome.findByPrimaryKey(new Integer(id)).getData();
	}

	/** @ejb.interface-method */
	public Collection retrieveAll() throws RemoteException, FinderException
	{
		List list = new ArrayList();
		for (Iterator i = m_summaryScheduleHome.findAll().iterator(); i.hasNext(); )
		{
			list.add(((SummarySchedule)i.next()).getData());
		}
		return list;
	}

	/** @ejb.interface-method */
	public void deleteSummarySchedule(int id) throws RemoteException, FinderException, RemoveException
	{
		SummarySchedule summarySchedule = m_summaryScheduleHome.findByPrimaryKey(new Integer(id));
		m_scheduleHome.remove(new Integer(summarySchedule.getScheduleId()));
		summarySchedule.remove();
	}

	/** @ejb.interface-method */
	public void setLogSchedule(ScheduleData scheduleData, SummaryScheduleData summaryScheduleData) throws RemoteException, CreateException, FinderException
	{
		sLog.info("!! " + scheduleData);
		sLog.info("!! " + summaryScheduleData);

		if (summaryScheduleData.getId() == null)
		{
			sLog.debug("Adding new schedule");
			// new schedule
			//
			// new base schedule first
			//ScheduleLocal currentSchedule = m_scheduleHome.create(scheduleData);
			Schedule currentSchedule = m_scheduleHome.create(scheduleData);
			ScheduleData currentScheduleData = currentSchedule.getData();

			// prepare log schedule, reference to schedule
			//
			summaryScheduleData.setScheduleId(currentScheduleData.getId().intValue());
			SummaryScheduleData currentSummaryScheduleData = m_summaryScheduleHome.create(summaryScheduleData).getData();

			// update schedule with a reference to the log schedule
			//
			currentSchedule.setReferenceId(currentSummaryScheduleData.getId().intValue());
		}
		else
		{
			sLog.debug("Changing existing schedule");
			// existing schedule, just update
			//
			SummarySchedule summarySchedule = m_summaryScheduleHome.findByPrimaryKey(summaryScheduleData.getId());
			//ScheduleLocal schedule = m_scheduleHome.findByPrimaryKey(new Integer(summarySchedule.getScheduleId()));
			Schedule schedule = m_scheduleHome.findByPrimaryKey(new Integer(summarySchedule.getScheduleId()));
			
			// get data, so we can poke around with it and change just what we want to
			//
			SummaryScheduleData currentSummaryScheduleData = summarySchedule.getData();
			ScheduleData currentScheduleData = schedule.getData();

			// transfer the parts we can change
			//
			currentSummaryScheduleData.setName(summaryScheduleData.getName());
			currentSummaryScheduleData.setHosts(summaryScheduleData.getHosts());
			currentSummaryScheduleData.setSeverities(summaryScheduleData.getSeverities());
			currentSummaryScheduleData.setFacilities(summaryScheduleData.getFacilities());
                        currentSummaryScheduleData.setMessagepatterns(summaryScheduleData.getMessagepatterns());
                        currentSummaryScheduleData.setAndSevs(summaryScheduleData.getAndSevs());
                        currentSummaryScheduleData.setAndFacs(summaryScheduleData.getAndFacs());
                        currentSummaryScheduleData.setFacsOrSevs(summaryScheduleData.getFacsOrSevs());
                        currentSummaryScheduleData.setFacsOrMessages(summaryScheduleData.getFacsOrMessages());
                        currentSummaryScheduleData.setSevsOrMessages(summaryScheduleData.getSevsOrMessages());
                        
			currentScheduleData.setTimeZone(scheduleData.getTimeZone());
			currentScheduleData.setStartDate(scheduleData.getStartDate());
			currentScheduleData.setEndDate(scheduleData.getEndDate());
			currentScheduleData.setWhenDate(scheduleData.getWhenDate());
			currentScheduleData.setRepeat(scheduleData.getRepeat());
			currentScheduleData.setIncDate(scheduleData.getIncDate());

			currentScheduleData.setEnabled(scheduleData.getEnabled());

			// hand data back to entity bean
			//
			summarySchedule.setData(currentSummaryScheduleData);
			schedule.setData(currentScheduleData);
		}
	}

	private SummaryScheduleHome m_summaryScheduleHome;
	private ScheduleHome m_scheduleHome;
        //private ScheduleLocalHome m_scheduleHome;

	private static Logger sLog = Logger.getLogger(SummaryScheduleCRUDBean.class);
}


