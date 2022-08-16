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
//import com.perpetual.logreport.model.ScheduleLocalHome;
//import com.perpetual.logreport.model.ScheduleLocal;
import com.perpetual.logreport.model.ScheduleHome;
import com.perpetual.logreport.model.Schedule;
import com.perpetual.logreport.model.ScheduleUtil;
import com.perpetual.logreport.model.ScheduleData;


/**
 * @ejb:bean name="ScheduleCRUD" jndi-name="perpetual/ScheduleCRUD" type="Stateless" view-type="remote" 
   @ejb.transaction="NotSupportd"
   @ejb.ejb-ref ejb-name="Schedule" view-type="remote" ref-name="Schedule"
   @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class ScheduleCRUDBean implements SessionBean
{
	/** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{ 
			//m_scheduleHome = ScheduleUtil.getLocalHome();
                        m_scheduleHome = ScheduleUtil.getHome();
		}
		catch (Throwable ex)
		{
			sLog.error("Couldn't initialize ScheduleCRUDBean", ex);
			throw new CreateException("Couldn't initialize ScheduleCRUDBean");
		}
	}

	/** @ejb.interface-method */
	public Object test(Object something) throws RemoteException
	{
		sLog.info("test being performed, arg=" + something);
		return null;
	}

	/** @ejb.interface-method */
	public ScheduleData retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		return m_scheduleHome.findByPrimaryKey(new Integer(id)).getData();
	}

	/** @ejb.interface-method */
	public Collection retrieveOutstandingSchedules(long time) throws RemoteException, FinderException
	{
		Map map = new TreeMap();	// keys will sort by start date (string), because it's LMS (Left Most Significant)
		for (Iterator i = m_scheduleHome.findScheduleRange(time).iterator(); i.hasNext(); )
		{
			//ScheduleData data = ((ScheduleLocal)i.next()).getData();
                        ScheduleData data = ((Schedule)i.next()).getData();
			map.put(new Long(data.getStartDate()), data);
		}
		return map.values();
	}

	/** @ejb.interface-method */
	public void setSchedule(ScheduleData data) throws RemoteException, FinderException
	{
		m_scheduleHome.findByPrimaryKey(data.getId()).setData(data);
	}

	/** @ejb.interface-method */
	public Collection listSchedules() throws FinderException, RemoteException
	{
		List list = new ArrayList();
		for (Iterator i = m_scheduleHome.findAll().iterator(); i.hasNext(); )
		{
			//ScheduleLocal schedule = (ScheduleLocal)i.next();
                        Schedule schedule = (Schedule)i.next();
			list.add(schedule.getData());
		}
		return list;
	}

	//private ScheduleLocalHome m_scheduleHome;
        private ScheduleHome m_scheduleHome;


	private static Logger sLog = Logger.getLogger(ScheduleCRUDBean.class);
}

