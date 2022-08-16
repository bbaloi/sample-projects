package com.perpetual.logreport.model;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.rmi.*;
import javax.ejb.*;

import com.perpetual.util.ejb.KeyGeneratorUtility;


/**
	@ejb.bean name="Schedule" view-type="remote" type="CMP" primkey-field="id" cmp-version="2.x"
	@ejb.finder signature="Collection findAll()"
	@ejb.finder signature="Collection findScheduleRange(long date)"
		query="SELECT OBJECT(o) FROM Schedule o where o.whenDate <= ?1 AND o.enabled <> 0"
        @ejb.transaction="NotSupported"
        @jboss.container-configuration name="EntityBean2.x_IPP_Pool200"        
 */
public abstract class ScheduleBean implements EntityBean
{
	/** @ejb.create-method */
	public Integer ejbCreate(ScheduleData data) throws CreateException
	{
System.out.println("!! " + data);
		if (data.getId() == null) data.setId(new Integer(KeyGeneratorUtility.generateKey()));

		setId(data.getId());
		setData(data);

		return null;
	}

	/** @ejb.interface-method */
	public abstract void setData(ScheduleData data);

	/** @ejb.interface-method */
	public abstract ScheduleData getData();

	/**
	  @ejb.pk-field
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract Integer getId();
	/** @ejb.interface-method */
	public abstract void setId(Integer id);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract String getTimeZone();
	/** @ejb.interface-method */
	public abstract void setTimeZone(String timeZone);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract long getStartDate();
	/** @ejb.interface-method */
	public abstract void setStartDate(long startDate);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract long getEndDate();
	/** @ejb.interface-method */
	public abstract void setEndDate(long endDate);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract long getWhenDate();
	/** @ejb.interface-method */
	public abstract void setWhenDate(long whenDate);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getRepeat();
	/** @ejb.interface-method */
	public abstract void setRepeat(int repeat);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract String getIncDate();
	/** @ejb.interface-method */
	public abstract void setIncDate(String incDate);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract String getSessionBeanName();
	/** @ejb.interface-method */
	public abstract void setSessionBeanName(String sessionBeanName);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getReferenceId();
	/** @ejb.interface-method */
	public abstract void setReferenceId(int id);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getEnabled();
	/** @ejb.interface-method */
	public abstract void setEnabled(int enabled);
}


