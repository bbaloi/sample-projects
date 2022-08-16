package com.perpetual.logreport.control;

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
//import com.perpetual.logreport.model.LogScheduleLocalHome;
//import com.perpetual.logreport.model.LogScheduleLocal;
import com.perpetual.logreport.model.LogScheduleHome;
import com.perpetual.logreport.model.LogSchedule;
import com.perpetual.logreport.model.LogScheduleUtil;
import com.perpetual.logreport.model.LogScheduleData;
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
 * @ejb:bean name="LogScheduleCRUD" jndi-name="perpetual/LogScheduleCRUD" type="Stateless" view-type="remote" 
   @ejb.transaction="NotSupported"
   @ejb.ejb-ref ejb-name="Schedule" view-type="remote" ref-name="Schedule"
   @ejb.ejb-ref ejb-name="LogSchedule" view-type="remote" ref-name="LogSchedule"
  @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class LogScheduleCRUDBean implements SessionBean
{
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{
			//m_logScheduleHome = LogScheduleUtil.getLocalHome();
			//m_scheduleHome = ScheduleUtil.getLocalHome();
                        m_logScheduleHome = LogScheduleUtil.getHome();
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
	public LogScheduleData retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		return m_logScheduleHome.findByPrimaryKey(new Integer(id)).getData();
	}

	/** @ejb.interface-method */
	public Collection retrieveAll() throws RemoteException, FinderException
	{
		List list = new ArrayList();
		for (Iterator i = m_logScheduleHome.findAll().iterator(); i.hasNext(); )
		{
			//list.add(((LogScheduleLocal)i.next()).getData());
                        list.add(((LogSchedule)i.next()).getData());
		}
		return list;
	}

	/** @ejb.interface-method */
	public void deleteLogSchedule(int id) throws RemoteException, FinderException, RemoveException
	{
		//LogScheduleLocal logSchedule = m_logScheduleHome.findByPrimaryKey(new Integer(id));
		LogSchedule logSchedule = m_logScheduleHome.findByPrimaryKey(new Integer(id));
		m_scheduleHome.remove(new Integer(logSchedule.getScheduleId()));
		logSchedule.remove();
	}

	/** @ejb.interface-method */
	public void setLogSchedule(ScheduleData scheduleData, LogScheduleData logScheduleData) throws RemoteException, CreateException, FinderException
	{
		sLog.info("!! " + scheduleData);
		sLog.info("!! " + logScheduleData);

		if (logScheduleData.getId() == null)
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
			logScheduleData.setScheduleId(currentScheduleData.getId().intValue());
			LogScheduleData currentLogScheduleData = m_logScheduleHome.create(logScheduleData).getData();

			// update schedule with a reference to the log schedule
			//
			currentSchedule.setReferenceId(currentLogScheduleData.getId().intValue());
		}
		else
		{
			sLog.debug("Changing existing schedule");
			// existing schedule, just update
			//
			//LogScheduleLocal logSchedule = m_logScheduleHome.findByPrimaryKey(logScheduleData.getId());
			//ScheduleLocal schedule = m_scheduleHome.findByPrimaryKey(new Integer(logSchedule.getScheduleId()));
			LogSchedule logSchedule = m_logScheduleHome.findByPrimaryKey(logScheduleData.getId());
			Schedule schedule = m_scheduleHome.findByPrimaryKey(new Integer(logSchedule.getScheduleId()));
			
			// get data, so we can poke around with it and change just what we want to
			//
			LogScheduleData currentLogScheduleData = logSchedule.getData();
			ScheduleData currentScheduleData = schedule.getData();

			// transfer the parts we can change
			//
			currentLogScheduleData.setName(logScheduleData.getName());
			currentLogScheduleData.setHosts(logScheduleData.getHosts());
			currentLogScheduleData.setSeverities(logScheduleData.getSeverities());
			currentLogScheduleData.setFacilities(logScheduleData.getFacilities());
                        currentLogScheduleData.setMessagepatterns(logScheduleData.getMessagepatterns());

			currentScheduleData.setTimeZone(scheduleData.getTimeZone());
			currentScheduleData.setStartDate(scheduleData.getStartDate());
			currentScheduleData.setEndDate(scheduleData.getEndDate());
			currentScheduleData.setWhenDate(scheduleData.getWhenDate());
			currentScheduleData.setRepeat(scheduleData.getRepeat());
			currentScheduleData.setIncDate(scheduleData.getIncDate());

			currentScheduleData.setEnabled(scheduleData.getEnabled());

			// hand data back to entity bean
			//
			logSchedule.setData(currentLogScheduleData);
			schedule.setData(currentScheduleData);
		}
	}

	//private LogScheduleLocalHome m_logScheduleHome;
	//private ScheduleLocalHome m_scheduleHome;
        private LogScheduleHome m_logScheduleHome;
	private ScheduleHome m_scheduleHome;

	private static Logger sLog = Logger.getLogger(LogScheduleCRUDBean.class);
}


