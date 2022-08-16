package com.perpetual.viewer.model.ejb;

/**
 * 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.rmi.*;
import javax.ejb.*;

import com.perpetual.util.ejb.KeyGeneratorUtility;


/*
	comment-out section, with at signs removed, because xjavadoc reads the comments
	ejb.finder signature="com.perpetual.logreport.model.LogSchedule findByName(java.lang.String name)"
		query="SELECT OBJECT(o) FROM LogSchedule o where o.name = ?1"
*/
/**
	@ejb.bean name="SummarySchedule" view-type="remote" type="CMP" primkey-field="id" cmp-version="2.x"
	@ejb.finder signature="Collection findAll()"
        @ejb.transaction="NotSupported"
        @jboss.container-configuration name="EntityBean2.x_IPP_Pool200"
 */
public abstract class SummaryScheduleBean implements EntityBean
{
	/** @ejb.create-method */
	public Integer ejbCreate(SummaryScheduleData data) throws CreateException
	{
		if (data.getId() == null) data.setId(new Integer(KeyGeneratorUtility.generateKey()));

		setId(data.getId());
		setData(data);

		return null;
	}

	/** @ejb.interface-method */
	public abstract void setData(SummaryScheduleData data);

	/** @ejb.interface-method */
	public abstract SummaryScheduleData getData();

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
	public abstract String getName();
	/** @ejb.interface-method */
	public abstract void setName(String name);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getScheduleId();
	/** @ejb.interface-method */
	public abstract void setScheduleId(int id);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract String getHosts();
	/** @ejb.interface-method */
	public abstract void setHosts(String hosts);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getSeverities();
	/** @ejb.interface-method */
	public abstract void setSeverities(int severities);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getFacilities();
	/** @ejb.interface-method */
	public abstract void setFacilities(int facilities);
        /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract int getMessagepatterns();
	/** @ejb.interface-method */
	public abstract void setMessagepatterns(int facilities);    
        /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
        public abstract int getAndSevs();
	/** @ejb.interface-method */
	public abstract void setAndSevs(int facilities);
        /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
        public abstract int getAndFacs();
	/** @ejb.interface-method */
	public abstract void setAndFacs(int facilities);
        /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
        public abstract int getFacsOrSevs();
	/** @ejb.interface-method */
	public abstract void setFacsOrSevs(int facilities);
        /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
         public abstract int getFacsOrMessages();
	/** @ejb.interface-method */
	public abstract void setFacsOrMessages(int facilities);
          /**
	  @ejb.persistence
	  @ejb.interface-method
	 */
         public abstract int getSevsOrMessages();
	/** @ejb.interface-method */
	public abstract void setSevsOrMessages(int facilities);
       
}



