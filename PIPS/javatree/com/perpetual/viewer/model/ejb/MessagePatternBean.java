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


/**
	@ejb.bean name="MessagePattern" view-type="remote" type="CMP" primkey-field="id" cmp-version="2.x"
	@ejb.finder signature="Collection findAll()"
        @ejb.transaction="NotSupported"
        @jboss.container-configuration name="EntityBean2.x_IPP_Pool200"
 */
public abstract class MessagePatternBean implements EntityBean
{
	/** @ejb.create-method */
	public Integer ejbCreate(MessagePatternData data) throws CreateException
	{
		if (data.getId() == null) data.setId(new Integer(KeyGeneratorUtility.generateKey()));

		setId(data.getId());
		setData(data);

		return null;
	}

	/** @ejb.interface-method */
	public abstract void setData(MessagePatternData data);

	/** @ejb.interface-method */
	public abstract MessagePatternData getData();

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
	public abstract String getPattern();
	/** @ejb.interface-method */
	public abstract void setPattern(String pattern);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract String getDescription();
	/** @ejb.interface-method */
	public abstract void setDescription(String description);
}



